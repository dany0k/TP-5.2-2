from re import template
from django.db.models.fields import return_None
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import permissions

from .models import *
from .serializers import * 
from .services import generate_report


class OperationCategoryViewSet(viewsets.ModelViewSet):
    queryset = OperationCategory.objects.all()
    serializer_class = OperationCategorySerializer
    permission_classes = (permissions.IsAuthenticated,)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    def get_queryset(self):
        return OperationCategory.objects.filter(user=self.request.user)

    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить все операции пользователей определенной категории
        """
        category = OperationCategory.objects.get(pk=pk)
        queryset = Operation.objects.filter(category=category)
        serializer = OperationSerializer(queryset, many=True)
        return Response(serializer.data)


class OperationTemplateViewSet(viewsets.ModelViewSet):
    queryset = OperationTemplate.objects.all()
    serializer_class = OperationTemplateSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        return OperationTemplate.objects.filter(category__user=self.request.user)


class OperationViewSet(viewsets.ModelViewSet):
    queryset = Operation.objects.all()
    serializer_class = OperationSerializer
    permission_classes = (permissions.IsAuthenticated,)

    @action(detail=False, methods=['get'])
    def report(self, request):
        """
        Получить сгенерированный отчет по операциям
        """
        response = generate_report(self.request.user)
        return Response(response)

    @action(detail=False, methods=['get'])
    def incomes(self, requset):
        """
        Получить доходы зарегистрированного пользователя
        """
        incomes = Operation.objects.filter(account__user=self.request.user,
                                           op_variant='доход')
        serializer = OperationSerializer(incomes, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'])
    def consumptions(self, requset):
        """
        Получить расходы зарегистрированного пользователя
        """
        consumptions = Operation.objects.filter(account__user=self.request.user,
                                           op_variant='расход')
        serializer = OperationSerializer(consumptions, many=True)
        return Response(serializer.data)

class AccountViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить операции с определенного счета
        """
        acc = Account.objects.get(pk=pk)
        queryset = acc.operation_set.all()
        serializer = OperationSerializer(queryset, many=True)
        return Response(serializer.data)


class CreditPayViewSet(viewsets.ModelViewSet):
    queryset = CreditPay.objects.all()
    serializer_class = CreditPaySerializer
    permission_classes = (permissions.IsAuthenticated,)


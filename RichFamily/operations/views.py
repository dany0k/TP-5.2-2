from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response

from .models import *
from .serializers import * 


class OperationCategoryViewSet(viewsets.ModelViewSet):
    queryset = OperationCategory.objects.all()
    serializer_class = OperationCategorySerializer

    @action(detail=False, methods=['get'])
    def name(self, request):
        name = request.GET['name']
        queryset = OperationCategory.objects.get(cat_name=name)
        serializer = OperationCategorySerializer(queryset)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        category = OperationCategory.objects.get(pk=pk)
        queryset = Operation.objects.filter(category=category)
        serializer = OperationSerializer(queryset, many=True)
        return Response(serializer.data)


class OperationTemplateViewSet(viewsets.ModelViewSet):
    queryset = OperationTemplate.objects.all()
    serializer_class = OperationTemplateSerializer


class OperationViewSet(viewsets.ModelViewSet):
    queryset = Operation.objects.all()
    serializer_class = OperationSerializer


class AccountViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer
    # Когда будет добавлена авторизация
    # def perform_create(self, serializer)
    #   serializer.save(user=self.request.user)

    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        acc = Account.objects.get(pk=pk)
        queryset = acc.operation_set.all()
        serializer = OperationSerializer(queryset, many=True)
        return Response(serializer.data)


class CreditPayViewSet(viewsets.ModelViewSet):
    queryset = CreditPay.objects.all()
    serializer_class = CreditPaySerializer


from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import permissions
from django.forms.utils import json

from .models import *
from .serializers import * 
from .services import generate_report, save_report, calc_payment


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


def _change_account(serializer):
        """
        Изменить состояние счета при создании или изменении операции
        """
        account = Account.objects.get(id=serializer.validated_data['account'].id)
        if serializer.validated_data['op_variant'] == 'ДОХОД':
            account.acc_sum += serializer.validated_data['op_sum']
        else:
            account.acc_sum -= serializer.validated_data['op_sum']
        account.save()


def _rollback_account(instance):
    """
    Откатить счет до состояния предыдущей операции
    """
    prev_sum = instance.op_sum
    acc = instance.account
    if instance.op_variant == 'ДОХОД':
        acc.acc_sum -= prev_sum
    else:
        acc.acc_sum += prev_sum
    acc.save()


class OperationViewSet(viewsets.ModelViewSet):
    queryset = Operation.objects.all()
    serializer_class = OperationSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def perform_create(self, serializer: OperationSerializer):
        _change_account(serializer)
        serializer.save()

    def perform_update(self, serializer: OperationSerializer):
        _rollback_account(serializer.instance) 
        _change_account(serializer)
        serializer.save()

    def perform_destroy(self, instance):
        _rollback_account(instance)
        instance.delete()

    @action(detail=False, methods=['get'])
    def report(self, request):
        """
        Получить сгенерированный отчет по операциям
        """
        response = generate_report(self.request.user)
        return Response(response)

    @action(detail=False, methods=['post'])
    def save_report(self, request):
        """
        Сохранить сгенерированный пользователем отчет
        """
        report = generate_report(self.request.user)
        save_report(report)
        return Response({'success': True})

    @action(detail=False, methods=['get'])
    def incomes(self, requset):
        """
        Получить доходы зарегистрированного пользователя
        """
        incomes = Operation.objects.filter(account__user=self.request.user,
                                           op_variant='ДОХОД')
        serializer = OperationSerializer(incomes, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'])
    def consumptions(self, requset):
        """
        Получить расходы зарегистрированного пользователя
        """
        consumptions = Operation.objects.filter(account__user=self.request.user,
                                           op_variant='РАСХОД')
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

    def perform_create(self, serializer):
        payment = calc_payment(serializer.validated_data['cr_all_sum'],
                                serializer.validated_data['cr_percent'],
                                serializer.validated_data['cr_period'])
        all_sum = payment * serializer.validated_data['cr_period']
        percents_sum = all_sum - serializer.validated_data['cr_all_sum']
        serializer.save(user=self.request.user,
                        cr_month_pay=payment,
                        cr_percents_sum=percents_sum,
                        cr_sum_plus_percents=all_sum)

    @action(methods=['post'], detail=False)
    def calc_credit(self, request):
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        payment = calc_payment(body_data['cr_all_sum'],
                                body_data['cr_percent'],
                                body_data['cr_period'])
        all_sum = payment * body_data['cr_period']
        percents_sum = all_sum - body_data['cr_all_sum']
        return Response({'cr_name': body_data['cr_name'], 'cr_month_pay': payment, 'cr_percents_sum': percents_sum, 'cr_sum_plus_percents': all_sum})

     

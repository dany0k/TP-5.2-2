from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import permissions
from django.forms.utils import json


from .models import *
from .serializers import * 
from .services import change_account, generate_report, get_operations_by_account, get_operations_by_category, rollback_account, save_report, calc_payment, open_report
from django.http.response import FileResponse


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
        return Response(get_operations_by_category(pk))


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

    def get_queryset(self):
        return Operation.objects.filter(category__user=self.request.user)

    def perform_create(self, serializer: OperationSerializer):
        change_account(serializer)
        serializer.save()

    def perform_update(self, serializer: OperationSerializer):
        rollback_account(serializer.instance) 
        change_account(serializer)
        serializer.save()

    def perform_destroy(self, instance):
        rollback_account(instance)
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
    def send_report(self, request):
        """
        Отправить сгенерированный отчет пользователю
        В случае ошибки: отправляется 503 код (сервис недоступен) и сообщение об ошибке message
        """
        try:
           file = open_report()
           return FileResponse(file, content_type='text/csv')
        except Exception as e:
           return Response({'message': e}, status=503)


class AccountViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        return Account.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить операции с определенного счета
        """
        return Response(get_operations_by_account(pk))


class CreditPayViewSet(viewsets.ModelViewSet):
    queryset = CreditPay.objects.all()
    serializer_class = CreditPaySerializer

    def get_queryset(self):
        return CreditPay.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        payment = calc_payment(serializer.validated_data['cr_all_sum'],
                                serializer.validated_data['cr_percent'],
                                serializer.validated_data['cr_period'],
                                serializer.validated_data['cr_first_pay'])
        all_sum = payment * serializer.validated_data['cr_period']
        percents_sum = all_sum - (serializer.validated_data['cr_all_sum'] - serializer.validated_data['cr_first_pay'])
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
                                body_data['cr_period'],
                                body_data['cr_first_pay'])
        all_sum = payment * body_data['cr_period']
        percents_sum = all_sum - (body_data['cr_all_sum'] - body_data['cr_first_pay'])
        return Response({'cr_name': body_data['cr_name'], 
                         'cr_month_pay': payment, 
                         'cr_percents_sum': percents_sum, 
                         'cr_sum_plus_percents': all_sum})

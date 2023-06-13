from drf_spectacular.utils import OpenApiResponse, extend_schema, extend_schema_view
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.exceptions import status
from rest_framework.response import Response
from rest_framework import permissions
from django.forms.utils import json


from .models import *
from .serializers import * 
from .services import change_account, generate_report, get_operations_by_account, get_operations_by_category, rollback_account, save_report, calc_payment, open_report
from django.http.response import FileResponse


@extend_schema_view(
    list=extend_schema(description="Получить все категории операций для авторизованного пользователя",  responses={
        status.HTTP_200_OK: OperationCategorySerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer}),
    create=extend_schema(description="Создать новую категорию операций для авторизованного пользователя", responses={
        status.HTTP_200_OK: OperationCategorySerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    update=extend_schema(description="Обновить существующую категорию для авторизованного пользователя", responses={
        status.HTTP_200_OK: OperationCategorySerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    retrieve=extend_schema(exclude=True),
    partial_update=extend_schema(exclude=True),
    destroy=extend_schema(description="Удалить категорию операций для авторизованного пользователя", responses={
        status.HTTP_204_NO_CONTENT: OpenApiResponse(
                response=None,
                description='No response body'), 
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer}),
)
class OperationCategoryViewSet(viewsets.ModelViewSet):
    queryset = OperationCategory.objects.all()
    serializer_class = OperationCategorySerializer
    permission_classes = (permissions.IsAuthenticated,)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    def perform_update(self, serializer):
        serializer.save(user=self.request.user)

    def get_queryset(self):
        return OperationCategory.objects.filter(user=self.request.user)

    @extend_schema(exclude=True, responses=OperationSerializer)
    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить все операции определенной категории
        """
        return Response(get_operations_by_category(pk))


@extend_schema_view(
    list=extend_schema(description="Получить все шаблоны создания операций для авторизованного пользователя",  responses={
        status.HTTP_200_OK: OperationTemplateSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer}),
    create=extend_schema(description="Создать новый шаблон операций для авторизованного пользователя", responses={
        status.HTTP_200_OK: OperationTemplateSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    update=extend_schema(description="Обновить существующий шаблон для авторизованного пользователя", responses={
        status.HTTP_200_OK: OperationTemplateSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    partial_update=extend_schema(exclude=True),
    retrieve=extend_schema(exclude=True),
    destroy=extend_schema(description="Удалить шаблон создания операций для авторизованного пользователя", responses={
        status.HTTP_204_NO_CONTENT: OpenApiResponse(
                response=None,
                description='No response body'), 
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer}),
)
class OperationTemplateViewSet(viewsets.ModelViewSet):
    queryset = OperationTemplate.objects.all()
    serializer_class = OperationTemplateSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        return OperationTemplate.objects.filter(category__user=self.request.user)


@extend_schema_view(
    list=extend_schema(description="Получить все финансовые операции для авторизованного пользователя",  responses={
        status.HTTP_200_OK: OperationSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer}),
    create=extend_schema(description="Создать новую финансовую операцию для авторизованного пользователя", responses={
        status.HTTP_200_OK: OperationSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    retrieve=extend_schema(description="Получить операцию по ее идентификатору id", responses={
        status.HTTP_200_OK: OperationSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer}),
    update=extend_schema(description="Обновить существующую операцию", responses={
        status.HTTP_200_OK: OperationSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    partial_update=extend_schema(exclude=True),
    destroy=extend_schema(description="Удалить существующую операцию", responses={
        status.HTTP_204_NO_CONTENT: OpenApiResponse(
                response=None,
                description='No response body'), 
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer}),
)
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
    
    @extend_schema(responses={
        status.HTTP_200_OK: OperationSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer})
    @action(detail=False, methods=['get'])
    def report(self, request):
        """
        Получить сгенерированный отчет по операциям
        """
        response = generate_report(self.request.user)
        return Response(response)

    @extend_schema(responses={
        status.HTTP_200_OK: SuccessSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer})
    @extend_schema(responses=SuccessSerializer)
    @action(detail=False, methods=['get'])
    def save_report(self, request):
        """
        Сохранить сгенерированный пользователем отчет
        """
        report = generate_report(self.request.user)
        save_report(report)
        return Response({'success': True})

    @extend_schema(responses={
        status.HTTP_200_OK: MessageSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_500_INTERNAL_SERVER_ERROR: MessageSerializer
        })
    @action(detail=False, methods=['get'])
    def send_report(self, request):
        """
        Отправить сгенерированный отчет пользователю
        """
        try:
           file = open_report()
           return FileResponse(file, content_type='text/csv')
        except Exception as e:
           return Response({'message': e}, status=503)


@extend_schema_view(
    list=extend_schema(description="Получить все счета для авторизованного пользователя",  responses={
        status.HTTP_200_OK: AccountSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer}),
    create=extend_schema(description="Создать новый счет для авторизованного пользователя", responses={
        status.HTTP_200_OK: AccountSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    retrieve=extend_schema(exclude=True),
    update=extend_schema(description="Обновить существующий счет для авторизованного пользователя", responses={
        status.HTTP_200_OK: AccountSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    partial_update=extend_schema(exclude=True),
    destroy=extend_schema(description="Удалить существующий счет для авторизованного пользователя", responses={
        status.HTTP_204_NO_CONTENT: OpenApiResponse(
                response=None,
                description='No response body'), 
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer}),
)
class AccountViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        return Account.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    def perform_update(self, serializer):
        serializer.save(user=self.request.user)

    @extend_schema(exclude=True, responses=OperationSerializer)
    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить финансовые операции с определенного счета 
        """
        return Response(get_operations_by_account(pk))


@extend_schema_view(
    list=extend_schema(description="Получить все кредитные платежи для авторизованного пользователя",  responses={
        status.HTTP_200_OK: CreditPaySerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer}),
    retrieve=extend_schema(exclude=True),
    create=extend_schema(description="Рассчитать новый кредитный платеж для авторизованного пользователя", responses={
        status.HTTP_200_OK: CreditPaySerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer}),
    update=extend_schema(exclude=True),
    partial_update=extend_schema(exclude=True),
    destroy=extend_schema(description="Удалить рассчитанный кредитный платеж", responses={
        status.HTTP_204_NO_CONTENT: OpenApiResponse(
                response=None,
                description='No response body'), 
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer}),
)
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

    @extend_schema(request=CreditPayNonAuthorizedRequestSerializer, responses={
        status.HTTP_200_OK: CreditPayNonAuthorizedResponseSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer})
    @action(methods=['post'], detail=False)
    def calc_credit(self, request):
        """
        Расчет кредитного платежа для неавторизованного пользователя
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        payment = calc_payment(body_data['cr_all_sum'],
                                body_data['cr_percent'],
                                body_data['cr_period'],
                                body_data['cr_first_pay'])
        all_sum = payment * body_data['cr_period']
        percents_sum = all_sum - (body_data['cr_all_sum'] - body_data['cr_first_pay'])
        return Response({'cr_name': body_data['cr_name'], 
                         'cr_all_sum': body_data['cr_all_sum'],
                         'cr_first_pay': body_data['cr_first_pay'],
                         'cr_percent': body_data['cr_percent'],
                         'cr_period': body_data['cr_period'],
                         'cr_month_pay': payment, 
                         'cr_percents_sum': percents_sum, 
                         'cr_sum_plus_percents': all_sum})

from rest_framework import serializers
from .models import *


class OperationCategorySerializer(serializers.ModelSerializer):
    """ Сериализатор для категории операции """
    class Meta:
        model = OperationCategory
        fields = ('id', 'user', 'cat_name',)


class OperationTemplateSerializer(serializers.ModelSerializer):
    """ Сериализатор для шаблона операции """
    class Meta:
        model = OperationTemplate
        fields = ('id', 'category', 'account', 'temp_name', 'temp_variant', 'temp_recipient', 'temp_sum', 'temp_comment')


class AccountSerializer(serializers.ModelSerializer):
    """ Сериализатор для счета пользователя """
    class Meta:
        model = Account
        fields = ('id', 'user', 'acc_name', 'acc_sum', 'acc_currency' ,'acc_comment')


class OperationSerializer(serializers.ModelSerializer):
    """ Сериализатор для финансовой операции """
    class Meta:
        model = Operation
        fields = ('id', 'account', 'category', 'op_variant', 'op_date', 'op_recipient', 'op_sum', 'op_comment')


class CreditPaySerializer(serializers.ModelSerializer):
    """ Сериализатор для кредитного платежа """
    class Meta:
        model = CreditPay
        fields = ('id', 'user', 'cr_name', 'cr_all_sum', 'cr_first_pay','cr_percent', 'cr_period' , 'cr_month_pay', 'cr_percents_sum' ,'cr_sum_plus_percents')


class CreditPayNonAuthorizedRequestSerializer(serializers.Serializer):
    """ Сериализатор для запроса расчета кредитного платежа неавторизованным пользователем """
    cr_name = serializers.CharField(max_length=255)
    cr_all_sum = serializers.FloatField()
    cr_first_pay = serializers.FloatField()
    cr_percent = serializers.IntegerField()
    cr_period = serializers.IntegerField()


class CreditPayNonAuthorizedResponseSerializer(serializers.Serializer):
    """ Сериализатор для результата расчета кредитного платежа неавторизованным пользователем """
    cr_name = serializers.CharField(max_length=255)
    cr_all_sum = serializers.FloatField()
    cr_first_pay = serializers.FloatField()
    cr_percent = serializers.IntegerField()
    cr_period = serializers.IntegerField()
    cr_month_pay = serializers.FloatField()
    cr_percents_sum = serializers.FloatField()
    cr_sum_plus_percents = serializers.FloatField()


class SuccessSerializer(serializers.Serializer):
    success = serializers.BooleanField()


class MessageSerializer(serializers.Serializer):
    message = serializers.CharField(max_length=255)


class DetailSerializer(serializers.Serializer):
    detail = serializers.CharField(max_length=255)


class BadRequestErrorSerializer(serializers.Serializer):
    field = serializers.ListField(child=serializers.CharField())

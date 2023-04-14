from rest_framework import serializers
from .models import *


class OperationCategorySerializer(serializers.ModelSerializer):
    """ Сериализатор для категории операции """
    class Meta:
        model = OperationCategory
        fields = ('id', 'cat_name',)


class OperationTemplateSerializer(serializers.ModelSerializer):
    """ Сериализатор для шаблона операции """
    class Meta:
        model = OperationTemplate
        fields = ('id', 'category', 'temp_name', 'temp_variant', 'temp_recipient', 'temp_sum')


class AccountSerializer(serializers.ModelSerializer):
    """ Сериализатор для счета пользователя """
    class Meta:
        models = Account
        fields = ('id', 'user', 'acc_sum', 'acc_currency' ,'acc_comment ')


class OperationSerializer(serializers.ModelSerializer):
    """ Сериализатор для финансовой операции """
    class Meta:
        model = Operation
        fields = ('id', 'account', 'category', 'op_variant', 'op_date', 'op_recipient', 'op_sum', 'op_comment')


class CreditPaySerializer(serializers.ModelSerializer):
    """ Сериализатор для кредитного платежа """
    class Meta:
        model = CreditPay
        fields = ('id', 'user', 'cr_all_sum', 'cr_first_pay', 'cr_percent', 'cr_period' , 'cr_month_pay', 'cr_percents_sum' ,'cr_sum_plus_percents')



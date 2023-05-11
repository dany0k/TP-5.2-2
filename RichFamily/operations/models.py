from django.db import models
from django.contrib.auth.models import User

class Account(models.Model):
    """ Модель счета пользователя """
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    acc_name = models.CharField(max_length=20, default='Счет')
    acc_sum = models.FloatField()
    acc_currency = models.CharField(max_length=10)
    acc_comment = models.TextField()

    class Meta:
        db_table = 'accounts'


class OperationCategory(models.Model):
    """ Модель категории операции """
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    cat_name = models.CharField(max_length=30)

    class Meta:
        db_table = 'categories'


class OperationTemplate(models.Model):
    """ Модель шаблона операции """
    category = models.ForeignKey(OperationCategory, on_delete=models.CASCADE)
    temp_name = models.CharField(max_length=40)
    temp_variant = models.CharField(max_length=10)
    temp_recipient = models.CharField(max_length=100)
    temp_sum = models.FloatField()

    class Meta:
        db_table = 'templates'


class Operation(models.Model):
    """ Модель финансовой операции (доход или расход) """
    account = models.ForeignKey(Account, on_delete=models.CASCADE)
    category = models.ForeignKey(OperationCategory, on_delete=models.CASCADE)
    op_variant = models.CharField(max_length=10)
    op_date = models.DateTimeField()
    op_recipient = models.CharField(max_length=100)
    op_sum = models.FloatField()
    op_comment = models.TextField() 

    class Meta:
        db_table = 'operations'


class CreditPay(models.Model):
    """ Модель кредита и кредитного платежа """
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    cr_name = models.CharField(max_length=255, default='Кредит')
    cr_all_sum = models.FloatField()
    cr_percent = models.IntegerField()
    cr_period = models.IntegerField()
    cr_month_pay = models.FloatField(null=True)
    cr_percents_sum = models.FloatField(null=True)
    cr_sum_plus_percents = models.FloatField(null=True)

    class Meta:
        db_table = 'credits'


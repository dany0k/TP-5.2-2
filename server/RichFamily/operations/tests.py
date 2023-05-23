from datetime import datetime
from django.test import TestCase
from django.contrib.auth.models import User

from .models import Account, OperationCategory, Operation, OperationTemplate, CreditPay


class OperationsTests(TestCase):
    def setUp(self):
        self.user_account = User.objects.create_user("user1", None, "pass1")
        self.account = Account.objects.create(user=self.user_account,
                                         acc_name="acc1", acc_sum=4660.0, acc_currency="RUB", acc_comment="")
        self.category = OperationCategory.objects.create(user=self.user_account, cat_name="cat1")

    def test_operation_create(self):
        operation = Operation.objects.create(account=self.account, category=self.category,
                                             op_variant="РАСХОД", op_date=datetime.now(), op_recipient="user",
                                             op_sum=60.0, op_comment="")
        self.assertIsNotNone(operation)
        self.assertEquals(operation.account, self.account)
        self.assertEquals(operation.category, self.category)
        self.assertEquals(operation.op_variant, "РАСХОД")
        self.assertEquals(operation.op_recipient, "user")
        self.assertEquals(operation.op_sum, 60.0)
        self.assertEquals(operation.op_comment, "")

    def test_operation_update(self):
        operation = Operation.objects.create(account=self.account, category=self.category,
                                             op_variant="РАСХОД", op_date=datetime.now(), op_recipient="user",
                                             op_sum=60.0, op_comment="")
        self.assertIsNotNone(operation)
        self.assertEquals(operation.op_sum, 60.0)
        operation.op_sum = 120.0
        operation.save()
        operation2 = Operation.objects.get(op_sum=120.0)
        self.assertIsNotNone(operation2)

    def test_operation_delete(self):
        operation = Operation.objects.create(account=self.account, category=self.category,
                                             op_variant="РАСХОД", op_date=datetime.now(), op_recipient="user",
                                             op_sum=60.0, op_comment="")
        self.assertIsNotNone(operation)
        operation.delete()
        with self.assertRaises(Exception):
            operation2 = Operation.objects.get(op_sum=60.0)

    def test_operation_template_create(self):
        operationTemplate = OperationTemplate.objects.create(category=self.category, account=self.account,
                                                             temp_name="name", temp_recipient="user", temp_sum=60.0,
                                                             temp_comment="")
        self.assertIsNotNone(operationTemplate)
        self.assertEquals(operationTemplate.account, self.account)
        self.assertEquals(operationTemplate.category, self.category)
        self.assertEquals(operationTemplate.temp_name, "name")
        self.assertEquals(operationTemplate.temp_recipient, "user")
        self.assertEquals(operationTemplate.temp_sum, 60.0)
        self.assertEquals(operationTemplate.temp_comment, "")

    def test_operation_template_update(self):
        operationTemplate = OperationTemplate.objects.create(category=self.category, account=self.account,
                                                             temp_name="name", temp_recipient="user", temp_sum=60.0,
                                                             temp_comment="")
        self.assertIsNotNone(operationTemplate)
        self.assertEquals(operationTemplate.temp_sum, 60.0)
        operationTemplate.temp_sum = 120.0
        operationTemplate.save()
        operationTemplate2 = OperationTemplate.objects.get(temp_sum=120.0)
        self.assertIsNotNone(operationTemplate2)

    def test_operation_template_delete(self):
        operationTemplate = OperationTemplate.objects.create(category=self.category, account=self.account,
                                                             temp_name="name", temp_recipient="user", temp_sum=60.0,
                                                             temp_comment="")
        self.assertIsNotNone(operationTemplate)
        operationTemplate.delete()
        with self.assertRaises(Exception):
            operationTemplate2 = OperationTemplate.objects.get(temp_sum=60.0)

    def test_operation_category_create(self):
        operationCategory = OperationCategory.objects.create(user=self.user_account, cat_name="cat_name")
        self.assertIsNotNone(operationCategory)
        self.assertEquals(operationCategory.user, self.user_account)
        self.assertEquals(operationCategory.cat_name, "cat_name")

    def test_operation_category_update(self):
        operationCategory = OperationCategory.objects.create(user=self.user_account, cat_name="cat_name")
        self.assertIsNotNone(operationCategory)
        self.assertEquals(operationCategory.cat_name, "cat_name")
        operationCategory.cat_name = "new_cat"
        operationCategory.save()
        operationCategory2 = OperationCategory.objects.get(cat_name="new_cat")
        self.assertIsNotNone(operationCategory2)

    def test_operation_category_delete(self):
        operationCategory = OperationCategory.objects.create(user=self.user_account, cat_name="cat_name")
        self.assertIsNotNone(operationCategory)
        operationCategory.delete()
        with self.assertRaises(Exception):
            operationCategory2 = OperationCategory.objects.get(cat_name="cat_name")

    def test_account_create(self):
        account = Account.objects.create(user=self.user_account, acc_name="acc_name",
                                         acc_sum=4660.0, acc_currency="RUB", acc_comment="")
        self.assertIsNotNone(account)
        self.assertEquals(account.user, self.user_account)
        self.assertEquals(account.acc_name, "acc_name")
        self.assertEquals(account.acc_sum, 4660.0)
        self.assertEquals(account.acc_currency, "RUB")
        self.assertEquals(account.acc_comment, "")

    def test_account_update(self):
        account = Account.objects.create(user=self.user_account, acc_name="acc_name",
                                         acc_sum=4660.0, acc_currency="RUB", acc_comment="")
        self.assertIsNotNone(account)
        self.assertEquals(account.acc_name, "acc_name")
        account.acc_name = "new_acc"
        account.save()
        account2 = Account.objects.get(acc_name="new_acc")
        self.assertIsNotNone(account2)

    def test_account_delete(self):
        account = Account.objects.create(user=self.user_account, acc_name="acc_name",
                                         acc_sum=4660.0, acc_currency="RUB", acc_comment="")
        self.assertIsNotNone(account)
        account.delete()
        with self.assertRaises(Exception):
            account2 = Account.objects.get(acc_name="acc_name")

    def test_credit_pay_create(self):
        creditPay = CreditPay.objects.create(user=self.user_account, cr_name="cr_name",
                                             cr_all_sum=4660.0, cr_percent=10, cr_period=12,
                                             cr_month_pay=427.17, cr_sum_plus_percents=4912.42)
        self.assertIsNotNone(creditPay)
        self.assertEquals(creditPay.user, self.user_account)
        self.assertEquals(creditPay.cr_name, "cr_name")
        self.assertEquals(creditPay.cr_all_sum, 4660.0)
        self.assertEquals(creditPay.cr_percent, 10)
        self.assertEquals(creditPay.cr_period, 12)
        self.assertEquals(creditPay.cr_month_pay, 427.17)
        self.assertEquals(creditPay.cr_sum_plus_percents, 4912.42)

    def test_credit_pay_update(self):
        creditPay = CreditPay.objects.create(user=self.user_account, cr_name="cr_name",
                                             cr_all_sum=4660.0, cr_percent=10, cr_period=12,
                                             cr_month_pay=427.17, cr_sum_plus_percents=4912.42)
        self.assertIsNotNone(creditPay)
        self.assertEquals(creditPay.cr_name, "cr_name")
        creditPay.cr_name = "new_cr"
        creditPay.save()
        creditPay2 = CreditPay.objects.get(cr_name="new_cr")
        self.assertIsNotNone(creditPay2)

    def test_credit_pay_delete(self):
        creditPay = CreditPay.objects.create(user=self.user_account, cr_name="cr_name",
                                             cr_all_sum=4660.0, cr_percent=10, cr_period=12,
                                             cr_month_pay=427.17, cr_sum_plus_percents=4912.42)
        self.assertIsNotNone(creditPay)
        creditPay.delete()
        with self.assertRaises(Exception):
            creditPay2 = CreditPay.objects.get(cr_name="cr_name")

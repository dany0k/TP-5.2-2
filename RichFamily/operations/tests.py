from datetime import datetime
from django.test import TestCase
from django.contrib.auth.models import User

from .models import Account, OperationCategory, Operation

class OperationsTests(TestCase):
    def setUp(self):
        user_account = User.objects.create_user("user1", None, "pass1")
        self.account = Account.objects.create(user=user_account,
                                         acc_name="acc1", acc_sum=4660.0, acc_currency="RUB", acc_comment="")
        self.category = OperationCategory.objects.create(user=user_account, cat_name="cat1")

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
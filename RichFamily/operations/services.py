"""
Сервисы генерации и сохранения отчета по доходам и расходам пользователя 
"""
from .models import Account, Operation
from .serializers import OperationSerializer


def generate_report(authorizated_user):
    """ 
    Генерация отчета в JSON-формате:
    Результат: {"incomes": [Operation(1), ..], "expenses": [Operation(1), ..]}
    """
    accounts = Account.objects.filter(user=authorizated_user)
    incomes = list()
    expenses = list()
    for acc in accounts:
        incomes.append(OperationSerializer(Operation.objects.filter(account=acc, op_variant='доход'), many=True).data)
        expenses.append(OperationSerializer(Operation.objects.filter(account=acc, op_variant='расход'), many=True).data)
    result = {"incomes": incomes, "expenses": expenses}
    return result



"""
Сервисы генерации и сохранения отчета по доходам и расходам пользователя 
"""
import csv
from datetime import datetime
from .models import Account, Operation
from .serializers import OperationSerializer

_csv_filename = 'OperationsReport.csv'


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


def _convert_date_str(date: str) -> tuple:
    tokens = date.split('T')
    return (tokens[0], tokens[1].split('.')[0])


def save_report(report: dict):
    """
    Сохранение всего отчета в формат CSV с названием filename
    """
    with open(_csv_filename, 'w') as file:
        writer = csv.writer(file, delimiter=',')
        writer.writerow(['Доходы'])
        writer.writerow(['№', 
                        'Операция', 
                        'Дата', 
                        'Получатель/отправитель', 
                        'Сумма', 
                         'Комментарий'])
        for income_list in report['incomes']:
            for income in income_list:
                date = _convert_date_str(income['op_date'])
                writer.writerow([income['id'], 
                             income['op_variant'], 
                             f'{date[0]} {date[1]}', 
                             income['op_recipient'], 
                             income['op_sum'], 
                             income['op_comment']])
        writer.writerow([''])
        writer.writerow(['Расходы'])
        writer.writerow(['№', 
                        'Операция', 
                        'Дата', 
                        'Получатель/отправитель', 
                        'Сумма', 
                         'Комментарий'])
        for expence_list in report['expenses']:
            for expence in expence_list:
                date = _convert_date_str(expence['op_date'])
                writer.writerow([expence['id'], 
                             expence['op_variant'], 
                             f'{date[0]} {date[1]}', 
                             expence['op_recipient'], 
                             expence['op_sum'], 
                             expence['op_comment']])


def open_report():
    """
    Попытка открытия файла сгенерированного отчета
    """
    try:
        file = open(_csv_filename, 'rb')
        return file
    except:
        raise Exception('The report file can\'t be opened')


def calc_payment(ost, month_percent, month_count, first_pay) -> float:
    """
    Расчет ежемесячного платежа
    input:
        ost -> остаток по кредиту
        month_percent -> процент годовых
        month_count -> период выплаты кредита (в месяцах)
        first_pay -> первоначальный взнос
    """
    return (ost - first_pay) * (month_percent / (100 * 12)) / (1 - (1 + month_percent / (100 * 12))**(-month_count))

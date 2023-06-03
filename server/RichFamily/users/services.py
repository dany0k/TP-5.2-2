"""
Сервисы с бизнес-логикой для пользователей
"""
from typing import Dict
from django.contrib.auth.base_user import check_password

from django.contrib.auth.hashers import make_password
from django.contrib.auth.models import User

from operations.models import Account, OperationCategory


def create_user(body_data: Dict) -> User:
    """
    Создать нового пользователя и добавить ему начальные данные
    """
    user = User.objects.get(id=body_data['user_id'])
    user.first_name = body_data['first_name']
    user.last_name = body_data['last_name']
    user.appuserprofile.secret_word = make_password(body_data['secret_word'])
    user.save()
    OperationCategory.objects.create(user=user, cat_name='Продукты')
    OperationCategory.objects.create(user=user, cat_name='Транспорт')
    OperationCategory.objects.create(user=user, cat_name='Развлечения')
    Account.objects.create(user=user, 
                           acc_name='Первоначальный счет',
                           acc_sum=0.0,
                           acc_currency='RUB',
                           acc_comment='Стартовый счет пользователя')
    return user


def update_user(user_data: dict) -> User:
    """
    Обновить данные существующего пользователя
    """
    user = User.objects.get(id=user_data['id'])
    user.first_name = user_data['first_name']
    user.last_name = user_data['last_name']
    user.save()
    return user


def reset_user_password(user_data: dict) -> Dict:
    """
    Восстановить пароль пользователя по секретному слову
    """
    try:
        user: User = User.objects.get(username=user_data['email'])
    except:
        return {'body' : {'message': 'Такой пользователь на зарегистрирован'}, 'status': 403}
    if check_password(user_data['secret_word'], user.appuserprofile.secret_word):
        user.set_password(user_data['new_password'])
        user.save()
        return {'body' : {'message': 'Пароль был изменен успешно'}, 'status': 200}
    else:
        return {'body' : {'message': 'Введено неправильное секретное слово'}, 'status': 403}

"""
Сервисы бизнес-логики для взаимодействия с группами
"""
from django.contrib.auth.models import User
from django.core.exceptions import ObjectDoesNotExist
from django.db.models.expressions import NoneType

from operations.services import get_last_operation_for_user

from .serializers import GroupSerializer
from .models import Group
from users.models import GroupUser


def create_group(data, user: User):
    """
    Создать новую группу
    """
    group = Group.objects.create(gr_name = data['gr_name'])
    operation = get_last_operation_for_user(user)
    if operation.first() is None:
        last_id = 0
    else:
        last_id = operation.first().id
    GroupUser.objects.create(user=user, group=group, is_leader=True, last_operation_id=last_id)
    serializer = GroupSerializer(group)
    return serializer.data


def remove_user(user_id, group_id) -> None:
    """
    Исключить пользователя с логином username из группы с идентификатором id
    """
    group = Group.objects.get(id=group_id)
    user = User.objects.get(id=user_id)
    removed_user = GroupUser.objects.get(user=user, group=group)
    removed_user.delete()


def select_group_operations(operations, user, group_id): 
    """
    Отсортировать операции для просмотра в группе
    """
    group = Group.objects.get(id=group_id)
    group_user = GroupUser.objects.get(group=group, user=user)
    result = operations.filter(id__gt=group_user.last_operation_id)
    return result


def add_user(username, group_id) -> None:
    """
    Добавить пользователя под логином username в группу с идентификатором id
    """
    group = Group.objects.get(id=group_id)
    user = User.objects.get(username=username)
    operation = get_last_operation_for_user(user)
    if operation.first() is None:
        last_id = 0
    else:
        last_id = operation.first().id
    GroupUser.objects.create(group=group, user=user, last_operation_id=last_id)


def get_users(group_id) -> list:
    """
    Получить список пользователей группы с идентификатором id
    """
    group = Group.objects.get(id=group_id)
    group_queryset = GroupUser.objects.filter(group=group)
    user_queryset = User.objects.filter(id__in=group_queryset.values('user_id'))
    result = list()
    for user in user_queryset:
        group_user = group_queryset.get(user=user)         
        result.append({
            'id': user.id,
            'first_name': user.first_name,
            'last_name': user.last_name,
            'is_leader': group_user.is_leader
            })
    return result


def get_leader(group_id) -> User:
    """
    Получить лидера для группы с идентификатором group_id
    """
    group = Group.objects.get(id=group_id)
    group_user = GroupUser.objects.get(group=group, is_leader=True)
    return group_user.user


def destroy_group(group_id):
    """
    Освободить участников группы и удалить группу
    """
    group = Group.objects.get(id=group_id)
    user_list = get_users(group_id) 
    for user in user_list:
        remove_user(user['id'], group_id)
    group.delete()

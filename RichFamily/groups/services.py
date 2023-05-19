"""
Сервисы бизнес-логики для взаимодействия с группами
"""
from django.contrib.auth.models import User

from .serializers import GroupSerializer
from .models import Group
from users.models import GroupUser


def create_group(data, user: User):
    """
    Создать новую группу
    """
    group = Group.objects.create(gr_name = data['gr_name'])
    GroupUser.objects.create(user=user, group=group, is_leader=True)
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


def add_user(username, group_id) -> None:
    """
    Добавить пользователя под логином username в группу с идентификатором id
    """
    group = Group.objects.get(id=group_id)
    user = User.objects.get(username=username)
    GroupUser.objects.create(group=group, user=user)


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

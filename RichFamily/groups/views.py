from django.contrib.auth.models import User
from django.forms.utils import json
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import permissions

from .models import Group
from .serializers import GroupSerializer
from users.serializers import AppUserProfileSerializer
from users.models import GroupUser, AppUserProfile


class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer
    permission_classes = (permissions.IsAuthenticated,)

    # При создании новой группы создается запись в таблице лидеров групп
    def create(self, request, *args, **kwargs):
        """ 
        Создать новую группу 
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        group = Group.objects.create(gr_name = body_data['gr_name'])
        GroupUser.objects.create(user=self.request.user, group=group, is_leader=True)
        serializer = GroupSerializer(group)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def users(self, request, pk=None):
        """
        Получить список пользователей группы с идентификатором id
        """
        group = Group.objects.get(id=pk)
        queryset = GroupUser.objects.filter(group=group).user_set.all()
        serializer = AppUserProfileSerializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['put'])
    def add_user(self, request, pk=None):
        """
        Добавить пользователя в группу с идентификатором id
        В теле запроса указывается строка "user_id" : "идентификатор пользователя"
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        group = Group.objects.get(id=pk)
        user_id = body_data['user_id']
        user = User.objects.get(id=user_id)
        GroupUser.objects.create(group=group, user=user)
        return Response({'success': 'true'})

    @action(detail=True, methods=['put'])
    def remove_user(self, request, pk=None):
        """
        Исключить пользователя из группы с идентификатором id
        В теле запроса указывается строка "user_id" : "идентификатор пользователя"
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        group = Group.objects.get(id=pk)
        user = User.objects.get(id=body_data['user_id'])
        removed_user = GroupUser.objects.get(user=user, group=group)
        removed_user.delete()
        return Response({'success': 'true'})





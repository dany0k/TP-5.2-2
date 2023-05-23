from django.forms.utils import json
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import permissions

from groups.services import create_group, destroy_group, get_leader, remove_user, add_user, get_users
from .models import Group
from .serializers import GroupSerializer


class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer
    permission_classes = (permissions.IsAuthenticated,)

    def create(self, request, *args, **kwargs):
        """ 
        Создать новую группу 
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        return Response(create_group(body_data, self.request.user))
    
    def destroy(self, request, pk=None):
        if self.request.user == get_leader(pk):
            destroy_group(pk)
            return Response(status=204)
        else:
            return Response({'message': 'Вы не являетесь лидером группы'}, status=403)

    @action(detail=True, methods=['get'])
    def users(self, request, pk=None):
        """
        Получить список пользователей группы с идентификатором id
        """ 
        result = get_users(pk)
        return Response(result)

    @action(detail=True, methods=['get'])
    def is_leader(self, request, pk=None):
        """
        Проверить, является ли авторизованный пользователь лидером группы
        """
        leader = get_leader(pk)
        return Response({'is_leader': leader == self.request.user})

    @action(detail=True, methods=['post'])
    def add_user(self, request, pk=None):
        """
        Добавить пользователя в группу с идентификатором id
        В теле запроса указывается строка "username" : "email пользователя"
        """
        if self.request.user == get_leader(pk):
            body_unicode = request.body.decode('utf-8')
            body_data = json.loads(body_unicode)
            add_user(body_data['username'], pk)
            return Response({'success': 'true'})
        else:
            return Response({'message': 'Вы не являетесь лидером группы'}, status=403)

    @action(detail=True, methods=['post'])
    def remove_user(self, request, pk=None):
        """
        Исключить пользователя из группы с идентификатором id
        В теле запроса указывается строка "user_id" : "id пользователя"
        """
        if self.request.user == get_leader(pk):
            body_unicode = request.body.decode('utf-8')
            body_data = json.loads(body_unicode)
            remove_user(body_data['user_id'], pk)
            return Response({'success': 'true'})
        else:
            return Response({'message': 'Вы не являетесь лидером группы'}, status=403)


    @action(detail=True, methods=['post'])
    def exit_from_group(self, request, pk=None):
        """
        Выйти зарегистрированному пользователю из группы с номером id
        """
        remove_user(self.request.user.id, pk)
        return Response({'success': 'true'})




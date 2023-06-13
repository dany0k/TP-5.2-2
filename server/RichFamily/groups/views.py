from django.forms.utils import json
from drf_spectacular.utils import OpenApiResponse, extend_schema, extend_schema_view
from rest_framework import status, viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import permissions

from groups.services import create_group, destroy_group, get_leader, remove_user, add_user, get_users
from operations.serializers import BadRequestErrorSerializer, DetailSerializer
from users.serializers import UserSerializer
from .models import Group
from .serializers import GroupAddUserSerializer, GroupRemoveUserSerializer, GroupSerializer, IsLeaderSerializer, MessageSerializer


@extend_schema_view(
    list=extend_schema(exclude=True),
    retrieve=extend_schema(exclude=True),
    update=extend_schema(exclude=True),
    partial_update=extend_schema(exclude=True),
)
class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer
    permission_classes = (permissions.IsAuthenticated,)
    
    @extend_schema(responses={
        status.HTTP_200_OK: GroupSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer})
    def create(self, request, *args, **kwargs):
        """ 
        Создать новую группу 
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        return Response(create_group(body_data, self.request.user))
    
    @extend_schema(responses={
        status.HTTP_204_NO_CONTENT: OpenApiResponse(
                response=None,
                description='No response body'), 
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer})
    def destroy(self, request, pk=None):
        """
        Исключить всех пользователей из группы с номером id и удалить группу
        """
        if self.request.user == get_leader(pk):
            destroy_group(pk)
            return Response(status=204)
        else:
            return Response({'message': 'Вы не являетесь лидером группы'}, status=403)

    @extend_schema(responses={
        status.HTTP_200_OK: UserSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer})
    @action(detail=True, methods=['get'])
    def users(self, request, pk=None):
        """
        Получить список пользователей группы с идентификатором id
        """ 
        result = get_users(pk)
        return Response(result)

    @extend_schema(responses={
        status.HTTP_200_OK: IsLeaderSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer})
    @action(detail=True, methods=['get'])
    def is_leader(self, request, pk=None):
        """
        Проверить, является ли авторизованный пользователь лидером группы
        """
        leader = get_leader(pk)
        return Response({'is_leader': leader == self.request.user})

    @extend_schema(request=GroupAddUserSerializer, responses={
        status.HTTP_200_OK: MessageSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_403_FORBIDDEN: MessageSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer})
    @action(detail=True, methods=['post'])
    def add_user(self, request, pk=None):
        """
        Добавить пользователя в группу с идентификатором id
        """
        if self.request.user == get_leader(pk):
            body_unicode = request.body.decode('utf-8')
            body_data = json.loads(body_unicode)
            add_user(body_data['username'], pk)
            return Response({'message': 'Успешно'})
        else:
            return Response({'message': 'Вы не являетесь лидером группы'}, status=403)

    @extend_schema(request=GroupRemoveUserSerializer, responses={
        status.HTTP_200_OK: MessageSerializer,
        status.HTTP_400_BAD_REQUEST: BadRequestErrorSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_403_FORBIDDEN: MessageSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer})
    @action(detail=True, methods=['post'])
    def remove_user(self, request, pk=None):
        """
        Исключить пользователя из группы с идентификатором id
        """
        if self.request.user == get_leader(pk):
            body_unicode = request.body.decode('utf-8')
            body_data = json.loads(body_unicode)
            remove_user(body_data['user_id'], pk)
            return Response({'message': 'Успешно'})
        else:
            return Response({'message': 'Вы не являетесь лидером группы'}, status=403)


    @extend_schema(responses={
        status.HTTP_200_OK: MessageSerializer,
        status.HTTP_401_UNAUTHORIZED: DetailSerializer,
        status.HTTP_404_NOT_FOUND: DetailSerializer})
    @action(detail=True, methods=['post'])
    def exit_from_group(self, request, pk=None):
        """
        Выйти авторизованному пользователю из группы с номером id
        """
        remove_user(self.request.user.id, pk)
        return Response({'message': 'Успешно'})

from drf_spectacular.types import OpenApiTypes
from drf_spectacular.utils import OpenApiParameter, extend_schema
from rest_framework import viewsets
from rest_framework.decorators import action, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from django.forms.utils import json
from django.contrib.auth.models import User
from djoser.utils import login_user
from djoser.serializers import TokenSerializer
from groups.services import select_group_operations

from operations.services import get_operations_by_user
from .services import create_user, update_user, reset_user_password
from .models import AppUserProfile, GroupUser
from .serializers import *
from operations.serializers import AccountSerializer, CreditPaySerializer, OperationSerializer
from groups.serializers import GroupSerializer


class UserProfileViewSet(viewsets.ModelViewSet):
    queryset = AppUserProfile.objects.all()
    serializer_class = AppUserProfileSerializer

    @extend_schema(request=AppUserProfileCreateSerializer, responses=TokenSerializer)
    def create(self, request, *args, **kwargs):
        """
        Создать профиль нового пользователя (после регистрации в системе)
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        user = create_user(body_data) 
        token = login_user(request, user)
        return Response(TokenSerializer(token).data)
    
    @extend_schema(request=AppUserProfileUpdateSerializer, responses=UserSerializer)
    def update(self, request, *args, **kwargs):
        """
        Обновить базовую информацию профиля пользователя
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        user = update_user(body_data)
        return Response(UserSerializer(user).data) 

    @extend_schema(responses=UserSerializer)
    @action(detail=False, methods=['get'])
    def me(self, request):
        """
        Получить информацию профиля зарегистрированного пользователя
        """
        serializer = UserSerializer(self.request.user)
        return Response(serializer.data)

    @extend_schema(responses=OnboardStatusSerializer)
    @permission_classes([IsAuthenticated])
    @action(detail=False, methods=['get'])
    def onboard_status(self, request):
        """
        Получить информацию о статусе показа onboard экрана зарегистрированного пользователя
        """
        status = self.request.user.appuserprofile.onboard
        return Response({'onboard': status})

    @extend_schema(responses=AccountSerializer)
    @action(detail=True, methods=['get'])
    def accounts(self, request, pk=None):
        """
        Получить счета авторизованного пользователя (или другого определенного пользователя)
        """
        if pk == None:
            user = self.request.user
        else:
            user = User.objects.get(id=pk)
        queryset = user.account_set.all()
        serializer = AccountSerializer(queryset, many=True)
        return Response(serializer.data)

    @extend_schema(parameters=[OpenApiParameter("group", OpenApiTypes.UUID, OpenApiParameter.QUERY, description="group id for operation selection by group")],responses=OperationSerializer)
    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить все операции определенного пользователя с идентификатором pk
        Чтобы сделать выборку по группе, необходимо указать в параметрах запроса group=id группы
        """
        user = User.objects.get(id=pk)
        data = get_operations_by_user(user)
        if request.GET.get('group') != None:
            data = select_group_operations(data, user, request.GET['group'])
        serializer = OperationSerializer(data, many=True)
        return Response(serializer.data)

    @extend_schema(responses=CreditPaySerializer)
    @action(detail=False, methods=['get'])
    def credits(self, request):
        """
        Получить кредитные платежи авторизованного пользователя
        """
        queryset = self.request.user.creditpay_set.all()
        serializer = CreditPaySerializer(queryset, many=True)
        return Response(serializer.data)

    @extend_schema(responses=CreditPaySerializer)
    @action(detail=False, methods=['get'])
    def groups(self, request):
        """
        Получить список групп, в которых состоит авторизованный пользователь
        """
        queryset = GroupUser.objects.filter(user=self.request.user)
        result = list()
        for q in queryset:
            data = GroupSerializer(q.group).data
            data['is_leader'] = q.is_leader 
            result.append(data)
        return Response(result)

    @extend_schema(request=UserResetPasswordSerializer, responses=MessageSerializer)
    @action(detail=False, methods=['post'])
    def reset_password(self, request):
        """
        Восстановить пароль пользователя
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        response = reset_user_password(body_data)
        return Response(response['body'], status=response['status'])

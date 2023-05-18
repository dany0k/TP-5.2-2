from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from django.forms.utils import json
from django.contrib.auth.hashers import check_password, make_password
from django.contrib.auth.models import User
from djoser.utils import login_user
from djoser.serializers import TokenSerializer

from operations.services import get_operations_by_user
from .models import AppUserProfile, GroupUser
from .serializers import AppUserProfileSerializer, UserSerializer
from operations.serializers import AccountSerializer, CreditPaySerializer
from groups.serializers import GroupSerializer


class UserProfileViewSet(viewsets.ModelViewSet):
    queryset = AppUserProfile.objects.all()
    serializer_class = AppUserProfileSerializer

    def create(self, request, *args, **kwargs):
        """
        Создать профиль нового пользователя (после регистрации в системе)
        В теле запроса указываются следующие данные:
            "user_id": "id зарегистрированного пользователя",
            "first_name": "имя",
            "last_name": "фамилия",
            "secret_word": "секретное слово"
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        user = User.objects.get(id=body_data['user_id'])
        user.first_name = body_data['first_name']
        user.last_name = body_data['last_name']
        user.appuserprofile.secret_word = make_password(body_data['secret_word'])
        user.save()
        token = login_user(request, user)
        return Response(TokenSerializer(token).data)
    
    def update(self, request, *args, **kwargs):
        """
        Обновить базовую информацию профиля пользователя
        В теле запроса указываются следующие данные:
            "id": "id зарегистрированного пользователя",
            "first_name": "имя",
            "last_name": "фамилия",
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        user = User.objects.get(id=body_data['id'])
        user.first_name = body_data['first_name']
        user.last_name = body_data['last_name']
        user.save()
        return Response(UserSerializer(user).data) 

    @action(detail=False, methods=['get'])
    def me(self, request):
        """
        Получить информацию профиля зарегистрированного пользователя
        """
        serializer = UserSerializer(self.request.user)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def accounts(self, request, pk=None):
        """
        Получить счета авторизованного пользователя (или другого определенного пользователя)
        """
        if pk == None:
            user = self.request.user
        else:
            user = AppUserProfile.objects.get(pk=pk)

        queryset = user.account_set.all()
        serializer = AccountSerializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def operations(self, request, pk=None):
        """
        Получить все операции определенного пользователя с идентификатором pk
        """
        user = User.objects.get(id=pk)
        data = get_operations_by_user(user)
        return Response(data)

    @action(detail=False, methods=['get'])
    def credits(self, request):
        """
        Получить кредитные платежи авторизованного пользователя
        """
        queryset = self.request.user.creditpay_set.all()
        serializer = CreditPaySerializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'])
    def groups(self, request):
        """
        Получить список групп, в которых состоит авторизованный пользователь
        """
        queryset = GroupUser.objects.filter(user=self.request.user)
        result = list()
        for q in queryset:
            result.append(q.group)
        serializer = GroupSerializer(result, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['post'])
    def reset_password(self, request):
        """
        Восстановить пароль пользователя
        В качестве тела запроса указывается:
            "email": "username пользователя",
            "secret_word": "секретное слово",
            "new_password": "новый пароль"
        """
        body_unicode = request.body.decode('utf-8')
        body_data = json.loads(body_unicode)
        try:
            user: User = User.objects.get(username=body_data['email'])
        except:
            return Response({'message': 'Такой пользователь на зарегистрирован'}, status=403)
        if check_password(body_data['secret_word'], user.appuserprofile.secret_word):
            user.set_password(body_data['new_password'])
            user.save()
            return Response({'message': 'Пароль был изменен успешно'})
        else:
            return Response({'message': 'Введено неправильное секретное слово'}, status=403)

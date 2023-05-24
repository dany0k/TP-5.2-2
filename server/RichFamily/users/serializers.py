from rest_framework import serializers

from .models import AppUserProfile, User

class UserSerializer(serializers.ModelSerializer):
    """ Сериализатор для модели базового профиля пользователя """
    class Meta:
        model = User
        fields = ('id', 'first_name', 'last_name', 'email')


class UserResetPasswordSerializer(serializers.Serializer):
    """ Сериализатор для смены пароля пользователя """
    email = serializers.EmailField()
    secret_word = serializers.CharField(max_length=255)
    new_password = serializers.CharField(max_length=255)


class MessageSerializer(serializers.Serializer):
    message = serializers.CharField(max_length=255)

class AppUserProfileSerializer(serializers.ModelSerializer):
    """ Сериализатор для модели дополнительных данных профиля пользователя """
    class Meta:
        model = AppUserProfile
        fields = '__all__'


class AppUserProfileCreateSerializer(serializers.Serializer):
    """ Сериализатор для создания профиля пользователя """
    user_id = serializers.IntegerField()
    first_name = serializers.CharField(max_length=255)
    last_name = serializers.CharField(max_length=255)
    secret_word = serializers.CharField(max_length=255)


class AppUserProfileUpdateSerializer(serializers.Serializer):
    """ Сериализатор для обновления профиля пользователя """
    id = serializers.IntegerField()
    first_name = serializers.CharField(max_length=255)
    last_name = serializers.CharField(max_length=255)

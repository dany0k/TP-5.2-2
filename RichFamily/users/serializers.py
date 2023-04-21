from rest_framework import serializers

from .models import AppUserProfile, User

class UserSerializer(serializers.ModelSerializer):
    """ Сериализатор для модели базового профиля пользователя """
    class Meta:
        model = User
        fields = ('id', 'first_name', 'last_name', 'email')

class AppUserProfileSerializer(serializers.ModelSerializer):
    """ Сериализатор для модели дополнительных данных профиля пользователя """
    class Meta:
        model = AppUserProfile
        fields = '__all__'


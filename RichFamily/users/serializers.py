from rest_framework import serializers

from .models import AppUserProfile


class AppUserProfileSerializer(serializers.ModelSerializer):
    """ Сериализатор для модели пользователя приложения """
    class Meta:
        model = AppUserProfile
        fields = '__all__'
        extra_kwargs = {'groups': {'required': False}}


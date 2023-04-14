from rest_framework import serializers

from .models import Group
from users.serializers import AppUserProfileSerializer

class MyGroupSerializer(serializers.ModelSerializer):
    """ Сериализатор для группы пользователей """
    users = AppUserProfileSerializer(many=True, read_only=True)

    class Meta:
        model = Group
        fields = 'id', 'gr_name', 'users'
        extra_kwargs = {'users': {'required': False}}

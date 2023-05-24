from rest_framework import serializers

from .models import Group

class GroupSerializer(serializers.ModelSerializer):
    class Meta:    
        model = Group
        fields = ('id', 'gr_name')


class GroupAddUserSerializer(serializers.Serializer):
    """ Сериализатор для добавления нового пользователя в группу """
    username = serializers.EmailField()


class GroupRemoveUserSerializer(serializers.Serializer):
    """ Сериализатор для удаления пользователя из группы """
    user_id = serializers.EmailField()


class MessageSerializer(serializers.Serializer):
    message = serializers.CharField(max_length=255)

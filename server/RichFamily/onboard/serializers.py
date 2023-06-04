from rest_framework import serializers


class OnboardScreenSerializer(serializers.Serializer):
    """ Класс сериализатора для onboard screen """
    title = serializers.CharField(max_length=100)
    description = serializers.CharField(max_length=255)
    onboard_type = serializers.CharField(max_length=20)

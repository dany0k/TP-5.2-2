from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response

from .models import Group
from .serializers import GroupSerializer
from users.serializers import AppUserProfileSerializer


class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer

    @action(detail=True, methods=['get'])
    def users(self, request, pk=None):
        queryset = Group.objects.get(pk=pk).user_set.all()
        serializer = AppUserProfileSerializer(queryset, many=True)
        return Response(serializer.data)


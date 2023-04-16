from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response

from .models import AppUserProfile
from .serializers import AppUserProfileSerializer
from operations.serializers import AccountSerializer, CreditPaySerializer
from groups.serializers import GroupSerializer


class UserProfileViewSet(viewsets.ModelViewSet):
    queryset = AppUserProfile.objects.all()
    serializer_class = AppUserProfileSerializer

    @action(detail=True, methods=['get'])
    def accounts(self, request, pk=None):
        if pk == None:
            user = self.request.user
        else:
            user = AppUserProfile.objects.get(pk=pk)

        queryset = user.account_set.all()
        serializer = AccountSerializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'])
    def credits(self, request):
        queryset = self.request.user.creditpay_set.all()
        serializer = CreditPaySerializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'])
    def groups(self, request):
        queryset = self.request.user.groups
        serializer = GroupSerializer(queryset, many=True)
        return Response(serializer.data)

   

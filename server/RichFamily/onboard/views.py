from drf_spectacular.types import OpenApiTypes
from drf_spectacular.utils import OpenApiParameter, extend_schema
from rest_framework.decorators import action
from rest_framework.viewsets import ModelViewSet
from rest_framework.response import Response

from .permissions import IsAdminOrReadOnly

from .services import get_screens_by_type
from .models import OnboardScreen
from .serializers import OnboardScreenSerializer


class OnboardScreenViewSet(ModelViewSet):
    queryset = OnboardScreen.objects.all()
    serializer_class = OnboardScreenSerializer
    permission_classes = [IsAdminOrReadOnly]

    @extend_schema(parameters=[OpenApiParameter("onboard_type", 
                                                OpenApiTypes.STR, 
                                                OpenApiParameter.QUERY, 
                                                description="onboard type for selection by user")],responses=OnboardScreenSerializer)
    @action(methods=['get'], detail=False)
    def get_onboards_by_type(self, request):
        onboards = get_screens_by_type(request.GET.get('onboard_type'))
        serializer = OnboardScreenSerializer(onboards, many=True)
        return Response(serializer.data)

from drf_spectacular.types import OpenApiTypes
from drf_spectacular.utils import OpenApiParameter, extend_schema, extend_schema_view
from rest_framework.decorators import action
from rest_framework.viewsets import ModelViewSet
from rest_framework.response import Response


from .permissions import IsAdminOrReadOnly

from .services import get_screens_by_type
from .models import OnboardScreen
from .serializers import OnboardScreenSerializer

@extend_schema_view(
    list=extend_schema(description="Получить список onboard экранов"),
    create=extend_schema(exclude=True),
    retrieve=extend_schema(exclude=True),
    update=extend_schema(exclude=True),
    partial_update=extend_schema(exclude=True),
    destroy=extend_schema(exclude=True),
)
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
        """
        Получить все onboard экраны по определенному статусу экрана
        """
        onboards = get_screens_by_type(request.GET.get('onboard_type'))
        serializer = OnboardScreenSerializer(onboards, many=True)
        return Response(serializer.data)

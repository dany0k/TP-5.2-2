from rest_framework.routers import SimpleRouter

from operations.views import OperationTemplateViewSet


router = SimpleRouter()
router.register(r'^', OperationTemplateViewSet)

urlpatterns = []

urlpatterns += router.urls

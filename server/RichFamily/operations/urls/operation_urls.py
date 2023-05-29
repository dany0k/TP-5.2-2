from rest_framework.routers import SimpleRouter

from operations.views import OperationViewSet


router = SimpleRouter()
router.register(r'^', OperationViewSet)

urlpatterns = []

urlpatterns += router.urls

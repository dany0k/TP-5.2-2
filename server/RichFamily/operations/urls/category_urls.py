from rest_framework.routers import SimpleRouter

from operations.views import OperationCategoryViewSet


router = SimpleRouter()
router.register(r'^', OperationCategoryViewSet)

urlpatterns = []

urlpatterns += router.urls

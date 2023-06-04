from rest_framework.routers import SimpleRouter

from operations.views import AccountViewSet


router = SimpleRouter()
router.register(r'^', AccountViewSet)

urlpatterns = []

urlpatterns += router.urls

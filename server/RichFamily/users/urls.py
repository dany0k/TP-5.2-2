from rest_framework.routers import SimpleRouter

from .views import UserProfileViewSet


router = SimpleRouter()
router.register(r'^', UserProfileViewSet)

urlpatterns = []

urlpatterns += router.urls

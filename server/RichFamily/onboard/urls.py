from rest_framework.routers import SimpleRouter

from .views import OnboardScreenViewSet


router = SimpleRouter()
router.register(r'^', OnboardScreenViewSet)

urlpatterns = []

urlpatterns += router.urls

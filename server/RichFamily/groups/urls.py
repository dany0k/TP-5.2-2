from rest_framework.routers import SimpleRouter

from .views import GroupViewSet


router = SimpleRouter()
router.register(r'^', GroupViewSet)

urlpatterns = []

urlpatterns += router.urls

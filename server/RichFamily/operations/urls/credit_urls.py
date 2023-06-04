from rest_framework.routers import SimpleRouter

from operations.views import CreditPayViewSet


router = SimpleRouter()
router.register(r'^', CreditPayViewSet)

urlpatterns = []

urlpatterns += router.urls

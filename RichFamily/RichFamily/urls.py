"""
URL configuration for RichFamily project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import include, path, re_path
from rest_framework import routers, permissions
from drf_yasg.views import get_schema_view
from drf_yasg import openapi

from operations.views import *
from users.views import *
from groups.views import *

# Блок подключения Swagger
schema_view = get_schema_view(
   openapi.Info(
      title="Swagger API",
      default_version='v1',
      description="Test description",
      terms_of_service="https://www.google.com/policies/terms/",
      contact=openapi.Contact(email="contact@snippets.local"),
      license=openapi.License(name="BSD License"),
   ),
   public=True,
   permission_classes=(permissions.AllowAny,),
)

router = routers.SimpleRouter()
router.register(r'categories', OperationCategoryViewSet, basename='categories')
router.register(r'templates', OperationTemplateViewSet, basename='templates')
router.register(r'accounts', AccountViewSet, basename='accounts')
router.register(r'operations', OperationViewSet, basename='operations')
router.register(r'credits', CreditPayViewSet, basename='credits')
router.register(r'users', UserProfileViewSet, basename='users')
router.register(r'groups', GroupViewSet, basename='groups')

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/v1/', include(router.urls)),
    path('api/v1/auth/', include('djoser.urls')),       
    re_path(r'^auth/', include('djoser.urls.authtoken')),
    path('swagger/', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
    path('redoc/', schema_view.with_ui('redoc', cache_timeout=0), name='schema-redoc'),
]
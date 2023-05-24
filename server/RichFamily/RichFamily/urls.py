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
from rest_framework import routers
from drf_spectacular.views import SpectacularAPIView, SpectacularSwaggerView

from operations.views import *
from users.views import *
from groups.views import *


urlpatterns = [
    path('admin/', admin.site.urls),
    re_path(r'^users', include('users.urls')),
    re_path(r'^accounts', include('operations.urls.account_urls')),
    re_path(r'^categories', include('operations.urls.category_urls')),
    re_path(r'^operations', include('operations.urls.operation_urls')),
    re_path(r'^templates', include('operations.urls.template_urls')),
    re_path(r'^credits', include('operations.urls.credit_urls')),
    re_path(r'^groups', include('groups.urls')),
    path('auth/utils/', include('djoser.urls')),       
    re_path(r'^auth/', include('djoser.urls.authtoken')),
    path('api/v1/swagger/', SpectacularSwaggerView.as_view(url_name="schema")),
    path('api/v1/schema/', SpectacularAPIView.as_view(), name="schema"),
]

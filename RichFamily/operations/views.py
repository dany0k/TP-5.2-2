from django.shortcuts import render
from rest_framework import generics

from .models import OperationCategory, OperationTemplate
from .serializers import OperationCategorySerializer, OperationTemplateSerializer


class OperationCategoryAPIList(generics.ListCreateAPIView):
    queryset = OperationCategory.objects.all()
    serializer_class = OperationCategorySerializer


class OperationCategoryAPIOne(generics.RetrieveUpdateDestroyAPIView):
    queryset = OperationCategory.objects.all()
    serializer_class = OperationCategorySerializer


class OperationTemplateAPIList(generics.ListCreateAPIView):
    queryset = OperationTemplate.objects.all()
    serializer_class = OperationTemplateSerializer

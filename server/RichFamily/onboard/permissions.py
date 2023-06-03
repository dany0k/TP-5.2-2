from rest_framework import permissions

class IsAdminOrReadOnly(permissions.BasePermission):
    """
    Разрешение на запись только для администраторов.
    """
    def has_permission(self, request, view):
        # Разрешено только чтение для неавторизованных пользователей
        if request.method in permissions.SAFE_METHODS:
            return True

        # Разрешено запись только для администраторов
        return request.user and request.user.is_staff



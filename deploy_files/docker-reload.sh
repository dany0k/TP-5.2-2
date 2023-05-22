#!/bin/bash

# Останавливаем обратный прокси, чтобы он не сыпал ошибками
docker-compose stop server

# Останавливаем и удаляем старый контейнер с приложением
docker-compose stop web
docker-compose rm web

# Подтягиваем новый контейнер с Docker Hub и перезапускаем приложение
docker-compose pull
docker-compose up -d web
docker-compose up -d server

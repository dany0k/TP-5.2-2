#!/bin/bash

docker-compose up -d app_database
docker-compose up -d web
docker-compose up -d server

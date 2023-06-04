#!/bin/bash

python ./manage.py migrate
python ./manage.py test
python ./manage.py collectstatic --noinput
gunicorn --forwarded-allow-ips=* --bind 0.0.0.0:8080 -w 2 RichFamily.wsgi:application

FROM python:3.11-bullseye

ENV PYTHONUNBUFFERED 1

RUN apt update && apt install -y python3-dev

WORKDIR /code

# Сначала копируем requirements.txt, для того, чтобы образ собирался быстрее (см. слои докера)
COPY requirements.txt /code/
RUN pip install -r requirements.txt

# Далее копируем сам код приложения
COPY . /code/
WORKDIR /code/

EXPOSE 8000

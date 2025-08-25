# Alfa Firestarter Project

## Описание
Alfa Firestarter — Это система обработки входящих файлов, состоящая из нескольких микросервисов на Spring Boot (Gradle), которые взаимодействуют между собой через Kafka и MongoDB.  
Основные сервисы:
- **file-uploader** — загружает файлы и публикует сообщения в Kafka.
- **file-processor** — обрабатывает файлы, читая сообщения из Kafka и обновляя их статус.
- **file-status-processor** — слушает топик Kafka и сохраняет статусы файлов в MongoDB.
- 
## API

https://web.postman.co/workspace/My-Workspace~bb478e3f-48e3-4ed1-8901-419e031ce8c3/collection/33961830-41b67582-f717-4672-b1f1-655bd473f8a8?action=share&source=copy-link&creator=33961830

## Запуск
Для запуска используется `Makefile`

Сначала `docker-compose up` для поднятия инфраструктуры, а потом `make start-all` для запуска всех сервисов

## Тестирование
Используются **Testcontainers** для запуска Kafka и MongoDB в контейнерах во время тестов.  
Пример интеграционного теста: `StatusConsumerIT` — проверяет, что сообщение, отправленное в Kafka, сохраняется в MongoDB.

Запуск тестов:
make test-all

## Требования
- Docker 
- JDK 17+
- Gradle (через wrapper `./gradlew` или `gradlew.bat`)

# Company finance management system
__Company finance management system__ - API для управления финансами предприятия и финансовая аналитика компании.
## Основные функции
- Управление подразделениями предприятия
- Многопользовательский и многоролевой доступ сотрудников
- Раздельный финансовый учет по каждому подразделению
- Финансовая аналитика
## Технологический стек
- Java 21
- Spring Boot 4.1.1
- Mapstruct
- Gradle 8.14.3
- PostgreSQL 18.6
## Как запустить локально
Для удобства, настройте файл окружения .dev
```dotenv
# Профиль
SPRING_PROFILES_ACTIVE=prod

# Подключение БД
DB_HOST=localhost
DB_PORT=5432
DB_USER=<пользователь>
DB_PASSWORD=<пароль>
DB_NAME=company_finance_management_system

# Безопасность
SECURITY_JWT_BASE64_SECRET=<base64-encoded секрет>
SECURITY_ACCESS_TOKEN_EXPIRY=5m
SECURITY_REFRESH_TOKEN_EXPIRY=30d
SECURITY_OAUTH2_GOOGLE_CLIENT_ID=<Google client ID>
SECURITY_OAUTH2_GOOGLE_CLIENT_SECRET=<Google client secret>

# Админ
BOOTSTRAP_ADMIN_PASSWORD=<adminPassword>
BOOTSTRAP_ADMIN_EMAIL=<admin@example.com>
```
Запустите docker-compose следующей командой
```shell
docker compose up
```
Или вот так, если имя файла переменных окруженя отличное от .env
```shell
docker compose --env-file <имя_env_файла> up
```
Чтобы "пробросить" .env файл в IntelliJ Idea вам будет необходимо установить плагин EnvFile из Marketplace. 
Далее добавляем .env файл: Run->Edit configurations. Включаем EnvFile и внизу нажимаем +, чтобы выбрать .env файл (при выборе файла нужно нажать Command + Shift + . (точка), чтобы отобразить скрытые файлы).
![Конфигурация](docs/img/getEnvSetUp.png)
> Для запуска приложения через IntelliJ Idea из docker-compose следует поднимать только сервис "db"
> ```shell
> docker compose up <имя-сервиса> # в данном случае db
> ```

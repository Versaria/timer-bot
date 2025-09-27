# Руководство разработчика
## 🛠️ Настройка окружения
### Предварительные требования
- **Java JDK 11+** (рекомендуется Zulu 11)
- **Apache Maven 3.6+**
- **Git 2.50.1+**
- **Telegram аккаунт** и токен бота от @BotFather

### Установка зависимостей
```bash
# Проверка установленных версий
java -version
mvn -version
git --version
```

## 🚀 Локальная разработка
### 1. Клонирование и настройка
```bash
git clone https://github.com/Versaria/timer-bot.git
cd timer-bot
```

### 2. Конфигурация бота
```bash
# Создание директории для конфигурации
mkdir -p src/main/resources/config

# Создание файла конфигурации
cat > src/main/resources/config/config.properties << EOF
bot.token=YOUR_BOT_TOKEN_HERE
bot.username=your_bot_username
log.level=INFO
EOF
```

### 3. Получение токена бота
1. Найти в Telegram [@BotFather](https://t.me/BotFather)
2. Отправить команду `/newbot`
3. Следовать инструкциям по созданию бота
4. Скопировать полученный токен в формате: `123456789:ABCdefGHIjklMnOpQRstUVwxyz`

### 4. Сборка и запуск
```bash
# Компиляция проекта
mvn clean compile

# Запуск бота
mvn exec:java

# Или сборка JAR файла
mvn clean package
java -jar target/timer-bot-1.0.0-jar-with-dependencies.jar
```

## 📁 Структура проекта
```
timer-bot/
├── src/main/java/com/timerbot/
│   ├── handlers/                # Обработчики сообщений
│   │   ├── CommandHandler.java
│   │   └── MessageHandler.java
│   ├── models/                  # Модели данных
│   │   └── UserTimer.java
│   ├── utils/                   # Утилитарные классы
│   │   ├── BotUtils.java
│   │   ├── TimeUtils.java
│   │   └── ValidationUtils.java
│   ├── TimerBot.java            # Основной класс бота
│   ├── TimerManager.java        # Менеджер таймеров
│   └── Main.java                # Точка входа
├── src/main/resources/          # Ресурсы
│   └── config/                  # Конфигурация
├── .github/workflows/           # CI/CD
├── docs/                        # Документация
└── pom.xml                      # Зависимости Maven
```

## 🔧 Команды разработки
### Сборка и тестирование
```bash
# Компиляция проекта
mvn clean compile

# Создание исполняемого JAR
mvn clean package

# Запуск с проверкой зависимостей
mvn dependency:tree

# Очистка проекта
mvn clean
```

### Анализ кода
```bash
# Проверка стиля кода (если настроен checkstyle)
mvn checkstyle:check

# Генерация документации
mvn javadoc:javadoc
```

## 🐛 Отладка и решение проблем
### Частые проблемы
**Ошибка компиляции:**
```bash
# Очистка и пересборка
mvn clean compile
```

**Бот не отвечает:**
- Проверьте токен в config.properties
- Убедитесь, что бот запущен
- Проверьте интернет-соединение

**Ошибки зависимостей:**
```bash
# Обновление зависимостей
mvn dependency:resolve
```

## 📝 Рекомендации по разработке
### Стандарты кода
- Следуйте Java Code Conventions
- Используйте понятные имена переменных и методов
- Комментируйте сложную логику
- Соблюдайте принципы SOLID

### Коммиты
Используйте семантические сообщения коммитов:
- `feat:` - новая функциональность
- `fix:` - исправление ошибки
- `docs:` - обновление документации
- `refactor:` - рефакторинг кода
- `test:` - добавление тестов

## 🔄 CI/CD
Проект использует GitHub Actions для автоматической сборки. При каждом push в main ветку запускается:
- Компиляция проекта
- Проверка зависимостей
- Сборка JAR файла

Файл конфигурации: `.github/workflows/maven.yml`

## 📚 Дополнительные ресурсы
- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Java 11 Documentation](https://docs.oracle.com/en/java/javase/11/)
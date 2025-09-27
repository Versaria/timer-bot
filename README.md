# Timer Bot ⏰

![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-orange.svg)
![Telegram Bot API](https://img.shields.io/badge/Telegram%20Bot%20API-6.9.7-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

Telegram-бот для управления таймерами с уведомлениями. Написан на Java 11 с использованием Telegram Bot API. Поддерживает установку таймеров, отмену, проверку статуса и автоматические уведомления.

[Документация Telegram Bot API](https://core.telegram.org/bots/api)

## 🚀 Быстрый старт
### Требования
- **Java JDK** 11 или новее
- **Apache Maven** 3.6+
- **Токен бота** от [@BotFather](https://t.me/BotFather)
- **Git** (для клонирования репозитория)

### Установка и запуск
```bash
# Клонирование репозитория
```bash
# 1. Клонирование репозитория
git clone https://github.com/Versaria/timer-bot.git
cd timer-bot

# 2. Настройка конфигурации
# Отредактируйте файл config/config.properties
# Установите ваш токен бота от @BotFather

# 3. Сборка проекта
mvn clean compile

# 4. Запуск бота
mvn exec:java

# Альтернативный запуск через JAR
mvn clean package
java -jar target/timer-bot-1.0.0-jar-with-dependencies.jar
```

## 📂 Структура проекта
```
timer-bot/
├── src/main/java/com/timerbot/
│   ├── handlers/                # Обработчики сообщений
│   │   ├── CommandHandler.java  # Обработчик команд бота
│   │   └── MessageHandler.java  # Основной обработчик сообщений
│   ├── models/                  # Модели данных
│   │   └── UserTimer.java       # Модель таймера пользователя
│   ├── utils/                   # Утилитарные классы
│   │   ├── BotUtils.java        # Утилиты для работы с Telegram API
│   │   ├── TimeUtils.java       # Утилиты для работы со временем
│   │   └── ValidationUtils.java # Утилиты для валидации данных
│   ├── TimerBot.java            # Основной класс бота
│   ├── TimerManager.java        # Менеджер таймеров
│   └── Main.java                # Точка входа приложения
├── config/
│   └── config.properties        # Конфигурационные параметры
├── pom.xml                      # Конфигурация Maven
├── .gitignore                   # Исключения для Git
└── README.md
```

## 📋 Функционал
- **Установка таймеров** - Команда `/timer 10m` (от 1 минуты до 24 часов)
- **Отмена таймеров** - Команда `/cancel` для отмены активного таймера
- **Проверка статуса** - Команда `/status` для просмотра оставшегося времени
- **Автоматические уведомления** - Уведомление "Время вышло!" по истечении таймера
- **Многопользовательская поддержка** - Одновременная работа с несколькими пользователями
- **Валидация данных** - Проверка корректности вводимого времени

### Основные команды 
- **`/start`** - Начало работы с ботом, вывод справки
- **`/timer <time>m`** - Установка таймера (от 1 минуты до 24 часов)
- **`/cancel`** - Отмена активного таймера
- **`/status`** - Проверка статуса текущего таймера
- **`/help`** - Полная справка по командам

## 📊 Покрытие тестами
На текущий момент проект находится в стадии разработки. Модульное тестирование планируется добавить в будущих версиях.

## 💻 Пример работы
### Установка таймера
```
Пользователь: /timer 5m

Бот: ✅ Таймер на 5 минут установлен.
     ⏳ Истекает в 14:30:25
     ⌛ Осталось: 5 минут
```

### Проверка статуса
```
Пользователь: /status

Бот: ⏰ Активный таймер:
     🕐 Истекает в: 14:30:25  
     ⌛ Осталось: 3 минуты 45 секунд
```

### Уведомление о завершении
```
Бот: ⏰ Время вышло! Таймер на 5 минут завершен.
```

## 📜 Лицензия
MIT License. Полный текст доступен в файле [LICENSE](LICENSE).

##  👉 Подробнее в [CONTRIBUTING.md](docs/CONTRIBUTING.md)

## 🤝 Как внести вклад
Мы приветствуем вклад в развитие проекта!

1. Форкните репозиторий
2. Создайте ветку для вашей функции (`git checkout -b feature/amazing-feature`)
3. Сделайте коммит изменений (`git commit -m 'Add amazing feature'`)
4. Запушьте ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request
---

<details>
<summary>🔧 Дополнительные команды</summary>

```bash
# Очистка проекта
mvn clean

# Только компиляция
mvn compile

# Запуск с дополнительными параметрами
mvn exec:java -Dexec.args="--config=path/to/config.properties"

# Создание исходников JAR
mvn source:jar

# Просмотр дерева зависимостей
mvn dependency:tree

# Проверка стиля кода
mvn checkstyle:check
```
</details>

**Примечание**: Проект создан для демонстрации лучших практик разработки Telegram ботов на Java. Подходит как для обучения, так и для production использования.
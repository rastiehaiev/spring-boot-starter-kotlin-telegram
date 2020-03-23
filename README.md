# spring-boot-starter-kotlin-telegram

This library is intended to simplify creation of Telegram Bots. 
Under the hood we use:
  - [kotlin-telegram-bot](https://jitpack.io/p/seik/kotlin-telegram-bot) project to work with BOTs API and
  - Spring Boot Framework 2.2.2

The main feature of this project is the possibility to describe Telegram Bot's logic using Kotlin DSL.

# Configuration
Simple Spring Boot configuration is described below:
```kotlin
@Bean
open fun requestLimiter(): RequestLimiter {
    return RequestLimiter(allowedRequestsPerMinute = 40, banDurationSeconds = 120)
}

@Bean
open fun telegramSupportedLanguages(): TelegramSupportedLanguages {
    return defaults("English" to Locale("en"))
            .then("Українська" to Locale("uk"))
            .then("Русский" to Locale("ru"))
}

@Bean
open fun startCommand(): TelegramCommand<NoOpCommand> {
    return startCommand {
        stage("start") {
            start { progress ->
                message {
                    key = "start.info.welcome.message"
                    args = listOf(progress.firstName)
                    parseMode = ParseMode.MARKDOWN
                }
            }
        }
    }
}

@Bean
open fun setLanguageCommand(view: TelegramView, localeSettingsService: LocaleSettingsService): TelegramCommand<NoOpCommand> {
    return setLanguage {
        stage("request_language") {
            start {
                message {
                    key = "setlanguage.info.specify.language.from.the.list"
                    keyboard = keyboard { row(view.supportedLanguagesButtons()) }
                }
            }
            text { _, text, progress ->
                if (!localeSettingsService.isLanguageSupported(text)) {
                    message(key = "setlanguage.error.unsupported.language.specified", args = listOf(progress.firstName))
                } else {
                    localeSettingsService.updateUserLanguagePreferences(progress.chatId!!, text)
                    finish {
                        message(key = "setlanguage.info.language.settings.changed", args = listOf(text))
                    }
                }
            }
        }
    }
}

@Bean
open fun telegramGlobalOperations(birthDayHelper: BirthDayHelper,
                                  reminderService: BirthDayReminderService): TelegramGlobalOperations {
    return globalOperations {
        callback<NotificationActionCallback>("notificationaction") { update, callbackData ->
            reminderService.reactOnNotificationAction(update, callbackData)
        }
        event<NotificationActionResult> { event ->
            answerCallbackQuery {
                callbackQueryId = event.payload.callbackQueryId
                key = "birthdayreminder.notification.action.result"
                args = listOf(event.payload.person.fullName())
            }
        }
        ban { _, options ->
            message {
                key = "system.user.exceeded.requests.limit"
                args = listOf(options.allowedRequestsPerMinute.toString(), options.banDurationSeconds.toString())
            }
        }
    }
}
```
The concept of `command` is used to define the set of actions to perform the single operation. Command can have many `stages`. For example, if you create reminder bot, it may request name of your event, description, time, priority and so on. So the idea is to enable bot to request information stage by stage. 

Bot can operate in 2 modes: `POLLING` and `WEBHOOK`. This is specified in your application configuration file:
```yaml
telegram:
  token: ${TELEGRAM_TOKEN}
  mode: ${TELEGRAM_MODE:POLLING}
```

# Features
- Limit requests by creating the bean of type RequestLimiter and specifying 2 parameters: allowed amount of requests per minute and ban duration seconds.
- Message resolving according to user's preferred locale.
- Command actions (available during execution of specific command) and also global actions
- React not only to telegram updates, but also the third-party events

# Notes
The library is currently in Beta. Feel free to suggest improvements/fixes.

# License
MIT
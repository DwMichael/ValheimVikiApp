package com.rabbitv.valheimviki.domain.model.language

/**
 * Supported application languages.
 * Each entry maps a display name + native label to a language code
 * used by the API and DataStore.
 */
enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeLabel: String,
    val flagEmoji: String,
) {
    ENGLISH("en", "English", "English", "🇬🇧"),
    POLISH("pl", "Polish", "Polski", "🇵🇱"),
    GERMAN("de", "German", "Deutsch", "🇩🇪"),
    FRENCH("fr", "French", "Français", "🇫🇷"),
    SPANISH("es", "Spanish", "Español", "🇪🇸"),
    PORTUGUESE("pt", "Portuguese", "Português", "🇵🇹"),
    RUSSIAN("ru", "Russian", "Русский", "🇷🇺"),
    LITHUANIAN("lt", "Lithuanian", "Lietuvių", "🇱🇹"),
    ESTONIAN("et", "Estonian", "Eesti", "🇪🇪"),
    CZECH("cs", "Czech", "Čeština", "🇨🇿"),
    SLOVAK("sk", "Slovak", "Slovenčina", "🇸🇰");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.firstOrNull { it.code == code } ?: ENGLISH
        }
    }
}

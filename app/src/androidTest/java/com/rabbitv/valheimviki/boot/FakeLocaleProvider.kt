package com.rabbitv.valheimviki.boot

import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLocaleProvider @Inject constructor() : LocaleProvider {
    private var locale: Locale = Locale.ENGLISH

    fun setLocale(locale: Locale) {
        this.locale = locale
    }

    override fun deviceLocale(): Locale = locale
}

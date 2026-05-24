package com.rabbitv.valheimviki.boot

import android.content.res.Resources
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Source of the device's preferred locale. Extracted so tests can inject
 * a fake without depending on system state.
 */
interface LocaleProvider {
	fun deviceLocale(): Locale
}

@Singleton
class DefaultLocaleProvider @Inject constructor() : LocaleProvider {
	override fun deviceLocale(): Locale =
		Resources.getSystem().configuration.locales[0] ?: Locale.ENGLISH
}

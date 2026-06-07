package com.rabbitv.valheimviki.di

/**
 * Runtime switches for swapping between fake (integration) and real (E2E) implementations
 * inside the @TestInstallIn modules.
 *
 * Hilt's `@UninstallModules` does not work on `@TestInstallIn` modules, so per-test selection
 * has to be done at @Provides time. Flip a flag in `@Before` (see `BaseRealE2ETest`) **before**
 * `HiltAndroidRule.inject()` runs — the @Provides functions read the flag when first invoked.
 *
 * After each test, [reset] must be called to restore defaults.
 */
object TestModuleConfig {
	@Volatile var useRealDatabase: Boolean = false
	@Volatile var useRealDataStore: Boolean = false
	@Volatile var useRealLocaleProvider: Boolean = false
	@Volatile var useRealNetwork: Boolean = false

	fun reset() {
		useRealDatabase = false
		useRealDataStore = false
		useRealLocaleProvider = false
		useRealNetwork = false
	}
}

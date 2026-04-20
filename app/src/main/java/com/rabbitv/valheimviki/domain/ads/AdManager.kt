package com.rabbitv.valheimviki.domain.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * Manages interstitial ads with a frequency-cap approach.
 *
 * Professional strategy: show an interstitial ad only after the user has visited
 * a certain number of detail screens (DETAIL_VIEWS_BETWEEN_ADS). This ensures
 * the user has had meaningful interaction before seeing any ad.
 *
 * The ad is pre-loaded in the background so there is zero visual delay when shown.
 */
@Singleton
class AdManager @Inject constructor(
	@ApplicationContext private val context: Context,
) {
	private var interstitialAd: InterstitialAd? = null
	private var detailViewCount = 0
	private var isLoading = false

	companion object {
		private const val TAG = "AdManager"

		// Show an interstitial every N detail-screen visits.
		// Professional apps typically use 4-6 to avoid annoying users.
		const val DETAIL_VIEWS_BETWEEN_ADS = 10

		// Google's official test interstitial ad unit ID.
		// TODO: Replace with your real ad unit ID from AdMob console before publishing!
		const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
	}

	/**
	 * Pre-load an interstitial ad in the background.
	 * Call this early (e.g. after app start or after showing an ad) so the next
	 * ad is ready instantly when needed.
	 */
	fun preloadAd() {
		if (interstitialAd != null || isLoading) return
		isLoading = true

		val adRequest = AdRequest.Builder().build()
		InterstitialAd.load(
			context,
			INTERSTITIAL_AD_UNIT_ID,
			adRequest,
			object : InterstitialAdLoadCallback() {
				override fun onAdLoaded(ad: InterstitialAd) {
					Log.d(TAG, "Interstitial ad pre-loaded successfully")
					interstitialAd = ad
					isLoading = false
				}

				override fun onAdFailedToLoad(error: LoadAdError) {
					Log.d(TAG, "Interstitial ad failed to load: ${error.message}")
					interstitialAd = null
					isLoading = false
				}
			}
		)
	}

	/**
	 * Called every time the user navigates to a detail screen.
	 * Returns true if an ad should be shown right now.
	 */
	fun onDetailScreenVisited(): Boolean {
		detailViewCount++
		Log.d(TAG, "Detail view count: $detailViewCount / $DETAIL_VIEWS_BETWEEN_ADS")
		return detailViewCount >= DETAIL_VIEWS_BETWEEN_ADS && interstitialAd != null
	}

	/**
	 * Show the interstitial ad on the given Activity.
	 * After the ad is dismissed (or fails), pre-loads the next one automatically.
	 */
	fun showAd(activity: Activity, onAdDismissed: () -> Unit = {}) {
		val ad = interstitialAd
		if (ad == null) {
			Log.d(TAG, "No ad available to show")
			onAdDismissed()
			return
		}

		ad.fullScreenContentCallback = object : FullScreenContentCallback() {
			override fun onAdDismissedFullScreenContent() {
				Log.d(TAG, "Interstitial ad dismissed")
				interstitialAd = null
				detailViewCount = 0
				onAdDismissed()
				// Pre-load next ad immediately
				preloadAd()
			}

			override fun onAdFailedToShowFullScreenContent(error: AdError) {
				Log.d(TAG, "Interstitial ad failed to show: ${error.message}")
				interstitialAd = null
				detailViewCount = 0
				onAdDismissed()
				preloadAd()
			}
		}

		ad.show(activity)
	}
}

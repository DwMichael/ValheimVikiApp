package com.rabbitv.valheimviki.testing

import android.content.Context
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

fun MockWebServer.enqueueJson(json: String, code: Int = 200) {
    enqueue(MockResponse().setResponseCode(code).setBody(json))
}

fun MockWebServer.enqueueFromAsset(context: Context, assetPath: String, code: Int = 200) {
    val json = context.assets.open(assetPath).bufferedReader().readText()
    enqueue(MockResponse().setResponseCode(code).setBody(json))
}

/**
 * Sets a path-based dispatcher so parallel requests each get the right response.
 * [routes] maps a URL path substring to the JSON body string.
 * Unmatched paths return 200 with an empty array (safe default for non-tested categories).
 */
fun MockWebServer.setPathDispatcher(routes: Map<String, String>) {
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: ""
            val match = routes.entries.firstOrNull { (key, _) -> path.contains(key, ignoreCase = true) }
            return if (match != null) {
                MockResponse().setResponseCode(200).setBody(match.value)
            } else {
                MockResponse().setResponseCode(200).setBody("[]")
            }
        }
    }
}

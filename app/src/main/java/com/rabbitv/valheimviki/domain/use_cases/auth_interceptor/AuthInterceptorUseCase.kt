package com.rabbitv.valheimviki.domain.use_cases.auth_interceptor


import com.rabbitv.valheimviki.utils.ApiKey.Companion.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptorUseCase : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()

		
		val newRequest = originalRequest.newBuilder()
			.header("Authorization", "Bearer $API_KEY")
			.build()

		return chain.proceed(newRequest)
	}


}
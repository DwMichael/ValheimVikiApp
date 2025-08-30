package com.rabbitv.valheimviki.domain.use_cases.auth_interceptor


import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptorUseCase : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()


		val newRequest = originalRequest.newBuilder()
			.header("Authorization", "Bearer ")
			.build()

		return chain.proceed(newRequest)
	}


}
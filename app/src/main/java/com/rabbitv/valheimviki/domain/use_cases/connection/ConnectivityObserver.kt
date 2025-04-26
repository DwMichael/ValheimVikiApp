package com.rabbitv.valheimviki.domain.use_cases.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkConnectivityObserver @Inject constructor(
    private val context: Context
) : NetworkConnectivity{
    private val _connectivityManager = context.getSystemService<ConnectivityManager>()



    override val isConnected: Flow<Boolean>
        get() = callbackFlow {
            val callback = object : NetworkCallback(){
                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(false)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    trySend(false)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(true)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    trySend(connected)
                }




            }
            _connectivityManager?.registerDefaultNetworkCallback(callback)
            awaitClose {
                _connectivityManager?.unregisterNetworkCallback(callback)
            }
        }

}
package com.rabbitv.valheimviki.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivity {
    val isConnected:Flow<Boolean>
}
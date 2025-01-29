package com.rabbitv.valheimviki.presentation.core

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel:ViewModel() {

    private val _drawerState = MutableStateFlow(DrawerValue.Closed)
    val drawerState: StateFlow<DrawerValue> = _drawerState.asStateFlow()

    fun openDrawer() {
        _drawerState.value = DrawerValue.Open
    }

    fun closeDrawer() {
        _drawerState.value = DrawerValue.Closed
    }
}
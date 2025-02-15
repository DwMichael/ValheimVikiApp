package com.rabbitv.valheimviki.assertions

import androidx.navigation.NavController
import org.junit.Assert.assertEquals


fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}
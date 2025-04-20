package com.rabbitv.valheimviki.presentation.detail.creature.components

import android.util.Log
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BossStatsFlowRow(
    mainBoss: MainBoss,
) {
    FlowRow(
        maxItemsInEachRow = 2,
    ) {
        if (mainBoss.baseDamage.split(",").any { it.isNotBlank() }) {
            LabeledCardWithList(
                mainBoss.baseDamage.split(","),
                painter = painterResource(R.drawable.base_damage_bg),
                label = "BASE DAMAGE",
                icon = Lucide.Swords
            )
        }

        if (mainBoss.weakness.split(",").any { it.isNotBlank() }) {

            LabeledCardWithList(
                mainBoss.weakness.split(","),
                painter = painterResource(R.drawable.weakness_bg),
                label = "WEAKNESS",
                icon = Lucide.Unlink
            )
        }
        if (mainBoss.resistance.split(",").any { it.isNotBlank() }) {
            Log.e("RESISTANCE ", "${mainBoss.resistance.split(",")}")
            LabeledCardWithList(
                mainBoss.resistance.split(","),
                painter = painterResource(R.drawable.resistance_bg),
                label = "RESISTANCE",
                icon = Lucide.Grab
            )
        }
        if (mainBoss.baseDamage.split(",").any { it.isNotBlank() }) {

            LabeledCardWithList(
                mainBoss.baseDamage.split(","),
                painter = painterResource(R.drawable.immune_bg),
                label = "IMMUNE",
                icon = Lucide.Shield
            )
        }
    }
}
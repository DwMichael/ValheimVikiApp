package com.rabbitv.valheimviki.presentation.detail.creature.components

import android.util.Log
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.Boss
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BossStatsFlowRow(
    item: Boss,
) {
    FlowRow(
        maxItemsInEachRow = 2,
    ) {
        if (item.baseDamage?.split(",")!!.any { it.isNotBlank() }) {
            LabeledCardWithList(
                item.baseDamage!!.split(","),
                painter = painterResource(R.drawable.base_damage_bg),
                label = "BASE DAMAGE",
                icon = Lucide.Swords
            )
        }

        if (item.weakness?.split(",")!!.any { it.isNotBlank() }) {

            LabeledCardWithList(
                item.weakness!!.split(","),
                painter = painterResource(R.drawable.weakness_bg),
                label = "WEAKNESS",
                icon = Lucide.Unlink
            )
        }
        if (item.resistance?.split(",")!!.any { it.isNotBlank() }) {
            Log.e("RESISTANCE ", "${item.resistance?.split(",")}")
            LabeledCardWithList(
                item.resistance!!.split(","),
                painter = painterResource(R.drawable.resistance_bg),
                label = "RESISTANCE",
                icon = Lucide.Grab
            )
        }
        if (item.baseDamage?.split(",")!!.any { it.isNotBlank() }) {

            LabeledCardWithList(
                item.baseDamage!!.split(","),
                painter = painterResource(R.drawable.immune_bg),
                label = "IMMUNE",
                icon = Lucide.Shield
            )
        }
    }
}

@Composable
@Preview("BossStatsFlowRow", showBackground = true)
fun BossStatsFlowRowPreview() {
    BossStatsFlowRow(
        item = MainBoss(
            id = "wqaew",
            category = "dasd",
            subCategory = "dasda",
            imageUrl = "dasda",
            name = "dasda",
            description = "dasda",
            order = 2,
            levels = 2,
            baseHP = 500,
            weakness = "dasda,140 Pierce + 100 Poison,sdfsdfsd,sdf3w2342,sedfswefs,dasdasda2",
            resistance = "dasda",
            baseDamage = "dasda",
            collapseImmune = "dasda",
            forsakenPower = "dasda"
        ),
    )
}
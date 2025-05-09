package com.rabbitv.valheimviki.presentation.detail.creature.components.rows

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Atom
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Unlink
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithLabeledList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatsFlowRow(
    baseDamage: String? = null,
    weakness: String? = null,
    resistance: String? = null,
    collapseImmune: String? = null,
    abilities: String? = null
) {
    fun String?.safeList(): List<String> {
        if (this == "null" || this == null) return emptyList()
        return this.split(",").filter { it.isNotBlank() && it != "null" }
    }

    val baseDamageList = baseDamage.safeList()
    val weaknessList = weakness.safeList()
    val resistanceList = resistance.safeList()
    val immunityList = collapseImmune.safeList()
    val abilitiesList = abilities.safeList()
    val firstLimit = 2
    val secondLimit = 3
    val heightFirst = 150.dp
    val heightSecond = 200.dp
    val heightThird = 240.dp

    fun getMaxHeight(list: List<String>): Dp {
        return when {
            list.size <= firstLimit -> heightFirst
            list.size <= secondLimit -> heightSecond
            else -> heightThird
        }
    }


    fun getMaxWidth(list: List<String>): Float {
        return if (list.size <= secondLimit) 0.5f else 1f
    }

    FlowRow(maxItemsInEachRow = 2) {
        if (baseDamageList.isNotEmpty()) {
            CardWithLabeledList(
                baseDamageList,
                painter = painterResource(R.drawable.base_damage_bg),
                label = stringResource(R.string.base_damage),
                icon = Lucide.Swords,
                maxHeight = getMaxHeight(baseDamageList),
                maxWidth = getMaxWidth(baseDamageList)
            )
        }

        if (weaknessList.isNotEmpty()) {
            CardWithLabeledList(
                weaknessList,
                painter = painterResource(R.drawable.weakness_bg),
                label = stringResource(R.string.weakness),
                icon = Lucide.Unlink,
                maxHeight = getMaxHeight(weaknessList),
                maxWidth = getMaxWidth(weaknessList)
            )
        }

        if (resistanceList.isNotEmpty()) {
            CardWithLabeledList(
                resistanceList,
                painter = painterResource(R.drawable.resistance_bg),
                label = stringResource(R.string.resistance),
                icon = Lucide.Grab,
                maxHeight = getMaxHeight(resistanceList),
                maxWidth = getMaxWidth(resistanceList)
            )
        }

        if (immunityList.isNotEmpty()) {
            CardWithLabeledList(
                immunityList,
                painter = painterResource(R.drawable.immune_bg),
                label = stringResource(R.string.immune),
                icon = Lucide.Shield,
                maxHeight = getMaxHeight(immunityList),
                maxWidth = getMaxWidth(immunityList)
            )
        }

        if (abilitiesList.isNotEmpty()) {
            CardWithLabeledList(
                abilitiesList,
                painter = painterResource(R.drawable.abilities),
                label = stringResource(R.string.abilities),
                icon = Lucide.Atom,
                maxHeight = getMaxHeight(abilitiesList),
                maxWidth = 1f
            )
        }
    }
}

@Composable
@Preview("BossStatsFlowRow", showBackground = true)
fun BossStatsFlowRowPreview() {
    StatsFlowRow(

        baseDamage = "dasda",
        weakness = "dasda,140 Pierce + 150 Poison,sdfsdfsd,sdf3w2342,sedfswefs,dasdasda2",
        resistance = "dasda",
        collapseImmune = "dasda",
        abilities = "dasda",
    )
}
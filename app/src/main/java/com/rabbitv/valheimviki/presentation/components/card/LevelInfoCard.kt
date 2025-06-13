package com.rabbitv.valheimviki.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.weapon.MaterialUpgrade
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.level1RequiredMaterials
import com.rabbitv.valheimviki.utils.FakeData.level1Stats

interface GridLevelInfo{
    val id:Int
    val icon:ImageVector
    val iconColor:Color
    val title:String
    val power:Int?
}


@Composable
fun LevelInfoCard(
    modifier: Modifier = Modifier,
    level:Int = 0,
    upgradeInfoList:List<GridLevelInfo> = emptyList(),
    materialsForUpgrade:List<MaterialUpgrade> = emptyList()
) {
    val gridState = rememberLazyGridState()
    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(),
        elevation = CardDefaults.cardElevation() ,
        border = BorderStroke(0.1.dp, Color.Gray),

    ) {
        TopExpandableItem(level =level)
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(BODY_CONTENT_PADDING.dp),
            columns =GridCells.Fixed(2),
            state = gridState,

            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            items(upgradeInfoList,key = {it.id}) { upgradeInfo ->
                LevelInfoGridItem(upgradeInfo = upgradeInfo)
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 10.dp),
            thickness = 0.1.dp,
            color = PrimaryWhite
        )

        Text(
            text = "Required Materials:",
            modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end = BODY_CONTENT_PADDING.dp , top = BODY_CONTENT_PADDING.dp),
            color = PrimaryWhite,
            style = MaterialTheme.typography.titleSmall
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = BODY_CONTENT_PADDING.dp),
        ) {
            materialsForUpgrade.forEach { material ->
                material.quantityList[level]?.let {
                    MaterialForUpgrade(
                        name = material.material.name,
                        quantity = it
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
    }
}

@Composable
fun MaterialForUpgrade(name:String,quantity:Int){
    Row(
        modifier = Modifier.padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
                text = name,
        style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.bodySmall
        )

    }
}

@Composable
fun LevelInfoGridItem(upgradeInfo :GridLevelInfo){
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = upgradeInfo.icon,
            contentDescription = upgradeInfo.title,
            tint = upgradeInfo.iconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = upgradeInfo.title +": ",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = upgradeInfo.power.toString(),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun TopExpandableItem(modifier :Modifier  = Modifier,level:Int){
    Row(
        modifier = Modifier.fillMaxWidth()
            .drawBehind {
                val borderStrokeWidth = 0.5.dp.toPx()
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderStrokeWidth
                )
            }
        .padding(BODY_CONTENT_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Level ${level+1}",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                modifier = modifier,
                tint = PrimaryWhite
            )
        }
    }
}
@Preview(name = "TopExpandableItem")
@Composable
private fun PreviewTopExpandableItem() {
    ValheimVikiAppTheme {
        TopExpandableItem(
            modifier = Modifier,
            level = 1
        )
    }
}


@Preview(name = "LevelInfoCard")
@Composable
private fun PreviewLevelInfoCard() {
    ValheimVikiAppTheme {
        LevelInfoCard(
            upgradeInfoList = level1Stats,
            materialsForUpgrade = level1RequiredMaterials
        )
    }
}

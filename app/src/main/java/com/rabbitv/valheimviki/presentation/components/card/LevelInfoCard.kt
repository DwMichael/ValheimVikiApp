package com.rabbitv.valheimviki.presentation.components.card

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.food.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.level1RequiredMaterials
import com.rabbitv.valheimviki.utils.FakeData.level1Stats
import com.rabbitv.valheimviki.utils.mapUpgradeInfoToGridList
import org.jetbrains.annotations.Async

data class GridLevelInfo(
    val id:Int,
    val icon:ImageVector,
    val iconColor:Color,
    val title:String,
    val power:Int?
)


@Composable
fun LevelInfoCard(
    modifier: Modifier = Modifier,
    level:Int = 0,
    upgradeInfoList:List<GridLevelInfo> = emptyList(),
    materialsForUpgrade:List<MaterialUpgrade> = emptyList(),
    foodForUpgrade:List<FoodAsMaterialUpgrade> = emptyList(),
    visibleContent : MutableState<Boolean> = remember{ mutableStateOf(false)}
) {

    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(
            contentColor = PrimaryWhite,
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation() ,
        border = BorderStroke(1.dp, Color.Gray),

    ) {
        TopExpandableItem(level =level, onToggleExpansion = {visibleContent.value = !visibleContent.value})
        AnimatedVisibility(visibleContent.value) {
            Column(
                Modifier.background(
                        color = Color(0xFF0d1c1d))
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BODY_CONTENT_PADDING.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    upgradeInfoList
                        .filter { (it.power ?: 0) > 0 }
                        .forEach { info ->
                            LevelInfoGridItem(
                                modifier = Modifier.wrapContentWidth(),
                                upgradeInfo = info
                            )
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
                    modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.titleLarge
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BODY_CONTENT_PADDING.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    materialsForUpgrade.forEach { material ->
                        material.quantityList[level]?.let {
                            MaterialForUpgrade(
                                name = material.material.name,
                                imageUrl = material.material.imageUrl,
                                quantity = it
                            )
                        }
                    }
                    foodForUpgrade.forEach { material ->
                        material.quantityList[level]?.let {
                            MaterialForUpgrade(
                                name = material.materialFood.name,
                                imageUrl = material.materialFood.imageUrl,
                                quantity = it
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
            }
        }
    }
}

@Composable
fun MaterialForUpgrade( name:String,imageUrl:String,quantity:Int){
    Row(
        modifier = Modifier.padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .size(100)
                .crossfade(true)
                .build(),
            error = if(LocalInspectionMode.current)  painterResource(R.drawable.testweapon) else null,
            contentDescription = "Item image",
            contentScale = ContentScale.FillBounds,

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
                text = name,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )

    }
}

@Composable
fun LevelInfoGridItem(
    upgradeInfo: GridLevelInfo,
    modifier: Modifier = Modifier    // ← add this
) {
    Row(
        modifier = modifier
            .wrapContentWidth()        // ← let the Row size itself to its content
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = upgradeInfo.icon,
            contentDescription = upgradeInfo.title,
            tint = upgradeInfo.iconColor
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${upgradeInfo.title}:",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = upgradeInfo.power?.toString() ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
        )
        Spacer(Modifier.width(10.dp))
    }
}


@Composable
fun TopExpandableItem(modifier :Modifier  = Modifier,level:Int , onToggleExpansion:()->Unit ={}){
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Color(0xFF193e43)).clickable { onToggleExpansion() }
            .drawBehind {
                val borderStrokeWidth = 1.dp.toPx()
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
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        IconButton(
            onClick = onToggleExpansion,
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
            level = 1,
            onToggleExpansion = {}
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

@Preview(name = "LevelInfoCard - All Stats")
@Composable
private fun PreviewLevelInfoCard_AllStats() {

    val allStatsData = UpgradeInfo(
        upgradeLevels = 4,
        fireDamage = 25,
        frostDamage = 25,
        slashDamage = 120,
        spiritDamage = 30,
        durability = 500,
        stationLevel = 5,
        lightningDamage = 25,
        pierceDamage = 6000000,
        bluntDamage = 110,
        poisonDamage = 40,
        chopDamage = 55,
        pureDamage = 15,
        pickaxeDamage = 75,
        damageAbsorbedBloodMagic0 = 10000,
        maximumSkeletonsControllable = 3,
        damageAbsorbedBloodMagic100 = 20,
        chopTreesDamage = 55.0
    )


    val statsForPreview = mapUpgradeInfoToGridList(allStatsData)

    ValheimVikiAppTheme {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
        ) {
            LevelInfoCard(
                level = 0,
                upgradeInfoList = statsForPreview,
                materialsForUpgrade = level1RequiredMaterials,
                visibleContent = remember { mutableStateOf(true) }
            )
        }
    }
}
package com.rabbitv.valheimviki.presentation.components.card

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
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
import com.rabbitv.valheimviki.ui.theme.ForestGreen20Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.utils.FakeData.level1RequiredMaterials
import com.rabbitv.valheimviki.utils.FakeData.level1Stats
import com.rabbitv.valheimviki.utils.mapUpgradeInfoToGridList
import org.jetbrains.annotations.Async

data class GridLevelInfo(
    val id:Int,
    val icon:ImageVector,
    val iconColor:Color,
    val title:String,
    val power:Int?,
)


@Composable
fun LevelInfoCard(
    modifier: Modifier = Modifier,
    level:Int = 0,
    upgradeStats:List<GridLevelInfo> = emptyList(),
    materialsForUpgrade:List<MaterialUpgrade> = emptyList(),
    foodForUpgrade:List<FoodAsMaterialUpgrade> = emptyList(),
    visibleContent : MutableState<Boolean> = remember{ mutableStateOf(false)}
) {
    val filteredList = upgradeStats
        .filter { (it.power ?: 0) > 0 }

    val hasMaterialsForLevel = materialsForUpgrade.any { material ->
        material.quantityList.getOrNull(level)?.let { it > 0 } ?: false
    }
    val hasFoodForLevel = foodForUpgrade.any { food ->
        food.quantityList.getOrNull(level)?.let { it > 0 } ?: false
    }
    Log.e("LevelInfoCard", "hasMaterialsForLevel: $hasMaterialsForLevel")
    val canUpgradeToLevel = hasMaterialsForLevel || hasFoodForLevel
    Log.e("canUpgradeToLevel", "canUpgradeToLevel: $canUpgradeToLevel")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(
            contentColor = PrimaryWhite,
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation() ,
        border = BorderStroke(1.dp, YellowDTBorder),

    ) {
        TopExpandableItem(level =level, onToggleExpansion = {visibleContent.value = !visibleContent.value})
        AnimatedVisibility(visibleContent.value) {
            Column(
                Modifier
                    .background(color = Color(0xFF0d1c1d))
                    .heightIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = BODY_CONTENT_PADDING.dp, start = BODY_CONTENT_PADDING.dp , top = BODY_CONTENT_PADDING.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    filteredList.forEach { info ->
                        val titleLength = info.title.length
                        val powerLength = info.power?.toString()?.length ?: 0
                        val totalTextLength = titleLength + powerLength

                        val isLongText = totalTextLength > 15

                        LevelInfoGridItem(
                            modifier = if (isLongText) {
                                Modifier.fillMaxWidth().padding(start = 8.dp)
                            } else {
                                Modifier.fillMaxWidth(0.5f).padding(start = 8.dp)
                            },
                            upgradeInfo = info
                        )
                    }
                }

                if (canUpgradeToLevel) {
                    RequiredMaterialColumn(
                        level = level,
                        foodForUpgrade = foodForUpgrade,
                        materialsForUpgrade = materialsForUpgrade
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BODY_CONTENT_PADDING.dp)
                            .background(
                                color = Color(0xFF1a2a2b),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Upgrade to this level not yet available",
                            color = Color(0xFFFFB800),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
            }
        }
    }
}

@Composable
fun RequiredMaterialColumn(level:Int = 0,
                           foodForUpgrade:List<FoodAsMaterialUpgrade> = emptyList(),
                           materialsForUpgrade:List<MaterialUpgrade> = emptyList(),){
    Text(
        text = "Required Materials:",
        modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
        color = PrimaryWhite,
        style = MaterialTheme.typography.titleLarge
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BODY_CONTENT_PADDING.dp)
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        materialsForUpgrade.forEach { material ->
            material.quantityList.getOrNull(level)?.let { quantity ->
                if (quantity > 0) {
                    MaterialForUpgrade(
                        name = material.material.name,
                        imageUrl = material.material.imageUrl,
                        quantity = quantity
                    )
                }
            }
        }

        foodForUpgrade.forEach { material ->
            material.quantityList.getOrNull(level)?.let { quantity ->
                if (quantity > 0) {
                    MaterialForUpgrade(
                        name = material.materialFood.name,
                        imageUrl = material.materialFood.imageUrl,
                        quantity = quantity
                    )
                }
            }
        }
    }
}

@Composable
fun MaterialForUpgrade(name: String, imageUrl: String, quantity: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .size(150)
                .crossfade(true)
                .build(),
            error = if (LocalInspectionMode.current) painterResource(R.drawable.testweapon) else null,
            contentDescription = name,
            contentScale = ContentScale.Fit,

            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))

        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Required: $quantity",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LevelInfoGridItem(
    upgradeInfo: GridLevelInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = upgradeInfo.icon,
            contentDescription = upgradeInfo.title,
            tint = upgradeInfo.iconColor,
            modifier = Modifier.size(34.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${upgradeInfo.title}:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 4.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = upgradeInfo.power?.toString() ?: "",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(10.dp))
    }
}


@Composable
fun TopExpandableItem(modifier :Modifier  = Modifier,level:Int , onToggleExpansion:()->Unit ={}){
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).background(color = ForestGreen20Dark).clickable { onToggleExpansion() }
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
            upgradeStats = level1Stats,
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
                upgradeStats = statsForPreview,
                materialsForUpgrade = level1RequiredMaterials,
                visibleContent = remember { mutableStateOf(true) }
            )
        }
    }
}
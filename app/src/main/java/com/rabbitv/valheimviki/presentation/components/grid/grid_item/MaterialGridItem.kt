package com.rabbitv.valheimviki.presentation.components.grid.grid_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDT
import com.rabbitv.valheimviki.ui.theme.YellowDTIconColor


@Composable
fun <T> MaterialGridItem(
    item: GridCategoryOption<T>,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = Color(0x25FFFFFF)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.height(6.dp))
                Text(
                    text = item.label.uppercase(),
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(-1f)
                    .blur(
                        radius = 28.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0x12FFFFFF),
                                YellowDT,
                                PrimaryOrange,
                                YellowDTIconColor,

                                ),
                            radius = 1000f,
                            center = Offset.Infinite
                        )
                    )
            )
        }
    }
}


@Preview(name = "MaterialGridItem", showBackground = false)
@Composable
private fun PreviewMaterialGridItem() {

    val item = MaterialSegmentOption.CREATURE_DROP
    ValheimVikiAppTheme {
        Row(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(200.dp)
            )
            {
                MaterialGridItem(
                    item = item,
                    onClick = { },
                    height = ITEM_HEIGHT_TWO_COLUMNS,
                    icon = Lucide.Cuboid,
                    modifier = Modifier,
                )
            }

        }
    }

}
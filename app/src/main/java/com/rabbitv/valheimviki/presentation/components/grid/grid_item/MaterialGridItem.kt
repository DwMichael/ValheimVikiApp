package com.rabbitv.valheimviki.presentation.components.grid.grid_item

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.ui.theme.ITEM_HEIGHT_TWO_COLUMNS
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDT
import com.rabbitv.valheimviki.ui.theme.YellowDTIconColor


@Composable
fun <T> MaterialGridItem(
    onClick: () -> Unit,
    item: GridCategoryOption<T>,
    modifier: Modifier = Modifier,
    height: Dp = 150.dp,
) {

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(SMALL_PADDING),
        colors = CardDefaults.cardColors(
            containerColor = YellowDT
        ),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(item.image),
                contentDescription = "Grid item image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(SMALL_PADDING)),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        8.dp
                    ),

                ) {
                Icon(
                    imageVector = item.icon,
                    tint = YellowDTIconColor,
                    contentDescription = "Icon ${item.label}",
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = item.label.uppercase(),
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }


    }
}


@Preview(name = "MaterialGridItem", showBackground = false)
@Composable
private fun PreviewMaterialGridItem() {

    val item = MaterialSegmentOption.CREATURE_DROP
    ValheimVikiAppTheme {
        Column(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
            )
            {
                MaterialGridItem(
                    item = item,
                    onClick = { },
                    height = ITEM_HEIGHT_TWO_COLUMNS,
                    modifier = Modifier,
                )
            }

        }
    }

}
package com.rabbitv.valheimviki.presentation.detail.creature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R

@Composable
fun LabeledCardWithList(
    textList: List<String>,
    painter: Painter = painterResource(id = R.drawable.base_damage_bg),
    label: String = "BASE DAMAGE",
    icon: ImageVector = Lucide.Flame,
    maxWidth : Float = 0.5f
) {
    val lazyListState = rememberLazyListState()

    Card(
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .heightIn(min = 100.dp, max = 200.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize())
        {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .matchParentSize()
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        OverlayLabel(
                            icon = icon,
                            label = label,
                        )
                    }
                    if (lazyListState.canScrollForward) {
                        Image(
                            painter = painterResource(R.drawable.scroll_up),
                            contentDescription = "ScrollUpImage",
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(6.dp)
                                .rotate(-10f)
                        )
                    }
                }

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                ) {
                    items(textList){ text ->
                        Box(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    bottom = 4.dp,
                                    start = 20.dp,
                                    end = 8.dp
                                )
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.4f))

                        ) {
                            Text(
                                text = text.trimStart { it == ' ' },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview("LabeledCardWithList", showBackground = true)
fun LabeledCardWithListPreview() {

    Box(Modifier.size(225.dp))
    {
        LabeledCardWithList(
            textList = listOf("SDASD","SDasdasdasd","fsr3qewr2q3rw","ASdsada","sdasdasda","dsadasdasd"),
            painter = painterResource(R.drawable.base_damage_bg),
            label = "ROGOTODO",
            icon = Lucide.Flame,
            maxWidth = 1f
        )
    }

}
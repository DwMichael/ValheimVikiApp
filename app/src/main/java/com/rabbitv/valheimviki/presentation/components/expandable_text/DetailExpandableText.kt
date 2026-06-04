package com.rabbitv.valheimviki.presentation.components.expandable_text

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.adaptive.LocalAdaptiveLayoutInfo
import com.rabbitv.valheimviki.ui.theme.DEFAULT_MINIMUM_TEXT_LINE
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

private val UnityColorTagRegex = Regex("</?color(?:=[^>]+)?>", RegexOption.IGNORE_CASE)

@Composable
fun DetailExpandableText(
	modifier: Modifier = Modifier,
	textModifier: Modifier = Modifier,
	text: String,
	collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
	showMoreText: String? = null,
	showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500, color = Color(0xFFAABBDD)),
	showLessText: String? = null,
	showLessStyle: SpanStyle = showMoreStyle,
	textAlign: TextAlign? = null,
	boxPadding: Dp = 0.dp
) {
	val isExpandable = remember { mutableStateOf(false) }
	var clickable by remember { mutableStateOf(false) }
	var lastCharIndex by remember { mutableIntStateOf(0) }
	val resolvedShowMoreText = showMoreText ?: stringResource(R.string.show_more)
	val resolvedShowLessText = showLessText ?: stringResource(R.string.show_less)
	val adaptiveInfo = LocalAdaptiveLayoutInfo.current
	val detailFontSize = when {
		adaptiveInfo.isExpandedWidth -> 21.sp
		adaptiveInfo.isMediumWidth -> 20.sp
		else -> 18.sp
	}
	val detailLineHeight = when {
		adaptiveInfo.isExpandedWidth -> 31.sp
		adaptiveInfo.isMediumWidth -> 29.sp
		else -> 26.sp
	}
	val htmlFormattedText = remember(text) {
		AnnotatedString.fromHtml(text.replace(UnityColorTagRegex, ""))
	}
	Box(
		modifier = modifier
			.padding(boxPadding)
			.clickable(clickable) {
				isExpandable.value = !isExpandable.value
			}
			.then(modifier)
	) {
		Text(
			modifier = textModifier
				.fillMaxWidth()
				.animateContentSize(),
			text = buildAnnotatedString {
				if (clickable) {
					if (isExpandable.value) {
						append(htmlFormattedText)
						withStyle(style = showLessStyle) { append(resolvedShowLessText) }
					} else {
						val adjustText =
							htmlFormattedText.substring(startIndex = 0, endIndex = lastCharIndex)
								.dropLast(resolvedShowMoreText.length)
								.dropLastWhile { Character.isWhitespace(it) || it == '.' }
						append(adjustText)
						withStyle(style = showMoreStyle) { append(resolvedShowMoreText) }
					}
				} else {
					append(htmlFormattedText)
				}
			},

			maxLines = if (isExpandable.value) Int.MAX_VALUE else collapsedMaxLine,
			onTextLayout = { textLayoutResult ->
				if (!isExpandable.value && textLayoutResult.hasVisualOverflow) {
					val maxLineIndex = (collapsedMaxLine - 1)
						.coerceAtMost(textLayoutResult.lineCount - 1)
					lastCharIndex = textLayoutResult.getLineEnd(maxLineIndex)
					clickable = true
				}
			},
			style = MaterialTheme.typography.bodyLarge.copy(
				fontSize = detailFontSize,
				lineHeight = detailLineHeight
			),
			textAlign = textAlign,
		)
	}
}

@Preview("DetailExpandableText", showBackground = true)
@Composable
fun PreviewDetailExpandableText() {
	ValheimVikiAppTheme {
		DetailExpandableText(text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum")
	}
}

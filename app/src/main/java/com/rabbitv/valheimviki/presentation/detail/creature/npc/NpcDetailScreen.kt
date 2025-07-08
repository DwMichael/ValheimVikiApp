package com.rabbitv.valheimviki.presentation.detail.creature.npc

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TreePine
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.dividers.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.button.AnimatedBackButton
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.OverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.npc.model.NpcDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.npc.viewmodel.NpcDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkGrey
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import com.rabbitv.valheimviki.utils.FakeData.fakeNpcDetailUiState
import com.rabbitv.valheimviki.utils.valid

@Composable
fun NpcDetailScreen(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	viewModel: NpcDetailScreenViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	NpcDetailContent(
		onBack = onBack,
		onItemClick = onItemClick,
		uiState = uiState,
	)

}


@Composable
fun NpcDetailContent(
	onBack: () -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
	uiState: NpcDetailUiState,
) {
	val isExpandable = remember { mutableStateOf(false) }
	val isExpandableLocation = remember { mutableStateOf(false) }
	val isExpandableBiography = remember { mutableStateOf(false) }
	val scrollState = rememberScrollState()
	val headersShopTable = listOf(
		"Name", "Icon", "Cost",
	)
	val headersSellTable = listOf(
		"Name", "Icon", "Price for One"
	)

	val sideBorder = Modifier.drawBehind {
		val strokeWidth = 2.dp.toPx()
		//BOTTOM BORDER
		drawLine(
			color = DarkWood,
			start = Offset(0f, size.height),
			end = Offset(size.width, size.height),
			strokeWidth = strokeWidth
		)
		///LEFT BORDER
		drawLine(
			color = DarkWood,
			start = Offset(0f, 0f),
			end = Offset(0f, size.height),
			strokeWidth = strokeWidth
		)
		//RIGHT BORDER
		drawLine(
			color = DarkWood,
			start = Offset(size.width, 0f),
			end = Offset(size.width, size.height),
			strokeWidth = strokeWidth
		)
	}
	Scaffold { padding ->
		uiState.npc?.let {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(padding)
			) {
				Column(
					modifier = Modifier.verticalScroll(scrollState),
					verticalArrangement = Arrangement.Top,
					horizontalAlignment = Alignment.Start,
				) {
					MainDetailImage(
						imageUrl = it.imageUrl,
						name = it.name,
						textAlign = TextAlign.Center
					)
					DetailExpandableText(
						text = it.description,
						collapsedMaxLine = 3,
						isExpanded = isExpandable,
						boxPadding = BODY_CONTENT_PADDING.dp
					)
					TridentsDividedRow(text = "NPC DETAIL")
					if (uiState.biome != null) {
						CardWithOverlayLabel(
							painter = rememberAsyncImagePainter(uiState.biome.imageUrl),
							content = {
								Row(
									modifier = Modifier.clickable {
										val destination =
											WorldDetailDestination.BiomeDetail(biomeId = uiState.biome.id)
										onItemClick(destination)
									}
								) {
									Box(
										modifier = Modifier.fillMaxHeight()
									) {
										OverlayLabel(
											icon = Lucide.TreePine,
											label = " PRIMARY SPAWN",
										)
									}
									Text(
										uiState.biome.name.uppercase(),
										style = MaterialTheme.typography.bodyLarge,
										modifier = Modifier
											.align(Alignment.CenterVertically)
											.fillMaxWidth()
											.padding(8.dp),
										color = Color.White,
										textAlign = TextAlign.Center
									)
								}

							}
						)
					}
					if (it.location.isNotBlank()) {
						Text(
							"Location",
							style = MaterialTheme.typography.titleLarge,
							modifier = Modifier
								.align(Alignment.Start)
								.fillMaxWidth()
								.padding(8.dp),
							color = Color.White,
							textAlign = TextAlign.Start
						)
						DetailExpandableText(
							text = it.location,
							collapsedMaxLine = 3,
							isExpanded = isExpandableLocation,
							boxPadding = BODY_CONTENT_PADDING.dp
						)
					}
					if (it.biography.isNotBlank()) {
						TridentsDividedRow()
						Text(
							"Biography",
							style = MaterialTheme.typography.titleLarge,
							modifier = Modifier
								.align(Alignment.Start)
								.fillMaxWidth()
								.padding(8.dp),
							color = Color.White,
							textAlign = TextAlign.Start
						)
						DetailExpandableText(
							text = it.biography,
							collapsedMaxLine = 3,
							isExpanded = isExpandableBiography,
							boxPadding = BODY_CONTENT_PADDING.dp
						)
					}
					if (uiState.npc.name == "Hildir" && uiState.chestsLocation.isNotEmpty() && uiState.hildirChests.isNotEmpty()) {
						SlavicDivider()
						TridentsDividedRow(text = "QUESTS")
						HildirQuestSection(
							onItemClick = onItemClick,
							chestsLocations = uiState.chestsLocation,
							chestList = uiState.hildirChests,
						)
					}
					if (uiState.shopItems.isNotEmpty()) {
						TridentsDividedRow(text = "TRADING")
						Row(
							modifier = Modifier
								.padding(
									top = BODY_CONTENT_PADDING.dp,
									start = BODY_CONTENT_PADDING.dp,
									end = BODY_CONTENT_PADDING.dp
								),
							horizontalArrangement = Arrangement.Start,
							verticalAlignment = Alignment.CenterVertically
						) {
							Icon(
								painter = painterResource(R.drawable.money_bag_24px),
								tint = Color.White,
								contentDescription = "Rectangle section Icon",
							)
							Spacer(modifier = Modifier.width(11.dp))
							Text(
								text = "Sells",
								style = MaterialTheme.typography.titleLarge,
							)
						}
						ShopItemsTable(
							onItemClick = onItemClick,
							itemList = uiState.shopItems,
							headers = headersShopTable,
							modifier = sideBorder,
						)
					}
					if (uiState.shopSellItems.isNotEmpty()) {
						Row(
							modifier = Modifier.padding(
								top = BODY_CONTENT_PADDING.dp,
								start = BODY_CONTENT_PADDING.dp,
								end = BODY_CONTENT_PADDING.dp
							),
							horizontalArrangement = Arrangement.Start,
							verticalAlignment = Alignment.CenterVertically
						) {
							Icon(
								painter = painterResource(R.drawable.paid_24px),
								tint = Color.White,
								contentDescription = "Rectangle section Icon",
							)
							Spacer(modifier = Modifier.width(11.dp))
							Text(
								text = "Buys",
								style = MaterialTheme.typography.titleLarge,
							)
						}
						SellItemsTable(
							onItemClick = onItemClick,
							itemList = uiState.shopSellItems,
							headers = headersSellTable,
							modifier = sideBorder
						)
					}
					SlavicDivider()
					Box(modifier = Modifier.size(45.dp))
				}
				AnimatedBackButton(
					modifier = Modifier
						.align(Alignment.TopStart)
						.padding(16.dp),
					scrollState = scrollState,
					onBack = onBack
				)
			}
		}
	}
}

@Composable
fun HildirQuestSection(
	onItemClick: (DetailDestination) -> Unit,
	chestsLocations: List<PointOfInterest>,
	chestList: List<Material>,
) {
	val htmlText: String = stringResource(R.string.hildir_html_text).trimIndent()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(BODY_CONTENT_PADDING.dp)
	) {
		Text(
			text = stringResource(R.string.hildir_text_first),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.horizontalScroll(rememberScrollState())
		) {
			chestsLocations.forEach { pointOfInterest ->
				CardItemData(
					onItemClick = {
						val destination =
							WorldDetailDestination.PointOfInterestDetail(pointOfInterestId = pointOfInterest.id)
						onItemClick(destination)
					},
					pointOfInterest = pointOfInterest
				)
			}
		}
		Spacer(modifier = Modifier.padding(10.dp))
		Text(
			text = stringResource(R.string.hildir_text_second),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
		Text(
			text = AnnotatedString.fromHtml(
				htmlText,
			),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
		Text(
			text = stringResource(R.string.hildir_text_third),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
		Text(
			text = stringResource(R.string.hildir_text_fourth),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.horizontalScroll(rememberScrollState())
		) {
			chestList.forEach { chest ->
				CardItemData(
					onItemClick = {
						val destination =
							NavigationHelper.routeToMaterial(chest.subCategory, chest.id)
						onItemClick(destination)
					},
					chest
				)
			}
		}
		Spacer(modifier = Modifier.padding(10.dp))
		Text(
			text = stringResource(R.string.hildir_text_fifth),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
		Text(
			text = stringResource(R.string.hildir_text_sixth),
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.padding(10.dp))
	}
}


@Composable
fun CardItemData(
	onItemClick: () -> Unit,
	pointOfInterest: ItemData
) {
	Card(
		Modifier
			.size(150.dp)
			.shadow(
				elevation = 8.dp,
				shape = RoundedCornerShape(8.dp),
				spotColor = Color.White.copy(alpha = 0.25f)
			)
			.padding(8.dp)
			.clickable { onItemClick() }
	) {
		Box(
			modifier = Modifier.wrapContentHeight()
		) {
			AsyncImage(
				modifier = Modifier
					.fillMaxSize()
					.background(DarkGrey),
				model = ImageRequest.Builder(LocalContext.current)
					.data(pointOfInterest.imageUrl)
					.crossfade(true)
					.build(),
				contentDescription = stringResource(R.string.item_grid_image),
				contentScale = ContentScale.Crop
			)
			Surface(
				modifier = Modifier
					.align(Alignment.BottomStart)
					.fillMaxHeight(0.2f)
					.fillMaxWidth(),
				tonalElevation = 0.dp,
				color = Color.Black.copy(alpha = ContentAlpha.medium),
			) {

				Text(
					modifier = Modifier
						.padding
							(horizontal = 5.dp)
						.wrapContentHeight(align = Alignment.CenterVertically),
					text = pointOfInterest.name,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = Color.White,
					style = MaterialTheme.typography.labelLarge,
				)
			}
		}
	}
}


@Composable
fun SellItemsTable(
	onItemClick: (DetailDestination) -> Unit,
	itemList: List<Material>,
	headers: List<String>,
	modifier: Modifier
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(BODY_CONTENT_PADDING.dp),
	) {
		TableHeader(headers, textAlign = TextAlign.Start)
		itemList.forEach {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(IntrinsicSize.Min)
					.then(modifier)
					.padding(8.dp)
					.clickable {
						val destination = NavigationHelper.routeToMaterial(it.subCategory, it.id)
						onItemClick(destination)
					},
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					it.name,
					modifier = Modifier.weight(1f),
					style = MaterialTheme.typography.bodyLarge
				)
				Box(
					modifier = Modifier
						.weight(1f)
						.fillMaxWidth(),
					contentAlignment = Alignment.Center
				) {
					AsyncImage(
						model = ImageRequest.Builder(LocalContext.current)
							.data(it.imageUrl)
							.crossfade(true)
							.build(),
						placeholder = painterResource(R.drawable.green_thorch2),
						contentDescription = "Shop Icon Image",
						contentScale = ContentScale.Fit,
						modifier = Modifier.size(48.dp)
					)
				}
				Row(
					modifier = Modifier.weight(1f),
					verticalAlignment = Alignment.CenterVertically
				) {
					Image(
						painter = painterResource(R.drawable.gold_coin),
						contentDescription = null,
						modifier = Modifier.size(24.dp)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = "${it.sellPrice} Coins",
						color = Color(0xFFddbb2b),
						style = MaterialTheme.typography.bodyLarge
					)
				}
			}
		}
	}
}


@Composable
fun ShopItemsTable(
	onItemClick: (destination: DetailDestination) -> Unit,
	itemList: List<Material>,
	headers: List<String>,
	modifier: Modifier
) {

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(BODY_CONTENT_PADDING.dp),
	) {
		TableHeader(headers)
		itemList.forEach {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.height(IntrinsicSize.Min)
					.then(modifier)
					.padding(8.dp),
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							val destination = NavigationHelper.routeToMaterial(it.subCategory, it.id)
							onItemClick(destination)
						},
					verticalAlignment = Alignment.CenterVertically,
				) {
					Text(
						it.name,
						modifier = Modifier.weight(1f),
						style = MaterialTheme.typography.bodyLarge
					)
					Box(
						modifier = Modifier
							.weight(1f)
							.fillMaxWidth(),
						contentAlignment = Alignment.Center
					) {
						AsyncImage(
							model = ImageRequest.Builder(LocalContext.current)
								.data(it.imageUrl)
								.crossfade(true)
								.build(),
							placeholder = painterResource(R.drawable.green_thorch2),
							contentDescription = "Shop Icon Image",
							contentScale = ContentScale.Fit,
							modifier = Modifier.size(48.dp)
						)
					}
					Row(
						modifier = Modifier.weight(1f),
						verticalAlignment = Alignment.CenterVertically
					) {
						Image(
							painter = painterResource(R.drawable.gold_coin),
							contentDescription = null,
							modifier = Modifier.size(24.dp)
						)
						Spacer(Modifier.width(4.dp))
						Text(
							text = "${it.price} Coins",
							color = Color(0xFFddbb2b),
							style = MaterialTheme.typography.bodyLarge
						)
					}
				}
				it.effect.valid()?.let { effectText ->
					Spacer(Modifier.height(10.dp))
					Row {
						Text(
							"Effect: ",
							Modifier.weight(0.2f),
							style = MaterialTheme.typography.bodyLarge
						)
						Text(
							effectText,
							Modifier.weight(1f),
							style = MaterialTheme.typography.bodyLarge
						)
					}
				}


			}
		}
	}
}

@Composable
fun TableHeader(
	headers: List<String>,
	textAlign: TextAlign = TextAlign.Center
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.drawBehind {
				val strokeWidth = 2.dp.toPx()
				//TOP BORDER
				drawLine(
					color = DarkWood,
					start = Offset(0f, 0f),
					end = Offset(size.width, 0f),
					strokeWidth = strokeWidth
				)
				//BOTTOM BORDER
				drawLine(
					color = DarkWood,
					start = Offset(0f, size.height),
					end = Offset(size.width, size.height),
					strokeWidth = strokeWidth
				)
				///LEFT BORDER
				drawLine(
					color = DarkWood,
					start = Offset(0f, 0f),
					end = Offset(0f, size.height),
					strokeWidth = strokeWidth
				)
				//RIGHT BORDER
				drawLine(
					color = DarkWood,
					start = Offset(size.width, 0f),
					end = Offset(size.width, size.height),
					strokeWidth = strokeWidth
				)
			}
			.padding(start = 8.dp)
	) {
		headers.forEach {
			Text(
				it,
				modifier = Modifier.weight(1f),
				textAlign = textAlign,
				fontWeight = FontWeight.Bold,
				style = MaterialTheme.typography.bodyLarge,
			)
		}
	}
}

@Preview("HildirQuestSection")
@Composable
fun PreviewHildirQuestSection() {
	ValheimVikiAppTheme {
		val pointsOfInterest = FakeData.pointOfInterest
		val materials = FakeData.generateFakeMaterials()
		HildirQuestSection(
			chestsLocations = pointsOfInterest,
			chestList = materials,
			onItemClick = {}
		)
	}
}


@Preview("SellItemsTable", showBackground = false)
@Composable
fun PreviewSellItemsTable() {
	val fakeMaterialList = FakeData.generateFakeMaterials()
	val headersSellTable = listOf(
		"Name", "Icon", "Price for One"
	)
	val sideBorder = Modifier.drawBehind {
		val strokeWidth = 2.dp.toPx()
		//BOTTOM BORDER
		drawLine(
			color = DarkWood,
			start = Offset(0f, size.height),
			end = Offset(size.width, size.height),
			strokeWidth = strokeWidth
		)
		///LEFT BORDER
		drawLine(
			color = DarkWood,
			start = Offset(0f, 0f),
			end = Offset(0f, size.height),
			strokeWidth = strokeWidth
		)
		//RIGHT BORDER
		drawLine(
			color = DarkWood,
			start = Offset(size.width, 0f),
			end = Offset(size.width, size.height),
			strokeWidth = strokeWidth
		)
	}
	ValheimVikiAppTheme {
		SellItemsTable(

			itemList = fakeMaterialList,
			headers = headersSellTable,
			modifier = sideBorder,
			onItemClick = {},
		)
	}
}

@Preview("ShopItemsTable", showBackground = false)
@Composable
fun PreviewShopItemsTable() {
	val fakeMaterialList = FakeData.generateFakeMaterials()
	val headers = listOf(
		"Name", "Icon", "Cost"
	)
	val sideBorder = Modifier.drawBehind {
		val strokeWidth = 2.dp.toPx()
		//BOTTOM BORDER
		drawLine(
			color = DarkWood,
			start = Offset(0f, size.height),
			end = Offset(size.width, size.height),
			strokeWidth = strokeWidth
		)
		///LEFT BORDER
		drawLine(
			color = DarkWood,
			start = Offset(0f, 0f),
			end = Offset(0f, size.height),
			strokeWidth = strokeWidth
		)
		//RIGHT BORDER
		drawLine(
			color = DarkWood,
			start = Offset(size.width, 0f),
			end = Offset(size.width, size.height),
			strokeWidth = strokeWidth
		)
	}
	ValheimVikiAppTheme {
		ShopItemsTable(
			onItemClick = {},
			itemList = fakeMaterialList,
			headers = headers,
			modifier = sideBorder,
		)
	}
}


@Preview(name = "PreviewNPCPage")
@Composable
fun PreviewNPCPage() {
	NPC(
		id = "1",
		category = "asd",
		subCategory = "sdasd",
		imageUrl = "sadasd",
		name = "sadsdd",
		description = "asdasd2",
		order = 2,
		biography = "ASDSDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
		location = "ASdasdaaaaaaasssssssssssssssssssssssssssssssssssssss",
	)
	remember { mutableStateOf(true) }
	ValheimVikiAppTheme {
		NpcDetailContent(
			onBack = {},
			onItemClick = {},
			uiState = fakeNpcDetailUiState
		)
	}
}


@Preview(name = "PreviewNPCPage")
@Composable
fun PreviewNPCPageSmal() {
	NPC(
		id = "1",
		category = "",
		subCategory = "sdasd",
		imageUrl = "sadasd",
		name = "sadsdd",
		description = "asdasd2",
		order = 2,
		biography = "",
		location = "",
	)
	remember { mutableStateOf(true) }
	ValheimVikiAppTheme {
		NpcDetailContent(
			onBack = {},
			onItemClick = {},
			uiState = NpcDetailUiState(
				npc = NPC(
					id = "npc_blacksmith",
					name = "Bjorn the Blacksmith",
					imageUrl = "https://example.com/images/npcs/blacksmith.png",
					description = "A sturdy dwarf who forges powerful weapons for travellers.",
					order = 1,
					category = "",
					subCategory = "",
					biography = "",
					location = "",
				),
				biome = Biome(
					id = "biome_plains",
					category = "Overworld",
					imageUrl = "https://example.com/images/biomes/plains.png",
					name = "Sunny Plains",
					description = "Rolling green fields with gentle hills and the occasional oak tree.",
					order = 0
				)
			)
		)
	}
}



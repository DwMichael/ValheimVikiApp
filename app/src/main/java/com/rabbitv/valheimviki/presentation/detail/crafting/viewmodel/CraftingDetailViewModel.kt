package com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.utils.Constants.CRAFTING_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CraftingDetailViewModel @Inject constructor(
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val craftingObjectId: String = checkNotNull(savedStateHandle[CRAFTING_KEY])
	

}
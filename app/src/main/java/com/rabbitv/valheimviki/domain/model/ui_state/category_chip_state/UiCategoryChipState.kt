package com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state

sealed class UiCategoryChipState<out A, out B, out C>(
    open val selectedCategory: A,
    open val selectedChip: B? = null
) {
    data class Loading<out A, out B>(
        override val selectedCategory: A,
        override val selectedChip: B? = null,
    ) : UiCategoryChipState<A, B, Nothing>(selectedCategory, selectedChip)

    data class Success<out A, out B, out C>(
        override val selectedCategory: A,
        override val selectedChip: B? = null,
        val list: List<C>
    ) : UiCategoryChipState<A, B, C>(selectedCategory, selectedChip)

    data class Error<out A, out B>(
        override val selectedCategory: A,
        override val selectedChip: B? = null,
        val message: String
    ) : UiCategoryChipState<A, B, Nothing>(selectedCategory, selectedChip)
}
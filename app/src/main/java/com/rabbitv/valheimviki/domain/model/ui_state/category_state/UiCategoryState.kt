package com.rabbitv.valheimviki.domain.model.ui_state.category_state

sealed class UiCategoryState<out C, out I>(
    open val selectedCategory: C
) {

    data class Loading<out C>(
        override val selectedCategory: C
    ) : UiCategoryState<C, Nothing>(selectedCategory)

    data class Success<out C, out I>(
        override val selectedCategory: C,
        val list: List<I>
    ) : UiCategoryState<C, I>(selectedCategory)

    data class Error<out C>(
        override val selectedCategory: C,
        val message: String
    ) : UiCategoryState<C, Nothing>(selectedCategory)
}
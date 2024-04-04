package com.sbdev.project.todo.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbdev.project.todo.data.FilterItem
import com.sbdev.project.todo.utils.getFilterList

@Preview(showBackground = true)
@Composable
fun FilterGroupView(
    modifier: Modifier = Modifier,
    list: List<FilterItem> = getFilterList(),
    selectedFilter: FilterItem? = null,
    onSelectedChanged: (FilterItem) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
    ) {
        items(list) {
            FilterItemView(
                it, isSelected = selectedFilter == it,
                onSelectionChanged = { item ->
                    onSelectedChanged(item)
                },
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}
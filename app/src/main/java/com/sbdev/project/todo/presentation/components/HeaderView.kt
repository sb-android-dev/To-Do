package com.sbdev.project.todo.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Preview(showBackground = true)
@Composable
fun HeaderView(
    accountDp: String? = null,
    onLogOut: () -> Unit = {},
    searchText: String = "",
    onSearchChanged: (String) -> Unit = {}
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
//                        onLogOut.invoke()
                        isContextMenuVisible = true
                    }
                    .size(48.dp),
                model = accountDp ?: "https://cdn.iconscout.com/icon/free/png-512/free-google-160-189824.png?f=webp&w=512",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                modifier = Modifier.weight(1f),
                value = searchText,
                textStyle = MaterialTheme.typography.titleMedium,
                onValueChange = onSearchChanged,
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (searchText.isEmpty()) {
                        Text(
                            text = "Search Task",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.outline
                            ),
                        )
                    }

                    innerTextField()
                }
            )

            if (searchText.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            onSearchChanged.invoke("")
                        }
                        .size(48.dp)
                        .padding(12.dp),
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Clear search",
                )
            }
        }
        
        DropdownMenu(expanded = isContextMenuVisible, onDismissRequest = { isContextMenuVisible = false }) {
            DropdownMenuItem(text = { Text(text = "Log out") }, onClick = { onLogOut.invoke() })
        }
    }
}
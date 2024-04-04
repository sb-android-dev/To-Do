package com.sbdev.project.todo.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbdev.project.todo.R
import com.sbdev.project.todo.data.ToDoItem
import com.sbdev.project.todo.ui.theme.greenStroke
import com.sbdev.project.todo.ui.theme.pastelGreen
import com.sbdev.project.todo.ui.theme.pastelYellow

@Preview
@Composable
fun ToDoItemView(
    modifier: Modifier = Modifier,
    item: ToDoItem = ToDoItem(
        title = "To-Do Title To-Do Title To-Do Title To-Do Title",
        description = "To-Do description To-Do description To-Do description To-Do description",
        status = "done"
    ),
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = when (item.status) {
                "in_progress" -> pastelYellow
                "done" -> pastelGreen
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when (item.status) {
                "done" -> greenStroke
                else -> MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 6.dp, vertical = 8.dp),
        ) {
            val iconResource = when (item.status) {
                "done" -> painterResource(id = R.drawable.round_task_alt)
                else -> painterResource(id = R.drawable.round_access_time)

            }

            Icon(
                painter = iconResource,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Text(text = item.description, style = MaterialTheme.typography.bodySmall)

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable {
                                onEdit.invoke()
                            }
                            .size(48.dp)
                            .padding(12.dp),
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit To-Do",
                    )

                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable {
                                onDelete.invoke()
                            }
                            .size(48.dp)
                            .padding(12.dp),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete To-Do",
                    )
                }
            }
        }
    }
}
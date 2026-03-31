package woowacourse.kanban.ui.board.common

import androidx.compose.runtime.Composable
import woowacourse.kanban.domain.card.CardTaskState

@Composable
fun CardTaskState.toDisplayText(): String {
    return when (this) {
        CardTaskState.TODO -> "To Do"
        CardTaskState.IN_PROGRESS -> "In Progress"
        CardTaskState.DONE -> "Done"
    }
}
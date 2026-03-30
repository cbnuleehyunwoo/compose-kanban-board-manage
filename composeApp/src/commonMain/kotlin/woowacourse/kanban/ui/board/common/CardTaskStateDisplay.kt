package woowacourse.kanban.ui.board.common

import androidx.compose.runtime.Composable
import woowacourse.kanban.domain.card.CardTaskStatus

@Composable
fun CardTaskStatus.toDisplayText(): String {
    return when (this) {
        CardTaskStatus.TODO -> "To Do"
        CardTaskStatus.IN_PROGRESS -> "In Progress"
        CardTaskStatus.DONE -> "Done"
    }
}

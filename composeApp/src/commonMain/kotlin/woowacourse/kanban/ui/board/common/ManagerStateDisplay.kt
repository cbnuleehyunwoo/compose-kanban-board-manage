package woowacourse.kanban.ui.board.common

import androidx.compose.runtime.Composable
import woowacourse.kanban.domain.card.CardManagerState

@Composable
fun CardManagerState.toDisplayText(): String {
    return when (this) {
        CardManagerState.DINO -> "DINO"
        CardManagerState.FAMES -> "FAMES"
    }
}

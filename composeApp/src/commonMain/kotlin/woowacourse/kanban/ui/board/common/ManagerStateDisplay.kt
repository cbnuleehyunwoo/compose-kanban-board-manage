package woowacourse.kanban.ui.board.common

import androidx.compose.runtime.Composable
import woowacourse.kanban.domain.card.CardManagerStatus

@Composable
fun CardManagerStatus.toDisplayText(): String {
    return when (this) {
        CardManagerStatus.DINO -> "DINO"
        CardManagerStatus.FAMES -> "FAMES"
    }
}

package woowacourse.kanban.domain.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskStatus
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class BoardStateHolder(
    private val board: () -> Board,
    private val onBoardChange: (Board) -> Unit,
    private val onShowSnackbar: (String) -> Unit
) {

    var draggedTask by mutableStateOf<Card?>(null)
        private set
    var currentDragPosition by mutableStateOf<Offset?>(null)
        private set
    val columnBounds = mutableStateMapOf<CardTaskStatus, Rect>()

    val currentBoard: Board get() = board()

    fun updateColumnBounds(
        status: CardTaskStatus,
        rect: Rect,
    ) {
        columnBounds[status] = rect
    }

    fun onDragStart(card: Card) {
        draggedTask = card
    }

    fun onDragChange(offset: Offset) {
        currentDragPosition = offset
    }


    fun onDragEnd() {
        val dropPosition = currentDragPosition
        val targetStatus = columnBounds.entries
            .firstOrNull { (_, rect) -> dropPosition?.let { rect.contains(it) } == true }?.key

        draggedTask?.let { task ->
            if (targetStatus != null && task.taskState != targetStatus) {
                val updatedBoard = board().withTaskState(
                    cardId = task.id,
                    targetState = targetStatus
                )
                onBoardChange(updatedBoard)
                onShowSnackbar("태스크가 이동되었습니다.")
            }
        }
        clearDrag()
    }

    fun isDropTarget(status: CardTaskStatus): Boolean {
        val pos = currentDragPosition ?: return false
        return columnBounds[status]?.contains(pos) ?: false
    }

    fun clearDrag() {
        draggedTask = null
        currentDragPosition = null
    }
}

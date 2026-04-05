package woowacourse.kanban.domain.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.card.MoveFailureReason
import woowacourse.kanban.domain.card.MoveResult

class BoardStateHolder(
    private val board: () -> Board,
    private val onBoardChange: (Board) -> Unit,
    private val onShowSnackbar: (String) -> Unit,
    private val onCardClick: (Card) -> Unit = {},
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

    fun onClick(card: Card) {
        onCardClick(card)
    }

    fun onDragChange(offset: Offset) {
        currentDragPosition = offset
    }


    fun onDragEnd() {
        val dropPosition = currentDragPosition
        val targetStatus = columnBounds.entries
            .firstOrNull { (_, rect) -> dropPosition?.let { rect.contains(it) } == true }?.key

        val taskId = draggedTask?.id
        val targetTask = currentBoard.cardList.find { it.id == taskId }

        targetTask?.let { task ->
            if (targetStatus != null && task.taskState != targetStatus) {
                when(val moveResult = task.moveTo(targetStatus = targetStatus)) {
                    is MoveResult.Success -> {
                        onBoardChange(currentBoard.updateCard(updatedCard = moveResult.updatedCard))
                        onShowSnackbar("태스크가 이동되었습니다.")
                    }
                    is MoveResult.Failure -> {
                        val message = when(moveResult.reason) {
                            MoveFailureReason.INVALID_TRANSITION -> "해당 상태로 옮길 수 없습니다."
                            MoveFailureReason.INVALID_MANAGER -> "담당자를 지정해야 상태를 옮길 수 있습니다."
                        }
                        onShowSnackbar(message)
                    }
                }
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

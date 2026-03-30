package woowacourse.kanban.domain.project

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.board.CardForm
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskStatus

class KanbanState(
    initialProject: Project,
    val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    val currentBoard: Board get() = project.selectedBoard
    var project by  mutableStateOf(initialProject)
        private set

    var isCreationDialogVisible by mutableStateOf(false)
        private set

    var cardForm by mutableStateOf(CardForm())
        private set

    var draggedTask by mutableStateOf<Card?>(null)
        private set
    var currentDragPosition by mutableStateOf<Offset?>(null)
        private set
    val columnBounds = mutableStateMapOf<CardTaskStatus, Rect>()


    fun switchBoard(index: Int) {
        project = project.switchBoard(index)
    }

    fun addCard(card: Card) {
        project = project.withUpdateBoard(project.selectedBoard + card)
        showSnackbar("새로운 태스크가 추가되었습니다.")
    }

    fun updateBoard(board: Board): Boolean {
        val oldProject = project
        project = project.withUpdateBoard(board)
        return oldProject != project
    }

    fun showCreationDialog() {
        cardForm = CardForm()
        isCreationDialogVisible = true
    }

    fun closeCreationDialog() {
        isCreationDialogVisible = false
        showSnackbar("새 태스크 추가가 취소되었습니다.")
    }

    fun updateCardForm(newForm: CardForm) {
        cardForm = newForm
    }

    fun updateColumnBounds(status: CardTaskStatus, rect: Rect) {
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
                moveCard(project.selectedBoard.withUpdatedTaskState(task.id, targetStatus))
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

    private fun moveCard(updatedBoard: Board) {
        project = project.withUpdateBoard(updatedBoard)
        showSnackbar("태스크가 이동되었습니다.")
    }

    private fun showSnackbar(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

}

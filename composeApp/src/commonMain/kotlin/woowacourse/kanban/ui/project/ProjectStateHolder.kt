package woowacourse.kanban.ui.project

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.project.Project
import woowacourse.kanban.ui.board.BoardStateHolder
import woowacourse.kanban.ui.dialog.DialogStateHolder

class ProjectStateHolder(
    initialProject: Project,
    val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    var project by mutableStateOf(initialProject)
        private set
    var boardState by mutableStateOf(
        BoardStateHolder(
            board = { project.selectedBoard },
            onBoardChange = { newBoard ->
                project = project.withBoard(newBoard)
            },
            onShowSnackbar = { showSnackbar(it) },
            onCardClick = { card -> dialogState.setCard(card) },
        ),
    )
    
    val dialogState by mutableStateOf(
        DialogStateHolder(
            onCardCreate = { newCard ->
                project = project.withBoard(project.selectedBoard + newCard)
                showSnackbar("새로운 태스크가 추가되었습니다.")
            },
            onCardUpdate = { newCard ->
                val updatedBoard = project.selectedBoard.updateCard(newCard)
                project = project.withBoard(updatedBoard)
                showSnackbar("태스크가 수정되었습니다.")
            },
            onCardDelete = { card ->
                val beforeBoard = project.selectedBoard
                val afterBoard = beforeBoard - card

                if (beforeBoard === afterBoard) {
                    showSnackbar("해당 상태에서는 태스크 삭제가 불가합니다.")
                } else {
                    project = project.withBoard(afterBoard)
                    showSnackbar("태스크가 삭제되었습니다.")
                }
            },
            onShowSnackbar = { message ->  showSnackbar(message) },
        ),
    )


    fun switchBoard(index: Int) {
        project = project.switchBoard(index)
    }

    fun showEditDialog(card: Card) {
        dialogState.showEditDialog(card)
    }

    fun showCreationDialog() {
        dialogState.showCreationDialog()
    }

    private fun showSnackbar(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "X",
                duration = SnackbarDuration.Short
            )
        }
    }
}

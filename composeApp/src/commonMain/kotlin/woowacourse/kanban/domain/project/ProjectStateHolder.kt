package woowacourse.kanban.domain.project

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import woowacourse.kanban.domain.board.BoardStateHolder
import woowacourse.kanban.domain.dialog.DialogStateHolder
import woowacourse.kanban.domain.dialog.EditDialogStateHolder

class ProjectStateHolder(
    initialProject: Project,
    val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    var project by mutableStateOf(initialProject)
        private set
    var boardState by mutableStateOf(
        BoardStateHolder(
            board = {project.selectedBoard},
            onBoardChange = { newBoard ->
                project = project.withBoard(newBoard)
            },
            onShowSnackbar = { showSnackbar(it) },
            onCardClick = {card -> editDialogState.setCard(card)}
        ),
    )

    val editDialogState by mutableStateOf(
        EditDialogStateHolder(
            onCardUpdate = { newCard ->
                val updatedBoard = project.selectedBoard.updateCard(newCard)
                project = project.withBoard(updatedBoard)
                showSnackbar("태스크가 수정되었습니다.")
            },
            onCardDelete = { card ->
                project = project.withBoard(project.selectedBoard - card)
                showSnackbar("태스크가 삭제되었습니다.")
            },

        )
    )
    val creationDialogState by mutableStateOf(
        DialogStateHolder(
            onCardCreate = { newCard ->
                project = project.withBoard(project.selectedBoard + newCard)
                showSnackbar("새로운 태스크가 추가되었습니다.")
            },
            onCancel = { showSnackbar("태스크 추가가 취소되었습니다.") },
        ),
    )


    fun switchBoard(index: Int) {
        project = project.switchBoard(index)
    }

    fun showEditDialog() {
        editDialogState.showEditDialog()
    }

    fun showCreationDialog() {
        creationDialogState.showCreationDialog()
    }

    private fun showSnackbar(message: String) {
        scope.launch { snackbarHostState.showSnackbar(
            message = message,
            actionLabel = "X",
        ) }
    }
}

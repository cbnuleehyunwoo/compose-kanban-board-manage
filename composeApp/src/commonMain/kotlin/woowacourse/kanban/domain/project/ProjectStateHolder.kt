package woowacourse.kanban.domain.project

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import woowacourse.kanban.domain.board.BoardStateHolder
import woowacourse.kanban.domain.dialog.DialogStateHolder

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
        ),
    )

    val dialogState by mutableStateOf(
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

    fun showCreationDialog() {
        dialogState.showCreationDialog()
    }

    private fun showSnackbar(message: String) {
        scope.launch { snackbarHostState.showSnackbar(message) }
    }
}

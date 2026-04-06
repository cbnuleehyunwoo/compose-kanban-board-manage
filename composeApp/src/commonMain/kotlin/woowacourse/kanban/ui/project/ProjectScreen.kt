package woowacourse.kanban.ui.project

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.project.Project
import woowacourse.kanban.ui.board.BoardScreen

@Composable
fun ProjectScreen(
    modifier: Modifier = Modifier,
    initialProject: Project = sampleProject(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state = remember {
        ProjectStateHolder(
            initialProject = initialProject,
            snackbarHostState = snackbarHostState,
            scope = scope,
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = state.snackbarHostState)
        },
    ) { innerPadding ->
        Row(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            ProjectTab(
                project = state.project,
                onBoardSelected = { index ->
                    state.switchBoard(
                        index = index,
                    )
                },
            )
            BoardScreen(
                dialogState = state.dialogState,
                boardState = state.boardState,
                onShowCreationDialog = { state.showCreationDialog() },
                onShowEditDialog = { state.showEditDialog(card = it) },
            )
        }
    }
}

/* Preview */

private fun sampleProject(): Project {
    return Project(
        boardList = listOf(
            Board(
                boardTitle = "Compose1",
                cardList = listOf(
                    Card.create(
                        title = "제목1",
                        content = "",
                        tags = listOf(
                            "태그1",
                            "태그2",
                        ),
                        manager = CardManagerStatus.NONE,
                        state = CardTaskStatus.TODO,
                    ),
                    Card.create(
                        title = "제목2",
                        content = "",
                        tags = listOf(
                            "태그1",
                            "태그2",
                        ),
                        manager = CardManagerStatus.DINO,
                        state = CardTaskStatus.IN_PROGRESS,
                    ),
                    Card.create(
                        title = "제목3",
                        content = "",
                        tags = listOf(
                            "태그1",
                            "태그2",
                        ),
                        manager = CardManagerStatus.DINO,
                        state = CardTaskStatus.REVIEW,
                    ),
                    Card.create(
                        title = "제목4",
                        content = "",
                        tags = listOf(
                            "태그1",
                            "태그2",
                            "태그3",
                        ),
                        manager = CardManagerStatus.DINO,
                        state = CardTaskStatus.DONE,
                    ),
                ),
            ),
            Board(
                boardTitle = "Compose2",
                cardList = listOf(
                    Card.create(
                        title = "제목4",
                        content = "",
                        tags = listOf(
                            "태그1",
                            "태그2",
                        ),
                        manager = CardManagerStatus.DINO,
                        state = CardTaskStatus.DONE,
                    ),
                ),
            ),
            Board(
                boardTitle = "Compose3너무너무긴제목",
                cardList = emptyList(),
            ),
        ),
        selectedBoardIndex = 0,
        projectTitle = "프로젝트 제목",
        projectDescription = "프로젝트 설명",
    )
}

@Preview(
    showBackground = true,
    widthDp = 1551,
    heightDp = 909,
)
@Composable
private fun ProjectScreenPreview() {
    ProjectScreen()
}

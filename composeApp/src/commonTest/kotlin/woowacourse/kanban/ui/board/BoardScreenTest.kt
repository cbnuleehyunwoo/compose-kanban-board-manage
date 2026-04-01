package woowacourse.kanban.ui.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.board.BoardStateHolder
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.dialog.DialogStateHolder
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BoardScreenTest {

    @Test
    fun `보드에 보드 제목, 완료율, 태스크 생성 버튼, 프로그레스 바, ToDo, In Progress, Done Column이 노출된다`() = runComposeUiTest {
        // Given
        val initialBoard = Board(boardTitle = "Compose Desktop 칸반 보드")
        setContent {
            var board by remember { mutableStateOf(initialBoard) }
            val boardState = BoardStateHolder(
                board = { board },
                onBoardChange = { board = it },
                onShowSnackbar = {},
            )
            val dialogState = DialogStateHolder(
                onCardCreate = {},
                onCancel = {},
            )
            BoardScreen(
                boardState = boardState,
                dialogState = dialogState,
            )
        }

        onNodeWithTag("보드 제목").assertExists()
        onNodeWithText("Compose Desktop 칸반 보드 ").assertExists()
        onNodeWithText("완료율: 0% (0/0)").assertExists()
        onNodeWithTag("새 태스크 생성 버튼").assertExists()
        onNodeWithTag("프로그레스 바").assertExists()
        onNodeWithText("To Do").assertExists()
        onNodeWithText("In Progress").assertExists()
        onNodeWithText("Done").assertExists()
    }

    @Test
    fun `새 태스크 생성 버튼을 누르면 카드 생성 모달이 나타난다`() = runComposeUiTest {
        // Given
        val initialBoard = Board(boardTitle = "Compose Desktop 칸반 보드")
        setContent {
            var board by remember { mutableStateOf(initialBoard) }
            val boardState = BoardStateHolder(
                board = { board },
                onBoardChange = { board = it },
                onShowSnackbar = {},
            )
            val dialogState = DialogStateHolder(
                onCardCreate = {},
                onCancel = {},
            )
            BoardScreen(
                boardState = boardState,
                dialogState = dialogState,
                onShowCreationDialog = { dialogState.showCreationDialog() },
            )
        }
        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("생성 모달 열림").assertExists()
    }

    @Test
    fun `카드 목록이 표시된다`() = runComposeUiTest {
        // Given
        val testCard = Card.create(
            title = "테스트 카드",
            content = "테스트 내용",
            tags = listOf("태그"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )
        val boardWithCard = Board(cardList = listOf(testCard))

        setContent {
            var board by remember { mutableStateOf(boardWithCard) }
            val boardState = BoardStateHolder(
                board = { board },
                onBoardChange = { board = it },
                onShowSnackbar = {},
            )
            val dialogState = DialogStateHolder(
                onCardCreate = {},
                onCancel = {},
            )
            BoardScreen(
                boardState = boardState,
                dialogState = dialogState,
            )
        }

        // Then
        onNodeWithText("테스트 카드").assertExists()
    }

    @Test
    fun `태스크 생성 후 완료율 텍스트가 변경된다`() = runComposeUiTest {
        val initialBoard = Board(boardTitle = "Compose Desktop 칸반 보드")
        setContent {
            var board by remember { mutableStateOf(initialBoard) }
            val boardState = BoardStateHolder(
                board = { board },
                onBoardChange = { board = it },
                onShowSnackbar = {},
            )
            val dialogState = DialogStateHolder(
                onCardCreate = { newCard ->
                    board += newCard
                },
                onCancel = {},
            )
            BoardScreen(
                boardState = boardState,
                dialogState = dialogState,
                onShowCreationDialog = { dialogState.showCreationDialog() },
            )
        }
        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("titleTextField").performTextInput("완료 카드")
        onNodeWithTag("descriptionTextField").performTextInput("설명")
        onNodeWithTag("tagTextField").performTextInput("태그")

        onNodeWithTag("Done").performClick()
        onNodeWithText("생성").performClick()

        onNodeWithTag("완료율").assertTextContains("완료율: 100% (1/1)")
    }

    @Test
    fun `태스크 생성 후 Snackbar가 노출된다`() = runComposeUiTest {
        val initialBoard = Board(boardTitle = "Compose Desktop 칸반 보드")
        setContent {
            var board by remember { mutableStateOf(initialBoard) }
            val boardState = BoardStateHolder(
                board = { board },
                onBoardChange = { board = it },
                onShowSnackbar = {},
            )
            val dialogState = DialogStateHolder(
                onCardCreate = {},
                onCancel = {},
            )
            BoardScreen(
                boardState = boardState,
                dialogState = dialogState,
            )
        }

        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("titleTextField").performTextInput("새 카드")
        onNodeWithTag("descriptionTextField").performTextInput("설명")
        onNodeWithTag("tagTextField").performTextInput("태그")
        onNodeWithText("생성").performClick()

        onNodeWithText("새로운 태스크가 추가되었습니다.").assertExists()
    }
}

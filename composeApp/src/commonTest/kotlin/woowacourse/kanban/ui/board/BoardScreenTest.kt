package woowacourse.kanban.ui.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.board.BoardStateHolder
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.dialog.DialogStateHolder
import woowacourse.kanban.domain.dialog.EditDialogStateHolder
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
            val editDialogState = remember {
                EditDialogStateHolder(
                    onCardUpdate = {},
                    onCardDelete = {}
                )
            }
            BoardScreen(
                boardState = boardState,
                createDialogState = dialogState,
                editDialogState = editDialogState
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
            val editDialogState = remember {
                EditDialogStateHolder(
                    onCardUpdate = {},
                    onCardDelete = {},
                )
            }
            BoardScreen(
                boardState = boardState,
                createDialogState = dialogState,
                onShowCreationDialog = { dialogState.showCreationDialog() },
                onShowEditDialog = { },
                editDialogState = editDialogState,
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
            val editDialogState = remember {
                EditDialogStateHolder(
                    onCardUpdate = {},
                    onCardDelete = {},
                )
            }
            BoardScreen(
                boardState = boardState,
                createDialogState = dialogState,
                editDialogState = editDialogState,
            )
        }

        // Then
        onNodeWithText("테스트 카드").assertExists()
    }

    @Test
    fun `태스크를 To Do에서 Done 컬럼으로 드래그 앤 드롭하면 카드가 이동한다`() = runComposeUiTest {
        // Given
        val testCard = Card.create(
            title = "드래그 테스트 카드",
            content = "내용",
            tags = listOf("태그"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )
        val initialBoard = Board(cardList = listOf(testCard))

        setContent {
            var board by remember { mutableStateOf(initialBoard) }
            val boardState = remember {
                BoardStateHolder(
                    board = { board },
                    onBoardChange = { board = it },
                    onShowSnackbar = {},
                )
            }
            val dialogState = remember {
                DialogStateHolder(
                    onCardCreate = {},
                    onCancel = {},
                )
            }
            val editDialogState = remember {
                EditDialogStateHolder(
                    onCardUpdate = {},
                    onCardDelete = {},
                )
            }
            BoardScreen(
                boardState = boardState,
                createDialogState = dialogState,
                editDialogState = editDialogState,
            )
        }

        onNodeWithTag("${CardTaskStatus.TODO.name}_개수").assertTextContains("1")
        onNodeWithTag("${CardTaskStatus.IN_PROGRESS.name}_개수").assertTextContains("0")

        // When
        val cardNode = onNodeWithTag("카드_${testCard.id}")
        val inProgressColumnNode = onNodeWithTag(CardTaskStatus.IN_PROGRESS.name)
        val inProgressColumnBounds = inProgressColumnNode.fetchSemanticsNode().boundsInRoot

        cardNode.performTouchInput {
            down(center)
            advanceEventTime(viewConfiguration.longPressTimeoutMillis + 100)

            moveTo(inProgressColumnBounds.center)
            advanceEventTime(100)
            up()
        }

        // Then
        onNodeWithTag("${CardTaskStatus.TODO.name}_개수").assertTextContains("0")
        onNodeWithTag("${CardTaskStatus.IN_PROGRESS.name}_개수").assertTextContains("1")
    }

    @Test
    fun `태스크 카드 클릭 후 다이얼로그에서 삭제 버튼을 눌렀을 때 해당 태스크 카드가 삭제된다`() = runComposeUiTest {
        // given
        val testCard = Card.create(
            title = "삭제 테스트 카드",
            content = "내용",
            tags = listOf("태그"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )
        val initialBoard = Board(cardList = listOf(testCard))

        setContent {
            var board by remember { mutableStateOf(initialBoard) }

            val editDialogState = remember {
                EditDialogStateHolder(
                    onCardUpdate = { updated -> board = board.updateCard(updated) },
                    onCardDelete = { target -> board -= target },
                )
            }

            val boardState = remember {
                BoardStateHolder(
                    board = { board },
                    onBoardChange = { board = it },
                    onShowSnackbar = {},
                    onCardClick = { card -> editDialogState.setCard(card) },
                )
            }

            val dialogState = remember {
                DialogStateHolder(
                    onCardCreate = {},
                    onCancel = {},
                )
            }

            BoardScreen(
                boardState = boardState,
                createDialogState = dialogState,
                editDialogState = editDialogState,
                onShowEditDialog = { editDialogState.showEditDialog() },
            )
        }

        // when & then
        onNodeWithTag("카드_${testCard.id}").performClick()
        onNodeWithTag("수정 모달 열림").assertIsDisplayed()
        onNodeWithText("삭제").performClick()
        onNodeWithTag("카드_${testCard.id}").assertDoesNotExist()
    }
}

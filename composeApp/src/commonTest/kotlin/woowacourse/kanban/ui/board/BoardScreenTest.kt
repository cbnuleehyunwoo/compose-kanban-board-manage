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
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BoardScreenTest {

    @Test
    fun `보드에 보드 제목, 완료율, 태스크 생성 버튼, 프로그레스 바, ToDo, In Progress, Done Column이 노출된다`() = runComposeUiTest {
        // Given
        setContent {
            BoardScreen(
                board = Board(),
                showCardCreationPanel = false,
                onAddCard = {},
                onShowCardCreationPanelChange = {},
                onBoardChange = { true },
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
        setContent {
            var board by remember { mutableStateOf(Board()) }
            var showCardCreationPanel by remember { mutableStateOf(false) }

            BoardScreen(
                board = board,
                showCardCreationPanel = showCardCreationPanel,
                onAddCard = { newCard -> board += newCard },
                onShowCardCreationPanelChange = { showCardCreationPanel = it },
                onBoardChange = { true },
            )
        }

        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("생성 모달 열림").assertExists()
    }

    @Test
    fun `카드 목록이 표시된다`() = runComposeUiTest {
        val board = Board(
            listOf(
                Card.create(
                    title = "테스트 카드",
                    content = "테스트 내용",
                    tags = listOf("태그"),
                    manager = CardManagerStatus.DINO,
                    state = CardTaskStatus.TODO,
                ),
            ),
        )

        setContent {
            BoardScreen(
                board = board,
                showCardCreationPanel = false,
                onAddCard = {},
                onShowCardCreationPanelChange = {},
                onBoardChange = { true },
            )
        }

        onNodeWithText("테스트 카드").assertExists()
    }

    @Test
    fun `태스크 생성 후 완료율 텍스트가 변경된다`() = runComposeUiTest {
        setContent {
            var board by remember { mutableStateOf(Board()) }
            var showCardCreationPanel by remember { mutableStateOf(false) }

            BoardScreen(
                board = board,
                showCardCreationPanel = showCardCreationPanel,
                onAddCard = { newCard -> board += newCard },
                onShowCardCreationPanelChange = { showCardCreationPanel = it },
                onBoardChange = { true },
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
        setContent {
            var board by remember { mutableStateOf(Board()) }
            var showCardCreationPanel by remember { mutableStateOf(false) }

            BoardScreen(
                board = board,
                showCardCreationPanel = showCardCreationPanel,
                onAddCard = { newCard -> board += newCard },
                onShowCardCreationPanelChange = { showCardCreationPanel = it },
                onBoardChange = { true },
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

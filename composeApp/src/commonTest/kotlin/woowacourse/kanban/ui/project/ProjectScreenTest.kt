package woowacourse.kanban.ui.project

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.project.Project
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProjectScreenTest {

    @Test
    fun `프로젝트 탭의 제목, 설명이 표시된다`() = runComposeUiTest {
        setContent {
            ProjectScreen()
        }

        onNodeWithText("프로젝트 제목").assertExists()
        onNodeWithText("프로젝트 설명").assertExists()
    }

    @Test
    fun `보드 간 전환 후 해당 보드의 태스크가 표시된다`() = runComposeUiTest {
        setContent {
            ProjectScreen()
        }

        onNodeWithText("Compose2").performClick()
        onNodeWithText("제목4").assertExists()
    }

    @Test
    fun `태스크 생성 후 Snackbar가 노출된다`() = runComposeUiTest {
        val initialBoard = Board(boardTitle = "Compose Desktop 칸반 보드")
        setContent {
            ProjectScreen(
                initialProject = Project(
                    boardList = listOf(
                        initialBoard,
                    ),
                ),
            )
        }

        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("titleTextField").performTextInput("새 카드")
        onNodeWithTag("descriptionTextField").performTextInput("설명")
        onNodeWithTag("tagTextField").performTextInput("태그")
        onNodeWithText("생성").performClick()

        onNodeWithText("새로운 태스크가 추가되었습니다.").assertExists()
    }


    @Test
    fun `태스크 수정 시 스낵바가 표시된다`() = runComposeUiTest {
        // given
        val initialBoard = Board(
            boardTitle = "Compose Desktop 칸반 보드",
            cardList = listOf(Card.create(
                title = "제목",
                content = "",
                tags = listOf(),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            )),
        )
        setContent {
            ProjectScreen(
                initialProject = Project(
                    boardList = listOf(
                        initialBoard,
                    ),
                ),
            )
        }
        // when & then
        onNodeWithTag("카드_${initialBoard.cardList.first().id}").performClick()
        onNodeWithTag("titleTextField").performTextReplacement("수정된 제목")
        onNodeWithText("수정").performClick()
        onNodeWithText("수정된 제목", useUnmergedTree = true).assertExists()
        onNodeWithText("태스크가 수정되었습니다.").assertExists()
    }

    @Test
    fun `태스크 삭제 시 스낵바가 표시된다`() = runComposeUiTest {
        // given
        val initialBoard = Board(
            boardTitle = "Compose Desktop 칸반 보드",
            cardList = listOf(Card.create(
                title = "삭제 테스트",
                content = "",
                tags = listOf(),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            )),
        )
        setContent {
            ProjectScreen(
                initialProject = Project(
                    boardList = listOf(
                        initialBoard,
                    ),
                ),
            )
        }
        // when & then
        onNodeWithTag("카드_${initialBoard.cardList.first().id}").performClick()
        onNodeWithText("삭제").performClick()
        onNodeWithText("삭제 테스트").assertDoesNotExist()
        onNodeWithText("태스크가 삭제되었습니다.").assertExists()
    }

    @Test
    fun `불가능한 전이 시 스낵바가 표시된다`() = runComposeUiTest {
        // given
        val initialBoard = Board(
            boardTitle = "Compose Desktop 칸반 보드",
            cardList = listOf(Card.create(
                title = "전이 불가 스낵바 테스트",
                content = "",
                tags = listOf(),
                manager = CardManagerStatus.NONE,
                state = CardTaskStatus.TODO,
            )),
        )
        setContent {
            ProjectScreen(
                initialProject = Project(
                    boardList = listOf(
                        initialBoard,
                    ),
                ),
            )
        }

        val cardNode = onNodeWithTag("카드_${initialBoard.cardList.first().id}")
        val inProgressColumnNode = onNodeWithTag(CardTaskStatus.TODO.name)
        val inProgressColumnBounds = inProgressColumnNode.fetchSemanticsNode().boundsInRoot

        // when
        cardNode.performTouchInput {
            down(center)
            advanceEventTime(viewConfiguration.longPressTimeoutMillis + 100)

            moveTo(inProgressColumnBounds.center)
            advanceEventTime(100)
            up()
        }

        // then
        onNodeWithText("담당자를 지정해야 상태를 옮길 수 있습니다.").assertExists()
    }
}

package woowacourse.kanban.ui.project

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
    fun `태스크를 다른 컬럼으로 옮기면 스낵바가 표시된다`() = runComposeUiTest {
        setContent {
            ProjectScreen(
                initialProject = Project(
                    boardList = listOf(
                        Board(
                            listOf(
                                Card.create(
                                    title = "드래그테스트",
                                    content = "TODO 에서 DONE 으로 이동",
                                    tags = listOf("드래그"),
                                    manager = CardManagerStatus.DINO,
                                    state = CardTaskStatus.TODO,
                                ),
                            ),
                        ),
                    ),
                ),
            )
        }

        onNodeWithTag("카드_드래그테스트").performTouchInput {
            down(center)
            advanceEventTime(viewConfiguration.longPressTimeoutMillis + 100)
            moveTo(onNodeWithText("Done").fetchSemanticsNode().positionInRoot)
            advanceEventTime(1000)
            up()
        }

        waitForIdle()
        onNodeWithText("태스크가 이동되었습니다.").assertExists()
    }
}

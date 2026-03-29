package woowacourse.kanban.domain.project

import androidx.compose.ui.test.ExperimentalTestApi
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProjectTest {

    @Test
    fun `프로젝트는 1개 이상의 보드를 가진다`() {
        val project = Project(
            projectTitle = "제목",
            projectDescription = "내용",
        )
        assertThat(project.boards.size).isEqualTo(1)
    }

    @Test
    fun `초기 프로젝트 생성 시 보드를 추가할 수 있다`() {
        val boardTitle: String = "abcd"
        val board: Board = Board(boardTitle = boardTitle)
        val project: Project = Project(
            boardList = listOf(board),
            selectedBoardIndex = 0,
            projectTitle = "프로젝트 제목",
            projectDescription = "프로젝트 설명",
        )

        assertThat(project.selectedBoard.title).isEqualTo(boardTitle)
    }

    @Test
    fun `프로젝트 내 보드 간 전환이 가능하다`() {
        val boardTitles = listOf("abcd", "efgh")
        val oldBoard: Board = Board(boardTitle = boardTitles[0])
        val newBoard: Board = Board(boardTitle = boardTitles[1])

        var project: Project = Project(
            boardList = listOf(oldBoard, newBoard),
            selectedBoardIndex = 0,
            projectTitle = "프로젝트 제목",
            projectDescription = "프로젝트 설명",
        )

        project = project.switchBoard(1)
        assertThat(project.selectedBoard.title).isEqualTo(boardTitles[1])
    }
}

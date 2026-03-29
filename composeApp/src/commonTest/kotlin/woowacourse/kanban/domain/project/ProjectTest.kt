package woowacourse.kanban.domain.project

import androidx.compose.ui.test.ExperimentalTestApi
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProjectTest {

    @Test
    fun `프로젝트가 1개 이상의 보드를 가진다`() {
        val project = Project(
            projectTitle = "제목",
            projectDescription = "내용",
        )
        assertThat(project.boards.size).isEqualTo(1)
    }






    @Test
    fun `프로젝트가 1개 이상의 보드를 가진다1`() {
        val boardTitle: String = "abcd"
        val board: Board = Board(boardTitle = boardTitle)
        val project: Project = Project(
            boardList = listOf(board),
            selectedBoardIndex = 0,
            projectTitle = "프로젝트 제목",
            projectDescription = "프로젝트 설명"
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
            projectDescription = "프로젝트 설명"
        )

        project = project.switchBoard(1)
        assertThat(project.selectedBoard.title).isEqualTo(boardTitles[1])
    }

    @Test
    fun `태스크를 옮기면 태스크 상태가 변경된다`() {
        var board: Board = Board()
        val oldCard: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )
        board += oldCard
        assertThat(board.toDoTaskCount).isEqualTo(1)
        assertThat(board.doneTaskCount).isEqualTo(0)
        val newCard: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.DONE,
        )

        board += newCard
        board -= oldCard
        assertThat(board.toDoTaskCount).isEqualTo(0)
        assertThat(board.doneTaskCount).isEqualTo(1)
    }

    @Test
    fun `태스크를 옮기면 태스크 완료율이 변경된다`() {
        var board: Board = Board()
        val oldCard: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )
        board += oldCard
        assertThat(board.completionPercentage).isEqualTo(0)
        val newCard: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.DONE,
        )

        board += newCard
        board -= oldCard
        assertThat(board.completionPercentage).isEqualTo(100)
    }
}

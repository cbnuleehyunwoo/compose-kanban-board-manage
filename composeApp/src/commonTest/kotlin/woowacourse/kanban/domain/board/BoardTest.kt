package woowacourse.kanban.domain.board

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoardTest {

    @Test
    fun `보드에 카드를 추가할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용내용",
            tags = listOf("태그"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        var board = Board() + card

        assertThat(board.totalTaskCount).isEqualTo(1)
        board += card
        assertThat(board.totalTaskCount).isEqualTo(2)
    }

    @Test
    fun `state에 따라 Card가 분류된다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.IN_PROGRESS,
            ),
            Card.create(
                title = "제목4",
                content = "내용내용4",
                tags = listOf("태그4"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.DONE,
            ),
        )

        val board = Board(cardList)

        assertThat(board.toDoTaskCount).isEqualTo(2)
        assertThat(board.inProgressTaskCount).isEqualTo(1)
        assertThat(board.doneTaskCount).isEqualTo(1)
    }

    @Test
    fun `카드가 없으면 빈 보드이다`() {
        val board = Board()

        assertThat(board.totalTaskCount).isEqualTo(0)
        assertThat(board.doneTaskCount).isEqualTo(0)
        assertThat(board.inProgressTaskCount).isEqualTo(0)
        assertThat(board.toDoTaskCount).isEqualTo(0)
    }

    @Test
    fun `카드가 없으면 완료율은 0%이다`() {
        val board = Board()

        assertThat(board.completionPercentage).isEqualTo(0)
    }

    @Test
    fun `전체 카드 3개 중 1개만 완료되었다면 완료율은 33%이다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerState.DINO,
                state = CardTaskState.IN_PROGRESS,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.DONE,
            ),
        )

        val board = Board(cardList)
        assertThat(board.completionPercentage).isEqualTo(33)
    }

    @Test
    fun `카드 상태가 모두 완료되었다면 완료율은 100%이다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerState.DINO,
                state = CardTaskState.DONE,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerState.DINO,
                state = CardTaskState.DONE,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.DONE,
            ),
        )

        val board = Board(cardList)
        assertThat(board.completionPercentage).isEqualTo(100)
    }

    @Test
    fun `보드의 태스크 상태를 변경할 수 있다`() {
        var board: Board = Board()
        val card: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )
        board += card
        assertThat(board.toDoTaskCount).isEqualTo(1)
        assertThat(board.doneTaskCount).isEqualTo(0)

        val updatedBoard = board.withUpdatedTaskState(card.id, CardTaskState.DONE)
        assertThat(updatedBoard.toDoTaskCount).isEqualTo(0)
        assertThat(updatedBoard.doneTaskCount).isEqualTo(1)
    }
}

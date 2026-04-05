package woowacourse.kanban.domain.board

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.card.MoveResult
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoardTest {

    @Test
    fun `보드에 카드를 추가할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용내용",
            tags = listOf("태그"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        var board = Board() + card

        assertThat(board.totalTaskCount).isEqualTo(1)
        board += card
        assertThat(board.totalTaskCount).isEqualTo(2)
    }

    @Test
    fun `보드에 카드를 삭제할 수 있다`()  {
        // given && when
        val card = Card.create(
            title = "제목",
            content = "내용내용",
            tags = listOf("태그"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        // then
        var board = Board() + card
        assertThat(board.totalTaskCount).isEqualTo(1)
        board -= card
        assertThat(board.totalTaskCount).isEqualTo(0)
    }

    @Test
    fun `state에 따라 Card가 분류된다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerStatus.FAMES,
                state = CardTaskStatus.IN_PROGRESS,
            ),
            Card.create(
                title = "제목4",
                content = "내용내용4",
                tags = listOf("태그4"),
                manager = CardManagerStatus.FAMES,
                state = CardTaskStatus.DONE,
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
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.IN_PROGRESS,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerStatus.FAMES,
                state = CardTaskStatus.DONE,
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
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.DONE,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.DONE,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerStatus.FAMES,
                state = CardTaskStatus.DONE,
            ),
        )

        val board = Board(cardList)
        assertThat(board.completionPercentage).isEqualTo(100)
    }

    @Test
    fun `보드는 태스크의 변화를 반영할 수 있다`() {
        // given
        var board = Board()
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerStatus.NONE,
            state = CardTaskStatus.TODO,
        )
        board +=  card

        assertThat(board.toDoTaskCount).isEqualTo(1)
        assertThat(board.doneTaskCount).isEqualTo(0)

        // when
        val updatedCard = card.withCardFormInput(
            title = "수정된 제목",
            content = "수정된 내용",
            tags = listOf("수정된 태그1", "수정된 태그2"),
            managerState = CardManagerStatus.DINO,
            taskState = CardTaskStatus.IN_PROGRESS,
        )

        val updatedBoard = board.updateCard(updatedCard)

        // then
        assertThat(updatedBoard.toDoTaskCount).isEqualTo(0)
        assertThat(updatedBoard.inProgressTaskCount).isEqualTo(1)

        val foundCard = updatedBoard.cardList.first()
        assertThat(foundCard.id).isEqualTo(card.id)
        assertThat(foundCard.title).isEqualTo("수정된 제목")
    }

    @Test
    fun `보드의 태스크 상태가 변경되면 태스크 완료율에 반영된다`() {
        // given
        var board = Board()
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )
        board += card

        assertThat(board.completionPercentage).isEqualTo(0)

        val result1 = card.moveTo(CardTaskStatus.IN_PROGRESS)
        val cardInProgress = (result1 as MoveResult.Success).updatedCard
        val step1Board = board.updateCard(cardInProgress)
        assertThat(step1Board.completionPercentage).isEqualTo(0)

        val result2 = cardInProgress.moveTo(CardTaskStatus.REVIEW)
        val cardInReview = (result2 as MoveResult.Success).updatedCard
        val step2Board = step1Board.updateCard(cardInReview)
        assertThat(step2Board.completionPercentage).isEqualTo(0)

        val result3 = cardInReview.moveTo(CardTaskStatus.DONE)
        val cardDone = (result3 as MoveResult.Success).updatedCard
        val updatedBoard = step2Board.updateCard(cardDone)

        // then
        assertThat(updatedBoard.completionPercentage).isEqualTo(100)
        assertThat(updatedBoard.doneTaskCount).isEqualTo(1)
    }
    @Test
    fun `Card의 Status가 To do라면, 담당자가 null인 상태로 생성할 수 있다`() {
        // given
        var board: Board = Board()
        val card: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf(
                "태그1",
                "태그2",
            ),
            manager = CardManagerStatus.NONE,
            state = CardTaskStatus.TODO,
        )
        // when
        board += card
        // then
        assertThat(board.toDoTaskCount).isEqualTo(1)
    }

    @Test
    fun `삭제 가능한 상태(TODO, IN_PROGRESS)의 카드는 보드에서 삭제할 수 있다`() {
        // given
        val todoCard = createTestCard(state = CardTaskStatus.TODO)
        val inProgressCard = createTestCard(state = CardTaskStatus.IN_PROGRESS)
        var board = Board(listOf(todoCard, inProgressCard))

        assertThat(board.totalTaskCount).isEqualTo(2)

        // when
        board -= todoCard
        board -= inProgressCard

        // then
        assertThat(board.totalTaskCount).isEqualTo(0)
    }

    @Test
    fun `삭제 불가능한 상태(DONE, REVIEW)의 카드는 삭제를 시도해도 보드에 남아있다`() {
        // given
        val doneCard = createTestCard(state = CardTaskStatus.DONE)
        val reviewCard = createTestCard(state = CardTaskStatus.REVIEW)
        var board = Board(listOf(doneCard, reviewCard))

        assertThat(board.totalTaskCount).isEqualTo(2)

        // when
        board -= doneCard
        board -= reviewCard

        // then
        assertThat(board.totalTaskCount).isEqualTo(2)
        assertThat(board.cardList).contains(doneCard, reviewCard)
    }

    private fun createTestCard(state: CardTaskStatus, managerStatus: CardManagerStatus = CardManagerStatus.DINO): Card {
        return Card.create(
            title = "테스트",
            content = "내용",
            tags = emptyList(),
            manager = managerStatus,
            state = state,
        )
    }
}

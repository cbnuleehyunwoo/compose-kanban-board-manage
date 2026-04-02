package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskStatus
import java.util.UUID

/**
 * Board Class입니다.
 * @param cardList 카드 리스트입니다.
 * @param boardTitle 보드 제목입니다.
 * @param id 보드 ID입니다.
 */
class Board(
    cardList: List<Card> = emptyList(),
    private val boardTitle: String = "",
    private val id: String = UUID.randomUUID().toString(),
) {
    val cardList = cardList.toList()
    val title: String = boardTitle
    val boardId: String = id
    val totalTaskCount: Int = cardList.size
    val doneTaskCount: Int = cardList.count { it.taskState == CardTaskStatus.DONE }
    val inProgressTaskCount: Int = cardList.count { it.taskState == CardTaskStatus.IN_PROGRESS }
    val toDoTaskCount: Int = cardList.count { it.taskState == CardTaskStatus.TODO }
    val completionRatio = if (totalTaskCount == 0) 0f
    else doneTaskCount.toFloat() / totalTaskCount
    val completionPercentage = (completionRatio * 100).toInt()

    fun cardsByState(state: CardTaskStatus): List<Card> = cardList.filter { it.taskState == state }
    operator fun plus(card: Card): Board = Board(
        id = id,
        boardTitle = boardTitle,
        cardList = cardList + card,
    )

    operator fun minus(card: Card): Board = Board(
        id = id,
        boardTitle = boardTitle,
        cardList = cardList - card,
    )

    fun withTaskState(
        cardId: String,
        targetState: CardTaskStatus,
    ): Board {
        return Board(
            id = id,
            boardTitle = boardTitle,
            cardList = cardList.map { currentCard ->
                if (currentCard.id == cardId && isValidTransition(
                        currentStatus = currentCard.taskState,
                        targetStatus = targetState,
                    )
                ) {
                    currentCard.withTaskState(newState = targetState)
                } else currentCard
            },
        )
    }

    companion object {
        val transitionRule = mapOf(
            CardTaskStatus.TODO to listOf(CardTaskStatus.IN_PROGRESS),
            CardTaskStatus.IN_PROGRESS to listOf(
                CardTaskStatus.TODO,
                CardTaskStatus.REVIEW,
            ),
            CardTaskStatus.REVIEW to listOf(
                CardTaskStatus.IN_PROGRESS,
                CardTaskStatus.DONE,
            ),
            CardTaskStatus.DONE to listOf(
                CardTaskStatus.TODO,
            ),
            CardTaskStatus.REVIEW to listOf(
                CardTaskStatus.IN_PROGRESS,
                CardTaskStatus.DONE,
            ),
        )

        fun isValidTransition(
            currentStatus: CardTaskStatus,
            targetStatus: CardTaskStatus,
        ): Boolean {
            return transitionRule
                .getValue(currentStatus)
                .contains(targetStatus)
        }
    }
}

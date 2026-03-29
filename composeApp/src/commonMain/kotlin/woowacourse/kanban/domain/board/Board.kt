package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskState
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
    val cardList  = cardList.toList()
    val title: String = boardTitle
    val boardId: String = id
    val totalTaskCount: Int = cardList.size
    val doneTaskCount: Int = cardList.count { it.taskState == CardTaskState.DONE }
    val inProgressTaskCount: Int = cardList.count { it.taskState == CardTaskState.IN_PROGRESS }
    val toDoTaskCount: Int = cardList.count { it.taskState == CardTaskState.TODO }
    val completionRatio = if (totalTaskCount == 0) 0f
    else doneTaskCount.toFloat() / totalTaskCount
    val completionPercentage = (completionRatio * 100).toInt()

    fun cardsByState(state: CardTaskState): List<Card> = cardList.filter { it.taskState == state }
    operator fun plus(card: Card): Board = Board(
        id = id,
        boardTitle = boardTitle,
        cardList = cardList + card
    )
    operator fun minus(card: Card): Board = Board(
        id = id,
        boardTitle = boardTitle,
        cardList = cardList - card
    )

    /**
     * Card의 상태를 변경합니다. Card Class의 updateWithNewState 메서드를 활용합니다.
     * @param cardId 변경할 카드의 ID입니다.
     */
    fun withUpdatedTaskState(cardId: String, targetState: CardTaskState): Board {
        return Board(
            id = id,
            boardTitle = boardTitle,
            cardList = cardList.map { card ->
                if (card.id == cardId) card.withUpdatedTaskState(targetState) else card
            }
        )
    }
}

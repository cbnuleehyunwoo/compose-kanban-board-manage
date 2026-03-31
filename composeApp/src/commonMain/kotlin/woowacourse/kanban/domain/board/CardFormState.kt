package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState

data class CardFormState(
    val title: String = "",
    val content: String = "",
    val tagInput: String = "",
    val taskState: CardTaskState = CardTaskState.TODO,
    val managerState: CardManagerState = CardManagerState.DINO,
) {
    val tags: List<String> = Card.parseTag(tagInput)
    val tagInfoText: String = Card.isValidTagInfo(tagInput)
    val isCreateEnabled: Boolean = Card.isValidText(title) && Card.isValidTag(tagInput)
}

package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus

data class CardForm(
    val title: String = "",
    val content: String = "",
    val tagInput: String = "",
    val taskState: CardTaskStatus = CardTaskStatus.TODO,
    val managerState: CardManagerStatus = CardManagerStatus.DINO,
) {
    val tags: List<String> = Card.parseTag(tagInput)
    val tagInfoText: String = Card.isValidTagInfo(tagInput)
    val isCreateEnabled: Boolean = Card.isValidText(title) && Card.isValidTag(tagInput)
}

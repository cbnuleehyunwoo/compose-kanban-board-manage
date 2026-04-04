package woowacourse.kanban.domain.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.domain.board.CardForm
import woowacourse.kanban.domain.card.Card

class EditDialogStateHolder(
    private val onCardUpdate: (Card) -> Unit,
    private val onCardDelete: (Card) -> Unit,
) {
    var isEditDialogVisible by mutableStateOf(false)
        private set

    var cardForm by mutableStateOf(CardForm())
        private set

    var targetCard by mutableStateOf<Card?>(null)

    fun showEditDialog() {
        isEditDialogVisible = true
    }

    fun closeEditDialog() {
        isEditDialogVisible = false
        targetCard = null
    }

    fun updateCardForm(newForm: CardForm) {
        cardForm = newForm
    }

    fun confirm() {
        val currentTarget = targetCard ?: return

        val updatedCard = Card (
            id = currentTarget.id,
            title = cardForm.title,
            content = cardForm.content,
            tags = cardForm.tags,
            managerState = cardForm.managerState,
            taskState = cardForm.taskState,
        )
        onCardUpdate(updatedCard)
        closeEditDialog()
    }

    fun deleteTarget() {
        val currentTarget = targetCard ?: return
        onCardDelete(currentTarget)
        closeEditDialog()
    }

    fun setCard(card: Card) {
        targetCard = card
        cardForm = CardForm(
            title = card.title,
            content = card.content,
            tagInput = card.tags.joinToString(","),
            taskState = card.taskState,
            managerState = card.managerState,
        )
    }
}

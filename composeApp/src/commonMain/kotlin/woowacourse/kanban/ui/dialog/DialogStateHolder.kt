package woowacourse.kanban.ui.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.domain.board.CardForm
import woowacourse.kanban.domain.card.Card

class DialogStateHolder(
    private val onCardCreate: (Card) -> Unit,
    private val onCancel: () -> Unit,
) {

    var isCreationDialogVisible by mutableStateOf(false)
        private set

    var cardForm by mutableStateOf(CardForm())
        private set

    fun showCreationDialog() {
        cardForm = CardForm()
        isCreationDialogVisible = true
    }

    fun closeCreationDialog() {
        isCreationDialogVisible = false
    }

    fun updateCardForm(newForm: CardForm) {
        cardForm = newForm
    }

    fun confirm(card: Card) {
        onCardCreate(card)
        isCreationDialogVisible = false
    }
    fun cancel() {
        onCancel()
        isCreationDialogVisible = false
    }
}

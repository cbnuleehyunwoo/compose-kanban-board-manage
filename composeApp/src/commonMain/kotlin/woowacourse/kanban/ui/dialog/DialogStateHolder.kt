package woowacourse.kanban.ui.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.domain.board.CardForm
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.MoveFailureReason
import woowacourse.kanban.domain.card.MoveResult

class DialogStateHolder(
    private val onCardCreate: (Card) -> Unit,
    private val onCardUpdate: (Card) -> Unit,
    private val onCardDelete: (Card) -> Unit,
    private val onShowSnackbar: (String) -> Unit,
) {
    var isDialogVisible by mutableStateOf(false)
        private set

    var cardForm by mutableStateOf(CardForm())
        private set


    var targetCard by mutableStateOf<Card?>(null)
        private set

    val isEditMode: Boolean get() = targetCard != null

    fun showCreationDialog() {
        targetCard = null
        cardForm = CardForm()
        isDialogVisible = true
    }

    fun showEditDialog(card: Card) {
        targetCard = card
        cardForm = CardForm(
            title = card.title,
            content = card.content,
            tagInput = card.tags.joinToString(","),
            taskState = card.taskState,
            managerState = card.managerState,
        )
        isDialogVisible = true
    }

    fun closeDialog() {
        isDialogVisible = false
        targetCard = null
    }

    fun updateCardForm(newForm: CardForm) {
        cardForm = newForm
    }

    fun confirm() {
        val currentTarget = targetCard

        if (currentTarget == null) {
            val newCard = Card.create(
                title = cardForm.title,
                content = cardForm.content,
                tags = cardForm.tags,
                manager = cardForm.managerState,
                state = cardForm.taskState,
            )
            onCardCreate(newCard)
            closeDialog()
        } else {
            handleUpdate(currentTarget)
        }
    }
    private fun handleUpdate(currentTarget: Card) {
        if (currentTarget.taskState != cardForm.taskState) {
            val cardWithFormInput = currentTarget.withCardFormInput(
                title = cardForm.title,
                content = cardForm.content,
                tags = cardForm.tags,
                managerState = cardForm.managerState,
                taskState = currentTarget.taskState
            )

            when (val moveResult = cardWithFormInput.moveTo(cardForm.taskState)) {
                is MoveResult.Success -> {
                    onCardUpdate(moveResult.updatedCard)
                    closeDialog()
                }
                is MoveResult.Failure -> {
                    val message = when (moveResult.reason) {
                        MoveFailureReason.INVALID_TRANSITION -> "해당 상태로 옮길 수 없습니다."
                        MoveFailureReason.INVALID_MANAGER -> "담당자를 지정해야 상태를 옮길 수 있습니다."
                    }
                    onShowSnackbar(message)
                    closeDialog()
                }
            }
        } else {
            val updatedCard = currentTarget.withCardFormInput(
                title = cardForm.title,
                content = cardForm.content,
                tags = cardForm.tags,
                managerState = cardForm.managerState,
                taskState = cardForm.taskState
            )
            onCardUpdate(updatedCard)
            closeDialog()
        }
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

    fun deleteTarget() {
        targetCard?.let {
            onCardDelete(it)
            closeDialog()
        }
    }
}

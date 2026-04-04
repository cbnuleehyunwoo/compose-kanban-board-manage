package woowacourse.kanban.ui.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.dialog.DialogStateHolder
import woowacourse.kanban.ui.card.creation.CardDialogBase


@Composable
fun CardCreationScreen(state: DialogStateHolder) {
    Dialog(onDismissRequest = { state.closeCreationDialog() }) {
        CardDialogBase(
            title = "새 태스크 생성",
            cardForm = state.cardForm,
            isEditDialog = false,
            onFormChange = { state.updateCardForm(it) },
            onDismiss = { state.closeCreationDialog() },
            onConfirm = {
                state.confirm(
                    Card.create(
                        title = state.cardForm.title,
                        content = state.cardForm.content,
                        tags = state.cardForm.tags,
                        manager = state.cardForm.managerState,
                        state = state.cardForm.taskState,
                    )
                )
                state.closeCreationDialog()
            }
        )
    }
}

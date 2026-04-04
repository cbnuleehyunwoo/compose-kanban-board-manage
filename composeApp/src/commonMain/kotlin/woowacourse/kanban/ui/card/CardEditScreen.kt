package woowacourse.kanban.ui.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.domain.dialog.EditDialogStateHolder
import woowacourse.kanban.ui.card.creation.CardDialogBase


@Composable
fun CardEditScreen(state: EditDialogStateHolder) {
    Dialog(onDismissRequest = { state.closeEditDialog() }) {
        CardDialogBase(
            title = "태스크 수정",
            cardForm = state.cardForm,
            isEditDialog = true,
            onFormChange = { state.updateCardForm(it) },
            onDismiss = { state.closeEditDialog() },
            onConfirm = { state.confirm() },
            onDelete = { state.deleteTarget() }
        )
    }
}

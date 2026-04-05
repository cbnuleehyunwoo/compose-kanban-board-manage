package woowacourse.kanban.ui.card

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.ui.dialog.EditDialogStateHolder
import woowacourse.kanban.ui.card.creation.ActionButton
import woowacourse.kanban.ui.card.creation.ActionButtonType
import woowacourse.kanban.ui.card.creation.CardDialogBase


@Composable
fun CardEditScreen(state: EditDialogStateHolder) {
    Dialog(onDismissRequest = { state.closeEditDialog() }) {
        CardDialogBase(
            title = "태스크 수정",
            cardForm = state.cardForm,
            onFormChange = { state.updateCardForm(it) },
            onDismiss = { state.closeEditDialog() },
            footer = {
                ActionButton(
                    buttonType = ActionButtonType.CANCEL,
                    enabled = true,
                    onClick = { state.closeEditDialog() },
                )

                Spacer(modifier = Modifier.width(12.dp))

                ActionButton(
                    buttonType = ActionButtonType.DELETE,
                    enabled = true,
                    onClick = { state.deleteTarget() },
                )

                Spacer(modifier = Modifier.width(12.dp))

                ActionButton(
                    buttonType = ActionButtonType.EDIT,
                    enabled = state.cardForm.isCreateEnabled,
                    onClick = { state.confirm() },
                )
            }
        )
    }
}

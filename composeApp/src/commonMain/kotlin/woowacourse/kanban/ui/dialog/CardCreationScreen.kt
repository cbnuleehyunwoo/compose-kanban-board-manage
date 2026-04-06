package woowacourse.kanban.ui.card

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.ui.dialog.DialogStateHolder
import woowacourse.kanban.ui.card.creation.ActionButton
import woowacourse.kanban.ui.card.creation.ActionButtonType
import woowacourse.kanban.ui.card.creation.CardDialogBase


@Composable
fun CardCreationScreen(state: DialogStateHolder) {
    Dialog(onDismissRequest = { state.closeDialog() }) {
        CardDialogBase(
            title = "새 태스크 생성",
            cardForm = state.cardForm,
            onFormChange = { state.updateCardForm(it) },
            onDismiss = { state.closeDialog() },
            footer = {
                ActionButton(
                    buttonType = ActionButtonType.CANCEL,
                    enabled = true,
                    onClick = { state.closeDialog() },
                )

                Spacer(modifier = Modifier.width(12.dp))

                ActionButton(
                    buttonType = ActionButtonType.CREATE,
                    enabled = state.cardForm.isCreateEnabled,
                    onClick = {
                        state.confirm()
                    },
                )
            },
        )
    }
}

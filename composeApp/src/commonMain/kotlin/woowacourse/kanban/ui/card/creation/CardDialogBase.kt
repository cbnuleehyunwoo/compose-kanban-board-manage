package woowacourse.kanban.ui.card.creation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.domain.board.CardForm
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.ui.board.common.toDisplayText
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultContent
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedContent

@Composable
fun CardDialogBase(
    title: String,
    cardForm: CardForm,
    isEditDialog: Boolean,
    onFormChange: (CardForm) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
) {
    OutlinedCard(modifier = modifier.testTag(if (isEditDialog) "수정 모달 열림" else "생성 모달 열림")) {
        Column(
            modifier = Modifier.background(DefaultBackground)
                .width(720.dp),
        ) {
            CardPanelHeaderSection(
                title = title,
                onClose = onDismiss,
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier.padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                CardFormInputSections(
                    cardForm = cardForm,
                    onFormChange = onFormChange,
                )

                CardStateSection(
                    selectedState = cardForm.taskState,
                    onStateChange = { newState ->
                        val newManager = if (newState != CardTaskStatus.TODO && cardForm.managerState == CardManagerStatus.NONE) {
                            CardManagerStatus.DINO
                        } else cardForm.managerState
                        onFormChange(
                            cardForm.copy(
                                taskState = newState,
                                managerState = newManager,
                            ),
                        )
                    },
                )

                CardManagerSection(
                    isManagerNullable = cardForm.taskState == CardTaskStatus.TODO,
                    selectedManager = cardForm.managerState,
                    onManagerChange = { onFormChange(cardForm.copy(managerState = it)) },
                )

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                ActionButtonSection(
                    isEditDialog = isEditDialog,
                    createEnabled = cardForm.isCreateEnabled,
                    onCancelClick = onDismiss,
                    onDeleteClick = { onDelete?.invoke() },
                    onConfirmClick = onConfirm,
                )
            }
        }
    }
}

@Composable
private fun CardPanelHeaderSection(
    title: String,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 28.dp,
                horizontal = 24.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "닫기",
            modifier = Modifier.clickable { onClose() },
        )
    }
}

@Composable
private fun CardFormInputSections(
    cardForm: CardForm,
    onFormChange: (CardForm) -> Unit,
) {
    CardCreationPanelFormSection(
        title = "제목 *",
        placeholder = "태스크 제목을 입력하세요",
        value = cardForm.title,
        onTextChange = { onFormChange(cardForm.copy(title = it)) },
        showAdditionalInfo = !Card.isValidText(cardForm.title),
        testTag = "titleTextField",
        infoText = Card.getTitleInfo(),
        isError = !Card.isValidText(cardForm.title),
    )

    CardCreationPanelFormSection(
        title = "설명",
        placeholder = "태스크에 대한 자세한 설명을 입력하세요",
        value = cardForm.content,
        onTextChange = { onFormChange(cardForm.copy(content = it)) },
        testTag = "descriptionTextField",
    )

    CardCreationPanelFormSection(
        title = "태그",
        placeholder = "태그를 쉼표로 구분하여 입력하세요",
        value = cardForm.tagInput,
        onTextChange = { onFormChange(cardForm.copy(tagInput = it)) },
        showAdditionalInfo = true,
        testTag = "tagTextField",
        infoText = cardForm.tagInfoText,
        isError = !Card.isValidTag(cardForm.tagInput),
    )
}

@Composable
private fun CardStateSection(
    selectedState: CardTaskStatus,
    onStateChange: (CardTaskStatus) -> Unit,
) {
    Column {
        TitleText("상태 *")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CardTaskStatus.entries.forEach { state ->
                SelectableButton(
                    text = state.toDisplayText(),
                    isSelected = selectedState == state,
                    onClick = { onStateChange(state) },
                    modifier = Modifier.width(120.dp)
                        .height(52.dp),
                )
            }
        }
    }
}

@Composable
private fun CardManagerSection(
    isManagerNullable: Boolean,
    selectedManager: CardManagerStatus,
    onManagerChange: (CardManagerStatus) -> Unit,
) {
    Column {
        TitleText("담당자${if (isManagerNullable) "" else " *"}")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CardManagerStatus.entries
                .filter { isManagerNullable || it != CardManagerStatus.NONE }
                .forEach { manager ->
                    SelectableButton(
                        text = manager.toDisplayText(),
                        isSelected = selectedManager == manager,
                        onClick = { onManagerChange(manager) },
                        modifier = Modifier.width(140.dp)
                            .height(68.dp),
                        hasIcon = manager != CardManagerStatus.NONE,
                    )
                }
        }
    }
}

@Composable
private fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasIcon: Boolean = false,
) {
    val containerColor = if (isSelected) SelectedBackground else DefaultBackground
    val contentColor = if (isSelected) SelectedContent else DefaultContent
    val borderColor = if (isSelected) SelectedContent else SelectedBackground

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier.testTag(text),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasIcon) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun ActionButtonSection(
    isEditDialog: Boolean,
    createEnabled: Boolean,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        ActionButton(
            buttonType = ActionButtonType.CANCEL,
            enabled = true,
            onClick = onCancelClick,
        )
        if (isEditDialog) {
            Spacer(modifier = Modifier.width(12.dp))
            ActionButton(
                buttonType = ActionButtonType.DELETE,
                enabled = true,
                onClick = onDeleteClick,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        ActionButton(
            buttonType = if (isEditDialog) ActionButtonType.EDIT else ActionButtonType.CREATE,
            enabled = createEnabled,
            onClick = onConfirmClick,
        )
    }
}

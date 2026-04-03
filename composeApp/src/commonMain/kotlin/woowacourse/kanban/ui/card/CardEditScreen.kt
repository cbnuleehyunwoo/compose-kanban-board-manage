package woowacourse.kanban.ui.card

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.domain.board.CardForm
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus
import woowacourse.kanban.domain.dialog.DialogStateHolder
import woowacourse.kanban.domain.dialog.EditDialogStateHolder
import woowacourse.kanban.ui.board.common.toDisplayText
import woowacourse.kanban.ui.card.creation.ActionButton
import woowacourse.kanban.ui.card.creation.ActionButtonType
import woowacourse.kanban.ui.card.creation.CardCreationPanelFormSection
import woowacourse.kanban.ui.card.creation.TitleText
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultContent
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedContent


@Composable

fun CardEditScreen(
    state: EditDialogStateHolder,
    modifier: Modifier = Modifier,

    ) {
    Dialog(
        onDismissRequest = { state.closeEditDialog() },
    ) {
        CardEditScreenContents(
            cardForm = state.cardForm,
            onFormChange = { newForm -> state.updateCardForm(newForm) },
            onAmendItem = { state.confirm() },
            onDismiss = { state.closeEditDialog() },
            onDelete = {
                state.deleteTarget()
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun CardEditScreenContents(
    cardForm: CardForm,
    onFormChange: (CardForm) -> Unit,
    onAmendItem: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedCard(
        modifier = modifier.testTag("수정 모달 열림"),
    ) {
        Column(
            modifier = Modifier
                .background(DefaultBackground)
                .width(720.dp),
        ) {
            CardEditPanelHeaderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 28.dp,
                        horizontal = 24.dp,
                    ),
                onClose = onDismiss,
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                CardCreationPanelFormSection(
                    title = "제목 *",
                    placeholder = "태스크 제목을 입력하세요",
                    value = cardForm.title,
                    onTextChange = {
                        onFormChange(cardForm.copy(title = it))
                    },
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
                    placeholder = "태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)",
                    value = cardForm.tagInput,
                    onTextChange = { onFormChange(cardForm.copy(tagInput = it)) },
                    showAdditionalInfo = true,
                    testTag = "tagTextField",
                    infoText = cardForm.tagInfoText,
                    isError = !Card.isValidTag(cardForm.tagInput),
                )

                CardCreationPanelStateSection(
                    selectedState = cardForm.taskState,
                    onStateChange = { newState ->
                        val newManager = if (newState != CardTaskStatus.TODO && cardForm.managerState == CardManagerStatus.NONE) {
                            CardManagerStatus.DINO
                        } else {
                            cardForm.managerState
                        }
                        onFormChange(
                            cardForm.copy(
                                taskState = newState,
                                managerState = newManager,
                            ),
                        )
                    },
                )

                CardCreationPanelManagerSection(
                    isManagerNullable = cardForm.taskState == CardTaskStatus.TODO,
                    selectedManager = cardForm.managerState,
                    onManagerChange = { onFormChange(cardForm.copy(managerState = it)) },
                )

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                ActionButtonSection(
                    createEnabled = cardForm.isCreateEnabled,
                    onCancelClick = onDismiss,
                    onAmendClick = {
                        onAmendItem()
                    },
                    onDeleteClick = { onDelete() },
                    isEditDialog = true,
                )
            }
        }
    }
}

@Composable
private fun CardEditPanelHeaderSection(
    modifier: Modifier,
    onClose: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "태스크 수정",
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            lineHeight = 28.sp,
            letterSpacing = (-0.45).sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "닫기 아이콘",
            modifier = Modifier.clickable { onClose() },
        )
    }
}

@Composable
private fun CardCreationPanelStateSection(
    selectedState: CardTaskStatus,
    onStateChange: (CardTaskStatus) -> Unit,
) {
    Column {
        TitleText("상태 *")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardTaskStatus.entries.forEach { state ->
                StateButton(
                    text = state.toDisplayText(),
                    isSelected = selectedState == state,
                    onClick = { onStateChange(state) },
                    modifier = Modifier
                        .width(120.dp)
                        .height(52.dp),
                )
            }
        }
    }
}

@Composable
private fun StateButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) SelectedBackground else DefaultBackground
    val contentColor = if (isSelected) SelectedContent else DefaultContent
    val borderColor = if (isSelected) SelectedContent else SelectedBackground

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier.testTag(text),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )
    }
}

@Composable
private fun CardCreationPanelManagerSection(
    selectedManager: CardManagerStatus,
    onManagerChange: (CardManagerStatus) -> Unit,
    isManagerNullable: Boolean,
) {
    Column {
        TitleText("담당자 *")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CardManagerStatus.entries
                .filter { manager ->
                    isManagerNullable || manager != CardManagerStatus.NONE
                }
                .forEach { manager ->
                    ManagerButton(
                        text = manager.toDisplayText(),
                        isSelected = selectedManager == manager,
                        isManagerNullable = isManagerNullable,
                        onClick = { onManagerChange(manager) },
                        modifier = Modifier
                            .width(120.dp)
                            .height(68.dp),
                    )
                }
        }
    }
}

@Composable
private fun ManagerButton(
    text: String,
    isManagerNullable: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) SelectedBackground else DefaultBackground
    val contentColor = if (isSelected) SelectedContent else DefaultContent
    val borderColor = if (isSelected) SelectedContent else SelectedBackground

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (text != "없음") {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "매니저 아이콘",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF838383),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-0.15).sp,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun ActionButtonSection(
    createEnabled: Boolean,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAmendClick: () -> Unit,
    isEditDialog: Boolean,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ActionButton(
            buttonType = ActionButtonType.CANCEL,
            enabled = true,
            onClick = onCancelClick,
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (isEditDialog) {
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
            onClick = onAmendClick,
        )
    }
}

@Preview(widthDp = 672, heightDp = 909)
@Composable
fun CardEditScreenRoot() {

    val state = remember {
        DialogStateHolder(
            onCardCreate = {},
            onCancel = {},
        )
    }

    CardCreationScreen(
        state = state,
    )
}

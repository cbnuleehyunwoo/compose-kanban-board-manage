package woowacourse.kanban.ui.card.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultContent

@Composable
fun CardCreationPanelFormSection(
    title: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    value: String,
    onTextChange: (String) -> Unit = {},
    showAdditionalInfo: Boolean = false,
    testTag: String,
    infoText: String = "",
    isError: Boolean = false,
) {
    val errorColor = Color(0xFFB3261E)
    Column(
        modifier = modifier,
    ) {
        TitleText(title)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { onTextChange(it) },
            trailingIcon = {
                if (isError) Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "에러 아이콘",
                    tint = errorColor,
                )
            },
            isError = isError,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFFAAAAAA),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 1.sp,
                )
            },
            textStyle = TextStyle(
                color = if (isError) errorColor else DefaultContent,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .testTag(testTag),
        )
        if (showAdditionalInfo) {
            Text(
                text = infoText,
                color = if (isError) errorColor else Color(0xFF49454F),
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

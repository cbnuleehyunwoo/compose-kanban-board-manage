package woowacourse.kanban.ui.card.creation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ActionButtonType(
    val buttonText: String,
    val contentColor: Color,
    val containerColor: Color,
    val elevation: Dp,
) {
    CREATE(
        buttonText = "생성",
        contentColor = Color.White,
        containerColor = Color(0xFF4F39F6),
        elevation = 3.dp,
    ),
    CANCEL(
        buttonText = "취소",
        contentColor = Color(0xFF364153),
        containerColor = Color.White,
        elevation = 0.dp,
    ),
    DELETE(
        buttonText = "삭제",
        contentColor = Color.White,
        containerColor = Color(0xFFDB6365),
        elevation = 0.dp,
    )
}

@Composable
fun ActionButton(
    buttonType: ActionButtonType,
    enabled: Boolean,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = buttonType.elevation,
            pressedElevation = buttonType.elevation,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonType.containerColor,
            contentColor = buttonType.contentColor,
        ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Text(
            text = buttonType.buttonText,
            color = buttonType.contentColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnabledActionButtonPreview() {
    ActionButton(
        buttonType = ActionButtonType.CREATE,
        enabled = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun DisabledActionButtonPreview() {
    ActionButton(
        buttonType = ActionButtonType.CREATE,
        enabled = false,
    )
}

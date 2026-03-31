package woowacourse.kanban.ui.card.creation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TitleText(
    title: String,
) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color(0xFF364153),
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp,
    )
}
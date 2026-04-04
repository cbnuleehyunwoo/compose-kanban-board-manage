package woowacourse.kanban.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 프로젝트 탭 버튼입니다. 프로젝트에 포함된 보드를 선택하는 버튼입니다.
 * @param tabTitle 탭 제목입니다.
 * @param onClick 탭을 클릭했을 때의 동작입니다.
 * @param modifier Modifier
 * @param isSelected 선택 여부입니다.
 */
@Composable
fun ProjectTabButton(
    tabTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val tabColor = if (isSelected) Color(0xFFEEF2FF) else Color.White
    val textColor = if (isSelected) Color(0xFF432DD7) else Color.Black
    val elevationDp = if (isSelected) 2.dp else 0.dp

    Box(
        modifier
            .shadow(
                elevation = elevationDp,
                shape = RoundedCornerShape(10.dp),
            )
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = tabColor)
            .padding(
                vertical = 12.dp,
                horizontal = 15.dp,
            )
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = tabTitle.ifEmpty { "이름 없는 보드 " },
            color = textColor,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.31).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

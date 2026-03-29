package woowacourse.kanban.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.project.Project
import woowacourse.kanban.ui.theme.BoardColor.ProjectTabHeaderColor
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultBackground

/**
 * 프로젝트 탭입니다. 프로젝트 제목, 설명, 보드 목록을 보여줍니다.
 * @param modifier Modifier
 * @param project 프로젝트 데이터입니다.
 * @param onBoardSelected 보드를 선택합니다.
 */
@Composable
fun ProjectTab(
    project: Project,
    modifier: Modifier = Modifier,
    onBoardSelected: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(207.dp)
            .background(color = DefaultBackground),
    ) {
        ProjectTabHeader(
            modifier = Modifier.fillMaxWidth(),
            title = project.getTitle,
            description = project.getDescription,
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = ProjectTabHeaderColor,
        )
        ProjectContents(
            modifier = Modifier.fillMaxWidth(),
            selectedBoardIndex = project.currentBoardIndex,
            onBoardSelected = onBoardSelected,
            boards = project.boards,
        )
    }
}

/**
 * 프로젝트 탭 헤더 영역입니다. 프로젝트 제목과 설명을 보여줍니다.
 * @param modifier Modifier
 * @param title 프로젝트 제목입니다.
 * @param description 프로젝트 설명입니다.
 */
@Composable
private fun ProjectTabHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier,
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.44).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            modifier = Modifier,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            color = Color(0xFF6A7282),
            lineHeight = 20.sp,
            letterSpacing = (-0.15).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 프로젝트 탭 컨텐츠 영역입니다. 프로젝트에 포함된 보드 목록을 보여줍니다.
 * @param boards 보드 리스트입니다.
 * @param modifier Modifier
 * @param selectedBoardIndex 선택된 보드의 인덱스입니다.
 * @param onBoardSelected 보드를 선택합니다.
 */
@Composable
private fun ProjectContents(
    boards: List<Board>,
    modifier: Modifier = Modifier,
    selectedBoardIndex: Int = 0,
    onBoardSelected: (Int) -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        boards.forEachIndexed { index, board ->
            ProjectTabButton(
                tabTitle = board.title,
                onClick = { onBoardSelected(index) },
                isSelected = index == selectedBoardIndex,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

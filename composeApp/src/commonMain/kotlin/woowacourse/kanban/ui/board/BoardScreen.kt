package woowacourse.kanban.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.project.KanbanState
import woowacourse.kanban.domain.project.Project
import woowacourse.kanban.ui.board.common.toDisplayText
import woowacourse.kanban.ui.card.CardCreationScreen
import woowacourse.kanban.ui.card.CardScreen
import woowacourse.kanban.ui.theme.BoardColor.DoneContentColor
import woowacourse.kanban.ui.theme.BoardColor.DoneHeaderColor
import woowacourse.kanban.ui.theme.BoardColor.InProgressContentColor
import woowacourse.kanban.ui.theme.BoardColor.InProgressHeaderColor
import woowacourse.kanban.ui.theme.BoardColor.TodoContentColor
import woowacourse.kanban.ui.theme.BoardColor.TodoHeaderColor


@Composable
fun BoardScreen(
    state: KanbanState,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = state.snackbarHostState)
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                BoardHeaderSection(
                    modifier = Modifier.fillMaxWidth(),
                    board = state.currentBoard,
                    onClick = { state.showCreationDialog() },
                )
                BoardContents(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                )
            }
            if (state.isCreationDialogVisible) {
                CardCreationScreen(state = state)
            }
        }
    }
}

/**
 * 보드 헤더 영역입니다. Board 제목, 진행율, 프로그레스 바가 포함됩니다.
 * @param board 보드 데이터입니다.
 * @param onClick 보드 생성 버튼 클릭 이벤트입니다.
 * @param modifier Modifier
 */
@Composable
private fun BoardHeaderSection(
    board: Board,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val doneCount = board.doneTaskCount
    val totalCount = board.totalTaskCount
    val completionRatio = board.completionRatio
    val completionPercentage = board.completionPercentage


    Column(
        modifier = modifier
            .border(1.dp, Color(0xFFE5E7EB))
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Compose Desktop 칸반 보드 ",
                    fontWeight = FontWeight.W500,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    letterSpacing = 0.07.sp,
                    modifier = Modifier.testTag("보드 제목"),
                )
                Text(
                    text = "완료율: ${completionPercentage}% (${doneCount}/${totalCount})",
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF6A7282),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = (-0.15).sp,
                    modifier = Modifier.testTag("완료율"),
                )
            }

            Button(
                onClick = { onClick() },
                modifier = Modifier.testTag("새 태스크 생성 버튼"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F39F6),
                ),
                shape = RoundedCornerShape(20),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "새 태스크 생성 아이콘",
                        tint = Color.White,
                    )
                    Text(
                        text = "새 태스크 생성",
                        color = Color.White,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = (-0.31).sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { completionRatio },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .testTag("프로그레스 바"),
            color = Color(0xFF4F39F6),
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}

@Composable
private fun BoardContents(
    state: KanbanState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        BoardCardColumn(
            modifier = Modifier.width(320.dp)
                .height(748.dp),
            filteredCards = state.currentBoard.cardsByState(CardTaskState.TODO),
            mode = CardTaskState.TODO,
            getIsDropTarget = { state.isDropTarget(CardTaskState.TODO) },
            onBoundsChanged = { rect -> state.updateColumnBounds(CardTaskState.TODO, rect) },
            onTaskDragStart = { card -> state.onDragStart(card) },
            onTaskDragChange = { offset -> state.onDragChange(offset) },
            onTaskDragEnd = { state.onDragEnd() },
            onTaskDragCancel = { state.clearDrag() },
        )

        BoardCardColumn(
            modifier = Modifier.width(320.dp)
                .height(748.dp),
            filteredCards = state.currentBoard.cardsByState(CardTaskState.IN_PROGRESS),
            mode = CardTaskState.IN_PROGRESS,
            getIsDropTarget = { state.isDropTarget(CardTaskState.IN_PROGRESS) },
            onBoundsChanged = { rect -> state.updateColumnBounds(CardTaskState.IN_PROGRESS, rect) },
            onTaskDragStart = { card -> state.onDragStart(card) },
            onTaskDragChange = { offset -> state.onDragChange(offset) },
            onTaskDragEnd = { state.onDragEnd() },
            onTaskDragCancel = { state.clearDrag() },
        )

        BoardCardColumn(
            modifier = Modifier.width(320.dp)
                .height(748.dp),
            filteredCards = state.currentBoard.cardsByState(CardTaskState.DONE),
            mode = CardTaskState.DONE,
            getIsDropTarget = { state.isDropTarget(CardTaskState.DONE) },
            onBoundsChanged = { rect -> state.updateColumnBounds(CardTaskState.DONE, rect) },
            onTaskDragStart = { card -> state.onDragStart(card) },
            onTaskDragChange = { offset -> state.onDragChange(offset) },
            onTaskDragEnd = { state.onDragEnd() },
            onTaskDragCancel = { state.clearDrag() },
        )
    }
}

@Composable
private fun BoardCardColumn(
    filteredCards: List<Card>,
    mode: CardTaskState,
    modifier: Modifier = Modifier,
    getIsDropTarget: () -> Boolean = { false },
    onBoundsChanged: (Rect) -> Unit = {},
    onTaskDragStart: (Card) -> Unit = {},
    onTaskDragChange: (Offset) -> Unit = {},
    onTaskDragEnd: () -> Unit = {},
    onTaskDragCancel: () -> Unit = {},

    ) {
    val isDropTarget by remember { derivedStateOf { getIsDropTarget() } }
    val lastBoundsHolder = remember { mutableStateOf<Rect?>(null) }
    val headerColor = when (mode) {
        CardTaskState.TODO -> TodoHeaderColor
        CardTaskState.IN_PROGRESS -> InProgressHeaderColor
        CardTaskState.DONE -> DoneHeaderColor
    }
    val contentColor = when (mode) {
        CardTaskState.TODO -> TodoContentColor
        CardTaskState.IN_PROGRESS -> InProgressContentColor
        CardTaskState.DONE -> DoneContentColor
    }

    Column(
        modifier = modifier
            .testTag(mode.name)
            .border(1.dp, headerColor, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .onGloballyPositioned {
                val newBounds = it.boundsInWindow()
                if (newBounds != lastBoundsHolder.value) {
                    lastBoundsHolder.value = newBounds
                    onBoundsChanged(newBounds)
                }
            }
            .then(
                if (isDropTarget) Modifier.border(2.dp, headerColor, RoundedCornerShape(12.dp)) else modifier,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = mode.toDisplayText(),
                color = Color.White,
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.31).sp,
            )
            Text(
                text = filteredCards.size.toString(),
                color = Color.Black,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.15).sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 2.dp)
                    .testTag("${mode.name}_개수"),
            )
        }
        LazyColumn(
            modifier = Modifier
                .background(contentColor)
                .padding(horizontal = 17.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = filteredCards,
                key = { card -> card.id },
            ) { card ->
                CardScreen(
                    cardData = card,
                    onDragStart = { onTaskDragStart(card) },
                    onDragChange = onTaskDragChange,
                    onDragEnd = onTaskDragEnd,
                    onDragCancel = onTaskDragCancel,
                )
            }
        }
    }
}

/* Preview */
@Preview(
    name = "BoardScreen Preview",
    widthDp = 1295,
    heightDp = 909,
    showBackground = true,
)
@Composable
private fun BoardScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val state = remember {
        KanbanState(
            initialProject = Project(),
            snackbarHostState = snackbarHostState,
            scope = scope,
        )
    }

    BoardScreen(
        state = state,
    )
}

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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
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
    board: Board,
    onAddCard: (Card) -> Unit,
    onBoardChange: (Board) -> Boolean,
) {
    var showCardCreationPanel by remember { mutableStateOf(false) }

    BoardScreen(
        board = board,
        showCardCreationPanel = showCardCreationPanel,
        onAddCard = onAddCard,
        onShowCardCreationPanelChange = { showCardCreationPanel = it },
        modifier = Modifier.fillMaxSize(),
        onBoardChange = onBoardChange,
    )
}

/**
 * 보드 화면입니다. 프로젝트 화면의 우측 보드 영역입니다.
 * @param board 보드 데이터입니다.
 * @param showCardCreationPanel 카드 생성 모달을 보여줄지 여부입니다.
 * @param onAddCard 보드에 카드를 추가합니다.
 * @param onShowCardCreationPanelChange 카드 생성창 표시 여부입니다.
 * @param modifier Modifier
 * @param onBoardChange 보드 데이터를 변경합니다.
 */
@Composable
fun BoardScreen(
    board: Board,
    showCardCreationPanel: Boolean,
    onAddCard: (Card) -> Unit,
    onShowCardCreationPanelChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onBoardChange: (Board) -> Boolean,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                    board = board,
                    onClick = {
                        onShowCardCreationPanelChange(true)
                    },
                )
                BoardContents(
                    modifier = Modifier.fillMaxSize(),
                    board = board,
                    onChangeContent = {
                        val isSuccess = onBoardChange(it)
                        coroutineScope.launch {
                            if (isSuccess) {
                                snackbarHostState.showSnackbar("태스크가 이동되었습니다.")
                            }
                        }
                    },
                )
            }

            if (showCardCreationPanel) {
                CardCreationScreen(
                    onAddItem = { newCard ->
                        onAddCard(newCard)
                        onShowCardCreationPanelChange(false)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("새로운 태스크가 추가되었습니다.")
                        }
                    },
                    onDismiss = {
                        onShowCardCreationPanelChange(false)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("새 태스크 추가가 취소되었습니다.")
                        }
                    },
                )
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

/**
 * 보드 컨텐츠 영역입니다. 태스크 작업 상태에 따른 카드 리스트를 표시합니다.
 * @param modifier Modifier
 * @param board 보드 데이터입니다.
 * @param onChangeContent 보드 데이터를 변경합니다.
 */
@Composable
private fun BoardContents(
    modifier: Modifier = Modifier,
    board: Board,
    onChangeContent: (Board) -> Unit = {},
) {
    var draggedTask by remember { mutableStateOf<Card?>(null) }
    var currentDragPosition by remember { mutableStateOf<Offset?>(null) }
    val columnBounds = remember { mutableStateMapOf<CardTaskState, Rect>() }

    Row(
        modifier = modifier
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var currentBoard by remember { mutableStateOf(board) }
        currentBoard = board

        fun taskEnd() {
            val dropPosition = currentDragPosition
            val targetStatus = columnBounds.entries
                .firstOrNull { (_, rect) -> dropPosition?.let { rect.contains(it) } == true }?.key

            draggedTask?.let { task ->
                if (targetStatus != null && task.taskState != targetStatus) {
                    onChangeContent(currentBoard.withUpdatedTaskState(task.id, targetStatus))
                    currentBoard = currentBoard.withUpdatedTaskState(task.id, targetStatus)
                }
            }
            currentDragPosition = null
            draggedTask = null
        }

        fun taskDragCancel() {
            currentDragPosition = null
            draggedTask = null
        }

        BoardCardColumn(
            modifier = Modifier
                .width(320.dp)
                .height(748.dp),
            filteredCards = board.cardsByState(CardTaskState.TODO),
            mode = CardTaskState.TODO,
            getIsDropTarget = {
                currentDragPosition?.let { columnBounds[CardTaskState.TODO]?.contains(it) } ?: false
            },
            onBoundsChanged = { rect -> columnBounds[CardTaskState.TODO] = rect },
            onTaskDragStart = { task -> draggedTask = task },
            onTaskDragChange = { pos -> currentDragPosition = pos },
            onTaskDragEnd = ::taskEnd,
            onTaskDragCancel = ::taskDragCancel,
        )
        BoardCardColumn(
            modifier = Modifier
                .width(320.dp)
                .height(748.dp),
            filteredCards = board.cardsByState(CardTaskState.IN_PROGRESS),
            mode = CardTaskState.IN_PROGRESS,
            getIsDropTarget = {
                currentDragPosition?.let { columnBounds[CardTaskState.IN_PROGRESS]?.contains(it) } ?: false
            },
            onBoundsChanged = { rect -> columnBounds[CardTaskState.IN_PROGRESS] = rect },
            onTaskDragStart = { task -> draggedTask = task },
            onTaskDragChange = { pos -> currentDragPosition = pos },
            onTaskDragEnd = ::taskEnd,
            onTaskDragCancel = ::taskDragCancel,
        )
        BoardCardColumn(
            modifier = Modifier
                .width(320.dp)
                .height(748.dp),
            filteredCards = board.cardsByState(CardTaskState.DONE),
            mode = CardTaskState.DONE,
            getIsDropTarget = {
                currentDragPosition?.let { columnBounds[CardTaskState.DONE]?.contains(it) } ?: false
            },
            onBoundsChanged = { rect -> columnBounds[CardTaskState.DONE] = rect },
            onTaskDragStart = { task -> draggedTask = task },
            onTaskDragChange = { pos -> currentDragPosition = pos },
            onTaskDragEnd = ::taskEnd,
            onTaskDragCancel = ::taskDragCancel,
        )
    }
}

/**
 * 보드 카드 컬럼입니다. 작업 상태에 따른 태스크 리스트 Column입니다.
 * @param filteredCards 필터링된 카드 리스트입니다.
 * @param mode 카드 상태입니다.
 * @param modifier Modifier
 * @param getIsDropTarget 드래그 타겟 여부를 리턴합니다.
 * @param onBoundsChanged 드래그 영역을 변경합니다.
 * @param onTaskDragStart 카드를 드래그 합니다.
 * @param onTaskDragChange 드래그 위치를 변경합니다.
 * @param onTaskDragEnd 드래그가 끝났을 때
 * @param onTaskDragCancel 드래그가 취소되었을 때
 */
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

data class BoardScreenPreviewState(
    val board: Board,
    val showCardCreationPanel: Boolean,
)

class BoardScreenPreviewProvider : PreviewParameterProvider<BoardScreenPreviewState> {
    override val values: Sequence<BoardScreenPreviewState>
        get() = sequenceOf(
            BoardScreenPreviewState(
                board = Board(emptyList()),
                showCardCreationPanel = false,
            ),
            BoardScreenPreviewState(
                board = Board(
                    listOf(
                        Card.create(
                            title = "UI 테스트용 1",
                            content = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
                            tags = listOf("UI", "테스트"),
                            manager = CardManagerState.DINO,
                            state = CardTaskState.TODO,
                        ),
                        Card.create(
                            title = "UI 테스트용 2 UI 테스트용 2 UI 테스트용 2",
                            content = "내용내용내용내용내용내용내용내용",
                            tags = listOf("UI", "테스트"),
                            manager = CardManagerState.DINO,
                            state = CardTaskState.IN_PROGRESS,
                        ),
                        Card.create(
                            title = "UI 테스트용 3 UI 테스트용 3 UI 테스트용 3",
                            content = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
                            tags = listOf("UI", "테스트"),
                            manager = CardManagerState.FAMES,
                            state = CardTaskState.DONE,
                        ),
                    ),
                ),
                showCardCreationPanel = false,
            ),
            BoardScreenPreviewState(
                board = Board(
                    listOf(
                        Card.create(
                            title = "UI 테스트용 1",
                            content = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
                            tags = listOf("UI", "테스트"),
                            manager = CardManagerState.DINO,
                            state = CardTaskState.DONE,
                        ),
                        Card.create(
                            title = "UI 테스트용 2 UI 테스트용 2 UI 테스트용 2",
                            content = "내용내용내용내용내용내용내용내용",
                            tags = listOf("UI", "테스트"),
                            manager = CardManagerState.DINO,
                            state = CardTaskState.DONE,
                        ),
                    ),
                ),
                showCardCreationPanel = false,
            ),
            BoardScreenPreviewState(
                board = Board(
                    listOf(
                        Card.create(
                            title = "태스크 생성 모달 테스트",
                            content = "모달이 열린 상태를 확인합니다.",
                            tags = listOf("모달"),
                            manager = CardManagerState.DINO,
                            state = CardTaskState.TODO,
                        ),
                    ),
                ),
                showCardCreationPanel = true,
            ),
        )
}

@Preview(
    name = "BoardScreen Preview",
    widthDp = 1295,
    heightDp = 909,
    showBackground = true,
)
@Composable
private fun BoardScreenPreview(
    @PreviewParameter(BoardScreenPreviewProvider::class)
    state: BoardScreenPreviewState,
) {
    BoardScreen(
        board = state.board,
        showCardCreationPanel = state.showCardCreationPanel,
        onAddCard = {},
        onShowCardCreationPanelChange = {},
        onBoardChange = { true },
        modifier = Modifier,
    )
}

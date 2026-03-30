package woowacourse.kanban.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.ui.board.common.toDisplayText

@Composable
fun CardScreen(
    modifier: Modifier = Modifier,
    cardData: Card,
    onDragStart: () -> Unit = {},
    onDragChange: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
) {
    var cardWindowPosition by remember { mutableStateOf(Offset.Zero) }

    CardScreen(
        modifier = modifier
            .testTag("카드_${cardData.id}")
            .onGloballyPositioned { cardWindowPosition = it.positionInWindow() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDrag = { change, _ ->
                        change.consume()
                        onDragChange(cardWindowPosition + change.position)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragCancel() },
                )
            },
        title = cardData.title,
        content = cardData.content,
        tags = cardData.tags,
        managerState = cardData.managerState,
        taskState = cardData.taskState,
        hasContent = cardData.hasContent(),
        hasTag = cardData.hasTag(),
    )
}

@Composable
fun CardScreen(
    title: String,
    content: String,
    tags: List<String>,
    managerState: CardManagerState,
    taskState: CardTaskState,
    hasContent: Boolean,
    hasTag: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xffffffff),
                shape = RoundedCornerShape(16.dp),
            )
            .border(
                color = Color(0xffE5E7Eb),
                width = 1.dp,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(all = 17.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        CardTitle(
            title = title,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Kanban Card Title"
                },
        )

        if (hasContent) {
            CardContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Kanban Card Content"
                    },
                content = content,
            )
        }

        if (hasTag) CardTagsSection(
            tags = tags,
        )

        HorizontalDivider()

        CardAccountInfo(
            accountName = managerState.toDisplayText(),
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Kanban Card Account Info"
                },
            accountImage = Icons.Default.AccountCircle, /* 추후 api나, Async 등으로 이미지를 불러올 경우 수정할 예정. */
        )
    }
}

/**
 * 최대 1줄까지 표시되는 Card의 Header입니다.
 * @param modifier Modifier
 * @param title 카드 제목으로, 너무 길면...로 표시됩니다.
 */
@Composable
private fun CardTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.3.sp,
        lineHeight = 24.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun CardTitlePreview() {
    CardTitle(title = "Card Title")
}


/**
 * 최대 2줄까지 표시되는 Card의 Content입니다.
 * @param modifier Modifier
 * @param content 카드 본문으로, 너무 길면 ...로 표시됩니다.
 */
@Composable
private fun CardContent(modifier: Modifier = Modifier, content: String) {
    Text(
        text = content,
        fontSize = 14.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.W400,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun CardContentPreview() {
    CardContent(content = "Card Content")
}

/**
 * CardTag 섹션입니다. TagChip이 표시됩니다.
 * @param tags 카드 태그로, 최대 5개까지 입력할 수 있습니다.
 */
@Composable
private fun CardTagsSection(tags: List<String> = listOf()) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {/* TagModifer, SectionModifier로 분리할까 고민했으나, 우선 현 방식대로 수정. */
        tags.forEach {
            TagChip(
                modifier = Modifier.semantics {
                    contentDescription = "Kanban Card Tag"
                },
                chipContent = it,
            )
        }
    }
}

/**
 * CardTag의 Chip입니다.
 * @param modifier Modifier
 * @param chipContent TagChip의 내용입니다.
 */
@Composable
private fun TagChip(modifier: Modifier = Modifier, chipContent: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = Color(0xfff3f4f6),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(vertical = 5.dp, horizontal = 8.dp),
    ) {
        Text(text = chipContent, fontWeight = FontWeight.W400, fontSize = 12.sp)
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun TagChipPreview() {
    TagChip(chipContent = "Tag")
}

/**
 * CardAccountInfo 섹션입니다.
 * @param modifier Modifier
 * @param accountImage 프로필 아이콘입니다. 기본 값은 Icons.Default.AccountCircle입니다.
 * @param accountName 카드 계정 이름으로, 너무 길면 ...로 표시됩니다.
 */
@Composable
private fun CardAccountInfo(
    accountName: String,
    modifier: Modifier = Modifier,
    accountImage: ImageVector = Icons.Default.AccountCircle,
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            imageVector = accountImage,
            contentDescription = "프로필 아이콘",
            modifier = Modifier.size(24.dp),
            tint = Color(0xff838383),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = accountName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun CardAccountInfoPreview() {
    CardAccountInfo(accountName = "Test")
}


package woowacourse.kanban.ui.card

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerStatus
import woowacourse.kanban.domain.card.CardTaskStatus

/**
 * 여러 케이스에 따른 KanbanCard의 Preview를 모아볼 수 있습니다.
 */
private class CardPreviewParameterProvider : PreviewParameterProvider<Card> {
    override val values: Sequence<Card> = sequenceOf(
        Card.create(
            title = "Lazy Column 컴포넌트 구현",
            content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
            tags = listOf("컴포넌트", "성능"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        ),
        Card.create(
            title = "Lazy Column 컴포넌트 구현",
            content = "",
            tags = listOf("컴포넌트", "성능"),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        ),
        Card.create(
            title = "Lazy Column 컴포넌트 구현",
            content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
            tags = emptyList(),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        ),
        Card.create(
            title = "Lazy Column 컴포넌트 구현",
            content = "",
            tags = emptyList(),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        ),
        Card.create(
            title = "너무너무 긴 제목은 한 줄까지만 노출너무너무 긴 제목은 한 줄까지만 노출",
            content = "너무너무너무 긴 설명은 두 줄까지만 노출하고 말줄임표로 처리합니다 두 줄까지만 노너무너무너무 긴 설명은 두 줄까지만 노출하고 말줄임표로 처리합니다 두 줄까지만 노",
            tags = listOf("너무너무", "긴 태그", "최대로", "5자까지진", "5개제한임"), /* 도메인 내 require문 수정에 따른 Tag 수정*/
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        ),
    )
}

@Preview(showBackground = true, name = "KanbanBoardCard")
@Composable
private fun CardPreview(
    @PreviewParameter(CardPreviewParameterProvider::class) cardData: Card,
) {
    CardScreen(
        modifier = Modifier.width(286.dp),
        cardData = cardData,
    )
}

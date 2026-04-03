package woowacourse.kanban.domain.card

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [Card] Unit 테스트 클래스입니다.
 */
class CardDataTest {
    @Test
    fun `제목이 공백 문자만 있으면 생성할 수 없다`() {
        val invalidTitles = listOf("   ", "\t", "\n", " \n\t ")

        invalidTitles.forEach { title ->
            assertFailsWith<IllegalArgumentException> {
                Card.create(
                    title = title,
                    content = "",
                    tags = emptyList(),
                    manager = CardManagerStatus.DINO,
                    state = CardTaskStatus.TODO,
                )
            }
        }
    }

    @Test
    fun `내용이 있으면 hasContent 리턴 값은 true이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = emptyList(),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertTrue(cardData.hasContent())
    }

    @Test
    fun `내용이 공백이면 hasContent 리턴 값은 false이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "   ",
            tags = emptyList(),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertFalse(cardData.hasContent())
    }

    @Test
    fun `태그의 앞뒤 공백은 제거된다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf(" 태그1 ", "  태그2  "),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertEquals(listOf("태그1", "태그2"), cardData.tags)
    }

    @Test
    fun `공백으로만 구성된 태그는 제거된다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "   ", "", "  "),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertEquals(listOf("태그1"), cardData.tags)
    }

    @Test
    fun `태그가 5개를 초과하면 생성할 수 없다`() {
        assertFailsWith<IllegalArgumentException> {
            Card.create(
                title = "제목",
                content = "내용",
                tags = listOf("태그1", "태그2", "태그3", "태그4", "태그5", "태그6"),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            )
        }
    }

    @Test
    fun `태그 내용이 5글자를 초과하면 5글자까지만 유지된다`() {
        assertFailsWith<IllegalArgumentException> {
            Card.create(
                title = "제목",
                content = "내용",
                tags = listOf("우아한테크코스", "안드로이드8기", "칸반보드리팩터링"),
                manager = CardManagerStatus.DINO,
                state = CardTaskStatus.TODO,
            )
        }
    }

    @Test
    fun `태그가 있으면 hasTag 리턴 값은 true이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "   "),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertTrue(cardData.hasTag())
    }

    @Test
    fun `태그가 비어 있으면 hasTag 리턴 값은 false이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("   ", ""),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertFalse(cardData.hasTag())
    }

    @Test
    fun `잘못된 태그 문자열이 주어질 시 false가 반환된다`() {
        assertThat(Card.isValidTag(",...")).isEqualTo(false)
    }

    @Test
    fun `잘못된 태그 문자열이 주어질 시 에러메시지가 반환된다`() {
        assertEquals("태그 형식이 올바르지 않습니다.", Card.isValidTagInfo(",..."))
        assertEquals("태그는 5자 이내로 5개까지만 등록할 수 있습니다.", Card.isValidTagInfo("태그1,태그2,태그3,태그4,태그5,태그6"))
    }

    @Test
    fun `쉼표를 기준으로 태그 문자열을 분리한다`() {
        assertEquals(
            listOf("태그1", "태그2", "태그3"),
            Card.parseTag("태그1,태그2,태그3"),
        )
    }

    @Test
    fun `태그 문자열의 앞뒤 공백을 제거한 후 쉼표를 기준으로 분리한다`() {
        assertEquals(
            listOf("태그1", "태그2"),
            Card.parseTag("태그1,태그2   "),
        )
    }

    @Test
    fun `카드의 타이틀이 중복되어도 고유 ID로 카드를 구분할 수 있다`() {
        val title: String = "중복 타이틀"
        val card1 = Card.create(
            title = title,
            content = "내용",
            tags = listOf("   ", ""),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )
        val card2 = Card.create(
            title = title,
            content = "내용",
            tags = listOf("   ", ""),
            manager = CardManagerStatus.DINO,
            state = CardTaskStatus.TODO,
        )

        assertThat(card1.id).isNotEqualTo(card2.id)
    }
}

package woowacourse.kanban.ui.card

import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.domain.dialog.DialogStateHolder
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CardCreationScreenTest {
    @Test
    fun `초기 진입 시 기본 UI 상태가 올바르게 표시된다`() = runComposeUiTest {
        //when
        setContent {
            val state = remember { DialogStateHolder(
                onCardCreate = {},
                onCancel = { },
            ) }

            CardCreationScreen(
                state = state,
            )
        }

        onNodeWithText("새 태스크 생성").assertExists()
        onNodeWithText("제목을 입력해 주세요.").assertExists()
        onNodeWithText("제목 *").assertExists()
        onNodeWithText("설명").assertExists()
        onNodeWithText("태그").assertExists()
        onNodeWithText("상태 *").assertExists()
        onNodeWithText("담당자").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `제목을 입력하지 않으면, 생성 버튼이 비활성화된다`() = runComposeUiTest {
        // given

        //when
        setContent {
            val state = remember { DialogStateHolder(
                onCardCreate = {},
                onCancel = { },
            ) }

            CardCreationScreen(
                state = state,
            )
        }
        onNodeWithTag("titleTextField").performTextInput(" ")

        //then
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `제목을 입력하지 않으면 에러메시지가 노출된다`() = runComposeUiTest {
        // given
        //when
        setContent {
            val state = remember { DialogStateHolder(
                onCardCreate = {},
                onCancel = { },
            ) }

            CardCreationScreen(
                state = state,
            )
        }
        onNodeWithTag("titleTextField").performTextInput("\t")
        //then
        onNodeWithText("제목을 입력해 주세요.").assertExists()
    }

    @Test
    fun `제목을 입력하면 제목 에러메시지가 사라진다`() = runComposeUiTest {
        //when
        setContent {
            val state = remember { DialogStateHolder(
                onCardCreate = {},
                onCancel = { },
            ) }

            CardCreationScreen(
                state = state,
            )
        }

        //then
        onNodeWithText("태스크 제목을 입력하세요").performTextInput("제목입니다~")

        onNodeWithText("제목을 입력해 주세요.").assertDoesNotExist()
    }

    @Test
    fun `올바른 태그를 입력하면 안내 문구가 유지된다`() = runComposeUiTest {
        //when
        setContent {
            val state = remember { DialogStateHolder(
                onCardCreate = {},
                onCancel = { },
            ) }

            CardCreationScreen(
                state = state,
            )
        }

        //then
        onNodeWithTag("tagTextField")
            .performTextInput("버그,긴급")

        onNodeWithText("5자 이내의 태그를 최대 5개까지 등록할 수 있습니다.").assertExists()
    }
}

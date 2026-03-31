package woowacourse.kanban.board

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import woowacourse.kanban.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kanban Board",
    ) {
        App()
    }
}

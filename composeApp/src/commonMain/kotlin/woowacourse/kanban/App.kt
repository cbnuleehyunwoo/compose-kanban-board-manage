package woowacourse.kanban

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.ui.project.ProjectScreen

@Composable
fun App() {
    MaterialTheme {
        ProjectScreen()
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, device = DESKTOP)
private fun AppPreview() {
    ProjectScreen()
}

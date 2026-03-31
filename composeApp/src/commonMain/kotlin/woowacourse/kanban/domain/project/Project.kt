package woowacourse.kanban.domain.project

import woowacourse.kanban.domain.board.Board

class Project(
    boardList: List<Board> = emptyList(),
    private val selectedBoardIndex: Int = 0,
    private val projectTitle: String = "",
    private val projectDescription: String = "",
) {
    val boards: List<Board> = boardList.ifEmpty { listOf(Board()) }
    val getTitle = projectTitle
    val getDescription = projectDescription
    val currentBoardIndex: Int = selectedBoardIndex
    val selectedBoard: Board = boards[selectedBoardIndex]

    fun switchBoard(newBoardIndex: Int): Project = Project(
        boardList = boards,
        selectedBoardIndex = newBoardIndex,
        projectTitle = projectTitle,
        projectDescription = projectDescription,
    )

    fun withBoard(newBoard: Board): Project = Project(
        boardList = boards.map { if (it.boardId == newBoard.boardId) newBoard else it },
        selectedBoardIndex = selectedBoardIndex,
        projectTitle = projectTitle,
        projectDescription = projectDescription,
    )
}

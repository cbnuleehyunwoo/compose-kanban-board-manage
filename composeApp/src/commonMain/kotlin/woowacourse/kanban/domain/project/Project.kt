package woowacourse.kanban.domain.project

import woowacourse.kanban.domain.board.Board

class Project(
    boardList: List<Board> = emptyList(),
    private val selectedBoardIndex: Int = 0,
    private val projectTitle: String = "",
    private val projectDescription: String ="",
) {
    val boards: List<Board> = boardList.ifEmpty { listOf(Board()) }
    val getTitle = projectTitle
    val getDescription = projectDescription
    val currentBoardIndex: Int = selectedBoardIndex
    val selectedBoard: Board = boards[selectedBoardIndex]

    /**
     * BoardList의 Index를 활용하여 프로젝트 탭에서 선택한 보드로 변경합니다.
     * 추후 보드 생성/수정/삭제 기능 요구사항이 추가될 시 id를 활용하도록 리팩토링할 수 있습니다.
     * @param newBoardIndex 변경할 보드의 인덱스입니다.
     * @return 변경된 Project 객체입니다.
     */
    fun switchBoard(newBoardIndex: Int): Project = Project(
        boardList = boards,
        selectedBoardIndex = newBoardIndex,
        projectTitle = projectTitle,
        projectDescription = projectDescription,
    )

    /**
     * BoardList에 새로운 Board를 추가합니다. Board ID를 활용하여 수정사항이 생긴 Board를 교체합니다.
     * @param newBoard 추가할 Board 객체입니다.
     * @return 변경된 Project 객체입니다.
     */
    fun withUpdateBoard(newBoard: Board): Project = Project(
        boardList = boards.map { if (it.boardId == newBoard.boardId) newBoard else it },
        selectedBoardIndex = selectedBoardIndex,
        projectTitle = projectTitle,
        projectDescription = projectDescription,
    )
}

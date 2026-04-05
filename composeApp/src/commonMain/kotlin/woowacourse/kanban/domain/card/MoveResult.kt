package woowacourse.kanban.domain.card

sealed class MoveResult {
    data class Success(val updatedCard: Card): MoveResult()
    data class Failure(val reason: MoveFailureReason) : MoveResult()
}

enum class MoveFailureReason {
    INVALID_TRANSITION,
    INVALID_MANAGER,
}

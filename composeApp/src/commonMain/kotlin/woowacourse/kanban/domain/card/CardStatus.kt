package woowacourse.kanban.domain.card

enum class CardTaskStatus {
    TODO,
    IN_PROGRESS,
    REVIEW,
    DONE;

    fun isTargetValid(target: CardTaskStatus): Boolean {
        return when (this) {
            TODO -> target == IN_PROGRESS
            IN_PROGRESS -> target == REVIEW || target == DONE
            REVIEW -> target == IN_PROGRESS || target == DONE
            DONE -> target == TODO
        }
    }
}

enum class CardManagerStatus {
    NONE,
    DINO,
    FAMES,
}

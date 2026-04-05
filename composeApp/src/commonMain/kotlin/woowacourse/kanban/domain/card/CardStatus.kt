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

    fun isAssigneeRequired(): Boolean {
        return when (this) {
            TODO -> false
            IN_PROGRESS -> true
            REVIEW -> true
            DONE -> true
        }
    }

    fun isDeletable(): Boolean {
        return when (this) {
            TODO -> true
            IN_PROGRESS -> true
            REVIEW -> false
            DONE -> false
        }
    }
}

enum class CardManagerStatus {
    NONE,
    DINO,
    FAMES,
}

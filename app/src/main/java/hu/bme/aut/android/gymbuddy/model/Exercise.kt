package hu.bme.aut.android.gymbuddy.model

data class Exercise(
    val name: String,
    val muscleGroup: MuscleGroup,
    val weightData: List<Set>
)

enum class MuscleGroup {
    Arms,
    Back,
    Chest,
    Legs,
    Shoulders,
    FullBody
}

data class Set(
    val reps: List<Rep>
)

data class Rep(
    val count: Int,
    val weight: Int
)


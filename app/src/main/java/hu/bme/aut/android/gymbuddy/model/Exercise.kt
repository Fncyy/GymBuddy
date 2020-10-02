package hu.bme.aut.android.gymbuddy.model

import java.util.*

data class Exercise(
    var name: String,
    var muscleGroup: MuscleGroup,
    var weightData: MutableList<Set> = mutableListOf(),
    var id: String? = null
) {
    fun asDbExercise() = DbExercise(name, muscleGroup, weightData)
}

data class DbExercise(
    var name: String = "",
    var muscleGroup: MuscleGroup = MuscleGroup.Arms,
    var weightData: MutableList<Set> = mutableListOf()
)

enum class MuscleGroup {
    Muscles,
    Arms,
    Back,
    Chest,
    Legs,
    Shoulders,
    FullBody
}

data class Set(
    var reps: MutableList<Rep> = mutableListOf()
)

data class Rep(
    var count: Int = -1,
    var weight: Int = -1
)


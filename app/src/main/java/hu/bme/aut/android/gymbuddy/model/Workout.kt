package hu.bme.aut.android.gymbuddy.model

data class Workout(
    var name: String,
    var id: String? = null,
    var exercises: MutableList<Exercise> = mutableListOf()
) {
    fun asDbWorkout() = DbWorkout(name, exercises.map { exercise -> exercise.id!! }.toList())
}

data class DbWorkout(
    var name: String = "",
    var exercises: List<String> = mutableListOf()
)
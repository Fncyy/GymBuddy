package hu.bme.aut.android.gymbuddy.model

data class Workout(
    val name: String,
    val exercises: List<Exercise>
) {
    fun getMuscleGroups(): String {
        val groups: MutableSet<MuscleGroup> = mutableSetOf()
        exercises.forEach { groups.add(it.muscleGroup) }
        return groups.sorted().joinToString(", ")
    }
}
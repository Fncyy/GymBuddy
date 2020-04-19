package hu.bme.aut.android.gymbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.model.Workout

class WorkoutAdapter : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    private val workouts: MutableList<Workout> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var workout: Workout? = null
        val tvWorkoutName: TextView = itemView.findViewById(R.id.tvWorkoutName)
        val tvMuscleGroup: TextView = itemView.findViewById(R.id.tvMuscleGroup)
    }

    fun addWorkout(workout: Workout) {
        val size = workouts.size
        workouts.add(workout)
        notifyItemInserted(size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = workouts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workouts[position]
        holder.workout = workout
        holder.tvWorkoutName.text = workout.name
        holder.tvMuscleGroup.text = workout.getMuscleGroups()
    }
}
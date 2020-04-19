package hu.bme.aut.android.gymbuddy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.model.Exercise
import kotlinx.android.synthetic.main.exercise_item.view.*

class ExerciseAdapter(private val context: Context) :
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    private val exerciseList: MutableList<Exercise> = mutableListOf()
    private var lastPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var exercise: Exercise? = null
        val tvExerciseName: TextView = itemView.tvExerciseName
        val tvMuscleGroup: TextView = itemView.tvMuscleGroup
    }

    fun addExercise(exercise: Exercise) {
        val size = exerciseList.size
        exerciseList.add(exercise)
        notifyItemInserted(size)
    }

    fun clearAndFill(exercises: List<Exercise>) {
        exerciseList.clear()
        exerciseList.addAll(exercises)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = exerciseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.exercise = exercise
        holder.tvExerciseName.text = exercise.name
        holder.tvMuscleGroup.text = exercise.muscleGroup.name
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}

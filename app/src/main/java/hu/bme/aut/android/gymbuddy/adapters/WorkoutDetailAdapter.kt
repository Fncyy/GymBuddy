package hu.bme.aut.android.gymbuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.gymbuddy.BaseActivity
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.model.Exercise
import hu.bme.aut.android.gymbuddy.model.Rep
import hu.bme.aut.android.gymbuddy.model.Set
import hu.bme.aut.android.gymbuddy.model.Workout

class WorkoutDetailAdapter(private val workout: Workout) :
    RecyclerView.Adapter<WorkoutDetailAdapter.ViewHolder>() {
    private val exercises: MutableList<Exercise> =
        workout.exercises.map { it.copy() }.toMutableList()
    private val finishedExercises: MutableList<Exercise> = mutableListOf()
    private var layoutInflater: LayoutInflater? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var exercise: Exercise? = null
        val btnRemoveExercise: Button = itemView.findViewById(R.id.btnRemoveExercise)
        val btnExerciseSave: Button = itemView.findViewById(R.id.btnExerciseSave)
        val tvExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
        val tvMuscleGroup: TextView = itemView.findViewById(R.id.tvMuscleGroup)

        private val etRep0: EditText = itemView.findViewById(R.id.etRep0)
        private val etRep1: EditText = itemView.findViewById(R.id.etRep1)
        private val etRep2: EditText = itemView.findViewById(R.id.etRep2)
        private val etWeight0: EditText = itemView.findViewById(R.id.etWeight0)
        private val etWeight1: EditText = itemView.findViewById(R.id.etWeight1)
        private val etWeight2: EditText = itemView.findViewById(R.id.etWeight2)
        private val btnClear0: ImageButton = itemView.findViewById(R.id.btnExerciseClear0)
        private val btnClear1: ImageButton = itemView.findViewById(R.id.btnExerciseClear1)
        private val btnClear2: ImageButton = itemView.findViewById(R.id.btnExerciseClear2)

        val etReps = listOf(etRep0, etRep1, etRep2)
        val etWeights = listOf(etWeight0, etWeight1, etWeight2)
        val btnClears = listOf(btnClear0, btnClear1, btnClear2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = layoutInflater ?: LayoutInflater.from(parent.context)
        val view = layoutInflater!!.inflate(R.layout.workout_exercise_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = exercises.size

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exercise = exercise
        holder.btnRemoveExercise.setOnClickListener {
            removeExercise(position)
        }

        holder.btnExerciseSave.setOnClickListener {
            exercise.weightData.add(0, Set())
            for (i in 0 until 3) {
                val etRep = holder.etReps[i]
                val etWeight = holder.etWeights[i]
                if (etRep.text.isNotEmpty() && etWeight.text.isNotEmpty()) {
                    exercise.weightData.first().reps.add(
                        Rep(
                            count = etRep.text.toString().toInt(),
                            weight = etWeight.text.toString().toInt()
                        )
                    )
                }
            }
            if (exercise.weightData.size > 5)
                exercise.weightData.removeLast()
            finishedExercises.add(exercise)
            removeExercise(position)
        }

        holder.tvExerciseName.text = exercise.name
        holder.tvMuscleGroup.text = exercise.muscleGroup.toString()
        val reps = when (exercise.weightData.size != 0) {
            true -> exercise.weightData.first().reps
            false -> mutableListOf()
        }

        for (i in 0 until 3) {
            holder.btnClears[i].setOnClickListener {
                holder.etReps[i].setText("")
                holder.etWeights[i].setText("")
            }
            holder.etReps[i].setText("")
            holder.etWeights[i].setText("")
        }

        for (i in 0 until reps.size) {
            holder.etReps[i].setText(reps[i].count.toString())
            holder.etWeights[i].setText(reps[i].weight.toString())
        }
    }

    fun saveData() {
        val dbExercise = FirebaseDatabase.getInstance().reference
            .child(BaseActivity.uid!!)
            .child(GymActivity.EXERCISES)
        finishedExercises.forEach {
            dbExercise.child(it.id!!).child("weightData").setValue(it.weightData)
        }
        workout.exercises = exercises
        workout.exercises.addAll(finishedExercises)
        FirebaseDatabase.getInstance().reference
            .child(BaseActivity.uid!!)
            .child(GymActivity.WORKOUTS)
            .child(workout.id!!)
            .setValue(workout.asDbWorkout())
    }

    fun addExercise(exercise: Exercise) {
        exercises.add(0, exercise)
        notifyDataSetChanged()
    }

    private fun removeExercise(position: Int) {
        exercises.removeAt(position)
        notifyDataSetChanged()
    }
}
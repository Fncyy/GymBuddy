package hu.bme.aut.android.gymbuddy.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.gymbuddy.BaseActivity
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.model.Exercise
import hu.bme.aut.android.gymbuddy.model.MuscleGroup
import kotlinx.android.synthetic.main.edit_exercise.view.*
import kotlinx.android.synthetic.main.exercise_item.view.*

class ExerciseAdapter(private val context: Context) :
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    private var lastPosition = -1

    class ViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        var exercise: Exercise? = null
        val tvExerciseName: TextView = itemView.tvExerciseName
        val tvMuscleGroup: TextView = itemView.tvMuscleGroup
        val btnEditExercise: ImageButton = itemView.btnEditExercise
        val btnDeleteExercise: ImageButton = itemView.btnDeleteExercise

        init {
            itemView.setOnClickListener {
                val bundle = bundleOf(
                    context.getString(R.string.EXERCISE_ID) to exercise!!.id,
                    context.getString(R.string.TITLE) to exercise!!.name
                )
                GymActivity.exercises.first().weightData.forEach { set ->
                    set.reps.forEach { rep ->
                        Log.d("exercise", "${rep.count} ${rep.weight}")
                    }
                }
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_nav_exercises_to_nav_progress, bundle)
            }
        }
    }

    init {
        GymActivity.onDataChangedListener.add { notifyDataSetChanged() }
    }

    fun addExercise(exercise: Exercise) {
        GymActivity.exercises.add(exercise)
        val key = FirebaseDatabase.getInstance().reference.child(GymActivity.EXERCISES).push().key
            ?: return
        exercise.id = key
        FirebaseDatabase.getInstance().reference
            .child("${BaseActivity.uid}/${GymActivity.EXERCISES}/$key")
            .setValue(exercise.asDbExercise())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount() = GymActivity.exercises.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = GymActivity.exercises[position]
        holder.exercise = exercise
        holder.tvExerciseName.text = exercise.name
        holder.tvMuscleGroup.text = exercise.muscleGroup.name
        holder.btnEditExercise.setOnClickListener {
            createDialog(exercise)
        }
        holder.btnDeleteExercise.setOnClickListener {
            exerciseDeleteAndCleanup(exercise)
        }
        setAnimation(holder.itemView, position)
    }

    private fun createDialog(exercise: Exercise?) {
        if (exercise == null)
            return
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_exercise, null)
        dialogView.spnMuscleGroups.adapter = MuscleGroupAdapter(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            MuscleGroup.values()
        )
        dialogView.etExerciseName.setText(exercise.name)
        dialogView.spnMuscleGroups.setSelection(exercise.muscleGroup.ordinal)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Editing ${exercise.name}")
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        alertDialog.setOnShowListener {
            val positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positive.setOnClickListener {
                val name = dialogView.etExerciseName
                val spinner = dialogView.spnMuscleGroups
                if (name.text.isNullOrEmpty()) {
                    Toast.makeText(context, "Form is not complete", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val newExercise = Exercise(
                        name = name.text.toString(),
                        muscleGroup = spinner.selectedItem as MuscleGroup,
                        id = exercise.id,
                        weightData = exercise.weightData
                    )
                    GymActivity.exercises[GymActivity.exercises.indexOf(exercise)] = newExercise
                    FirebaseDatabase.getInstance().reference
                        .child(BaseActivity.uid!!)
                        .child(GymActivity.EXERCISES)
                        .child(newExercise.id!!)
                        .setValue(newExercise)
                    alertDialog.dismiss()
                    notifyDataSetChanged()
                }
            }
        }
        alertDialog.show()
    }

    private fun exerciseDeleteAndCleanup(exercise: Exercise?) {
        if (exercise == null)
            return
        val database = FirebaseDatabase.getInstance().reference.child(BaseActivity.uid!!)
        GymActivity.exercises.remove(exercise)
        database
            .child(GymActivity.EXERCISES)
            .child(exercise.id!!)
            .removeValue()
        GymActivity.workouts.forEach {
            if (it.exercises.contains(exercise)) {
                it.exercises.remove(exercise)
                database
                    .child(GymActivity.WORKOUTS)
                    .child(it.id!!)
                    .setValue(it.asDbWorkout())
            }
        }
        notifyDataSetChanged()
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)
        lastPosition = position
    }
}

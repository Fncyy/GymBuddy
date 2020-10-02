package hu.bme.aut.android.gymbuddy.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.gymbuddy.BaseActivity
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.model.Workout

class WorkoutAdapter(private val context: Context) :
    RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var workout: Workout? = null
        val tvWorkoutName: TextView = itemView.findViewById(R.id.tvWorkoutName)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnWorkoutDelete)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnWorkoutEdit)

        init {
            itemView.setOnClickListener {
                val bundle = bundleOf(
                    context.getString(R.string.WORKOUT_ID) to workout!!.id,
                    context.getString(R.string.TITLE) to workout!!.name
                )
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_nav_home_to_nav_home_details, bundle)
            }
        }
    }

    init {
        GymActivity.onDataChangedListener.add { notifyDataSetChanged() }
    }

    fun addWorkout(workout: Workout) {
        val key = FirebaseDatabase.getInstance().reference.child("workouts").push().key ?: return
        workout.id = key
        FirebaseDatabase.getInstance().reference
            .child("${BaseActivity.uid}/${GymActivity.WORKOUTS}/$key")
            .setValue(workout.asDbWorkout())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount() = GymActivity.workouts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = GymActivity.workouts[position]
        holder.workout = workout
        holder.tvWorkoutName.text = workout.name
        holder.btnDelete.setOnClickListener {
            GymActivity.workouts.removeAt(position)
            FirebaseDatabase.getInstance().reference
                .child(BaseActivity.uid!!)
                .child(GymActivity.WORKOUTS)
                .child(workout.id!!)
                .removeValue()
            notifyDataSetChanged()
        }
        holder.btnEdit.setOnClickListener {
            val input = EditText(context)
            input.hint = "Name"
            input.setText(workout.name)
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Enter workout name")
                .setView(input)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    if (input.text.isNotEmpty()) {
                        workout.name = input.text.toString()
                        FirebaseDatabase.getInstance().reference
                            .child(BaseActivity.uid!!)
                            .child(GymActivity.WORKOUTS)
                            .child(workout.id!!)
                            .child(Workout::name.name)
                            .setValue(input.text.toString())
                        notifyDataSetChanged()
                        alertDialog.dismiss()
                    }
                    else
                        Toast.makeText(context, "Name cannot be empty!", Toast.LENGTH_LONG).show()
                }
            }
            alertDialog.show()
        }
    }
}
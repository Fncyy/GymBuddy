package hu.bme.aut.android.gymbuddy.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.adapters.ExerciseAdapter
import hu.bme.aut.android.gymbuddy.adapters.MuscleGroupAdapter
import hu.bme.aut.android.gymbuddy.model.Exercise
import hu.bme.aut.android.gymbuddy.model.MuscleGroup
import hu.bme.aut.android.gymbuddy.model.Rep
import hu.bme.aut.android.gymbuddy.model.Set
import kotlinx.android.synthetic.main.edit_exercise.*
import kotlinx.android.synthetic.main.edit_exercise.view.*

class ExercisesFragment : Fragment() {

    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseAdapter = ExerciseAdapter(requireContext())
        val rvExercises: RecyclerView = view.findViewById(R.id.rvExercises)
        rvExercises.layoutManager = LinearLayoutManager(requireContext())
        rvExercises.adapter = exerciseAdapter
        val fab: FloatingActionButton? = activity?.findViewById(R.id.fab)
        fab?.visibility = View.VISIBLE
        fab?.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_exercise, null)
            dialogView.spnMuscleGroups.adapter = MuscleGroupAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                MuscleGroup.values()
            )

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle(R.string.add_exercise)
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()

            alertDialog.setOnShowListener {
                val positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positive.setOnClickListener {
                    val name = dialogView.etExerciseName
                    val spinner = dialogView.spnMuscleGroups
                    if (name.text.isNullOrEmpty() || spinner.selectedItemPosition == 0) {
                        Toast.makeText(requireContext(), "Form is not complete", Toast.LENGTH_LONG)
                            .show()
                    }
                    else {
                        exerciseAdapter.addExercise(
                            Exercise(
                                name.text.toString(),
                                spinner.selectedItem as MuscleGroup
                            )
                        )
                        alertDialog.dismiss()
                    }
                }
            }
            alertDialog.show()
        }
    }
}

package hu.bme.aut.android.gymbuddy.ui.workoutDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.adapters.ExerciseSpinnerAdapter
import hu.bme.aut.android.gymbuddy.adapters.WorkoutDetailAdapter
import hu.bme.aut.android.gymbuddy.model.Exercise

class WorkoutDetailsFragment : Fragment() {

    private lateinit var workoutDetailAdapter: WorkoutDetailAdapter
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val workoutID = arguments?.getString(getString(R.string.WORKOUT_ID))
        for (workout in GymActivity.workouts)
            if (workout.id == workoutID) {
                workoutDetailAdapter = WorkoutDetailAdapter(workout)
                break
            }
        val rvWorkoutExercises: RecyclerView = view.findViewById(R.id.rvWorkoutExercises)
        rvWorkoutExercises.layoutManager = LinearLayoutManager(requireContext())
        rvWorkoutExercises.adapter = workoutDetailAdapter
        spinner = view.findViewById(R.id.spnExercises)
        val spinnerAdapter = ExerciseSpinnerAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, GymActivity.exercises)
        spinner.adapter = spinnerAdapter
        view.findViewById<Button>(R.id.btnWorkoutDetailsAddExercise).setOnClickListener {
            workoutDetailAdapter.addExercise(spinner.selectedItem as Exercise)
        }
        view.findViewById<Button>(R.id.btnWorkoutDetailsSave).setOnClickListener {
            workoutDetailAdapter.saveData()
            Navigation.findNavController(view).popBackStack()
        }
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE
    }
}

package hu.bme.aut.android.gymbuddy.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.adapters.ExerciseAdapter
import hu.bme.aut.android.gymbuddy.model.Exercise
import hu.bme.aut.android.gymbuddy.model.MuscleGroup
import hu.bme.aut.android.gymbuddy.model.Rep
import hu.bme.aut.android.gymbuddy.model.Set

class ExercisesFragment : Fragment() {

    private lateinit var exercisesViewModel: ExercisesViewModel
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exercisesViewModel =
            ViewModelProviders.of(this).get(ExercisesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseAdapter = ExerciseAdapter(requireContext())
        val rvExercises: RecyclerView = view.findViewById(R.id.rvExercises)
        rvExercises.layoutManager = LinearLayoutManager(requireContext())
        rvExercises.adapter = exerciseAdapter
        val fab: FloatingActionButton? = activity?.findViewById(R.id.fab)
        fab?.setOnClickListener {
            exerciseAdapter.addExercise(Exercise("Exercise A", MuscleGroup.Arms, listOf(Set(listOf(Rep(10, 30))))))
        }
        for (i in 0 until 10)
            exerciseAdapter.addExercise(Exercise("Exercise $i", MuscleGroup.Arms, listOf(Set(listOf(Rep(10, 30))))))
    }
}

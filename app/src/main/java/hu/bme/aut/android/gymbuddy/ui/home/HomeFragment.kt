package hu.bme.aut.android.gymbuddy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.adapters.WorkoutAdapter
import hu.bme.aut.android.gymbuddy.model.*
import hu.bme.aut.android.gymbuddy.model.Set

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutAdapter = WorkoutAdapter()
        val rvWorkouts: RecyclerView = view.findViewById(R.id.rvWorkouts)
        rvWorkouts.layoutManager = GridLayoutManager(requireContext(), 2)
        rvWorkouts.adapter = workoutAdapter
        val fab: FloatingActionButton? = activity?.findViewById(R.id.fab)
        fab?.setOnClickListener {
            workoutAdapter.addWorkout(Workout("Workout A", listOf(
                Exercise("Exercise 1", MuscleGroup.Arms, listOf(Set(listOf(Rep(12, 10))))),
                Exercise("Exercise 2", MuscleGroup.Chest, listOf(Set(listOf(Rep(10, 30))))),
                Exercise("Exercise 3", MuscleGroup.Back, listOf(Set(listOf(Rep(15, 20))))),
                Exercise("Exercise 4", MuscleGroup.Legs, listOf(Set(listOf(Rep(8, 15)))))
            )))
        }
        fab?.performClick()
        fab?.performClick()
        fab?.performClick()
        fab?.setOnClickListener {
            workoutAdapter.addWorkout(Workout("Workout A", listOf(
                Exercise("Exercise 1", MuscleGroup.Arms, listOf(Set(listOf(Rep(12, 10))))),
                Exercise("Exercise 2", MuscleGroup.Chest, listOf(Set(listOf(Rep(10, 30))))),
                Exercise("Exercise 3", MuscleGroup.Back, listOf(Set(listOf(Rep(15, 20))))),
                Exercise("Exercise 4", MuscleGroup.Legs, listOf(Set(listOf(Rep(8, 15))))),
                Exercise("Exercise 5", MuscleGroup.FullBody, listOf(Set(listOf(Rep(8, 15))))),
                Exercise("Exercise 6", MuscleGroup.Shoulders, listOf(Set(listOf(Rep(8, 15)))))
            )))
        }
    }
}

package hu.bme.aut.android.gymbuddy.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.gymbuddy.BaseActivity
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.adapters.WorkoutAdapter
import hu.bme.aut.android.gymbuddy.model.*
import hu.bme.aut.android.gymbuddy.model.Set
import java.lang.invoke.WrongMethodTypeException
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        workoutAdapter = WorkoutAdapter(context)
        val rvWorkouts: RecyclerView = view.findViewById(R.id.rvWorkouts)
        rvWorkouts.layoutManager = LinearLayoutManager(context)
        rvWorkouts.adapter = workoutAdapter
        val fab: FloatingActionButton? = activity?.findViewById(R.id.fab)
        fab?.visibility = View.VISIBLE
        fab?.setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "Name"
            val alertDialog = AlertDialog.Builder(requireContext())
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
                        workoutAdapter.addWorkout(Workout(input.text.toString()))
                        alertDialog.dismiss()
                    } else
                        Toast.makeText(context, "Name cannot be empty!", Toast.LENGTH_LONG).show()
                }
            }
            alertDialog.show()

        }
    }
}

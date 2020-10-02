package hu.bme.aut.android.gymbuddy.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hu.bme.aut.android.gymbuddy.GymActivity
import hu.bme.aut.android.gymbuddy.R
import hu.bme.aut.android.gymbuddy.model.Exercise
import kotlinx.android.synthetic.main.fragment_progress.view.*


class ProgressFragment : Fragment() {

    private lateinit var exercise: Exercise

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        val chart = view.chartExerciseData
        val leftAxis = chart.axisLeft
        val rightAxis = chart.axisRight
        val xAxis = chart.xAxis
        val yTextSize = 15f


        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawLabels(false)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)

        leftAxis.textSize = yTextSize
        leftAxis.setDrawLabels(true)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)

        rightAxis.textSize = yTextSize
        rightAxis.setDrawLabels(true)
        rightAxis.setDrawAxisLine(true)
        rightAxis.setDrawGridLines(false)

        val exerciseID = arguments?.getString(getString(R.string.EXERCISE_ID))
        for (exercise in GymActivity.exercises)
            if (exercise.id == exerciseID) {
                this.exercise = exercise
                break
            }
        val entries: MutableList<BarEntry> = ArrayList()
        var i = 5f
        exercise.weightData.forEach { set ->
            var sum = 0
            set.reps.forEach {
                sum += it.weight * it.count
            }
            entries.add(BarEntry(i--, sum.toFloat()))
        }

        val dataSet = BarDataSet(entries, "Total weight")
        dataSet.color = R.color.colorPrimaryDark
        val data = BarData(dataSet)
        data.isHighlightEnabled = false
        data.setValueTextSize(15f)

        chart.data = data
        chart.description.isEnabled = false
        chart.legend.textSize = 15f

        chart.invalidate()
    }
}

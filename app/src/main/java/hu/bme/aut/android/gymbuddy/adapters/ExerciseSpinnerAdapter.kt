package hu.bme.aut.android.gymbuddy.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hu.bme.aut.android.gymbuddy.model.Exercise

class ExerciseSpinnerAdapter(context: Context, resource: Int, private val objects: MutableList<Exercise>) :
    ArrayAdapter<Exercise>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = objects[position].name
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = "${objects[position].name} (${objects[position].muscleGroup})"
        return label
    }
}
package hu.bme.aut.android.gymbuddy.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hu.bme.aut.android.gymbuddy.model.MuscleGroup


class MuscleGroupAdapter(
    context: Context,
    resource: Int,
    objects: Array<MuscleGroup>
) : ArrayAdapter<MuscleGroup>(context, resource, objects) {

    override fun isEnabled(position: Int) = position != 0

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View? {
        val view: View = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        when (position) {
            0 -> tv.setTextColor(Color.GRAY)
            else -> tv.setTextColor(Color.BLACK)
        }
        return view
    }
}
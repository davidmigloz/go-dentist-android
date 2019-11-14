package com.davidmiguel.godentist.manageworkdays.addworkdayexectreatment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.davidmiguel.godentist.core.model.Treatment
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayExecTreatmentItemTreatmentBinding

class AddWorkDayExecTreatmentTreatmentsAdapter(
    private val context: Context
) : BaseAdapter(), Filterable {

    private var treatments: MutableList<Treatment> = mutableListOf()
    private val filter by lazy { TreatmentsFilter() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = if (convertView == null) {
            TreatmentViewHolder(
                FragmentAddWorkDayExecTreatmentItemTreatmentBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            ).apply { getRoot().tag = this }
        } else {
            convertView.tag as TreatmentViewHolder
        }
        viewHolder.bind(treatments[position])
        return viewHolder.getRoot()
    }

    override fun getItem(position: Int) = treatments[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = treatments.size

    override fun getFilter(): Filter = filter

    fun setTreatments(treatments: List<Treatment>) {
        this.treatments.apply {
            clear()
            addAll(treatments)
        }
        notifyDataSetChanged()
    }

    private inner class TreatmentViewHolder(
        private val binding: FragmentAddWorkDayExecTreatmentItemTreatmentBinding
    ) {

        fun bind(treatment: Treatment) {
            binding.treatment = treatment
        }

        fun getRoot() = binding.root
    }

    private inner class TreatmentsFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = treatments
                count = treatments.size
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}
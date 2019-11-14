package com.davidmiguel.godentist.manageworkdays.addworkday

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayItemClinicBinding

class AddWorkDayClinicsAdapter(
    private val context: Context
) : BaseAdapter(), Filterable {

    private var clinics: MutableList<Clinic> = mutableListOf()
    private val filter by lazy { ClinicsFilter() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = if (convertView == null) {
            ClinicViewHolder(
                FragmentAddWorkDayItemClinicBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            ).apply { getRoot().tag = this }
        } else {
            convertView.tag as ClinicViewHolder
        }
        viewHolder.bind(clinics[position])
        return viewHolder.getRoot()
    }

    override fun getItem(position: Int) = clinics[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = clinics.size

    override fun getFilter(): Filter = filter

    fun setClinics(clinics: List<Clinic>) {
        this.clinics.apply {
            clear()
            addAll(clinics)
        }
        notifyDataSetChanged()
    }

    private inner class ClinicViewHolder(
        private val binding: FragmentAddWorkDayItemClinicBinding
    ) {

        fun bind(clinic: Clinic) {
            binding.clinic = clinic
        }

        fun getRoot() = binding.root
    }

    private inner class ClinicsFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = clinics
                count = clinics.size
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}
package com.davidmiguel.godentist.manageclinics.clinics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.manageclinics.R
import com.davidmiguel.godentist.manageclinics.databinding.ItemClinicBinding

class ClinicsAdapter : ListAdapter<Clinic, ClinicsAdapter.ClinicViewHolder>(ClinicsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        return ClinicViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ClinicViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemClinicBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_clinic,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clinic: Clinic) {
            binding.name = clinic.name
            binding.percentage = "${clinic.percentage}%"
        }
    }

    class ClinicsDiffCallback : DiffUtil.ItemCallback<Clinic>() {

        override fun areItemsTheSame(oldItem: Clinic, newItem: Clinic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Clinic, newItem: Clinic): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(clinics: List<Clinic>?) {
            clinics?.run {
                val adapter = adapter as ClinicsAdapter
                adapter.submitList(clinics)
            }
        }
    }
}
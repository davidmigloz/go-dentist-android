package com.davidmiguel.godentist.manageclinics.clinics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.manageclinics.R
import com.davidmiguel.godentist.manageclinics.databinding.ItemClinicBinding

class ClinicsAdapter : RecyclerView.Adapter<ClinicsAdapter.ClinicViewHolder>() {

    private var clinics: List<Clinic> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        return ClinicViewHolder(parent)
    }

    override fun getItemCount() = clinics.size

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        if (clinics.size > position) {
            holder.bind(clinics[position])
        }
    }

    fun setClinics(clinics: List<Clinic>) {
        this.clinics = clinics
        notifyDataSetChanged()
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
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(clinics: List<Clinic>) {
            val adapter = adapter as ClinicsAdapter
            adapter.setClinics(clinics)
        }
    }
}
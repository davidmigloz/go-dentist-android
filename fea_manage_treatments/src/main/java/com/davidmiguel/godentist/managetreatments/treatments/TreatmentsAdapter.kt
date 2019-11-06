package com.davidmiguel.godentist.managetreatments.treatments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.model.Treatment
import com.davidmiguel.godentist.managetreatments.R
import com.davidmiguel.godentist.managetreatments.databinding.ItemTreatmentBinding

class TreatmentsAdapter :
    ListAdapter<Treatment, TreatmentsAdapter.TreatmentViewHolder>(TreatmentsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        return TreatmentViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TreatmentViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemTreatmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_treatment,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(treatment: Treatment) {
            binding.name = treatment.name
        }
    }

    class TreatmentsDiffCallback : DiffUtil.ItemCallback<Treatment>() {

        override fun areItemsTheSame(oldItem: Treatment, newItem: Treatment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Treatment, newItem: Treatment): Boolean {
            return oldItem == newItem
        }
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(treatments: List<Treatment>?) {
            treatments?.run {
                val adapter = adapter as TreatmentsAdapter
                adapter.submitList(this)
            }
        }
    }
}

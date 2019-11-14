package com.davidmiguel.godentist.manageworkdays.addworkday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.model.WorkDay.ExecutedTreatment
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayItemTreatmentBinding

class AddWorkDayTreatmentsAdapter : ListAdapter<ExecutedTreatment,
        AddWorkDayTreatmentsAdapter.TreatmentViewHolder>(TreatmentsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        return TreatmentViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TreatmentViewHolder(
        private val parent: ViewGroup,
        private val binding: FragmentAddWorkDayItemTreatmentBinding = FragmentAddWorkDayItemTreatmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(treatment: ExecutedTreatment) {
            binding.et = treatment
        }
    }

    class TreatmentsDiffCallback : DiffUtil.ItemCallback<ExecutedTreatment>() {

        override fun areItemsTheSame(oldItem: ExecutedTreatment, newItem: ExecutedTreatment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExecutedTreatment, newItem: ExecutedTreatment): Boolean {
            return oldItem == newItem
        }
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(treatments: List<ExecutedTreatment>?) {
            treatments?.run {
                val adapter = adapter as AddWorkDayTreatmentsAdapter
                adapter.submitList(this)
            }
        }
    }
}
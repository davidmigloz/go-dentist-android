package com.davidmiguel.godentist.manageworkdays.workdays

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentWorkDaysItemBinding

class WorkDaysAdapter(private val listener : Listener) : ListAdapter<WorkDay, WorkDaysAdapter.WorkDayViewHolder>(
    WorkDaysDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkDayViewHolder {
        return WorkDayViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: WorkDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WorkDayViewHolder(
        private val parent: ViewGroup,
        private val listener : Listener,
        private val binding: FragmentWorkDaysItemBinding = FragmentWorkDaysItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workDay: WorkDay) {
            binding.workDay = workDay
            binding.root.setOnClickListener { listener.onWorkDayClicked(workDay) }
        }
    }

    class WorkDaysDiffCallback : DiffUtil.ItemCallback<WorkDay>() {

        override fun areItemsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
            return oldItem == newItem
        }
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(workDays: List<WorkDay>?) {
            workDays?.run {
                val adapter = adapter as WorkDaysAdapter
                adapter.submitList(this)
            }
        }
    }

    interface Listener {

        fun onWorkDayClicked(workDay: WorkDay)
    }
}

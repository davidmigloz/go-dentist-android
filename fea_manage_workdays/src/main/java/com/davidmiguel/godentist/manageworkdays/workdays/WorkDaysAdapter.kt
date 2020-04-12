package com.davidmiguel.godentist.manageworkdays.workdays

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.formatDMY
import com.davidmiguel.godentist.core.utils.isToday
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentWorkDaysItemBinding
import com.davidmiguel.godentist.core.R as RC

class WorkDaysAdapter(private val workDaysViewModel: WorkDaysViewModel) :
    ListAdapter<WorkDay, WorkDaysAdapter.WorkDayViewHolder>(
        WorkDaysDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkDayViewHolder {
        return WorkDayViewHolder(parent, workDaysViewModel)
    }

    override fun onBindViewHolder(holder: WorkDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WorkDayViewHolder(
        private val parent: ViewGroup,
        private val workDaysViewModel: WorkDaysViewModel,
        private val binding: FragmentWorkDaysItemBinding = FragmentWorkDaysItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workDay: WorkDay) {
            binding.vm = workDaysViewModel
            binding.lifecycleOwner
            binding.workDay = workDay
            binding.date.text = if (workDay.date?.isToday() == true) {
                parent.context.getString(RC.string.all_dateToday)
            } else {
                workDay.date?.formatDMY() ?: ""
            }
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
}

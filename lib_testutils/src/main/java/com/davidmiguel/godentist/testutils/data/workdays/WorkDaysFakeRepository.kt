package com.davidmiguel.godentist.testutils.data.workdays

import androidx.annotation.VisibleForTesting
import com.davidmiguel.godentist.core.data.workdays.WorkDaysRepository
import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkDaysFakeRepository : WorkDaysRepository {

    var workDaysServiceData: LinkedHashMap<String, WorkDay> = LinkedHashMap()

    private var shouldReturnError = false

    override suspend fun exists(uid: String, workDayId: String): Result<Boolean> {
        return if (shouldReturnError) {
            Result.forFailure()
        } else {
            Result.forSuccess(workDaysServiceData.contains(workDayId))
        }
    }

    override fun get(uid: String, workDayId: String): Flow<Result<WorkDay>> {
        return flow {
            val result = if (shouldReturnError) {
                Result.forFailure()
            } else {
                workDaysServiceData[workDayId]?.let { workDay -> Result.forSuccess(workDay) }
                    ?: Result.forFailureNotFound()
            }
            emit(result)
        }
    }

    override fun getAll(uid: String): Flow<Result<List<WorkDay>>> {
        return flow {
            val result = if (shouldReturnError) {
                Result.forFailure()
            } else {
                Result.forSuccess(workDaysServiceData.values.toList())
            }
            emit(result)
        }
    }

    override suspend fun put(uid: String, workDay: WorkDay): Result<WorkDay> {
        return if (shouldReturnError) {
            Result.forFailure()
        } else {
            workDaysServiceData[workDay.id] = workDay
            Result.forSuccess(workDay)
        }
    }

    @VisibleForTesting
    fun addWorkDays(vararg workDays: WorkDay) {
        for (workDay in workDays) {
            workDaysServiceData[workDay.id] = workDay
        }
    }

    @VisibleForTesting
    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
}

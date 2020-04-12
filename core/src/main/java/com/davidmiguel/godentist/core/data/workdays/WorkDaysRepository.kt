package com.davidmiguel.godentist.core.data.workdays

import com.davidmiguel.godentist.core.model.WorkDay
import com.davidmiguel.godentist.core.utils.Result
import kotlinx.coroutines.flow.Flow

interface WorkDaysRepository {

    suspend fun exists(uid: String, workDayId: String): Result<Boolean>

    fun get(uid: String, workDayId: String): Flow<Result<WorkDay>>

    fun getAll(uid: String): Flow<Result<List<WorkDay>>>

    /**
     * Updates (or creates if it doesn't exist) a work day.
     * To create a work day the id must be empty.
     */
    suspend fun put(uid: String, workDay: WorkDay): Result<WorkDay>
}
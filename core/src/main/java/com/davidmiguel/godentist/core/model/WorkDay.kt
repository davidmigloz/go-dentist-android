package com.davidmiguel.godentist.core.model

import com.google.firebase.Timestamp

data class WorkDay(
    var id: String = "",
    var date: Timestamp? = null,
    /** Work day duration in minutes */
    var duration: Int? = null,
    var clinic: Clinic? = null,
    /** List of executed treatments **/
    var executedTreatments: MutableList<ExecutedTreatment>? = null,
    /** Mood from 1 (awful) to 5 (rad); -1 = no mood */
    var mood: Int? = null,
    var notes: String? = null
) : Comparable<WorkDay> {

    data class ExecutedTreatment(
        val id: String = "",
        val treatment: Treatment? = null,
        val price: Double? = null,
        val earnings: Double? = null
    )

    override fun compareTo(other: WorkDay): Int {
        val thisDate = this.date ?: return 0
        val otherDate = other.date ?: return 0
        return thisDate.compareTo(otherDate)
    }
}

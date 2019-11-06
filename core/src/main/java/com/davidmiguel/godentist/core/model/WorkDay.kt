package com.davidmiguel.godentist.core.model

import java.util.*

data class WorkDay(
    var id: String = "",
    var clinic: Clinic? = null,
    var date: Date? = null,
    /** Work day duration in minutes */
    var duration: Int? = null,
    /** List of executed treatments **/
    var executedTreatments: List<ExecutedTreatment>? = null,
    /** Mood from 1 (awful) to 5 (rad) */
    var mood: Int? = null
) {

    data class ExecutedTreatment(
        val treatmentId: String? = null,
        val name: String? = null,
        val price: Double? = null
    )
}

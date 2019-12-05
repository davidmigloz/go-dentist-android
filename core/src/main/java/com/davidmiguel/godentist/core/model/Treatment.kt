package com.davidmiguel.godentist.core.model

data class Treatment(
    var id: String = "",
    var name: String? = null
) : Comparable<Treatment> {

    override fun toString() = name ?: ""

    override fun compareTo(other: Treatment): Int {
        val thisName = this.name ?: return 0
        val otherName = other.name ?: return 0
        return thisName.compareTo(otherName)
    }
}

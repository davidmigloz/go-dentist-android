package com.davidmiguel.godentist.core.model

data class Clinic(
    var id: String = "",
    var name: String? = null,
    var percentage: Int? = null
) : Comparable<Clinic> {
    override fun toString(): String {
        return name ?: ""
    }

    override fun compareTo(other: Clinic): Int {
        val thisName = this.name ?: return 0
        val otherName = other.name ?: return 0
        return thisName.compareTo(otherName)
    }
}
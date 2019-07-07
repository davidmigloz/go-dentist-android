package com.davidmiguel.godentist.core.utils

/**
 * Base state model object.
 * <p>
 * This state can either be successful or not. In either case, it must be complete to represent
 * these states.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
data class Resource<out T> private constructor(
    val state: State,
    private val _value: T?,
    private val _exception: Throwable?
) {

    var isUsed: Boolean = false
        private set

    val value: T
        get() {
            isUsed = true
            return _value ?: throw IllegalStateException("Resource doesn't have a value because state is $state")
        }

    val exception: Throwable
        get() {
            isUsed = true
            return _exception ?: throw IllegalStateException("Resource doesn't have an exception because state is $state")
        }

    enum class State {
        SUCCESS, FAILURE, LOADING
    }

    companion object {
        /**
         * Creates a successful resource containing a value.
         */
        fun <T> forSuccess(value: T) = Resource(State.SUCCESS, value, null)

        /**
         * Creates a failed resource with an exception.
         */
        fun forFailure(exception: Throwable) = Resource(State.FAILURE, null, exception)

        /**
         * Creates a resource in the loading state, without a value or an exception.
         */
        fun forLoading() = Resource(State.LOADING, null, null)
    }
}

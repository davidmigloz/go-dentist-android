@file:Suppress("unused")

package com.davidmiguel.godentist.core.utils

sealed class Result<out T> {

    var isUsed: Boolean = false
        protected set

    companion object {
        /**
         * Creates a successful result containing a value.
         */
        fun <T> forSuccess(value: T) = Success(value)

        /**
         * Creates a failed result with an exception.
         */
        fun forFailure(exception: Throwable = RuntimeException()) = Failure(exception)

        /**
         * Creates a failed result with an ResourceNotFoundException.
         */
        fun forFailureNotFound() = Failure(ResourceNotFoundException())
    }
}

class Success<out T> internal constructor(
    private val _value: T
) : Result<T>() {

    val value: T
        get() {
            isUsed = true
            return _value
        }
}

class Failure internal constructor(
    private val _exception: Throwable
) : Result<Nothing>() {

    val exception: Throwable
        get() {
            isUsed = true
            return _exception
        }
}

class ResourceNotFoundException(
    message: String = "",
    cause: Throwable? = null

) : RuntimeException(message, cause)

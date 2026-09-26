package com.yehorlevchenko.data.utils.errors

sealed class ApiError : Throwable() {
    data object NetworkError : ApiError() {
        private fun readResolve(): Any = NetworkError
    }

    data object NotFound : ApiError() {
        private fun readResolve(): Any = NotFound
    }

    data object Unauthorized : ApiError() {
        private fun readResolve(): Any = Unauthorized
    }

    data object InternalServerError : ApiError() {
        private fun readResolve(): Any = InternalServerError
    }

    data object HttpError : ApiError() {
        private fun readResolve(): Any = HttpError
    }
}
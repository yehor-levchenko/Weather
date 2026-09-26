package com.yehorlevchenko.data.utils.errors

sealed class DbError : Throwable() {
    data object NotAvailable : DbError() {
        private fun readResolve(): Any = NotAvailable
    }

    data object InsertError : DbError() {
        private fun readResolve(): Any = InsertError
    }

    data object QueryError : DbError() {
        private fun readResolve(): Any = QueryError
    }

    data object DeleteError : DbError() {
        private fun readResolve(): Any = DeleteError
    }
}
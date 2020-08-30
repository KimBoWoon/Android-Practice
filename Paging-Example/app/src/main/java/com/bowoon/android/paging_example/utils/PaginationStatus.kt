package com.bowoon.android.paging_example.utils

sealed class PaginationStatus {
    object Loading : PaginationStatus()
    object Empty : PaginationStatus()
    object NotEmpty : PaginationStatus()
    data class Error(val errorStringRes: Int): PaginationStatus()
}
package com.generator.invoice.dtos

import java.time.LocalDate

data class ArgsDto(
    val invoiceNumber: String,
    val workDateStart: LocalDate,
    val workDateEnd: LocalDate,
    val hours: Int
)

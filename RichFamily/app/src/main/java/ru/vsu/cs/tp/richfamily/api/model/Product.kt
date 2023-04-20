package ru.vsu.cs.tp.richfamily.api.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Int,
    val rating: Int,
    val stock: Float,
    val brand: String,
    val images: List<String>
)
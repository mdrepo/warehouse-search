package nz.co.warehouseandroidtest.core.entity

data class Paginated<T>(
    val totalItems: Int,
    val items: List<T>
)

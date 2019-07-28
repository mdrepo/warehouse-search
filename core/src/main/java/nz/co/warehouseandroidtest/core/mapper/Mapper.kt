package nz.co.warehouseandroidtest.core.mapper

interface Mapper<in T, R> {
    fun map(input: T): R
}
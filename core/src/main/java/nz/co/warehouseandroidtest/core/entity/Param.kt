package nz.co.warehouseandroidtest.core.entity

import com.google.gson.Gson
import com.google.gson.JsonElement

interface Param {
    fun toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val json = Gson().toJsonTree(this, this::class.java).asJsonObject
        for (entry in json.entrySet()) {
            if (entry.value.isJsonArray) {
                var index = 0
                entry.value.asJsonArray.forEach { element ->
                    val value = element?.getAsStringOrNull()
                    if (!value.isNullOrBlank()) {
                        map[QUERY_PARAM.format(entry.key, index++)] = value
                    }
                }
            } else {
                val value = entry.value?.getAsStringOrNull()
                if (!value.isNullOrBlank()) {
                    map[entry.key] = value
                }
            }
        }
        return map
    }

    private fun JsonElement.getAsStringOrNull(): String? {
        return if (!this.isJsonPrimitive) null else this.asJsonPrimitive.asString
    }

    companion object {
        private const val QUERY_PARAM = "%s[%d]"
    }
}
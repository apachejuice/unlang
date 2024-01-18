package dev.apachejuice.unlang.debug

data class TagData(val name: String) {
    val data: MutableMap<String, Any?> = mutableMapOf()

    fun put(key: String, value: Any?) {
        data[key] = value
    }

    fun consoleString(): String {
        val builder = StringBuilder()

        builder.appendLine("TAG DATA:")
        data.forEach { (key, value) ->
            builder.appendLine("KEY: $key\t\t$value")
        }

        return builder.toString()
    }

    override fun toString(): String {
        return data.toString()
    }
}

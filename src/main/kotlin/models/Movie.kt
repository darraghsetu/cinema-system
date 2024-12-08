package models

data class Movie(
    var title: String,
    var director: String,
    var runtime: Int,
    var certification: String
) {
    internal var movieID = 0

    override fun toString(): String {
        val textAreaWidth = 50
        val formatString = "%-${textAreaWidth}s"
        val hours =
            when (runtime / 60) {
                0 -> ""
                1 -> "1 hour"
                else -> "${runtime / 60} hours"
            }
        val minutes =
            when (runtime % 60) {
                0 -> ""
                1 -> "1 minute"
                else -> "${runtime % 60} minutes"
            }

        return """
        | │ ${String.format(formatString, "(ID: $movieID)")} │
        | │ ${String.format(formatString, "$title (dir: $director)")} │
        | │ ${String.format(formatString, "Runtime: $hours $minutes")} │
        | │ ${String.format(formatString, "Certification: $certification")} │
        """.trimMargin()
    }
}

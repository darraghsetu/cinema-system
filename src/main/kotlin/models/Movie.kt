package models

data class Movie(
    var title: String,
    var director: String,
    var runtime: Int,
    var certification: String,
    var id: Int = 0,
)
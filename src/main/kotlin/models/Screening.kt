package models

import java.time.LocalDateTime

data class Screening(
    var movie: Movie,
    var screeningDateTime: LocalDateTime,
    var theatreNumber: Int,
    var screeningID: Int = 0
){
    private var seats = mutableListOf<String>()
}
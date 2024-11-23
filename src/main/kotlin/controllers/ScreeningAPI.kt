package controllers

import models.Movie
import models.Screening
import java.time.LocalDate

class ScreeningAPI {

    private var screenings = ArrayList<Screening>()
    private var currentID = 1000
    private fun getNextID() = currentID++
    private val theatreSeating: Map<Int, MutableList<String>> =
        mapOf(
            1 to mutableListOf("A1", "A2", "A3"),
            2 to mutableListOf("A1", "B1", "B2"),
            3 to mutableListOf("A1", "B1", "C2"),
        )

    fun addScreening(screening: Screening): Boolean {
        return true
    }

    fun updateScreening(id: Int, screening: Screening): Boolean {
        return false
    }

    fun deleteScreening(id: Int): Screening? {
        return null
    }

    fun reserveSeats(id: Int, seats: List<String>): Boolean {
        return false
    }

    fun getScreening(id: Int): Screening? {
        return null
    }

    fun listAllScreenings(): List<String> {
        return listOf<String>()
    }

    fun listScreeningsByMovie(movieID: Int): List<String> {
        return listOf<String>()
    }

    fun listScreeningsByDate(date: LocalDate): List<String> {
        return listOf<String>()
    }

    fun listScreeningsByMovieAndDate(movieID: Int, date: LocalDate): List<String> {
        return listOf<String>()
    }

    fun listDayRemainingScreenings(): List<String> {
        return listOf<String>()
    }

    fun listDayRemainingScreeningsByMovie(movieID: Int): List<String> {
        return listOf<String>()
    }

    fun listWeekRemainingScreenings(): List<String> {
        return listOf<String>()
    }

    fun listWeekRemainingScreeningsByMovie(movieID: Int): List<String> {
        return listOf<String>()
    }

    fun listAvailableSeats(id: Int): List<String> {
        return listOf<String>()
    }

    fun numberOfScreenings(): Int {
        return -1
    }

    fun numberOfScreeningsByMovie(movieID: Int): Int {
        return -1
    }

    fun numberOfScreeningsByDate(date: LocalDate): Int {
        return -1
    }

    fun numberOfScreeningsByMovieAndDate(movieID: Int, date: LocalDate): Int {
        return -1
    }

    fun numberOfAvailableSeats(id: Int): Int {
        return -1
    }

    fun numberOfSoldSeats(id: Int): Int {
        return -1
    }
}
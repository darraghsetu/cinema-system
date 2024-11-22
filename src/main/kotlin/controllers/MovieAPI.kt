package controllers

import models.Movie

class MovieAPI {

    private var movies = ArrayList<Movie>()

    fun addMovie(movie: Movie): Boolean {
        return false
    }

    fun getMovie(id: Int): Movie? {
        return null
    }

    fun listAllMovies(): List<String> {
        return listOf<String>()
    }

    fun listMoviesByCertification(certification: String): List<String> {
        return listOf<String>()
    }

    fun numberOfMovies(): Int {
        return -1
    }

    fun numberOfMoviesByCertification(certification: String): Int {
        return -1
    }
}
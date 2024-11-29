package controllers

import models.Movie

class MovieAPI {
    private val movies = ArrayList<Movie>()
    private val certificateList = listOf("G","PG","12A", "15A", "16", "18")
    private var currentID = 1000

    fun addMovie(movie: Movie): Boolean {
        movie.movieID = getNextID()
        return movies.add(movie)
    }

    fun getMovie(id: Int) =
        movies.find { it.movieID == id }

    private fun getNextID() =
        currentID++

    fun listAllMovies() =
        movies.map{ it.toString() }.ifEmpty { null }

    fun listAllTitles() =
        movies.map { "${it.movieID}: ${it.title}" }.ifEmpty { null }

    fun listMoviesByCertification(certification: String) =
        movies.filter { it.certification == certification }.map{ it.toString() }.ifEmpty { null }

    fun numberOfMovies() =
        movies.size

    fun numberOfMoviesByCertification(certification: String) =
        movies.count{ it.certification == certification }

    fun movieExists(id: Int) =
        movies.find{ it.movieID == id } != null

    fun hasMovies() =
        movies.size > 0

    fun hasCertificate(cert: String) =
        movies.count{ it.certification == cert } > 0

    fun isValidCertificate(str: String) =
        certificateList.contains(str)
}
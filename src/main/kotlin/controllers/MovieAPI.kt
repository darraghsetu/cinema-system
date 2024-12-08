package controllers

import models.Movie
import persistence.Serializer

class MovieAPI(serializerType: Serializer) {

    private val serializer: Serializer = serializerType
    private var movies = ArrayList<Movie>()
    private val certificateList = listOf("G", "PG", "12A", "15A", "16", "18")
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
        movies.map { it.toString() }.ifEmpty { null }

    fun listAllTitles() =
        movies.map { "${it.movieID}: ${it.title}" }.ifEmpty { null }

    fun listMoviesByCertification(certification: String) =
        movies.filter { it.certification == certification }.map { it.toString() }.ifEmpty { null }

    fun numberOfMovies() =
        movies.size

    fun numberOfMoviesByCertification(certification: String) =
        movies.count { it.certification == certification }

    fun hasMovies() =
        movies.size > 0

    fun hasCertificate(cert: String) =
        movies.count { it.certification == cert } > 0

    fun movieExists(id: Int) =
        movies.find { it.movieID == id } != null

    fun isValidCertificate(str: String) =
        certificateList.contains(str)

    @Throws(Exception::class)
    fun load() {
        @Suppress("UNCHECKED_CAST")
        movies = serializer.read() as ArrayList<Movie>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(movies)
    }
}

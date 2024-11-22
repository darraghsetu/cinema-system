package controllers

import models.Movie

class MovieAPI {

    private var movies = ArrayList<Movie>()
    private var currentID = 1000

    fun addMovie(movie: Movie): Boolean {
        movie.id = currentID++
        return movies.add(movie)
    }

    fun getMovie(id: Int) = movies.find { it.id == id }

    fun listAllMovies() = movies.map{ it.toString() }.toList()

    fun listMoviesByCertification(certification: String) =
        movies
            .filter { it.certification == certification }
            .map{ it.toString() }
            .toList()

    fun numberOfMovies() = movies.size

    fun numberOfMoviesByCertification(certification: String) =
        movies.count{ it.certification == certification }
}
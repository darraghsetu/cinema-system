package controllers

import models.Movie
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MovieAPITest {

    private var paddington: Movie? = null
    private var matrix: Movie? = null
    private var gladiator: Movie? = null
    private var robocop: Movie? = null
    private var casablanca: Movie? = null

    private var populatedMovies: MovieAPI? = MovieAPI()
    private var emptyMovies: MovieAPI? = MovieAPI()

    @BeforeEach
    fun setup() {
        paddington = Movie("Paddington", "Paul King", 95, "G")
        matrix = Movie("The Matrix", "Lana Wachowski, Lilly Wachowski", 136, "15A")
        gladiator = Movie("Gladiator", "Ridley Scott", 155, "16")
        robocop = Movie("Robocop", "Paul Verhoeven", 102, "18")
        casablanca = Movie( "Casablanca", "Michael Curtiz", 102, "G" )

        // Casablanca not added
        populatedMovies!!.addMovie(paddington!!)
        populatedMovies!!.addMovie(matrix!!)
        populatedMovies!!.addMovie(gladiator!!)
        populatedMovies!!.addMovie(robocop!!)
    }

    @AfterEach
    fun tearDown() {
        paddington = null
        matrix = null
        gladiator = null
        robocop = null
        casablanca = null
        populatedMovies = null
        emptyMovies = null
    }

    @Nested
    inner class GetMovie {
        @Test
        fun `getMovie returns null when Movies ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertNull(emptyMovies!!.getMovie(1000))
        }

        @Test
        fun `getMovie returns null when given a Movie ID that does not exist in Movie ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNull(populatedMovies!!.getMovie(9999))
        }

        @Test
        fun `getMovie returns specified Movie when given that movie's ID`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNotNull(populatedMovies!!.getMovie(1000))
            assertEquals(paddington, populatedMovies!!.getMovie(1000))
        }
    }

    @Nested
    inner class AddMovies {
        @Test
        fun `Adding a movie to a populated list adds to the arrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNotNull(populatedMovies!!.addMovie(casablanca!!))
            assertEquals(5, populatedMovies!!.numberOfMovies())
            assertEquals(casablanca, populatedMovies!!.getMovie(1004))
        }

        @Test
        fun `Adding a movie to an empty list adds to the ArrayList`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertTrue(emptyMovies!!.addMovie(casablanca!!))
            assertEquals(1, emptyMovies!!.numberOfMovies())
            assertEquals(casablanca, emptyMovies!!.getMovie(1000))
        }
    }

    @Nested
    inner class ListMovies {
        @Test
        fun `listAllMovies returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertEquals(listOf<String>(), emptyMovies!!.listAllMovies()) // mutable list of?
        }

        @Test
        fun `listAllMovies returns list of Movie strings when ArrayList is not empty`() {
            val moviesList = populatedMovies!!.listAllMovies()
            assertEquals(moviesList.size, populatedMovies!!.numberOfMovies())
            assertEquals(populatedMovies!!.getMovie(1000).toString(), moviesList[0])
            assertEquals(populatedMovies!!.getMovie(1001).toString(), moviesList[1])
            assertEquals(populatedMovies!!.getMovie(1002).toString(), moviesList[2])
            assertEquals(populatedMovies!!.getMovie(1003).toString(), moviesList[3])

        }

        @Test
        fun `listMoviesByCertification returns empty string list when ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertEquals(listOf<String>(), emptyMovies!!.listMoviesByCertification("G"))
        }

        @Test
        fun `listMoviesByCertification returns empty string list when no Movies of specified cert exist`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertEquals(listOf<String>(), populatedMovies!!.listMoviesByCertification("12A"))
        }

        @Test
        fun `listMoviesByCertification returns list of Movie Strings when Movies of specified cert exist`() {
            populatedMovies!!.addMovie(casablanca!!)
            assertEquals(5, populatedMovies!!.numberOfMovies())

            val gCertMovies = populatedMovies!!.listMoviesByCertification("G")
            assertEquals(2, gCertMovies.size)
            assertTrue(gCertMovies[0].lowercase().contains("paddington"))
            assertTrue(gCertMovies[1].lowercase().contains("casablanca"))
        }
    }

    @Nested
    inner class CountingMovies {
        @Test
        fun numberOfMoviesCalculatedCorrectly() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertEquals(4, populatedMovies!!.numberOfMovies())
        }

        @Test
        fun numberOfMoviesByCertificationCalculatedCorrectly() {
            assertEquals(0, emptyMovies!!.numberOfMoviesByCertification("18"))
            assertEquals(0, populatedMovies!!.numberOfMoviesByCertification("12A"))
            assertEquals(1, populatedMovies!!.numberOfMoviesByCertification("18"))
        }
    }
}
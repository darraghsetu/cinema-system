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
        casablanca = Movie( "Casablanca", "Michael Curtiz", 102, "G" ) // Not added

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
    inner class AddMovies {
        @Test
        fun `addMovie returns true and adds Movie to an empty ArrayList`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertTrue(emptyMovies!!.addMovie(casablanca!!))
            assertEquals(1, emptyMovies!!.numberOfMovies())
            assertEquals(casablanca, emptyMovies!!.getMovie(1000))
        }

        @Test
        fun `addMovie returns true and adds Movie to a populated ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNotNull(populatedMovies!!.addMovie(casablanca!!))
            assertEquals(5, populatedMovies!!.numberOfMovies())
            assertEquals(casablanca, populatedMovies!!.getMovie(1004))
        }
    }

    @Nested
    inner class GetMovie {
        @Test
        fun `getMovie returns null if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertNull(emptyMovies!!.getMovie(1000))
        }

        @Test
        fun `getMovie returns null if no Movie with specified id exists in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNull(populatedMovies!!.getMovie(9999))
        }

        @Test
        fun `getMovie returns Movie if specified Movie ID exists in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNotNull(populatedMovies!!.getMovie(1000))
            assertEquals(paddington, populatedMovies!!.getMovie(1000))
        }
    }

    @Nested
    inner class ListMovies {
        @Test
        fun `listAllMovies returns null if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertNull(emptyMovies!!.listAllMovies())
        }

        @Test
        fun `listAllMovies returns list of Movie strings if the ArrayList is not empty`() {
            val moviesList = populatedMovies!!.listAllMovies()!!
            assertEquals(populatedMovies!!.numberOfMovies(), moviesList.size)
            assertEquals(populatedMovies!!.getMovie(1000).toString(), moviesList[0])
            assertEquals(populatedMovies!!.getMovie(1001).toString(), moviesList[1])
            assertEquals(populatedMovies!!.getMovie(1002).toString(), moviesList[2])
            assertEquals(populatedMovies!!.getMovie(1003).toString(), moviesList[3])
        }

        @Test
        fun `listAllTitles returns null if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertNull(emptyMovies!!.listAllTitles())
        }

        @Test
        fun `listAllTitles returns list of Movie title strings if the ArrayList is not empty`() {
            val titlesList = populatedMovies!!.listAllTitles()!!
            assertEquals(populatedMovies!!.numberOfMovies(), titlesList.size)
            assertEquals("1000: Paddington", titlesList[0])
            assertEquals("1001: The Matrix", titlesList[1])
            assertEquals("1002: Gladiator", titlesList[2])
            assertEquals("1003: Robocop", titlesList[3])
        }

        @Test
        fun `listMoviesByCertification returns null if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertNull(emptyMovies!!.listMoviesByCertification("G"))
        }

        @Test
        fun `listMoviesByCertification returns null if no Movies of specified cert exist in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNull(populatedMovies!!.listMoviesByCertification("12A"))
        }

        @Test
        fun `listMoviesByCertification returns list of Movie strings if Movies of specified cert exist`() {
            populatedMovies!!.addMovie(casablanca!!)
            assertEquals(5, populatedMovies!!.numberOfMovies())

            val gCertMovies = populatedMovies!!.listMoviesByCertification("G")!!
            assertEquals(populatedMovies!!.numberOfMoviesByCertification("G"), gCertMovies.size)
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
            assertEquals(1, populatedMovies!!.numberOfMoviesByCertification("G"))
            populatedMovies!!.addMovie(casablanca!!)
            assertEquals(2, populatedMovies!!.numberOfMoviesByCertification("G"))
        }
    }

    @Nested
    inner class BooleanQueries {

        @Test
        fun `movieExists returns false if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertFalse(emptyMovies!!.movieExists(1000))
        }

        @Test
        fun `movieExists returns false if specified Movie ID does not exist in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertFalse(populatedMovies!!.movieExists(9999))
        }

        @Test
        fun `movieExists returns true if specified Movie ID exists in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertTrue(populatedMovies!!.movieExists(1000))
            assertTrue(populatedMovies!!.movieExists(1001))
            assertTrue(populatedMovies!!.movieExists(1002))
            assertTrue(populatedMovies!!.movieExists(1003))
            assertFalse(populatedMovies!!.movieExists(1004))
        }

        @Test
        fun `hasMovies returns false if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertFalse(emptyMovies!!.hasMovies())
        }

        @Test
        fun `hasMovies returns true if the ArrayList is not empty`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertTrue(populatedMovies!!.hasMovies())
            assertFalse(emptyMovies!!.hasMovies())
            emptyMovies!!.addMovie(casablanca!!)
            assertTrue(emptyMovies!!.hasMovies())
        }

        @Test
        fun `hasCertificate returns false if the ArrayList is empty`() {
            assertEquals(0, emptyMovies!!.numberOfMovies())
            assertFalse(emptyMovies!!.hasCertificate("G"))
        }

        @Test
        fun `hasCertificate returns false if no Movies with specified certificate exist in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertFalse(populatedMovies!!.hasCertificate("12A"))
            assertFalse(populatedMovies!!.hasCertificate("ABC"))
        }

        @Test
        fun `hasCertificate returns true if Movies with specified certificate exist in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertTrue(populatedMovies!!.hasCertificate("G"))
            assertFalse(populatedMovies!!.hasCertificate("12A"))
            populatedMovies!!.addMovie(Movie("Fake Movie", "Me", 123, "12A"))
            assertTrue(populatedMovies!!.hasCertificate("12A"))
        }

        @Test
        fun `isValidCertificate returns false if the specified certificate does not exist`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertFalse(populatedMovies!!.isValidCertificate("15"))
            assertFalse(populatedMovies!!.isValidCertificate("16+"))
            assertFalse(populatedMovies!!.isValidCertificate("ABC"))
            assertFalse(populatedMovies!!.isValidCertificate("pG"))
        }

        @Test
        fun `isValidCertificate returns false if the specified certificate exists`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertTrue(populatedMovies!!.isValidCertificate("G"))
            assertTrue(populatedMovies!!.isValidCertificate("PG"))
            assertTrue(populatedMovies!!.isValidCertificate("12A"))
            assertTrue(populatedMovies!!.isValidCertificate("15A"))
            assertTrue(populatedMovies!!.isValidCertificate("16"))
            assertTrue(populatedMovies!!.isValidCertificate("18"))
        }
    }
}
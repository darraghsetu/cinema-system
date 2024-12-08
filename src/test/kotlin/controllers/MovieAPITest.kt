package controllers

import models.Movie
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File

class MovieAPITest {
    private var paddington: Movie? = null
    private var matrix: Movie? = null
    private var gladiator: Movie? = null
    private var robocop: Movie? = null
    private var casablanca: Movie? = null

    private var populatedMovies: MovieAPI? = MovieAPI(XMLSerializer(File("MovieAPITest.xml")))
    private var emptyMovies: MovieAPI? = MovieAPI(XMLSerializer(File("MovieAPITest.xml")))

    @BeforeEach
    fun setup() {
        paddington = Movie("Paddington", "Paul King", 95, "G")
        matrix = Movie("The Matrix", "Lana Wachowski, Lilly Wachowski", 136, "15A")
        gladiator = Movie("Gladiator", "Ridley Scott", 155, "16")
        robocop = Movie("Robocop", "Paul Verhoeven", 102, "18")
        casablanca = Movie("Casablanca", "Michael Curtiz", 102, "G") // Not added

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
    inner class AddMovie {
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
        fun `getMovie returns null if no Movie with specified Movie ID exists in the ArrayList`() {
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
    inner class ListingMovies {
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
        fun `listMoviesByCertification returns null if no Movies of specified certification exist in the ArrayList`() {
            assertEquals(4, populatedMovies!!.numberOfMovies())
            assertNull(populatedMovies!!.listMoviesByCertification("12A"))
        }

        @Test
        fun `listMoviesByCertification returns list of Movie strings if Movies of specified certification exist`() {
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

    @Nested
    inner class PersistenceTests {
        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty moviesTest.xml file.
            val storingMovies = MovieAPI(XMLSerializer(File("moviesTest.xml")))
            storingMovies.store()

            // Loading the empty moviesTest.xml file into a new object
            val loadedMovies = MovieAPI(XMLSerializer(File("moviesTest.xml")))
            loadedMovies.load()

            // Comparing the source of the movies (storingMovies) with the XML loaded movies (loadedMovies)
            assertEquals(0, storingMovies.numberOfMovies())
            assertEquals(0, loadedMovies.numberOfMovies())
            assertEquals(storingMovies.numberOfMovies(), loadedMovies.numberOfMovies())
        }

        @Test
        fun `saving and loading a loaded collection in XML doesn't lose data`() {
            // Storing 3 movies to the MovieAPITest.xml file.
            val storingMovies = MovieAPI(XMLSerializer(File("moviesTest.xml")))
            storingMovies.addMovie(paddington!!)
            storingMovies.addMovie(matrix!!)
            storingMovies.addMovie(gladiator!!)
            storingMovies.store()

            // Loading moviesTest.xml into a different collection
            val loadedMovies = MovieAPI(XMLSerializer(File("moviesTest.xml")))
            loadedMovies.load()

            // Comparing the source of the movies (storingMovies) with the XML loaded movies (loadedMovies)
            assertEquals(3, storingMovies.numberOfMovies())
            assertEquals(3, loadedMovies.numberOfMovies())
            assertEquals(storingMovies.numberOfMovies(), loadedMovies.numberOfMovies())
            assertEquals(storingMovies.getMovie(1000), loadedMovies.getMovie(1000))
            assertEquals(storingMovies.getMovie(1001), loadedMovies.getMovie(1001))
            assertEquals(storingMovies.getMovie(1002), loadedMovies.getMovie(1002))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty moviesTest.json file.
            val storingMovies = MovieAPI(JSONSerializer(File("moviesTest.json")))
            storingMovies.store()

            // Loading the empty moviesTest.json file into a new object
            val loadedMovies = MovieAPI(JSONSerializer(File("moviesTest.json")))
            loadedMovies.load()

            // Comparing the source of the movies (storingMovies) with the json loaded movies (loadedMovies)
            assertEquals(0, storingMovies.numberOfMovies())
            assertEquals(0, loadedMovies.numberOfMovies())
            assertEquals(storingMovies.numberOfMovies(), loadedMovies.numberOfMovies())
        }

        @Test
        fun `saving and loading a loaded collection in JSON doesn't lose data`() {
            // Storing 3 movies to the moviesTest.json file.
            val storingMovies = MovieAPI(JSONSerializer(File("moviesTest.json")))
            storingMovies.addMovie(paddington!!)
            storingMovies.addMovie(matrix!!)
            storingMovies.addMovie(gladiator!!)
            storingMovies.store()

            // Loading moviesTest.json into a different collection
            val loadedMovies = MovieAPI(JSONSerializer(File("moviesTest.json")))
            loadedMovies.load()

            // Comparing the source of the movies (storingMovies) with the json loaded movies (loadedMovies)
            assertEquals(3, storingMovies.numberOfMovies())
            assertEquals(3, loadedMovies.numberOfMovies())
            assertEquals(storingMovies.numberOfMovies(), loadedMovies.numberOfMovies())
            assertEquals(storingMovies.getMovie(0), loadedMovies.getMovie(0))
            assertEquals(storingMovies.getMovie(1), loadedMovies.getMovie(1))
            assertEquals(storingMovies.getMovie(2), loadedMovies.getMovie(2))
        }
    }
}

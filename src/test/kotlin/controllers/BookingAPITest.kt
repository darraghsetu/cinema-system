package controllers

import models.Booking
import models.Customer
import models.Movie
import models.Screening
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BookingAPITest {
    private var paddington: Movie? = null
    private var gladiator: Movie? = null
    
    private var screening1: Screening? = null
    private var screening2: Screening? = null
    private var screening3: Screening? = null
    private var screening4: Screening? = null
    
    private var customer1: Customer? = null
    private var customer2: Customer? = null
    private var customer3: Customer? = null
    
    private var booking1: Booking? = null
    private var booking2: Booking? = null
    private var booking3: Booking? = null
    private var booking4: Booking? = null

    private var movies: MovieAPI? = null
    private var screenings: ScreeningAPI? = null
    private var customers: CustomerAPI? = null
    private var emptyBookings: BookingAPI? = null
    private var populatedBookings: BookingAPI? = null
    
    @BeforeEach
    fun setup() {
        movies = MovieAPI()
        screenings = ScreeningAPI()
        customers = CustomerAPI()
        emptyBookings = BookingAPI()
        populatedBookings = BookingAPI()
        
        // Movies
        movies!!.addMovie(Movie("Paddington", "Paul King", 95, "G"))
        movies!!.addMovie(Movie("Gladiator", "Ridley Scott", 155, "16"))
        paddington = movies!!.getMovie(1000)
        gladiator = movies!!.getMovie(1001)
        
        // Screenings
        screenings!!.addScreening(
            Screening(
                paddington!!, LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)), theatreID = 1
            )
        )
        screenings!!.addScreening(
            Screening(
                paddington!!, LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)).plusDays(1), theatreID = 2
            )
        )
        screenings!!.addScreening(
            Screening(
                gladiator!!, LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0)), theatreID = 1
            )
        )
        screenings!!.addScreening(
            Screening(
                gladiator!!, LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0)).plusDays(1), theatreID = 2
            )
        )
        screening1 = screenings!!.getScreening(1000)
        screening2 = screenings!!.getScreening(1001)
        screening3 = screenings!!.getScreening(1002)
        screening4 = screenings!!.getScreening(1003)

        // Customers
        customers!!.addCustomer(Customer("Aoife", "Ayy", "aoife@gmail.com", LocalDate.now().minusYears(24)))
        customers!!.addCustomer(Customer("Brendan", "Bee", "brendan@gmail.com", LocalDate.now().minusYears(18)))
        customers!!.addCustomer(Customer("Cillian", "Cee", "cillian@gmail.com", LocalDate.now().minusYears(16)))
        customer1 = customers!!.getCustomer(1000)
        customer2 = customers!!.getCustomer(1001)
        customer3 = customers!!.getCustomer(1002)
        
        // Bookings
        populatedBookings!!.addBooking(Booking(screening1!!, customer1!!, 1, 10.00, listOf("A1")))
        populatedBookings!!.addBooking(Booking(screening2!!, customer2!!, 2, 19.50, listOf("B1, B2")))
        populatedBookings!!.addBooking(Booking(screening3!!, customer3!!, 3, 29.00, listOf("A1", "A2", "A3")))
        booking1 = populatedBookings!!.getBooking(1000)
        booking2 = populatedBookings!!.getBooking(1001)
        booking3 = populatedBookings!!.getBooking(1002)
        booking4 = Booking(screening4!!, customer1!!, 2, 19.50, listOf("A1, A2"))
    }

    @AfterEach
    fun tearDown() {
        paddington = null
        gladiator = null

        screening1 = null
        screening2 = null
        screening3 = null
        screening4 = null

        customer1 = null
        customer2 = null
        customer3 = null

        booking1 = null
        booking2 = null
        booking3 = null
        booking4 = null

        movies = null
        screenings = null
        customers = null
        emptyBookings = null
        populatedBookings = null
    }

    @Nested
    inner class AddBooking {
        @Test
        fun `addBooking returns Booking and adds Booking to an empty ArrayList`() {
            assertEquals(0, emptyBookings!!.numberOfBookings())
            assertNotNull(emptyBookings!!.addBooking(booking4!!))
            assertEquals(1, emptyBookings!!.numberOfBookings())
        }

        @Test
        fun `addBooking returns Booking and adds Booking to a populated ArrayList`() {
            assertEquals(3, populatedBookings!!.numberOfBookings())
            assertNotNull(populatedBookings!!.addBooking(booking4!!))
            assertEquals(4, populatedBookings!!.numberOfBookings())
        }
    }

    @Nested
    inner class CancelBooking {
        @Test
        fun `cancelBooking returns false if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertFalse(emptyBookings!!.cancelBooking(1000))
        }

        @Test
        fun `cancelBooking returns false if the specified Booking ID does not exist in the ArrayList`() {
            assertEquals(3, populatedBookings!!.numberOfBookings())
            assertFalse(emptyBookings!!.cancelBooking(9999))
            assertEquals(3, populatedBookings!!.numberOfBookings())
        }

        @Test
        fun `cancelBooking returns true and cancels the Booking if the specified Booking ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertEquals(false, populatedBookings!!.getBooking(1000)!!.cancelled)
            assertTrue(populatedBookings!!.cancelBooking(1000))
            assertEquals(true, populatedBookings!!.getBooking(1000)!!.cancelled)
        }
    }

    @Nested
    inner class GetBooking {
        @Test
        fun `getBooking returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertNull(emptyBookings!!.getBooking(1000))
        }

        @Test
        fun `getBooking returns null if the specified Booking ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertNull(populatedBookings!!.getBooking(9999))
        }

        @Test
        fun `getBooking returns Booking if the specified Booking ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertNotNull(populatedBookings!!.getBooking(1000))
            assertEquals(booking1, populatedBookings!!.getBooking(1000))
        }
    }

    @Nested
    inner class ListingBookings {
        @Test
        fun `listAllBookings returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertNull(emptyBookings!!.listAllBookings())
        }

        @Test
        fun `listAllBookings returns list of Booking strings if Bookings exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            val bookingsList = populatedBookings!!.listAllBookings()
            assertNotNull(bookingsList!!)

            booking1 = populatedBookings!!.getBooking(1000)
            booking2 = populatedBookings!!.getBooking(1001)
            booking3 = populatedBookings!!.getBooking(1002)

            assertEquals(booking1.toString(), bookingsList[0])
            assertEquals(booking2.toString(), bookingsList[1])
            assertEquals(booking3.toString(), bookingsList[2])
        }

        @Test
        fun `listAllActiveBookings returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertNull(emptyBookings!!.listAllActiveBookings())
        }

        @Test
        fun `listAllActiveBookings returns null if no active Bookings exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasActiveBookings())

            val startID = 1000
            for(i in startID until (startID + populatedBookings!!.numberOfBookings())){
                populatedBookings!!.cancelBooking(i)
            }

            assertFalse(populatedBookings!!.hasActiveBookings())
            assertNull(populatedBookings!!.listAllActiveBookings())
        }

        @Test
        fun `listAllActiveBookings returns list of Booking strings if active Bookings exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasActiveBookings())
            val bookingsList = populatedBookings!!.listAllActiveBookings()
            assertNotNull(bookingsList!!)

            booking1 = populatedBookings!!.getBooking(1000)
            booking2 = populatedBookings!!.getBooking(1001)
            booking3 = populatedBookings!!.getBooking(1002)

            assertEquals(booking1.toString(), bookingsList[0])
            assertEquals(booking2.toString(), bookingsList[1])
            assertEquals(booking3.toString(), bookingsList[2])
        }

        @Test
        fun `listAllCancelledBookings returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasCancelledBookings())
            assertNull(emptyBookings!!.listAllCancelledBookings())
        }

        @Test
        fun `listAllCancelledBookings returns null if no cancelled Bookings exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertFalse(populatedBookings!!.hasCancelledBookings())
            assertNull(populatedBookings!!.listAllCancelledBookings())
        }

        @Test
        fun `listAllCancelledBookings returns list of Booking strings if cancelled Bookings exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertFalse(populatedBookings!!.hasCancelledBookings())

            populatedBookings!!.cancelBooking(1000)
            assertTrue(populatedBookings!!.hasCancelledBookings())
            val bookingsList = populatedBookings!!.listAllCancelledBookings()
            booking1 = populatedBookings!!.getBooking(1000)

            assertNotNull(bookingsList!!)
            assertEquals(1, bookingsList.size)
            assertEquals(booking1.toString(), bookingsList[0])
        }

        @Test
        fun `listActiveBookingsByCustomer returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertNull(emptyBookings!!.listActiveBookingsByCustomer(1000))
        }

        @Test
        fun `listActiveBookingsByCustomer returns null if the specified Customer ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertNull(populatedBookings!!.listActiveBookingsByCustomer(9999))
        }

        @Test
        fun `listActiveBookingsByCustomer returns list of Booking strings of Customer if specified Customer ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())

            val bookingsList = populatedBookings!!.listActiveBookingsByCustomer(1000)
            assertNotNull(bookingsList!!)
            assertEquals(populatedBookings!!.getBooking(1000).toString(), bookingsList[0])
        }

        @Test
        fun `listActiveBookingsByScreening returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertNull(emptyBookings!!.listActiveBookingsByScreening(1000))
        }

        @Test
        fun `listActiveBookingsByScreening returns null if the specified Screening ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertNull(populatedBookings!!.listActiveBookingsByScreening(9999))
        }

        @Test
        fun `listActiveBookingsByScreening returns list of Booking strings of Screening if specified Screening ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            val bookingsList = populatedBookings!!.listActiveBookingsByScreening(1000)
            assertNotNull(bookingsList!!)
            assertEquals(bookingsList[0], populatedBookings!!.getBooking(1000).toString())
        }

        @Test
        fun `listActiveBookingsByMovie returns null if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertNull(emptyBookings!!.listActiveBookingsByMovie(1000))
        }

        @Test
        fun `listActiveBookingsByMovie returns null if the specified Movie ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertNull(populatedBookings!!.listActiveBookingsByMovie(9999))
        }

        @Test
        fun `listActiveBookingsByMovie returns list of Booking strings of Movie if specified Movie ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            val bookingsList = populatedBookings!!.listActiveBookingsByMovie(1000)
            assertNotNull(bookingsList!!)
            assertEquals(bookingsList[0], populatedBookings!!.getBooking(1000).toString())
            assertEquals(bookingsList[1], populatedBookings!!.getBooking(1001).toString())
        }
    }

    @Nested
    inner class CountingBookings {
        @Test
        fun numberOfBookingsCalculatedCorrectly() {
            assertEquals(0, emptyBookings!!.numberOfBookings())
            emptyBookings!!.addBooking(booking1!!)
            assertEquals(1, emptyBookings!!.numberOfBookings())
            assertEquals(3, populatedBookings!!.numberOfBookings())
            populatedBookings!!.cancelBooking(1000)
            assertEquals(3, populatedBookings!!.numberOfBookings())
            populatedBookings!!.addBooking(booking4!!)
            assertEquals(4, populatedBookings!!.numberOfBookings())
        }

        @Test
        fun numberOfActiveBookingsCalculatedCorrectly() {
            assertEquals(0, emptyBookings!!.numberOfActiveBookings())
            assertEquals(3, populatedBookings!!.numberOfActiveBookings())
            populatedBookings!!.cancelBooking(1000)
            assertEquals(2, populatedBookings!!.numberOfActiveBookings())
        }

        @Test
        fun numberOfCancelledBookingsCalculatedCorrectly() {
            assertFalse(emptyBookings!!.hasBookings())
            assertEquals(0, emptyBookings!!.numberOfCancelledBookings())
            emptyBookings!!.addBooking(booking4!!)
            assertEquals(1, emptyBookings!!.numberOfBookings())
            assertEquals(0, emptyBookings!!.numberOfCancelledBookings())
            emptyBookings!!.cancelBooking(1000)
            assertEquals(1, emptyBookings!!.numberOfBookings())
            assertEquals(1, emptyBookings!!.numberOfCancelledBookings())

            assertTrue(populatedBookings!!.hasBookings())
            assertEquals(0, populatedBookings!!.numberOfCancelledBookings())
            populatedBookings!!.cancelBooking(1000)
            assertEquals(1, populatedBookings!!.numberOfCancelledBookings())
        }

        @Test
        fun numberOfActiveBookingsByCustomerCalculatedCorrectly() {
            assertFalse(emptyBookings!!.hasBookings())
            assertEquals(0, emptyBookings!!.numberOfActiveBookingsByCustomer(1000))
            assertTrue(populatedBookings!!.hasBookings())
            assertEquals(1, populatedBookings!!.numberOfActiveBookingsByCustomer(1000))
        }

        @Test
        fun numberOfActiveBookingsByScreeningCalculatedCorrectly() {
            assertFalse(emptyBookings!!.hasBookings())
            assertEquals(0, emptyBookings!!.numberOfActiveBookingsByScreening(1000))
            assertTrue(populatedBookings!!.hasBookings())
            assertEquals(1, populatedBookings!!.numberOfActiveBookingsByScreening(1000))
        }

        @Test
        fun numberOfActiveBookingsByMovieCalculatedCorrectly() {
            assertFalse(emptyBookings!!.hasBookings())
            assertEquals(0, emptyBookings!!.numberOfActiveBookingsByMovie(1000))
            assertEquals(2, populatedBookings!!.numberOfActiveBookingsByMovie(1000))
            assertEquals(1, populatedBookings!!.numberOfActiveBookingsByMovie(1001))
        }
    }

    @Nested
    inner class TotallingSales {
        @Test
        fun totalSalesCalculatedCorrectly() {
            assertEquals(0.0, emptyBookings!!.totalSales())
            emptyBookings!!.addBooking(booking1!!)
            assertEquals(10.0, emptyBookings!!.totalSales())
            assertEquals((10.0 + 19.5 + 29.0), populatedBookings!!.totalSales())
            populatedBookings!!.cancelBooking(1000)
            assertEquals((19.5 + 29.0), populatedBookings!!.totalSales())
        }

        @Test
        fun totalCancelledSalesCalculatedCorrectly() {
            assertEquals(0.0, emptyBookings!!.totalCancelledSales())
            emptyBookings!!.addBooking(booking4!!)
            assertEquals(0.0, emptyBookings!!.totalCancelledSales())
            emptyBookings!!.cancelBooking(1000)
            assertEquals(19.5, emptyBookings!!.totalCancelledSales())
            assertEquals(0.0, populatedBookings!!.totalCancelledSales())
            populatedBookings!!.cancelBooking(1000)
            populatedBookings!!.cancelBooking(1001)
            populatedBookings!!.cancelBooking(1002)
            assertEquals((10.0 + 19.5 + 29.0), populatedBookings!!.totalCancelledSales())
        }

        @Test
        fun totalSalesByScreeningCalculatedCorrectly() {
            assertEquals(0.0, emptyBookings!!.totalSalesByScreening(1000))
            assertEquals(10.0, populatedBookings!!.totalSalesByScreening(1000))
            assertEquals(19.5, populatedBookings!!.totalSalesByScreening(1001))
            assertEquals(29.0, populatedBookings!!.totalSalesByScreening(1002))
            populatedBookings!!.addBooking(booking4!!.copy(screening = screening2!!))
            assertEquals((19.5 + 19.5), populatedBookings!!.totalSalesByScreening(1001))
        }

        @Test
        fun totalSalesByMovieCalculatedCorrectly() {
            assertEquals(0.0, emptyBookings!!.totalSalesByMovie(1000))
            emptyBookings!!.addBooking(booking1!!)
            assertEquals(10.0, emptyBookings!!.totalSalesByMovie(1000))
            assertEquals((10.0 + 19.5), populatedBookings!!.totalSalesByMovie(1000))
            assertEquals(29.0, populatedBookings!!.totalSalesByMovie(1001))
            populatedBookings!!.addBooking(booking4!!)
            assertEquals((29.0 + 19.5), populatedBookings!!.totalSalesByMovie(1001))
        }
    }

    @Nested
    inner class BooleanQueries {
        @Test
        fun `hasBookings returns false if the ArrayList is empty`() {
            assertEquals(0, emptyBookings!!.numberOfBookings())
            assertFalse(emptyBookings!!.hasBookings())
        }

        @Test
        fun `hasBookings returns true if the ArrayList is not empty`() {
            assertEquals(3, populatedBookings!!.numberOfBookings())
            assertTrue(populatedBookings!!.hasBookings())
        }

        @Test
        fun `hasActiveBookings returns false if the ArrayList is empty`() {
            assertEquals(0, emptyBookings!!.numberOfBookings())
            assertFalse(emptyBookings!!.hasActiveBookings())
        }

        @Test
        fun `hasActiveBookings returns false if no active Bookings exist in the ArrayList`() {
            assertEquals(3, populatedBookings!!.numberOfActiveBookings())
            populatedBookings!!.cancelBooking(1000)
            populatedBookings!!.cancelBooking(1001)
            populatedBookings!!.cancelBooking(1002)
            assertEquals(0, populatedBookings!!.numberOfActiveBookings())
            assertFalse(populatedBookings!!.hasActiveBookings())
        }

        @Test
        fun `hasActiveBookings returns true if active Bookings exist in the ArrayList`() {
            assertEquals(3, populatedBookings!!.numberOfActiveBookings())
            assertTrue(populatedBookings!!.hasActiveBookings())
        }

        @Test
        fun `hasCancelledBookings returns false if the ArrayList is empty`() {
            assertEquals(0, emptyBookings!!.numberOfCancelledBookings())
            assertFalse(emptyBookings!!.hasCancelledBookings())
        }

        @Test
        fun `hasCancelledBookings returns false if no cancelled Bookings exist in the ArrayList`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertEquals(0, emptyBookings!!.numberOfCancelledBookings())
            assertFalse(emptyBookings!!.hasCancelledBookings())

            assertTrue(populatedBookings!!.hasBookings())
            assertEquals(0, populatedBookings!!.numberOfCancelledBookings())
            assertFalse(populatedBookings!!.hasCancelledBookings())
        }

        @Test
        fun `hasCancelledBookings returns true if cancelled Bookings exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertEquals(0, populatedBookings!!.numberOfCancelledBookings())
            assertFalse(populatedBookings!!.hasCancelledBookings())
            populatedBookings!!.cancelBooking(1000)
            assertTrue(populatedBookings!!.hasCancelledBookings())
        }

        @Test
        fun `hasCustomer returns false if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertFalse(emptyBookings!!.hasCustomer(1000))
        }

        @Test
        fun `hasCustomer returns false if the specified Customer ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertFalse(populatedBookings!!.hasCustomer(9999))
        }

        @Test
        fun `hasCustomer returns true if the specified Customer ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertTrue(populatedBookings!!.hasCustomer(1000))
            assertTrue(populatedBookings!!.hasCustomer(1001))
            assertTrue(populatedBookings!!.hasCustomer(1002))
        }

        @Test
        fun `hasScreening returns false if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertFalse(emptyBookings!!.hasScreening(1000))
        }

        @Test
        fun `hasScreening returns false if the specified Screening ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertFalse(populatedBookings!!.hasScreening(9999))
        }

        @Test
        fun `hasScreening returns true if the specified Screening ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertTrue(populatedBookings!!.hasScreening(1000))
            assertTrue(populatedBookings!!.hasScreening(1001))
            assertTrue(populatedBookings!!.hasScreening(1002))
            assertFalse(populatedBookings!!.hasScreening(1003))
            populatedBookings!!.addBooking(booking4!!)
            assertTrue(populatedBookings!!.hasScreening(1003))
        }

        @Test
        fun `bookingExists returns false if the ArrayList is empty`() {
            assertFalse(emptyBookings!!.hasBookings())
            assertFalse(emptyBookings!!.bookingExists(1000))
        }

        @Test
        fun `bookingExists returns false if the specified Booking ID does not exist in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertFalse(populatedBookings!!.bookingExists(9999))
        }

        @Test
        fun `bookingExists returns true if the specified Booking ID exists in the ArrayList`() {
            assertTrue(populatedBookings!!.hasBookings())
            assertTrue(populatedBookings!!.bookingExists(1000))
            assertTrue(populatedBookings!!.bookingExists(1001))
            assertTrue(populatedBookings!!.bookingExists(1002))
        }
    }
}
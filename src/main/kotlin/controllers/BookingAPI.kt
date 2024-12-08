package controllers

import models.Booking

class BookingAPI {
    private val bookings = ArrayList<Booking>()
    private var currentID = 1000

    fun addBooking(booking: Booking): Booking? {
        val bookingID = getNextID()
        booking.bookingID = bookingID
        return if( bookings.add(booking) ) getBooking(bookingID) else null
    }

    fun cancelBooking(id: Int) =
        if (bookingExists(id)) {
            getBooking(id)!!.cancelled = true
            true
        } else false

    fun getBooking(id: Int) =
        bookings.find{ it.bookingID == id }

    private fun getNextID() =
        currentID++

    private fun filterBookings(predicate: (Booking) -> (Boolean)) =
        bookings
            .filter{ predicate(it) }
            .map{ it.toString() }
            .ifEmpty { null }

    fun listAllBookings() =
        filterBookings{ true }

    fun listAllActiveBookings() =
        filterBookings{ !it.cancelled }

    fun listAllCancelledBookings() =
        filterBookings{ it.cancelled }

    fun listActiveBookingsByCustomer(customerID: Int) =
        filterBookings { it.customer.customerID == customerID && !it.cancelled }

    fun listActiveBookingsByScreening(screeningID: Int) =
        filterBookings { it.screening.screeningID == screeningID && !it.cancelled }

    fun listActiveBookingsByMovie(movieID: Int) =
        filterBookings { it.screening.movie.movieID == movieID && !it.cancelled }

    fun numberOfBookings() =
        bookings.size

    fun numberOfActiveBookings() =
        listAllActiveBookings()?.size ?: 0

    fun numberOfCancelledBookings() =
        listAllCancelledBookings()?.size ?: 0

    fun numberOfActiveBookingsByCustomer(customerID: Int) =
        listActiveBookingsByCustomer(customerID)?.size ?: 0

    fun numberOfActiveBookingsByScreening(screeningID: Int) =
        listActiveBookingsByScreening(screeningID)?.size ?: 0

    fun numberOfActiveBookingsByMovie(movieID: Int) =
        listActiveBookingsByMovie(movieID)?.size ?: 0

    fun totalSales() =
        bookings.filter{ !it.cancelled }.sumOf{ it.salePrice }

    fun totalCancelledSales() =
        bookings.filter{ it.cancelled }.sumOf{ it.salePrice }

    fun totalSalesByScreening(screeningID: Int) =
        bookings.filter{ it.screening.screeningID == screeningID }.sumOf{ it.salePrice }

    fun totalSalesByMovie(movieID: Int) =
        bookings.filter{ it.screening.movie.movieID == movieID }.sumOf{ it.salePrice }

    fun hasBookings() =
        numberOfBookings() > 0

    fun hasActiveBookings() =
        numberOfActiveBookings() > 0

    fun hasCancelledBookings() =
        numberOfCancelledBookings() > 0

    fun hasCustomer(customerID: Int) =
        numberOfActiveBookingsByCustomer(customerID) > 0

    fun hasScreening(screeningID: Int) =
        numberOfActiveBookingsByScreening(screeningID) > 0

    fun bookingExists(id: Int) =
        bookings.any{ it.bookingID == id }
}
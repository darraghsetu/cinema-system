package controllers

import models.Booking

class BookingAPI {
    private val bookings = ArrayList<Booking>()
    private var currentID = 1000

    fun addBooking(booking: Booking): Boolean {
        return true
    }

    fun cancelBooking(id: Int) = false

    fun getBooking(id: Int): Booking? {
        return null
    }

    private fun getNextID() =
        currentID++

    private fun filterBookings(predicate: (Booking) -> (Boolean)) = false

    fun listAllBookings() = null

    fun listAllActiveBookings() = null

    fun listAllCancelledBookings() = null

    fun listBookingsByCustomer(customerID: Int) = null

    fun listBookingsByScreening(screeningID: Int) = null

    fun listBookingsByMovie(movieID: Int) = null

    fun numberOfBookings() = 0

    fun numberOfActiveBookings() = 0

    fun numberOfCancelledBookings() = 0

    fun numberOfBookingsByCustomer(customerID: Int) = 0

    fun numberOfBookingsByScreening(screeningID: Int) = 0

    fun numberOfBookingsByMovie(movieID: Int) = 0

    fun totalSales() = false

    fun totalCancelledSales() = false

    fun totalSalesByScreening(screeningID: Int) = false

    fun totalSalesByMovie(movieID: Int) = false

    fun hasBookings() = false

    fun hasActiveBookings() = false

    fun hasCancelledBookings() = false

    fun hasCustomer(customerID: Int) = false

    fun hasScreening(screeningID: Int) = false

    fun bookingExists(id: Int) = false
}
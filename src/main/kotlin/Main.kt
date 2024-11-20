import utils.*

/*
 * NOTES:
 * Can mainMenu, bookingsMenu, ... be generalised into one menu function
 */

fun main() {
    runMenu()
}

fun runMenu() {
    do {
        printMainMenu()
        val option = getUserOption()

        when (option) {
            1 -> bookingsMenu()
            2 -> moviesMenu()
            3 -> screeningsMenu()
            4 -> customersMenu()
            5 -> reportsMenu()
        }
    } while(option != 0)
}

fun bookingsMenu() {
    do{
        printBookingsMenu()
        val option = getUserOption()

        when (option) {
            1 -> addBooking( )
            2 -> viewBooking( )
            3 -> viewBookingsByMovie( )
            4 -> viewBookingsByDate( )
        }
    } while(option != 0)
}

fun moviesMenu() {
    do{
        printMoviesMenu()
        val option = getUserOption()

        when (option) {
            1 -> addMovie()
            2 -> viewMovie()
            3 -> viewAllMovies()
            4 -> viewAllMoviesByCert()
        }
    } while(option != 0)
}

fun screeningsMenu() {
    do{
        printScreeningsMenu()
        val option = getUserOption()

        when (option) {
            1 -> addScreening()
            2 -> deleteScreening()
            3 -> deleteScreeningsByMovie()
            4 -> deleteScreeningsByDate()
            5 -> updateScreenings()
            6 -> viewScreening()
            7 -> viewScreeningsByMovie()
            8 -> viewScreeningsByDate()
        }
    } while (option != 0)
}

fun customersMenu() {
    do{
        printCustomersMenu()
        val option = getUserOption()

        when (option) {
            1 -> addCustomer()
            2 -> deleteCustomer()
            3 -> updateCustomer()
            4 -> viewCustomer()
            5 -> viewAllCustomers()
            6 -> viewAllAdultCustomers()
            7 -> viewAllChildCustomers()
        }
    } while (option != 0)
}

fun reportsMenu() {
    printReportsMenu()
}

// Bookings

fun addBooking() = println("Add booking called")

fun viewBooking() = println("View booking called")

fun viewBookingsByMovie() = println("View booking by movie called")

fun viewBookingsByDate() = println("View booking by date called")

// Movies

fun addMovie() = println("Add movie called")

fun viewMovie() = println("View movie called")

fun viewAllMovies() = println("View all movies called")

fun viewAllMoviesByCert() = println("View all movies by cert called")

// Screenings

fun addScreening() = println("Add screening called")

fun deleteScreening() = println("Delete screening called")

fun deleteScreeningsByMovie() = println("Delete screenings by movie called")

fun deleteScreeningsByDate() = println("Delete screenings by date called")

fun updateScreenings() = println("Update screenings called")

fun viewScreening() = println("View screening called")

fun viewScreeningsByMovie() = println("View screening by movie called")

fun viewScreeningsByDate() = println("View screening by date called")

// Customers

fun addCustomer() = println("Add customer called")

fun deleteCustomer() = println("Delete customer called")

fun updateCustomer() = println("Update customer called")

fun viewCustomer() = println("View customer called")

fun viewAllCustomers() = println("View all customers called")

fun viewAllAdultCustomers() = println("View all adults customers called")

fun viewAllChildCustomers() = println("View all child customers called")

// Misc

fun getUserOption() = readNextInt(">")
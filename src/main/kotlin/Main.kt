import controllers.MovieAPI
import models.Movie
import utils.*

/*
 * NOTES:
 * Can mainMenu, bookingsMenu, ... be generalised into one menu function?
 */

private const val PROMPT = "> "
private val movieAPI = MovieAPI()

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
            1 -> addBooking()
            2 -> viewBooking()
            3 -> viewBookingsByMovie()
            4 -> viewBookingsByDate()
        }
    } while(option != 0)
}

fun moviesMenu() {
    do{
        printMoviesMenu()
        val option = getUserOption()

        when (option) {
            1 -> addMovie()
            2 -> getMovie()
            3 -> listAllMovies()
            4 -> listAllMoviesByCert()
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
            5 -> updateScreening()
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
    println()
}

// Bookings

fun addBooking() = println("Add booking called\n")

fun viewBooking() = println("View booking called\n")

fun viewBookingsByMovie() = println("View booking by movie called\n")

fun viewBookingsByDate() = println("View booking by date called\n")

// Movies

fun addMovie() {
    val title = readNextLine("Title: ")
    val director = readNextLine("Director: ")
    val runtime = readNextInt("Runtime: ")
    val certification = readNextLine("Certification: ")
    if( movieAPI.addMovie(Movie(title, director, runtime, certification)) ) print("Added")
    else print("Not Added")
    println()
    println()
}

fun getMovie() {
    listAllMovies()
    val id = readNextInt("ID: ")
    if( movieAPI.getMovie(id) != null ) print(movieAPI.getMovie(id))
    else print("No movie found")
    println()
    println()
}

fun listAllMovies(){
    if( movieAPI.listAllMovies().isNotEmpty() ) movieAPI.listAllMovies().forEach{ println(it) }
    else print("No movies found")
    println()
    println()
}

fun listAllMoviesByCert(){
    val cert = readNextLine("Certification: ")
    if( movieAPI.listMoviesByCertification(cert).isNotEmpty() )
        movieAPI.listMoviesByCertification(cert).forEach{ println(it) }
    else print("No movies found")
    println()
    println()
}

// Screenings

fun addScreening() = println("Add screening called\n")

fun deleteScreening() = println("Delete screening called\n")

fun deleteScreeningsByMovie() = println("Delete screenings by movie called\n")

fun deleteScreeningsByDate() = println("Delete screenings by date called\n")

fun updateScreening() = println("Update screenings called\n")

fun viewScreening() = println("View screening called\n")

fun viewScreeningsByMovie() = println("View screening by movie called\n")

fun viewScreeningsByDate() = println("View screening by date called\n")

// Customers

fun addCustomer() = println("Add customer called\n")

fun deleteCustomer() = println("Delete customer called\n")

fun updateCustomer() = println("Update customer called\n")

fun viewCustomer() = println("View customer called\n")

fun viewAllCustomers() = println("View all customers called\n")

fun viewAllAdultCustomers() = println("View all adults customers called\n")

fun viewAllChildCustomers() = println("View all child customers called\n")

// Misc

fun getUserOption(): Int {
    val userChoice = readNextInt(PROMPT)
    println()
    return userChoice
}
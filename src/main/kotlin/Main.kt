import models.Customer
import models.Movie
import models.Screening
import controllers.CustomerAPI
import controllers.MovieAPI
import controllers.ScreeningAPI
import utils.ScannerInput as In
import utils.MenuUtilities as Menu
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private const val PROMPT = "> "
//private lateinit var movieAPI: MovieAPI
//private lateinit var screeningAPI: ScreeningAPI
private val movieAPI = MovieAPI()
private val screeningAPI = ScreeningAPI()

fun main() {
    // initControllersWithSerializer()
    dummyData()
    mainMenu()
}

fun dummyData() {
    movieAPI.addMovie(Movie("Paddington", "Paul King", 95, "G"))
    movieAPI.addMovie(Movie("The Matrix", "Lana Wachowski, Lilly Wachowski", 136, "15A"))
    val paddington = movieAPI.getMovie(1000)!!
    val matrix = movieAPI.getMovie(1001)!!

    val todayMiddayDateTime = LocalDateTime.of( LocalDate.now(), LocalTime.NOON )
    val todayMiddayDateTimePlusOneDays = todayMiddayDateTime.plusDays(1)
    val todayMiddayDateTimePlusTwoDays = todayMiddayDateTime.plusDays(2)

    val paddingtonScreening1 = Screening(paddington, todayMiddayDateTimePlusOneDays!!, 1)
    val paddingtonScreening2 = Screening(paddington, todayMiddayDateTimePlusTwoDays!!, 3)
    val matrixScreening1 = Screening(matrix, todayMiddayDateTime!!, 1)
    val matrixScreening2 = Screening(matrix, todayMiddayDateTimePlusOneDays.plusHours(3), 2)

    screeningAPI.addScreening(paddingtonScreening1)
    screeningAPI.addScreening(paddingtonScreening2)
    screeningAPI.addScreening(matrixScreening1)
    screeningAPI.addScreening(matrixScreening2)
}

fun runMenu(menuPrinter: () -> (Unit), options: List<() -> (Unit)>) {
    do{
        menuPrinter()
        val option = getUserOption()

        if (option in 1..options.size) {
            options[option-1]()
        } else if (option != 0) {
            println(" Invalid option, please try again")
            println()
        }
    } while(option != 0)
}

fun mainMenu() =
    runMenu(
        Menu::printMainMenu,
        listOf(
            ::bookingsMenu,
            ::moviesMenu,
            ::screeningsMenu,
            ::customersMenu,
            ::reportsMenu
        )
    )

fun bookingsMenu() =
    runMenu(
        Menu::printBookingsMenu,
        listOf(
            ::addBooking,
            ::viewBooking,
            ::listBookingsByMovie,
            ::listBookingsByDate,
        )
    )

fun moviesMenu() =
    runMenu(
        Menu::printMoviesMenu,
        listOf(
            ::addMovie,
            ::viewMovie,
            ::listAllMovies,
            ::listAllMoviesByCert,
        )
    )

fun screeningsMenu() =
    runMenu(
        Menu::printScreeningsMenu,
        listOf(
            ::addScreening,
            ::deleteScreeningMenu,
            ::updateScreeningMenu,
            ::viewScreeningMenu,
        )
    )

fun customersMenu() =
    runMenu(
        Menu::printCustomersMenu,
        listOf(
            ::addCustomer,
            ::deleteCustomer,
            ::updateCustomer,
            ::viewCustomer,
            ::listAllCustomers,
            ::listAllAdultCustomers,
            ::listAllChildCustomers,
        )
    )

fun reportsMenu() =
    println("reports menu")

fun deleteScreeningMenu() =
    if (screeningAPI.hasScreenings()) {
        runMenu(
            Menu::printDeleteScreeningMenu,
            listOf(
                ::deleteScreening,
                ::deleteScreeningByMovie,
                ::deleteScreeningByDate,
                ::deleteAllScreeningsByMovie,
                ::deleteAllScreeningsByDate,
            )
        )
    } else println(" No screenings found")

fun updateScreeningMenu() =
    if (screeningAPI.hasScreenings()) {
        runMenu(
            Menu::printUpdateScreeningMenu,
            listOf(
                ::updateScreeningMovie,
                ::updateScreeningDateTime,
                ::updateScreeningTheatre,
                ::updateScreening,
            )
        )
    } else println(" No screenings found")

fun viewScreeningMenu() =
    if (screeningAPI.hasScreenings()) {
        runMenu(
            Menu::printViewScreeningMenu,
            listOf(
                ::listAllScreenings,
                ::listScreeningsByMovie,
                ::listScreeningsByDate,
                ::listScreeningsByMovieAndDate,
                ::listDayRemainingScreenings,
                ::listDayRemainingScreeningsByMovie,
                ::listWeekRemainingScreenings,
                ::listWeekRemainingScreeningsByMovie,
            )
        )
    } else println(" No screenings found")

// Bookings

fun addBooking() = println("Add booking called\n")

fun viewBooking() = println("View booking called\n")

fun listBookingsByMovie() = println("View booking by movie called\n")

fun listBookingsByDate() = println("View booking by date called\n")

// Movies

fun addMovie() {
    val title = In.readNextLine(" Title: ")
    val director = In.readNextLine(" Director: ")
    val runtime = In.readNextInt(" Runtime: ")
    var certification: String
    do{
        certification = In.readNextLine(" Certification: ")
        if (! movieAPI.isValidCertificate(certification))
            println(" Certification must be: G, PG, 12A, 15A, 16 or 18")
    } while (! movieAPI.isValidCertificate(certification))
    if (movieAPI.addMovie(Movie(title, director, runtime, certification)))
        println(" Movie has been successfully added")
    else println(" Movie has not been added")
}

fun viewMovie() =
    if (movieAPI.hasMovies()) {
        movieAPI.listAllTitles()!!.forEach { println(it) }
        println()
        val movieID = In.readNextInt(" ID: ")
        println(movieAPI.getMovie(movieID) ?: " No movie found for ID: $movieID")
    } else println(" No movies found")

fun listAllMovies() =
    if (movieAPI.hasMovies()) movieAPI.listAllMovies()!!.forEach{ println(it) }
    else println(" No movies found")

fun listAllMoviesByCert() =
    if (movieAPI.hasMovies()) {
        val cert = In.readNextLine(" Certification: ")
        if (movieAPI.hasCertificate(cert))
            println(movieAPI.listMoviesByCertification(cert))
        else println(" No movies for certificate: $cert")
    } else println(" No movies found")

// Screenings

fun addScreening() =
    if (movieAPI.hasMovies()) {
        movieAPI.listAllTitles()!!.forEach { println(it) }
        println()
        val movieID = In.readNextInt(" Movie ID: ")
        val movie: Movie? = movieAPI.getMovie(movieID)
        if (movie != null) {
            val theatreID = In.readNextInt(" Theatre Number(1-3): ")
            if (screeningAPI.theatreExists(theatreID)) {
                val date = In.readNextLine(" Date (dd/mm/yyyy): ")
                val time = In.readNextLine(" Time (24 hour format - hh:mm): ")
                try {
                    val dateTime = LocalDateTime.parse(
                        "$date $time",
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    )
                    if (screeningAPI.availableDateTime(dateTime, movie.runtime, theatreID)) {
                        if (screeningAPI.addScreening(Screening(movie, dateTime, theatreID))) {
                            println(" Screening has been successfully added")
                        } else println(" Screening has not been added")
                    } else println(" Theatre $theatreID not available for screening on that date and time")
                } catch (d: DateTimeParseException) {
                    println(" Invalid date or time entered")
                }
            } else println(" Theatre $theatreID does not exist")
        } else println(" No movie found for ID: $movieID")
    } else println(" No movies to screen")

fun deleteScreening() =
    if (screeningAPI.hasScreenings()) {
        screeningAPI.listAllScreenings()!!.forEach { println(it) }
        println()
        if (
            screeningAPI
                .deleteScreening(
                    In.readNextInt(" Screening ID: ")
                ) != null
        ) println(" Screening has been successfully deleted")
        else println(" Screening has not been deleted")
    } else println(" No screenings found")

fun deleteScreeningByMovie() =
    if (screeningAPI.hasScreenings()) {
        if (movieAPI.hasMovies()) {
            movieAPI.listAllTitles()!!.forEach { println(it) }
            println()
            val movieID = In.readNextInt(" Movie ID: ")
            if (movieAPI.movieExists(movieID)) {
                if (screeningAPI.hasScreeningsByMovie(movieID)) {
                    screeningAPI.listScreeningsByMovie(movieID)!!.forEach { println(it) }
                    println()
                    val screeningID = In.readNextInt(" Screening ID: ")
                    if (screeningAPI.screeningExists(screeningID)) {
                        if (screeningAPI.getScreening(screeningID)!!.movie.movieID == movieID) {
                            if (screeningAPI.deleteScreening(screeningID) != null) {
                                println(" Screening has been successfully deleted")
                            } else println(" Screening has not been deleted")
                        } else println(
                            " Invalid screening ID: $screeningID for ${movieAPI.getMovie(movieID)!!.title}"
                        )
                    } else println(" No screening found for ID: $screeningID")
                } else println(
                    " No screenings found for ${movieAPI.getMovie(movieID)?.title}"
                )
            } else println(" No movies found for ID: $movieID")
        } else println(" No movies found")
    } else println(" No screenings found")

fun deleteScreeningByDate() =
    if (screeningAPI.hasScreenings()) {
        try {
            val date = LocalDate.parse(
                In.readNextLine(" Date (dd/mm/yyyy): "),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
            if (screeningAPI.hasScreeningsByDate(date)) {
                screeningAPI.listScreeningsByDate(date)!!.forEach { println(it) }
                println()
                val screeningID = In.readNextInt(" Screening ID: ")
                if ( screeningAPI.screeningExists(screeningID)) {
                    if (screeningAPI.getScreening(screeningID)!!.screeningDateTime.toLocalDate() == date) {
                        if (screeningAPI.deleteScreening(screeningID) != null) {
                            println(" Screening has been successfully deleted")
                        } else println(" Screening has not been deleted")
                    } else println(
                        " Invalid screening ID: $screeningID for " +
                        date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    )
                } else println(" No screening found for ID: $screeningID")
            } else println(
                " No screenings found for ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
            )
        } catch (d: DateTimeParseException) {
            println(" Invalid date entered")
        }
    } else println(" No screenings found")

fun deleteAllScreeningsByMovie() =
    if (screeningAPI.hasScreenings()){
        if (movieAPI.hasMovies()) {
            movieAPI.listAllTitles()!!.forEach { println(it) }
            println()
            val movieID = In.readNextInt(" Movie ID: ")
            if (movieAPI.movieExists(movieID)) {
                if (screeningAPI.hasScreeningsByMovie(movieID)) {
                    if (screeningAPI.deleteScreeningsByMovie(movieID) != null) {
                        println(" Screenings for ${movieAPI.getMovie(movieID)!!.title} have been deleted")
                    } else println(" Screenings have not been deleted")
                } else println(" No screenings found for ${movieAPI.getMovie(movieID)!!.title}")
            } else println(" No movies found for ID: $movieID")
        } else println(" No movies found")
    } else println(" No screenings found")

fun deleteAllScreeningsByDate() =
    if (screeningAPI.hasScreenings()) {
        try {
            val date = LocalDate.parse(
                In.readNextLine(" Date (dd/mm/yyyy): "),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
            if (screeningAPI.hasScreeningsByDate(date)) {
                if (screeningAPI.deleteScreeningsByDate(date) != null) {
                    println(" Screenings on ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} " +
                            " have been successfully deleted"
                    )
                } else println(" Screenings have not been deleted")
            } else println(
                " No screenings found for ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
            )
        } catch (d: DateTimeParseException) {
            println(" Invalid date entered")
        }
    } else println(" No screenings found")

fun updateScreening() =
    if (screeningAPI.hasScreenings()) {
        screeningAPI.listAllScreenings()!!.forEach { print(it) }
        println()
        val screeningID = In.readNextInt(" Screening ID to update: ")
        println()
        if (screeningAPI.screeningExists(screeningID)) {
            if (movieAPI.hasMovies()) {
                movieAPI.listAllTitles()!!.forEach { println(it) }
                println()
                val movieID = In.readNextInt(" New Movie ID: ")
                val movie = movieAPI.getMovie(movieID)
                if (movie != null) {
                    val theatreID = In.readNextInt(" New Theatre ID: ")
                    if (screeningAPI.theatreExists(theatreID)) {
                        try {
                            val date = LocalDate.parse(
                                In.readNextLine(" New date (dd/mm/yyyy): "),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            )
                            val time = LocalTime.parse(
                                In.readNextLine(" New time (24 hour format - hh:mm): "),
                                DateTimeFormatter.ofPattern("HH:mm")
                            )
                            val screeningDatetime = LocalDateTime.of(date, time)
                            if (screeningAPI.availableDateTime(screeningDatetime, movie.runtime, theatreID)) {
                                val newScreening = Screening(movie, screeningDatetime, theatreID)
                                if (screeningAPI.updateScreening(screeningID, newScreening)) {
                                    println(" Screening has been successfully updated:")
                                    println(screeningAPI.getScreening(screeningID))
                                } else println(" Screening has not been updated")
                            } else println(" Theatre $theatreID not available for screening on that date and time")
                        } catch (d: DateTimeParseException) {
                            println(" Invalid date entered")
                        }
                    } else println(" Invalid theatre ID: $theatreID")
                } else println(" No movie found for ID: $movieID")
            } else println(" No movies found")
        } else println(" No screenings found for ID: $screeningID")
    } else println(" No screenings found")

fun updateScreeningMovie() {
    if (screeningAPI.hasScreenings()) {
        screeningAPI.listAllScreenings()!!.forEach { print(it) }
        println()
        val screeningID = In.readNextInt(" Screening ID to update: ")
        println()
        if (screeningAPI.screeningExists(screeningID)) {
            val screening = screeningAPI.getScreening(screeningID)!!
            if (movieAPI.hasMovies()) {
                movieAPI.listAllTitles()!!.forEach { println(it) }
                println()
                val movieID = In.readNextInt(" New Movie ID: ")
                val movie = movieAPI.getMovie(movieID)
                if (movie != null) {
                    val newScreening = Screening(movie, screening.screeningDateTime, screening.theatreID)
                    if (screeningAPI.updateScreening(screeningID, newScreening)) {
                        println(" Screening has been successfully updated:")
                        println(screeningAPI.getScreening(screeningID))
                    } else println(" Screening has been not been updated")
                } else println(" No movie found for ID: $movieID")
            } else println(" No movies found")
        } else println(" No screening found for ID: $screeningID")
    } else println(" No screenings found")
}

fun updateScreeningDateTime() {
    if (screeningAPI.hasScreenings()) {
        screeningAPI.listAllScreenings()!!.forEach { print(it) }
        println()
        val screeningID = In.readNextInt(" Screening ID to update: ")
        println()
        if (screeningAPI.screeningExists(screeningID)) {
            val screening = screeningAPI.getScreening(screeningID)!!
            try {
                val date = LocalDate.parse(
                    In.readNextLine(" New date (dd/mm/yyyy): "),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
                val time = LocalTime.parse(
                    In.readNextLine(" New time (24 hour format - hh:mm): "),
                    DateTimeFormatter.ofPattern("HH:mm")
                )
                val screeningDatetime = LocalDateTime.of(date, time)
                if (screeningAPI.availableDateTime(screeningDatetime, screening.movie.runtime, screening.theatreID)) {
                    val newScreening = Screening(screening.movie, screeningDatetime, screening.theatreID)
                    if (screeningAPI.updateScreening(screeningID, newScreening)) {
                        println(" Screening has been successfully updated:")
                        println(screeningAPI.getScreening(screeningID))
                    } else println(" Screening has not been updated")
                } else println(" Theatre ${screening.theatreID} not available for screening on that date and time")
            } catch (d: DateTimeParseException) {
                println(" Invalid date entered")
            }
        } else println(" No screening found for ID: $screeningID")
    } else println(" No screenings found")
}

fun updateScreeningTheatre() {
    if (screeningAPI.hasScreenings()) {
        screeningAPI.listAllScreenings()!!.forEach { print(it) }
        println()
        val screeningID = In.readNextInt(" Screening ID to update: ")
        println()
        if (screeningAPI.screeningExists(screeningID)) {
            val screening = screeningAPI.getScreening(screeningID)!!
            val theatreID = In.readNextInt(" New Theatre ID: ")
            println()
            if (screeningAPI.theatreExists(theatreID)) {
                val newScreening = Screening(screening.movie, screening.screeningDateTime, theatreID)
                if (screeningAPI.updateScreening(screeningID, newScreening)) {
                    println(" Screening has been successfully updated:")
                    println(screeningAPI.getScreening(screeningID))
                } else println(" Screening has been not been updated")
            }
        }
    }
}

fun listAllScreenings() =
    if (screeningAPI.hasScreenings()) {
        screeningAPI.listAllScreenings()!!.forEach { println(it) }
        println()
    } else println(" No screenings found")

fun listScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        movieAPI.listAllTitles()!!.forEach { println(it) }
        println()
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasScreeningsByMovie(movieID)) {
                screeningAPI.listScreeningsByMovie(movieID)!!.forEach { println(it) }
                println()
            } else println(" No screenings found for ${movieAPI.getMovie(movieID)!!.title}")
        } else println(" No movie found for ID: $movieID")
    } else println(" No movies found")

fun listScreeningsByDate() =
    if (movieAPI.hasMovies()) {
        try {
            val date =
                LocalDate.parse(
                    In.readNextLine(" Date (dd/mm/yyyy): "),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
            if (screeningAPI.hasScreeningsByDate(date)) {
                screeningAPI
                    .listScreeningsByDate(date)!!
                    .forEach { println(it) }
                println()
            } else println(
                " No screenings for ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
            )
        } catch (d: DateTimeParseException) {
            println(" Invalid date entered")
        }
    } else println(" No movies found")

fun listScreeningsByMovieAndDate() =
    if (movieAPI.hasMovies()) {
        movieAPI.listAllTitles()!!.forEach { println(it) }
        println()
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            try {
                val date =
                    LocalDate.parse(
                        In.readNextLine(" Date (dd/mm/yyyy): "),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                if (screeningAPI.hasScreeningsByMovieAndDate(movieID, date)) {
                    screeningAPI
                        .listScreeningsByMovieAndDate(movieID, date)!!
                        .forEach { println(it) }
                    println()
                } else println(
                    " No screenings found for ${movieAPI.getMovie(movieID)!!.title} on " +
                    date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            } catch (d: DateTimeParseException) {
                println(" Invalid date entered")
            }
        } else println(" No movies found for ID: $movieID")
    } else println(" No movies found")

fun listDayRemainingScreenings() =
    if (screeningAPI.hasDayRemainingScreenings()) {
        screeningAPI
            .listDayRemainingScreenings()!!
            .forEach { println(it) }
        println()
    } else println(" No remaining screenings today")

fun listDayRemainingScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        movieAPI.listAllTitles()!!.forEach { println(it) }
        println()
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasDayRemainingScreeningsByMovie(movieID))
                screeningAPI
                    .listDayRemainingScreeningsByMovie(movieID)!!
                    .forEach{ println(it) }
            else println(
                " No remaining screenings for ${movieAPI.getMovie(movieID)!!.title} today"
            )
        } else println(" No movies found for ID: $movieID")
    } else println(" No movies found")

fun listWeekRemainingScreenings() =
    if (screeningAPI.hasWeekRemainingScreenings()) {
        screeningAPI
            .listWeekRemainingScreenings()!!
            .forEach { println(it) }
        println()
    } else println(" No remaining screenings this week")

fun listWeekRemainingScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        movieAPI.listAllTitles()!!.forEach { println(it) }
        println()
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasWeekRemainingScreeningsByMovie(movieID))
                screeningAPI
                    .listWeekRemainingScreeningsByMovie(movieID)!!
                    .forEach{ println(it) }
            else println(
                " No remaining screenings for ${movieAPI.getMovie(movieID)!!.title} this week"
            )
        } else println(" No movies found for ID: $movieID")
    } else println(" No movies found")

// Customers

fun addCustomer() = println("Add customer called\n")

fun deleteCustomer() = println("Delete customer called\n")

fun updateCustomer() = println("Update customer called\n")

fun viewCustomer() = println("View customer called\n")

fun listAllCustomers() = println("View all customers called\n")

fun listAllAdultCustomers() = println("View all adults customers called\n")

fun listAllChildCustomers() = println("View all child customers called\n")

// Misc

fun getUserOption(): Int {
    val userChoice = In.readNextInt(PROMPT)
    println()
    return userChoice
}

/*
fun initControllersWithSerializer() {
    var option  = 0
    while(option !in 1..2) {
        println("---")
        println("Would you like to save using JSON or XML")
        println("---")
        println("1 - JSON")
        println("2 - XML")
        option  = getUserOption()
        if (option != 1 && option != 2)
            println("You must choose 1 (JSON) or 2 (XML) to proceed")
        println("---")
    }

    if (option == 1) {
        // init with json serializer
        movieAPI = MovieAPI()
        screeningAPI = ScreeningAPI()
    }
    else {
        // init with xml serializer
        movieAPI = MovieAPI()
        screeningAPI = ScreeningAPI()
    }
}*/
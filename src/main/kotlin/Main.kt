import controllers.BookingAPI
import controllers.CustomerAPI
import controllers.MovieAPI
import controllers.ScreeningAPI
import models.Booking
import models.Customer
import models.Movie
import models.Screening
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import utils.MenuUtilities as Menu
import utils.ScannerInput as In
import utils.Utilities as Utils

private lateinit var bookingAPI: BookingAPI
private lateinit var customerAPI: CustomerAPI
private lateinit var movieAPI: MovieAPI
private lateinit var screeningAPI: ScreeningAPI

private const val PROMPT = " > "
private const val datePattern = "dd/MM/yyyy"
private const val timePattern = "HH:mm"
private val datePrompt = " Date (${datePattern.lowercase()}): "
private val timePrompt = " Time (24 hour format - ${timePattern.lowercase()}): "
private val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
private val dateTimeFormatter = DateTimeFormatter.ofPattern("$datePattern $timePattern")

fun main() {
    initControllersWithSerializer()
    load()
    mainMenu()
    save()
}

fun runMenu(menuPrinter: () -> (Unit), options: List<() -> (Unit)>) {
    do {
        menuPrinter()
        val option = getUserOption()

        if (option in 1..options.size) {
            options[option - 1]()
        } else if (option != 0) {
            println(" Invalid selection: $option")
            println()
        }
    } while (option != 0)
}

fun mainMenu() =
    runMenu(
        Menu::printMainMenu,
        listOf(
            ::bookingsMenu,
            ::moviesMenu,
            ::screeningsMenu,
            ::customersMenu
        )
    )

fun bookingsMenu() =
    runMenu(
        Menu::printBookingsMenu,
        listOf(
            ::addBooking,
            ::viewBookingsMenu
        )
    )

fun moviesMenu() =
    runMenu(
        Menu::printMoviesMenu,
        listOf(
            ::addMovie,
            ::viewMovie,
            ::listAllMovies,
            ::listAllMoviesByCert
        )
    )

fun screeningsMenu() =
    runMenu(
        Menu::printScreeningsMenu,
        listOf(
            ::addScreening,
            ::deleteScreeningMenu,
            ::updateScreeningMenu,
            ::viewScreeningsMenu
        )
    )

fun customersMenu() =
    runMenu(
        Menu::printCustomersMenu,
        listOf(
            ::addCustomer,
            ::deleteCustomer,
            ::updateCustomerMenu,
            ::viewCustomersMenu
        )
    )

fun viewBookingsMenu() =
    if (bookingAPI.hasBookings()) {
        runMenu(
            Menu::printViewBookingsMenu,
            listOf(
                ::listAllBookings,
                ::listAllActiveBookings,
                ::listAllCancelledBookings,
                ::listAllBookingsByCustomer,
                ::listAllBookingsByScreening,
                ::listAllBookingsByMovie
            )
        )
    } else {
        println(" No bookings found")
    }

fun deleteScreeningMenu() =
    if (screeningAPI.hasScreenings()) {
        runMenu(
            Menu::printDeleteScreeningsMenu,
            listOf(
                ::deleteScreening,
                ::deleteScreeningByMovie,
                ::deleteScreeningByDate,
                ::deleteAllScreeningsByMovie,
                ::deleteAllScreeningsByDate
            )
        )
    } else {
        println(" No screenings found")
    }

fun updateScreeningMenu() =
    if (screeningAPI.hasScreenings()) {
        runMenu(
            Menu::printUpdateScreeningMenu,
            listOf(
                ::updateScreeningMovie,
                ::updateScreeningDateTime,
                ::updateScreeningTheatre,
                ::updateScreening
            )
        )
    } else {
        println(" No screenings found")
    }

fun viewScreeningsMenu() =
    if (screeningAPI.hasScreenings()) {
        runMenu(
            Menu::printViewScreeningsMenu,
            listOf(
                ::listAllScreenings,
                ::listScreeningsByMovie,
                ::listScreeningsByDate,
                ::listScreeningsByMovieAndDate,
                ::listDayRemainingScreenings,
                ::listDayRemainingScreeningsByMovie,
                ::listWeekRemainingScreenings,
                ::listWeekRemainingScreeningsByMovie
            )
        )
    } else {
        println(" No screenings found")
    }

fun updateCustomerMenu() =
    if (customerAPI.hasCustomers()) {
        runMenu(
            Menu::printUpdateCustomerMenu,
            listOf(
                ::updateCustomerFName,
                ::updateCustomerLName,
                ::updateCustomerEmail,
                ::updateCustomerDOB,
                ::updateCustomer
            )
        )
    } else {
        println(" No customers found")
    }

fun viewCustomersMenu() =
    if (customerAPI.hasCustomers()) {
        runMenu(
            Menu::printViewCustomersMenu,
            listOf(
                ::listAllCustomers,
                ::listAllCustomersByFirstName,
                ::listAllCustomersByLastName,
                ::listAllCustomersByAge,
                ::listAllCustomersByAgeRange,
                ::listAllAdultCustomers,
                ::listAllChildCustomers
            )
        )
    } else {
        println(" No customers found")
    }

// Bookings

fun addBooking() =
    if (screeningAPI.hasScreenings()) {
        val helper = BookingHelperFunctions()
        val movieID = helper.getMovieID()
        if (screeningAPI.hasScreeningsByMovie(movieID)) {
            val screeningID = helper.getScreeningID(movieID)
            if (screeningAPI.screeningExists(screeningID)) {
                val screening = helper.getScreening(movieID, screeningID)
                if (screening != null) {
                    val underage = helper.underAgeCheck(screening.movie.certification)
                    if (!underage) {
                        if (screeningAPI.numberOfAvailableSeats(screeningID) > 0) {
                            val numberOfTickets = helper.numTickets(screeningID)
                            if (numberOfTickets in 1..screening.seats.size) {
                                val seatsList = helper.seatCheck(screeningID)
                                if (screeningAPI.hasSeatsAvailable(screeningID, seatsList)) {
                                    val customer = helper.getCustomer()
                                    if (customer != null) {
                                        val totalPrice = helper.calculatePrice(numberOfTickets)
                                        val booking = bookingAPI.addBooking(
                                            Booking(screening, customer, numberOfTickets, totalPrice, seatsList)
                                        )
                                        if (booking != null && screeningAPI.reserveSeats(screeningID, seatsList)) {
                                            println(" Booking has been successful:")
                                            println(" $booking")
                                        } else {
                                            println(" Booking has not been successful")
                                        }
                                    } else {
                                        println(" Invalid selection")
                                    }
                                } else {
                                    println(" Invalid seats chosen")
                                }
                            } else {
                                println(" Invalid number of tickets chosen")
                            }
                        } else {
                            println(" No seats available")
                        }
                    } else {
                        println(" Must meet the requirements for ${screening.movie.certification}")
                    }
                } else {
                    println(" No screening found of ${movieAPI.getMovie(movieID)!!.title} for screening ID: $screeningID")
                }
            } else {
                println(" No screening found for screening ID: $screeningID")
            }
        } else {
            println(" No screenings found for movie ID: $movieID")
        }
    } else {
        println(" No screenings found")
    }

internal class BookingHelperFunctions {
    fun getMovieID(): Int {
        val movieTitles = screeningAPI.listAllMovieTitles()!!
        Utils.printStringList(movieTitles)
        return In.readNextInt(" Movie ID: ")
    }

    fun getScreeningID(movieID: Int): Int {
        val movieScreenings = screeningAPI.listScreeningsByMovie(movieID)!!
        Utils.printStringList(movieScreenings)
        return In.readNextInt(" Screening ID: ")
    }

    fun getScreening(movieID: Int, screeningID: Int) =
        if (screeningAPI.listScreeningsByMovie(movieID)!!.contains(screeningAPI.getScreening(screeningID).toString())) {
            screeningAPI.getScreening(screeningID)!!
        } else {
            null
        }

    fun underAgeCheck(certification: String): Boolean {
        val restrictedCertifications = listOf("12A", "15A", "16", "18")
        var underage = false
        if (certification in restrictedCertifications) {
            underage = In.readNextLine(
                " Do all customers meet the age/parental supervision requirement" +
                    " for this certification ($certification? (Y/N): \n "
            ).uppercase() != "Y"
        }
        return underage
    }

    fun numTickets(screeningID: Int): Int {
        println(" ${screeningAPI.numberOfAvailableSeats(screeningID)} available seats")
        return In.readNextInt(" Number of tickets: ")
    }

    fun seatCheck(screeningID: Int): List<String> {
        Utils.printStringList(screeningAPI.listAvailableSeats(screeningID)!!)
        val seats = In.readNextLine(" Please choose seats (separated by commas): ")
        return seats.split(",").map { it.trim().uppercase() }.toList()
    }

    fun getCustomer(): Customer? {
        val isCustomer = In.readNextLine(
            " Is this customer registered with this cinema? (Y/N): \n "
        ).uppercase()
        val unregisteredCustomer = Customer("Unregistered", "Customer", "", LocalDate.of(1900, 1, 1))
        val customer = when (isCustomer) {
            "Y" -> {
                var customerID = 0
                val lName = In.readNextLine(" Last name on customer's account: ")
                val customersByLName = customerAPI.listAllCustomersByLastName(lName)
                if (customersByLName != null) {
                    Utils.printStringList(customersByLName)
                    customerID = In.readNextInt(" Customer ID: ")
                    if (!customerAPI.customerExists(customerID)) {
                        println(" Customer not found. Proceeding with unregistered purchase")
                    }
                }
                customerAPI.getCustomer(customerID) ?: unregisteredCustomer
            }

            "N" -> unregisteredCustomer
            else -> null
        }
        return customer
    }

    fun calculatePrice(numberOfTickets: Int) =
        numberOfTickets.toDouble() * 10.0
}

fun listAllBookings() =
    if (bookingAPI.hasBookings()) {
        Utils.printStringList(bookingAPI.listAllBookings()!!)
    } else {
        println(" No bookings found")
    }

fun listAllActiveBookings() =
    if (bookingAPI.hasActiveBookings()) {
        Utils.printStringList(bookingAPI.listAllActiveBookings()!!)
    } else {
        println(" No active bookings found")
    }

fun listAllCancelledBookings() =
    if (bookingAPI.hasCancelledBookings()) {
        Utils.printStringList(bookingAPI.listAllCancelledBookings()!!)
    } else {
        println(" No cancelled bookings found")
    }

fun listAllBookingsByCustomer() =
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID: ")
        if (customerAPI.customerExists(customerID)) {
            if (bookingAPI.hasCustomer(customerID)) {
                Utils.printStringList(bookingAPI.listActiveBookingsByCustomer(customerID)!!)
            } else {
                println(" No bookings found for customer ID: $customerID")
            }
        } else {
            println(" No customer found for customer ID: $customerID")
        }
    } else {
        println(" No customers found")
    }

fun listAllBookingsByScreening() =
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
        val screeningID = In.readNextInt(" Screening ID: ")
        if (screeningAPI.screeningExists(screeningID)) {
            if (bookingAPI.hasScreening(screeningID)) {
                Utils.printStringList(bookingAPI.listActiveBookingsByScreening(screeningID)!!)
            } else {
                println(" No bookings found for screening ID: $screeningID")
            }
        } else {
            println(" No screenings found for screening ID: $screeningID")
        }
    } else {
        println(" No screenings found")
    }

fun listAllBookingsByMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (bookingAPI.listActiveBookingsByMovie(movieID) != null) {
                Utils.printStringList(bookingAPI.listActiveBookingsByMovie(movieID)!!)
            } else {
                println(" No bookings found for movie ID: $movieID")
            }
        } else {
            println(" No movie found for movie ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

// Movies

fun addMovie() {
    val title = In.readNextLine(" Title: ")
    val director = In.readNextLine(" Director: ")
    val runtime = In.readNextInt(" Runtime: ")
    var certification = ""
    var validCert = false
    while (!validCert) {
        certification = In.readNextLine(" Certification: ")
        validCert = movieAPI.isValidCertificate(certification)
        if (!validCert) {
            println(" Certification must be: G, PG, 12A, 15A, 16 or 18")
        }
    }
    if (movieAPI.addMovie(Movie(title, director, runtime, certification))) {
        println(" Movie has been successfully added")
    } else {
        println(" Movie has not been added")
    }
}

fun viewMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" ID: ")
        println(movieAPI.getMovie(movieID) ?: " No movie found for ID: $movieID")
    } else {
        println(" No movies found")
    }

fun listAllMovies() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllMovies()!!)
    } else {
        println(" No movies found")
    }

fun listAllMoviesByCert() =
    if (movieAPI.hasMovies()) {
        val cert = In.readNextLine(" Certification: ")
        if (movieAPI.hasCertificate(cert)) {
            println(movieAPI.listMoviesByCertification(cert))
        } else {
            println(" No movies for certificate: $cert")
        }
    } else {
        println(" No movies found")
    }

// Screenings

fun addScreening() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        val movie: Movie? = movieAPI.getMovie(movieID)
        if (movie != null) {
            val theatreID = In.readNextInt(" Theatre Number(1-3): ")
            if (screeningAPI.theatreExists(theatreID)) {
                val date = Utils.getValidDate(datePrompt, datePattern)
                val time = Utils.getValidTime(timePrompt, timePattern)
                val dateTime = LocalDateTime.of(date, time)

                if (screeningAPI.availableDateTime(dateTime, movie.runtime, theatreID)) {
                    if (screeningAPI.addScreening(Screening(movie, dateTime, theatreID))) {
                        println(" Screening has been successfully added")
                    } else {
                        println(" Screening has not been added")
                    }
                } else {
                    println(" Theatre $theatreID is not available for screening at that date and time")
                }
            } else {
                println(" Theatre $theatreID does not exist")
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies to screen")
    }

fun deleteScreening() =
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
        val screening = screeningAPI.deleteScreening(In.readNextInt(" Screening ID: "))
        if (screening != null) {
            println(" Screening has been successfully deleted:")
            println(" $screening")
        } else {
            println(" Screening has not been deleted")
        }
    } else {
        println(" No screenings found")
    }

fun deleteScreeningByMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasScreeningsByMovie(movieID)) {
                Utils.printStringList(screeningAPI.listScreeningsByMovie(movieID)!!)
                val screeningID = In.readNextInt(" Screening ID: ")
                if (screeningAPI.screeningExists(screeningID)) {
                    val screening = screeningAPI.getScreening(screeningID)!!
                    if (screening.movie.movieID == movieID) {
                        if (screeningAPI.deleteScreening(screeningID) != null) {
                            println(" Screening has been successfully deleted:")
                            println(" $screening")
                        } else {
                            println(" Screening has not been deleted")
                        }
                    } else {
                        println(
                            " Invalid screening ID: $screeningID for ${screening.movie.title}"
                        )
                    }
                } else {
                    println(" No screening found for ID: $screeningID")
                }
            } else {
                println(
                    " No screenings found for ${movieAPI.getMovie(movieID)!!.title}"
                )
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

fun deleteScreeningByDate() =
    if (screeningAPI.hasScreenings()) {
        val date = Utils.getValidDate(datePrompt, datePattern)
        if (screeningAPI.hasScreeningsByDate(date)) {
            Utils.printStringList(screeningAPI.listScreeningsByDate(date)!!)
            val screeningID = In.readNextInt(" Screening ID: ")
            if (screeningAPI.screeningExists(screeningID)) {
                val screening = screeningAPI.getScreening(screeningID)!!
                if (screening.screeningDateTime.toLocalDate() == date) {
                    if (screeningAPI.deleteScreening(screeningID) != null) {
                        println(" Screening has been successfully deleted:")
                        println(" $screening")
                    } else {
                        println(" Screening has not been deleted")
                    }
                } else {
                    println(
                        " Invalid screening ID: $screeningID for " +
                            date.format(dateFormatter)
                    )
                }
            } else {
                println(" No screening found for ID: $screeningID")
            }
        } else {
            println(
                " No screenings found for ${date.format(dateFormatter)}"
            )
        }
    } else {
        println(" No screenings found")
    }

fun deleteAllScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasScreeningsByMovie(movieID)) {
                val numberOfScreenings = screeningAPI.numberOfScreeningsByMovie(movieID)
                if (screeningAPI.deleteScreeningsByMovie(movieID) != null) {
                    println(" $numberOfScreenings screenings for ${movieAPI.getMovie(movieID)!!.title} have been deleted")
                } else {
                    println(" Screenings have not been deleted")
                }
            } else {
                println(" No screenings found for ${movieAPI.getMovie(movieID)!!.title}")
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

fun deleteAllScreeningsByDate() =
    if (screeningAPI.hasScreenings()) {
        val date = Utils.getValidDate(datePattern, datePattern)
        if (screeningAPI.hasScreeningsByDate(date)) {
            val numberOfScreenings = screeningAPI.numberOfScreeningsByDate(date)
            if (screeningAPI.deleteScreeningsByDate(date) != null) {
                println(
                    " $numberOfScreenings screenings on ${date.format(dateFormatter)} " +
                        " have been successfully deleted"
                )
            } else {
                println(" Screenings have not been deleted")
            }
        } else {
            println(
                " No screenings found for ${date.format(dateFormatter)}"
            )
        }
    } else {
        println(" No screenings found")
    }

fun updateScreening() =
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
        val screeningID = In.readNextInt(" Screening ID to update: ")
        if (screeningAPI.screeningExists(screeningID)) {
            Utils.printStringList(movieAPI.listAllTitles()!!)
            val movieID = In.readNextInt(" New movie ID: ")
            val movie = movieAPI.getMovie(movieID)
            if (movie != null) {
                val theatreID = In.readNextInt(" New theatre ID: ")
                if (screeningAPI.theatreExists(theatreID)) {
                    val date = Utils.getValidDate(datePrompt, datePattern)
                    val time = Utils.getValidTime(timePrompt, timePattern)
                    val screeningDatetime = LocalDateTime.of(date, time)
                    if (screeningAPI.availableDateTime(screeningDatetime, movie.runtime, theatreID)) {
                        val newScreening = Screening(movie, screeningDatetime, theatreID)
                        if (screeningAPI.updateScreening(screeningID, newScreening)) {
                            println(" Screening has been successfully updated:")
                            println(" ${screeningAPI.getScreening(screeningID)}")
                        } else {
                            println(" Screening has not been updated")
                        }
                    } else {
                        println(
                            " Theatre $theatreID is not available on " +
                                screeningDatetime.format(dateTimeFormatter)
                        )
                    }
                } else {
                    println(" No theatre found for ID: $theatreID")
                }
            } else {
                println(" No movie found for ID: $movieID")
            }
        } else {
            println(" No screening found for ID: $screeningID")
        }
    } else {
        println(" No screenings found")
    }

fun updateScreeningMovie() {
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
        val screeningID = In.readNextInt(" Screening ID to update: ")
        if (screeningAPI.screeningExists(screeningID)) {
            val screening = screeningAPI.getScreening(screeningID)!!
            Utils.printStringList(movieAPI.listAllTitles()!!)
            val movieID = In.readNextInt(" New Movie ID: ")
            val movie = movieAPI.getMovie(movieID)
            if (movie != null) {
                val newScreening = screening.copy(movie = movie)
                if (screeningAPI.updateScreening(screeningID, newScreening)) {
                    println(" Screening has been successfully updated:")
                    println(" ${screeningAPI.getScreening(screeningID)}")
                } else {
                    println(" Screening has not been updated")
                }
            } else {
                println(" No movie found for ID: $movieID")
            }
        } else {
            println(" No screening found for ID: $screeningID")
        }
    } else {
        println(" No screenings found")
    }
}

fun updateScreeningDateTime() {
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
        val screeningID = In.readNextInt(" Screening ID to update: ")
        if (screeningAPI.screeningExists(screeningID)) {
            val screening = screeningAPI.getScreening(screeningID)!!
            val date = Utils.getValidDate("New ${datePrompt.lowercase()}", datePattern)
            val time = Utils.getValidTime("New ${timePrompt.lowercase()}", timePattern)
            val screeningDatetime = LocalDateTime.of(date, time)
            if (screeningAPI.availableDateTime(screeningDatetime, screening.movie.runtime, screening.theatreID)) {
                val newScreening = screening.copy(screeningDateTime = screeningDatetime)
                if (screeningAPI.updateScreening(screeningID, newScreening)) {
                    println(" Screening has been successfully updated:")
                    println(" ${screeningAPI.getScreening(screeningID)}")
                } else {
                    println(" Screening has not been updated")
                }
            } else {
                println(
                    " Theatre ${screening.theatreID} is not available for screening on " +
                        screeningDatetime.format(dateTimeFormatter)
                )
            }
        } else {
            println(" No screening found for ID: $screeningID")
        }
    } else {
        println(" No screenings found")
    }
}

fun updateScreeningTheatre() {
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
        val screeningID = In.readNextInt(" Screening ID to update: ")
        if (screeningAPI.screeningExists(screeningID)) {
            val screening = screeningAPI.getScreening(screeningID)!!
            val theatreID = In.readNextInt(" New theatre ID: ")
            if (screeningAPI.theatreExists(theatreID)) {
                val newScreening = screening.copy(theatreID = theatreID)
                if (screeningAPI.updateScreening(screeningID, newScreening)) {
                    println(" Screening has been successfully updated:")
                    println(" ${screeningAPI.getScreening(screeningID)}")
                } else {
                    println(" Screening has been not been updated")
                }
            } else {
                println(" Theatre not found for ID: $theatreID")
            }
        } else {
            println(" No screening found for ID: $screeningID")
        }
    }
}

fun listAllScreenings() =
    if (screeningAPI.hasScreenings()) {
        Utils.printStringList(screeningAPI.listAllScreenings()!!)
    } else {
        println(" No screenings found")
    }

fun listScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasScreeningsByMovie(movieID)) {
                Utils.printStringList(screeningAPI.listScreeningsByMovie(movieID)!!)
            } else {
                println(" No screenings found for ${movieAPI.getMovie(movieID)!!.title}")
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

fun listScreeningsByDate() =
    if (movieAPI.hasMovies()) {
        val date = Utils.getValidDate(datePrompt, datePattern)
        if (screeningAPI.hasScreeningsByDate(date)) {
            Utils.printStringList(screeningAPI.listScreeningsByDate(date)!!)
        } else {
            println(" No screenings for ${date.format(dateFormatter)}")
        }
    } else {
        println(" No movies found")
    }

fun listScreeningsByMovieAndDate() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            val date = Utils.getValidDate(datePrompt, datePattern)
            if (screeningAPI.hasScreeningsByMovieAndDate(movieID, date)) {
                Utils.printStringList(
                    screeningAPI.listScreeningsByMovieAndDate(movieID, date)!!
                )
            } else {
                println(
                    " No screenings found for ${movieAPI.getMovie(movieID)!!.title} on " +
                        date.format(dateFormatter)
                )
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

fun listDayRemainingScreenings() =
    if (screeningAPI.hasDayRemainingScreenings()) {
        Utils.printStringList(
            screeningAPI.listDayRemainingScreenings()!!
        )
    } else {
        println(" No remaining screenings today")
    }

fun listDayRemainingScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasDayRemainingScreeningsByMovie(movieID)) {
                Utils.printStringList(
                    screeningAPI
                        .listDayRemainingScreeningsByMovie(movieID)!!
                )
            } else {
                println(
                    " No remaining screenings for ${movieAPI.getMovie(movieID)!!.title} today"
                )
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

fun listWeekRemainingScreenings() =
    if (screeningAPI.hasWeekRemainingScreenings()) {
        Utils.printStringList(screeningAPI.listWeekRemainingScreenings()!!)
    } else {
        println(" No remaining screenings this week")
    }

fun listWeekRemainingScreeningsByMovie() =
    if (movieAPI.hasMovies()) {
        Utils.printStringList(movieAPI.listAllTitles()!!)
        val movieID = In.readNextInt(" Movie ID: ")
        if (movieAPI.movieExists(movieID)) {
            if (screeningAPI.hasWeekRemainingScreeningsByMovie(movieID)) {
                Utils.printStringList(
                    screeningAPI
                        .listWeekRemainingScreeningsByMovie(movieID)!!
                )
            } else {
                println(
                    " No remaining screenings for ${movieAPI.getMovie(movieID)!!.title} this week"
                )
            }
        } else {
            println(" No movie found for ID: $movieID")
        }
    } else {
        println(" No movies found")
    }

// Customers

fun addCustomer() {
    val fName = In.readNextLine(" First Name: ")
    val lName = In.readNextLine(" Last Name: ")
    val email = Utils.getValidEmail(" Email: ")
    val dob = Utils.getValidDate(" Date of birth ($datePattern): ", datePattern)
    if (customerAPI.addCustomer(Customer(fName, lName, email, dob))) {
        println(" Customer has been successfully added")
    } else {
        println(" Customer has not been added")
    }
}

fun deleteCustomer() =
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID: ")
        if (customerAPI.customerExists(customerID)) {
            val customer = customerAPI.deleteCustomer(customerID)
            if (customer != null) {
                println(" Customer has been successfully deleted:")
                println(" $customer")
            } else {
                println(" Customer has not been deleted")
            }
        } else {
            println(" No customer found for ID: $customerID")
        }
    } else {
        println(" No customers found")
    }

fun updateCustomer() =
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID to update: ")
        if (customerAPI.customerExists(customerID)) {
            val fName = In.readNextLine(" New first name: ")
            val lName = In.readNextLine(" New last name: ")
            val email = Utils.getValidEmail(" New email: ")
            val dob = Utils.getValidDate(" New date of birth ($datePattern): ", datePattern)
            val newCustomer = Customer(fName, lName, email, dob)
            if (customerAPI.updateCustomer(customerID, newCustomer)) {
                println(" Customer has been successfully updated:")
                println(" ${customerAPI.getCustomer(customerID)}")
            } else {
                println(" Customer has not been updated")
            }
        } else {
            println(" No customer found for ID: $customerID")
        }
    } else {
        println(" No customers found")
    }

fun updateCustomerFName() {
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID to update: ")
        if (customerAPI.customerExists(customerID)) {
            val customer = customerAPI.getCustomer(customerID)!!
            val fName = In.readNextLine(" New customer first name: ")
            val newCustomer = customer.copy(fName = fName)
            if (customerAPI.updateCustomer(customerID, newCustomer)) {
                println(" Customer has been successfully updated:")
                println(" ${customerAPI.getCustomer(customerID)}")
            } else {
                println(" Customer has not been updated")
            }
        } else {
            println(" No customer found for ID: $customerID")
        }
    } else {
        println(" No customers found")
    }
}

fun updateCustomerLName() {
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID to update: ")
        if (customerAPI.customerExists(customerID)) {
            val customer = customerAPI.getCustomer(customerID)!!
            val lName = In.readNextLine(" New customer last name: ")
            val newCustomer = customer.copy(lName = lName)
            if (customerAPI.updateCustomer(customerID, newCustomer)) {
                println(" Customer has been successfully updated:")
                println(" ${customerAPI.getCustomer(customerID)}")
            } else {
                println(" Customer has not been updated")
            }
        } else {
            println(" No customer found for ID: $customerID")
        }
    } else {
        println(" No customers found")
    }
}

fun updateCustomerEmail() {
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID to update: ")
        if (customerAPI.customerExists(customerID)) {
            val customer = customerAPI.getCustomer(customerID)!!
            val email = Utils.getValidEmail(" New customer email: ")
            val newCustomer = customer.copy(email = email)
            if (customerAPI.updateCustomer(customerID, newCustomer)) {
                println(" Customer has been successfully updated:")
                println(" ${customerAPI.getCustomer(customerID)}")
            } else {
                println(" Customer has not been updated")
            }
        } else {
            println(" No customer found for ID: $customerID")
        }
    } else {
        println(" No customers found")
    }
}

fun updateCustomerDOB() {
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
        val customerID = In.readNextInt(" Customer ID to update: ")
        if (customerAPI.customerExists(customerID)) {
            val customer = customerAPI.getCustomer(customerID)!!
            val dob = Utils.getValidDate(" New date of birth ($datePattern): ", datePattern)
            val newCustomer = customer.copy(dob = dob)
            if (customerAPI.updateCustomer(customerID, newCustomer)) {
                println(" Customer has been successfully updated:")
                println(" ${customerAPI.getCustomer(customerID)}")
            } else {
                println(" Customer has not been updated")
            }
        } else {
            println(" No customer found for ID: $customerID")
        }
    } else {
        println(" No customers found")
    }
}

fun listAllCustomers() =
    if (customerAPI.hasCustomers()) {
        Utils.printStringList(customerAPI.listAllCustomers()!!)
    } else {
        println(" No customers found")
    }

fun listAllCustomersByFirstName() =
    if (customerAPI.hasCustomers()) {
        val fName = In.readNextLine(" First Name: ")
        val customerList =
            customerAPI
                .listAllCustomersByFirstName(
                    fName
                )
        if (customerList != null) {
            Utils.printStringList(customerList)
        } else {
            println(" No customers found with first name: $fName")
        }
    } else {
        println(" No customers found")
    }

fun listAllCustomersByLastName() =
    if (customerAPI.hasCustomers()) {
        val lName = In.readNextLine(" Last Name: ")
        val customerList =
            customerAPI
                .listAllCustomersByLastName(
                    lName
                )
        if (customerList != null) {
            Utils.printStringList(customerList)
        } else {
            println(" No customers found with last name: $lName")
        }
    } else {
        println(" No customers found")
    }

fun listAllCustomersByAge() =
    if (customerAPI.hasCustomers()) {
        val age = In.readNextInt(" Age: ")
        val customerList =
            customerAPI
                .listAllCustomersByAge(
                    age
                )
        if (customerList != null) {
            Utils.printStringList(customerList)
        } else {
            println(" No customers found with age: age")
        }
    } else {
        println(" No customers found")
    }

fun listAllCustomersByAgeRange() {
    if (customerAPI.hasCustomers()) {
        val minAge = In.readNextInt(" Minimum Age: ")
        val maxAge = In.readNextInt(" Maximum Age: ")
        val customerList =
            customerAPI
                .listAllCustomersByAgeRange(
                    minAge,
                    maxAge
                )
        if (customerList != null) {
            Utils.printStringList(customerList)
        } else {
            println(" No customers found within age range: $minAge - $maxAge")
        }
    } else {
        println(" No customers found")
    }
}

fun listAllAdultCustomers() =
    if (customerAPI.hasAdultCustomers()) {
        Utils.printStringList(customerAPI.listAllAdultCustomers()!!)
    } else {
        println(" No adult customers found")
    }

fun listAllChildCustomers() {
    if (customerAPI.hasChildCustomers()) {
        Utils.printStringList(customerAPI.listAllChildCustomers()!!)
    } else {
        println(" No child customers found")
    }
}

// Miscellaneous

fun getUserOption(): Int {
    val userChoice = In.readNextInt(PROMPT)
    println()
    return userChoice
}

fun initControllersWithSerializer() {
    var option = 0
    while (option !in 1..2) {
        println(" ---")
        println(" Would you like to save using JSON or XML")
        println(" ---")
        println(" 1 - JSON")
        println(" 2 - XML")
        option = getUserOption()
        if (option != 1 && option != 2) {
            println(" You must choose 1 (JSON) or 2 (XML) to proceed")
        }
        println(" ---")
    }

    if (option == 1) {
        // JSON
        bookingAPI = BookingAPI(JSONSerializer(File("bookings.json")))
        customerAPI = CustomerAPI(JSONSerializer(File("customers.json")))
        movieAPI = MovieAPI(JSONSerializer(File("movies.json")))
        screeningAPI = ScreeningAPI(JSONSerializer(File("screenings.json")))
    } else {
        // XML
        bookingAPI = BookingAPI(XMLSerializer(File("bookings.xml")))
        customerAPI = CustomerAPI(XMLSerializer(File("customers.xml")))
        movieAPI = MovieAPI(XMLSerializer(File("movies.xml")))
        screeningAPI = ScreeningAPI(XMLSerializer(File("screenings.xml")))
    }
}

fun save() {
    try {
        // bookingAPI.store()
        customerAPI.store()
        movieAPI.store()
        screeningAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        // bookingAPI.load()
        customerAPI.load()
        movieAPI.load()
        screeningAPI.load()

        addBookings()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

private fun addBookings() {
    val paddingtonScreening1 = screeningAPI.getScreening(1000)!!
    val paddingtonScreening2 = screeningAPI.getScreening(1001)!!
    val matrixScreening1 = screeningAPI.getScreening(1002)!!
    val matrixScreening2 = screeningAPI.getScreening(1003)!!
    val aoife = customerAPI.getCustomer(1000)!!
    val brendan = customerAPI.getCustomer(1001)!!
    val cillian = customerAPI.getCustomer(1002)!!

    bookingAPI.addBooking(Booking(paddingtonScreening1, aoife, 2, 19.50, listOf("A1", "A2")))
    bookingAPI.addBooking(Booking(paddingtonScreening2, brendan, 1, 10.00, listOf("A1")))
    bookingAPI.addBooking(Booking(matrixScreening1, cillian, 1, 10.00, listOf("B1")))
    bookingAPI.addBooking(Booking(matrixScreening2, aoife, 1, 10.00, listOf("A1")))
    bookingAPI.addBooking(Booking(paddingtonScreening1, brendan, 1, 10.00, listOf("B1")))
    bookingAPI.addBooking(Booking(matrixScreening1, brendan, 1, 10.00, listOf("A1")))
}

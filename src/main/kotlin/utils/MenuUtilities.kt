package utils

object MenuUtilities {

    private fun printMenu(heading: String, menuOptions: List<String>) {
        val optionLowerBound = 0
        val optionUpperBound = menuOptions.size - 1

        val optionsString =
            menuOptions
                .dropLast(1)
                .joinToString(separator = "\n") {
                    option -> "| ${menuOptions.indexOf(option) + 1} - $option"
                }

        print(
            """
            | $heading
            | ---
            $optionsString
            | $optionLowerBound - ${menuOptions.last()}
            | ---
            | Please enter a number between $optionLowerBound and $optionUpperBound
            | """.trimMargin()
        )
    }

    @JvmStatic
    fun printMainMenu() =
        printMenu(
            "Main Menu",
            listOf(
                "Bookings Menu",
                "Movies Menu",
                "Screenings Menu",
                "Customers Menu",
                "Reports Menu",
                "Exit",
            )
        )

    @JvmStatic
    fun printBookingsMenu() =
        printMenu(
            "Bookings Menu",
            listOf(
                "Add a booking",
                "View a booking",
                "View all bookings by movie",
                "View all bookings by date",
                "Return to Main Menu",
            )
        )

    @JvmStatic
    fun printMoviesMenu() =
        printMenu(
            "Movies Menu",
            listOf(
                "Add a movie",
                "View movie details",
                "View all movies",
                "View all movies by certification",
                "Return to Main Menu",
            )
        )

    @JvmStatic
    fun printScreeningsMenu() =
        printMenu(
            "Screenings Menu",
            listOf(
                "Add a screening",
                "Delete a screening",
                "Update a screening",
                "View a screening",
                "Return to Main Menu",
            )
        )

    @JvmStatic
    fun printCustomersMenu() =
        printMenu(
            "Customers Menu",
            listOf(
                "Add a customer",
                "Delete a customer",
                "Update a customer",
                "View a customer",
                "View all customers",
                "View all adult customers",
                "View all child customers",
                "Return to Main Menu",
            )
        )

    @JvmStatic
    fun printReportsMenu() =
        printMenu(
            "Reports Menu",
            listOf(
                "View reports",
                "Return to Main Menu",
            )
        )

    @JvmStatic
    fun printDeleteScreeningMenu() =
        printMenu(
            "Delete Screening Menu",
            listOf(
                "Delete a screening",
                "Delete a screening by movie",
                "Delete a screening by date",
                "Delete all screenings by movie",
                "Delete all screenings by date",
                "Return to Screenings Menu",
            )
        )

    @JvmStatic
    fun printUpdateScreeningMenu() =
        printMenu(
            "Update Screening Menu",
            listOf(
                "Update screening's movie",
                "Update screening's date and time",
                "Update screening's theatre",
                "Update all details for screening",
                "Return to Screenings Menu"
            )
        )

    @JvmStatic
    fun printViewScreeningMenu() =
        printMenu(
            "View Screening Menu",
            listOf(
                "View all screenings",
                "View screenings by movie",
                "View screenings by date",
                "View screenings by movie and date",
                "View today's remaining screenings",
                "View today's remaining screenings by movie",
                "View this week's remaining screenings",
                "View this week's remaining screenings by movie",
                "Return to Screenings Menu",
            )
        )
}
package utils

object MenuUtilities {

    private fun printMenu(heading: String, menuOptions: List<String>) {
        val optionLowerBound = 0
        val optionUpperBound = menuOptions.size - 1

        val optionsString =
            menuOptions
                .dropLast(1)
                .joinToString(separator = "\n") {
                        option ->
                    "| ${menuOptions.indexOf(option) + 1} - $option"
                }

        print(
            """
            | $heading
            | ---
            $optionsString
            | $optionLowerBound - ${menuOptions.last()}
            | ---
            | Please enter a number between $optionLowerBound and $optionUpperBound
            |
            """.trimMargin()
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
                "Exit"
            )
        )

    @JvmStatic
    fun printBookingsMenu() =
        printMenu(
            "Bookings Menu",
            listOf(
                "Add a booking",
                "View bookings",
                "Return to Main Menu"
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
                "Return to Main Menu"
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
                "View screenings",
                "Return to Main Menu"
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
                "View customers",
                "Return to Main Menu"
            )
        )

    @JvmStatic
    fun printViewBookingsMenu() =
        printMenu(
            "View Bookings Menu",
            listOf(
                "View all bookings",
                "View all active bookings",
                "View all cancelled bookings",
                "View all bookings by customer",
                "View all bookings by screening",
                "View all bookings by movie",
                "Return to Bookings Menu"
            )
        )

    @JvmStatic
    fun printDeleteScreeningsMenu() =
        printMenu(
            "Delete Screenings Menu",
            listOf(
                "Delete a screening",
                "Delete a screening by movie",
                "Delete a screening by date",
                "Delete all screenings by movie",
                "Delete all screenings by date",
                "Return to Screenings Menu"
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
    fun printViewScreeningsMenu() =
        printMenu(
            "View Screenings Menu",
            listOf(
                "View all screenings",
                "View screenings by movie",
                "View screenings by date",
                "View screenings by movie and date",
                "View today's remaining screenings",
                "View today's remaining screenings by movie",
                "View this week's remaining screenings",
                "View this week's remaining screenings by movie",
                "Return to Screenings Menu"
            )
        )

    @JvmStatic
    fun printUpdateCustomerMenu() =
        printMenu(
            "Update Customer Menu",
            listOf(
                "Update customer's first name",
                "Update customer's last name",
                "Update customer's email",
                "Update customer's date of birth",
                "Update all details for customer",
                "Return to Customers Menu"
            )
        )

    @JvmStatic
    fun printViewCustomersMenu() =
        printMenu(
            "View Customers Menu",
            listOf(
                "View all customers",
                "View all customers by first name",
                "View all customers by last name",
                "View all customers by age",
                "View all customers by age range",
                "View all adult customers",
                "View all child customers",
                "Returns to Customers Menu"
            )
        )
}

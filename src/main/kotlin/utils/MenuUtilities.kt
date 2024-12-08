package utils

object MenuUtilities {

    private const val TEXT_AREA_WIDTH = 52

    private fun printMenu(heading: String, menuOptions: List<String>) {
        val optionLowerBound = 0
        val optionUpperBound = menuOptions.size - 1

        val lineDivide = "| ┝━━━━━━━╋━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┥"
        val optionFormatString = "%-${TEXT_AREA_WIDTH - 10}s"
        val optionsString =
            menuOptions
                .dropLast(1)
                .joinToString(separator = "\n$lineDivide\n") {
                        option ->
                    "| │${String.format("%4s", (menuOptions.indexOf(option) + 1))}   ┃  " +
                        "${String.format(optionFormatString, option)}│"
                }

        val lastOption =
            "$lineDivide\n" +
                "| │${String.format("%4s", optionLowerBound)}   ┃  " +
                "${String.format(optionFormatString, menuOptions.last())}│"

        print(
            """
            | ╭────────────────────────────────────────────────────╮
            | │                    Cinema System                   │
            | ┝━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┥
            ${centerHeading(heading)}
            | ┝━━━━━━━┳━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┥
            $optionsString
            $lastOption
            | ╰───────┸────────────────────────────────────────────╯
            |  Please enter a number between $optionLowerBound and $optionUpperBound:
            | 
            """.trimMargin()
        )
    }

    private fun centerHeading(heading: String): String {
        val paddingAvailable = TEXT_AREA_WIDTH - heading.length
        val paddingLeft: Int
        val paddingRight: Int
        if (paddingAvailable % 2 == 0) {
            paddingLeft = paddingAvailable / 2
            paddingRight = paddingLeft
        } else {
            paddingLeft = (paddingAvailable / 2) + 1
            paddingRight = paddingLeft - 1
        }

        val formatString = "| │%2\$${paddingLeft}s%1\$s%2\$${paddingRight}s│"
        return String.format(formatString, heading, " ")
    }

    @JvmStatic
    fun printStringList(list: List<String>) {
        val lineDivide = "\n ├────────────────────────────────────────────────────┤\n"
        val listString = list.joinToString(separator = lineDivide) { it }

        println(" ╭────────────────────────────────────────────────────╮")
        println(listString)
        println(" ╰────────────────────────────────────────────────────╯")
        println()
    }

    @JvmStatic
    fun printMessage(message: String) {
        val messageFormat = "%-50s"
        println(" ╭────────────────────────────────────────────────────╮")
        println(" │ ${String.format(messageFormat, message)} │")
        println(" ╰────────────────────────────────────────────────────╯")
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
                "View todays remaining screenings",
                "View todays remaining screenings by movie",
                "View weeks remaining screenings",
                "View weeks remaining screenings by movie",
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

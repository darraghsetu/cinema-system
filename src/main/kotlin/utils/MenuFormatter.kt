package utils

/*
 * NOTES:
 * Add check in printMenu for empty menu options list
 */

fun printMenu(heading: String, menuOptions: List<String>) {
    val optionLowerBound = 0
    val optionUpperBound = menuOptions.size - 1

    val optionsString = menuOptions
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

fun printMainMenu() {
    val title = "Main Menu"
    val menuOptions = listOf(
        "Bookings Menu",
        "Movies Menu",
        "Screenings Menu",
        "Customers Menu",
        "Reports Menu",
        "Exit",
    )

    printMenu(title, menuOptions)
}

fun printBookingsMenu() {
    val title = "Bookings Menu"
    val menuOptions = listOf(
        "Add a booking",
        "View a booking",
        "View all bookings by movie",
        "View all bookings by date",
        "Return to Main Menu",
    )
    
    printMenu(title, menuOptions)
}

fun printMoviesMenu() {
    val title = "Movies Menu"
    val menuOptions = listOf(
        "Add a movie",
        "View a movie",
        "View all movies",
        "View all movies by certification",
        "Return to Main Menu",
    )

    printMenu(title, menuOptions)
}

fun printScreeningsMenu() {
    val title = "Screening Menu"
    val menuOptions = listOf(
        "Add a screening",
        "Delete a screening",
        "Delete all screenings by movie",
        "Delete all screenings by date(s)",
        "Update a screening",
        "View a screening",
        "View all screenings by movie",
        "View all screenings by date",
        "Return to Main Menu",
    )

    printMenu(title, menuOptions)
}

fun printCustomersMenu() {
    val title = "Customers Menu"
    val menuOptions = listOf(
        "Add a customer",
        "Delete a customer",
        "Update a customer",
        "View a customer",
        "View all customers",
        "View all adult customers",
        "View all child customers",
        "Return to Main Menu",
    )

    printMenu(title, menuOptions)
}

fun printReportsMenu() {
    val title = "Reports Menu"
    val menuOptions = listOf(
        "View reports",
        "Return to Main Menu",
    )

    printMenu(title, menuOptions)
}
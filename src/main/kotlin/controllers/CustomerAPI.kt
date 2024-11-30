package controllers

import models.Customer
import models.Screening
import java.time.LocalDate
import java.time.Period

class CustomerAPI() {

    private val customers = ArrayList<Customer>()
    private var currentID = 1000

    fun addCustomer(customer: Customer) =
        if (isValidEmail(customer.email)) {
            customer.customerID = getNextID()
            customers.add(customer)
        } else false

    fun getCustomer(id: Int) =
        customers.find { it.customerID == id }

    private fun getNextID() =
        currentID++

    fun updateCustomer(id: Int, customer: Customer) =
        if (customerExists(id)) {
            val customerToUpdate = getCustomer(id)!!
            customerToUpdate.fName = customer.fName
            customerToUpdate.lName = customer.lName
            customerToUpdate.email = customer.email
            customerToUpdate.dob = customer.dob
            customer.isAdult = false
            true
        } else false

    fun deleteCustomer(id: Int) =
        if (customerExists(id)) {
            val customer = getCustomer(id)
            customers.remove(customer)
            customer
        } else null

    private fun filterCustomers(predicate: (Customer) -> (Boolean)) =
        if (hasCustomers())
            customers
                .filter{ predicate(it) }
                .map{ it.toString() }
                .ifEmpty { null }
        else null

    fun listAllCustomers() =
        filterCustomers { true }

    fun listAllCustomersByFirstName(fName: String) =
        filterCustomers { it.fName == fName }

    fun listAllCustomersByLastName(lName: String) =
        filterCustomers { it.lName == lName }

    fun listAllCustomersByAge(age: Int) =
        filterCustomers { calculateCustomerAge(it.dob) == age }

    fun listAllCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        filterCustomers {
            calculateCustomerAge(it.dob) in lowerAgeInclusive..upperAgeInclusive
        }

    fun listAllAdultCustomers() =
        filterCustomers { isAdult(it) }

    fun listAllChildCustomers() =
        filterCustomers { !isAdult(it) }

    fun numberOfCustomers() =
        customers.size

    fun numberOfAdultCustomers() =
        listAllAdultCustomers()?.size ?: 0

    fun numberOfChildCustomers() =
        numberOfCustomers() - numberOfAdultCustomers()

    fun numberOfCustomersByAge(age: Int) =
        listAllCustomersByAge(age)?.size ?: 0

    fun numberOfCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        listAllCustomersByAgeRange(lowerAgeInclusive, upperAgeInclusive)?.size ?: 0

    fun hasCustomers() =
        numberOfCustomers() > 0

    fun hasAdultCustomers() =
        numberOfAdultCustomers() > 0

    fun hasChildCustomers() =
        numberOfChildCustomers() > 0

    fun hasCustomersByAge(age: Int) =
        numberOfCustomersByAge(age) > 0

    fun hasCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        numberOfCustomersByAgeRange(lowerAgeInclusive, upperAgeInclusive) > 0

    fun customerExists(id: Int) =
        customers.find { it.customerID == id } != null

    private fun isAdult(customer: Customer) =
        if (customer.isAdult) true
        else {
            if (Period.between(customer.dob, LocalDate.now()).years >= 18) {
                customer.isAdult = true
            }
            customer.isAdult
        }

    @Suppress("RegExpRedundantEscape")
    private fun isValidEmail(email: String) =
        /*
         * NOT ORIGINAL WORK:
         * This regular expression is not my own work,
         * it was obtained from: https://www.emailregex.com/
         */
        Regex(
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+" +
            ")*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7" +
            "f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-" +
            "z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0" +
            "-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]" +
            "?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1" +
            "f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        )
        .matches(email)

    private fun calculateCustomerAge(dob: LocalDate) =
        Period.between(dob, LocalDate.now()).years
}
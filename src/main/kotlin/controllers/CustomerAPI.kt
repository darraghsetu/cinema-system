package controllers

import models.Customer
import utils.Utilities as Utils
import java.time.LocalDate
import java.time.Period

class CustomerAPI {
    private val customers = ArrayList<Customer>()
    private var currentID = 1000

    fun addCustomer(customer: Customer): Boolean {
        customer.customerID = getNextID()
        return customers.add(customer)
    }

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
        customers
            .filter{ predicate(it) }
            .map{ it.toString() }
            .ifEmpty { null }

    fun listAllCustomers() =
        filterCustomers { true }

    fun listAllCustomersByFirstName(fName: String) =
        filterCustomers { it.fName == fName }

    fun listAllCustomersByLastName(lName: String) =
        filterCustomers { it.lName == lName }

    fun listAllCustomersByAge(age: Int) =
        filterCustomers { Utils.dateToYears(it.dob) == age }

    fun listAllCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        filterCustomers {
            Utils.dateToYears(it.dob) in lowerAgeInclusive..upperAgeInclusive
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

    private fun isAdult(customer: Customer) =
        if (customer.isAdult) true
        else {
            if (Period.between(customer.dob, LocalDate.now()).years >= 18) {
                customer.isAdult = true
            }
            customer.isAdult
        }

    fun customerExists(id: Int) =
        customers.find { it.customerID == id } != null
}
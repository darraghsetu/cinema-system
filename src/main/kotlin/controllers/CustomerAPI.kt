package controllers

import models.Customer
import persistence.Serializer
import java.time.LocalDate
import java.time.Period
import utils.Utilities as Utils

/**
 * This class serves as a controller for managing [Customer] data.
 * It leverages an `XStream` serializer, specifically either [persistence.JSONSerializer] or [persistence.XMLSerializer],
 * to handle data serialization and deserialization.
 *
 * @param serializerType The specific [Serializer] implementation to be used for data serialization/deserialization.
 * @constructor Initialises the CustomerAPI with the specified serializerType
 */
class CustomerAPI(serializerType: Serializer) {

    private val serializer: Serializer = serializerType
    private var customers = ArrayList<Customer>()
    private var currentID = 1000

    /**
     * Adds a new customer to the API.
     *
     * @param customer the customer object to be added
     * @return `true` if the customer is added successfully, `false` otherwise
     */
    fun addCustomer(customer: Customer): Boolean {
        customer.customerID = getNextID()
        return customers.add(customer)
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the ID of the customer to retrieve
     * @return the customer with the specified ID, or `null` if not found
     */
    fun getCustomer(id: Int) =
        customers.find { it.customerID == id }

    private fun getNextID() =
        currentID++

    /**
     * Updates an existing customer.
     *
     * @param id the ID of the customer to update
     * @param customer the updated customer data, excluding the ID
     * @return `true` if the customer is updated successfully, `false` otherwise
     */
    fun updateCustomer(id: Int, customer: Customer) =
        if (customerExists(id)) {
            val customerToUpdate = getCustomer(id)!!
            customerToUpdate.fName = customer.fName
            customerToUpdate.lName = customer.lName
            customerToUpdate.email = customer.email
            customerToUpdate.dob = customer.dob
            customer.isAdult = false
            true
        } else {
            false
        }

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     * @return the deleted customer, or `null` if the customer is not found
     */
    fun deleteCustomer(id: Int) =
        if (customerExists(id)) {
            val customer = getCustomer(id)
            customers.remove(customer)
            customer
        } else {
            null
        }

    private fun filterCustomers(predicate: (Customer) -> (Boolean)) =
        customers
            .filter { predicate(it) }
            .map { it.toString() }
            .ifEmpty { null }

    /**
     * Lists all customers as strings.
     *
     * @return a list of strings representing customer details, or `null` if no customers are found
     */
    fun listAllCustomers() =
        filterCustomers { true }

    /**
     * Lists customers, with specified first name, as strings.
     *
     * @param fName the first name of the customers to search for
     * @return a list of customers details with the specified first name, or `null` if no customers are found
     */
    fun listAllCustomersByFirstName(fName: String) =
        filterCustomers { it.fName == fName }

    /**
     * Lists customers, with specified last name, as strings.
     *
     * @param lName the first name of the customers to search for
     * @return a list of customers details with the specified last name, or `null` if no customers are found
     */
    fun listAllCustomersByLastName(lName: String) =
        filterCustomers { it.lName == lName }

    /**
     * Lists customers, of specified age, as strings.
     *
     * @param age the age of the customers to search for
     * @return a list of strings representing customer details for customers with the specified age, or `null` if no customers are found
     */
    fun listAllCustomersByAge(age: Int) =
        filterCustomers { Utils.dateToYears(it.dob) == age }

    /**
     * Lists customers, within a specified age range, as strings.
     *
     * @param lowerAgeInclusive the lower bound of the age range (inclusive)
     * @param upperAgeInclusive the upper bound of the age range (inclusive)
     * @return a list of strings representing customer details for customers within the specified age range, or `null` if no customers are found
     */
    fun listAllCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        filterCustomers {
            Utils.dateToYears(it.dob) in lowerAgeInclusive..upperAgeInclusive
        }

    /**
     * Lists all adult customers as strings.
     *
     * @return a list of strings representing adult customer details, or `null` if no adult customers are found
     */
    fun listAllAdultCustomers() =
        filterCustomers { isAdult(it) }

    /**
     * Lists all child customers, customers under 18 years old, as strings.
     *
     * @return a list of strings representing child customer details, or `null` if no adult customers are found
     */
    fun listAllChildCustomers() =
        filterCustomers { !isAdult(it) }

    /**
     * Returns the total number of customers.
     *
     * @return the number of customers
     */
    fun numberOfCustomers() =
        customers.size

    /**
     * Returns the total number of adult customers.
     *
     * @return the number of adult customers
     */
    fun numberOfAdultCustomers() =
        listAllAdultCustomers()?.size ?: 0

    /**
     * Returns the total number of child customers, customers under 18 years old.
     *
     * @return the number of child customers
     */
    fun numberOfChildCustomers() =
        numberOfCustomers() - numberOfAdultCustomers()

    /**
     * Returns the number of customers of a specific age.
     *
     * @param age the age of the customers to count
     * @return the number of customers with the specified age
     */
    fun numberOfCustomersByAge(age: Int) =
        listAllCustomersByAge(age)?.size ?: 0

    /**
     * Returns the number of customers within a specific age range.
     *
     * @param lowerAgeInclusive the lower bound of the age range (inclusive)
     * @param upperAgeInclusive the upper bound of the age range (inclusive)
     * @return the number of customers within the specified age range
     */
    fun numberOfCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        listAllCustomersByAgeRange(lowerAgeInclusive, upperAgeInclusive)?.size ?: 0

    /**
     * Checks if there are any customers.
     *
     * @return `true` if there are customers, `false` otherwise
     */
    fun hasCustomers() =
        numberOfCustomers() > 0

    /**
     * Checks if there are any adult customers.
     *
     * @return `true` if there are adult customers, `false` otherwise
     */
    fun hasAdultCustomers() =
        numberOfAdultCustomers() > 0

    /**
     * Checks if there are any child customers, customers under 18 years old.
     *
     * @return `true` if there are adult customers, `false` otherwise
     */
    fun hasChildCustomers() =
        numberOfChildCustomers() > 0

    /**
     * Checks if there are any customers of a specific age.
     *
     * @param age the age of the customers to check for
     * @return `true` if there are customers of specified age, `false` otherwise
     */
    fun hasCustomersByAge(age: Int) =
        numberOfCustomersByAge(age) > 0

    /**
     * Checks if there are customers within a specified age range.
     *
     * @param lowerAgeInclusive the lower bound of the age range (inclusive)
     * @param upperAgeInclusive the upper bound of the age range (inclusive)
     * @return `true` if there are customers within the specified age range, `false` otherwise
     */
    fun hasCustomersByAgeRange(lowerAgeInclusive: Int, upperAgeInclusive: Int) =
        numberOfCustomersByAgeRange(lowerAgeInclusive, upperAgeInclusive) > 0

    private fun isAdult(customer: Customer) =
        if (customer.isAdult) {
            true
        } else {
            if (Period.between(customer.dob, LocalDate.now()).years >= 18) {
                customer.isAdult = true
            }
            customer.isAdult
        }

    /**
     * Checks if a customer with the given ID exists.
     *
     * @param id the ID of the customer to check
     * @return `true` if the customer exists, `false` otherwise
     */
    fun customerExists(id: Int) =
        customers.find { it.customerID == id } != null

    /**
     * Loads customer data from a file (specified by serializerType) into the API.
     *
     * @throws Exception if an error occurs during the loading process
     */
    @Throws(Exception::class)
    fun load() {
        @Suppress("UNCHECKED_CAST")
        customers = serializer.read() as ArrayList<Customer>
    }

    /**
     * Saves customer data to a file (specified by serializerType) into the API.
     *
     * @throws Exception if an error occurs during the saving process
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(customers)
    }
}

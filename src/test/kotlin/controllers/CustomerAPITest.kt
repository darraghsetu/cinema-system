package controllers

import models.Customer
import persistence.JSONSerializer
import persistence.XMLSerializer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

class CustomerAPITest {
    private var aoife: Customer? = null
    private var brendan: Customer? = null
    private var cillian: Customer? = null
    private var daithi: Customer? = null

    private var populatedCustomers: CustomerAPI? = null
    private var emptyCustomers: CustomerAPI? = null

    @BeforeEach
    fun setup() {
        populatedCustomers = CustomerAPI(XMLSerializer(File("CustomerAPITest.xml")))
        emptyCustomers = CustomerAPI(XMLSerializer(File("CustomerAPITest.xml")))

        populatedCustomers!!.addCustomer(
            Customer("Aoife", "Ayy", "aoife@gmail.com", LocalDate.now().minusYears(24))
        )
        populatedCustomers!!.addCustomer(
            Customer("Brendan", "Bee", "brendan@gmail.com", LocalDate.now().minusYears(18))
        )
        populatedCustomers!!.addCustomer(
            Customer("Cillian", "Cee", "cillian@gmail.com", LocalDate.now().minusYears(16))
        )

        aoife = populatedCustomers!!.getCustomer(1000)
        brendan = populatedCustomers!!.getCustomer(1001)
        cillian = populatedCustomers!!.getCustomer(1002)
        daithi = Customer("Daithi", "Dee", "daithi@gmail.com", LocalDate.now().minusYears(14))
    }

    @AfterEach
    fun tearDown() {
        aoife = null
        brendan = null
        cillian = null
        daithi = null
        populatedCustomers = null
        emptyCustomers = null
    }

    @Nested
    inner class AddCustomer {
        @Test
        fun `addCustomer returns true and adds Customer to an empty ArrayList`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertTrue(emptyCustomers!!.addCustomer(aoife!!))
            assertTrue(emptyCustomers!!.hasCustomers())
            assertEquals(1, emptyCustomers!!.numberOfCustomers())
            assertEquals(aoife, emptyCustomers!!.getCustomer(1000))
        }

        @Test
        fun `addCustomer returns true and adds Customer to a populated ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertTrue(populatedCustomers!!.addCustomer(daithi!!))
            assertEquals(4, populatedCustomers!!.numberOfCustomers())
            assertEquals(daithi!!.email, populatedCustomers!!.getCustomer(1003)!!.email)
        }
    }

    @Nested
    inner class GetCustomer {
        @Test
        fun `getCustomer returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.getCustomer(1000))
        }

        @Test
        fun `getCustomer returns null if the specified Customer ID does not exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertFalse(populatedCustomers!!.customerExists(9999))
            assertNull(populatedCustomers!!.getCustomer(9999))
        }

        @Test
        fun `getCustomer returns Customer if specified Customer ID exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertTrue(populatedCustomers!!.customerExists(1000))
            assertNotNull(populatedCustomers!!.getCustomer(1000))
            assertEquals(aoife!!, populatedCustomers!!.getCustomer(1000))
        }
    }

    @Nested
    inner class UpdateCustomer {
        @Test
        fun `updateCustomer returns false if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertFalse(emptyCustomers!!.updateCustomer(1000, daithi!!))
        }

        @Test
        fun `updateCustomer returns false if specified Customer ID does not exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertFalse(populatedCustomers!!.customerExists(9999))
            assertFalse(populatedCustomers!!.updateCustomer(9999, daithi!!))
        }

        @Test
        fun `updateCustomer returns true and updates the values, except ID, of specified Customer ID if it exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertTrue(populatedCustomers!!.customerExists(1000))

            val originalID = populatedCustomers!!.getCustomer(1000)!!.customerID
            val originalFName = populatedCustomers!!.getCustomer(1000)!!.fName
            val originalLName = populatedCustomers!!.getCustomer(1000)!!.lName
            val originalDOB = populatedCustomers!!.getCustomer(1000)!!.dob

            assertTrue(populatedCustomers!!.updateCustomer(1000, daithi!!))
            val updatedCustomer = populatedCustomers!!.getCustomer(1000)!!

            assertEquals(originalID, updatedCustomer.customerID)
            assertNotEquals(originalFName, updatedCustomer.fName)
            assertNotEquals(originalLName, updatedCustomer.lName)
            assertNotEquals(originalDOB, updatedCustomer.dob)
        }
    }

    @Nested
    inner class DeleteCustomer {
        @Test
        fun `deleteCustomer returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.deleteCustomer(1000))
        }

        @Test
        fun `deleteCustomer returns null if the specified Customer ID does not exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertFalse(populatedCustomers!!.customerExists(9999))
            assertNull(populatedCustomers!!.deleteCustomer(9999))
        }

        @Test
        fun `deleteCustomer deletes and returns Customer if specified Customer ID exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertTrue(populatedCustomers!!.customerExists(1000))
            assertNotNull(populatedCustomers!!.getCustomer(1000))

            val countBefore = populatedCustomers!!.numberOfCustomers()
            val deletedCustomer = populatedCustomers!!.deleteCustomer(1000)
            val countAfter = populatedCustomers!!.numberOfCustomers()

            assertEquals(countBefore - 1, countAfter)
            assertFalse(populatedCustomers!!.customerExists(1000))
            assertNull(populatedCustomers!!.getCustomer(1000))
            assertNotNull(deletedCustomer)
            assertEquals(aoife!!, deletedCustomer)
        }
    }

    @Nested
    inner class ListCustomers {
        @Test
        fun `listAllCustomers returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllCustomers())
        }

        @Test
        fun `listAllCustomers returns list of Customer strings if the ArrayList is not empty`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            val customersList = populatedCustomers!!.listAllCustomers()!!
            assertEquals(populatedCustomers!!.numberOfCustomers(), customersList.size)
            assertEquals(aoife!!.toString(), customersList[0])
            assertEquals(brendan!!.toString(), customersList[1])
            assertEquals(cillian!!.toString(), customersList[2])
        }

        @Test
        fun `listAllCustomersByFirstName returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllCustomersByFirstName("Aoife"))
        }

        @Test
        fun `listAllCustomersByFirstName returns null if no Customers with the specified first name exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertNull(populatedCustomers!!.listAllCustomersByFirstName("John"))
        }

        @Test
        fun `listAllCustomersByFirstName returns list of Customer strings if the specified first name exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            populatedCustomers!!.addCustomer(Customer("Aoife", "Zee", "aoife2@gmail.com", LocalDate.of(1970, 1, 1)))
            val aoifeList = populatedCustomers!!.listAllCustomersByFirstName("Aoife")!!
            assertNotNull(aoifeList)
            assertEquals(2, aoifeList.size)
            assertTrue(aoifeList[0].lowercase().contains("ayy"))
            assertTrue(aoifeList[1].lowercase().contains("zee"))
        }

        @Test
        fun `listAllCustomersByLastName returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllCustomersByLastName("Ayy"))
        }

        @Test
        fun `listAllCustomersByLastName returns null if no Customers with the specified last name exist in the ArrayList`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllCustomersByLastName("Emm"))
        }

        @Test
        fun `listAllCustomersByLastName returns list of Customer strings if the specified last name exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            populatedCustomers!!.addCustomer(Customer("John", "Ayy", "john@gmail.com", LocalDate.of(1970, 1, 1)))
            val ayyList = populatedCustomers!!.listAllCustomersByLastName("Ayy")!!
            assertNotNull(ayyList)
            assertEquals(2, ayyList.size)
            assertTrue(ayyList[0].lowercase().contains("aoife"))
            assertTrue(ayyList[1].lowercase().contains("john"))
        }

        @Test
        fun `listAllCustomersByAge returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllCustomersByAge(18))
        }

        @Test
        fun `listAllCustomersByAge returns null if no Customers of the specified age exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertNull(populatedCustomers!!.listAllCustomersByAge(100))
        }

        @Test
        fun `listAllCustomersByAge returns list of Customer strings if Customers of the specified age exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            val daithi2 = daithi!!.copy(dob=LocalDate.of(2006, 1, 1))
            populatedCustomers!!.addCustomer(daithi2)
            val eighteensList = populatedCustomers!!.listAllCustomersByAge(18)!!
            assertNotNull(eighteensList)
            assertEquals(populatedCustomers!!.numberOfCustomersByAge(18), eighteensList.size)
            assertEquals(brendan!!.toString(), eighteensList[0])
            assertTrue(eighteensList[1].contains(daithi2.email))
        }

        @Test
        fun `listAllCustomersByAgeRange returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllCustomersByAgeRange(0, 17))
        }

        @Test
        fun `listAllCustomersByAgeRange returns null if no Customers in the specified age range exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertNull(populatedCustomers!!.listAllCustomersByAgeRange(90, 100))
        }

        @Test
        fun `listAllCustomersByAgeRange returns list of Customer strings if Customers in the specified age range exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertEquals(1, populatedCustomers!!.numberOfChildCustomers())
            val childList = populatedCustomers!!.listAllCustomersByAgeRange(0, 17)!!
            assertNotNull(childList)
            assertEquals(populatedCustomers!!.numberOfChildCustomers(), childList.size)
            assertEquals(cillian!!.toString(), childList[0])
        }

        @Test
        fun `listAllAdultCustomers returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllAdultCustomers())
        }

        @Test
        fun `listAllAdultCustomers returns null if no adult Customers (18 or over) exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertTrue(populatedCustomers!!.hasAdultCustomers())
            populatedCustomers!!.deleteCustomer(1000)
            populatedCustomers!!.deleteCustomer(1001)
            assertTrue(populatedCustomers!!.hasCustomers())
            assertFalse(populatedCustomers!!.hasAdultCustomers())
            assertNull(populatedCustomers!!.listAllAdultCustomers())
        }

        @Test
        fun `listAllAdultCustomers returns list of Customer strings if adult Customers exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertEquals(2, populatedCustomers!!.numberOfAdultCustomers())
            val adultList = populatedCustomers!!.listAllAdultCustomers()!!
            assertNotNull(adultList)
            assertEquals(populatedCustomers!!.numberOfAdultCustomers(), adultList.size)
            assertEquals(aoife!!.toString(), adultList[0])
            assertEquals(brendan!!.toString(), adultList[1])
        }

        @Test
        fun `listAllChildCustomers returns null if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertNull(emptyCustomers!!.listAllChildCustomers())
        }

        @Test
        fun `listAllChildCustomers returns null if no child Customers (Under 18) exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertTrue(populatedCustomers!!.hasChildCustomers())
            populatedCustomers!!.deleteCustomer(1002)
            assertFalse(populatedCustomers!!.hasChildCustomers())
            assertNull(populatedCustomers!!.listAllChildCustomers())
        }

        @Test
        fun `listAllChildCustomers returns list of Customer strings if child Customers exist in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertEquals(1, populatedCustomers!!.numberOfChildCustomers())
            val childList = populatedCustomers!!.listAllChildCustomers()!!
            assertNotNull(childList)
            assertEquals(populatedCustomers!!.numberOfChildCustomers(), childList.size)
            assertEquals(cillian!!.toString(), childList[0])
        }
    }

    @Nested
    inner class CountingCustomers {
        @Test
        fun numberOfCustomersCalculatedCorrectly() {
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
            assertEquals(3, populatedCustomers!!.numberOfCustomers())

        }

        @Test
        fun numberOfAdultCustomersCalculatedCorrectly() {
            assertEquals(0, emptyCustomers!!.numberOfAdultCustomers())
            assertEquals(2, populatedCustomers!!.numberOfAdultCustomers())
        }

        @Test
        fun numberOfChildCustomersCalculatedCorrectly() {
            assertEquals(0, emptyCustomers!!.numberOfChildCustomers())
            assertEquals(1, populatedCustomers!!.numberOfChildCustomers())
        }

        @Test
        fun numberOfCustomersByAgeCalculatedCorrectly() {
            assertEquals(0, emptyCustomers!!.numberOfCustomersByAge(18))
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAge(16))
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAge(18))
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAge(24))
        }

        @Test
        fun numberOfCustomersByAgeRangeCalculatedCorrectly() {
            assertEquals(0, emptyCustomers!!.numberOfCustomersByAgeRange(18, 100))
            assertEquals(2, populatedCustomers!!.numberOfCustomersByAgeRange(18, 100))
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAgeRange(0, 17))
        }
    }

    @Nested
    inner class BooleanQueries {

        @Test
        fun `hasCustomers returns false if the ArrayList is empty`() {
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
            assertFalse(emptyCustomers!!.hasCustomers())
        }

        @Test
        fun `hasCustomers returns true if the ArrayList is not empty`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertTrue(populatedCustomers!!.hasCustomers())
        }

        @Test
        fun `hasAdultCustomers returns false if the ArrayList is empty`() {
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
            assertEquals(0, emptyCustomers!!.numberOfAdultCustomers())
            assertFalse(emptyCustomers!!.hasAdultCustomers())
        }

        @Test
        fun `hasAdultCustomers returns false if no adult Customers exist in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(2, populatedCustomers!!.numberOfAdultCustomers())
            populatedCustomers!!.deleteCustomer(1000)
            populatedCustomers!!.deleteCustomer(1001)
            assertEquals(0, populatedCustomers!!.numberOfAdultCustomers())
            assertFalse(populatedCustomers!!.hasAdultCustomers())
        }

        @Test
        fun `hasAdultCustomers returns true if there are adult Customers in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(2, populatedCustomers!!.numberOfAdultCustomers())
            assertTrue(populatedCustomers!!.hasAdultCustomers())
        }

        @Test
        fun `hasChildCustomers returns false if the ArrayList is empty`() {
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
            assertEquals(0, emptyCustomers!!.numberOfChildCustomers())
            assertFalse(emptyCustomers!!.hasChildCustomers())
        }

        @Test
        fun `hasChildCustomers returns false if there are no child Customers in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(1, populatedCustomers!!.numberOfChildCustomers())
            populatedCustomers!!.deleteCustomer(1002)
            assertEquals(0, populatedCustomers!!.numberOfChildCustomers())
            assertFalse(populatedCustomers!!.hasChildCustomers())
        }

        @Test
        fun `hasChildCustomers returns true if there are child Customers in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(1, populatedCustomers!!.numberOfChildCustomers())
            assertTrue(populatedCustomers!!.hasChildCustomers())
        }

        @Test
        fun `hasCustomersByAge returns false if the ArrayList is empty`() {
            assertEquals(0, emptyCustomers!!.numberOfCustomers())
            assertEquals(0, emptyCustomers!!.numberOfCustomersByAge(18))
            assertFalse(emptyCustomers!!.hasCustomersByAge(18))
        }

        @Test
        fun `hasCustomersByAge returns false if no Customers of the specified age exist in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAge(18))
            populatedCustomers!!.deleteCustomer(1001)
            assertEquals(0, populatedCustomers!!.numberOfCustomersByAge(18))
            assertFalse(populatedCustomers!!.hasCustomersByAge(18))
        }

        @Test
        fun `hasCustomersByAge returns true if there are Customers of the specified age in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAge(18))
            assertTrue(populatedCustomers!!.hasCustomersByAge(18))
        }

        @Test
        fun `hasCustomersByAgeRange returns false if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertEquals(0, emptyCustomers!!.numberOfCustomersByAgeRange(0, 100))
            assertFalse(emptyCustomers!!.hasCustomersByAgeRange(0, 100))
        }

        @Test
        fun `hasCustomersByAgeRange returns false if no Customers in the specified age range exist in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(1, populatedCustomers!!.numberOfCustomersByAgeRange(0, 16))
            populatedCustomers!!.deleteCustomer(1002)
            assertEquals(0, populatedCustomers!!.numberOfCustomersByAgeRange(0, 16))
            assertFalse(populatedCustomers!!.hasCustomersByAgeRange(0, 16))
        }

        @Test
        fun `hasCustomersByAgeRange returns true if Customers in the specified age range exist in the ArrayList`() {
            assertEquals(3, populatedCustomers!!.numberOfCustomers())
            assertEquals(2, populatedCustomers!!.numberOfCustomersByAgeRange(18, 100))
            assertTrue(populatedCustomers!!.hasCustomersByAgeRange(18, 100))
        }

        @Test
        fun `customerExists returns false if the ArrayList is empty`() {
            assertFalse(emptyCustomers!!.hasCustomers())
            assertFalse(emptyCustomers!!.customerExists(1000))
        }

        @Test
        fun `customerExists returns false if no Customer with the specified ID exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertFalse(populatedCustomers!!.customerExists(9999))
        }

        @Test
        fun `customerExists returns true if specified Customer ID exists in the ArrayList`() {
            assertTrue(populatedCustomers!!.hasCustomers())
            assertTrue(populatedCustomers!!.customerExists(1000))
            assertTrue(populatedCustomers!!.customerExists(1001))
            assertTrue(populatedCustomers!!.customerExists(1002))
            assertFalse(populatedCustomers!!.customerExists(1003))
            populatedCustomers!!.addCustomer(daithi!!)
            assertTrue(populatedCustomers!!.customerExists(1003))
        }
    }

    @Nested
    inner class PersistenceTests {
        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty customersTest.xml file.
            val storingCustomers = CustomerAPI(XMLSerializer(File("customersTest.xml")))
            storingCustomers.store()

            //Loading the empty customersTest.xml file into a new object
            val loadedCustomers = CustomerAPI(XMLSerializer(File("customersTest.xml")))
            loadedCustomers.load()

            //Comparing the source of the notes (storingCustomers) with the XML loaded notes (loadedCustomers)
            assertEquals(0, storingCustomers.numberOfCustomers())
            assertEquals(0, loadedCustomers.numberOfCustomers())
            assertEquals(storingCustomers.numberOfCustomers(), loadedCustomers.numberOfCustomers())
        }

        @Test
        fun `saving and loading a loaded collection in XML doesn't lose data`() {
            // Storing 3 customers to the customersTest.xml file.
            val storingCustomers = CustomerAPI(XMLSerializer(File("customersTest.xml")))
            storingCustomers.addCustomer(aoife!!)
            storingCustomers.addCustomer(brendan!!)
            storingCustomers.addCustomer(cillian!!)
            storingCustomers.store()

            //Loading customersTest.xml into a different collection
            val loadedCustomers = CustomerAPI(XMLSerializer(File("customersTest.xml")))
            loadedCustomers.load()

            //Comparing the source of the customers (storingCustomers) with the XML loaded customers (loadedCustomers)
            assertEquals(3, storingCustomers.numberOfCustomers())
            assertEquals(3, loadedCustomers.numberOfCustomers())
            assertEquals(storingCustomers.numberOfCustomers(), loadedCustomers.numberOfCustomers())
            assertEquals(storingCustomers.getCustomer(1000), loadedCustomers.getCustomer(1000))
            assertEquals(storingCustomers.getCustomer(1001), loadedCustomers.getCustomer(1001))
            assertEquals(storingCustomers.getCustomer(1002), loadedCustomers.getCustomer(1002))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty customersTest.json file.
            val storingCustomers = CustomerAPI(JSONSerializer(File("customersTest.json")))
            storingCustomers.store()

            //Loading the empty customersTest.json file into a new object
            val loadedCustomers = CustomerAPI(JSONSerializer(File("customersTest.json")))
            loadedCustomers.load()

            //Comparing the source of the customers (storingCustomers) with the json loaded customers (loadedCustomers)
            assertEquals(0, storingCustomers.numberOfCustomers())
            assertEquals(0, loadedCustomers.numberOfCustomers())
            assertEquals(storingCustomers.numberOfCustomers(), loadedCustomers.numberOfCustomers())
        }

        @Test
        fun `saving and loading a loaded collection in JSON doesn't lose data`() {
            // Storing 3 customers to the customersTest.json file.
            val storingCustomers = CustomerAPI(JSONSerializer(File("customersTest.json")))
            storingCustomers.addCustomer(aoife!!)
            storingCustomers.addCustomer(brendan!!)
            storingCustomers.addCustomer(cillian!!)
            storingCustomers.store()

            //Loading customersTest.json into a different collection
            val loadedCustomers = CustomerAPI(JSONSerializer(File("customersTest.json")))
            loadedCustomers.load()

            //Comparing the source of the customers (storingCustomers) with the json loaded customers (loadedCustomers)
            assertEquals(3, storingCustomers.numberOfCustomers())
            assertEquals(3, loadedCustomers.numberOfCustomers())
            assertEquals(storingCustomers.numberOfCustomers(), loadedCustomers.numberOfCustomers())
            assertEquals(storingCustomers.getCustomer(0), loadedCustomers.getCustomer(0))
            assertEquals(storingCustomers.getCustomer(1), loadedCustomers.getCustomer(1))
            assertEquals(storingCustomers.getCustomer(2), loadedCustomers.getCustomer(2))
        }
    }

}
package controllers

import models.Customer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested

class CustomerAPITest {
    private var aoife: Customer? = null
    private var brendan: Customer? = null
    private var cillian: Customer? = null
    private var daithi: Customer? = null

    private var littleAoife: Customer? = null
    private var littleBrendan: Customer? = null
    private var littleCillian: Customer? = null
    private var littleDaithi: Customer? = null

    private var populatedCustomers: CustomerAPI? = CustomerAPI()
    private var customerAPI: CustomerAPI? = CustomerAPI()

    @BeforeEach
    fun setup() {

    }

    @AfterEach
    fun tearDown() {

    }

    @Nested
    inner class AddCustomer {

    }

    @Nested
    inner class GetCustomer {

    }

    @Nested
    inner class UpdateCustomer {

    }

    @Nested
    inner class DeleteCustomer {

    }

    @Nested
    inner class ListCustomers {

    }

    @Nested
    inner class CountingCustomers {

    }

    @Nested
    inner class BooleanQueries {

    }

    @Nested
    inner class Miscellaneous {

    }
}
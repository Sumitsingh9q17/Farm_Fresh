package com.sumit.farm_fresh

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FarmFresh.db"
        private const val DATABASE_VERSION = 4

        // Admin Table
        private const val TABLE_ADMIN = "Admin"
        private const val COL_ADMIN_ID = "id"
        private const val COL_ADMIN_EMAIL = "email"
        private const val COL_ADMIN_PASSWORD = "password"

        // Users Table
        const val TABLE_USERS = "Users"
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "name"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PASSWORD = "password"

        // Products Table
        private const val TABLE_PRODUCTS = "Products"
        private const val COL_PRODUCT_ID = "id"
        private const val COL_PRODUCT_NAME = "name"
        private const val COL_PRODUCT_PRICE = "price"
        private const val COL_PRODUCT_TYPE = "type"
        private const val COL_PRODUCT_IMAGE = "image"
        private const val COL_PRODUCT_CATEGORY = "category"

        private const val TABLE_ORDER_HISTORY = "OrderHistory"
        private const val COL_ORDER_ID = "id"
        private const val COL_ORDER_USER_EMAIL = "user_email"
        private const val COL_ORDER_DATE = "date"
        private const val COL_ORDER_STATUS = "status"
        private const val COL_ORDER_AMOUNT = "amount"
        private const val COL_ORDER_IMAGE = "image"


        private const val TABLE_PAYMENT = "payment"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_CITY = "city"
        private const val COLUMN_POSTAL_CODE = "postal_code"
        private const val COLUMN_TOTAL_PRICE = "total_price"
        private const val COLUMN_PAYMENT_METHOD = "payment_method"
        private const val COLUMN_ORDER_DATE = "order_date"

        // Saved Addresses Table
        private const val TABLE_ADDRESSES = "SavedAddresses"
        private const val COL_ADDRESS_ID = "id"
        private const val COL_ADDRESS_USER_EMAIL = "user_email"
        private const val COL_ADDRESS_TYPE = "type" // Billing or Shipping
        private const val COL_ADDRESS_LINE1 = "line1"
        private const val COL_ADDRESS_LINE2 = "line2"
        private const val COL_ADDRESS_CITY = "city"
        private const val COL_ADDRESS_STATE = "state"
        private const val COL_ADDRESS_ZIP = "zip"

        // Payment Methods Table
        private const val TABLE_PAYMENT_METHODS = "PaymentMethods"
        private const val COL_PAYMENT_ID = "id"
        private const val COL_PAYMENT_USER_EMAIL = "user_email"
        private const val COL_PAYMENT_TYPE = "type" // Credit/Debit/UPI
        private const val COL_PAYMENT_DETAILS = "details"

    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Admin Table
        val createAdminTable = """
            CREATE TABLE $TABLE_ADMIN (
                $COL_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ADMIN_EMAIL TEXT NOT NULL,
                $COL_ADMIN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createAdminTable)

        // Insert Default Admin Credentials
        val defaultAdmin = ContentValues().apply {
            put(COL_ADMIN_EMAIL, "admin@farmfresh.com") // Default admin email
            put(COL_ADMIN_PASSWORD, "admin123") // Default admin password
        }
        db.insert(TABLE_ADMIN, null, defaultAdmin)

        // Create Users Table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_NAME TEXT NOT NULL,
                $COL_USER_EMAIL TEXT NOT NULL,
                $COL_USER_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUsersTable)

        // Create Products Table
        val createProductsTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COL_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PRODUCT_NAME TEXT NOT NULL,
                $COL_PRODUCT_PRICE REAL NOT NULL,
                $COL_PRODUCT_TYPE TEXT NOT NULL,
                $COL_PRODUCT_IMAGE TEXT NOT NULL,
                $COL_PRODUCT_CATEGORY TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createProductsTable)

        val createOrderHistoryTable = """
            CREATE TABLE $TABLE_ORDER_HISTORY (
                $COL_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ORDER_USER_EMAIL TEXT NOT NULL,
                $COL_ORDER_DATE TEXT NOT NULL,
                $COL_ORDER_STATUS TEXT NOT NULL,
                $COL_ORDER_AMOUNT REAL NOT NULL,
                $COL_ORDER_IMAGE TEXT NOT NULL
                
            )
        """.trimIndent()
        db.execSQL(createOrderHistoryTable)

        // Create Saved Addresses Table
        val createAddressesTable = """
            CREATE TABLE $TABLE_ADDRESSES (
                $COL_ADDRESS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ADDRESS_USER_EMAIL TEXT NOT NULL,
                $COL_ADDRESS_TYPE TEXT NOT NULL,
                $COL_ADDRESS_LINE1 TEXT NOT NULL,
                $COL_ADDRESS_LINE2 TEXT,
                $COL_ADDRESS_CITY TEXT NOT NULL,
                $COL_ADDRESS_STATE TEXT NOT NULL,
                $COL_ADDRESS_ZIP TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createAddressesTable)

        // Create Payment Methods Table
        val createPaymentMethodsTable = """
            CREATE TABLE $TABLE_PAYMENT_METHODS (
                $COL_PAYMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PAYMENT_USER_EMAIL TEXT NOT NULL,
                $COL_PAYMENT_TYPE TEXT NOT NULL,
                $COL_PAYMENT_DETAILS TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createPaymentMethodsTable)

        val CREATE_PAYMENT_TABLE = "CREATE TABLE $TABLE_PAYMENT (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_PHONE TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_CITY TEXT, " +
                "$COLUMN_POSTAL_CODE TEXT, " +
                "$COLUMN_TOTAL_PRICE REAL, " +
                "$COLUMN_PAYMENT_METHOD TEXT, " +
                "$COLUMN_ORDER_DATE INTEGER)"
        db?.execSQL(CREATE_PAYMENT_TABLE)
    }


    fun isValidUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COL_USER_EMAIL = ? AND $COL_USER_PASSWORD = ?",
            arrayOf(email, password)
        )
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }


    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COL_USER_EMAIL = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ADD NEW USER
    fun addUser(name: String, email: String, password: String): Long {
        if (isEmailExists(email)) {
            return -1 // Indicate user already exists
        }
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_NAME, name)
            put(COL_USER_EMAIL, email)
            put(COL_USER_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return result
    }

    // UPDATE USER
    fun updateUser(email: String, newName: String, newEmail: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_NAME, newName)
            put(COL_USER_EMAIL, newEmail)
        }

        // Update user by email
        return db.update(TABLE_USERS, values, "$COL_USER_EMAIL = ?", arrayOf(email))
    }

    fun insertPayment(payment: Payment): Long {
        val db = writableDatabase

        val values = ContentValues()
        values.put(COLUMN_NAME, payment.name)
        values.put(COLUMN_ADDRESS, payment.address)
        values.put(COLUMN_PHONE, payment.phone)
        values.put(COLUMN_EMAIL, payment.email)
        values.put(COLUMN_CITY, payment.city)
        values.put(COLUMN_POSTAL_CODE, payment.postalCode)
        values.put(COLUMN_TOTAL_PRICE, payment.totalPrice)
        values.put(COLUMN_PAYMENT_METHOD, payment.paymentMethod)
        values.put(COLUMN_ORDER_DATE, payment.orderDate)

        // Insert the payment into the database and return the row ID
        return db.insert(TABLE_PAYMENT, null, values)
    }

    fun getProductsByCategory(category: String): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PRODUCTS WHERE $COL_PRODUCT_CATEGORY = ?",
            arrayOf(category)
        )
        cursor?.use { // Automatically closes the cursor after use
            while (it.moveToNext()) {
                val product = Product(
                    id = it.getInt(it.getColumnIndexOrThrow(COL_PRODUCT_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COL_PRODUCT_NAME)),
                    price = it.getFloat(it.getColumnIndexOrThrow(COL_PRODUCT_PRICE)),
                    type = it.getString(it.getColumnIndexOrThrow(COL_PRODUCT_TYPE)),
                    category = it.getString(it.getColumnIndexOrThrow(COL_PRODUCT_CATEGORY)),
                    imageUri = it.getString(it.getColumnIndexOrThrow(COL_PRODUCT_IMAGE))
                )
                productList.add(product)
            }
        }
        return productList
    }


    fun getUserByEmail(email: String): Cursor? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USER_EMAIL = ?"

        // Use the email string properly, ensure no leading/trailing spaces or case issues
        val cleanedEmail = email.trim().lowercase()

        // Execute the query with the cleaned email value
        return db.rawQuery(query, arrayOf(cleanedEmail))
    }


    fun getOrderHistory(userEmail: String): List<OrderHistory> {
        val orderList = mutableListOf<OrderHistory>()
        val db = readableDatabase
        val query = """
        SELECT 
            o.$COL_ORDER_ID, 
            o.$COL_ORDER_DATE, 
            o.$COL_ORDER_STATUS, 
            o.$COL_ORDER_AMOUNT, 
            p.$COL_PRODUCT_NAME, 
            p.$COL_PRODUCT_PRICE, 
            o.$COL_ORDER_IMAGE 
        FROM $TABLE_ORDER_HISTORY o
        JOIN $TABLE_PRODUCTS p ON o.$COL_ORDER_USER_EMAIL = ?
    """
        val cursor = db.rawQuery(query, arrayOf(userEmail))

        cursor?.use {
            while (it.moveToNext()) {
                val order = OrderHistory(
                    it.getInt(it.getColumnIndexOrThrow(COL_ORDER_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_ORDER_DATE)),
                    it.getString(it.getColumnIndexOrThrow(COL_ORDER_STATUS)),
                    it.getFloat(it.getColumnIndexOrThrow(COL_ORDER_AMOUNT)),
                    it.getString(it.getColumnIndexOrThrow(COL_PRODUCT_NAME)),
                    it.getFloat(it.getColumnIndexOrThrow(COL_PRODUCT_PRICE)),
                    1, // Assuming quantity is always 1 for now, you can update based on your order data
                    it.getString(it.getColumnIndexOrThrow(COL_ORDER_IMAGE))
                )
                orderList.add(order)
            }
        }
        return orderList
    }


    // ADD SAVED ADDRESS
    fun addSavedAddress(
        userEmail: String,
        type: String,
        line1: String,
        line2: String?,
        city: String,
        state: String,
        zip: String
    ): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_ADDRESS_USER_EMAIL, userEmail)
            put(COL_ADDRESS_TYPE, type)
            put(COL_ADDRESS_LINE1, line1)
            put(COL_ADDRESS_LINE2, line2)
            put(COL_ADDRESS_CITY, city)
            put(COL_ADDRESS_STATE, state)
            put(COL_ADDRESS_ZIP, zip)
        }
        val result = db.insert(TABLE_ADDRESSES, null, contentValues)
        db.close()
        return result
    }

    // ADD PAYMENT METHOD
    fun addPaymentMethod(userEmail: String, type: String, details: String): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_PAYMENT_USER_EMAIL, userEmail)
            put(COL_PAYMENT_TYPE, type)
            put(COL_PAYMENT_DETAILS, details)
        }
        val result = db.insert(TABLE_PAYMENT_METHODS, null, contentValues)
        db.close()
        return result
    }

    // Get Saved Addresses for a user
    fun getSavedAddresses(userEmail: String): List<SavedAddress> {
        val addressList = mutableListOf<SavedAddress>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_ADDRESSES WHERE $COL_ADDRESS_USER_EMAIL = ?",
            arrayOf(userEmail)
        )
        cursor?.use {
            while (it.moveToNext()) {
                val address = SavedAddress(
                    it.getInt(it.getColumnIndexOrThrow(COL_ADDRESS_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_ADDRESS_TYPE)),
                    it.getString(it.getColumnIndexOrThrow(COL_ADDRESS_LINE1)),
                    it.getString(it.getColumnIndexOrThrow(COL_ADDRESS_LINE2)),
                    it.getString(it.getColumnIndexOrThrow(COL_ADDRESS_CITY)),
                    it.getString(it.getColumnIndexOrThrow(COL_ADDRESS_STATE)),
                    it.getString(it.getColumnIndexOrThrow(COL_ADDRESS_ZIP))
                )
                addressList.add(address)
            }
        }
        return addressList
    }

    // Get Payment Methods for a user
    fun getPaymentMethods(userEmail: String): List<PaymentMethod> {
        val paymentList = mutableListOf<PaymentMethod>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PAYMENT_METHODS WHERE $COL_PAYMENT_USER_EMAIL = ?",
            arrayOf(userEmail)
        )
        cursor?.use {
            while (it.moveToNext()) {
                val paymentMethod = PaymentMethod(
                    it.getInt(it.getColumnIndexOrThrow(COL_PAYMENT_ID)),
                    it.getString(it.getColumnIndexOrThrow(COL_PAYMENT_TYPE)),
                    it.getString(it.getColumnIndexOrThrow(COL_PAYMENT_DETAILS))
                )
                paymentList.add(paymentMethod)
            }
        }
        return paymentList
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }
}
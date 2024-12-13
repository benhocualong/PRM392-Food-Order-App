package com.harusora.longcn.apporderfood.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.harusora.longcn.apporderfood.database.table.CartTable;
import com.harusora.longcn.apporderfood.database.table.CommentTable;
import com.harusora.longcn.apporderfood.database.table.OrderTable;
import com.harusora.longcn.apporderfood.database.table.ProductTable;
import com.harusora.longcn.apporderfood.database.table.UserTable;
import com.harusora.longcn.apporderfood.model.Cart;
import com.harusora.longcn.apporderfood.model.Order;
import com.harusora.longcn.apporderfood.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_order_food.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductTable.TB_NAME + "("
                + ProductTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductTable.COLUMN_NAME + " TEXT,"
                + ProductTable.COLUMN_DESCRIPTION + " TEXT,"
                + ProductTable.COLUMN_PRICE + " REAL,"
                + ProductTable.COLUMN_IMAGE + " TEXT,"
                + ProductTable.COLUMN_CATEGORY_ID + " INTEGER" + ")";
        String CREATE_USERS_TABLE = "CREATE TABLE " + UserTable.TB_NAME + "("
                + UserTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserTable.COLUMN_USERNAME + " TEXT,"
                + UserTable.COLUMN_PASSWORD + " TEXT,"
                + UserTable.COLUMN_FULL_NAME + " TEXT,"
                + UserTable.COLUMN_ADDRESS + " TEXT,"
                + UserTable.COLUMN_GENDER + " TEXT,"
                + UserTable.COLUMN_PHONE_NUMBER + " TEXT" + ")";
        String CREATE_CART_TABLE = "CREATE TABLE " + CartTable.TB_NAME + "("
                + CartTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CartTable.COLUMN_USER_ID + " TEXT,"
                + CartTable.COLUMN_QUANTITY + " INTEGER,"
                + CartTable.COLUMN_PRODUCT_ID + " TEXT,"
                + CartTable.COLUMN_PRODUCT_NAME + " TEXT,"
                + CartTable.COLUMN_PRODUCT_PRICE + " REAL,"
                + CartTable.COLUMN_PRODUCT_IMAGE + " TEXT" + ")";
        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + CommentTable.TB_NAME + "("
                + CommentTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CommentTable.COLUMN_USERNAME + " TEXT,"
                + CommentTable.COLUMN_DETAIL + " TEXT,"
                + CommentTable.COLUMN_PRODUCT_ID + " TEXT,"
                + CommentTable.COLUMN_DATE + " TEXT" + ")";
        String CREATE_ORDER_TABLE = "CREATE TABLE " + OrderTable.TB_NAME + "(" +
                OrderTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrderTable.COLUMN_ORDER_CODE + " TEXT," +
                OrderTable.COLUMN_USER_ID + " INTEGER," +
                OrderTable.COLUMN_TOTAL_AMOUNT + " REAL," +
                OrderTable.COLUMN_STATUS + " TEXT," +
                OrderTable.COLUMN_PAYMENT_TYPE + " TEXT," +
                OrderTable.COLUMN_CART_LIST + " TEXT," +
                OrderTable.COLUMN_ADDRESS + " TEXT," +
                OrderTable.COLUMN_NOTE + " TEXT," +
                OrderTable.COLUMN_ORDER_DATE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM products ORDER BY id DESC", null);
    }

    public Cursor getAllOrdersByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders where user_id = ? and status = 1 ORDER BY order_date DESC", new String[]{String.valueOf(userId)});
    }

    public Cursor getAllCartByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select c.id, p.id as product_id, p.price as product_price, c.user_id, p.name as product_name, c.quantity, p.image as product_image from cart c inner join main.products p on p.id = c.product_id where c.user_id = ?", new String[]{String.valueOf(userId)}, null);
    }

    public Cursor getAllCartByUserIdAndProductId(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select c.id, p.id as product_id, p.price as product_price, c.user_id, p.name as product_name, c.quantity, p.image as product_image from cart c inner join main.products p on p.id = c.product_id where c.user_id = ? and p.id=?", new String[]{String.valueOf(userId), String.valueOf(productId)}, null);
    }



    public Cursor getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM products where id = ?", new String[]{String.valueOf(productId)});
    }

    public Cursor getCommentByProductId(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM comment where product_id = ?", new String[]{String.valueOf(productId)});
    }

    public void insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO products(name, description, price, image, category) VALUES(?, ?, ?, ?, ?)",
                new Object[]{product.getName(), product.getDescription(), product.getPrice(), product.getImage(), product.getCategoryId()});
        db.close();
    }

    public void updateCart(int id, int productId, int quantity, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE cart SET quantity=? WHERE id=? and product_id=? and user_id=?",
                new Object[]{quantity, id, productId, userId});
        db.close();
    }

    public Cursor getBySearch(String searchTerm) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM products WHERE name LIKE ?",new String[]{searchTerm}, null);
    }

    public void deleteProduct(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM products WHERE id=?", new Object[]{id});
        db.close();
    }


    public Cursor checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from user where username = ? and password = ?", new String[]{username, password});
    }

    public void insertCart(int userId, int quantity, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into cart(user_id, quantity, product_id) values (?,?,?)",
                new Object[]{userId, quantity, productId});
        db.close();
    }

    public void deleteCartByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE user_id=?", new Object[]{userId});
        db.close();
    }

    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_code", order.getOrderCode());
        values.put("user_id", order.getUserId());
        values.put("total_amount", order.getTotalAmount());
        values.put("status", order.getStatus());
        values.put("payment_type", order.getPaymentType());
        values.put("address", order.getAddress());
        values.put("note", order.getNote());
        values.put("order_date", order.getOrderDate());

        ByteArrayOutputStream bass = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bass);
            oos.writeObject(order.getCartList());
            byte[] bytes = bass.toByteArray();
            values.put("cart", bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long id = db.insert("orders", null, values);
        db.close();
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from user where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean insertData(String username, String password, String phone) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("phone_number", phone);
        long result = MyDatabase.insert("user", null, contentValues);
        return result != -1;
    }
}


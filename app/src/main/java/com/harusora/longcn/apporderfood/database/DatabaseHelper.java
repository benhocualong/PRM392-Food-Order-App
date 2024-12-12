package com.harusora.longcn.apporderfood.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        String schemaSQL =
                "CREATE TABLE IF NOT EXISTS category (\n" +
                        "    id   INTEGER\n" +
                        "        constraint category_pk\n" +
                        "            primary key autoincrement,\n" +
                        "    name TEXT\n" +
                        ");\n\n" +

                        "CREATE TABLE IF NOT EXISTS \"order\" (\n" +
                        "    id           integer\n" +
                        "        constraint order_pk\n" +
                        "            primary key autoincrement,\n" +
                        "    order_code   text,\n" +
                        "    user_id      int,\n" +
                        "    total_amount real,\n" +
                        "    status       integer,\n" +
                        "    payment_type int,\n" +
                        "    cart         blob,\n" +
                        "    address      text,\n" +
                        "    note         text\n" +
                        ");\n\n" +

                        "CREATE TABLE IF NOT EXISTS products (\n" +
                        "    id          INTEGER\n" +
                        "        primary key autoincrement,\n" +
                        "    name        TEXT,\n" +
                        "    description TEXT,\n" +
                        "    price       REAL,\n" +
                        "    image       TEXT,\n" +
                        "    category_id INT\n" +
                        "        references category\n" +
                        ");\n\n" +

                        "ALTER TABLE IF NOT EXISTS category\n" +
                        "    ADD CONSTRAINT IF NOT EXISTS category_products_category_id_fk\n" +
                        "        FOREIGN KEY (id)\n" +
                        "        REFERENCES products (category_id);\n\n" +

                        "CREATE TABLE IF NOT EXISTS comment (\n" +
                        "    id         integer\n" +
                        "        constraint comment_pk\n" +
                        "            primary key autoincrement,\n" +
                        "    username   text,\n" +
                        "    detail     text,\n" +
                        "    product_id integer\n" +
                        "        constraint comment_products_id_fk\n" +
                        "        REFERENCES products,\n" +
                        "    date       TEXT\n" +
                        ");\n\n" +

                        "CREATE TABLE IF NOT EXISTS user (\n" +
                        "    id           integer\n" +
                        "        constraint id\n" +
                        "            primary key autoincrement,\n" +
                        "    username     text,\n" +
                        "    password     text,\n" +
                        "    full_name    text,\n" +
                        "    address      text,\n" +
                        "    gender       text,\n" +
                        "    phone_number string\n" +
                        ");\n\n" +

                        "CREATE TABLE IF NOT EXISTS cart (\n" +
                        "    id         INTEGER\n" +
                        "        constraint cart_pk\n" +
                        "            primary key autoincrement,\n" +
                        "    user_id    INT\n" +
                        "        references user,\n" +
                        "    quantity   INT,\n" +
                        "    product_id INT\n" +
                        "        references products\n" +
                        ");";

        db.execSQL(schemaSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM products ORDER BY id DESC", null);
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

        long id = db.insert("order", null, values);
        db.close();
    }
}


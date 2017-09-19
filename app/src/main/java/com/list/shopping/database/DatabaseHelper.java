package com.list.shopping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathieu on 15/09/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // TAG
    private static final String TAG = DatabaseHelper.class.getName();

    // Database Info
    private static final String DATABASE_NAME = "DB_SHOP";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USER = "user";
    private static final String TABLE_ITEM = "item";
    private static final String TABLE_GROCERY = "grocery";

    // User Table Columns
    private static final String USER_ID = "id";
    private static final String USER_LOGIN = "login";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWD = "passwd";
    private static final String USER_FIRST_NAME = "first_name";
    private static final String USER_LAST_NAME = "last_name";

    // Library Table Columns
    private static final String GROCERY_USER_ID = "user_id";
    private static final String GROCERY_ITEM_ID = "item_id";

    // Item Table Columns
    private static final String ITEM_ID = "id";
    private static final String ITEM_REFERENCE = "reference";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_PRICE = "price";

    private static DatabaseHelper sInstance;

    private Cursor userCursor;
    private Cursor itemCursor;
    private Cursor groceryCursor;

    // Constructeur privé pour empêcher l'instanciation directe.
    // Effectuer un appel à la méthode statique "getInstance()" à la place.
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Utilise le contexte de l'application, qui veillera à ce que le contexte d'une activité ne fui pas accidentellement.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Appelé lorsque la connexion à la base de données est configurée.
    // Configure les paramètres de la base de données
    // Exemple : support de clé étrangère, journalisation en écriture, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Appelée lorsque la base de données est créée pour la première fois.
    // Si une base de données existe déjà sur un disque avec le même DATABASE_NAME, cette méthode ne sera pas appelée.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
                "(" +
                USER_ID + " INTEGER PRIMARY KEY," +
                USER_LOGIN + " TEXT," +
                USER_EMAIL + " TEXT," +
                USER_PASSWD + " TEXT," +
                USER_FIRST_NAME + " TEXT," +
                USER_LAST_NAME + " TEXT" +
                ");";

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM +
                "(" +
                ITEM_ID + " INTEGER PRIMARY KEY," +
                ITEM_REFERENCE + " TEXT," +
                ITEM_NAME + " TEXT," +
                ITEM_PRICE + " TEXT" +
                ");";

        String CREATE_GROCERY_TABLE = "CREATE TABLE " + TABLE_GROCERY +
                "(" +
                GROCERY_USER_ID + " INTEGER REFERENCES " + TABLE_USER + "(" + USER_ID + ")," +
                GROCERY_ITEM_ID + " INTEGER REFERENCES " + TABLE_ITEM + "(" + ITEM_ID + ")," +
                "PRIMARY KEY(" + GROCERY_USER_ID + ", " + GROCERY_ITEM_ID + ")" +
                ");";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_GROCERY_TABLE);
    }

    // Appelée lorsque la base de données doit être mise à niveau.
    // Cette méthode ne sera appelée que si une base de données existe déjà sur un disque avec le même DATABASE_NAME,
    // mais DATABASE_VERSION doit être différente de la version de la base de données existante sur le disque.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        }
    }

    //////////////////
    // // // ITEM
    //////////////////
    /**
     * @param item Item
     * @return String
     */
    public String addItem(Item item) {
        // Ouverture de la BD en écriture
        SQLiteDatabase db = getWritableDatabase();
        String item_id = "";

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ITEM_REFERENCE, item.reference);
            values.put(ITEM_NAME, item.name);
            values.put(ITEM_PRICE, item.price);

            item_id = String.valueOf(db.insertOrThrow(TABLE_ITEM, null, values));
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de l'ajout d'un article en BD.");
        } finally {
            db.endTransaction();
        }
        return item_id;
    }

    /**
     * @param item
     */
    public void updateItem(Item item) {
        // Ouverture de la BD en écriture
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ITEM_ID, item.id);
            values.put(ITEM_REFERENCE, item.reference);
            values.put(ITEM_NAME, item.name);
            values.put(ITEM_PRICE, item.price);

            db.update(TABLE_ITEM, values, ITEM_ID + "= ?", new String[]{item.id});
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la mise à jour d'un article en BD.");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param item Item
     */
    public void deleteOneItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_GROCERY, GROCERY_ITEM_ID + "= ?", new String[]{item.id});
            db.delete(TABLE_ITEM, ITEM_REFERENCE + "= ?", new String[]{item.reference});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la suppression d'un article.");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllItems() {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_GROCERY, null, null);
            db.delete(TABLE_ITEM, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la suppression de tous les articles ainsi que leurs relations.");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @return List<Item>
     */
    public List<Item> getAllItems() {
        List<Item> T_items = new ArrayList<>();

        String ITEMS_SELECT_QUERY = String.format("SELECT * FROM %s ;", TABLE_ITEM);

        SQLiteDatabase db = getReadableDatabase();
        itemCursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (itemCursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.id = itemCursor.getString(itemCursor.getColumnIndex(ITEM_ID));
                    item.reference = itemCursor.getString(itemCursor.getColumnIndex(ITEM_REFERENCE));
                    item.name = itemCursor.getString(itemCursor.getColumnIndex(ITEM_NAME));
                    item.price = itemCursor.getString(itemCursor.getColumnIndex(ITEM_PRICE));

                    T_items.add(item);
                } while (itemCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la récuperation des tous les articles.");
        } finally {
            if (itemCursor != null && !itemCursor.isClosed()) {
                itemCursor.close();
            }
        }
        return T_items;
    }

    /**
     * @param id
     * @return Item
     */
    public Item getItemById(String id) {
        Item item = new Item();

        SQLiteDatabase db = getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_ITEM + " WHERE id = ? ;";
        itemCursor = db.rawQuery(queryString, new String[]{id});
        try {
            if (itemCursor.moveToFirst()) {
                do {
                    item.id = itemCursor.getString(itemCursor.getColumnIndex(ITEM_ID));
                    item.reference = itemCursor.getString(itemCursor.getColumnIndex(ITEM_REFERENCE));
                    item.name = itemCursor.getString(itemCursor.getColumnIndex(ITEM_NAME));
                    item.price = itemCursor.getString(itemCursor.getColumnIndex(ITEM_PRICE));
                } while (itemCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la récuperation d'un article.");
        } finally {
            if (itemCursor != null && !itemCursor.isClosed()) {
                itemCursor.close();
            }
        }
        return item;
    }

    //////////////////
    // // // USER
    //////////////////
    /**
     * @param user User
     * @return String
     */
    public String addUser(User user) {
        // La connexion à la base de données est mise en cache, ce n'est donc pas gourmand de rappeller getWriteableDatabase() de multiples fois.
        SQLiteDatabase db = getWritableDatabase();
        String user_id = "";

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(USER_LOGIN, user.login);
            values.put(USER_PASSWD, user.email);
            values.put(USER_PASSWD, user.passwd);
            values.put(USER_FIRST_NAME, user.firstName);
            values.put(USER_LAST_NAME, user.lastName);

            // On l'ajoute
            user_id = String.valueOf(db.insertOrThrow(TABLE_USER, null, values));
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de l'ajout d'un user.");
        } finally {
            db.endTransaction();
        }
        return user_id;
    }

    /**
     * @param user User
     */
    public void updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(USER_ID, user.id);
            values.put(USER_LOGIN, user.login);
            values.put(USER_PASSWD, user.email);
            values.put(USER_PASSWD, user.passwd);
            values.put(USER_FIRST_NAME, user.firstName);
            values.put(USER_LAST_NAME, user.lastName);

            db.update(TABLE_USER, values, USER_ID + "= ?", new String[]{user.id});
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la mise à jour d'un user.");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @param user User
     */
    public void deleteOneUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_GROCERY, GROCERY_USER_ID + "= ?", new String[]{user.id});
            db.delete(TABLE_USER, USER_LOGIN + "= ?", new String[]{user.login});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la suppression d'un user.");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllUsers(){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_GROCERY, null, null);
            db.delete(TABLE_USER, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la suppression de tous les users ainsi que leurs relations.");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * @return List<User>
     */
    public List<User> getAllUsers() {
        List<User> T_users = new ArrayList<>();

        String USERS_SELECT_QUERY = String.format("SELECT * FROM %s ;", TABLE_USER);

        SQLiteDatabase db = getReadableDatabase();
        userCursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (userCursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.id = userCursor.getString(userCursor.getColumnIndex(USER_ID));
                    user.login = userCursor.getString(userCursor.getColumnIndex(USER_LOGIN));
                    user.email = userCursor.getString(userCursor.getColumnIndex(USER_EMAIL));
                    user.passwd = userCursor.getString(userCursor.getColumnIndex(USER_PASSWD));
                    user.firstName = userCursor.getString(userCursor.getColumnIndex(USER_FIRST_NAME));
                    user.lastName = userCursor.getString(userCursor.getColumnIndex(USER_LAST_NAME));

                    T_users.add(user);
                } while (userCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la récuperation des tous les users. \n" + e.getMessage());
        } finally {
            if (userCursor != null && !userCursor.isClosed()) {
                userCursor.close();
            }
        }
        return T_users;
    }

    /**
     * @return User||Item
     */
    public User getUserById(String id) {
        User user = new User();

        SQLiteDatabase db = getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_USER + " WHERE id = ? ;";
        userCursor = db.rawQuery(queryString, new String[]{id});
        try {
            if (userCursor.moveToFirst()) {
                do {
                    user.id = userCursor.getString(userCursor.getColumnIndex(USER_ID));
                    user.login = userCursor.getString(userCursor.getColumnIndex(USER_LOGIN));
                    user.email = userCursor.getString(userCursor.getColumnIndex(USER_EMAIL));
                    user.passwd = userCursor.getString(userCursor.getColumnIndex(USER_PASSWD));
                    user.firstName = userCursor.getString(userCursor.getColumnIndex(USER_FIRST_NAME));
                    user.lastName = userCursor.getString(userCursor.getColumnIndex(USER_LAST_NAME));
                } while (userCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la récuperation d'un user.");
        } finally {
            if (userCursor != null && !userCursor.isClosed()) {
                userCursor.close();
            }
        }
        return user;
    }


    //////////////////
    // // // LIBRARY

    /**
     * Récupère la liste de jeux d'un user
     *
     * @param user User
     * @return List
     */
    public List<Item> getUsersGrocery(User user) {
        List<Item> T_items = new ArrayList<>();

        String GROCERY_SELECT_QUERY = "SELECT * FROM " + TABLE_GROCERY +
                " WHERE " + GROCERY_USER_ID + "=" + user.id + ";";

        SQLiteDatabase db = getReadableDatabase();
        groceryCursor = db.rawQuery(GROCERY_SELECT_QUERY, null);

        try {
            if (groceryCursor.moveToFirst()) {
                do {
                    T_items.add(getItemById(groceryCursor.getString(groceryCursor.getColumnIndex(GROCERY_ITEM_ID))));
                } while (groceryCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la récuperation d'une liste de courses d'un user.");
        } finally {
            if (groceryCursor != null && !groceryCursor.isClosed()) {
                groceryCursor.close();
            }
        }
        return T_items;
    }

    /**
     * Récupère la liste des users possédant cet item dans leurs listes
     *
     * @param item
     * @return List
     */
    public List<User> getItemOwnedBy(Item item) {
        List<User> T_users = new ArrayList<>();

        String GROCERY_SELECT_QUERY = "SELECT * FROM " + TABLE_GROCERY +
                " WHERE " + GROCERY_ITEM_ID + "=" + item.id + ";";

        SQLiteDatabase db = getReadableDatabase();
        groceryCursor = db.rawQuery(GROCERY_SELECT_QUERY, null);

        try {
            if (groceryCursor.moveToFirst()) {
                do {
                    T_users.add(getUserById(groceryCursor.getString(groceryCursor.getColumnIndex(GROCERY_USER_ID))));
                } while (groceryCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Une erreur s'est produite lors de la récuperation de la liste des personnes possédant cet article.");
        } finally {
            if (groceryCursor != null && !groceryCursor.isClosed()) {
                groceryCursor.close();
            }
        }
        return T_users;
    }

}

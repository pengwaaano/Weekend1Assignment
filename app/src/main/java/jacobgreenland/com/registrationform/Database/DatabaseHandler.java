package jacobgreenland.com.registrationform.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jacobgreenland.com.registrationform.Constants;
import jacobgreenland.com.registrationform.Model.Person;

/**
 * Created by Jacob on 03/06/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.DATABASE_NAME, factory, Constants.DATABASE_VERSION);
    }
    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + Constants.TABLE_PEOPLE + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_FIRSTNAME + " TEXT,"
                + Constants.KEY_LASTNAME + " TEXT" + ")";
        /*String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT" + ")";*/
        db.execSQL(CREATE_PEOPLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_PEOPLE);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void Add_Contact(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_FIRSTNAME, person.getFirstName()); // Contact First Name
        values.put(Constants.KEY_LASTNAME, person.getLastName()); // Contact Last Name
        //values.put(KEY_EMAIL, contact.getEmail()); // Contact Email
        // Inserting Row
        db.insert(Constants.TABLE_PEOPLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Person Get_Person(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_PEOPLE, new String[] { Constants.KEY_ID,
                        Constants.KEY_FIRSTNAME, Constants.KEY_LASTNAME},Constants.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Person person = new Person(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        cursor.close();
        db.close();

        return person;
    }

}

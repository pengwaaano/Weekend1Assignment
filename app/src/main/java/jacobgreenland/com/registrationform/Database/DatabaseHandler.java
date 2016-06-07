package jacobgreenland.com.registrationform.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import jacobgreenland.com.registrationform.Constants;
import jacobgreenland.com.registrationform.Model.Person;

/**
 * Created by Jacob on 03/06/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    /*public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.DATABASE_NAME, factory, Constants.DATABASE_VERSION);
    }*/

    ArrayList<Person> person_list = new ArrayList<Person>();

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + Constants.TABLE_PEOPLE + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_FIRSTNAME + " TEXT,"
                + Constants.KEY_LASTNAME + " TEXT," + Constants.KEY_COUNTRY + " TEXT,"
                + Constants.KEY_GENDER + " TEXT," + Constants.KEY_DATEOFBIRTH + " TEXT,"
                + Constants.KEY_PHOTO + " TEXT" + ")";
        /*String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT" + ")";*/
        db.execSQL(CREATE_PEOPLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_PEOPLE);
        Log.w("myApp", "Table Dropped!");
        // Create tables again
        onCreate(db);
    }


    // Adding new contact
    public void Add_Person(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_FIRSTNAME, person.getFirstName()); // Person First Name
        values.put(Constants.KEY_LASTNAME, person.getLastName()); // Person Last Name
        values.put(Constants.KEY_COUNTRY, person.getCountry()); // Person Country
        values.put(Constants.KEY_GENDER, person.getGender()); // Person Gender
        values.put(Constants.KEY_DATEOFBIRTH, person.getDateOfBirth()); // Person DOB
        values.put(Constants.KEY_PHOTO, person.getPhoto()); // Person DOB

        //values.put(KEY_EMAIL, contact.getEmail()); // Contact Email
        // Inserting Row
        db.insert(Constants.TABLE_PEOPLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Person Get_Person(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_PEOPLE, new String[] { Constants.KEY_ID,
                        Constants.KEY_FIRSTNAME, Constants.KEY_LASTNAME, Constants.KEY_COUNTRY, Constants.KEY_GENDER, Constants.KEY_DATEOFBIRTH, Constants.KEY_PHOTO},Constants.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Person person = new Person(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return contact
        cursor.close();
        db.close();

        return person;
    }

    public ArrayList<Person> Get_People()
    {
        try {
            person_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Constants.TABLE_PEOPLE;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Person person = new Person();
                    person.setID(Integer.parseInt(cursor.getString(0)));
                    person.setFirstName(cursor.getString(1));
                    person.setLastName(cursor.getString(2));
                    person.setCountry(cursor.getString(3));
                    person.setGender(cursor.getString(4));
                    person.setDateOfBirth(cursor.getString(5));
                    person.setPhoto(cursor.getString(6));
                    // Adding contact to list
                    person_list.add(person);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return person_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }
        return person_list;
    }

    public int Update_Person(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_FIRSTNAME, person.getFirstName());
        values.put(Constants.KEY_LASTNAME, person.getLastName());
        values.put(Constants.KEY_COUNTRY, person.getCountry());
        values.put(Constants.KEY_DATEOFBIRTH, person.getDateOfBirth());
        values.put(Constants.KEY_GENDER, person.getGender());
        values.put(Constants.KEY_PHOTO, person.getPhoto());
        // updating row
        return db.update(Constants.TABLE_PEOPLE, values, Constants.KEY_ID + " = ?",
                new String[] { String.valueOf(person.getID()) });
    }

    public void Delete_Person(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_PEOPLE, Constants.KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

}

package jacobgreenland.com.registrationform;

import java.util.ArrayList;

import jacobgreenland.com.registrationform.Model.Person;

/**
 * Created by Jacob on 03/06/16.
 */
public class Constants {

    public static final int DATABASE_VERSION = 4;

    // Database Name
    public static final String DATABASE_NAME = "registrationManager";

    // Contacts table name
    public static final String TABLE_PEOPLE = "people";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_GENDER = "gender";
    //private static final String KEY_EMAIL = "email";
    public final ArrayList<Person> people_list = new ArrayList<Person>();

}

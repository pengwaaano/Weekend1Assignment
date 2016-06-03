package jacobgreenland.com.registrationform.Model;

/**
 * Created by Jacob on 03/06/16.
 */
public class Person {
    int _id;
    String _firstName;
    String _lastName;
    String _country;
    String _gender;

    public Person()
    {

    }

    public Person(int id,String fN, String lN, String c, String g)
    {
        this._id = id;
        this._firstName = fN;
        this._lastName = lN;
        this._country = c;
        this._gender = g;
    }

    public Person(String fN, String lN, String c, String g)
    {
        this._firstName = fN;
        this._lastName = lN;
        this._country = c;
        this._gender = g;
    }
    public int getID()
    {
        return this._id;
    }
    public String getFirstName()
    {
        return this._firstName;
    }
    public String getLastName()
    {
        return this._lastName;
    }
    public String getCountry() { return this._country; }
    public String getGender() {return this._gender; }
    public void setID(int i)
    {
        this._id = i;
    }
    public void setFirstName(String fN)
    {
        this._firstName = fN;
    }
    public void setLastName(String lN)
    {
        this._lastName = lN;
    }
    public void setCountry(String c) {this._country = c; }
    public void setGender(String g) {this._gender = g; }
}

package jacobgreenland.com.registrationform.Model;

/**
 * Created by Jacob on 03/06/16.
 */
public class Person {
    int _id;
    String _firstName;
    String _lastName;

    public Person()
    {

    }

    public Person(int id,String fN, String lN)
    {
        this._id = id;
        this._firstName = fN;
        this._lastName = lN;
    }

    public Person(String fN, String lN)
    {
        this._firstName = fN;
        this._lastName = lN;
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
}

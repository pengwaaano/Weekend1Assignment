package jacobgreenland.com.registrationform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import jacobgreenland.com.registrationform.Database.DatabaseHandler;
import jacobgreenland.com.registrationform.Model.Person;

public class listActivity extends AppCompatActivity {

    ArrayList<Person> person_data = new ArrayList<Person>();
    DatabaseHandler db;
    ArrayAdapter adapter;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = (ListView) findViewById(R.id.lv_List);

        Set_Refresh_Data();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Set_Refresh_Data();

    }

    public void Set_Refresh_Data() {
        person_data.clear();
        db = new DatabaseHandler(this);
        ArrayList<Person> contact_array_from_db = db.Get_Contacts();

        for (int i = 0; i < contact_array_from_db.size(); i++) {

            int tidno = contact_array_from_db.get(i).getID();
            String firstname = contact_array_from_db.get(i).getFirstName();
            String lastname = contact_array_from_db.get(i).getLastName();
            String country = contact_array_from_db.get(i).getCountry();
            String gender = contact_array_from_db.get(i).getGender();
            String dob = contact_array_from_db.get(i).getDateOfBirth();
            String photo = contact_array_from_db.get(i).getPhoto();

            Person p = new Person();
            p.setID(tidno);
            p.setFirstName(firstname);
            p.setLastName(lastname);
            p.setCountry(country);
            p.setGender(gender);
            p.setDateOfBirth(dob);
            p.setPhoto(photo);

            person_data.add(p);
        }
        db.close();
        adapter = new Person_Adapter(listActivity.this, R.layout.listview_template,
                person_data);
        list.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
    public void addUser(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public class Person_Adapter extends ArrayAdapter<Person> {
        public Activity activity;
        int layoutResourceId;
        Person person;
        ArrayList<Person> data = new ArrayList<Person>();

        public Person_Adapter(Activity act, int layoutResourceId,
                              ArrayList<Person> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            PersonHolder holder = null;
            ListView list = (ListView) findViewById(R.id.lv_List);

            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter2, View item, int pos, long id)
                {
                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final int person_id = pos;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyDataObject.remove(positionToRemove);
                                    DatabaseHandler dBHandler = new DatabaseHandler(
                                            activity.getApplicationContext());
                                    dBHandler.Delete_Contact(data.get(person_id).getID());
                                    person_data.remove(person_id);
                                    listActivity.this.onResume();
                                    adapter.notifyDataSetChanged();


                                }
                            });
                    adb.show();
                    //writeItems();
                    return true;
                }
            });

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new PersonHolder();
                holder.name = (TextView) row.findViewById(R.id.lv_name);
                holder.dob = (TextView) row.findViewById(R.id.lv_dob);
                holder.country = (TextView) row.findViewById(R.id.lv_country);
                holder.gender = (TextView) row.findViewById(R.id.lv_gender);
                holder.photo = (ImageView) row.findViewById(R.id.lv_photo);
                //holder.edit = (Button) row.findViewById(R.id.btn_update);
                //holder.delete = (Button) row.findViewById(R.id.btn_delete);
                row.setTag(holder);
            } else {
                holder = (PersonHolder) row.getTag();
            }
            person = data.get(position);
            //holder.edit.setTag(person.getID());
            //holder.delete.setTag(person.getID());
            holder.name.setText(person.getFirstName() + " " + person.getLastName());
            holder.dob.setText(person.getDateOfBirth());
            holder.country.setText(person.getCountry());
            holder.gender.setText(person.getGender());

            if (person.getPhoto() != "") {
                Uri photoLocation;
                photoLocation = Uri.parse(person.getPhoto());
                holder.photo.setImageURI(photoLocation);
            }

            notifyDataSetChanged();
            //INSERT PHOTO CODE

        /*holder.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Edit Button Clicked", "**********");

                Intent update_user = new Intent(activity,
                        Add_Update_User.class);
                update_user.putExtra("called", "update");
                update_user.putExtra("USER_ID", v.getTag().toString());
                activity.startActivity(update_user);

            }
        });*/

        /*holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub

                // show a message while loader is loading

                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete ");
                final int user_id = Integer.parseInt(v.getTag().toString());
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok",
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // MyDataObject.remove(positionToRemove);
                                DatabaseHandler dBHandler = new DatabaseHandler(
                                        activity.getApplicationContext());
                                dBHandler.Delete_Contact(user_id);
                                Main_Screen.this.onResume();

                            }
                        });
                adb.show();
            }

        });*/
            return row;

        }

        class PersonHolder {
            TextView name;
            TextView country;
            TextView gender;
            TextView dob;
            ImageView photo;
            Button edit;
            Button delete;
        }

    }

}

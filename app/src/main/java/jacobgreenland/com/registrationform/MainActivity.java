package jacobgreenland.com.registrationform;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jacobgreenland.com.registrationform.Database.DatabaseHandler;
import jacobgreenland.com.registrationform.Model.Person;

public class MainActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogHandler, Communicator{

    private static final int REQUEST_CODE = 1;

    TextView mFirstName;
    TextView mLastName;
    ImageView mProfilePhoto;
    TextView mDateOfBirth;
    private Bitmap bitmap;
    Spinner mCountrySpinner;
    private BottomSheetBehavior mBottomSheetBehavior;
    Button dateButton;
    Button mOkButton, mViewAllButton;
    Button mConfirmButton;
    RadioButton mMale, mFemale, mOther;
    DatabaseHandler dbHandler = new DatabaseHandler(this);

    private String valid_firstName = null, valid_lastName = null, valid_country = null, valid_gender = null, valid_dob = null, valid_photo = null;
    String Toast_msg = null;

    private ActionBar actionBar;

    String change = "add";
    int selectedEdit = 0;

    ArrayList<Person> person_data = new ArrayList<Person>();
    DatabaseHandler db;
    ArrayAdapter adapter;

    ListView listV;

    //Date mDateOfBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        actionBar = getActionBar();
        // Hide the action bar title
        //actionBar.setDisplayShowTitleEnabled(false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListFragment list = new ListFragment();
        fragmentTransaction.add(R.id.main_fragment, list, "details");
        fragmentTransaction.commit();

        //Set_Refresh_Data();
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //Set_Refresh_Data();
        //RefreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        Fragment currentFrag = getFragmentManager().findFragmentById(R.id.main_fragment);
        if(currentFrag instanceof DetailsFragment)
        {
            inflater.inflate(R.menu.adduser, menu);
        }
        else
        {
            inflater.inflate(R.menu.main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void initialise() {
        initialiseUI();
    }
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void update() {
        change = "edit";
    }
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void show(ListView lv) {
        Set_Refresh_Data(lv);
    }
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void confirm() {
        //save data
        valid_firstName = mFirstName.getText().toString();
        valid_lastName = mLastName.getText().toString();
        valid_country = mCountrySpinner.getSelectedItem().toString();
        valid_dob = mDateOfBirth.getText().toString();
        //valid_photo = (String) mProfilePhoto.getTag();
        if (mMale.isChecked())
            valid_gender = "Male";
        else if (mFemale.isChecked())
            valid_gender = "Female";
        else if (mOther.isChecked())
            valid_gender = "Other";

        //valid_email = add_email.getText().toString();
        if (change.equalsIgnoreCase("edit")) {
            dbHandler.Update_Person(new Person(selectedEdit, valid_firstName,
                    valid_lastName, valid_country, valid_gender, valid_dob, valid_photo));
            dbHandler.close();
            Toast_msg = "Data updated successfully";
            Show_Toast(Toast_msg);
        } else {
            Log.d("TEST", "Person Added");
            dbHandler.Add_Person(new Person(valid_firstName,
                    valid_lastName, valid_country, valid_gender, valid_dob, valid_photo));
            Toast_msg = "Data inserted successfully";
            Show_Toast(Toast_msg);
        }
        change = "add";
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mOkButton.setVisibility(View.VISIBLE);
        mViewAllButton.setVisibility(View.VISIBLE);

            Reset_Text();

    }
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void returnViews(TextView fn, TextView ln, Spinner country, TextView dob, ImageView photo, RadioButton m, RadioButton f, RadioButton o, Button b, Button ok, Button va, Button c)
    {
        mFirstName = fn;
        mLastName = ln;
        mCountrySpinner = country;
        mDateOfBirth = dob;
        mProfilePhoto = photo;
        mMale = m;
        mFemale = f;
        mOther = o;
        dateButton = b;
        mOkButton = ok;
        mViewAllButton = va;
        mConfirmButton = c;

        RefreshData();
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void cancel() {
        change = "add";
        Reset_Text();
        ListFragment list = new ListFragment();
        changeFragment(list);
    }
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void resetText() {
        //change = "add";
        Reset_Text();
    }

    public void initialiseUI()
    {
        mDateOfBirth.setText(R.string.dob);
        //set date picker onclick
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });

        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> av, View v,
                                       int position, long itemId) {
                // TODO Auto-generated method stub
                String item=av.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> av) {
                // TODO Auto-generated method stub

            }
        });
        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //refresh data if it's being edited rather than added

        RefreshData();
    }

    public void RefreshData()
    {
            if (change.equalsIgnoreCase("edit"))
            {
                //int editID = getIntent().getIntExtra("ID", 0);

                Person editPerson = dbHandler.Get_Person(selectedEdit);
                // load data for the details being edited
                mFirstName.setText(editPerson.getFirstName());
                mLastName.setText(editPerson.getLastName());
                mDateOfBirth.setText(editPerson.getDateOfBirth());
                valid_photo = editPerson.getPhoto();
                //if(editPerson.getPhoto().contains("content:"))
                if (editPerson.getPhoto() != "") {
                    byte[] decodedString = Base64.decode(editPerson.getPhoto(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    mProfilePhoto.setImageBitmap(decodedByte);
                }
                //mProfilePhoto.setImageURI(Uri.parse(editPerson.getPhoto()));

                Log.d("Gender Test", editPerson.getGender().toString());
                //mMale.setChecked(true);
                if (editPerson.getGender().equalsIgnoreCase("male")) {
                    mMale.setChecked(true);
                    //mFemale.setChecked(false);
                    //mOther.setChecked(false);
                } else if (editPerson.getGender().equalsIgnoreCase("female")) {
                    //mMale.setChecked(false);
                    mFemale.setChecked(true);
                    //mOther.setChecked(false);
                } else if (editPerson.getGender().equalsIgnoreCase("other")) {
                    //mMale.setChecked(false);
                    //mFemale.setChecked(false);
                    mOther.setChecked(true);
                }

                mCountrySpinner.setSelection(((ArrayAdapter<String>) mCountrySpinner.getAdapter()).getPosition(editPerson.getCountry()));
                //mCountrySpinner.getAdapter().getI
                //mCountrySpinner.set
            }
            else
            {
                Reset_Text();
            }
        //}
            //set confirm button onClick
            mConfirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    //save data
                    valid_firstName = mFirstName.getText().toString();
                    valid_lastName = mLastName.getText().toString();
                    valid_country = mCountrySpinner.getSelectedItem().toString();
                    valid_dob = mDateOfBirth.getText().toString();
                    //valid_photo = (String) mProfilePhoto.getTag();
                    if (mMale.isChecked())
                        valid_gender = "Male";
                    else if (mFemale.isChecked())
                        valid_gender = "Female";
                    else if (mOther.isChecked())
                        valid_gender = "Other";

                    //valid_email = add_email.getText().toString();
                        if (change.equalsIgnoreCase("edit")) {
                            dbHandler.Update_Person(new Person(selectedEdit, valid_firstName,
                                    valid_lastName, valid_country, valid_gender, valid_dob, valid_photo));
                            dbHandler.close();
                            Toast_msg = "Data updated successfully";
                            Show_Toast(Toast_msg);
                        } else {
                            Log.d("TEST", "Person Added");
                            dbHandler.Add_Person(new Person(valid_firstName,
                                    valid_lastName, valid_country, valid_gender, valid_dob, valid_photo));
                            Toast_msg = "Data inserted successfully";
                            Show_Toast(Toast_msg);
                        }
                    change = "add";
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mOkButton.setVisibility(View.VISIBLE);
                    mViewAllButton.setVisibility(View.VISIBLE);
                    Reset_Text();
                }
            });
    }
    //show bottom sheet
    public void bottomSheetGo(View v) {
        switch( v.getId() ) {
            case R.id.et_okButton: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                if (mMale.isChecked())
                    valid_gender = "Male";
                else if (mFemale.isChecked())
                    valid_gender = "Female";
                else if (mOther.isChecked())
                    valid_gender = "Other";
                else
                    valid_gender = "";
                //check all data has been entered
                if(mFirstName.getText() == null || mLastName.getText() == null || mCountrySpinner.getSelectedItem() == null || mDateOfBirth.getText() == "Date Of Birth"
                        || valid_gender == "")
                {
                    //Log.d("TEST", mProfilePhoto.getTag().toString());
                    mConfirmButton.setEnabled(false);
                }
                else
                    mConfirmButton.setEnabled(true);

                mOkButton.setVisibility(View.INVISIBLE);
                mViewAllButton.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }
    //hide Bottom sheet
    public void bottomSheetGone(View v) {
        switch( v.getId() ) {
            case R.id.et_cancelButton: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                mOkButton.setVisibility(View.VISIBLE);
                mViewAllButton.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        mDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
        valid_dob = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
        //getString(R.string.date_picker_result_value, year, monthOfYear+1, dayOfMonth));
    }
    public void onImageClick(View v)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                Log.d("Activity", data.getData().toString());
                stream = getContentResolver().openInputStream(data.getData());
                //Log.d("Test", stream.toString() + " 1");
                bitmap = BitmapFactory.decodeStream(stream);

                mProfilePhoto.setImageBitmap(bitmap);

// get the base 64 string
                String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                        Base64.NO_WRAP);

                valid_photo = imgString;//getImageUri(getApplicationContext(),bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
    }
    //convert bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void ViewAll(View v)
    {
        ListFragment list = new ListFragment();
        changeFragment(list);
        //Set_Refresh_Data();
    }

    public void changeFragment(Fragment f)
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, f, "details");
        fragmentTransaction.commit();
    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void Reset_Text() {
        mFirstName.setText("");
        mLastName.setText("");
        mDateOfBirth.setText("Date Of Birth");
        mProfilePhoto.setImageResource(android.R.drawable.ic_dialog_info);
        mMale.setChecked(false);
        mFemale.setChecked(false);
        mOther.setChecked(false);
    }

    public void Set_Refresh_Data(ListView lv) {
        person_data.clear();
        db = new DatabaseHandler(this);
        ArrayList<Person> contact_array_from_db = db.Get_People();

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

        adapter = new Person_Adapter(MainActivity.this, R.layout.listview_template,
                person_data);

        //listV = (ListView)findViewById(R.id.lv_List);

        lv.setAdapter(adapter);
        listV = lv;
        adapter.notifyDataSetChanged();
    }

    public class Person_Adapter extends ArrayAdapter<Person> {
        Activity activity;
        int layoutResourceId;
        Person user;
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
            UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();

                holder.name = (TextView) row.findViewById(R.id.lv_name);
                holder.country = (TextView) row.findViewById(R.id.lv_country);
                holder.dob = (TextView) row.findViewById(R.id.lv_dob);
                holder.gender = (TextView) row.findViewById(R.id.lv_gender);
                holder.photo = (ImageView) row.findViewById(R.id.lv_photo);
                holder.IDtag = (Button) row.findViewById(R.id.invisibutton);

                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            user = data.get(position);
            holder.name.setText(user.getFirstName() + " " + user.getLastName());
            holder.country.setText(user.getCountry());
            holder.dob.setText(user.getDateOfBirth());
            holder.gender.setText(user.getGender());
            holder.IDtag.setTag(user.getID());
            byte[] decodedString = Base64.decode(user.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.photo.setImageBitmap(decodedByte);

            ListView list = listV;
            //set onClick for edit
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter2, View item, int pos, long id) {
                    // TODO Auto-generated method stub
                    String ID = item.findViewById(R.id.invisibutton).getTag().toString();
                    Log.d("test", ID);

                    //invalidateOptionsMenu();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailsFragment details = new DetailsFragment();
                    fragmentTransaction.replace(R.id.main_fragment, details, "details");
                    fragmentTransaction.commit();
                    change = "edit";
                    selectedEdit = Integer.parseInt(ID);

                    /*Intent edit_user = new Intent(MainActivity.this,
                            MainActivity.class);
                    edit_user.putExtra("Update", "edit");
                    edit_user.putExtra("ID", data.get(pos).getID());
                    edit_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(edit_user);
                    finish();*/
                }
            });
            //set long click for delete
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter2, View item, int pos, long id) {
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
                                    dBHandler.Delete_Person(data.get(person_id).getID());
                                    person_data.remove(person_id);
                                    MainActivity.this.onResume();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    adb.show();
                    //writeItems();
                    return true;
                }
            });

            return row;

        }

        class UserHolder {
            TextView name;
            TextView country;
            TextView dob;
            TextView gender;
            ImageView photo;
            Button IDtag;
        }

    }

}

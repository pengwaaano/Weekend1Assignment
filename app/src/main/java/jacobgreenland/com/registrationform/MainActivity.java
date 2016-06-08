package jacobgreenland.com.registrationform;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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

    private String valid_firstName = null, valid_lastName = null, valid_country = null, valid_gender = null, valid_dob = null, valid_photo = null;
    String Toast_msg = null;

    private ActionBar actionBar;

    String change = "add";
    int selectedEdit = 0;

    ArrayList<Person> person_data = new ArrayList<Person>();


    private RecyclerView mRecyclerView;
    private PersonAdapter mAdapter;

    //ListView listV;

    RealmConfiguration realmConfig;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        actionBar = getActionBar();

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        RealmQuery<Person> query = realm.where(Person.class);

        RealmResults<Person> allPeople = query.findAll();

        Log.d("test", allPeople.get(0).getFirstName());

        //realm.beginTransaction();
        //Realm.getDefaultInstance().deleteAll();
        //realm.commitTransaction();

        // Associate searchable configuration with the SearchView

        // Hide the action bar title
        //actionBar.setDisplayShowTitleEnabled(false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListFragment list = new ListFragment();
        fragmentTransaction.add(R.id.main_fragment, list, "details");
        fragmentTransaction.commit();

        //Set_Refresh_Data();
    }

    public void initialiseRecyclerView()
    {
        //mRecyclerView = (RecyclerView) findViewById(R.id.lv_List);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //mAdapter = new PersonAdapter(person_data, R.layout.listview_template, getApplicationContext());
        //mRecyclerView.setAdapter(mAdapter);
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
    public void show(RecyclerView lv) {
        mRecyclerView = lv;
        initialiseRecyclerView();
        Set_Refresh_Data();
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

            Person person = new Person();
            person.setID(selectedEdit);

            person.setFirstName(valid_firstName);
            person.setLastName(valid_lastName);
            person.setDateOfBirth(valid_dob);
            person.setCountry(valid_country);
            person.setGender(valid_gender);
            person.setPhoto(valid_photo);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(person);
            realm.commitTransaction();

            Toast_msg = "Data updated successfully";
            Show_Toast(Toast_msg);
        } else {
            realm.beginTransaction();
            Person person = realm.createObject(Person.class);
            person.setID(realm.where(Person.class).max("_id").intValue()+1);

            person.setFirstName(valid_firstName);
            person.setLastName(valid_lastName);
            person.setDateOfBirth(valid_dob);
            person.setCountry(valid_country);
            person.setGender(valid_gender);
            person.setPhoto(valid_photo);
            realm.commitTransaction();

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
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean checkEnabled() {
        boolean check = false;
        if(mFirstName.getText() == null || mLastName.getText() == null || mCountrySpinner.getSelectedItem() == null || mDateOfBirth.getText() == "Date Of Birth"
                || valid_gender == null || valid_photo == null)
        {
            check = false;
        }
        else
        {
            check = true;
        }
        return check;
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
                RealmQuery query = realm.where(Person.class);
                    query.equalTo("_id", selectedEdit);
                RealmResults<Person> result1 = query.findAll();

                Person editPerson = result1.first();
                // load data for the details being edited
                mFirstName.setText(editPerson.getFirstName());
                mLastName.setText(editPerson.getLastName());
                mDateOfBirth.setText(editPerson.getDateOfBirth());
                valid_photo = editPerson.getPhoto();
                if (editPerson.getPhoto() != "") {
                    byte[] decodedString = Base64.decode(editPerson.getPhoto(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    mProfilePhoto.setImageBitmap(decodedByte);
                }

                if (editPerson.getGender().equalsIgnoreCase("male")) {
                    mMale.setChecked(true);
                } else if (editPerson.getGender().equalsIgnoreCase("female")) {
                    mFemale.setChecked(true);
                } else if (editPerson.getGender().equalsIgnoreCase("other")) {
                    mOther.setChecked(true);
                }
                mCountrySpinner.setSelection(((ArrayAdapter<String>) mCountrySpinner.getAdapter()).getPosition(editPerson.getCountry()));
            }
            else
            {
                Reset_Text();
            }
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
                    else
                        valid_gender = null;

                    confirm();

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
                if(checkEnabled())
                {
                    mConfirmButton.setEnabled(true);
                }
                else {
                    mConfirmButton.setEnabled(false);
                }

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
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);

                mProfilePhoto.setImageBitmap(bitmap);

                // get the base 64 string
                String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                        Base64.NO_WRAP);

                valid_photo = imgString;

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
        mProfilePhoto.setImageResource(R.drawable.ic_face);

        mMale.setChecked(false);
        mFemale.setChecked(false);
        mOther.setChecked(false);
    }

    public void Set_Refresh_Data() {
        person_data.clear();
        //db = new DatabaseHandler(this);
        RealmQuery<Person> query = realm.where(Person.class);

        RealmResults<Person> allPeople = query.findAll();
        //ArrayList<Person> allPeople = query.findAll();

        for (int i = 0; i < allPeople.size(); i++) {

            int tidno = allPeople.get(i).getID();
            String firstname = allPeople.get(i).getFirstName();
            String lastname = allPeople.get(i).getLastName();
            String country = allPeople.get(i).getCountry();
            String gender = allPeople.get(i).getGender();
            String dob = allPeople.get(i).getDateOfBirth();
            String photo = allPeople.get(i).getPhoto();

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
        //db.close();

        /*adapter = new Person_Adapter(MainActivity.this, R.layout.listview_template,
                person_data);

        lv.setAdapter(adapter);
        listV = lv;*/

        Log.d("test2", person_data.get(0).getFirstName());
        mAdapter = new PersonAdapter(person_data, R.layout.listview_template, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void onRCClick(View view, int position, boolean isLongClick)
    {
        String ID = view.findViewById(R.id.invisibutton).getTag().toString();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailsFragment details = new DetailsFragment();
        fragmentTransaction.replace(R.id.main_fragment, details, "details");
        fragmentTransaction.commit();
        change = "edit";
        selectedEdit = Integer.parseInt(ID);
    }

    /*public class Person_Adapter extends ArrayAdapter<Person> {
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
            if(!user.getPhoto().equals("")) {
                byte[] decodedString = Base64.decode(user.getPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.photo.setImageBitmap(decodedByte);
            }
            else
            {
                holder.photo.setImageResource(R.drawable.ic_face);
            }
            /*
            //set onClick for edit
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter2, View item, int pos, long id) {
                    // TODO Auto-generated method stub

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

                                    RealmQuery<Person> query2 = realm.where(Person.class);
                                    query2.equalTo("_id",data.get(person_id).getID());
                                    RealmResults<Person> result1 = query2.findAll();
                                    realm.beginTransaction();
                                    result1.deleteAllFromRealm();
                                    realm.commitTransaction();


                                    Set_Refresh_Data(listV);
                                    MainActivity.this.onResume();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    adb.show();
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

    }*/

}

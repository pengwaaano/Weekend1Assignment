package jacobgreenland.com.registrationform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jacobgreenland.com.registrationform.Database.DatabaseHandler;
import jacobgreenland.com.registrationform.Model.Person;

public class MainActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogHandler{

    private static final int REQUEST_CODE = 1;
    TextView mFirstName;
    TextView mLastName;
    ImageView mProfilePhoto;
    TextView mDateOfBirth;
    private Bitmap bitmap;
    Spinner mCountrySpinner;
    private BottomSheetBehavior mBottomSheetBehavior;
    Button mOkButton, mViewAllButton;
    Button mConfirmButton;
    RadioButton mMale, mFemale, mOther;
    RadioGroup group;
    DatabaseHandler dbHandler = new DatabaseHandler(this);

    private String valid_firstName = null, valid_lastName = null, valid_country = null, valid_gender = null, valid_dob = null, valid_photo = null;
    String Toast_msg = null;
    //Date mDateOfBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // call initialise UI elements
        initialiseUI();
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        RefreshData();
    }

    public void initialiseUI()
    {
        //Initialise UI elements
        mFirstName = (TextView) findViewById(R.id.et_firstName);
        mLastName = (TextView) findViewById(R.id.et_lastName);
        mProfilePhoto = (ImageView) findViewById(R.id.et_profilePicture);

        mDateOfBirth = (TextView) findViewById(R.id.et_DateOfBirth);
        Button button = (Button) findViewById(R.id.et_datePicker);

        mMale = (RadioButton) findViewById(R.id.et_maleRadio);
        mFemale = (RadioButton) findViewById(R.id.et_femaleRadio);
        mOther = (RadioButton) findViewById(R.id.et_otherRadio);
        group = (RadioGroup) findViewById(R.id.et_radioGroup);

        mOkButton = (Button) findViewById(R.id.et_okButton);
        mViewAllButton = (Button) findViewById(R.id.et_viewAll);
        mDateOfBirth.setText(R.string.dob);
        //set date picker onclick
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });

        mCountrySpinner = (Spinner) findViewById(R.id.et_countrySpinner);
        //set spinner onclick
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
        mConfirmButton = (Button) findViewById(R.id.et_confirmButton);

        //refresh data if it's being edited rather than added
        RefreshData();
    }

    public void RefreshData()
    {
        String called_from = getIntent().getStringExtra("Update");
        //check if edit
        if(called_from.equalsIgnoreCase("edit"))
        {
            int editID = getIntent().getIntExtra("ID",0);

            Person editPerson = dbHandler.Get_Person(editID);
            // load data for the details being edited
            mFirstName.setText(editPerson.getFirstName());
            mLastName.setText(editPerson.getLastName());
            mDateOfBirth.setText(editPerson.getDateOfBirth());
            valid_photo = editPerson.getPhoto();
            //if(editPerson.getPhoto().contains("content:"))
            if (editPerson.getPhoto() != "")
            {
                byte[] decodedString = Base64.decode(editPerson.getPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                mProfilePhoto.setImageBitmap(decodedByte);
            }
            //mProfilePhoto.setImageURI(Uri.parse(editPerson.getPhoto()));

            Log.d("Gender Test", editPerson.getGender().toString());
            //mMale.setChecked(true);
            if (editPerson.getGender().equalsIgnoreCase("male"))
            {
                mMale.setChecked(true);
                //mFemale.setChecked(false);
                //mOther.setChecked(false);
            }
            else if (editPerson.getGender().equalsIgnoreCase("female"))
            {
                //mMale.setChecked(false);
                mFemale.setChecked(true);
                //mOther.setChecked(false);
            }
            else if (editPerson.getGender().equalsIgnoreCase("other"))
            {
                //mMale.setChecked(false);
                //mFemale.setChecked(false);
                mOther.setChecked(true);
            }

            mCountrySpinner.setSelection(((ArrayAdapter<String>)mCountrySpinner.getAdapter()).getPosition(editPerson.getCountry()));
            //mCountrySpinner.getAdapter().getI
            //mCountrySpinner.set
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

                //valid_email = add_email.getText().toString();
                if(getIntent().getStringExtra("Update").equalsIgnoreCase("edit")) {
                    dbHandler.Update_Person(new Person(getIntent().getIntExtra("ID",0),valid_firstName,
                            valid_lastName, valid_country, valid_gender, valid_dob, valid_photo));
                    dbHandler.close();
                    Toast_msg = "Data updated successfully";
                    Show_Toast(Toast_msg);
                }
                else
                {
                    Log.d("TEST", "Person Added");
                    dbHandler.Add_Person(new Person(valid_firstName,
                            valid_lastName, valid_country, valid_gender, valid_dob, valid_photo));
                    Toast_msg = "Data inserted successfully";
                    Show_Toast(Toast_msg);
                }

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

                    Log.d("TEST", mFirstName.getText().toString());
                    Log.d("TEST", mLastName.getText().toString());
                    Log.d("TEST", mCountrySpinner.getSelectedItem().toString());
                    Log.d("TEST", mDateOfBirth.getText().toString());
                    Log.d("TEST", valid_gender.toString());
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
        Intent intent = new Intent(this,listActivity.class);
        startActivity(intent);
    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void Reset_Text() {

        mFirstName.setText("");
        mLastName.setText("");
    }
}

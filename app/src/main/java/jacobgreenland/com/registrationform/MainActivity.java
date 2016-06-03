package jacobgreenland.com.registrationform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;

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
    Button mOkButton;
    Button mConfirmButton;

    DatabaseHandler dbHandler = new DatabaseHandler(this);

    private String valid_firstName = null;
    private String valid_lastName = null;
    String Toast_msg = null;
    //Date mDateOfBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseUI();

    }
    public void initialiseUI()
    {
        mFirstName = (TextView) findViewById(R.id.et_firstName);
        mLastName = (TextView) findViewById(R.id.et_lastName);
        mProfilePhoto = (ImageView) findViewById(R.id.et_profilePicture);

        mDateOfBirth = (TextView) findViewById(R.id.et_DateOfBirth);
        Button button = (Button) findViewById(R.id.et_datePicker);

        mOkButton = (Button) findViewById(R.id.et_okButton);
        mDateOfBirth.setText(R.string.dob);
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

        mConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // check the value state is null or not
		/*if (valid_name != null && valid_mob_number != null
			&& valid_email != null && valid_name.length() != 0
			&& valid_mob_number.length() != 0
			&& valid_email.length() != 0)*/
                valid_firstName = mFirstName.getText().toString();
                valid_lastName = mLastName.getText().toString();
                //valid_email = add_email.getText().toString();
                dbHandler.Add_Contact(new Person(valid_firstName,
                        valid_lastName));
                Toast_msg = "Data inserted successfully";
                Show_Toast(Toast_msg);
                Reset_Text();



            }
        });
    }

    public void bottomSheetGo(View v) {
        switch( v.getId() ) {
            case R.id.et_okButton: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                mOkButton.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }
    public void bottomSheetGone(View v) {
        switch( v.getId() ) {
            case R.id.et_cancelButton: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mOkButton.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        mDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);//getString(R.string.date_picker_result_value, year, monthOfYear+1, dayOfMonth));
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
    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void Reset_Text() {

        mFirstName.setText("");
        mLastName.setText("");
    }
}

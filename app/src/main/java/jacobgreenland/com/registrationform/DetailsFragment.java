package jacobgreenland.com.registrationform;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Jacob on 06/06/16.
 */
public class DetailsFragment extends Fragment {
    Communicator comm;

    TextView mFirstName;
    TextView mLastName;
    ImageView mProfilePhoto;
    TextView mDateOfBirth;
    Spinner mCountrySpinner;
    RadioButton mMale, mFemale, mOther;
    Button dateButton, okButton, viewAllButton, confirmButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.activity_main, container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();


        mFirstName = (TextView) getView().findViewById(R.id.et_firstName);
        mLastName = (TextView) getView().findViewById(R.id.et_lastName);
        mProfilePhoto = (ImageView) getView().findViewById(R.id.et_profilePicture);

        mCountrySpinner = (Spinner) getView().findViewById(R.id.et_countrySpinner);

        mDateOfBirth = (TextView) getView().findViewById(R.id.et_DateOfBirth);

        mMale = (RadioButton) getView().findViewById(R.id.et_maleRadio);
        mFemale = (RadioButton) getView().findViewById(R.id.et_femaleRadio);
        mOther = (RadioButton) getView().findViewById(R.id.et_otherRadio);
        dateButton = (Button) getView().findViewById(R.id.et_datePicker);

        okButton = (Button) getView().findViewById(R.id.et_okButton);
        viewAllButton = (Button) getView().findViewById(R.id.et_viewAll);
        confirmButton = (Button) getView().findViewById(R.id.et_confirmButton);

        comm.returnViews(mFirstName,mLastName,mCountrySpinner,mDateOfBirth,mProfilePhoto,mMale,mFemale,mOther,dateButton, okButton, viewAllButton, confirmButton);
        comm.initialise();
        //group = (RadioGroup) getView().findViewById(R.id.et_radioGroup);

        //STEP 2: Reference to Main Activity
        /*com=(Communicator) getActivity();
        btnClick=(Button) getActivity().findViewById(R.id.btn1);


        btnClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                counter++;
                //STEP 3:Call Interface Methods
                com.respond("The button was clicked"+counter+"times");
            }
        });*/
    }
}
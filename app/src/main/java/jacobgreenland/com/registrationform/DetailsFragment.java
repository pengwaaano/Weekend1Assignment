package jacobgreenland.com.registrationform;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    Button confirmAction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.activity_main, container,false);
        setHasOptionsMenu(true);
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
        confirmAction = (Button) getView().findViewById(R.id.action_confirm);

        comm.returnViews(mFirstName,mLastName,mCountrySpinner,mDateOfBirth,mProfilePhoto,mMale,mFemale,mOther,dateButton, okButton, viewAllButton, confirmButton);
        comm.initialise();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_confirm:
                // search action
                if(comm.checkEnabled())
                    comm.confirm();
                return true;
            case R.id.action_cancel:
                comm.cancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package jacobgreenland.com.registrationform;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jacob on 06/06/16.
 */
public class DetailsFragment extends Fragment {
    Communicator comm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.activity_main, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
        comm.initialise();
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

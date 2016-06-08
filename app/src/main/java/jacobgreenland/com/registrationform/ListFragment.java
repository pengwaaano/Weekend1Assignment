package jacobgreenland.com.registrationform;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Jacob on 06/06/16.
 */
public class ListFragment extends Fragment {
    Communicator comm;
    Button addUser;
    public static RecyclerView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.activity_list, container, false);

        addUser = (Button) v.findViewById(R.id.lv_AddUser);
        addUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DetailsFragment details = new DetailsFragment();
                fragmentTransaction.replace(R.id.main_fragment, details, "details");
                fragmentTransaction.commit();
                //comm.resetText();
                //comm.update();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();

        lv = (RecyclerView) getView().findViewById(R.id.lv_List);
        comm.show(lv);

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
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

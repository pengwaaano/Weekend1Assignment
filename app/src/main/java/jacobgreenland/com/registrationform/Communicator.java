package jacobgreenland.com.registrationform;

import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Jacob on 06/06/16.
 */
public interface Communicator {
    public void initialise();
    public void update();
    public void show(RecyclerView lv);
    public void returnViews(TextView fn, TextView ln, Spinner country, TextView dob, ImageView photo, RadioButton m, RadioButton f, RadioButton o, Button b, Button ok, Button va, Button c);
    public void confirm();
    public void cancel();
    public void resetText();
    public boolean checkEnabled();
}

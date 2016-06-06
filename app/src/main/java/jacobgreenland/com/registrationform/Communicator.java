package jacobgreenland.com.registrationform;

import android.widget.ListView;

/**
 * Created by Jacob on 06/06/16.
 */
public interface Communicator {
    public void initialise();
    public void update();
    public void show(ListView lv);
}

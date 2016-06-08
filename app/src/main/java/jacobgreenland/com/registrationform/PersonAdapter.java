package jacobgreenland.com.registrationform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jacobgreenland.com.registrationform.Model.Person;

/**
 * Created by Jacob on 08/06/16.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{

    private ArrayList<Person> people;
    private int rowLayout;
    private Context mContext;
    private ItemClickListener icl;
    public PersonAdapter(ArrayList<Person> personList, int rowLayout, Context context) {
        this.people = personList;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonAdapter.ViewHolder holder, int position) {
        final Person person = people.get(position);
        holder.name.setText(person.getFirstName() + " " + person.getLastName());
        holder.country.setText(person.getCountry());
        holder.dob.setText(person.getDateOfBirth());
        holder.gender.setText(person.getGender());

        if(!person.getPhoto().equals("")) {
            byte[] decodedString = Base64.decode(person.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.profilePicture.setImageBitmap(decodedByte);
        }
        else
        {
            holder.profilePicture.setImageResource(R.drawable.ic_face);
        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView country;
        public TextView dob;
        public TextView gender;
        public ImageView profilePicture;

        public String versionName;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.lv_name);
            country = (TextView) itemView.findViewById(R.id.lv_country);
            dob = (TextView) itemView.findViewById(R.id.lv_dob);
            gender = (TextView) itemView.findViewById(R.id.lv_gender);
            profilePicture = (ImageView) itemView.findViewById(R.id.lv_photo);

            itemView.setTag(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(), "OnClick Version :" + getPosition(),
                            Toast.LENGTH_SHORT).show();
                    Log.i("RecyclerView", "Clicked");

                }
                });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(v.getContext(), "OnLongClick Version :" + versionName,
                            Toast.LENGTH_SHORT).show();
                    Log.i("RecyclerView", "OnLongClick");
                    return true;

                }
            });

        }
        /*public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
*/

    }
}

package jacobgreenland.com.registrationform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
    DetailsFragment mFragment;

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

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    fragmentJump(view, people.get(position).getID(), true);
                    Toast.makeText(mContext, "#" + position + " - " + person.getFirstName() + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    fragmentJump(view, people.get(position).getID(), false);
                    Toast.makeText(mContext, "#" + position + " - " +person.getFirstName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fragmentJump(View view, int pos, boolean delete) {
        mFragment = new DetailsFragment();
        switchContent(R.id.main_fragment, mFragment, view, pos, delete);
    }

    public void switchContent(int id, DetailsFragment fragment, View view, int pos, boolean delete) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            DetailsFragment frag = fragment;
            mainActivity.switchContent(id, frag, view, pos, delete);
        }

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

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
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);



        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
        /*public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
*/

    }
}

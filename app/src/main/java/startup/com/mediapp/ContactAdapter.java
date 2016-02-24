package startup.com.mediapp;

/**
 * Created by Harshil on 17/02/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;
    Context context;


    public ContactAdapter(Context context, List<ContactInfo> contactList) {
        this.contactList = contactList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.title.setText(ci.name);
        //contactViewHolder.img_category.setImageResource(ci.imgsrc);
        Glide.with(context)
                .load(ci.imgsrc)
                        //.fitCenter()
                .placeholder(R.drawable.load)
                .crossFade()
                .into(contactViewHolder.img_category);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected ImageView img_category;


        public ContactViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            img_category = (ImageView) v.findViewById(R.id.img);

        }
    }
}
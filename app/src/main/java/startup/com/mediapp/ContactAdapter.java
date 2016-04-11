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
    ClickListener clickListener;
    onLongListener onLongClick;

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

    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void setonLongListener(onLongListener onLongClick){this.onLongClick=onLongClick;}

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        protected TextView title;
        protected ImageView img_category;


        public ContactViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            title =  (TextView) v.findViewById(R.id.title);
            img_category = (ImageView) v.findViewById(R.id.img);

        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.itemClicked(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(onLongClick!=null){
                onLongClick.itemLongClicked(v,getPosition());
            }

            return false;
        }
    }

    public interface ClickListener{

        void itemClicked(View view, int position);
    }

    public interface onLongListener{
        void itemLongClicked(View view, int position);
    }

}
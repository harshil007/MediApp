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

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ContactViewHolder> {

    private List<MainCategoryInfo> contactList;
    Context context;
    ClickListener clickListener;
    onLongListener onLongClick;

    public MainCategoryAdapter(Context context, List<MainCategoryInfo> contactList) {
        this.contactList = contactList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        MainCategoryInfo ci = contactList.get(i);
        contactViewHolder.title.setText(ci.name);
        contactViewHolder.desc.setText(ci.description);
        contactViewHolder.subcategory = ci.subcategory;
        //contactViewHolder.img_category.setImageResource(ci.imgsrc);
        Glide.with(context)
                .load(ci.imgsrc)
                //.fitCenter()
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .into(contactViewHolder.img_category);


     /*   RequestQueue mRequestQueue;
// Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(ci.imgsrc,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        contactViewHolder.img_category.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        contactViewHolder.img_category.setImageResource(R.drawable.load);
                    }
                });
// Access the RequestQueue through your singleton class.
        mRequestQueue.add(request);*/

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

        protected String subcategory;
        protected TextView title;
        protected  TextView desc;
        protected ImageView img_category;


        public ContactViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            title =  (TextView) v.findViewById(R.id.title);
            desc = (TextView) v.findViewById(R.id.description);
            img_category = (ImageView) v.findViewById(R.id.img);

        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.itemClicked(v, getPosition(), subcategory);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(onLongClick!=null){
                onLongClick.itemLongClicked(v,getPosition(), subcategory);
            }

            return false;
        }
    }

    public interface ClickListener{

        void itemClicked(View view, int position, String sc);
    }

    public interface onLongListener{
        void itemLongClicked(View view, int position, String sc);
    }



    public void animateTo(List<MainCategoryInfo> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<MainCategoryInfo> newModels) {
        for (int i = contactList.size() - 1; i >= 0; i--) {
            final MainCategoryInfo model = contactList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<MainCategoryInfo> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final MainCategoryInfo model = newModels.get(i);
            if (!contactList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<MainCategoryInfo> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final MainCategoryInfo model = newModels.get(toPosition);
            final int fromPosition = contactList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public MainCategoryInfo removeItem(int position) {
        final MainCategoryInfo model = contactList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, MainCategoryInfo model) {
        contactList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final MainCategoryInfo model = contactList.remove(fromPosition);
        contactList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}
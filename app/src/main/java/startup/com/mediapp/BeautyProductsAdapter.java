/**
 * Created by Pursnani Kapil on 09-Mar-16.
 */
package startup.com.mediapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BeautyProductsAdapter extends RecyclerView.Adapter<BeautyProductsAdapter.ProductViewHolder> {

    private ArrayList<String> mDataset;
    private List<BeautyProductInfo> productList;
    Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = productList.indexOf(item);
        productList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BeautyProductsAdapter(Context context, List<BeautyProductInfo> productList) {
        this.productList = productList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_product_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ProductViewHolder vh = new ProductViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       BeautyProductInfo bp = productList.get(position);
        final String name = bp.Item_name;
        Glide.with(context)
                .load(bp.imgsrc)
                        //.fitCenter()
                .placeholder(R.drawable.load)
                .crossFade()
                .into(holder.img);
        holder.txtHeader.setText(bp.Item_name);
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });

        holder.txtFooter.setText(bp.Brand);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        protected TextView txtHeader;
        protected TextView txtFooter;
        protected ImageView img;

        public ProductViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            img = (ImageView) v.findViewById(R.id.icon);
        }
    }
}

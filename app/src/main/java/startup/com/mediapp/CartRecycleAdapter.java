package startup.com.mediapp;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 15/04/2016.
 */
public class CartRecycleAdapter extends RecyclerView.Adapter<CartRecycleAdapter.MyViewHolder>  {

    Context context;
    private final List<ItemModel> mModels,myModels;
    ClickListener clickListener;
    onLongListener onLongClick;
    private LayoutInflater inflater;
    private float price=0;
    private int cart_quant;

    public CartRecycleAdapter(Context context, List<ItemModel> models,float price,int cart_quant) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        myModels = new ArrayList<>(mModels);
        this.cart_quant = cart_quant;
        this.price = price;

        //iA = new ItemListActivity();
    }

    public List<ItemModel> get_order(){
        return mModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemModel model = mModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvBrand.setText(model.getBrand_name());
        holder.tvQuant.setText(String.valueOf(model.getQuantity()));
        holder.tvPrice.setText("₹ " + model.getPrice());
        Glide.with(context)
                .load(model.getImg_url())
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .centerCrop()
                .into(holder.iv_item);

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setIs_added(1);
                model.setQuantity(model.getQuantity()+1);
                price = price + Float.parseFloat(model.getPrice());
                String p = "" + price;
                String qt = "" + cart_quant;
                ((CartActivity)context).set_text(p,qt);
                holder.tvQuant.setText(String.valueOf(model.getQuantity()));
            }
        });

        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    model.setQuantity(model.getQuantity()-1);
                    price = price - Float.parseFloat(model.getPrice());
                    String p = "" + price;
                    holder.tvQuant.setText(String.valueOf(model.getQuantity()));
                    if(model.getQuantity()==0){
                        model.setIs_added(0);
                        removeItem(position);
                       notifyDataSetChanged();
                        cart_quant = cart_quant - 1;
                        //animateTo(myModels);
                    }
                    String qt = "" + cart_quant;
                    ((CartActivity)context).set_text(p,qt);

            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = price - model.getQuantity()*Float.parseFloat(model.getPrice());
                String p = "" + price;
                removeItem(position);
                notifyDataSetChanged();
                cart_quant = cart_quant - 1;
                //animateTo(myModels);
                String qt = "" + cart_quant;
                ((CartActivity)context).set_text(p,qt);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }




    public void animateTo(List<ItemModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ItemModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final ItemModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<ItemModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ItemModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ItemModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ItemModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ItemModel removeItem(int position) {
        final ItemModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ItemModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ItemModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setonLongListener(onLongListener onLongClick) {
        this.onLongClick = onLongClick;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView tvName,tvBrand,tvPrice,tvQuant;
        private final ImageView iv_item,iv_add,iv_remove,iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            tvName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_brand_name);
            tvQuant = (TextView) itemView.findViewById(R.id.tv_quantity_item);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            iv_item = (ImageView) itemView.findViewById(R.id.iv_item);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            iv_remove = (ImageView) itemView.findViewById(R.id.iv_remove);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete_item);



        }

        @Override
        public void onClick(View v) {


            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onLongClick != null) {
                onLongClick.itemLongClicked(v, getPosition());
            }

            return false;
        }
    }

    public interface ClickListener {

        void itemClicked(View view, int position);
    }

    public interface onLongListener {
        void itemLongClicked(View view, int position);
    }
}

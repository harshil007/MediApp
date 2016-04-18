package startup.com.mediapp;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 07/03/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    ArrayList<ItemModel> orderList;

    Context context;
    private final List<ItemModel> mModels;
    ClickListener clickListener;
    onLongListener onLongClick;
    TextView tv_cart_q;
    TextView tv_cart_price;
    double price = 0;
    int cart_quant = 0;
    //ItemListActivity iA;


    public RecycleAdapter(Context context, List<ItemModel> models) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        orderList = new ArrayList<>();
        //iA = new ItemListActivity();
    }

    public List<ItemModel> setOrders() {
        orderList.clear();
        for (ItemModel item : mModels) {
            if (item.is_added() == 1) {
                orderList.add(item);
            }
        }
        return orderList;
    }

    public void remove(int position) {
        mModels.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemModel model = mModels.get(position);
        int q;
        //holder.bind(model);

        holder.tvItemName.setText(model.getName());
        holder.tvBrandName.setText(model.getBrand_name());
        holder.tvPrice.setText("₹ " + model.getPrice());
        holder.tvDesc.setText(model.getDescription());
        Glide.with(context)
                .load(model.getImg_url())
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .centerCrop()
                .into(holder.iv_item);

        //holder.tvQuantity.setText(String.valueOf(model.getQuantity()));
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setIs_added(1);
                model.setQuantity(model.getQuantity()+1);
                price = price + Double.parseDouble(model.getPrice());
                String p = "" + price;
                String qt = "" + cart_quant;
                ((ItemListActivity)context).set_text(p,qt);
                holder.tvQuantity.setText(String.valueOf(model.getQuantity()));
            }
        });

       holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getQuantity()==0){
                    model.setIs_added(0);
                }
                else{
                    model.setQuantity(model.getQuantity()-1);
                    price = price - Double.parseDouble(model.getPrice());
                    String p = "" + price;
                    if(model.getQuantity()==0){
                        model.setIs_added(0);
                        cart_quant = cart_quant - 1;
                        holder.ll_add_remove.setVisibility(View.GONE);
                        holder.fab_add_cart.setVisibility(View.VISIBLE);
                    }
                    String qt = "" + cart_quant;
                    ((ItemListActivity)context).set_text(p,qt);
                    holder.tvQuantity.setText(String.valueOf(model.getQuantity()));
                }

            }
        });

        holder.fab_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.is_added() == 0) {
                    model.setIs_added(1);
                    String q = "" + 1;
                    model.setQuantity(1);
                    price = price + Double.parseDouble(model.getPrice());
                    String p = "" + price;
                    cart_quant = cart_quant + 1;
                    String qt = "" + cart_quant;
                    holder.ll_add_remove.setVisibility(View.VISIBLE);
                    holder.fab_add_cart.setVisibility(View.GONE);
                    holder.tvQuantity.setText(q);
                    ((ItemListActivity)context).set_text(p,qt);
                    //iA.set_text(p,qt);
                    Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Already added !!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.tvClickDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cv_child.getVisibility() == View.GONE){
                    holder.cv_child.setVisibility(View.VISIBLE);
                    /*TranslateAnimation animate = new TranslateAnimation(0,0,0,holder.cv_main.getHeight());
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    holder.cv_child.startAnimation(animate);*/
                    //holder.cv_child.animate().translationY(holder.cv_child.getHeight()).alpha(0.0f).setDuration(2000);;
                }
                else{
                    /*
                    TranslateAnimation animate = new TranslateAnimation(0,0,0,-holder.cv_main.getHeight());
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    holder.cv_child.startAnimation(animate);*/
                    //holder.cv_child.animate().translationY(-holder.cv_child.getHeight()).alpha(0.0f).setDuration(2000);;
                    holder.cv_child.setVisibility(View.GONE);
                }
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

        private final TextView tvItemName, tvPrice,tvQuantity, tvClickDesc, tvBrandName,tvDesc;
        //private final EditText etQuantity;
        private final ImageView iv_item,iv_add,iv_remove;
        private final FloatingActionButton fab_add_cart;
        private final LinearLayout ll_add_remove;
        private final CardView cv_child,cv_main;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            tvItemName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvBrandName = (TextView) itemView.findViewById(R.id.tv_brand_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            fab_add_cart = (FloatingActionButton) itemView.findViewById(R.id.fab_cart_add);
            //etQuantity = (EditText) itemView.findViewById(R.id.et_quantity);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity_item);
            tvClickDesc = (TextView) itemView.findViewById(R.id.tv_click_desc);
            iv_item = (ImageView) itemView.findViewById(R.id.iv_item);
            ll_add_remove = (LinearLayout) itemView.findViewById(R.id.ll_add_remove);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            iv_remove = (ImageView) itemView.findViewById(R.id.iv_remove);
            cv_child = (CardView) itemView.findViewById(R.id.card_list_item_desc);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_description);
            cv_main = (CardView) itemView.findViewById(R.id.cv_list_main);
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
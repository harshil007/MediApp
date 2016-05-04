package startup.com.mediapp;

/**
 * Created by Pursnani Kapil on 03-May-16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderAdapter.ItemViewHolder> {

    private List<SellerOrderItemInfo> ItemList;
    Context context;
    ClickListener clickListener;
    onLongListener onLongClick;



    public SellerOrderAdapter(Context context, List<SellerOrderItemInfo> ItemList) {
        this.ItemList = ItemList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    @Override
    public void onBindViewHolder(ItemViewHolder contactViewHolder,final int i) {
        SellerOrderItemInfo ci = ItemList.get(i);
        contactViewHolder.name.setText(ci.name);
        contactViewHolder.amount.setText(ci.amount);
        contactViewHolder.dat.setText(ci.date);
        contactViewHolder.arrivalTime.setText(ci.arrival_time);
        contactViewHolder.addr.setText(ci.address);
        contactViewHolder.product.setText(ci.product);
        contactViewHolder.quant.setText(ci.quntity);
        contactViewHolder.prize.setText(ci.prize);



    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.seller_order, viewGroup, false);

        return new ItemViewHolder(itemView);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        protected TextView oid;
        protected TextView name;
        protected TextView amount;
        protected TextView dat;
        protected TextView arrivalTime;
        protected TextView addr;
        protected TextView product;
        protected TextView quant;
        protected TextView prize;




        public ItemViewHolder(View v) {
            super(v);
            oid =  (TextView) v.findViewById(R.id.oid);
            name =  (TextView) v.findViewById(R.id.oid);
            amount =  (TextView) v.findViewById(R.id.oid);
            dat =  (TextView) v.findViewById(R.id.oid);
            arrivalTime =  (TextView) v.findViewById(R.id.oid);
            addr =  (TextView) v.findViewById(R.id.oid);
            product =  (TextView) v.findViewById(R.id.oid);
            quant =  (TextView) v.findViewById(R.id.oid);
            prize =  (TextView) v.findViewById(R.id.oid);

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


    public void animateTo(List<SellerOrderItemInfo> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<SellerOrderItemInfo> newModels) {
        for (int i = ItemList.size() - 1; i >= 0; i--) {
            final SellerOrderItemInfo model = ItemList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<SellerOrderItemInfo> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SellerOrderItemInfo model = newModels.get(i);
            if (!ItemList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SellerOrderItemInfo> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SellerOrderItemInfo model = newModels.get(toPosition);
            final int fromPosition = ItemList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SellerOrderItemInfo removeItem(int position) {
        final SellerOrderItemInfo model = ItemList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SellerOrderItemInfo model) {
        ItemList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SellerOrderItemInfo model = ItemList.remove(fromPosition);
        ItemList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}


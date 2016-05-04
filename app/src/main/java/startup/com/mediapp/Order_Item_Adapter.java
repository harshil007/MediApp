package startup.com.mediapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pursnani Kapil on 17-Apr-16.
 */
public class Order_Item_Adapter extends RecyclerView.Adapter<Order_Item_Adapter.ContactViewHolder>{

    private List<OrderItemInfo> contactList;
    Context context;


    public Order_Item_Adapter(Context context, List<OrderItemInfo> contactList) {
        this.contactList = contactList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        OrderItemInfo ci = contactList.get(i);
        contactViewHolder.oid.setText("ORDER ID : " + ci.oid);
        contactViewHolder.amount.setText("AMOUNT : " + ci.amount);
        contactViewHolder.status.setText("STATUS : " +ci.status);
        contactViewHolder.date.setText("DATE : " + ci.date);
        contactViewHolder.arrival_time.setText("ARRIVAL TIME : " + ci.arrival_time);
    }

    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.order_list_item, viewGroup, false);

        return new ContactViewHolder(itemView);
    }




    public class ContactViewHolder extends RecyclerView.ViewHolder {


        protected TextView oid;
        protected TextView amount;
        protected TextView status;
        protected TextView date;
        protected TextView arrival_time;



        public ContactViewHolder(View v) {
            super(v);
            oid = (TextView) v.findViewById(R.id.oid);
            amount = (TextView) v.findViewById(R.id.amount);
            status = (TextView) v.findViewById(R.id.status);
            date = (TextView) v.findViewById(R.id.date);
            arrival_time = (TextView) v.findViewById(R.id.arrival_time);


        }

    }


    public void animateTo(List<OrderItemInfo> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<OrderItemInfo> newModels) {
        for (int i = contactList.size() - 1; i >= 0; i--) {
            final OrderItemInfo model = contactList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<OrderItemInfo> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final OrderItemInfo model = newModels.get(i);
            if (!contactList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<OrderItemInfo> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final OrderItemInfo model = newModels.get(toPosition);
            final int fromPosition = contactList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public OrderItemInfo removeItem(int position) {
        final OrderItemInfo model = contactList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, OrderItemInfo model) {
        contactList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final OrderItemInfo model = contactList.remove(fromPosition);
        contactList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}


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
        contactViewHolder.oid.setText(ci.oid);
        contactViewHolder.amount.setText(ci.amount);
        contactViewHolder.status.setText(ci.status);
        contactViewHolder.date.setText(ci.date);
        contactViewHolder.arrival_time.setText(ci.arrival_time);
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

}


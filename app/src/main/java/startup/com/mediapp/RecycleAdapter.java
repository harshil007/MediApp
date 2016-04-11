package startup.com.mediapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 07/03/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<String> allNames;
    private List<String> filteredNames;
    //private StringFilter filter;
    private ArrayList<String> name_array;
    Context context;
    private final List<ItemModel> mModels;
    ClickListener clickListener;
    onLongListener onLongClick;


    public RecycleAdapter(Context context, List<ItemModel> models) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        /*name_array = name;
        this.allNames = new ArrayList<String>();
        allNames.addAll(name);
        this.filteredNames = new ArrayList<String>();
        filteredNames.addAll(allNames);
        this.context = context;
        //getFilter();*/
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemModel model = mModels.get(position);
        int q;
        //holder.bind(model);
        holder.tvItemName.setText(model.getName());
        holder.tvPrice.setText(model.getPrice()+" ₹");
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setQuantity(model.getQuantity()+1);
                holder.tvQuantity.setText(model.getQuantity());
            }
        });

       holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getQuantity()!=0){
                    model.setQuantity(model.getQuantity()-1);
                    holder.tvQuantity.setText(model.getQuantity());
                }

            }
        });

        Glide.with(context)
                .load(model.getImg_url())
                .placeholder(R.drawable.load)
                .crossFade()
                .into(holder.iv_item);
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


    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void setonLongListener(onLongListener onLongClick){this.onLongClick=onLongClick;}

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        private final TextView tvItemName,tvPrice,tvQuantity,tvClickDesc;
        //private final EditText etQuantity;
        private final ImageView iv_item,iv_add,iv_remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            //etQuantity = (EditText) itemView.findViewById(R.id.et_quantity);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity_amt);
            tvClickDesc = (TextView) itemView.findViewById(R.id.tv_click_desc);
            iv_item = (ImageView) itemView.findViewById(R.id.iv_item);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            iv_remove = (ImageView) itemView.findViewById(R.id.iv_remove);

            iv_add.setOnClickListener(this);


        }

        public void bind(final ItemModel model) {

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
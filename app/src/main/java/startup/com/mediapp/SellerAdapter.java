package startup.com.mediapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 17/04/2016.
 */
public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.MyViewHolder>{


    private LayoutInflater inflater;
    private List<SellerModel> mModels;
    Context context;

    public SellerAdapter(Context context, List<SellerModel> mModels) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mModels = new ArrayList<>(mModels);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.seller_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SellerModel model = mModels.get(position);
        holder.tv_shop_name.setText(model.getName());
        holder.tv_shop_addr.setText(model.getLocality());
        Glide.with(context)
                .load(model.getImg_url())
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .centerCrop()
                .into(holder.iv_shop);

        holder.b_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellerSelectActivity)context).return_seller(model.getId(),model.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_shop,iv_star1,iv_star2,iv_star3,iv_star4,iv_star5;
        TextView tv_shop_name,tv_shop_addr;
        Button b_select;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_shop = (ImageView) itemView.findViewById(R.id.iv_seller_shop);
            iv_star1 = (ImageView) itemView.findViewById(R.id.iv_star1);
            iv_star2 = (ImageView) itemView.findViewById(R.id.iv_star2);
            iv_star3 = (ImageView) itemView.findViewById(R.id.iv_star3);
            iv_star4 = (ImageView) itemView.findViewById(R.id.iv_star4);
            iv_star5 = (ImageView) itemView.findViewById(R.id.iv_star5);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            tv_shop_addr = (TextView) itemView.findViewById(R.id.tv_shop_address);
            b_select = (Button) itemView.findViewById(R.id.b_select_shop);

        }
    }



    public void animateTo(List<SellerModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<SellerModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final SellerModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<SellerModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SellerModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SellerModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SellerModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SellerModel removeItem(int position) {
        final SellerModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SellerModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SellerModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    
}

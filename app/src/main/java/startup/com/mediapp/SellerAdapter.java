package startup.com.mediapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Harshil on 17/04/2016.
 */
public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.MyViewHolder>{


    public void SellerAdapter(Context context, List<SellerModel> sellerModels){

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
}

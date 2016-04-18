package startup.com.mediapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Harshil on 16/04/2016.
 */
public class SellerSelectActivity extends AppCompatActivity{

    TextView tvUserLoc;
    RecyclerView rv_seller;
    ImageView iv_change_loc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_list);

        tvUserLoc = (TextView) findViewById(R.id.tv_user_loc);
        rv_seller = (RecyclerView) findViewById(R.id.rv_seller_list);
        iv_change_loc = (ImageView) findViewById(R.id.iv_change_loc);

    }



}

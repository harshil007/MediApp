package startup.com.mediapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 12/04/2016.
 */
public class CartActivity extends AppCompatActivity implements View.OnClickListener{

    CartRecycleAdapter adapter;
    private List<ItemModel> items_list,final_order;
    Button b_add_more,b_checkout;
    TextView tv_cart_price,tv_cart_quant,tv_clear_cart,tv_seller_name,tv_delivery;
    ImageView iv_edit_seller,iv_edit_date;
    LinearLayout ll_delivery;
    RelativeLayout rl_delivery;
    RecyclerView rv;
    int cart_quant;
    float cart_price;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = (RecyclerView) findViewById(R.id.rc_cart_items);
        Bundle bundle = getIntent().getExtras();
        items_list = new ArrayList<>();
        items_list = bundle.getParcelableArrayList("orders_array");
        cart_price = bundle.getFloat("price");
        cart_quant = bundle.getInt("quantity");

        tv_cart_price = (TextView) findViewById(R.id.tv_cart_total);
        tv_cart_quant = (TextView) findViewById(R.id.tv_cart_quantity);
        tv_clear_cart = (TextView) findViewById(R.id.tv_clear_cart);
        tv_clear_cart.setOnClickListener(this);

        tv_delivery = (TextView) findViewById(R.id.tv_date_time);
        rl_delivery = (RelativeLayout) findViewById(R.id.rl_delivery_show);
        ll_delivery = (LinearLayout) findViewById(R.id.ll_delivery_select);
        iv_edit_date = (ImageView) findViewById(R.id.iv_change_date);
        iv_edit_date.setOnClickListener(this);

        tv_seller_name = (TextView) findViewById(R.id.tv_seller_name);
        iv_edit_seller = (ImageView) findViewById(R.id.iv_edit_seller);
        iv_edit_seller.setOnClickListener(this);

        tv_delivery.setText("Today, ASAP");

        b_add_more = (Button) findViewById(R.id.b_add_more);
        b_checkout = (Button) findViewById(R.id.b_checkout);
        b_add_more.setOnClickListener(this);
        b_checkout.setOnClickListener(this);


        set_text(""+cart_price,""+cart_quant);

        adapter = new CartRecycleAdapter(this,items_list,cart_price,cart_quant);



        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv.setLayoutManager(linearLayoutManager);



        //ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        //ItemTouchHelper helper = new ItemTouchHelper(callback);
        //helper.attachToRecyclerView(rv);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clear_cart:
                //Restart the ItemListActivity --- not done yet
                finish();
                break;
            case R.id.iv_change_date:
                rl_delivery.setVisibility(View.GONE);
                ll_delivery.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_edit_seller:

                break;
            case R.id.b_add_more:
                finish();
                break;
            case R.id.b_checkout:
                final_order = adapter.get_order();

                Intent intent = new Intent(CartActivity.this,OrderPlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("final_order",(ArrayList)final_order);
                bundle.putFloat("price",cart_price);
                bundle.putString("seller_id","s_951");
                bundle.putString("arrival_time",tv_delivery.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }



    public void set_text(String price,String quantity){
        cart_quant=Integer.parseInt(quantity);
        cart_price=Float.parseFloat(price);
        tv_cart_price.setText("₹ "+price);
        tv_cart_quant.setText(quantity);
    }
/*
    public void swipeRemove(int position){
        items_list.remove(position);
        adapter.animateTo(items_list);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        items_list=null;
        adapter=null;
    }

}

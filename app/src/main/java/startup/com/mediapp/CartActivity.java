package startup.com.mediapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 12/04/2016.
 */
public class CartActivity extends AppCompatActivity{

    RecycleAdapter adapter;
    private List<ItemModel> items_list;
    Button b_checkout,b_cancel;
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = (RecyclerView) findViewById(R.id.rc_cart_items_list);
        Bundle bundle = getIntent().getExtras();
        items_list = new ArrayList<>();
        items_list = bundle.getParcelableArrayList("orders_array");


        adapter = new RecycleAdapter(this,items_list);


        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        b_checkout = (Button) findViewById(R.id.b_checkout);
        b_cancel = (Button) findViewById(R.id.b_cancel_order);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

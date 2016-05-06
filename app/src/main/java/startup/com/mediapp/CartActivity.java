package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    Spinner sp_day,sp_time;
    String del_day,del_time;
    String SELLER_URL="http://mediapp.netai.net/select_seller.php";
    private RequestQueue mQueue;
    SharedPreferences prefs;
    String pincode,city;
    String[] s_id,s_name;
    String sel_seller_id,sel_seller_name;

    ProgressDialog progressDialog;

    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;

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



        prefs = getSharedPreferences(getString(R.string.preference_file_key),0);
        pincode = prefs.getString("pincode","382424");
        city = prefs.getString("city","Ahmedabad");

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

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

        del_day="Today";
        del_time="ASAP";
        tv_delivery.setText("Today, ASAP");
        sp_day = (Spinner) findViewById(R.id.spinner_delivery_day);
        sp_time = (Spinner) findViewById(R.id.spinner_delivery_time);

        final List<String> spinnerDay =  new ArrayList<String>();
        spinnerDay.add("Today");
        spinnerDay.add("Tommorow");
        spinnerDay.add("Day after Tommorow");




        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerDay);

        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_day.setAdapter(adapterDay);


        final String[] times = {"10:00 am","11:00 am","12:00 pm","1:00 pm","2:00 pm","3:00 pm","4:00 pm","5:00 pm","6:00 pm","7:00 pm"};
        List<String> spinnerTime =  new ArrayList<String>();
        spinnerTime = Arrays.asList(times);

        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerTime);

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_time.setAdapter(adapterTime);

        b_add_more = (Button) findViewById(R.id.b_add_more);
        b_checkout = (Button) findViewById(R.id.b_checkout);
        b_add_more.setOnClickListener(this);
        b_checkout.setOnClickListener(this);


        set_text(""+cart_price,""+cart_quant);

        adapter = new CartRecycleAdapter(this,items_list,cart_price,cart_quant);


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        selectSeller();


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

        sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                del_day = spinnerDay.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                del_time = times[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        //ItemTouchHelper helper = new ItemTouchHelper(callback);
        //helper.attachToRecyclerView(rv);


    }


    public void selectSeller(){

        JSONObject son = new JSONObject();
        try {
            son.put("pincode",pincode);
            son.put("city",city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(SELLER_URL, son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject flag = response.getJSONObject(0);
                    int success = flag.getInt("success");
                    if(success==1){
                        JSONArray s_id_array = response.getJSONArray(1);
                        JSONArray s_name_array = response.getJSONArray(2);
                        s_id = new String[s_id_array.length()];
                        s_name = new String[s_id_array.length()];
                        for(int i=0;i<s_id_array.length();i++){
                            s_id[i] = s_id_array.getString(i);
                            s_name[i] = s_name_array.getString(i);

                        }

                        Random r = new Random();
                        int x = r.nextInt(s_id_array.length());
                        sel_seller_name = s_name[x];
                        sel_seller_id = s_id[x];
                        tv_seller_name.setText(sel_seller_name);

                    }
                    else{
                        sel_seller_name="Acha Medical Store";
                        sel_seller_id="s_951";
                        tv_seller_name.setText(sel_seller_name);
                        Toast.makeText(CartActivity.this,"No sellers found in vicinity",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });

        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);

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
                Intent i = new Intent(getApplicationContext(),SellerSelectActivity.class);
                startActivityForResult(i,SUB_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.b_add_more:
                finish();
                break;
            case R.id.b_checkout:
                final_order = adapter.get_order();

                Intent intent = new Intent(getApplicationContext(),OrderPlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("final_order",(ArrayList)final_order);
                bundle.putFloat("price",cart_price);
                bundle.putString("seller_id",sel_seller_id);
                bundle.putString("arrival_time",del_day+", "+del_time);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SUB_ACTIVITY_REQUEST_CODE){
            String id = data.getExtras().getString("id");
            String name = data.getExtras().getString("name");
            sel_seller_name = name;
            sel_seller_id = id;
            tv_seller_name.setText(sel_seller_name);

        }
    }

    public void set_text(String price, String quantity){
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

package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.List;

/**
 * Created by Harshil on 03/05/2016.
 */
public class SellerOrdersActivity extends AppCompatActivity implements Order_Item_Adapter.ClickListener{

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    List<OrderItemInfo> result = new ArrayList<OrderItemInfo>();
    List<OrderItemInfo> mainlist = new ArrayList<OrderItemInfo>();
    Order_Item_Adapter ca;
    String url="http://mediapp.netai.net/seller_get_order.php";
    String url_cust="http://mediapp.netai.net/cust_get_details.php";
    ProgressDialog pDialog;
    private RequestQueue mQueue;
    SharedPreferences prefs;
    String seller_id;
    RelativeLayout rl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        prefs = getSharedPreferences(getString(R.string.preference_file_key),0);
        seller_id = prefs.getString("id","s_951");

        mainlist = new ArrayList<>();

        RecyclerView recList = (RecyclerView) findViewById(R.id.sellerorder);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ca = new Order_Item_Adapter(SellerOrdersActivity.this,mainlist);
        recList.setAdapter(ca);

        ca.setClickListener(this);

        fetch();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }


        };


    }

    private void fetch() {
        mainlist.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching orders...");
        pDialog.show();

        JSONObject son = new JSONObject();
        try {
            son.put("s_id",seller_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(url, son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray oid,amount,status,date,arrival_time,p_id,quant,c_id;
                try {
                    JSONArray flag = response.getJSONArray(0);

                    if(flag.getInt(0)==0){
                        Toast.makeText(SellerOrdersActivity.this,"No Orders!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(SellerOrdersActivity.this,"Orders!",Toast.LENGTH_LONG).show();
                        oid = response.getJSONArray(1);
                        amount = response.getJSONArray(2);
                        status = response.getJSONArray(3);
                        date = response.getJSONArray(4);
                        arrival_time = response.getJSONArray(5);
                        p_id = response.getJSONArray(6);
                        quant = response.getJSONArray(7);
                        c_id = response.getJSONArray(8);

                        for(int i=0;i<oid.length();i++){
                            OrderItemInfo ci = new OrderItemInfo();
                            ci.oid = oid.getString(i);
                            ci.amount = amount.getString(i);
                            ci.status = status.getString(i);
                            ci.date = date.getString(i);
                            ci.arrival_time = arrival_time.getString(i);
                            ci.p_id = p_id.getString(i);
                            ci.quantity = quant.getString(i);
                            ci.c_id = c_id.getString(i);
                            Log.d("ci",ci.toString());


                            result.add(ci);


                        }
                        mainlist = result;
                        ca.animateTo(mainlist);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @Override
    protected void onResume() {
        //fetch();
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,new IntentFilter("noti_order"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void itemClicked(View view, int position) {

        OrderItemInfo model=mainlist.get(position);

        get_cust(model);
    }

    private void get_cust(OrderItemInfo model) {

        final String oid = model.getOid();
        final String amount = model.getAmount();
        final String arr_time = model.getArrival_time();
        final String date = model.getDate();
        final String p_id = model.getP_id();
        final String quant = model.getQuantity();
        final String c_id = model.getC_id();

        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Getting info...");
        pDialog.show();

        JSONObject son = new JSONObject();
        try {
            son.put("c_id",c_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(url_cust, son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    String c_name = response.getJSONArray(2).getString(0);

                    String addr_no = response.getJSONArray(4).getString(0);
                    String soc_name = response.getJSONArray(5).getString(0);
                    String loc = response.getJSONArray(6).getString(0);
                    String city = response.getJSONArray(7).getString(0);
                    String state = response.getJSONArray(8).getString(0);
                    String pincode = response.getJSONArray(9).getString(0);
                    String mob_no = response.getJSONArray(10).getString(0);

                    Intent i = new Intent(getApplicationContext(), SOrderDetailActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("oid",oid);
                    i.putExtra("amount",amount);
                    i.putExtra("arr_time",arr_time);
                    i.putExtra("date",date);
                    i.putExtra("p_id",p_id);
                    i.putExtra("quant",quant);
                    i.putExtra("c_id",c_id);
                    i.putExtra("c_name",c_name);
                    i.putExtra("addr_no",addr_no);
                    i.putExtra("soc_name",soc_name);
                    i.putExtra("loc",loc);
                    i.putExtra("city",city);
                    i.putExtra("pincode",pincode);
                    i.putExtra("mob_no",mob_no);
                    Log.i("test1","YO");
                    pDialog.dismiss();
                    startActivity(i);


                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(SellerOrdersActivity.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });

        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);


    }
}

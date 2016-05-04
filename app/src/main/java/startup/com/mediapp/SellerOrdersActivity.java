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
import android.view.MenuItem;

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
public class SellerOrdersActivity extends AppCompatActivity{

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    List<SellerOrderItemInfo> result = new ArrayList<SellerOrderItemInfo>();
    List<SellerOrderItemInfo> mainlist = new ArrayList<SellerOrderItemInfo>();
    SellerOrderAdapter ca;
    String url="http://mediapp.netai.net/get_seller_orders.php";
    ProgressDialog pDialog;
    private RequestQueue mQueue;
    SharedPreferences prefs;
    String seller_id;

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

        RecyclerView recList = (RecyclerView) findViewById(R.id.sellerorder);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ca = new SellerOrderAdapter(this,mainlist);
        recList.setAdapter(ca);

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
        pDialog.setMessage("Fetching sellers list...");
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
                JSONArray p_id,quant,total_amt,status,arr_time,name,mob_no,shop_no,society_name,locaity,city,pincode,date,o_id;
                try {
                    p_id = response.getJSONArray(0);
                    quant= response.getJSONArray(1);
                    total_amt = response.getJSONArray(2);
                    arr_time = response.getJSONArray(3);
                    name = response.getJSONArray(4);
                    mob_no=response.getJSONArray(5);
                    shop_no=response.getJSONArray(6);
                    society_name=response.getJSONArray(7);
                    locaity=response.getJSONArray(8);
                    city=response.getJSONArray(9);
                    pincode=response.getJSONArray(10);
                    date=response.getJSONArray(11);
                    o_id=response.getJSONArray(12);

                    for(int i=0;i<p_id.length();i++){
                        SellerOrderItemInfo model = new SellerOrderItemInfo();
                        model.setName(name.getString(i));
                        model.setAmount(total_amt.getString(i));
                        model.setArrival_time(arr_time.getString(i));
                        model.setDate(date.getString(i));
                        model.setMobile(mob_no.getString(i));
                        model.setAddress(shop_no.getString(i)+", "+society_name.getString(i)+
                                ",\n"+locaity.getString(i)+", "+city.getString(i)+",\n"+pincode.getString(i)
                        );
                        model.setOid(o_id.getString(i));
                        model.setPrize("");
                        model.setProduct(p_id.getString(i));
                        model.setQuntity(quant.getString(i));

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
}

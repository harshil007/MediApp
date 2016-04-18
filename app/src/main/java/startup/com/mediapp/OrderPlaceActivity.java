package startup.com.mediapp;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Harshil on 16/04/2016.
 */
public class OrderPlaceActivity extends AppCompatActivity {

    TextView tv_name, tv_num, tv_addr, tv_amt;
    ImageButton ib_change_addr;
    Button b_place_order;
    private List<ItemModel> final_order;
    private float total_amount = 0;
    private String seller_id, arrival_time;
    ProgressDialog pDialog;
    private RequestQueue mQueue;
    Toolbar toolbar;
    String url = "http://mediapp.netai.net/place_order.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirm);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        toolbar = (Toolbar) findViewById(R.id.toolbar_order);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_name = (TextView) findViewById(R.id.tv_checkout_name);
        tv_num = (TextView) findViewById(R.id.tv_checkout_num);
        tv_addr = (TextView) findViewById(R.id.tv_checkout_addr);
        tv_amt = (TextView) findViewById(R.id.tv_checkout_amt);



        ib_change_addr = (ImageButton) findViewById(R.id.ib_checkout_change_addr);
        b_place_order = (Button) findViewById(R.id.b_place_order);

        Bundle bundle = getIntent().getExtras();

        final_order = new ArrayList<>();
        final_order = bundle.getParcelableArrayList("final_order");
        total_amount = bundle.getFloat("price");
        seller_id = bundle.getString("seller_id");
        arrival_time = bundle.getString("arrival_time");

        tv_amt.setText("₹ "+total_amount);

        ib_change_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order();
            }
        });

    }

    private void place_order() {
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Placing your order...");
        pDialog.show();
        StringBuilder p_ids = new StringBuilder("");
        String total_quantity = "" + final_order.size();
        StringBuilder quants = new StringBuilder("");

        for (ItemModel model : final_order) {
            p_ids.append(model.getId() + ",");
            quants.append(model.getQuantity() + ",");
        }

        String ids = p_ids.toString();
        String quantitites = quants.toString();
        String status = "PLACED";
        Calendar c = Calendar.getInstance();
        String order_date = "" + c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);

        String order_id = "o_" + c.getTimeInMillis() + "c_104";     //Change this Line Bro..
        String c_id = "c_104";                                  //This one too
        String Arrival_time = arrival_time;

        JSONObject order_details = new JSONObject();
        try {
            order_details.put("order_id", order_id);
            order_details.put("p_id", ids);
            order_details.put("quantity", quantitites);
            order_details.put("total_amount", total_amount);
            order_details.put("order_status", status);
            order_details.put("order_date", order_date);
            order_details.put("arrival_time", arrival_time);
            order_details.put("c_id", c_id);
            order_details.put("s_id", seller_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jreq = new JsonObjectRequest(url, order_details, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json array response
                    // loop through each json objectString jsonRespons = response.toString();
                    int jsonResponse;
                    jsonResponse = response.getInt("success");

                    pDialog.dismiss();

                    if (jsonResponse == 1) {

                        Toast.makeText(getApplicationContext(),
                                "Order Placed!",
                                Toast.LENGTH_LONG).show();
                        //Intent i = new Intent(signup.this,PostSignupCust.class);
                        //i.putExtra("cid",cid);
                        //startActivity(i);
                        //finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Order cannot be placed! Try again",
                                Toast.LENGTH_LONG).show();

                    }
                    // Log.d("VolleyTest", person.toString());


                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    VolleyLog.d("OrderPlaceActivity", "ErrorJ: " + e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "ErrorJ: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.d("VolleyTest", e.getMessage());
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("OrderPlaceActivity", "ErrorV: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                pDialog.dismiss();
            }
        });


        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}

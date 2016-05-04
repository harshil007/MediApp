package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
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
    SharedPreferences sharedPref;
    String fetch_url = "http://mediapp.netai.net/CustomerDetails.php";
    String id,display_name,mobile,address;

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
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Getting details...");

        sharedPref=getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        id=sharedPref.getString("id","c_655");
        display_name = sharedPref.getString("name","Harshil Laheri");
        mobile=sharedPref.getString("mob_no","8238522541");
        String adr = sharedPref.getString("addr_no","18A")+", "+sharedPref.getString("soc_name","Nandanvan bunglows")+",\n"+
                sharedPref.getString("loc","Chandkheda")+", "+sharedPref.getString("city","Ahmedabad")+",\n"+sharedPref.getString("pincode","382424");
        address = adr;

        setDetails();

        //fetch_details();

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

    /*private void fetch_details() {
        pDialog.show();
        sharedPref = this.getSharedPreferences(this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String email = sharedPref.getString(this.getString(R.string.Email),"YO");
        String password = sharedPref.getString(this.getString(R.string.Password),"YO");

        JSONObject son = new JSONObject();
        try {
            Log.d("email",email);
            son.put("email",email);
            son.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(fetch_url, son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("Response: ",response.toString());
                Toast.makeText(OrderPlaceActivity.this,"Error in try",Toast.LENGTH_SHORT).show();

                try {
                    JSONArray id = response.getJSONArray(0);
                    JSONArray name = response.getJSONArray(1);
                    JSONArray image = response.getJSONArray(2);
                    JSONArray addr_id = response.getJSONArray(3);
                    //JSONArray addr_id_main = response.getJSONArray(4);
                    JSONArray addr_no = response.getJSONArray(4);
                    JSONArray soc_name = response.getJSONArray(5);
                    JSONArray loc = response.getJSONArray(6);
                    JSONArray city = response.getJSONArray(7);
                    JSONArray state = response.getJSONArray(8);
                    JSONArray pincode = response.getJSONArray(9);
                    JSONArray mob_no = response.getJSONArray(10);

                    String c_id = id.getString(0);
                    String user_name = name.getString(0);
                    display_name = user_name;
                    String mobile_num = mob_no.getString(0);
                    String house_num = addr_no.getString(0);
                    String society = soc_name.getString(0);
                    String locality = loc.getString(0);
                    String city_name = city.getString(0);
                    String pin = pincode.getString(0);

                    mobile = mobile_num;

                    address = display_name+"\n"+house_num+", "+society+",\n"+locality+", "+city_name+"\n"+pin;
                    setDetails();



                } catch (JSONException e) {
                    Toast.makeText(OrderPlaceActivity.this,"Error in catch",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(OrderPlaceActivity.this,"Error in response",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

       // jreq.setRetryPolicy(new DefaultRetryPolicy(
              //  5000,
              //  DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
               // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);



    }*/

    private void setDetails() {
        tv_addr.setText(address);
        tv_name.setText(display_name);
        tv_num.setText(mobile);
    }

    private void place_order() {

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

        final String ids = p_ids.toString();
        String quantitites = quants.toString();
        String status = "PLACED";
        Calendar c = Calendar.getInstance();
        String order_date = "" + c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);

        String order_id = "o_" + c.getTimeInMillis() + id;     //Change this Line Bro..
        String c_id = id;                                  //This one too
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
                        Intent intent = new Intent(OrderPlaceActivity.this, MainCategory.class);
                        intent.putExtra("name",display_name);
                        intent.putExtra("email",sharedPref.getString("email","harshil.laheri@gmail.com"));
                        intent.putExtra("img_url",sharedPref.getString("img_url",""));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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

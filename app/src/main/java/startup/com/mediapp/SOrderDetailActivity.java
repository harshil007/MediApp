package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.Arrays;
import java.util.List;

/**
 * Created by Harshil on 06/05/2016.
 */
public class SOrderDetailActivity extends AppCompatActivity{

    String product;
    String quant;
    String oid,c_name,amt,date,arr_time,address,contact;
    String url="http://mediapp.netai.net/fetchitemdetails.php";

    TextView tv_oid,tv_name,tv_amt,tv_date,tv_arr_time,tv_product,tv_quant,tv_price,tv_addr,tv_contact;

    ProgressDialog pDialog;
    private RequestQueue mQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_od);
        setSupportActionBar(toolbar);
        Intent e = getIntent();
        oid = e.getExtras().getString("oid");
        c_name = e.getExtras().getString("c_name");
        amt = e.getExtras().getString("amount");
        date = e.getExtras().getString("date");
        arr_time = e.getExtras().getString("arr_time");
        address = e.getExtras().getString("addr_no")+", "+e.getExtras().getString("soc_name")+"\n"+
                e.getExtras().getString("loc")+", "+e.getExtras().getString("city")+"\n"+
                e.getExtras().getString("pincode");
        contact = e.getExtras().getString("mob_no");
        product = e.getExtras().getString("p_id");
        quant = e.getExtras().getString("quant");

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        Log.i("test2","YO1");

        tv_oid = (TextView) findViewById(R.id.oid);
        tv_name = (TextView) findViewById(R.id.name);
        tv_amt = (TextView) findViewById(R.id.amount);
        tv_date = (TextView) findViewById(R.id.date);
        tv_arr_time = (TextView) findViewById(R.id.arrival_time);
        tv_product = (TextView) findViewById(R.id.pname);
        tv_quant = (TextView) findViewById(R.id.quant);
        tv_price = (TextView) findViewById(R.id.price);
        tv_addr = (TextView) findViewById(R.id.addr);
        tv_contact = (TextView) findViewById(R.id.contact);

        tv_oid.setText("ORDER No#"+oid);
        tv_name.setText("Name: "+c_name);
        tv_amt.setText("Total amount: "+"₹ "+amt);
        tv_date.setText("Order date: "+date);
        tv_arr_time.setText("Delivery time: "+arr_time);
        tv_addr.setText(address);
        tv_contact.setText(contact);

        get_prods();

    }

    public void get_prods(){
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching product details...");
        pDialog.show();

        product = product.substring(0,product.length()-1);
        quant = quant.substring(0,quant.length()-1);

        List<String> items = Arrays.asList(product.split("\\s*,\\s*"));
        final List<String> q = Arrays.asList(quant.split("\\s*,\\s*"));


        JSONArray jar = new JSONArray(items);


        JSONObject son = new JSONObject();
        try {
            son.put("pid",jar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jreq = new JsonArrayRequest(url, son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray pname,pprice;
                try {
                    pname=response.getJSONArray(1);
                    pprice=response.getJSONArray(2);

                    StringBuilder sbname = new StringBuilder();
                    StringBuilder sbquant = new StringBuilder();
                    StringBuilder sbprice = new StringBuilder();
                    for(int i=0;i<pname.length();i++){
                        sbname.append(pname.getString(i));
                        sbname.append("\n");
                        sbquant.append(q.get(i).toString());
                        sbquant.append("\n");
                        sbprice.append("₹ "+pprice.getString(i));
                        sbprice.append("\n");
                    }
                    pDialog.dismiss();
                    tv_product.setText(sbname);
                    tv_quant.setText(sbquant);
                    tv_price.setText(sbprice);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(SOrderDetailActivity.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });

        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);

    }


}

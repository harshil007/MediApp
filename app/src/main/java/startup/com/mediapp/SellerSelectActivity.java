package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 16/04/2016.
 */
public class SellerSelectActivity extends AppCompatActivity{

    TextView tvUserLoc;
    RecyclerView rv_seller;
    ImageView iv_change_loc;
    public final static int SUCCESS_RETURN_CODE = 1;
    ProgressDialog pDialog;
    SellerAdapter adapter;
    private List<SellerModel> seller_list;
    String url = "http://mediapp.netai.net/get_sellers.php";
    private RequestQueue mQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_select);


        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        seller_list = new ArrayList<>();

        tvUserLoc = (TextView) findViewById(R.id.tv_user_loc);
        rv_seller = (RecyclerView) findViewById(R.id.rv_seller_list);
        iv_change_loc = (ImageView) findViewById(R.id.iv_change_loc);

        adapter = new SellerAdapter(this,seller_list);

        rv_seller.setItemAnimator(new DefaultItemAnimator());
        rv_seller.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_seller.setAdapter(adapter);
        rv_seller.setLayoutManager(new LinearLayoutManager(this));


        get_sellers();


    }

    private void get_sellers() {
        seller_list.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching sellers list...");
        pDialog.show();

        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray sid,name,image_url,locality;
                try {
                    sid = response.getJSONArray(0);
                    name = response.getJSONArray(1);
                    image_url = response.getJSONArray(2);
                    locality = response.getJSONArray(3);
                    Log.i("Response: ",response.toString());
                    for(int i=0;i<sid.length();i++){
                        SellerModel model = new SellerModel(sid.getString(i),name.getString(i),image_url.getString(i),locality.getString(i));
                        seller_list.add(model);
                    }
                    adapter.animateTo(seller_list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.animateTo(seller_list);
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();
                finish();
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
        super.onBackPressed();
        Intent i = new Intent();
        i.putExtra("id","s_951");
        i.putExtra("name","Acha Medical Store");
        setResult(SUCCESS_RETURN_CODE, i);
        finish();
    }

    public void return_seller(String id, String name){
        Intent i = new Intent();
        i.putExtra("id",id);
        i.putExtra("name",name);
        setResult(SUCCESS_RETURN_CODE, i);
        finish();
    }



}

package startup.com.mediapp;

/**
 * Created by Harshil on 17/02/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainCategory extends AppCompatActivity implements MainCategoryAdapter.ClickListener{

    List<MainCategoryInfo> result = new ArrayList<MainCategoryInfo>();
    String[] category_img;

    ProgressDialog pDialog;
    private RequestQueue mQueue;
    private final String fetch_url = "http://mediapp.netai.net/FetchMainCategoryItems.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setHasFixedSize(true);

        category_img = new String[7];
        category_img[0]="http://mediapp.netai.net/Images/MainCategory/category_0.png";
        category_img[1]="http://mediapp.netai.net/Images/MainCategory/category_1.png";
        category_img[2]="http://mediapp.netai.net/Images/MainCategory/category_2.png";
        category_img[3]="http://mediapp.netai.net/Images/MainCategory/category_3.png";
        category_img[4]="http://mediapp.netai.net/Images/MainCategory/category_4.png";
        category_img[5]="http://mediapp.netai.net/Images/MainCategory/category_5.png";
        category_img[6]="http://mediapp.netai.net/Images/MainCategory/category_6.png";

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        fetch_items();
        MainCategoryAdapter ca = new MainCategoryAdapter(this,result);
        recList.setAdapter(ca);
        ca.setClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void fetch_items(){
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching product list...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, fetch_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int len = response.length();
                JSONArray id,name,subcategory,description,img_url;
                try {
                    id = response.getJSONArray(0);
                    name = response.getJSONArray(1);
                    subcategory = response.getJSONArray(2);
                    description = response.getJSONArray(3);
                    img_url = response.getJSONArray(4);

                    Log.d("Success", response.toString());

                    for (int i=0; i < len; i++) {
                        MainCategoryInfo ci = new MainCategoryInfo();
                        ci.name = name.getString(i);
                        ci.imgsrc=category_img[i];
                        //ci.imgsrc = img_url.getString(i);
                        //ci.imgsrc="http://mediapp.netai.net/Images/SubCategory/HealthCare Devices/RCD.jpg";
                        ci.id = id.getInt(i);
                        ci.description = description.getString(i);
                        ci.subcategory = subcategory.getString(i);

                        result.add(ci);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    return;
                }


                pDialog.dismiss();
                Toast.makeText(MainCategory.this,"Success1",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.e("Error : ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(MainCategory.this,"Connection error",Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(req);
    }



    @Override
    public void itemClicked(View view, int position) {
        TextView tv_category = (TextView) view.findViewById(R.id.title);
        String category = tv_category.getText().toString();
        Intent i = new Intent(MainCategory.this,ItemListActivity.class);
        i.putExtra("category",category);
        i.putExtra("sub_category","nope");
        startActivity(i);
    }
}
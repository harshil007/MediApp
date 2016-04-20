package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategory extends AppCompatActivity implements SubCategoryAdapter.ClickListener{



    protected String title[];
    protected String category_img[];
    int len;
    String category;
    List<SubCategoryItemInfo> result = new ArrayList<SubCategoryItemInfo>();
    List<SubCategoryItemInfo> mainlist = new ArrayList<SubCategoryItemInfo>();
    SubCategoryAdapter ca;

    ProgressDialog pDialog;
    private RequestQueue mQueue;
    private final String fetch_url = "http://mediapp.netai.net/FetchSubCategory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        category = b.getString("category");

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        RecyclerView recList = (RecyclerView) findViewById(R.id.my_recycler_view);
        //GridLayoutManager gm = new GridLayoutManager(this,2);
        StaggeredGridLayoutManager gm = new StaggeredGridLayoutManager(2,1);
        recList.setLayoutManager(gm);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ca = new SubCategoryAdapter(this,mainlist);
        recList.setAdapter(ca);

        title = new String[7];
        category_img = new String[7];

        fetch_items();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        JSONObject fetch = new JSONObject();
        try{
            fetch.put("category",category);

        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, fetch_url, fetch, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                len = response.length();
                JSONArray id,name,img_url;
                try {

                    name = response.getJSONArray(1);
                    id = response.getJSONArray(0);
                    img_url = response.getJSONArray(2);
                    title = new String[len];
                    category_img = new String[len];
                    Log.d("Success", response.toString());

                    for (int i=0; i < len; i++) {

                        SubCategoryItemInfo ci = new SubCategoryItemInfo();
                        ci.id = id.getInt(i);
                        ci.name = name.getString(i);
                        ci.imgsrc = img_url.getString(i);
                        result.add(ci);

                    }

                    mainlist = result;
                    ca.animateTo(mainlist);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    return;
                }


                pDialog.dismiss();
                Toast.makeText(SubCategory.this,"Success",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.e("Error : ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(SubCategory.this,"Connection error",Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(req);
    }




    private List<SubCategoryItemInfo> createList(int size) {

        List<SubCategoryItemInfo> result = new ArrayList<SubCategoryItemInfo>();
        for (int i=0; i < size; i++) {
            SubCategoryItemInfo sci = new SubCategoryItemInfo();
            sci.name = title[i];
            sci.imgsrc = category_img[i];

            result.add(sci);

        }

        return result;
    }

    @Override
    public void itemClicked(View view, int position) {
        TextView tv_category = (TextView) view.findViewById(R.id.title);
        String subcategory = tv_category.getText().toString();

            Intent i = new Intent(SubCategory.this, ItemListActivity.class);
            i.putExtra("category", category);
            i.putExtra("subcategory",subcategory);
            startActivity(i);


    }

}

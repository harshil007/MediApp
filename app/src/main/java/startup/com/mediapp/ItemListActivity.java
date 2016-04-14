package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

/**
 * Created by Harshil on 11/04/2016.
 */
public class ItemListActivity extends AppCompatActivity{

    ImageView iv_search,iv_cart;
    TextView tv_title;
    Toolbar toolbar;
    RecyclerView rv;
    RecycleAdapter adapter;
    private ArrayList<ItemModel> items_list;
    ProgressDialog pDialog;
    private final String fetch_url = "http://mediapp.netai.net/fetch_items.php";
    private String category,sub_category;
    private int sb;
    private RequestQueue mQueue;
    public List<ItemModel> orderCartList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.items_list_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        toolbar = (Toolbar) findViewById(R.id.item_list_toolbar);
        rv = (RecyclerView) findViewById(R.id.rc_items_list);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);

        category = getIntent().getExtras().getString("category");
        sub_category = getIntent().getExtras().getString("sub_category");
        if(sub_category.equals("nope")){
            sb=0;
        }
        else{
            sb=1;
        }

        items_list=new ArrayList<>();

        /*ItemModel i = new ItemModel();
        i.setId("p000");
        i.setName("Demo");
        i.setPrice("30");
        i.setQuantity(0);
        i.setImg_url(""+R.drawable.icon_leaf);
        i.setDescription("Demo");

        items_list.add(i);*/





        adapter = new RecycleAdapter(this,items_list);

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fetch_items();



        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCartList = new ArrayList<ItemModel>();
                orderCartList=null;
                orderCartList = adapter.setOrders();
                Intent intent = new Intent(ItemListActivity.this,CartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("orders_array",(ArrayList)orderCartList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }


    private void fetch_items(){

        Toast.makeText(ItemListActivity.this,"Success",Toast.LENGTH_SHORT).show();
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching product list...");
        pDialog.show();

        JSONObject fetch = new JSONObject();
        try{
            fetch.put("category",category);
            fetch.put("sub_category",sub_category);
            fetch.put("sb",sb);

        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, fetch_url, fetch, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int len = response.length();
                JSONArray id,name,price,description,img_url;
                try {
                    id = response.getJSONArray(0);
                    name = response.getJSONArray(1);
                    price = response.getJSONArray(2);
                    description = response.getJSONArray(3);
                    img_url = response.getJSONArray(4);

                    for(int i=0;i<id.length();i++){
                        ItemModel item = new ItemModel();
                        item.setQuantity(0);
                        item.setId(id.getString(i));
                        item.setName(name.getString(i));
                        item.setPrice(price.getString(i));
                        item.setDescription(description.getString(i));
                        item.setImg_url(img_url.getString(i));
                        items_list.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ItemListActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    return;
                }

                adapter.animateTo(items_list);

                pDialog.dismiss();
                Toast.makeText(ItemListActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.e("Error : ",error.getMessage());
                error.printStackTrace();
                Toast.makeText(ItemListActivity.this,"Connection error",Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(req);
    }




    private List<ItemModel> filter(List<ItemModel> models, String query) {
        query = query.toLowerCase();

        final List<ItemModel> filteredModelList = new ArrayList<>();
        for (ItemModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}

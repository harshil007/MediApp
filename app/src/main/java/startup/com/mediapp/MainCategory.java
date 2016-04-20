package startup.com.mediapp;

/**
 * Created by Harshil on 17/02/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
public class MainCategory extends AppCompatActivity implements MainCategoryAdapter.ClickListener,NavigationView.OnNavigationItemSelectedListener,
        FeedbackFragment.OnFragmentInteractionListener,OrderFragment.OnFragmentInteractionListener2 {

    List<MainCategoryInfo> result = new ArrayList<MainCategoryInfo>();
    List<MainCategoryInfo> mainlist = new ArrayList<MainCategoryInfo>();
    String[] category_img;
    String[] title;
    int len;

    ImageView pp;
    TextView name, email;
    String ppurl;
    MainCategoryAdapter main_adapter;

    LinearLayout ll_refresh;

    ProgressDialog pDialog;
    private RequestQueue mQueue;
    private final String fetch_url = "http://mediapp.netai.net/FetchMainCategoryItems.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        pp = (ImageView) headerLayout.findViewById(R.id.profilepic);
        name = (TextView) headerLayout.findViewById(R.id.nam);
        email = (TextView) headerLayout.findViewById(R.id.emai);

        ll_refresh = (LinearLayout) findViewById(R.id.ll_refresh_category);


        ll_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetch_items();
            }
        });

        name.setText(getIntent().getExtras().getString("name"));
        email.setText(getIntent().getExtras().getString("email"));
        ppurl = getIntent().getExtras().getString("img_url");

      Glide.with(getApplicationContext())
                .load(ppurl)
                .fitCenter()
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .into(pp);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.hasFixedSize();

        main_adapter = new MainCategoryAdapter(this,mainlist);
        recList.setAdapter(main_adapter);
        main_adapter.setClickListener(this);


        category_img = new String[7];
        category_img[0]="R.drawable.category_0";
        category_img[1]="R.drawable.category_1";
        category_img[2]="R.drawable.category_2";
        category_img[3]="R.drawable.category_3";
        category_img[4]="R.drawable.category_4";
        category_img[5]="R.drawable.category_5";
        category_img[6]="R.drawable.category_6";

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();


        fetch_items();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }
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
        if (id == R.id.logout) {

            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.Flag), "false");
            editor.commit();

            Intent in = new Intent(this,LoginActivity.class);
            startActivity(in);

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

                len = response.length();
                JSONArray id,name,subcategory,description,img_url;
                try {
                    id = response.getJSONArray(0);
                    name = response.getJSONArray(1);
                    img_url = response.getJSONArray(2);
                    subcategory = response.getJSONArray(4);
                    description = response.getJSONArray(3);

                    title = new String[len];
                    category_img = new String[len];
                    Log.d("Success", response.toString());

                    for (int i=0; i < len; i++) {

                        //title[i]=name.getString(i);
                        //category_img[i]=img_url.getString(i);
                        MainCategoryInfo ci = new MainCategoryInfo();
                        ci.name = name.getString(i);
                        //ci.imgsrc=category_img[i];
                        ci.imgsrc = img_url.getString(i);
                        //.replace("\\","");
                        //ci.imgsrc="http://mediapp.netai.net/Images/SubCategory/HealthCare Devices/RCD.jpg";
                        ci.id = id.getInt(i);
                        ci.description = description.getString(i);
                        ci.subcategory = subcategory.getString(i);

                        result.add(ci);


                    }
                    ll_refresh.setVisibility(View.GONE);
                    mainlist = result;
                    main_adapter.animateTo(mainlist);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    return;
                }


                pDialog.dismiss();
                Toast.makeText(MainCategory.this,"Success",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_refresh.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                VolleyLog.e("Error : ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(MainCategory.this,"Connection error",Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(req);
    }


    private List<MainCategoryInfo> createList(int size) {

        List<MainCategoryInfo> result = new ArrayList<MainCategoryInfo>();
        for (int i = 0; i < size; i++) {
            MainCategoryInfo sci = new MainCategoryInfo();
            sci.name = title[i];
            sci.imgsrc = category_img[i];

            result.add(sci);

        }
        return result;
    }

    @Override
    public void itemClicked(View view, int position, String subcategory) {
        TextView tv_category = (TextView) view.findViewById(R.id.title);
        String category = tv_category.getText().toString();

        if(subcategory.equals("no")) {
            Intent i = new Intent(MainCategory.this, ItemListActivity.class);
            i.putExtra("category", category);
            i.putExtra("sub_category", "nope");
            startActivity(i);

        }

        else{

            Intent i = new Intent(MainCategory.this, SubCategory.class);
            i.putExtra("category", category);
            startActivity(i);

        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {


        } else if (id == R.id.notifications) {

            Fragment fragment = new NotificationFragment();
            Bundle args = new Bundle();
            //args.putInt(NotificationFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();


        } else if (id == R.id.cart) {

            //Show cart items here

        } else if (id == R.id.orders) {

            Fragment fragment = new OrderFragment();
            Bundle args = new Bundle();
            //args.putInt(NotificationFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();

        } else if (id == R.id.feedback) {


            Fragment fragment = new FeedbackFragment();
            Bundle args = new Bundle();
            //args.putInt(NotificationFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();



        } else if (id == R.id.profile) {

        } else if (id == R.id.settings) {

        }

        if (id == R.id.share){


            //create the send intent
            Intent shareIntent =
                    new Intent(android.content.Intent.ACTION_SEND);

//set the type
            shareIntent.setType("text/plain");

//add a subject
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "MediApp");

//build the body of the message to be shared
            String shareMessage ="Sharing MediApp";

//add the message
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    shareMessage);

//start the chooser for sharing
            startActivity(Intent.createChooser(shareIntent,
                    "Share Via"));


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onFragmentInteraction(Uri uri){};



    public void onFragmentInteraction2(Uri uri){};

}
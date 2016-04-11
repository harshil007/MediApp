package startup.com.mediapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BeautyProducts extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected String title[];
    protected String brand[];
    protected int category_img[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        title = new String[7];
        title[0]="Medicines";
        title[1]="Personal & Baby Care Products";
        title[2]="Women Care";
        title[3]="Vitamins & Supplements";
        title[4]="Health Care Devices";
        title[5]="Beauty Products";
        title[6]="Ayurvedic Medicines & Products";

        brand = new String[7];
        brand[0]="abc";
        brand[1]="def";
        brand[2]="ijk";
        brand[3]="mno";
        brand[4]="rst";
        brand[5]="uvw";
        brand[6]="xyz";

        category_img = new int[7];
        category_img[0]=R.drawable.category_0;
        category_img[1]=R.drawable.category_1;
        category_img[2]=R.drawable.category_2;
        category_img[3]=R.drawable.category_3;
        category_img[4]=R.drawable.category_4;
        category_img[5]=R.drawable.category_5;
        category_img[6]=R.drawable.category_6;

        // specify an adapter (see also next example)
        mAdapter = new BeautyProductsAdapter(this,createList(7));
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<BeautyProductInfo> createList(int size) {

        List<BeautyProductInfo> result = new ArrayList<BeautyProductInfo>();
        for (int i=0; i < size; i++) {
            BeautyProductInfo bpi = new BeautyProductInfo();
            bpi.Item_name = title[i];
            bpi.imgsrc = category_img[i];
            bpi.Brand = brand[i];

            result.add(bpi);

        }

        return result;
    }

}

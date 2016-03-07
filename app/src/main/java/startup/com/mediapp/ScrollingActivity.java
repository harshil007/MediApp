package startup.com.mediapp;

/**
 * Created by Harshil on 17/02/2016.
 */
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    String title[];
    int category_img[];
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


        title = new String[7];
        title[0]="Medicines";
        title[1]="Personal & Baby Care Products";
        title[2]="Women Care";
        title[3]="Vitamins & Supplements";
        title[4]="Health Care Devices";
        title[5]="Beauty Products";
        title[6]="Ayurvedic Medicines & Products";

        category_img = new int[7];
        category_img[0]=R.drawable.category_0;
        category_img[1]=R.drawable.category_1;
        category_img[2]=R.drawable.category_2;
        category_img[3]=R.drawable.category_3;
        category_img[4]=R.drawable.category_4;
        category_img[5]=R.drawable.category_5;
        category_img[6]=R.drawable.category_6;



        ContactAdapter ca = new ContactAdapter(this,createList(7));
        recList.setAdapter(ca);

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

    private List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i=0; i < size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.name = title[i];
            ci.imgsrc = category_img[i];

            result.add(ci);

        }

        return result;
    }
}
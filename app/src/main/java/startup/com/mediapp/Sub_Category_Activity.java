package startup.com.mediapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by Harshil on 29/01/2016.
 */
public class Sub_Category_Activity extends Activity{


    GridView mygrid;
    public static final String names[] = {"Bad Ass", "Smart Ass", "Great Ass"};
    public static final int ids[] = {R.drawable.d1,R.drawable.d1,R.drawable.d1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sub_category_main);
        setup();
    }

    private void setup(){

        mygrid = (GridView) findViewById(R.id.grid_sub_category);
        BaseAdapter myadapter = new CustomGrid(Sub_Category_Activity.this,names,ids);
        mygrid.setAdapter(myadapter);
    }


}
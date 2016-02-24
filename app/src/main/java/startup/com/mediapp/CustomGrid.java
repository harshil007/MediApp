package startup.com.mediapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Harshil on 02/02/2016.
 */
public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] names;
    private final int[] Imageid;

    public CustomGrid(Context c,String[] names,int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.names = names;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.sub_category_list_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.tv1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.iv1);
            textView.setText(names[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
            TextView textView = (TextView) grid.findViewById(R.id.tv1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.iv1);
            textView.setText(names[position]);
            imageView.setImageResource(Imageid[position]);
        }

        return grid;
    }

}

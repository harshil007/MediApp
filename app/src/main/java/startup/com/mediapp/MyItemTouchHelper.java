package startup.com.mediapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Harshil on 14/04/2016.
 */
public class MyItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecycleAdapter mRecycleAdapter;

    public MyItemTouchHelper(RecycleAdapter recycleAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mRecycleAdapter = recycleAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: Not implemented here
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove item
        CartActivity ca = new CartActivity();
        mRecycleAdapter.remove(viewHolder.getAdapterPosition());
    }
}
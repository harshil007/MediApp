package startup.com.mediapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ItemViewHolder> {

    private List<SubCategoryItemInfo> ItemList;
    Context context;
    ClickListener clickListener;
    onLongListener onLongClick;



    public SubCategoryAdapter(Context context, List<SubCategoryItemInfo> ItemList) {
        this.ItemList = ItemList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    @Override
    public void onBindViewHolder(ItemViewHolder contactViewHolder,final int i) {
        SubCategoryItemInfo ci = ItemList.get(i);
        contactViewHolder.title.setText(ci.name);

        //contactViewHolder.img_category.setImageResource(ci.imgsrc);
       Glide.with(context)
                .load(ci.imgsrc)
                        //.fitCenter()
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .into(contactViewHolder.img_category);



    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.sub_category_item, viewGroup, false);

        return new ItemViewHolder(itemView);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        protected TextView title;
        protected ImageView img_category;


        public ItemViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            img_category = (ImageView) v.findViewById(R.id.img);

        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.itemClicked(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(onLongClick!=null){
                onLongClick.itemLongClicked(v,getPosition());
            }

            return false;
        }


    }

    public interface ClickListener{

        void itemClicked(View view, int position);
    }

    public interface onLongListener{
        void itemLongClicked(View view, int position);
    }


    public void animateTo(List<SubCategoryItemInfo> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<SubCategoryItemInfo> newModels) {
        for (int i = ItemList.size() - 1; i >= 0; i--) {
            final SubCategoryItemInfo model = ItemList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<SubCategoryItemInfo> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SubCategoryItemInfo model = newModels.get(i);
            if (!ItemList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SubCategoryItemInfo> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SubCategoryItemInfo model = newModels.get(toPosition);
            final int fromPosition = ItemList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SubCategoryItemInfo removeItem(int position) {
        final SubCategoryItemInfo model = ItemList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SubCategoryItemInfo model) {
        ItemList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SubCategoryItemInfo model = ItemList.remove(fromPosition);
        ItemList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}

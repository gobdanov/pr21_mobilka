package domains.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr19_20_mobilka.R;

import java.util.List;

import domains.callbacks.OnTabClickListener;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    List<domains.models.MenuItem> Items;
    Integer SelectPosition = 0;
    public OnTabClickListener listener;
    public MenuAdapter(List<domains.models.MenuItem> items, OnTabClickListener listener){
        Items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu,parent, false);
        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Title.setText(Items.get(position).Title);
        holder.Image.setImageResource(Items.get(position).IdDrawable);

        Integer SelectColor = position == SelectPosition ?
                Color.parseColor("#1a5fee") :
                Color.parseColor("#B8C1CC") ;

        holder.Title.setTextColor(SelectColor);
        holder.Image.setColorFilter(SelectColor);

        holder.Parent.setOnClickListener(v ->{
            notifyItemChanged(SelectPosition);

            SelectPosition = position;

            notifyItemChanged(SelectPosition);

            if (listener != null ) listener.OnTabClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout Parent;
        ImageView Image;
        TextView Title;

        public ViewHolder(@NonNull View ItemView){
            super(ItemView);

            Image = ItemView.findViewById(R.id.imageView);
            Title = ItemView.findViewById(R.id.textView);

            Parent = (LinearLayout) Image.getParent();
        }
    }
}

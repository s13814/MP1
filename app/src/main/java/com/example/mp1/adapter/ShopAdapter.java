package com.example.mp1.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp1.DB.Shop;
import com.example.mp1.R;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<Shop> shopList;
    private OnItemClickListener listener;


    public ShopAdapter() {
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item_shop, parent, false);

        return new ShopAdapter.ShopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        if(shopList != null) {
            Shop s = shopList.get(position);
            holder.tvShopName.setText(s.getName());
            holder.tvDesc.setText(s.getDescription());
            holder.tvRadius.setText(String.valueOf(s.getRadius()));
            holder.tvLatLng.setText(String.valueOf(s.getLocation()));
        } else{
            holder.tvShopName.setText("None");
        }
    }

    @Override
    public int getItemCount() {
        if(shopList != null)
            return shopList.size();
        else
            return 0;
    }

    public void setShops(List<Shop> shops){
        this.shopList = shops;
        notifyDataSetChanged();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tvShopName;
        TextView tvDesc;
        TextView tvRadius;
        TextView tvLatLng;

        ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvRadius = itemView.findViewById(R.id.tv_radius);
            tvLatLng = itemView.findViewById(R.id.tv_LatLng);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(shopList.get(position));
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Options");
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit");
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Delete");
        }
    }

    public Shop getShopAtIndex(int index){
        return shopList.get(index);
    }

    public interface OnItemClickListener{
        void onItemClick(Shop product);
    }

    public void setOnItemClickListener(ShopAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}


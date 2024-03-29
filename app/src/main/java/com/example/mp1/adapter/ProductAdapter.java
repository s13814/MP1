package com.example.mp1.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp1.DB.Product;
import com.example.mp1.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> list;
    private OnItemClickListener listener;

    public ProductAdapter(){
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);

        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if(list != null) {
            Product p = list.get(position);
            holder.tvName.setText(p.getProductName());
            holder.tvPrice.setText(String.valueOf(p.getPrice()));
            if (p.getBought())
                holder.cbBought.setChecked(true);
            else
                holder.cbBought.setChecked(false);
        } else{
            holder.tvName.setText("None");
        }
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        else
            return 0;
    }

    public void setProducts(List<Product> products){
        this.list = products;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tvName;
        TextView tvPrice;
        CheckBox cbBought;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            cbBought = itemView.findViewById(R.id.cb_bought);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(list.get(position));
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

    public Product getProductAtIndex(int index){
        return list.get(index);
    }

    public interface OnItemClickListener{
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

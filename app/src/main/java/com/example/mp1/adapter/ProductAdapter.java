package com.example.mp1.adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp1.DB.Product;
import com.example.mp1.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> list;
    private Context context;
    private OnItemClickListener listener;

    public ProductAdapter( Context context){
        this.context = context;
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

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvPrice;
        CheckBox cbBought;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            cbBought = itemView.findViewById(R.id.cb_bought);

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
    }

    public interface OnItemClickListener{
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

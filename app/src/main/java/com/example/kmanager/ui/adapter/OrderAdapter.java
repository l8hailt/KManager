package com.example.kmanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmanager.databinding.ItemOrderBinding;
import com.example.kmanager.db.entity.OrderEntity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private List<OrderEntity> orders;
    private OnOrderClickListener listener;

    public OrderAdapter(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding binding = ItemOrderBinding.inflate(inflater);
        return new OrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.bind(orders.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public static class OrderHolder extends RecyclerView.ViewHolder {

        private ItemOrderBinding binding;

        public OrderHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderEntity orderEntity, OnOrderClickListener listener) {
//            binding.tvOrderName.setText(orderEntity.getName());
//            binding.tvPrice.setText(String.valueOf(orderEntity.getPrice()));

            binding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onEdit(orderEntity, getAdapterPosition());
                }
            });

            binding.imgRemove.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onRemove(orderEntity, getAdapterPosition());
                }
            });
        }
    }

    public interface OnOrderClickListener {
        void onEdit(OrderEntity orderEntity, int position);

        void onRemove(OrderEntity orderEntity, int position);
    }
}

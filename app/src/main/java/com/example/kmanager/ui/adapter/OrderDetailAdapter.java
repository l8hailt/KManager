package com.example.kmanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmanager.databinding.ItemOrderDetailBinding;
import com.example.kmanager.db.entity.OrderDetailEntity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder> {

    private List<OrderDetailEntity> orders;
    private boolean isAdmin;
    private OnOrderDetailClickListener listener;
    private SimpleDateFormat sdf;
    private NumberFormat nf;

    public OrderDetailAdapter(List<OrderDetailEntity> orders, boolean isAdmin, NumberFormat nf) {
        this.orders = orders;
        this.isAdmin = isAdmin;
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        this.nf = nf;
    }

    public void setOnOrderClickListener(OnOrderDetailClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(inflater);
        return new OrderDetailHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder holder, int position) {
        holder.bind(orders.get(position), isAdmin, listener, sdf, nf);
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public static class OrderDetailHolder extends RecyclerView.ViewHolder {

        private ItemOrderDetailBinding binding;

        public OrderDetailHolder(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderDetailEntity detailEntity,
                  boolean isAdmin,
                  OnOrderDetailClickListener listener,
                  SimpleDateFormat sdf,
                  NumberFormat nf) {
            binding.tvOrderName.setText(detailEntity.getName());
            binding.tvOrderTime.setText(sdf.format(detailEntity.getOrderTime()));
            binding.tvPrice.setText(nf.format(detailEntity.getPrice()));
            binding.imgRemove.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

            binding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onEdit(detailEntity, getAdapterPosition());
                }
            });

            binding.imgRemove.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onRemove(detailEntity, getAdapterPosition());
                }
            });
        }
    }

    public interface OnOrderDetailClickListener {
        void onEdit(OrderDetailEntity orderEntity, int position);

        void onRemove(OrderDetailEntity orderEntity, int position);
    }
}

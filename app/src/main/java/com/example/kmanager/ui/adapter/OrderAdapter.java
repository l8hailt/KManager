package com.example.kmanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmanager.databinding.ItemOrderStatisticsBinding;
import com.example.kmanager.db.entity.OrderDetailEntity;
import com.example.kmanager.db.entity.OrderEntity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private List<OrderEntity> orders;
    private OnOrderClickListener listener;
    private SimpleDateFormat sdf;
    private NumberFormat nf;

    public OrderAdapter(List<OrderEntity> orders) {
        this.orders = orders;
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderStatisticsBinding binding = ItemOrderStatisticsBinding.inflate(inflater);
        return new OrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
//        holder.bind(orders.get(position), listener);
        holder.bind(orders.get(position), listener, sdf, nf);
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public static class OrderHolder extends RecyclerView.ViewHolder {

        private ItemOrderStatisticsBinding binding;

        public OrderHolder(@NonNull ItemOrderStatisticsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

//        void bind(OrderDetailEntity detailEntity, OnOrderClickListener listener) {
//            binding.tvOrderName.setText(detailEntity.getName());
//            binding.tvPrice.setText(String.valueOf(detailEntity.getPrice()));
//
//            binding.getRoot().setOnClickListener(view -> {
//                if (listener != null) {
//                    listener.onEdit(detailEntity, getAdapterPosition());
//                }
//            });
//
//            binding.imgRemove.setOnClickListener(view -> {
//                if (listener != null) {
//                    listener.onRemove(detailEntity, getAdapterPosition());
//                }
//            });
//        }

        void bind(OrderEntity orderEntity, OnOrderClickListener listener, SimpleDateFormat sdf, NumberFormat nf) {
            binding.tvOrderId.setText("HD" + orderEntity.getId());
            binding.tvTotal.setText(nf.format(orderEntity.getTotal()));
            binding.tvStartDate.setText(sdf.format(new Date(orderEntity.getTimeEnd())));

            binding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(orderEntity);
                }
            });
//
//            binding.imgRemove.setOnClickListener(view -> {
//                if (listener != null) {
//                    listener.onRemove(detailEntity, getAdapterPosition());
//                }
//            });
        }
    }

    public interface OnOrderClickListener {
        void onClick(OrderEntity orderEntity);
//
//        void onRemove(OrderDetailEntity orderEntity, int position);
    }
}

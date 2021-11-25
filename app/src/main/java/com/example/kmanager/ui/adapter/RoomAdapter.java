package com.example.kmanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmanager.R;
import com.example.kmanager.databinding.ItemRoomBinding;
import com.example.kmanager.db.entity.RoomEntity;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {

    private List<RoomEntity> rooms;
    private OnRoomClickListener listener;

    public RoomAdapter(List<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public void setOnRoomClickListener(OnRoomClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRoomBinding binding = ItemRoomBinding.inflate(inflater);
        return new RoomHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
        holder.bind(rooms.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return rooms != null ? rooms.size() : 0;
    }

    public static class RoomHolder extends RecyclerView.ViewHolder {

        private ItemRoomBinding binding;

        public RoomHolder(@NonNull ItemRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RoomEntity roomEntity, OnRoomClickListener listener) {
            binding.tvRoomName.setText(roomEntity.getName());
            binding.tvRoomStatus.setTextColor(roomEntity.isUse()
                    ? ContextCompat.getColor(binding.getRoot().getContext(), R.color.red)
                    : ContextCompat.getColor(binding.getRoot().getContext(), android.R.color.tertiary_text_light));
            binding.tvRoomStatus.setText(roomEntity.isUse()
                    ? "Đang sử dụng"
                    : "Đang trống");
            binding.imgIcon.setImageResource(roomEntity.isUse()
                    ? R.drawable.ic_room_close
                    : R.drawable.ic_room_open);

            binding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(roomEntity);
                }
            });
        }
    }

    public interface OnRoomClickListener {
        void onClick(RoomEntity roomEntity);
    }
}

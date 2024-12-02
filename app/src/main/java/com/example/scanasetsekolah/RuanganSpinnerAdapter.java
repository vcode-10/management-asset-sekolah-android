package com.example.scanasetsekolah;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RuanganSpinnerAdapter extends ArrayAdapter<Ruangan> {
    private Context mContext;
    private List<Ruangan> ruanganList;
    private int mResource;

    public RuanganSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Ruangan> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.ruanganList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Ruangan ruangan = ruanganList.get(position);

        viewHolder.textViewRuangan.setText(ruangan.getNama());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Ruangan ruangan = ruanganList.get(position);

        viewHolder.textViewRuangan.setText(ruangan.getNama());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewRuangan;

        ViewHolder(View view) {
            textViewRuangan = view.findViewById(R.id.textViewRuangan);
        }
    }
}

package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.jyt.baseapp.R;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by v7 on 2016/12/24.
 */

public class AddressViewHolder extends BaseViewHolder {
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.name)
    TextView name;

    OnAddressClickListener onAddressClickListener;

    public AddressViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_address, parent, false));
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddressClickListener!=null) {
                    onAddressClickListener.onClick((PoiInfo) data,position);
                }
            }
        });
    }

    @Override
    public void setData(Object data, int index) {
        super.setData(data, index);
        name.setText(((PoiInfo) data).name);
        address.setText(((PoiInfo) data).address);
    }

    public void setOnAddressClickListener(OnAddressClickListener onAddressClickListener) {
        this.onAddressClickListener = onAddressClickListener;
    }

    public interface OnAddressClickListener{
        void onClick(PoiInfo info, int index);
    }
}

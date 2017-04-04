package br.com.henriquecocito.lunchtime.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ItemListBinding;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.viewmodel.ItemListViewModel;

/**
 * Created by hrcocito on 28/03/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private Location mLocation;
    private List<Place> mData;

    public MainAdapter(Context context, Location location, List<Place> data) {
        this.mContext = context;
        this.mLocation = location;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemListBinding.setItemListViewModel(new ItemListViewModel(mLocation, mData.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemListBinding mItemListBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemListBinding = DataBindingUtil.bind(itemView);
        }
    }
}

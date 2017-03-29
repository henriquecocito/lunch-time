package br.com.henriquecocito.lunchtime.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ItemListBinding;
import br.com.henriquecocito.lunchtime.viewmodel.ItemListViewModel;

/**
 * Created by hrcocito on 28/03/17.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mData;

    public BaseAdapter(Context context, ArrayList<String> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemListBinding.setItemListViewModel(new ItemListViewModel(mData.get(position)));
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

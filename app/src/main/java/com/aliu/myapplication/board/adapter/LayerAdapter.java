package com.aliu.myapplication.board.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliu.myapplication.R;
import com.aliu.myapplication.board.bean.Layer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * describe
 * Created by liudong on 2017/11/19.
 */

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.ViewHolder> {

    private RecyclerView mRecyclerView;

    private Context     mContext;
    private List<Layer> layerList;
    private View        viewHead;
    private View        viewFooter;
    private int         currentIndex;

    //Type
    public final static int TYPE_NORMAL = 1000;
    public final static int TYPE_HEADER = 1001;
    public final static int TYPE_FOOTER = 1002;

    public LayerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setLayerList(List<Layer> layerList) {
        this.layerList = layerList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("LayerAdapter", "onCreateViewHolder: viewType = " + viewType);
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(viewHead);
        } else if (viewType == TYPE_FOOTER) {
            return new ViewHolder(viewFooter);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_layer, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("LayerAdapter", "onBindViewHolder: position = " + position);
        boolean isHeaderView = isHeaderView(position);
        boolean isFooterView = isFooterView(position);
        position = isHeaderView || isFooterView ? -1 : position;
        final int resultPos = haveHeaderView() ? --position : position;
        final int type = isHeaderView ? TYPE_HEADER : isFooterView ? TYPE_FOOTER : TYPE_NORMAL;
        if (!isHeaderView && !isFooterView) {
            Log.d("LayerAdapter", "onBindViewHolder: position = " + resultPos);
            Layer layer = layerList.get(resultPos);
            holder.ivLayer.setImageBitmap(layer.getBitmap());
            holder.tvTitle.setText(layer.getTitle());
            holder.ivChoose.setVisibility(currentIndex == resultPos ? View.VISIBLE : View.GONE);// 表示选择的当前的结果
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onClickListener(resultPos, type);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onLongClickListener(resultPos, type);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = (layerList == null ? 0 : layerList.size());
        Log.d("LayerAdapter", "getItemCount: count1 = " + count);
        if (viewHead != null) {
            count++;
        }
        if (viewFooter != null) {
            count++;
        }
        Log.d("LayerAdapter", "getItemCount: count2 = " + count);
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("LayerAdapter", "getItemViewType: position = " + position);
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && null != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderView(View headerView) {
        //避免出现宽度自适应
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(params);
        viewHead = headerView;
        ifGridLayoutManager();
        notifyItemInserted(0);
    }

    public void setFooterView(View footerView) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        viewFooter = footerView;
        ifGridLayoutManager();
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 设置当前使用的下标
     */
    public void setCurrentIndex(int index) {
        if (index < 0)
            index = 0;
        int count = (layerList == null ? 0 : layerList.size());
        if (index > count)
            index = count - 1;
        this.currentIndex = index;
        notifyDataSetChanged();
    }

    /**
     * 刷新当前数据下标
     */
    public void notifyCurrentIndex() {
        if (layerList == null || layerList.size() == 0)
            return;
        int position = currentIndex;
        if (haveHeaderView()) {
            position = currentIndex + 1;
        }
        notifyItemChanged(position);
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
    }

    private boolean haveHeaderView() {
        return viewHead != null;
    }

    private boolean haveFooterView() {
        return viewFooter != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    // ======== 监听 ========

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onClickListener(int position, int type);

        void onLongClickListener(int position, int type);
    }
    // ======== ViewHolder ========

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_choose)
        ImageView ivChoose;
        @BindView(R.id.iv_layer)
        ImageView ivLayer;
        @BindView(R.id.tv_title)
        TextView  tvTitle;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            if (itemView != viewHead && itemView != viewFooter) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}

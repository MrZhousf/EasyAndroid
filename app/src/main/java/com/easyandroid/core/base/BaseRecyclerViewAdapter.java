package com.easyandroid.core.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class BaseRecyclerViewAdapter<E extends Object,T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    protected List<E> mList;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerViewAdapter() {
        if(null == mList)
            mList = new ArrayList<E>();
    }

    public abstract  T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(T viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClickRecycler(v, position);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnItemLongClickListener) {
                    return mOnItemLongClickListener.onItemLongClickRecycler(v, position);
                }
                return false;
            }
        });
    }

    public List<E> getList() {
        return mList;
    }

    public E getItem(int position){
        if(null != mList && mList.size() > 0){
            return mList.get(position);
        }else {
            return null;
        }
    }

    public boolean isNotEmpty(){
        return null != mList && mList.size() > 0;
    }

    public E getFirstItem(){
        return getItem(0);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void insert(E data, int position){
        mList.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void replaceAll(Collection<E> collection){
        mList.clear();
        if(collection != null){
            mList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void addAll(Collection<E> collection){
        if(collection != null){
            mList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void addItem(E e){
        mList.add(e);
        notifyItemInserted(getPosition(e));
    }

    public int getPosition(E e){
        return mList.indexOf(e);
    }

    public void addAllItem(List<E> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(E e){
        mList.remove(e);
        notifyItemRemoved(getPosition(e));
    }

    public void removeAllItem(List<E> list){
        mList.removeAll(list);
        notifyDataSetChanged();
    }

    public List<E> getAllItem(){
        return mList;
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClickRecycler(View parent, int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClickRecycler(View parent, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }



}

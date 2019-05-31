package com.dhc.mvp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dhc.library.base.BaseActivity;
import com.dhc.mvp.net.NetSampleActivity;
import com.dhc.mvp.net.RoomSampleActivity;
import com.dhc.mvp.net.RxCacheSampleActivity;
import com.dhc.mvp.net.RxJavaSampleActivity;

/**
 * @creator： denghc(desoce)
 * @updateTime：
 * @description： home
 */
public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private String[] menu = {"net Sample","room Sample","rxcache Sample", "protobuf Sample"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initEventAndData(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        recyclerView = $(R.id.rv_menu);
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onItemClickListener=new OnItemClickListener() {
            @Override
            public void itemClick(int pos, String title) {
                router(pos,title);

            }
        };
        recyclerView.setAdapter(adapter);
    }



    private void router(int pos, String title) {

        switch (pos){
           case  0 :
               startActivity(new Intent(this, NetSampleActivity.class));
            break;
           case  1 :
               startActivity(new Intent(this, RoomSampleActivity.class));
            break;
           case  2 :
               startActivity(new Intent(this, RxCacheSampleActivity.class));
            break;
            case  3 :
                startActivity(new Intent(this, RxJavaSampleActivity.class));
                break;
        }

    }


    RecyclerView.Adapter adapter = new RecyclerView.Adapter<BaseViewHolder>() {
        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, null, false);

            return new BaseViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null)
                    onItemClickListener.itemClick(position,menu[position]);
                }
            });
            holder.onBind(menu[position]);
        }

        @Override
        public int getItemCount() {
            return menu.length;
        }
    };
   private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {

       void  itemClick(int pos,String title);
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        public void onBind(String title) {

            tvTitle.setText(title);
        }

    }


}

package com.example.carpoolgl.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolgl.R;
import com.example.carpoolgl.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchRecycAdapter extends RecyclerView.Adapter<SearchRecycAdapter.myHodler> {

    private Context context;
    private ArrayList<String> datas;
    private List<HashMap<String, String>> listString;

    public SearchRecycAdapter(SearchActivity context, ArrayList<String> datas){
        this.context = context;
        this.datas = datas;
    }

    public SearchRecycAdapter(SearchActivity context,List<HashMap<String, String>> datas){
        this.context =context;
        this.listString = datas;
    }

    class myHodler extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_title;
        private TextView tv_content;

        public myHodler(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,"data="+datas.get(getLayoutPosition()),Toast.LENGTH_SHORT).show();
//                    if(onItemClickListener!=null){
//                        onItemClickListener.OnItemclick(v,datas.get(getLayoutPosition()));
//                    }
                    if(onItemClickListener!=null){
                        HashMap<String, String> map = listString.get(getLayoutPosition());
                        for(String title:map.keySet()){
                            String content = map.get(title);
                            onItemClickListener.OnItemclick(v,title+" "+content);
                        }

                    }

                }
            });
        }

    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        /**
         * 当recyclerview某个被点击的时候回调
         * @param view  点击item的视图
         * @param data  点击得到数据
         */
        void OnItemclick(View view,String data);


    }
    //设置recyclerview某条的监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 相当于getView方法中创建View和ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public myHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_search_recyc,null);

        return new myHodler(itemView);
    }

    /**
     * 相当于getView中绑定数据部分的代码
     * 数据和view绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull myHodler holder, int position) {
        //根据位置得到对应的数据
//        String data = datas.get(position);
//        holder.tv_title.setText(data);
        HashMap<String, String> map = listString.get(position);
        Log.i("map",map+"");
        holder.tv_content.setText(map.get("name"));
        holder.tv_title.setText(map.get("address"));
    }

    /**
     * 得到总条数
     * @return
     */
    @Override
    public int getItemCount() {
//        return this.datas.size();
        return this.listString.size();
    }


}

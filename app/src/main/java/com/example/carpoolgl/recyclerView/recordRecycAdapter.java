package com.example.carpoolgl.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carpoolgl.R;
import com.example.carpoolgl.bean.RecordInfo;

import java.util.List;

public class recordRecycAdapter extends RecyclerView.Adapter<recordRecycAdapter.myHolder>{
    private Context context;
    private List<RecordInfo> datas;

    private RecordInfo record;

    public recordRecycAdapter(Context context, List<RecordInfo> datas){
        this.context = context;
        this.datas = datas;

        Log.i("driverOrderr_M5",datas.toString());
    }

    class myHolder extends RecyclerView.ViewHolder{

        private TextView record_order_seq_tv;//订单编号
        private TextView record_geton_tv;//起点地址
        private TextView record_getoff_tv;//终点地址
        private TextView record_time_tv;//出发时间
        private TextView record_money_tv;//钱
        private Button record_detail_bt;//查看详情按钮
        private LinearLayout color_linear;//cardview 左边条颜色


        public myHolder(@NonNull View itemView){
            super(itemView);
            record_order_seq_tv = itemView.findViewById(R.id.record_order_seq_tv);
            record_geton_tv = itemView.findViewById(R.id.record_geton_tv);
            record_getoff_tv = itemView.findViewById(R.id.record_getoff_tv);
            record_time_tv = itemView.findViewById(R.id.record_time_tv);
            record_money_tv = itemView.findViewById(R.id.record_money_tv);
            record_detail_bt = itemView.findViewById(R.id.record_detail_bt);
            color_linear = itemView.findViewById(R.id.color_linear);


            record_detail_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(RecordClickLisener!=null){
                        record = datas.get(getLayoutPosition());    //获取当前cardview的数据信息
                        RecordClickLisener.OnRouteClick(record);

                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public recordRecycAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_recyc,parent,false);
//        View itemview = View.inflate(context,R.layout.item_orders_recyc,null);
        recordRecycAdapter.myHolder holder = new recordRecycAdapter.myHolder(itemview);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull recordRecycAdapter.myHolder holder, int position) {
        record = datas.get(position);
        Log.i("driverOrderr_M5",record.toString());

        //设置cardvierw边条颜色
        if(record.getIdentity().equals(0)){
            holder.color_linear.setBackground(context.getDrawable(R.drawable.linear_pa_radius));
        }else if(record.getIdentity().equals(1)){
            holder.color_linear.setBackground(context.getDrawable(R.drawable.linear_dr_radius));
        }

        holder.record_order_seq_tv.setText(record.getOrderSeq());
        holder.record_geton_tv.setText(record.getStartLoc());
        holder.record_getoff_tv.setText(record.getEndLoc());
        holder.record_time_tv.setText(record.getTime());
        holder.record_money_tv.setText(record.getMoney()+"");


//        holder.passName.setText(passMems.getName());
//        holder.paymoney.setText(STATIC_CLASS.getSingleMoney()+passMems.getAprMoney()+"");
//        holder.togetherNum.setText("人数： "+passMems.getTogetherNum());
//        holder.passPhone.setText(passMems.getPhone());

    }

    private recordRecycAdapter.OrderClickLisener RecordClickLisener;
    public interface OrderClickLisener{
        /**
         * 当recyclerview某个被点击的时候回调
         * @param record  点击视图中button
         *
         */
        void OnRouteClick(RecordInfo record);

    }
    //设置recyclerview某条的监听
    public void setOnItemClickListener(recordRecycAdapter.OrderClickLisener RecordClickLisener) {
        this.RecordClickLisener = RecordClickLisener;
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }
}

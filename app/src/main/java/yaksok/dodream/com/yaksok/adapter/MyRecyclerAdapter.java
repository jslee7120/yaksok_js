package yaksok.dodream.com.yaksok.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yaksok.dodream.com.yaksok.ChattingMenu;
import yaksok.dodream.com.yaksok.ChattingRoom;
import yaksok.dodream.com.yaksok.LoginActivity;
import yaksok.dodream.com.yaksok.R;
import yaksok.dodream.com.yaksok.item.ConnectedFamily;
import yaksok.dodream.com.yaksok.item.RecyclerItem;
import yaksok.dodream.com.yaksok.vo.message.ChatMessage;
import yaksok.dodream.com.yaksok.vo.message.SendMessageVO;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private ArrayList<SendMessageVO> albumList;
    private int itemLayout;
    public ChattingRoom chattingRoom;
    public String getTime;


    /**
     * 생성자
     * @param items
     * @param itemLayout
     */
    public MyRecyclerAdapter(ArrayList<SendMessageVO> items , int itemLayout){

        this.albumList = items;
        this.itemLayout = itemLayout;
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        getTime = sdf.format(date);


    }

    /**
     * 레이아웃을 만들어서 Holer에 저장
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);

        return new ViewHolder(view);
    }

    /**
     * listView getView 를 대체
     * 넘겨 받은 데이터를 화면에 출력하는 역할
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MyRecyclerAdapter.ViewHolder viewHolder1 = viewHolder;


        SendMessageVO item = albumList.get(position);
//        viewHolder.user2.setText(item.getGivingUser());
//        viewHolder.textTitle.setText(item.getReceivingUser());
//        viewHolder.user1.setText(item.getContent());

        if(!albumList.get(position).getGivingUser().equals("")){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(
                    100,
                    100);
            layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            //layoutParams3.addRule(RelativeLayout.CENTER_IN_PARENT);



            //layoutParams.topMargin = 700;//in my case

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);//in my case
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams3.addRule(RelativeLayout.START_OF,R.id.user_context);



            viewHolder1.textTitle.setLayoutParams(layoutParams);
            viewHolder1.textTitle.setText(albumList.get(position).getContent());
            viewHolder1.textTitle.setBackgroundResource(R.drawable.patch_right);
            viewHolder1.user2.setText(item.getGivingUser());
            viewHolder1.user2.setVisibility(View.INVISIBLE);
            viewHolder1.user1.setText(item.getReceivingUser());
            viewHolder1.user1.setVisibility(View.INVISIBLE);
            viewHolder1.user1.setLayoutParams(layoutParams2);
            viewHolder1.time.setText(albumList.get(position).getRegidate());
            viewHolder1.time.setLayoutParams(layoutParams3);
            viewHolder1.user_lv.setImageResource(R.drawable.user_pic);
            //viewHolder1.user_lv.setLayoutParams(layoutParams9);
            viewHolder1.user_lv.setLayoutParams(layoutParams5);
            viewHolder1.user_lv.setVisibility(View.GONE);


            Log.d("givingtimeadatper",albumList.get(position).getRegidate());
            // viewHolder1.giving_time.setLayoutParams(layoutParams3);


        }
       Log.d("test15", albumList.get(position).getReceivingUser() + "," + LoginActivity.userVO.getId());
        if(ChattingRoom.albumList.get(position).getReceivingUser().equals(LoginActivity.userVO.getId())){

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(
                    100,
                    100);
            layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);





            // layoutParams3.addRule(RelativeLayout.CENTER_IN_PARENT);

            //layoutParams.topMargin = 700;//in my case
            //layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.BELOW,R.id.user2_txt);
            layoutParams.addRule(RelativeLayout.END_OF,R.id.user_iv);
            layoutParams2.addRule(RelativeLayout.ALIGN_BASELINE,R.id.user_context);
            layoutParams7.addRule(RelativeLayout.ALIGN_TOP,R.id.user_context);
            layoutParams3.addRule(RelativeLayout.END_OF,R.id.user_context);
            layoutParams3.addRule(RelativeLayout.BELOW,R.id.user2_txt);
            layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams8.addRule(RelativeLayout.END_OF,R.id.user_iv);
            //layoutParams8.addRule(RelativeLayout.ABOVE,R.id.user_iv);
            layoutParams9.addRule(RelativeLayout.CENTER_IN_PARENT);

            layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_START,R.id.user_iv);
            layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            //layoutParams.topMargin = 20;


            viewHolder1.textTitle.setText(albumList.get(position).getContent());
            viewHolder1.textTitle.setLayoutParams(layoutParams);
            viewHolder1.textTitle.setBackgroundResource(R.drawable.patch_left);
            //viewHolder1.textTitle.setBackgroundColor(getResources().getColor(R.color.main_color));
            viewHolder1.user1.setText(item.getReceivingUser());
            viewHolder1.user1.setVisibility(View.INVISIBLE);
//            viewHolder1.user2.setLayoutParams(layoutParams2);
//            viewHolder1.user2.setLayoutParams(layoutParams7);
            viewHolder1.user2.setText(ChattingRoom.connectedName);
            viewHolder1.user2.setVisibility(View.VISIBLE);
            viewHolder1.user2.setTextSize(13);
            viewHolder1.user2.setLayoutParams(layoutParams8);
            //viewHolder1.user2.setLayoutParams(layoutParams4);
            //viewHolder1.user2.setLayoutParams(layoutParams4);

            //viewHolder1.user2.setLayoutParams(layoutParams8);

            viewHolder1.time.setText(albumList.get(position).getRegidate());
            viewHolder1.time.setLayoutParams(layoutParams3);
            viewHolder1.user_lv.setImageResource(R.drawable.user_pic);
            //viewHolder1.user_lv.setLayoutParams(layoutParams9);
            viewHolder1.user_lv.setVisibility(View.VISIBLE);
            viewHolder1.user_lv.setLayoutParams(layoutParams5);



           // viewHolder1.time.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);



      }

      //setFadeAnimation(viewHolder.itemView);


    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    /**
     * 뷰 재활용을 위한 viewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView user1, textTitle,user2,time;
        private ImageView user_lv;


        public ViewHolder(View itemView){
            super(itemView);

            user1 = (TextView) itemView.findViewById(R.id.user1_txt);
            user2 = (TextView) itemView.findViewById(R.id.user2_txt);
            textTitle = (TextView) itemView.findViewById(R.id.user_context);
            time = (TextView) itemView.findViewById(R.id.receiving_time);
            user_lv = (ImageView) itemView.findViewById(R.id.user_iv);



        }

    }
    public void addItem(String givinguser,String context, String receivinguser){
       SendMessageVO messageVO = new SendMessageVO();
       messageVO.setGivingUser(givinguser);
       messageVO.setContent(context);
       messageVO.setReceivingUser(receivinguser);

       albumList.add(messageVO);
    }
//    private void setFadeAnimation(View view) {
//        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(1000);
//        view.startAnimation(anim);
//    }



}




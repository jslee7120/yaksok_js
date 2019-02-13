package yaksok.dodream.com.yaksok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.adapter.MyRecyclerAdapter;
import yaksok.dodream.com.yaksok.item.RecyclerItem;
import yaksok.dodream.com.yaksok.service.MessageService;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.BodyVO;
import yaksok.dodream.com.yaksok.vo.UserID2;
import yaksok.dodream.com.yaksok.vo.UserVO;
import yaksok.dodream.com.yaksok.vo.message.MessageBodyVO;
import yaksok.dodream.com.yaksok.vo.message.MessageResultBodyVO;
import yaksok.dodream.com.yaksok.vo.message.MessageVO;
import yaksok.dodream.com.yaksok.vo.message.SendMessageVO;

public class ChattingRoom extends AppCompatActivity  {
    public static RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Retrofit retrofit;
    private MessageService messageService;
    private EditText user_contextEdt;
    private String user_context;
    private String user2_id;
    private Button send_btn;
    TextView user1_txt,user2_txt,user_context2;
    public ArrayList<RecyclerItem> recyclerItems;
    public static UserID2 userID2;
    public static String connectedName;
    public static boolean iInTheChattingRoom;
    public static LinearLayoutManager linearLayoutManager;
    public static boolean msgStatus=true;
    public static String your_id;


    public static ArrayList<SendMessageVO> albumList;
    public static MyRecyclerAdapter myRecyclerAdapter;
    public LoginActivity login_activity;
    String u_id;
    String y_id;



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getIntent());
        user2_id = ChattingMenu.user_me;
        connectedName = intent.getStringExtra("name");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.chat2);
        actionBar.setTitle(connectedName);

        setContentView(R.layout.activity_chatting_room);

        // setCustomActionBar();
        final ArrayList<RecyclerItem> items = new ArrayList<>();

        albumList = new ArrayList<SendMessageVO>();
        iInTheChattingRoom = true;

        linearLayoutManager = new LinearLayoutManager(this);
        initLayout();

        Log.d("inthechatroom","aa"+iInTheChattingRoom);

        user_contextEdt = (EditText)findViewById(R.id.user_context_edt);
        user_contextEdt.setFocusable(true);
        user_context = user_contextEdt.getText().toString();
        send_btn = (Button)findViewById(R.id.send_btn);



        user1_txt = (TextView)findViewById(R.id.user1_txt);
        user2_txt = (TextView)findViewById(R.id.user2_txt);
        user_context2 = (TextView)findViewById(R.id.user_context);





        //Log.d("iiiiiiiii",intent.getStringExtra("from"));
        //Toast.makeText(getApplicationContext(),intent.getStringExtra("send_user")+""+intent.getStringExtra("recived_message"),Toast.LENGTH_LONG).show();


        userID2 = new UserID2(user2_id);

        your_id = user2_id;


        retrofit = new Retrofit.Builder()
                .baseUrl(messageService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        messageService = retrofit.create(MessageService.class);
        Collections.reverse(albumList);



        myRecyclerAdapter = new MyRecyclerAdapter(albumList,R.layout.recycleritem);

        if(intent.getStringExtra("goBack")==null){
            linearLayoutManager.setStackFromEnd(true);
            getPreviouseConversation(LoginActivity.userVO.getId(),user2_id);

        }
        else if(intent.getStringExtra("goBack").equals("123")){
            u_id = intent.getStringExtra("user_id");
            y_id = intent.getStringExtra("your_id");

            if(intent.getStringExtra("send_user")!=null){
                connectedName = intent.getStringExtra("send_user");
            }

            Log.d("uuuuuu",u_id+"   "+y_id);
            getPreviouseConversation(u_id,y_id);

        }

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgStatus = true;
                String inTime = new SimpleDateFormat("HHmmss", Locale.KOREA).format(new Date());
                String time = inTime.substring(0,2)+":"+inTime.substring(2,4);
                Log.d("time","!!!!!!!!!!!!!"+inTime);


                if(user_contextEdt.getText().length()==0){
                    return;
                }


                //linearLayoutManager.setReverseLayout(true);

                //mRecyclerView.setAdapter(myRecyclerAdapter);
                //mRecyclerView.setLayoutManager(linearLayoutManager);

                //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                //user_contextEdt.setText("");
                final SendMessageVO sendVO = new SendMessageVO();
                sendVO.setGivingUser(LoginActivity.userVO.getId());
                sendVO.setContent(user_contextEdt.getText().toString());
                sendVO.setReceivingUser(user2_id);
                //sendVO.setRegidate(time);


                Log.d("@@@@@@@@@@@"+"id",sendVO.getGivingUser()+"recive"+sendVO.getReceivingUser()+"context"+sendVO.getContent());
                Call<MessageResultBodyVO> call = messageService.sendAmeesage(sendVO);
                call.enqueue(new Callback<MessageResultBodyVO>() {
                    @Override
                    public void onResponse(Call<MessageResultBodyVO> call, Response<MessageResultBodyVO> response) {
                        MessageResultBodyVO messageBodyVO = response.body();
                        //201 : OK
                        //400: FCM error
                        //500 : Server Error
                        if(messageBodyVO.getStatus().equals("201")){
                            Toast.makeText(getApplicationContext(),"전송되었습니다.",Toast.LENGTH_SHORT).show();
                            sendVO.setRegidate(messageBodyVO.getRegiDate().substring(11,16));
                            albumList.add(sendVO);
                            mRecyclerView.setAdapter(myRecyclerAdapter);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            user_contextEdt.setText("");
                            linearLayoutManager.setStackFromEnd(true);
                            //getPreviouseConversation(LoginActivity.userVO.getId(),user2_id);
                            //linearLayoutManager.setStackFromEnd(true);
                            //Log.d("get",""+linearLayoutManager.getStackFromEnd());

                        }
                        else if(messageBodyVO.getStatus().equals("400")){
                            Toast.makeText(getApplicationContext(),"FCM Error",Toast.LENGTH_SHORT).show();
                        } else if (messageBodyVO.getStatus().equals("500")) {
                            Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResultBodyVO> call, Throwable t) {

                    }
                });

            }
        });


    }

//    private void setCustomActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
//        actionBar.setDisplayShowCustomEnabled(true);
//
//        View customView = LayoutInflater.from(this).inflate(R.layout.chattingactionbar,null);
//        actionBar.setTitle(connectedName);
//        actionBar.setCustomView(customView);
//    }

    private void initLayout() {
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recyclerview);


    }

    public void getPreviouseConversation(String u_id,String y_id) {
        Call<MessageBodyVO> call = messageService.getTheChatting(u_id,y_id);
        call.enqueue(new Callback<MessageBodyVO>() {
            @Override
            public void onResponse(Call<MessageBodyVO> call, Response<MessageBodyVO> response) {
                MessageBodyVO bodyVO = response.body();
                String name ="" ;
                //200 : OK
                //204 : 값없음(null반환)
                //500 : Server Error
                assert bodyVO != null;
                // Log.d("@@@@@@@@@@@@@@@@@@",bodyVO.getResult().get(0).getContent()+"id"+bodyVO.getResult().get(0).getGivingUser()+"id2"+bodyVO.getResult().get(0).getReceivingUser());
                //Toast.makeText(getApplicationContext(),"body"+bodyVO.getStatus()+"result"+bodyVO.getResult().size(),Toast.LENGTH_SHORT).show();

                assert bodyVO != null;
                if(bodyVO.getStatus().equals("200")){
                    for(int i = 0; i < bodyVO.getResult().size(); i++){
                        Log.d("실행","실행 됨");

                        SendMessageVO sendMessageVO = new SendMessageVO();
                        sendMessageVO.setGivingUser(bodyVO.getResult().get(i).getGivingUser());
                        sendMessageVO.setContent(bodyVO.getResult().get(i).getContent());
                        sendMessageVO.setReceivingUser(bodyVO.getResult().get(i).getReceivingUser());
                        sendMessageVO.setRegidate(bodyVO.getResult().get(i).getRegiDate().substring(11,16));
                        //Collections.reverse(albumList);//역순으로
                        Log.d("list_test", sendMessageVO.getContent() + "," + i);
                        albumList.add(sendMessageVO);
                        Log.d("album_test", albumList.get(i).getContent());
                        name = bodyVO.getResult().get(i).getGivingUser();
                    }
                    Collections.reverse(albumList);
                    mRecyclerView.setAdapter(new MyRecyclerAdapter(albumList,R.layout.recycleritem));
                    linearLayoutManager.setStackFromEnd(true);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    Log.d("name111","aaa"+name);
                }else if(bodyVO.getStatus().equals("204")){
                    user_contextEdt.setFocusable(true);
                }
                else if(bodyVO.getStatus().equals("500")){
                    Toast.makeText(getApplicationContext(),"서버 오류입니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessageBodyVO> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        iInTheChattingRoom = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        iInTheChattingRoom = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        iInTheChattingRoom = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iInTheChattingRoom = false;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("newIntent");
        if (intent != null) {
            if(!intent.getStringExtra("from").equals("")) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("from"), Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getApplicationContext(),"NULLLLL",Toast.LENGTH_LONG).show();
            }

        }
        super.onNewIntent(intent);
    }
    /*    public void whenGenNoticification(String u_id,String y_id){
            Call<MessageBodyVO> call = messageService.getTheChatting(u_id,y_id);
            call.enqueue(new Callback<MessageBodyVO>() {
                @Override
                public void onResponse(Call<MessageBodyVO> call, Response<MessageBodyVO> response) {
                    MessageBodyVO bodyVO = response.body();
                    //200 : OK
                    //204 : 값없음(null반환)
                    //500 : Server Error
                    assert bodyVO != null;
                    // Log.d("@@@@@@@@@@@@@@@@@@",bodyVO.getResult().get(0).getContent()+"id"+bodyVO.getResult().get(0).getGivingUser()+"id2"+bodyVO.getResult().get(0).getReceivingUser());
                    //Toast.makeText(getApplicationContext(),"body"+bodyVO.getStatus()+"result"+bodyVO.getResult().size(),Toast.LENGTH_SHORT).show();

                    assert bodyVO != null;
                    if(bodyVO.getStatus().equals("200")){
                        for(int i = 0; i < bodyVO.getResult().size(); i++){
                            Log.d("실행","실행 됨");

                            SendMessageVO sendMessageVO = new SendMessageVO();
                            sendMessageVO.setGivingUser(bodyVO.getResult().get(i).getGivingUser());
                            sendMessageVO.setContent(bodyVO.getResult().get(i).getContent());
                            sendMessageVO.setReceivingUser(bodyVO.getResult().get(i).getReceivingUser());
                            sendMessageVO.setRegidate(bodyVO.getResult().get(i).getRegiDate().substring(11,16));

                            //Collections.reverse(albumList);//역순으로
                            albumList.add(sendMessageVO);
                        }
                        Collections.reverse(albumList);
                        linearLayoutManager.setStackFromEnd(true);
                        mRecyclerView.setAdapter(new MyRecyclerAdapter(albumList,R.layout.recycleritem));
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                       // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    }else if(bodyVO.getStatus().equals("204")){
                        user_contextEdt.setFocusable(true);
                    }
                    else if(bodyVO.getStatus().equals("500")){
                        Toast.makeText(getApplicationContext(),"서버 오류입니다.",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<MessageBodyVO> call, Throwable t) {

                }
            });


        }*/
    @Override
    protected void onStart() {
        super.onStart();

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View view = LayoutInflater.from(this).inflate(R.layout.chattingactionbar,null);
        ImageView imageView = view.findViewById(R.id.back_layout_imv);
        TextView textView = view.findViewById(R.id.title_txt);


        Intent resultIntent = new Intent();
        setResult(4000,resultIntent);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        textView.setText(connectedName);
        textView.setGravity(Gravity.CENTER);
//        textView.setGravity(Gravity.CENTER);
        actionBar.setTitle(textView.getText().toString());



        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        actionBar.setCustomView(view,layoutParams);
    }



}
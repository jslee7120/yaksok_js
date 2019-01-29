package yaksok.dodream.com.yaksok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.FindFamilyVO;

public class AlarmReciveFamily extends AppCompatActivity {

    public Retrofit retrofit;
    public UserService userService;
    List<String> items;
    List<String> items_id;
    ArrayAdapter adapter;
    ListView listview;
    Button bt_select;
    Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmrecivefamily);
        bt_select = (Button)findViewById(R.id.bt_AlarmReciveFamily_Ok);


        items = new ArrayList<String>();
        items_id = new ArrayList<String>();

        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, items) ;

        // listview 생성 및 adapter 지정.

        listview = (ListView) findViewById(R.id.listview_family);
        listview.setAdapter(adapter);

        retrofit = new Retrofit.Builder()
                .baseUrl(userService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        addFamilyList(); // 나의 가족 리스트를 가져온다

        resultIntent = new Intent();
        setResult(9000,resultIntent);

        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                Log.d("data+++","들어 왔는데..");
                int count = adapter.getCount();
                resultIntent.putExtra("list_size",String.valueOf(count));
                Log.d("list_size",String.valueOf(count));
                for(int i=count-1; i>=0; i--) {
                    Log.d("dataNum",String.valueOf(i));
                    if (checkedItems.get(i)) {
                        resultIntent.putExtra("name"+i,items.get(i));
                        resultIntent.putExtra("id"+i,items_id.get(i));
                        Log.d("data_name", items.get(i) + items.get(i));
                    }
                    else{
                        resultIntent.putExtra("name"+i,"null");
                        resultIntent.putExtra("id"+i,"null");
                    }
                }
                    finish();
            }
        });


    }

    public void addFamilyList(){
        Call<FindFamilyVO> findFamilyVOCall = userService.getConnectedFamilyInfo(LoginActivity.userVO.getId());
        findFamilyVOCall.enqueue(new Callback<FindFamilyVO>() {
            @Override
            public void onResponse(Call<FindFamilyVO> call, Response<FindFamilyVO> response) {
                FindFamilyVO findFamilyVO = response.body();

                if (findFamilyVO.getStatus().equals("200")) {
                    Log.d("가족등록","실행됨" + findFamilyVO.getResult().size());
                    for(int i = 0; i < findFamilyVO.getResult().size();i++){
                        Log.d("f_id",findFamilyVO.getResult().get(i).getNickName()+"/"+findFamilyVO.getResult().get(i).getUserId());
                        items.add(findFamilyVO.getResult().get(i).getNickName());
                        items_id.add(findFamilyVO.getResult().get(i).getUserId());
                    }
                    adapter.notifyDataSetChanged();
                } else if (findFamilyVO.getStatus().equals("204")) {
                    Toast.makeText(getApplicationContext(), "상대의 계정이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                } else if (findFamilyVO.getStatus().equals("400")) {
                    Toast.makeText(getApplicationContext(), "잘못된 요청입니다.", Toast.LENGTH_LONG).show();
                } else if (findFamilyVO.getStatus().equals("500")) {
                    Toast.makeText(getApplicationContext(), "서버 오루 입니다..", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onFailure(Call<FindFamilyVO> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

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


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
//                startActivityForResult(intent,7777);
                finish();
            }
        });
        textView.setText("가족 선택");
        textView.setGravity(Gravity.CENTER);
//        textView.setGravity(Gravity.CENTER);
        actionBar.setTitle(textView.getText().toString());



        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        actionBar.setCustomView(view,layoutParams);
    }
}

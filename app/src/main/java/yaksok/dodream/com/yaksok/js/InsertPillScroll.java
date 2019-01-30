package yaksok.dodream.com.yaksok.js;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.AlarmReceive;
import yaksok.dodream.com.yaksok.AlarmReciveFamily;
import yaksok.dodream.com.yaksok.InsertPillActivity;
import yaksok.dodream.com.yaksok.LoginActivity;
import yaksok.dodream.com.yaksok.R;
import yaksok.dodream.com.yaksok.service.UserService;

public class InsertPillScroll  extends AppCompatActivity implements View.OnClickListener {

    Button bt_List_Add, bt_PillInsert, bt_1time, bt_2time, bt_3time, bt_AlarmReciveFamily;
    EditText et_Pill, et_DiseaseName;
    TextView  et_dosagi;
    LinearLayout lo_tab;
    TimePickerDialog dialog1,dialog2,dialog3;
    TextView tv_1h, tv_1m, tv_2h, tv_2m, tv_3h, tv_3m;
    int  i=0;
    ListView lv_alarmFamily;
    List<String> alarm_f_list;
    ArrayAdapter adapter;
    List<String> f_id;
    List<String> time;
    List<Integer> pillList;
    String h, m;
    Button bt_add, bt_delete;
    Retrofit retrofit;
    UserService userService;
    ImageView minus_count,plus_count;
    Intent intent;
    LinearLayout lv_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.chat2);
        actionBar.setTitle("직접 등록");
        setContentView(R.layout.insertself);

        final LinearLayout inLayout = (LinearLayout) findViewById(R.id.lo_EditText);
        //추가 될 곳을 지정 -> R.layout.main 안에 지정

        pillList = new ArrayList<>();
        time =  new ArrayList<>();
        f_id = new ArrayList<String>();

        alarm_f_list = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alarm_f_list);


        bt_List_Add = (Button) findViewById(R.id.bt_ListAdd);
        bt_PillInsert = (Button) findViewById(R.id.bt_PillInsert);
        bt_1time = (Button) findViewById(R.id.bt_1time);
        bt_2time = (Button) findViewById(R.id.bt_2time);
        bt_3time = (Button) findViewById(R.id.bt_3time);
        tv_1h = (TextView) findViewById(R.id.tv_1_h);
        tv_1m = (TextView) findViewById(R.id.tv_1_m);
        tv_2h = (TextView) findViewById(R.id.tv_2_h);
        tv_2m = (TextView) findViewById(R.id.tv_2_m);
        tv_3h = (TextView) findViewById(R.id.tv_3_h);
        tv_3m = (TextView) findViewById(R.id.tv_3_m);
        et_dosagi = (TextView) findViewById(R.id.et_Dosagi);
        et_DiseaseName = (EditText) findViewById(R.id.et_DiseaseName);
        minus_count = (ImageView) findViewById(R.id.minus_count);
        plus_count = (ImageView) findViewById(R.id.plus_count);
        bt_AlarmReciveFamily = (Button)findViewById(R.id.bt_AlarmReciveFamily);
        lv_layout = (LinearLayout)findViewById(R.id.lv_layout);
        lv_alarmFamily = (ListView)findViewById(R.id.lv_alarmFamily);


        lv_alarmFamily.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lv_alarmFamily.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        retrofit = new Retrofit.Builder()
                .baseUrl(userService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        bt_AlarmReciveFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmReciveFamily.class); // 다음 넘어갈 클래스 지정
                // startActivity(intent);
                startActivityForResult(intent, 9000);
            }
        });



        // 버튼을 누르면 새 뷰가 추가됨.
        bt_List_Add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                lo_tab = (LinearLayout) inflater.inflate(R.layout.edittext, null);
                bt_add = (Button) lo_tab.findViewById(R.id.bt_Search_Add);
                et_Pill = (EditText) lo_tab.findViewById(R.id.et_Search);
              //  bt_delete = (Button) lo_tab.findViewById(R.id.bt_delete);
                inLayout.addView(lo_tab);


                bt_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String test = et_Pill.getText().toString();
                        System.out.println(test);
                        Intent intent = new Intent(getApplicationContext(), SearchPill.class); // 다음 넘어갈 클래스 지정
                        // startActivity(intent);
                        intent.putExtra("PillName",test);
                        startActivityForResult(intent, 3000);
                    }
                });
            }
        });

        intent = getIntent();
        if(intent.getExtras()!=null) {
            if (intent.getStringExtra("QRkey").equals("123")) {
                for (int i = 1; i < 4; i++) {
                    final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    lo_tab = (LinearLayout) inflater.inflate(R.layout.edittext, null);
                    bt_add = (Button) lo_tab.findViewById(R.id.bt_Search_Add);
                    et_Pill = (EditText) lo_tab.findViewById(R.id.et_Search);
                    // bt_delete = (Button) lo_tab.findViewById(R.id.bt_delete);
                    inLayout.addView(lo_tab);
                    pillList.add(Integer.parseInt(intent.getExtras().getString("num" + i)));
                    et_Pill.setText(intent.getExtras().getString("pill" + i));
                    //약 자동으로 추가되고 더이상 추가 할 수 없도록 버튼 disable
                    et_Pill.setEnabled(false);
                    bt_add.setEnabled(false);
                }
                et_dosagi.setText(intent.getExtras().getString("dosagi"));
            }
        }


        dialog1 = new TimePickerDialog(this, listener1, 00, 00, true);
        bt_1time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });
        dialog2 = new TimePickerDialog(this, listener2, 00, 00, true);
        bt_2time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.show();
            }
        });
        dialog3 = new TimePickerDialog(this, listener3, 00, 00, true);
        bt_3time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.show();
            }
        });


        bt_PillInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_DiseaseName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "약 이름을 입력하세요", Toast.LENGTH_LONG).show();
                }else if(lo_tab == null){
                    Toast.makeText(getApplicationContext(), "약 성분을 추가하세요", Toast.LENGTH_LONG).show();
                }else if(et_Pill.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "약 성분을 입력하세요", Toast.LENGTH_LONG).show();
                }else if(et_dosagi.getText().toString().equals("0")){
                    Toast.makeText(getApplicationContext(), "복용횟수를 설정하세요", Toast.LENGTH_LONG).show();
                }else if(time.size() == 0){
                    Toast.makeText(getApplicationContext(), "시간을 입력하세요", Toast.LENGTH_LONG).show();
                }else if(f_id.size() == 0){
                    Toast.makeText(getApplicationContext(), "알림받을 가족을 선택하세요", Toast.LENGTH_LONG).show();
                }else {
                    InsertPillList vo = new InsertPillList();
                    vo.setDosagi(et_dosagi.getText().toString());
                    vo.setElementList(pillList);
                    vo.setName(et_DiseaseName.getText().toString());
                    vo.setUserId(LoginActivity.userVO.getId());
                    vo.setAlarmList(f_id);
                    Log.d("rrr", f_id.get(0));
                    vo.setTimeList(time);
                    System.out.println(vo.getUserId() + "!!!!!!!!!!!!!et_dosagi" + et_dosagi.getText().toString() + "  pilllist" + pillList + "et_diseasename " + et_DiseaseName.getText().toString() + "time " + time);
                    Call<StatusVO> call = userService.postMyInserttPill(vo);
                    call.enqueue(new Callback<StatusVO>() {
                        @Override
                        public void onResponse(Call<StatusVO> call, Response<StatusVO> response) {
                            StatusVO statusVO = response.body();
                            System.out.println("############" + statusVO.getStatus());
                            if (statusVO.getStatus().equals("201")) {
                                Toast.makeText(getApplicationContext(), "등록 성공", Toast.LENGTH_LONG).show();
                                InsertPillActivity.user_id = LoginActivity.userVO.getId();
                                finish();
                            } else if (statusVO.getStatus().equals("403"))
                                Toast.makeText(getApplicationContext(), "약 중복", Toast.LENGTH_LONG).show();
                            else if (statusVO.getStatus().equals("500"))
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<StatusVO> call, Throwable t) {

                        }
                    });
                }

            }
        });


        minus_count.setOnClickListener(this);
        plus_count.setOnClickListener(this);

        Intent resultIntent = new Intent();
        setResult(5000,resultIntent);
    }

    private TimePickerDialog.OnTimeSetListener listener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 설정버튼 눌렀을 때
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            if(String.valueOf(hourOfDay).length() < 2)
                h = "0"+String.valueOf(hourOfDay);
            else
                h = String.valueOf(hourOfDay);
            if(String.valueOf(minute).length() < 2)
                m = "0" + String.valueOf(minute);
            else
                 m = String.valueOf(minute);

            tv_1h.setText(h + "시 ");
            tv_1m.setText(m + "분");
            time.add(h+m);
        }
    };
    private TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 설정버튼 눌렀을 때
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            if(String.valueOf(hourOfDay).length() < 2)
                h = "0"+String.valueOf(hourOfDay);
            else
                h = String.valueOf(hourOfDay);
            if(String.valueOf(minute).length() < 2)
                m = "0" + String.valueOf(minute);
            else
                m = String.valueOf(minute);
            tv_2h.setText(h + "시 ");
            tv_2m.setText(m + "분");
            time.add(h+m);
        }
    };
    private TimePickerDialog.OnTimeSetListener listener3 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 설정버튼 눌렀을 때
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            if(String.valueOf(hourOfDay).length() < 2)
                h = "0"+String.valueOf(hourOfDay);
            else
                h = String.valueOf(hourOfDay);
            if(String.valueOf(minute).length() < 2)
                m = "0" + String.valueOf(minute);
            else
                m = String.valueOf(minute);
            tv_3h.setText(h + "시 ");
            tv_3m.setText(m + "분");
            time.add(h+m);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if(requestCode == 3000) {
                int number = Integer.parseInt(data.getStringExtra("number"));
                pillList.add(number);
                et_Pill.setText(data.getStringExtra("result"));
                et_Pill.setEnabled(false);
                bt_add.setEnabled(false);
            }
            if(requestCode == 9000) {
                int size = 0;
                if (data.getStringExtra("status").equals("111")) {
                    Log.d("datadddd",String.valueOf(data));
                    Log.d("dataSize", String.valueOf(data.getExtras().size()));
                    for (int i = 0; i < Integer.parseInt(data.getStringExtra("list_size")); i++) {
                        if (data.getStringExtra("name" + i).equals("null")) {
                        } else {
                            f_id.add(data.getStringExtra("id" + i));
                            alarm_f_list.add(data.getStringExtra("name" + i));
                            Log.d("data1", data.getStringExtra("name" + i) + "/" + data.getStringExtra("id" + i));
                            size++;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    bt_AlarmReciveFamily.setEnabled(false);
                    ViewGroup.LayoutParams params = lv_alarmFamily.getLayoutParams();
                    params.height = 200 * size;
                    lv_alarmFamily.setLayoutParams(params);
                    lv_alarmFamily.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minus_count:
               if(Integer.parseInt(et_dosagi.getText().toString())<=0){
                   Toast.makeText(getApplicationContext(),"복욕횟수를 차감할 수 없습니다.",Toast.LENGTH_LONG).show();
               }
               else{
                   int count = Integer.parseInt(et_dosagi.getText().toString());
                   count--;
                   System.out.print(count);
                   et_dosagi.setText(String.valueOf(count));
                   //e_dosagi.setText(et_dosagi.getText().toString().substring(0,1)+String.valueOf(count)+et_dosagi.getText().toString().substring(2));
               }
               break;
            case R.id.plus_count:
                int count = Integer.parseInt(et_dosagi.getText().toString());
                count++;
                System.out.print(count);
                et_dosagi.setText(String.valueOf(count));
                break;

        }

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
        textView.setText("직접 등록");
        textView.setGravity(Gravity.CENTER);
//        textView.setGravity(Gravity.CENTER);
        actionBar.setTitle(textView.getText().toString());



        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        actionBar.setCustomView(view,layoutParams);
    }

}

package yaksok.dodream.com.yaksok;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.js.InsertPillScroll;
import yaksok.dodream.com.yaksok.js.InsertPillScrollQR;
import yaksok.dodream.com.yaksok.adapter.ListViewAdapter;
import yaksok.dodream.com.yaksok.js.MyMedicineResponseTypeVO;
import yaksok.dodream.com.yaksok.js.PillListItem;
import yaksok.dodream.com.yaksok.service.DeleteService;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.DeleteMyMeidicineVO;
import yaksok.dodream.com.yaksok.vo.FamilyBodyVO;

public class InsertPillActivity extends AppCompatActivity{

    Button bt_Insert, bt_QRScan;
    private IntentIntegrator qrScan;
    String qrResult;

    Retrofit retrofit;
    UserService userService;

    ListView lv_MyPill;
    ListViewAdapter adapter;

    public static String user_id;
    public AlertDialog.Builder dialog;

    private ArrayList<PillListItem> pillListItems = new ArrayList<PillListItem>() ;
    public String regidate,name;
    public  int myMedicineNo,selectMyMedi;
    public DeleteService deleteService;
    public MyMedicineResponseTypeVO myMedicineResponseTypeVO = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.chat2);
        actionBar.setTitle("약 등록");
        setContentView(R.layout.activity_insertpill);


        user_id = LoginActivity.userVO.getId();

        bt_Insert = (Button)findViewById(R.id.bt_Insert);
        bt_QRScan = (Button)findViewById(R.id.bt_QRScan);
        lv_MyPill = (ListView)findViewById(R.id.lv_MyPill);

        adapter = new ListViewAdapter(getApplicationContext(),pillListItems,R.layout.pill_list_item);
        lv_MyPill.setAdapter(adapter);


       dialog = new AlertDialog.Builder(this);


        qrScan = new IntentIntegrator(this);

        bt_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InsertPillScroll.class);
                startActivityForResult(intent,5000);
            }
        });


        //QR코드 스캔
        bt_QRScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("QR코드를 스캔하세요");
                qrScan.setOrientationLocked(false); //디폴트는 가로인데 세로일 경우 세로로 바꾸는 함
                qrScan.setCaptureActivity(CaptureActivity.class);
                qrScan.initiateScan();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(userService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        deleteService = retrofit.create(DeleteService.class);

        Call<MyMedicineResponseTypeVO> call = userService.getMymediciens(LoginActivity.userVO.getId());
        call.enqueue(new Callback<MyMedicineResponseTypeVO>() {
            @Override
            public void onResponse(Call<MyMedicineResponseTypeVO> call, Response<MyMedicineResponseTypeVO> response) {
                myMedicineResponseTypeVO = response.body();
                //1System.out.println("############" + myMedicineResponseTypeVO.getStatus());
                if (myMedicineResponseTypeVO.getStatus().equals("200")) {
                    for(int i=0; i<myMedicineResponseTypeVO.getResult().size(); i++) {
                        adapter.addItem(myMedicineResponseTypeVO.getResult().get(i).getName());
                        myMedicineNo = myMedicineResponseTypeVO.getResult().get(i).getMyMedicineNo();
                        Log.d("meddddi",""+myMedicineNo);

                        lv_MyPill.setAdapter(adapter);
                    }
                } else if (myMedicineResponseTypeVO.getStatus().equals("204"))
                    Toast.makeText(getApplicationContext(), "등록된 약이 없습니다", Toast.LENGTH_LONG).show();
                else if (myMedicineResponseTypeVO.getStatus().equals("500"))
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<MyMedicineResponseTypeVO> call, Throwable t) {

            }
        });

        Intent resultIntent = new Intent();
        setResult(4000,resultIntent);


        lv_MyPill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                PillListItem pillListItem = new PillListItem();
                pillListItem = (PillListItem)(lv_MyPill.getItemAtPosition(position));
                selectMyMedi = position;
                showDialog(myMedicineResponseTypeVO.getResult().get(position).getName(),
                        myMedicineResponseTypeVO.getResult().get(position).getRegiDate(),
                        myMedicineResponseTypeVO.getResult().get(position).getMyMedicineNo());
//                Call<MyMedicineResponseTypeVO> call = userService.getMymediciens(LoginActivity.userVO.getId());
//                final PillListItem finalPillListItem = pillListItem;
//                call.enqueue(new Callback<MyMedicineResponseTypeVO>() {
//                    @Override
//                    public void onResponse(Call<MyMedicineResponseTypeVO> call, Response<MyMedicineResponseTypeVO> response) {
//                        MyMedicineResponseTypeVO myMedicineResponseTypeVO = response.body();
//                        //1System.out.println("############" + myMedicineResponseTypeVO.getStatus());
//                        if (myMedicineResponseTypeVO.getStatus().equals("200")) {
////                            for(int i=0; i<myMedicineResponseTypeVO.getResult().size(); i++) {
////                                if(myMedicineResponseTypeVO.getResult().get(i).getName().equals(finalPillListItem.getName())){
////                                    name = myMedicineResponseTypeVO.getResult().get(i).getName();
////                                    regidate = myMedicineResponseTypeVO.getResult().get(i).getRegiDate();
////                                    myMedicineNo = myMedicineResponseTypeVO.getResult().get(i).getMyMedicineNo();
////
////                                    selectMyMedi = position;
////                                   //showDialog(finalPillListItem3.getName(),regidate);
////                                    Log.d("usermedi",regidate+"   "+name+" "+myMedicineNo);
////                                    showDialog(name,regidate,myMedicineNo);
////                                }
////
////                            }
//                        } else if (myMedicineResponseTypeVO.getStatus().equals("204"))
//                            Toast.makeText(getApplicationContext(), "등록된 약이 없습니다", Toast.LENGTH_LONG).show();
//                        else if (myMedicineResponseTypeVO.getStatus().equals("500"))
//                            Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<MyMedicineResponseTypeVO> call, Throwable t) {
//
//                    }
//                });

//






            }
        });
    }




    //QR스캔후 정보를 파싱한다(해당 액티비티에서 QR스캐너 띄움)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {

            } else {
                //qrcode 결과가 있으면
                Toast.makeText(this, "스캔완료!", Toast.LENGTH_SHORT).show();
                System.out.println("@@@@@@@@@@@@@@@@@@@@Respone" + result.getContents());//  반환값 뭔지 확인
                qrResult = result.getContents();
                StringTokenizer tokens = new StringTokenizer(qrResult);
                Intent intent = new Intent(getApplicationContext(), InsertPillScroll.class);

                //QR로 찍은 정보 토큰으로 나누어 스트링에 저장 후 인텐트에 담아 다음 액티비티로 넘김
                String num1 = tokens.nextToken(",");
                String num2 = tokens.nextToken(",");
                String num3 = tokens.nextToken(",");
                String pill1 = tokens.nextToken(",");
                String pill2 = tokens.nextToken(",");
                String pill3 = tokens.nextToken(",");
                String dosagi = tokens.nextToken(",");
                intent.putExtra("num1",num1);
                intent.putExtra("num2",num2);
                intent.putExtra("num3",num3);
                intent.putExtra("pill1",pill1);
                intent.putExtra("pill2",pill2);
                intent.putExtra("pill3",pill3);
                intent.putExtra("dosagi",dosagi);
                intent.putExtra("QRkey","123");
                startActivityForResult(intent,5000);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == 5000) {
            finish();
            startActivity(new Intent(this, InsertPillActivity.class));
        }

    }
    public void showDialog(String name, String regidate, final int myMedicineNo){

        dialog.setTitle("약 등록 확인");
        dialog.setMessage("등록된 약 이름은  \n"+name+"\n"+regidate.substring(0,10));
        dialog.setCancelable(false);



        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DeleteMyMeidicineVO myMedicineNoVO1 = new DeleteMyMeidicineVO();
                myMedicineNoVO1.setMyMedicineNo(myMedicineNo);

                Log.d("aaaaa",""+myMedicineNoVO1.getMyMedicineNo());

                Call<FamilyBodyVO> myMedicineNoVOCall = deleteService.deleteMyMedicine(myMedicineNoVO1);
//yaksok.dodream.com.yaksok.vo.DeleteMyMeidicineVO@2db64c7
                Log.d("aaaaa",""+myMedicineNoVO1);

                myMedicineNoVOCall.enqueue(new Callback<FamilyBodyVO>() {
                    @Override
                    public void onResponse(Call<FamilyBodyVO> call, Response<FamilyBodyVO> response) {
                        FamilyBodyVO meDiDelete = response.body();

                        if(meDiDelete.getStatus().equals("201")){
                            pillListItems.remove(selectMyMedi);
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                        }
                        else if(meDiDelete.getStatus().equals("500")){
                            Toast.makeText(getApplicationContext(),"서버 오류입니다.",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FamilyBodyVO> call, Throwable t) {

                    }
                });

            }
        });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();





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
        textView.setText("약 등록");
        textView.setGravity(Gravity.CENTER);
//        textView.setGravity(Gravity.CENTER);
        actionBar.setTitle(textView.getText().toString());



        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        actionBar.setCustomView(view,layoutParams);
    }


}

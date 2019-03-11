package yaksok.dodream.com.yaksok;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.adapter.FamilyFindAdapter;
import yaksok.dodream.com.yaksok.item.FamilyItem;
import yaksok.dodream.com.yaksok.service.DeleteService;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.FamilyBodyVO;
import yaksok.dodream.com.yaksok.vo.FamilyDelVO;
import yaksok.dodream.com.yaksok.vo.FamilyVO;
import yaksok.dodream.com.yaksok.vo.BodyVO;
import yaksok.dodream.com.yaksok.vo.FindFamilyVO;

public class AddYourFmaily extends AppCompatActivity {
    public EditText fmaily_number_edt;
    public Button family_find_btn, family_find_skip_btn,complete_btn,deleteFamilyBtn;
    public ListView family_list_view;
    public Retrofit retrofit,retorofit2;
    public UserService userService;
    public DeleteService deleteService;
    public FamilyFindAdapter adapter;
    public ArrayList<FamilyItem> familyItems;
    public  AlertDialog.Builder dialog;
    public static FamilyVO familyVO;
    public String family_user_id = "";
    public boolean isAddedFamily = false;//김대표
    Intent intent;
    String itForSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.chat2);
        actionBar.setTitle("나의 가족 등록");
        setContentView(R.layout.activity_find_your_fmailies);
        intent = new Intent(getIntent());
        itForSignUp = intent.getStringExtra("x");
        //Log.d("aaaaaaaaaaaaa",itForSignUp);

        fmaily_number_edt = (EditText) findViewById(R.id.findFamily_edt);
        family_find_btn = (Button) findViewById(R.id.findFamily_btn);
        family_list_view = (ListView) findViewById(R.id.family_list);
        family_find_skip_btn = (Button) findViewById(R.id.family_skip_btn);
        complete_btn = (Button)findViewById(R.id.family_complete_btn);




       // intent.putExtra("signUp","itAboutSignUp");



        if(itForSignUp == null)
        {
            family_find_skip_btn.setVisibility(View.GONE);

        }
        else if(itForSignUp.equals("xx")){

            family_find_skip_btn.setVisibility(View.VISIBLE);

        }





        //스킵버튼
        family_find_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("aaaaaaaaaaaaaaaLoginId",""+LoginActivity.userVO.getId());

                LoginActivity.editor.putString("id",LoginActivity.userVO.getId());
                Log.d("aaaaaaaaaaaaaaa",""+LoginActivity.loginInformation.getBoolean("auto",true)+LoginActivity.loginInformation.getString("id",LoginActivity.userVO.getId()));
                Log.d("aaaaaaaaaaaaaaaid",""+LoginActivity.userVO.getId());


                if(LoginActivity.loginInformation.getBoolean("auto",true)){
                    LoginActivity.editor.putString("id",LoginActivity.userVO.getId());
                }
                startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
                finish();
            }
        });

        /*
            스킵 버튼 구현 그리고 인텐트 값 받아온 것을 이용해서
            skip btn 보여주기 , 안 보여주기 나눔
         */




         dialog = new AlertDialog.Builder(this);

        familyItems = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(userService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);



        retorofit2 = new Retrofit.Builder()
                .baseUrl(deleteService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        deleteService = retrofit.create(DeleteService.class);



        adapter = new FamilyFindAdapter(this,familyItems,R.layout.family_list_item);


       alreadyConnectedFamily();


//        for(int i  = 0; i < familyItems.size(); i++){

        //}
        family_find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fmaily_number_edt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력하세요", Toast.LENGTH_LONG).show();
                }
                else {
                    Call<FindFamilyVO> findFamilyVOCall = userService.getUserList(fmaily_number_edt.getText().toString(), "phoneNumber");
                    findFamilyVOCall.enqueue(new Callback<FindFamilyVO>() {
                        @Override
                        public void onResponse(Call<FindFamilyVO> call, Response<FindFamilyVO> response) {
                            final FindFamilyVO findFamilyVO = response.body();

                            if (findFamilyVO.getStatus().equals("200")) {
                                isAddedFamily = true;
                                for (int i = 0; i < findFamilyVO.getResult().size(); i++) {
                                    if (findFamilyVO.getResult().get(i).getPhoneNumber().equals(LoginActivity.userVO.getPhoneNumber())) {
                                    } else {
                                        if(LoginActivity.userVO.getPhoneNumber().equals(findFamilyVO.getResult().get(i).getPhoneNumber())){

                                        }
                                        else {
                                            adapter.addItem(findFamilyVO.getResult().get(i).getNickName() + "/" + findFamilyVO.getResult().get(i).getUserId());
                                            adapter.setNameToId(findFamilyVO.getResult().get(i).getUserId());
                                            //Log.d("ddddddd",id);
                                            family_list_view.setAdapter(adapter);
                                            familyVO = new FamilyVO();
                                            //Log.d("bbbbbbbbbbb",""+family_list_view.getMeasuredHeight());


                                            family_user_id = findFamilyVO.getResult().get(i).getUserId();
                                        }
                                    }
                                }


//                              if(family_list_view != null){
////                                  family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                      @Override
////                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////
////                                                                        }


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
            }
        });

        family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((FamilyItem)adapter.getItem(position)).getName();
                dialog.setTitle("가족찾기");
                dialog.setMessage(name+"을 가족으로 등록 하시겠습니까?");
                dialog.setCancelable(false);
                String user_id = "";

                fmaily_number_edt.setText("");
                FamilyItem familyItem = (FamilyItem)family_list_view.getItemAtPosition(position);
                String user2_id = familyItem.getName();

                int user_index = user2_id.indexOf("/")+1;

                user2_id = user2_id.substring(user_index);

                //final String finalUser_id = user_id;
                final String finalUser2_id = user2_id;
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fmaily_number_edt.setText("");

                        final FamilyVO familyVO = new FamilyVO();
                        familyVO.setUser_1(LoginActivity.userVO.getId());
                        familyVO.setUser_2(finalUser2_id);
                        //code
                        //201 : OK
                        //403 : 삽입시 중복
                        //500 : Server Error
                        Call<BodyVO> call = userService.postRegisterFamily(familyVO);
                        call.enqueue(new Callback<BodyVO>() {
                            @Override
                            public void onResponse(Call<BodyVO> call, Response<BodyVO> response) {
                                BodyVO bodyVO = response.body();
                                assert bodyVO != null;
                                switch (bodyVO.getStatus()) {
                                    case "201":
//                                        adapter.addItem(bodyVO.getResult().getNickName()+"/"+bodyVO.getResult().getUserId());
//                                        family_list_view.setAdapter(adapter);
                                        Toast.makeText(getApplicationContext(), "가족 추가가 되었습니다.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "403":
                                        Toast.makeText(getApplicationContext(), "삽입시 중복이 됩니다.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "500":
                                        Toast.makeText(getApplicationContext(), "서버 에러", Toast.LENGTH_LONG).show();
                                        break;
                                }

                            }

                            @Override
                            public void onFailure(Call<BodyVO> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fmaily_number_edt.setText("");
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();


            }
        });






        //삭제할때
        family_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final String name = ((FamilyItem)adapter.getItem(position)).getName();
                FamilyItem familyItem = (FamilyItem)family_list_view.getItemAtPosition(position);
                String user2_id = familyItem.getName();

                int user_index = user2_id.indexOf("/")+1;

                user2_id = user2_id.substring(user_index);
                Log.d("user222222",user2_id);

                dialog.setTitle("가족 삭제");
                dialog.setMessage(name+"을 삭제 하시겠습니까?");
                dialog.setCancelable(false);

                final String finalUser2_id = user2_id;
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("user_id_2",name);
                        FamilyDelVO familyDelVO = new FamilyDelVO();
                        familyDelVO.setUser_1(LoginActivity.userVO.getId());
                        familyDelVO.setUser_2(finalUser2_id);
                        Log.d("aaaaaaaa",family_user_id);

                        Call<FamilyBodyVO> delectionCall = deleteService.deleteBody(familyDelVO);

                        delectionCall.enqueue(new Callback<FamilyBodyVO>() {
                            @Override
                            public void onResponse(Call<FamilyBodyVO> call, Response<FamilyBodyVO> response) {
                                FamilyBodyVO familyBodyVO = response.body();
                                if(familyBodyVO.getStatus().equals("201")){
                                    Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_LONG).show();

                                    familyItems.remove(position);
                                    adapter.notifyDataSetChanged();
                                    adapter.notifyDataSetInvalidated();

                                    // alreadyConnectedFamily();
                                }else if(familyBodyVO.getStatus().equals("500")){
                                    Toast.makeText(getApplicationContext(),"서버 에러입니다.",Toast.LENGTH_LONG).show();

                                }

                            }


                            @Override
                            public void onFailure(Call<FamilyBodyVO> call, Throwable t) {

                            }
                        });
                    }

                });

                dialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();






                return false;
            }
        });




        //완료버튼
        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),LoginActivity.userVO.getId(),Toast.LENGTH_LONG).show();
                if(isAddedFamily){
                    Log.d("aaaaaaaaaaaaaaa",""+LoginActivity.loginInformation.getBoolean("auto",true));
                    if(LoginActivity.loginInformation.getBoolean("auto",true)){
                        LoginActivity.editor.putString("id",LoginActivity.userVO.getId());
                    }
                    Log.d("aaaaaaaaavvvvvvvvvv",LoginActivity.loginInformation.getString("id",""));
                    startActivity(new Intent(getApplicationContext(),MainPageActivity.class));

                    finish();
                }
                if(itForSignUp == null) {


                    if(LoginActivity.loginInformation.getBoolean("auto",true)){
                        LoginActivity.editor.putString("id",LoginActivity.userVO.getId());
                        startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
                        finish();
                    }


                }

                /*
                    완료될 떄 원래 가족을 한 사람이라도 지정 한 사람들만 완료 버튼을 누를 수 있게 했는데
                    이건 첫 회원가입 떄 만 필요한거고 다음번에 가족 등록을 안 하더라도 메인 엑티비티로 넘어갈 수
                    있게 해줌.
                 */


            }
        });

    }


    private void alreadyConnectedFamily() {

        userService = retrofit.create(UserService.class);

        Call<FindFamilyVO> findFamilyVOCall = userService.getConnectedFamilyInfo(LoginActivity.userVO.getId());
        findFamilyVOCall.enqueue(new Callback<FindFamilyVO>() {
            @Override
            public void onResponse(Call<FindFamilyVO> call, Response<FindFamilyVO> response) {
                FindFamilyVO findFamilyVO = response.body();

                if (findFamilyVO.getStatus().equals("200")) {

                    for(int i = 0; i < findFamilyVO.getResult().size();i++){
                        adapter.addItem(findFamilyVO.getResult().get(i).getNickName()+"/"+findFamilyVO.getResult().get(i).getUserId());
                        family_list_view.setAdapter(adapter);
                    }
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

        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent resultIntent = new Intent();
        setResult(4000,resultIntent);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),MainPageActivity.class);
//                startActivityForResult(intent,7777);
                finish();
            }
        });
        textView.setText("나의 가족 등록");
        textView.setGravity(Gravity.CENTER);
//        textView.setGravity(Gravity.CENTER);
        actionBar.setTitle(textView.getText().toString());



        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        actionBar.setCustomView(view,layoutParams);
    }



}




        //===전화번호를 사용하여 유저의 정보의 리스트를 가지고온다=====
//HTTP GET   users/{item} ? itemTyp
//ex) /01027250856 ? itemType=phoneNumber
//
//Host:  http://54.180.81.180:8080/users/010?itemType=phoneNumber
//
//request path variable  -  item
//request query paramter - itemType
//
//response
//BODY{
//	“status” : “code”
//	“result” : “UserVO” List
//
//}
//
//code
//200 : OK
//204: 계정 존재하지않음(null반환)
//400: 잘못된 요청(itemType)
//500 : Server Error









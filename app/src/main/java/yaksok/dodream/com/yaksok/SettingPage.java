package yaksok.dodream.com.yaksok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.service.DeleteService;
import yaksok.dodream.com.yaksok.vo.FamilyBodyVO;
import yaksok.dodream.com.yaksok.vo.UserDeleteVO;

public class SettingPage extends AppCompatActivity {

    public Retrofit retrofit,retorofit2;
    public DeleteService deleteService;
    TextView id,nickname,email,phone;
    ToggleButton auto_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.chat2);
        actionBar.setTitle("설정");

        setContentView(R.layout.activity_setting_page);

        id = (TextView) findViewById(R.id.tv_setting_id);
        nickname = (TextView)findViewById(R.id.tv_setting_nickname);
        email = (TextView)findViewById(R.id.tv_setting_email);
        phone = (TextView)findViewById(R.id.tv_setting_phone);


        retorofit2 = new Retrofit.Builder()
                .baseUrl(deleteService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        deleteService = retorofit2.create(DeleteService.class);

        id.setText("아이디 : " + LoginActivity.userVO.getId());
        nickname.setText("닉네임 : " + LoginActivity.userVO.getNickname());
        email.setText("이메일 : " + LoginActivity.userVO.getEmail());
        phone.setText("전화번호 : " + LoginActivity.userVO.getPhoneNumber());


        auto_cancel = (ToggleButton) findViewById(R.id.tgbt_autoLogin);
        if(LoginActivity.autologin){
            auto_cancel.setChecked(true);
        }
        else{
            auto_cancel.setChecked(false);
            auto_cancel.setEnabled(false);
        }
        auto_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auto_cancel.isChecked()){

                }
                else {
                    LoginActivity.autologin = false;
                    LoginActivity.editor.putBoolean("auto", false);
                    LoginActivity.editor.remove("id");
                    LoginActivity.editor.remove("pw");
                    LoginActivity.editor.remove("userType");
                    LoginActivity.editor.apply();
                    finishAffinity();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });


        Button goOutBtn = (Button)findViewById(R.id.bt_secessionOUT);
        goOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDeleteVO userDeleteVO = new UserDeleteVO();
                userDeleteVO.setId(LoginActivity.userVO.getId());
                userDeleteVO.setUserType(LoginActivity.userVO.getUserType());
                Call<FamilyBodyVO> familyBodyVOCall = deleteService.deleteUser(userDeleteVO);
                familyBodyVOCall.enqueue(new Callback<FamilyBodyVO>() {
                    @Override
                    public void onResponse(Call<FamilyBodyVO> call, Response<FamilyBodyVO> response) {
                        FamilyBodyVO familyBodyVO = response.body();

                        if(familyBodyVO.getStatus().equals("201")){
                            Toast.makeText(getApplicationContext(),"탈퇴합니다.",Toast.LENGTH_SHORT).show();



//                             LoginActivity.editor.putBoolean("auto",false);
//                             LoginActivity.editor.putString("id","");
//                             LoginActivity.editor.putString("pw","");
//                             LoginActivity.editor.putString("userType","");
                              /*LoginActivity.editor.clear();
                              SharedPreferences.Editor editor = LoginActivity.loginInformation.edit();
                              editor.clear();*/

                           //  LoginActivity.editor.commit();



                          finishAffinity(); //해당 앱의 루트 액티비티를 종료시키낟.
                          System.runFinalization(); //간단히 말해 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
                          System.exit(0);//현재 엑티비티를 종료시킨다.

                            //moveTaskToBack(true);





                            finish();

                            //android.os.Process.killProcess(android.os.Process.myPid());



                        }
                    }

                    @Override
                    public void onFailure(Call<FamilyBodyVO> call, Throwable t) {

                    }
                });
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
        textView.setText("설 정");
        textView.setGravity(Gravity.CENTER);
//        textView.setGravity(Gravity.CENTER);
        actionBar.setTitle(textView.getText().toString());



        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        actionBar.setCustomView(view,layoutParams);
    }




}

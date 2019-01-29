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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.chat2);
        actionBar.setTitle("설정");
        setContentView(R.layout.activity_setting_page);


        retorofit2 = new Retrofit.Builder()
                .baseUrl(deleteService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        deleteService = retorofit2.create(DeleteService.class);


        Button auto_cancel = (Button)findViewById(R.id.auto_cancel);
        auto_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginActivity.editor.putBoolean("auto",false);
                LoginActivity.editor.remove("id");
                LoginActivity.editor.remove("pw");
                LoginActivity.editor.remove("userType");
                LoginActivity.editor.apply();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });



        Button kakao_logout = (Button)findViewById(R.id.kakao_logout);
        kakao_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        LoginActivity.editor.remove("id");
                        LoginActivity.editor.remove("pw");
                        LoginActivity.editor.remove("userType");
                        LoginActivity.editor.apply();
                    }
                });
            }
        });

        final Button naver_logout = (Button)findViewById(R.id.naver_logout);
        naver_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OAuthLogin.getInstance().logout(getApplicationContext());
                LoginActivity.editor.remove("id");
                LoginActivity.editor.remove("pw");
                LoginActivity.editor.remove("userType");
                LoginActivity.editor.apply();

            }
        });

        Button goOutBtn = (Button)findViewById(R.id.goOut_btn);
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




}

package yaksok.dodream.com.yaksok.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import yaksok.dodream.com.yaksok.vo.DeleteMyMeidicineVO;
import yaksok.dodream.com.yaksok.vo.FamilyBodyVO;
import yaksok.dodream.com.yaksok.vo.FamilyDelVO;
import yaksok.dodream.com.yaksok.vo.UserDeleteVO;
import yaksok.dodream.com.yaksok.vo.medicine.MyMedicineVO;

public interface DeleteService {
    public static String API_URL = "http://54.180.81.180:8080";

    @HTTP(method = "DELETE",path = "/families?",hasBody = true)
    Call<FamilyBodyVO> deleteBody(@Body FamilyDelVO familyDelVO);

    @HTTP(method = "DELETE",path = "users?",hasBody = true)
    Call<FamilyBodyVO> deleteUser(@Body UserDeleteVO userDeleteVO);

    @HTTP(method = "DELETE",path = "/mymedicines?",hasBody = true)
    Call<FamilyBodyVO> deleteMyMedicine(@Body DeleteMyMeidicineVO myMeidicineVO);




}

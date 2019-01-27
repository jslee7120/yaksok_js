package yaksok.dodream.com.yaksok.js;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyMedicineResponseTypeVO {


    @SerializedName("status") String status;
    @SerializedName("result") List<Result> result;
    MyMedicineResponseTypeVO(){}

    public class Result {
        @SerializedName("myMedicineNo")@Expose private int myMedicineNo;
        @SerializedName("name") @Expose private String name;
        @SerializedName("regiDate") @Expose private String regiDate;


        public int getMyMedicineNo() {
            return myMedicineNo;
        }

        public void setMyMedicineNo(int myMedicineNo) {
            this.myMedicineNo = myMedicineNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegiDate() {
            return regiDate;
        }

        public void setRegiDate(String regiDate) {
            this.regiDate = regiDate;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}

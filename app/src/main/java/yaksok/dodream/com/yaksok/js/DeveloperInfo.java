package yaksok.dodream.com.yaksok.js;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import yaksok.dodream.com.yaksok.R;

public class DeveloperInfo extends AppCompatActivity{

    ListView lv1, lv2;
    private String[] name1 = {"박성빈","박한을","홍지윤","서범준","이승준","한수정","윤예찬"};
    private String[] col1 = {"Columbia University in the City Of New York Computer Science and Mathematics",
            "성균관대학교 화학공학부",
            "홍익대학교 산업공학과",
            "강남대학교 소프트웨어응용학부",
            "숭실대학교 소프트웨어학부",
            "한양대학교 ERICA캠퍼스 산업경영공학과",
            "주식회사 스칼라데이터(SCALAR DATA Co., Ltd.) 대표이사"};
    private String[] name2 = {"박한을","혼지윤","서범준","윤성희","권재환","이재석","임명준"};
    private String[] col2 = {"성균관대학교 화학공학부",
            "홍익대학교 산업공학과",
            "강남대학교 소프트웨어응용학부",
            "강남대학교 컴퓨터미디어정보공학부",
            "강남대학교 컴퓨터공학과",
            "강남대학교 컴퓨터공학과",
            "강남대학교 컴퓨터공학과"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_info);


        lv1 = (ListView)findViewById(R.id.lv_developer1);
        lv2 = (ListView)findViewById(R.id.lv_developer2);

        ArrayList<Item> arrayList1 = new ArrayList<Item>();
        ArrayList<Item> arrayList2 = new ArrayList<Item>();

        for(int i=0; i<name1.length; i++){
            arrayList1.add(new Item(name1[i],col1[i]));
        }

        MyAdapter adapter1 = new MyAdapter(this, arrayList1);

        ViewGroup.LayoutParams params = lv1.getLayoutParams();
        params.height = 200 * 7;
        lv1.setLayoutParams(params);
        lv1.setAdapter(adapter1);

        for(int i=0; i<name2.length; i++){
            arrayList2.add(new Item(name2[i],col2[i]));
        }
        MyAdapter adapter2 = new MyAdapter(this, arrayList2);
        ViewGroup.LayoutParams params1 = lv2.getLayoutParams();
        params1.height = 200 * 7;
        lv2.setLayoutParams(params1);
        lv2.setAdapter(adapter2);


    }

}

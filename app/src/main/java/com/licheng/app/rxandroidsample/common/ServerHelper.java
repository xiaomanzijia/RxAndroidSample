package com.licheng.app.rxandroidsample.common;

import android.content.Context;

import com.google.gson.Gson;
import com.licheng.app.rxandroidsample.data.Beauty;
import com.licheng.app.rxandroidsample.data.ListBeautyCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by licheng on 29/4/16.
 */
public class ServerHelper {

    private ArrayList<Map<String, Object>> cityList;
    private ArrayList<String> citySelected;

    private Context mContext;

    public ServerHelper(Context context) {
        this.mContext = context;
        citySelected = new ArrayList<>();
        String cityBody = CommonUtil.getData(mContext,"citypy.json");
        Map<String,Object> map = new Gson().fromJson(cityBody,HashMap.class);
        cityList = (ArrayList<Map<String, Object>>) map.get("city");
    }

    private static List<Beauty> list = new ArrayList<>();

    public static List<Beauty> getBeautyList(int pageIndex, int pageSize){
        String url = "http://www.diandidaxue.com:8080/apiServer.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("opcode", "getBeauty");
        params.put("pageNum", String.valueOf(pageIndex));
        params.put("numPerPage", String.valueOf(pageSize));
        OkHttpUtils.post().url(url).params(params).build().execute(new ListBeautyCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(List<Beauty> response) {
                if(response != null){
                    list = response;
                }
            }
        });
        return list;
    }

    //根据拼音匹配城市
    public ArrayList<String> searchCity(String city){
        citySelected.clear();
        for (Map<String,Object> map1: cityList){
            if(map1.get("pinyin").toString().toLowerCase().startsWith(city.toLowerCase())){
                citySelected.add(map1.get("name").toString());
            }
        }
        return citySelected;
    }


}

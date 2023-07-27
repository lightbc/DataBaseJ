package com.lightbc.databasej.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.File;
import java.util.*;

public class JsonUtil {

    /**
     * 导出json文件
     *
     * @param savePath 保存路径
     * @param dataMap  导出数据结果集
     * @throws Exception
     */
    public void writeJson(String savePath, Map<Integer, List<Object>> dataMap) throws Exception {
        if (dataMap == null) {
            throw new Exception("没有可以导出的数据结果集！");
        }
        FileUtil fileUtil = new FileUtil();
        // 文件导出
        File file = new File(savePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        fileUtil.write(savePath, getData(dataMap));
    }

    /**
     * 获取处理过后的数据结果
     *
     * @param dataMap 处理数据
     * @return string 处理结果
     */
    private String getData(Map<Integer, List<Object>> dataMap) {
        String result = null;
        try {
            if (dataMap != null) {
                List<Map<Object, Object>> resultList = new ArrayList<>();
                List<Object> header = dataMap.get(0);
                for (Integer key : dataMap.keySet()) {
                    if (key > 0) {
                        List<Object> data = dataMap.get(key);
                        Map<Object, Object> map = new LinkedHashMap<>();
                        for (int i = 0; i < data.size(); i++) {
                            map.put(header.get(i), data.get(i));
                        }
                        resultList.add(map);
                    }
                }
                result = JSONObject.toJSONString(resultList, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
            }
        } catch (Exception e) {
        } finally {
            return result;
        }
    }
}

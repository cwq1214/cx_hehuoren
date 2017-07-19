package com.jyt.baseapp.entity;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/13.
 */

public class ScanIdCardResult {
    public List<Output> outputs;

    public class Output{
        public String outputLabel;
        public OutputValue outputValue;
    }


    public class OutputValue{
        public String dataValue;
    }

    class DataValue{
        String address;//浙江省杭州市余杭区文一西路969号
        String name;//张三
        String nationality;//汉
        String sex;//男
        String birth;//20000101
        boolean success;//true

    }
}

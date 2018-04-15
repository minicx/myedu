package com.example.demo.service.impl;

import com.example.demo.config.PayConstants;
import com.example.demo.config.PayUtil;
import com.example.demo.dao.PayRepository;
import com.example.demo.dto.PayDto;
import com.example.demo.http.HttpUtil;
import com.example.demo.service.PayService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 17:07 2018/4/14
 * @Modified By;
 */
@Service
public class PayServiceImpl implements PayService {

    @Value("${key}")
    private String key;
    // = "C89C2D1BCAF9CA258A34E59DD4BF4668";
    // 商户ID.
    @Value("${merno}")
    private String merno;
    // = "1256060676";

    @Autowired
    PayRepository payRepository;

    @Override
    public List<PayDto> findAll() {
        return payRepository.findAll();
    }

    @Override
    public JsonObject pay(PayDto payDto) {
        StringBuilder builder = new StringBuilder();
        builder.append("X1_Amount").append(PayConstants.EQU).append(payDto.getAmout()).append(PayConstants.AND);
        String billNo = UUID.randomUUID().toString().replaceAll("-", "");
        builder.append("X2_BillNo").append(PayConstants.EQU).append(billNo).append(PayConstants.AND);
        builder.append("X3_MerNo").append(PayConstants.EQU).append(merno).append(PayConstants.AND);
        builder.append("X4_ReturnURL").append(PayConstants.EQU).append("http://mystore.com/payresult").append(PayConstants.AND);
        String x6 = PayUtil.EncodingMD5(builder.toString() + PayUtil.EncodingMD5(key).toUpperCase()).toUpperCase();

        payDto.setBillNo(billNo);
        payRepository.save(payDto);
        Map<String, String> map = new TreeMap<String, String>();
        map.put("X1_Amount", String.valueOf(payDto.getAmout()));
        map.put("X2_BillNo", billNo);
        map.put("X3_MerNo", merno);
        map.put("X4_ReturnURL", "http://mystore.com/payresult");
        map.put("X6_MD5info", x6);
        map.put("X5_NotifyURL", "");
        map.put("X7_PaymentType", String.valueOf(payDto.getPayMethod()));
        map.put("X8_MerRemark", "1");
        map.put("X9_ClientIp", "");
        map.put("isApp", String.valueOf(payDto.getApp()));
        System.out.println("参与签名：" + builder);
        System.out.println("上行参数：" + map);
        String httpResult = null;
        JsonObject obj = null;
        try {
            httpResult = HttpUtil.post("https://bq.baiqianpay.com/webezf/web/?app_act=openapi/bq_pay/pay", map, 2);
            System.out.println("返回参数：" + httpResult);
            obj = (JsonObject) new Gson().fromJson(httpResult, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public JsonObject query(String billNo) {

        StringBuilder builder = new StringBuilder();
        builder.append("#").append("REQ_BankOrderQuery");
//        String billNo = UUID.randomUUID().toString().replaceAll("-", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        builder.append("#").append(billNo);
        builder.append("#").append(merno);
        builder.append("#").append(time).append("#").append(key);
        String x6 = PayUtil.EncodingMD5(builder.toString());
        Map<String, String> map = new TreeMap<String, String>();
        map.put("trxType", "REQ_BankOrderQuery");
        map.put("r1_orderNumber", billNo);
        map.put("merchantNo", merno);

        map.put("timestamp", time);
        map.put("sign", x6);
        System.out.println("参与签名：" + builder);
        System.out.println("上行参数：" + map);
        JsonObject obj = null;
        try {
            String httpResult = HttpUtil.post("https://bq.baiqianpay.com/webezf/web/?app_act=openapi/bq_pay/query", map, 2);
            System.out.println("返回参数：" + httpResult);
            obj = (JsonObject) new Gson().fromJson(httpResult, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public PayDto update(PayDto book) {
        return null;
    }

    @Override
    public PayDto delete(String billNo) {
        PayDto payDto = payRepository.findByBillNo(billNo);
        payRepository.delete(payDto);
        return payDto;
    }

    @Override
    public PayDto findById(String billNo) {
        return payRepository.findByBillNo(billNo);
    }
}
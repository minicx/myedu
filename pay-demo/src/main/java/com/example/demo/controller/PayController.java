package com.example.demo.controller;

import com.example.demo.config.PayConstants;
import com.example.demo.config.PayUtil;
import com.example.demo.dao.PayRepository;
import com.example.demo.dto.NotifyDto;
import com.example.demo.dto.PayDto;
import com.example.demo.dto.ReturnDto;
import com.example.demo.http.HttpAPIService;
import com.example.demo.http.HttpUtil;
import com.example.demo.service.PayService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lxg
 * on 2017/2/6.
 */
@Controller
public class PayController {
    @Resource
    private PayService payService;

    @Value("${merno}")
    private String merno;

    @Value("${key}")
    private String key;

    @Autowired
    PayRepository payRepository;


    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map) {
        map.addAttribute("payDto", new PayDto());
        map.addAttribute("action", "pay");
        return "/index.html";
    }

    /**
     * 支付
     *
     * @param payDto
     * @return
     * @throws Exception
     */
//    @RequestMapping(value = "/pay", method = RequestMethod.POST)
//    public String pay(@ModelAttribute PayDto payDto, ModelMap map) throws Exception {
//        JsonObject jsonObject =  payService.pay(payDto);
//        map.addAttribute("result", jsonObject.toString());
//        return "result";
//    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(@ModelAttribute PayDto payDto, ModelMap map) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("X1_Amount").append(PayConstants.EQU).append(String.valueOf(payDto.getAmout())).append(PayConstants.AND);
        String billNo = UUID.randomUUID().toString().replaceAll("-", "");
        builder.append("X2_BillNo").append(PayConstants.EQU).append(billNo).append(PayConstants.AND);
        builder.append("X3_MerNo").append(PayConstants.EQU).append(merno).append(PayConstants.AND);
        builder.append("X4_ReturnURL").append(PayConstants.EQU).append("http://118.25.52.126:8080/returnUrl").append(PayConstants.AND);
        String x6 = PayUtil.EncodingMD5(builder.toString() + PayUtil.EncodingMD5(key).toUpperCase()).toUpperCase();

        payDto.setBillNo(billNo);
        payRepository.save(payDto);
        map.addAttribute("action", "http://bq.baiqianpay.com/webezf/web/?app_act=openapi/bq_pay/pay");
        map.addAttribute("X1_Amount", String.valueOf(payDto.getAmout()));
        map.addAttribute("X2_BillNo", billNo);
        map.addAttribute("X3_MerNo", merno);
        map.addAttribute("X4_ReturnURL", "http://118.25.52.126:8080/returnUrl");
        map.addAttribute("X6_MD5info", x6);
        map.addAttribute("X5_NotifyURL", "http://118.25.52.126:8080/notify");
        map.addAttribute("X7_PaymentType", String.valueOf(payDto.getPayMethod()));
        map.addAttribute("X8_MerRemark", "1");
        map.addAttribute("X9_ClientIp", "");
        map.addAttribute("isApp", String.valueOf(payDto.getApp()));
        System.out.println("参与签名：" + builder);
        System.out.println("上行参数：" + map);
        return "pay";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap map) {
        map.addAttribute("payList", payService.findAll());
        return "/payList.html";
    }

    @RequestMapping(value = "/query/{payId}", method = RequestMethod.GET)
    public String query(@PathVariable String payId, ModelMap map) throws Exception {
        JsonObject jsonObject =  payService.query(payId);
        map.addAttribute("result", jsonObject.toString());
        return "result";
    }


    @RequestMapping(value = "/notify", method = RequestMethod.GET)
    public String notify(@PathVariable NotifyDto notifyDto, ModelMap map) throws Exception {
        System.out.println(notifyDto);
        return "result";
    }

    @RequestMapping(value = "/returnUrl", method = RequestMethod.GET)
    public String notify(@PathVariable ReturnDto returnDto, ModelMap map) throws Exception {
        System.out.println(returnDto);
        return "result";
    }



    public static void main(String[] args) throws Exception {

//        String str = "{\"MerNo\":\"1531985336\",\"Amount\":\"1.71\",\"BillNo\":\"1523630125961085\",\"Succeed\":88," +
//                "\"MD5info\":\"B75D16E461B366323E14F99DEACA5309\",\"Result\":\"\\u652f\\u4ed8\\u6210\\u529f\",\"MerRemark\":\"\"}";
//        JsonObject obj = (JsonObject)new Gson().fromJson(str, JsonObject.class);
//        System.out.println(obj.toString());
//                new HttpClientController().pay();
//        new PayController().query();
    }
}

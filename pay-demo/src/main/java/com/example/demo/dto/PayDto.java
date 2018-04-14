package com.example.demo.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Book 实体类
 *
 * Created by bysocket on 30/09/2017.
 */
@Entity
public class PayDto implements Serializable {

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private String billNo;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 支付金额
     */
    private Double amout;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 是否app支付
     */
    private String app;

    @Override
    public String toString() {
        return "PayDto{" +
                "id=" + id +
                ", billNo='" + billNo + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", amout=" + amout +
                ", cardNo='" + cardNo + '\'' +
                ", app=" + app +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Double getAmout() {
        return amout;
    }

    public void setAmout(Double amout) {
        this.amout = amout;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

}

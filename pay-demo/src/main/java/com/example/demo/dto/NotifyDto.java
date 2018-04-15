package com.example.demo.dto;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 16:25 2018/4/15
 * @Modified By;
 */
public class NotifyDto {

    /**
     * 商户ID.	>=9
     */
    private String merNo;

    /**
     * 该笔订单总金额(元).	1­12
     */
    private String amount;

    /**
     * 商户网站产生的订单号	50
     */
    private String billNo;

    /**
     * 支付返回结果状态码(支付状态码 对应消息请参考附录).	10	88
     */
    private String succeed;

    /**
     * Return参数签名	50	MD5("Amount=100.01&BillN o=1688888888&MerNo=168 885&Succeed= 88&"+MD5(12345678).toU pperCase()).toUpperC ase()
     */
    private String mD5info;

    /**
     * 支付状态说明	50
     */
    private String result;

    /**
     * 商户自定义备注信息.	200
     */
    private String merRemark;

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getmD5info() {
        return mD5info;
    }

    public void setmD5info(String mD5info) {
        this.mD5info = mD5info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMerRemark() {
        return merRemark;
    }

    public void setMerRemark(String merRemark) {
        this.merRemark = merRemark;
    }

    @Override
    public String toString() {
        return "NotifyDto{" +
                "merNo='" + merNo + '\'' +
                ", amount='" + amount + '\'' +
                ", billNo='" + billNo + '\'' +
                ", succeed='" + succeed + '\'' +
                ", mD5info='" + mD5info + '\'' +
                ", result='" + result + '\'' +
                ", merRemark='" + merRemark + '\'' +
                '}';
    }
}
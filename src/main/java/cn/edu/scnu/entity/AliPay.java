package cn.edu.scnu.entity;
import lombok.Data;

@Data
public class AliPay {
    private String traceNo;// 商户订单号
    private double totalAmount;// 订单金额
    private String subject; // 订单标题
    private String alipayTraceNo;//支付宝订单号
}


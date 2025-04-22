package cn.edu.scnu.controller;

import cn.edu.scnu.Service.TbMemberService;
import cn.edu.scnu.config.AliPayConfig;
import cn.edu.scnu.entity.AliPay;
import cn.edu.scnu.mapper.TbMemberMapper;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.easysdk.factory.Factory;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {
    @Resource
    AliPayConfig aliPayConfig;

    @Resource
    private TbMemberMapper tbMemberMapper;
    @Resource
    private TbMemberService tbMemberService;
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";

//    private final String RETURN_URL = "http://127.0.0.1:8090/alipay/returnUrl";

    @GetMapping("/pay")
    public void pay(AliPay aliPay, @RequestParam("memberId") String memberId, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();


        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl("http://127.0.0.1:8090/alipay/returnUrl?memberId="+memberId);


        String bizContent = "{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}";

        request.setBizContent(bizContent);

        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping("/returnUrl")
    public String returnUrlMethod(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        System.out.println("=================================同步回调=====================================");
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
            // System.out.println(name + " = " + request.getParameter(name));
        }

        String tradeNo = params.get("out_trade_no");
        String gmtPayment = params.get("gmt_payment");
        String alipayTradeNo = params.get("trade_no");
        // 支付宝验签
        if (Factory.Payment.Common().verifyNotify(params)) {
            // 验签通过
            System.out.println("交易名称: " + params.get("subject"));
            System.out.println("交易状态: " + params.get("trade_status"));
            System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
            System.out.println("商户订单号: " + params.get("out_trade_no"));
            System.out.println("交易金额: " + params.get("total_amount"));
            System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
            System.out.println("买家付款时间: " + params.get("gmt_payment"));
            System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
        }

        String memberId = request.getParameter("memberId");
        System.out.println("Member ID is: " + memberId);
        tbMemberService.updateVipStatus(Integer.valueOf(memberId),1);

//        boolean updateResult = tbMemberService.updateVipStatus(7, 1);
//        if (updateResult) {model.addAttribute("vipUpdated", true);}

        return "redirect:/index/toLogin";
    }
}


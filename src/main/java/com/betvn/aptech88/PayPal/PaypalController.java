package com.betvn.aptech88.PayPal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaypalController {

    @Autowired
    PaypalService service;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
    
    
    @PostMapping("/pay")
    public ResponseEntity<String> payment(@RequestBody Order order) {
        try {
            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://192.168.1.7:8080/" + CANCEL_URL,
                    "http://192.168.1.7:8080/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                	String okela=link.getHref();
					return ResponseEntity.status(404).body(okela);
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return null;
    }

     @GetMapping(value = CANCEL_URL)
        public String cancelPay() {
            return "cancel";
        }

        @GetMapping(value = SUCCESS_URL)
        public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
            try {
                Payment payment = service.executePayment(paymentId, payerId);
                System.out.println(payment.toJSON());
                if (payment.getState().equals("approved")) {
                	return "success";
                }
            } catch (PayPalRESTException e) {
             System.out.println(e.getMessage());
            }
            return null;
        }

}
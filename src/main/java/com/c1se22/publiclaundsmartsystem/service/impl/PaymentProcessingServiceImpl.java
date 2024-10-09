package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.exception.PaymentProcessingException;
import com.c1se22.publiclaundsmartsystem.payload.CheckoutResponseDto;
import com.c1se22.publiclaundsmartsystem.payload.CreatePaymentLinkRequestBody;
import com.c1se22.publiclaundsmartsystem.payload.PaymentLinkDto;
import com.c1se22.publiclaundsmartsystem.payload.PayosTransactionDto;
import com.c1se22.publiclaundsmartsystem.service.PaymentProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentProcessingServiceImpl implements PaymentProcessingService {
    PayOS payOS;
    @Override
    public CheckoutResponseDto createPaymentLink(CreatePaymentLinkRequestBody RequestBody) {
        try {
            final String productName = RequestBody.getProductName();
            final String description = RequestBody.getDescription();
            final String returnUrl = "/success";
            final String cancelUrl = "/cancel";
            final int price = RequestBody.getPrice();

            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder().name(productName).price(price).quantity(1).build();

            PaymentData paymentData = PaymentData.builder().orderCode(orderCode).description(description).amount(price)
                    .item(item).returnUrl(returnUrl).cancelUrl(cancelUrl).build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return CheckoutResponseDto.builder()
                    .accountNumber(data.getAccountNumber())
                    .accountName(data.getAccountName())
                    .amount(data.getAmount())
                    .description(data.getDescription())
                    .checkoutUrl(data.getCheckoutUrl())
                    .qrCode(data.getQrCode())
                    .orderCode(data.getOrderCode())
                    .status(data.getStatus())
                    .build();
        } catch (Exception e) {
            throw new PaymentProcessingException("Failed to create payment link. Error: "+e.getMessage());
        }
    }

    @Override
    public PaymentLinkDto getPaymentLinkData(long paymentLinkId) {
        try{
            PaymentLinkData data = payOS.getPaymentLinkInformation(paymentLinkId);
            return mapToPaymentLinkDto(data);
        } catch (Exception e) {
            throw new PaymentProcessingException("Failed to get payment link data. Error: "+e.getMessage());
        }
    }

    @Override
    public PaymentLinkDto cancelPaymentLink(long paymentLinkId) {
        try{
            PaymentLinkData data = payOS.cancelPaymentLink(paymentLinkId, null);
            return mapToPaymentLinkDto(data);
        } catch (Exception e) {
            throw new PaymentProcessingException("Failed to get payment link data. Error: "+e.getMessage());
        }
    }

    @Override
    public ObjectNode confirmWebhook(String webhookUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(webhookUrl);
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            return response;
        } catch (Exception e) {
            throw new PaymentProcessingException("Failed to confirm webhook. Error: "+e.getMessage());
        }
    }

    @Override
    public void payosTransferHandler(ObjectNode body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Webhook webhookBody = Webhook.builder()
                    .code(body.findValue("code").asText())
                    .desc(body.findValue("desc").asText())
                    .success(body.findValue("desc").asText().equals("success"))
                    .data(objectMapper.treeToValue(body.findValue("data"), WebhookData.class))
                    .signature(body.findValue("signature").asText())
                    .build();
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            if (webhookBody.getSuccess()){
                // TODO: Handle successful payos transfer
                // TODO: Insert the transaction data to the database
                System.out.println("Payos transfer success");
            }
        } catch (Exception e){
            throw new PaymentProcessingException("Failed to handle payos transfer. Error: "+e.getMessage());
        }
    }

    private PayosTransactionDto mapTransactionData(vn.payos.type.Transaction transaction) {
        return PayosTransactionDto.builder()
                .reference(transaction.getReference())
                .amount(transaction.getAmount())
                .accountNumber(transaction.getAccountNumber())
                .description(transaction.getDescription())
                .transactionDateTime(transaction.getTransactionDateTime())
                .build();
    }

    private PaymentLinkDto mapToPaymentLinkDto(PaymentLinkData data) {
        return PaymentLinkDto.builder()
                .id(data.getId())
                .amount(data.getAmount())
                .amountPaid(data.getAmountPaid())
                .amountRemaining(data.getAmountRemaining())
                .orderCode(data.getOrderCode())
                .status(data.getStatus())
                .createdAt(data.getCreatedAt())
                .cancellationReason(data.getCancellationReason())
                .canceledAt(data.getCanceledAt())
                .transactions(data.getTransactions().stream()
                        .map(this::mapTransactionData).collect(Collectors.toList()))
                .build();
    }
}
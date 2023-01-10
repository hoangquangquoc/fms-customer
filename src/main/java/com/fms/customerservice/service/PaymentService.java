package com.fms.customerservice.service;

import java.util.List;

import com.fms.customerservice.model.PaymentHistory;
import com.fms.customerservice.model.PaymentMapped;
import com.fms.customerservice.model.PaymentRequest;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface PaymentService {
	ResponseModel<List<PaymentHistory>> getPaymentHistory(SearchModel searchModel, UserHeader userHeader);

	ResponseModel<List<PaymentMapped>> getPaymentList(SearchModel searchModel, UserHeader userHeader);

	String renewalService(PaymentRequest paymentRequest,UserHeader userHeader);
}

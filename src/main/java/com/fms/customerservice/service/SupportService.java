package com.fms.customerservice.service;

import java.util.List;

import com.fms.customerservice.model.SupportInfoList;
import com.fms.customerservice.model.SupportSMSList;
import com.fms.customerservice.model.TipDTO;
import com.fms.customerservice.model.VideoDTO;

public interface SupportService {
	SupportInfoList getSupportInfo();

	List<SupportSMSList> getSupportSMS();
	
	List<VideoDTO> getListVideo();
	
	List<TipDTO> getListTip();
}

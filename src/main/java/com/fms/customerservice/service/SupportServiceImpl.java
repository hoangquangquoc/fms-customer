package com.fms.customerservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.SupportInfo;
import com.fms.customerservice.model.SupportInfoDTO;
import com.fms.customerservice.model.SupportInfoList;
import com.fms.customerservice.model.SupportSMS;
import com.fms.customerservice.model.SupportSMSDTO;
import com.fms.customerservice.model.SupportSMSList;
import com.fms.customerservice.model.TipDTO;
import com.fms.customerservice.model.VideoDTO;
import com.fms.customerservice.repository.SupportRepository;
import com.fms.module.exception.ErrorException;

@Service
public class SupportServiceImpl implements SupportService {
	@Autowired
	SupportRepository supportRepository;

	@Override
	public SupportInfoList getSupportInfo() {
		try {
			SupportInfoList supportInfos = new SupportInfoList();
			List<SupportInfo> listSupport = new ArrayList<>();
			List<SupportInfoDTO> data = supportRepository.getSupportInfo();
			for (SupportInfoDTO supportInfoDTO : data) {
				SupportInfo support = new SupportInfo(supportInfoDTO.getName(), supportInfoDTO.getPhone(),supportInfoDTO.getEmail(), supportInfoDTO.getPosition());;
				listSupport.add(support);
				supportInfos.setSupportInfos(listSupport);
			}
			return supportInfos;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public List<SupportSMSList> getSupportSMS() {
		try {
			List<SupportSMSList> lstSupport = new ArrayList<>();
			List<SupportSMSDTO> data = supportRepository.getSupportSMS();
			List<SupportSMS> listSupportSMS = new ArrayList<>();
			for (SupportSMSDTO obj : data) {
				SupportSMS sms = new SupportSMS();
				sms.setDescription(obj.getDescription());
				sms.setSetSms(obj.getSetSms());
				sms.setGetSms(obj.getGetSms());
				sms.setTitle(obj.getTitle());
				sms.setType(obj.getType());
				listSupportSMS.add(sms);
			}

			String type = "";
			SupportSMSList objList = new SupportSMSList();
			int index = 0;
			for (SupportSMS obj : listSupportSMS) {
				if (!obj.getType().equals(type)) {
					if (!("".equals(type))) {
						lstSupport.add(objList);
						objList = new SupportSMSList();
						objList.setSupportSMSList(new ArrayList<SupportSMS>());
					} else {
						objList = new SupportSMSList();
						objList.setSupportSMSList(new ArrayList<SupportSMS>());
					}
				}

				objList.setType(obj.getType());
				objList.getSupportSMSList().add(obj);
				type = obj.getType();
				index++;
				if (index == listSupportSMS.size()) {
					lstSupport.add(objList);
				}

			}
			return lstSupport;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public List<VideoDTO> getListVideo() {
		try {
			return supportRepository.getListVideo();
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public List<TipDTO> getListTip() {
		try {
			return supportRepository.getListTip();
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

}

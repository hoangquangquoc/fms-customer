package com.fms.customerservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.StopPointTypeDTO;
import com.fms.customerservice.repository.StopPointTypeRepository;
import com.fms.module.exception.ErrorException;

@Service
public class StopPointTypeServiceImpl implements StopPointTypeService {
	@Autowired
	StopPointTypeRepository stopPointTypeRepository;

	@Override
	public List<StopPointTypeDTO> getAllStopPointType() {
		try {
			return stopPointTypeRepository.getAllStopPointType();
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

}

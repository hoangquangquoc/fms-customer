package com.fms.customerservice.service.ship;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.fms.customerservice.client.LogServiceClient;
import com.fms.customerservice.controller.utils.AppUtils;
import com.fms.customerservice.model.Customer;
import com.fms.customerservice.model.CustomerAccount;
import com.fms.customerservice.model.CustomerContract;
import com.fms.customerservice.model.CustomerDTO;
import com.fms.customerservice.model.PackageFullDTO;
import com.fms.customerservice.model.ship.ShipCustomerDTO;
import com.fms.customerservice.model.ship.ShipCustomerDetailDTO;
import com.fms.customerservice.model.ship.ShipCustomerModel;
import com.fms.customerservice.repository.ConfigRepository;
import com.fms.customerservice.repository.CustomerAccountRepository;
import com.fms.customerservice.repository.CustomerContractRepository;
import com.fms.customerservice.repository.CustomerRepository;
import com.fms.customerservice.repository.RouteRepository;
import com.fms.customerservice.repository.UserRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.LogAction;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.LogUtils;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;

@Service
public class ShipCustomerServiceImpl implements ShipCustomerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipCustomerServiceImpl.class);
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerAccountRepository customerAccountRepository;
	@Autowired
	CustomerContractRepository customerContractRepository;

	@Autowired
	ConfigRepository configRepository;

	@Autowired
	RouteRepository routeRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	UserRepository userRepository;


	@Autowired
	LogServiceClient logServiceClient;

	@Autowired
	public ShipCustomerServiceImpl() {
	}

	/*
	 * lay danh sach khach hang
	 */
	@Override
	public Page<ShipCustomerDTO> findAllCustomers(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, boolean export) {
		Page<ShipCustomerDTO> data = null;
		try {
			String name = ValidateUtils.isNullOrEmpty(searchModel.getName()) ? null
					: Utils.toLikeString(searchModel.getName());
			String phone = ValidateUtils.isNullOrEmpty(searchModel.getPhone()) ? null
					: Utils.toLikeString(searchModel.getPhone());

			Date fromDate = null;
			Date toDate = null;

			if (!AppUtils.isNullOrEmpty(searchModel.getFromDate())) {
				fromDate = AppUtils.convertToDate(searchModel.getFromDate());
				if (fromDate == null) {
					throw new BusinessException(Utils.getValidateMessage("user.date-error"));
				}
			}
			if (!AppUtils.isNullOrEmpty(searchModel.getToDate())) {
				toDate = AppUtils.convertToDate(searchModel.getToDate());
				if (toDate == null) {
					throw new BusinessException(Utils.getValidateMessage("user.date-error"));
				}
				toDate = DateUtils.addDays(toDate, 1);
			}
			if (fromDate != null && toDate != null) {
				if (fromDate.after(toDate)) {
					throw new BusinessException(Utils.getValidateMessage("user.fromdate-error"));
				}
			}

			Pageable pageable = PageRequest.of(searchModel.getPageNumber() - 1, searchModel.getPageSize(),
					Sort.Direction.ASC, searchModel.getOrderBy());

			if (export) {
				pageable = Pageable.unpaged();
			}

			if (roleId == Constants.SYS_ADMIN_ROLE || roleId == Constants.PROVINCE_ADMIN_ROLE) {
				provinceId = roleId == Constants.SYS_ADMIN_ROLE ? -1 : provinceId;
				data = customerRepository.getShipCustomersForAdmin(name, phone, fromDate, toDate, provinceId, pageable);
			} else if (roleId == Constants.DISTRICT_ADMIN_ROLE) {
				districtId = districtId != null ? districtId : -1;
				data = customerRepository.getShipCustomersForDistrict(name, phone, fromDate, toDate, districtId,
						pageable);
			} else {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

		} catch (Exception e) {
			throw new ErrorException(e);
		}

		return data;
	}

	@Override
	public String create(ShipCustomerModel model, UserHeader userHeader, HttpServletRequest request) {
		try {
			//lay luon province cua admin
			model.setProvinceId(userHeader.getProvinceId());
			
			// validate customer
			validateCustomer(model);
			
			Customer customer = new Customer();
			customer = getCustomerData(customer, model);
			customer.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
			try {
				customer = customerRepository.save(customer);
			} catch (Exception e) {
				throw new BusinessException(Utils.getMessageByKey("validate.invalid"));
			}

			saveLogCreate(customer, userHeader.getUsername(), request);
			genCustomerAccount(customer.getCustomerId(), userHeader.getUsername());
			
			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateCustomer(ShipCustomerModel model) {
		// ten khach hang bat buoc
		if (ValidateUtils.isNullOrEmpty(model.getName())) {
			throw new BusinessException(Utils.getValidateMessage("required"));
		}
		// provinceId bat buoc nhap
		if (ValidateUtils.isNullOrEmpty(model.getProvinceId())) {
			throw new BusinessException(Utils.getValidateMessage("required"));
		}

		// validate length info nhập vào
		if (model.getAddress().length() > Constants.MAXLENGTH_255 || model.getEmail().length() > Constants.MAXLENGTH_100
				|| model.getName().length() > Constants.MAXLENGTH_255
				|| model.getTaxCode().length() > Constants.MAXLENGTH_255
				|| model.getPhone().length() > Constants.MAXLENGTH_100)
			throw new BusinessException(Utils.getMessageByKey("validate.invalid"));

	}

	private Customer getCustomerData(Customer customer, ShipCustomerModel model) {
		if (!ValidateUtils.isNullOrEmpty(model.getName())) {
			customer.setName(model.getName().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getAddress())) {
			customer.setAddress(model.getAddress().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getEmail())) {
			customer.setEmail(model.getEmail().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getPhone())) {
			customer.setPhone(model.getPhone().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getTaxCode())) {
			customer.setTaxCode(model.getTaxCode().trim());
		}
		customer.setProvinceId(model.getProvinceId());
		customer.setDistrictId(model.getDistrictId());
		customer.setCustomerTypeId(1);//1: SHIP
		return customer;
	}

	private void saveLogCreate(Customer customer, String userName, HttpServletRequest request) {
		try {
			String content = Utils.getMessageByKey("function.insert-success");
			LogAction logAction = LogUtils.getActionLog(request, userName, Constants.ActionType.CREATE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), content, customer.getName(), null, 0,
					customer.getCustomerId(), customer.getProvinceId(), customer.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ", e);
		}
	}

	//Ham sinh goi cuoc cho khach hang. Mac dinh sinh 10 goi cuoc
	private void genCustomerAccount(Integer customerId, String userName) {
		try {			
			CustomerDTO obj = customerRepository.getCustomer(customerId);
			if (obj == null) {
				return;
			}			
			Integer zero = 0;
			String contractCode = "CTC" + customerId;
			
			if(zero.equals(customerContractRepository.isExistContractCode(contractCode))){
				LocalDateTime startDate = LocalDateTime.now();
				LocalDateTime endDate = null;
				String custId = customerRepository.getCustIdByCustomerId(customerId);
				CustomerContract custContract = new CustomerContract(customerId, custId, contractCode, contractCode, 1, startDate, endDate);
				custContract.audit(Constants.LOCALE_CREATE, userName);
				customerContractRepository.save(custContract);
			}
			int packageIdDefault = 1;
			PackageFullDTO packageInfo = customerRepository.getPackageById(packageIdDefault);
			for (int i = 0; i < 10; i++) {
					String accountCode = genAccountCode(customerId,packageIdDefault);
					CustomerAccount customerAccount = new CustomerAccount(accountCode, customerId, "", "",
							packageInfo.getPackageCode(), packageInfo.getPackageName(), packageInfo.getNetworkedTypeCode(), packageInfo.getNetworkedTypeName(), 
							packageInfo.getPromotionCode(), packageInfo.getPromotionName(), packageInfo.getPrePaymentCode(), packageInfo.getPrePaymentName(), 
							packageInfo.getDuration(), "", contractCode, LocalDate.now().plusYears(5));//tau ca mac dinh 5 năm
					customerAccount.audit(Constants.LOCALE_CREATE, userName);
					customerAccountRepository.save(customerAccount);
				}
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	private String genAccountCode(Integer customerId, Integer packageId) {
		String template = customerId+"_"+packageId;
		String lastAccountCode = customerRepository.getLastAccountCode("%"+template+"%");
		Integer id = 0;
		if(lastAccountCode == null) id = 0;
		else {
			try {
				int len = lastAccountCode.length();
				id = Integer.parseInt(lastAccountCode.substring(len-4));
			} catch (Exception e) {
				id = 0;
				LOGGER.info("Error",e);
			}
		}
		String res = template + "_"+ String.format("%04d", id+1);
		return res;
	}
	
	
	//UPDATE
	@Override
	public String update(ShipCustomerModel model, UserHeader userHeader, HttpServletRequest request) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					model.getCustomerId());
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			ShipCustomerDTO obj = customerRepository.getShipCustomer(model.getCustomerId());
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}

			//lay luon province cua admin
			model.setProvinceId(userHeader.getProvinceId());
			
			validateCustomer(model);
			Customer customer = customerRepository.findById(model.getCustomerId()).get();
			Customer old = (Customer) customer.clone();
			
			// update các trường cần cập nhật
			customer.setAddress(model.getAddress());
			customer.setPhone(model.getPhone());
			customer.setName(model.getName());
			customer.setEmail(model.getEmail());
			customer.setTaxCode(model.getTaxCode());
			customer.audit(Constants.LOCALE_UPDATE, userHeader.getUsername());

			customer = customerRepository.save(customer);
			
			saveLogUpdate(old, customer, userHeader.getUsername(), request);
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	private void saveLogUpdate(Customer old, Customer updated, String userName, HttpServletRequest request) {
		try {
			LogAction logAction = LogUtils.getActionLog(request, userName, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), "", old.getName(),
					old.getCustomerId(), 0, old.getCustomerId(), old.getProvinceId(), old.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	//DETAIL
	@Override
	public ShipCustomerDetailDTO getCustomer(Integer customerId, UserHeader userHeader) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(), customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			ShipCustomerDTO customer = customerRepository.getShipCustomer(customerId);
			
			if (customer == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}
			ShipCustomerDetailDTO res = new ShipCustomerDetailDTO(customerId, customer.getName(), customer.getAddress(), customer.getEmail(), customer.getPhone(),
									customer.getTaxCode());
			return res;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String delete(Integer customerId, UserHeader userHeader, HttpServletRequest request) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(), customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			ShipCustomerDTO obj = customerRepository.getShipCustomer(customerId);
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}

			Customer customer = customerRepository.findById(customerId).get();
			customer.audit(Constants.LOCALE_DELETE, userHeader.getUsername());

			customerRepository.save(customer);
			saveLogDelete(customer, userHeader.getUsername(),request);
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	private void saveLogDelete(Customer customer, String username, HttpServletRequest request) {
		try {
			// String content = Utils.getMessageByKey("function.delete-des");
			String content = "Xóa thành công;"; 
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.DELETE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), content, customer.getName(),
					customer.getCustomerId(), 0, customer.getCustomerId(), customer.getProvinceId(), customer.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

}

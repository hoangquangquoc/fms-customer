package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "transport_fuel_calib")
public class TransportFuelCalib extends BaseModel {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "transport_fuel_calib_id")
	private Integer transportFuelCalibId;
	
	@Column(name = "transport_id")
	private int TransportId;
	
	@Column(name = "imei")
	private String Imei;
	
	@Column(name = "type")
	private int Type;
	
	@Column(name = "max_fuel_capacity")
	private int MaxFuelCapacity;
	
	@Column(name = "fuel_level_1")
	private String FuelLevel1;
	
	@Column(name = "fuel_level_2")
	private String FuelLevel2;
	
	@Column(name = "fuel_level_3")
	private String FuelLevel3;
	
	@Column(name = "fuel_level_4")
	private String FuelLevel4;
	
	@Column(name = "fuel_level_5")
	private String FuelLevel5;
	
	@Column(name = "fuel_level_6")
	private String FuelLevel6;
	
	@Column(name = "fuel_level_7")
	private String FuelLevel7;
	
	@Column(name = "fuel_level_8")
	private String FuelLevel8;
	
	@Column(name = "fuel_level_9")
	private String FuelLevel9;
	
	@Column(name = "fuel_level_10")
	private String FuelLevel10;
	
	@Column(name = "fuel_level_11")
	private String FuelLevel11;
	
	@Column(name = "fuel_level_12")
	private String FuelLevel12;
	
	@Column(name = "fuel_level_13")
	private String FuelLevel13;
	
	@Column(name = "fuel_level_14")
	private String FuelLevel14;
	
	@Column(name = "fuel_level_15")
	private String FuelLevel15;
	
	@Column(name = "fuel_level_16")
	private String FuelLevel16;
	
	@Column(name = "fuel_level_17")
	private String FuelLevel17;
	
	@Column(name = "fuel_level_18")
	private String FuelLevel18;
	
	@Column(name = "fuel_level_19")
	private String FuelLevel19;
	
	@Column(name = "fuel_level_20")
	private String FuelLevel20;
	
	@Column(name = "fuel_calib_1")
	private String FuelCalib1;
	
	@Column(name = "fuel_calib_2")
	private String FuelCalib2;
	
	@Column(name = "fuel_calib_3")
	private String FuelCalib3;
	
	@Column(name = "fuel_calib_4")
	private String FuelCalib4;
	
	@Column(name = "fuel_calib_5")
	private String FuelCalib5;
	
	@Column(name = "fuel_calib_6")
	private String FuelCalib6;
	
	@Column(name = "fuel_calib_7")
	private String FuelCalib7;
	
	@Column(name = "fuel_calib_8")
	private String FuelCalib8;
	
	@Column(name = "fuel_calib_9")
	private String FuelCalib9;
	
	@Column(name = "fuel_calib_10")
	private String FuelCalib10;
	
	@Column(name = "fuel_calib_11")
	private String FuelCalib11;
	
	@Column(name = "fuel_calib_12")
	private String FuelCalib12;
	
	@Column(name = "fuel_calib_13")
	private String FuelCalib13;
	
	@Column(name = "fuel_calib_14")
	private String FuelCalib14;
	
	@Column(name = "fuel_calib_15")
	private String FuelCalib15;
	
	@Column(name = "fuel_calib_16")
	private String FuelCalib16;
	
	@Column(name = "fuel_calib_17")
	private String FuelCalib17;
	
	@Column(name = "fuel_calib_18")
	private String FuelCalib18;
	
	@Column(name = "fuel_calib_19")
	private String FuelCalib19;
	
	@Column(name = "fuel_calib_20")
	private String FuelCalib20;	


}

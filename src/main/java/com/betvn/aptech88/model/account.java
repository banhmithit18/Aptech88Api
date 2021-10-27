package com.betvn.aptech88.model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="account")
public class account {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="age")
	private int age;
	
	@Column(name="address")
	private String address;
	
	@Column(name="email")
	private String email;
	
	@Column(name = "phonenumber")
	private String phonenumber;
	
	@Column(name = "status")
	private boolean status;
	
	@Column(name = "verified")
	private boolean verified;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "protect_time_id")
	private int proctectTimeId;
	
	@Column(name = "protect_time_start")
	private Date protectTimeStart;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "province")
	private String province;
	
	@Column(name = "maximum_deposit")
	private double maximumDeposit;
	
	@Column(name = "today_deposit")
	private double todayDeposit;
	
	@Column(name = "verified_code")
	private String verifiedCode;
	
	@Column(name = "verified_create_date")
	private Time verifiedCreateDate;
	
	@Column(name = "banned_reason")
	private String bannedReason;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "protect_time_id", referencedColumnName = "id", insertable = false, updatable = false)
	private protect_time protect_time;

	@OneToMany(mappedBy = "account")
	private List<wallet> wallet;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getProctectTimeId() {
		return proctectTimeId;
	}

	public void setProctectTimeId(int proctectTimeId) {
		this.proctectTimeId = proctectTimeId;
	}
	
	public Date getProtectTimeStart() {
		return protectTimeStart;
	}

	public void setProtectTimeStart(Date protectTimeStart) {
		this.protectTimeStart = protectTimeStart;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public double getMaximumDeposit() {
		return maximumDeposit;
	}

	public void setMaximumDeposit(double maximumDeposit) {
		this.maximumDeposit = maximumDeposit;
	}

	public double getTodayDeposit() {
		return todayDeposit;
	}

	public void setTodayDeposit(double todayDeposit) {
		this.todayDeposit = todayDeposit;
	}

	public protect_time getProtect_time() {
		return protect_time;
	}

	public void setProtect_time(protect_time protect_time) {
		this.protect_time = protect_time;
	}

	public List<wallet> getWallet() {
		return wallet;
	}

	public void setWallet(List<wallet> wallet) {
		this.wallet = wallet;
	}

	public String getVerifiedCode() {
		return verifiedCode;
	}

	public void setVerifiedCode(String verifiedCode) {
		this.verifiedCode = verifiedCode;
	}

	public Time getVerifiedCreateDate() {
		return verifiedCreateDate;
	}

	public void setVerifiedCreateDate(Time verifiedCreateDate) {
		this.verifiedCreateDate = verifiedCreateDate;
	}

	public String getBannedReason() {
		return bannedReason;
	}

	public void setBannedReason(String bannedReason) {
		this.bannedReason = bannedReason;
	}
	
	
		
}

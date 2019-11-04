package com.example.template;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User{

	@Id
	private String username;
	private String nickname;

	private String address;
	private Long mileage;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getMileage() {
		return mileage;
	}

	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}
}

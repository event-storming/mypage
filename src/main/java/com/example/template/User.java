package com.example.template;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User{

	@Id
	private String username;
	private String nickname;

	@Column(length=400)
	private String password;

	private String address;
	private Long money;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}
}

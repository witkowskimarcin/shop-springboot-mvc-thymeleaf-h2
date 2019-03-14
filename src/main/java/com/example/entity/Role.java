package com.example.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
//    @SequenceGenerator(name="role_generator", sequenceName="role_sequence")
	private int id;

	@Column(name = "role")
	private String role;

	@ManyToMany(mappedBy = "roles")
	//@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}

package com.labo.budgets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labo.budgets.models.AppRole;
import com.labo.budgets.repositories.RoleRepo;


@Service
public class AppRoleService {
	
	private RoleRepo roleRepo;
	
	@Autowired
	public AppRoleService(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	public AppRole addNewRole(AppRole role) {
        return this.roleRepo.save(role);
    }

}

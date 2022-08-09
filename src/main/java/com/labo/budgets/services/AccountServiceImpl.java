package com.labo.budgets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labo.budgets.models.AppRole;
import com.labo.budgets.models.Utilisateur;
import com.labo.budgets.repositories.RoleRepo;
import com.labo.budgets.repositories.UtilisateurRepo;

@Service
@Transactional
public class AccountServiceImpl {

    private RoleRepo roleRepo;
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    public AccountServiceImpl(RoleRepo roleRepo, UtilisateurRepo utilisateurRepo, PasswordEncoder passwordEncoder){
        this.roleRepo = roleRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    public void affectRoleToUser(String username, String role) {
        Utilisateur user = utilisateurRepo.findByUsername(username);
        AppRole role1 = roleRepo.findByLibelle(role);
        user.getRoles().add(role1);
        role1.getUsers().add(user);
    }
    
    public void delete(Utilisateur user) {
    	this.utilisateurRepo.delete(user);
    }
    
}

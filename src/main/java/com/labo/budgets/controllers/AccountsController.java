package com.labo.budgets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.labo.budgets.dto.AffectRoleToUserDto;
import com.labo.budgets.models.AppRole;
import com.labo.budgets.models.Utilisateur;
import com.labo.budgets.services.AccountServiceImpl;
import com.labo.budgets.services.UtilisateurService;

import java.security.Principal;

@RestController
@RequestMapping("api/accounts")
public class AccountsController {

    private AccountServiceImpl accountService;
    private UtilisateurService utilisateurService;

    @Autowired
    public AccountsController(AccountServiceImpl accountService, UtilisateurService utilisateurService){
        this.accountService = accountService;
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("roletouser")
    @PostAuthorize("hasAnyAuthority('ADMIN')")
    public void affectRoleToUser(@RequestBody AffectRoleToUserDto affectRoleToUserDto){
        this.accountService.affectRoleToUser(affectRoleToUserDto.getUsername(), affectRoleToUserDto.getRole());
    }

    @GetMapping(path = "/profile")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Utilisateur profile(Principal principal){
        return utilisateurService.loadUserByUsername(principal.getName());
    }
    
    @PostMapping(path = "/register")
    public Utilisateur create(@RequestBody Utilisateur user) {
    	user = this.utilisateurService.addNewUser(user);
    	this.accountService.affectRoleToUser(user.getUsername(), "USER");
    	return user;
    }
    
    @DeleteMapping(path = "/delete/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utilisateur delete(@PathVariable(name = "username") String username) {
    	Utilisateur user = this.utilisateurService.loadUserByUsername(username);
    	if(user!=null)
    		this.accountService.delete(user);
    	return user;
    }
    
   @PatchMapping(path = "/modify")
   @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Utilisateur modify(@RequestBody Utilisateur user, Principal principal) {
	   if(utilisateurService.loadUserByUsername(principal.getName()).getUsername().equals(user.getUsername()) ||
			   utilisateurService.loadUserByUsername(principal.getName()).getRoles().get(0).equals(new AppRole("ADMIN")))
		   return this.utilisateurService.edit(user);
	   return null;
   }

}

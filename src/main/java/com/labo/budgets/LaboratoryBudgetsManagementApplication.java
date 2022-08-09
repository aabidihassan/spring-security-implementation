package com.labo.budgets;

import com.labo.budgets.models.AppRole;
import com.labo.budgets.models.Utilisateur;
import com.labo.budgets.services.AccountServiceImpl;
import com.labo.budgets.services.AppRoleService;
import com.labo.budgets.services.UtilisateurService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class LaboratoryBudgetsManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaboratoryBudgetsManagementApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

	@Bean
  CommandLineRunner start(AccountServiceImpl accountService, AppRoleService appRoleService, UtilisateurService utilisateurService){
      return args -> {
          appRoleService.addNewRole(new AppRole("ADMIN"));
          appRoleService.addNewRole(new AppRole("USER"));
          utilisateurService.addNewUser(new Utilisateur("hassan", "hassan"));
          utilisateurService.addNewUser(new Utilisateur("aabidi", "hassan"));
          accountService.affectRoleToUser("hassan", "ADMIN");
          accountService.affectRoleToUser("aabidi", "USER");

      };
  }

}

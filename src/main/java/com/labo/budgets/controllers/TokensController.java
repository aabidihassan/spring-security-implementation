package com.labo.budgets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labo.budgets.security.JwtUtil;
import com.labo.budgets.services.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/token")
public class TokensController {

    private UtilisateurService utilisateurService;

    @Autowired
    public TokensController(UtilisateurService utilisateurService){
        this.utilisateurService = utilisateurService;
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        JwtUtil.refreshToken(request, response, utilisateurService);
    }

}

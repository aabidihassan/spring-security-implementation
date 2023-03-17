package com.labo.budgets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppRole {

    @Id
    @Column(length = 5)
    private String libelle;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<Utilisateur> users = new ArrayList<>();

    public AppRole(String libelle){
        this.libelle = libelle;
    }

}

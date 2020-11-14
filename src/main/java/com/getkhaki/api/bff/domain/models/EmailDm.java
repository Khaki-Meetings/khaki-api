package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmailDm extends BaseDm {
    String user;
    DomainTypeDm domain;

    public String getEmail(){
        return this.user+"@"+this.domain.getName();
    }

}

package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmailDm {
    String user;
    DomainDm domain;

    public String getEmail(){
        return this.user+"@"+this.domain.getName();
    }

}

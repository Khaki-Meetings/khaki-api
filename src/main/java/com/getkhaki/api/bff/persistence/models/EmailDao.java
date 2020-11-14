package com.getkhaki.api.bff.persistence.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmailDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column
    String user;

    @ManyToOne
    DomainTypeDao domain;
}

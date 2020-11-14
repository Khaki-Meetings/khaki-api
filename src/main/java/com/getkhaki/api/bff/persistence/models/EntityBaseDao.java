package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
@Accessors
public class EntityBaseDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
}

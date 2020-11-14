package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PersonDao extends EntityBaseDao {
    String firstName;
    String lastName;

    @OneToMany(mappedBy = "person")
    List<EmailDao> emails;
}

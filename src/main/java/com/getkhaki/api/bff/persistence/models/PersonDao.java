package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@ToString(callSuper = true)
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PersonDao extends EntityBaseDao {
    String firstName;
    String lastName;

    @ManyToMany(mappedBy = "people")
    List<EmailDao> emails;

    @OneToOne(mappedBy = "person")
    EmployeeDao employee;
}

package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Setter
@Getter
@Accessors(chain = true)
public class PersonDao extends EntityBaseDao {
    String firstName;
    String lastName;

    @ManyToMany(mappedBy = "people")
    List<EmailDao> emails;

    @OneToOne(mappedBy = "person")
    EmployeeDao employee;

    @Transient
    public EmailDao getPrimaryEmail() {
        return getEmails().size() > 0 ? getEmails().get(0) : null;
    }

    @Transient
    public boolean getNotify() {
        return getEmails()
                .stream()
                .anyMatch(
                        emailDao -> emailDao
                                .getFlags()
                                .stream()
                                .anyMatch(flagDao -> flagDao.getName().equals(FlagDao.CONTACTABLE))
                );
    }
}

package com.getkhaki.api.bff.domain.models.csv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmployeeCsvDm {
    String firstName;

    String lastName;

    String email;

    String department;
}

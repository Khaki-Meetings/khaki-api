package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DepartmentDm {
    String name;

    Set<PersonDm> members;
}

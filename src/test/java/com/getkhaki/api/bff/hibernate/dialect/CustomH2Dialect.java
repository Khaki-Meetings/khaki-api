package com.getkhaki.api.bff.hibernate.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomH2Dialect extends H2Dialect {
    public CustomH2Dialect() {
        super();

        registerFunction(
                "timestampdiff",
                new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "datediff(?1, ?2, ?3)")
        );
    }
}

package com.getkhaki.api.bff.hibernate.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomH2Dialect extends H2Dialect {
    public CustomH2Dialect() {
        super();

        registerFunction("timestampdiff",
                new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "datediff(?1, ?2, ?3)")
        );

        registerFunction("dayofweek",
                new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "DAY_OF_WEEK(?1) - 1")
        );

        registerFunction("SUBSTR",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "SUBSTRING(?1, ?2, ?3)")
        );

        registerFunction("HEX",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "HEXTORAW(?1)")
        );

    }
}

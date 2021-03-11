package com.getkhaki.api.bff.hibernate.dialect;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomMariaDbDialect extends MariaDB103Dialect {
    public CustomMariaDbDialect() {
        super();

        registerFunction("timestampdiff",
                new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "timestampdiff(?1, ?2, ?3)")
        );

        registerFunction("dayofweek",
                new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "DAYOFWEEK(?1)")
        );

    }
}

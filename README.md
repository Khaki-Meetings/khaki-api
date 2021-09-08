# khaki-api

# IDE

The recommended IDE is IntelliJ, but any popular IDE should work with some configuration.

# Local
To get api running locally:

There are a couple environment variables to set for the calendar synchonization

```
CALENDAR_CRON_IMPORT_HISTORY_MINUTES
CALENDAR_IMPORT_HISTORY_MINUTES
GOOGLE_SERVICE_ACCOUNT_KEY
```

Unless you are testing imports, you probably want to set the ```*_MINUTES``` variables to very small numbers, so as not to have massive imports running constantly. Copy the ```GOOGLE_SERVICE_ACCOUNT_KEY``` from the Heroku staging instance.

You need to set the following environment variables for database access. The values here are example values, substitute your own relevant ones.

```
JDBC_DATABASE_URL=jdbc:mariadb://localhost:3306/khaki
JDBC_DATABASE_USERNAME=jacob
JDBC_DATABASE_PASSWORD=
JDBC_DATABASE_DIALECT=com.getkhaki.api.bff.hibernate.dialect.CustomMariaDbDialect
```

Once those are set, you can either start the service, which will create the tables if required, or run the following command:

```
./gradlew update
```

If you're on the development branch the tests might still be failing, in which case you can add the ```-x test``` switch.

If you'd like to add test data, run the following command:

```
gradle update -x test -PchangeLogFile=liquibase-mariadb-test-data
```

To run this, do the following:

- Edit gss-jaas.conf to have the correct principal or no principal if you
want to be promted for princ on command line.
- Edit the krb5.conf with the correct values or copy one of your own over it.
- Edit HiveJDBC.java to have correct Connect URI.
- Compile the project with Maven3.
- Run java -cp $HADOOP_CLASSPATH:target/SentryClient-1.0-SNAPSHOT-jar-with-dependencies.jar  com.impetus.idw.sentry.SentryJDBCClient

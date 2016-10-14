package com.impetus.idw.sentry;

import org.apache.sentry.provider.db.service.thrift.SentryPolicyService;
import org.apache.sentry.provider.db.service.thrift.TListSentryPrivilegesRequest;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSaslClientTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import javax.security.sasl.Sasl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by amit.k.mula on 22/9/16.
 */
public class SentryServiceClient {

    public static void main(String args[]) {

        Properties props = System.getProperties();
        props.setProperty("java.security.krb5.realm", "IDWREALM.COM");
        props.setProperty("java.security.krb5.kdc", "quickstart.cloudera");
        props.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        props.setProperty("java.security.auth.login.config", "/etc/sentry/conf/jaas.conf");
        props.setProperty("sun.security.krb5.debug", "true");

        try {
            TTransport transport = new TSocket("quickstart.cloudera", 8038), saslTransport= null;
            Map<String,String> saslProps = new HashMap();

            saslProps.put(Sasl.QOP, "true");
            saslProps.put(Sasl.QOP, "auth-conf"); // Use authorization and confidentiality

            saslTransport = new TSaslClientTransport(
                "GSSAPI",       //  tell SASL to use GSSAPI, which supports Kerberos
                null,           //  authorizationid - null
                "idw-admin",   //  base kerberos principal name - myprincipal/my.client.com@MY.REALM
                "quickstart.cloudera",    //  kerberos principal server - myprincipal/my.server.com@MY.REALM
                saslProps,      //  Properties set, above
                null,           //  callback handler - null
                transport);     //  underlying transport

            TProtocol protocol = new TBinaryProtocol(saslTransport);    // set up our new Thrift protocol

            SentryPolicyService.Client client = new SentryPolicyService.Client(protocol);

            saslTransport.open();

            String response = client.list_sentry_privileges_by_role(new TListSentryPrivilegesRequest(2, "idw-admin", "idw_admin_role")).privileges.toString();

            System.out.println("response = " + response);

            transport.close();

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException x) {
            x.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

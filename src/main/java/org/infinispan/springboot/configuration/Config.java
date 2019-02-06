package org.infinispan.springboot.configuration;

import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.NearCacheConfiguration;
import org.infinispan.client.hotrod.configuration.NearCacheMode;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.callback.*;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import java.io.IOException;
import java.util.Properties;

/**
 * A spring boot configuration class. JDG is configured here.
 */
@Configuration
public class Config {

    /**
     * This method returns the infinispan configuration object. hotrod client properties are set here.
     *
     * @return
     * @throws IOException
     */
    @Bean
    public org.infinispan.client.hotrod.configuration.Configuration customConfiguration() throws IOException {

        Properties hotrodProps = new Properties();

        //TODO: This properties file must be put in a ConfigMap and read dynamically.
        hotrodProps.load(Thread.currentThread().getContextClassLoader().getResource("hotrod-client-custom.properties").openStream());

        NearCacheMode nearCacheMode = NearCacheMode.DISABLED; // NearCache disabled by default
        int nearCacheMaxEntries = 100; // By default

        // Get NearCache configuration
        if ("true".equals(hotrodProps.getProperty("infinispan.client.hotrod.nearcache"))) {
            nearCacheMode = NearCacheMode.INVALIDATED;
        }

        if (hotrodProps.getProperty("infinispan.client.hotrod.nearcache.maxentries") != null) {
            nearCacheMaxEntries = Integer.valueOf(hotrodProps.getProperty("infinispan.client.hotrod.nearcache.maxentries"));
        }

        NearCacheConfiguration ncc = new NearCacheConfiguration(nearCacheMode, nearCacheMaxEntries);

        // HotRod ConfigurationBuilder.
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.withProperties(hotrodProps);

        /*cb.security()
                .authentication()
                .enable()
                .serverName("jdg-server")
                .saslMechanism("DIGEST-MD5")
                .callbackHandler(new TestCallbackHandler("jdguser", "ApplicationRealm", "P@ssword1".toCharArray()));*/


        cb.nearCache().read(ncc);

        // Make sure to register the ProtoStreamMarshaller.
        cb.marshaller(new ProtoStreamMarshaller());

        return cb.build();
    }

    public static class TestCallbackHandler implements CallbackHandler {
        final private String username;
        final private char[] password;
        final private String realm;

        public TestCallbackHandler(String username, String realm, char[] password) {
            this.username = username;
            this.password = password;
            this.realm = realm;
        }

        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    NameCallback nameCallback = (NameCallback) callback;
                    nameCallback.setName(username);
                } else if (callback instanceof PasswordCallback) {
                    PasswordCallback passwordCallback = (PasswordCallback) callback;
                    passwordCallback.setPassword(password);
                } else if (callback instanceof AuthorizeCallback) {
                    AuthorizeCallback authorizeCallback = (AuthorizeCallback) callback;
                    authorizeCallback.setAuthorized(authorizeCallback.getAuthenticationID().equals(
                            authorizeCallback.getAuthorizationID()));
                } else if (callback instanceof RealmCallback) {
                    RealmCallback realmCallback = (RealmCallback) callback;
                    realmCallback.setText(realm);
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }

}

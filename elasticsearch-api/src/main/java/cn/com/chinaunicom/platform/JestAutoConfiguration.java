package cn.com.chinaunicom.platform;

import cn.com.chinaunicom.util.HttpUtil;
import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.config.HttpClientConfig.Builder;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

@Configuration
@ConditionalOnClass({JestClient.class})
@EnableConfigurationProperties({JestProperties.class})
@AutoConfigureAfter({GsonAutoConfiguration.class})
public class JestAutoConfiguration {
    private final JestProperties properties;
    private final ObjectProvider<Gson> gsonProvider;
    private final List<HttpClientConfigBuilderCustomizer> builderCustomizers;

    public JestAutoConfiguration(JestProperties properties, ObjectProvider<Gson> gson, ObjectProvider<List<HttpClientConfigBuilderCustomizer>> builderCustomizers) {
        this.properties = properties;
        this.gsonProvider = gson;
        this.builderCustomizers = (List) builderCustomizers.getIfAvailable();
    }

    @Bean(
            destroyMethod = "shutdownClient"
    )
    @ConditionalOnMissingBean
    public JestClient jestClient() {
        if (this.properties.getGuardFlag()){
            return getGuardClient();
        }else{
            return getNotGuardClient();
        }
    }

    @Bean
    public int initHttpClient(){
        HttpUtil.username = this.properties.getUsername();
        HttpUtil.password = this.properties.getPassword();
        HttpUtil.uris = this.properties.getUris();
        return 1;
    }

    private JestClient getNotGuardClient(){
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(this.createHttpClientConfig());
        return factory.getObject();
    }

    private JestClient getGuardClient(){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();

            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            SchemeIOSessionStrategy httpsIOSessionStrategy = new SSLIOSessionStrategy(sslContext, hostnameVerifier);

            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(this.properties.getUris())
                    .defaultSchemeForDiscoveredNodes("https") // required, otherwise uses http
                    .sslSocketFactory(sslSocketFactory) // this only affects sync calls
                    .httpsIOSessionStrategy(httpsIOSessionStrategy) // this only affects async calls
                    .defaultCredentials(this.properties.getUsername(), this.properties.getPassword())
                    .build()
            );
            return factory.getObject();
        }catch (Exception e){
            return null;
        }
    }

    protected HttpClientConfig createHttpClientConfig() {
        Builder builder = new Builder(this.properties.getUris());
        if (StringUtils.hasText(this.properties.getUsername())) {
            builder.defaultCredentials(this.properties.getUsername(), this.properties.getPassword());
        }

        String proxyHost = this.properties.getProxy().getHost();
        if (StringUtils.hasText(proxyHost)) {
            Integer gson = this.properties.getProxy().getPort();
            Assert.notNull(gson, "Proxy port must not be null");
            builder.proxy(new HttpHost(proxyHost, gson.intValue()));
        }

        Gson gson1 = (Gson) this.gsonProvider.getIfUnique();
        if (gson1 != null) {
            builder.gson(gson1);
        }

        builder.multiThreaded(this.properties.isMultiThreaded());
        ((Builder) builder.connTimeout(this.properties.getConnectionTimeout())).readTimeout(this.properties.getReadTimeout());
        this.customize(builder);
        return builder.build();
    }

    private void customize(Builder builder) {
        if (this.builderCustomizers != null) {
            Iterator var2 = this.builderCustomizers.iterator();

            while (var2.hasNext()) {
                HttpClientConfigBuilderCustomizer customizer = (HttpClientConfigBuilderCustomizer) var2.next();
                customizer.customize(builder);
            }
        }

    }
}
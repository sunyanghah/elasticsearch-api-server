package cn.com.chinaunicom.platform;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.elasticsearch.jest"
)
public class JestProperties {
    private List<String> uris = new ArrayList(Collections.singletonList("http://localhost:9200"));
    private String username;
    private String password;
    private boolean multiThreaded = true;
    private int connectionTimeout = 3000;
    private int readTimeout = 3000;
    private boolean guardFlag = false;
    private final JestProperties.Proxy proxy = new JestProperties.Proxy();

    public JestProperties() {
    }

    public List<String> getUris() {
        return this.uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMultiThreaded() {
        return this.multiThreaded;
    }

    public void setMultiThreaded(boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public JestProperties.Proxy getProxy() {
        return this.proxy;
    }

    public boolean getGuardFlag(){
        return this.guardFlag;
    }

    public void setGuardFlag(boolean guardFlag){
        this.guardFlag = guardFlag;
    }

    public static class Proxy {
        private String host;
        private Integer port;

        public Proxy() {
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return this.port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }
}
package in.capp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Db {
    private static volatile boolean schemaInitialized = false;

	 public static Connection getConnection() {
        try {
            // Try JNDI lookup first (for Tomcat WAR deployment)
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/contact_ap");
                if (ds != null) {
                    return ds.getConnection();
                }
            } catch (Exception e) {
                // JNDI not available, fall back to direct JDBC
            }
            
            // Direct JDBC via explicit MySQL env vars.
            String host = getEnv("MYSQLHOST");
            String port = getEnv("MYSQLPORT");
            String database = getEnv("MYSQLDATABASE");
            String user = getEnv("MYSQLUSER");
            String password = getEnv("MYSQLPASSWORD");
            String jdbcParams = getEnv("MYSQL_JDBC_PARAMS");

            if (host == null || host.trim().isEmpty()) {
                throw new IllegalStateException("MYSQLHOST is not configured");
            }
            if (port == null || port.trim().isEmpty()) {
                throw new IllegalStateException("MYSQLPORT is not configured");
            }
            if (database == null || database.trim().isEmpty()) {
                throw new IllegalStateException("MYSQLDATABASE is not configured");
            }
            if (user == null || user.trim().isEmpty()) {
                throw new IllegalStateException("MYSQLUSER is not configured");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalStateException("MYSQLPASSWORD is not configured");
            }
            if (jdbcParams == null || jdbcParams.trim().isEmpty()) {
                // Default works for cloud MySQL/TiDB endpoints that require TLS.
                jdbcParams = "useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"
                        + "&useSSL=true&sslMode=REQUIRED&enabledTLSProtocols=TLSv1.2";
            }

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + jdbcParams;

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to create database connection", e);
        }
    }

    public static void initializeSchemaIfRequired() {
        if (schemaInitialized) {
            return;
        }
        synchronized (Db.class) {
            if (schemaInitialized) {
                return;
            }
            try (Connection con = getConnection()) {
                ScriptUtils.executeSqlScript(con, new ClassPathResource("schema.sql"));
                schemaInitialized = true;
                System.out.println("Database schema initialized successfully.");
            } catch (Exception e) {
                throw new IllegalStateException("Schema initialization failed", e);
            }
        }
    }

    private static String getEnv(String... keys) {
        for (String key : keys) {
            String value = System.getenv(key);
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }
        }
        return null;
    }
 	
}

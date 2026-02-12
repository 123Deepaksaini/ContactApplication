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
            
            // Fall back to direct JDBC using Render/Railway env vars first,
            // then legacy variable names for backward compatibility.
            String host = getEnv("MYSQLHOST", "HOST");
            String port = getEnv("MYSQLPORT", "DB_PORT");
            String database = getEnv("MYSQLDATABASE", "DATABASE");
            String user = getEnv("MYSQLUSER", "USERNAME");
            String password = getEnv("MYSQLPASSWORD", "PASSWORD");
            String jdbcParams = getEnv("MYSQL_JDBC_PARAMS");

            if (host == null || host.trim().isEmpty()) {
                throw new IllegalStateException("Database host is not configured");
            }
            if (port == null || port.trim().isEmpty()) {
                port = "3306";
            }
            if (database == null || database.trim().isEmpty()) {
                throw new IllegalStateException("Database name is not configured");
            }
            if (user == null || user.trim().isEmpty()) {
                throw new IllegalStateException("Database username is not configured");
            }
            if (password == null) {
                password = "";
            }
            if (jdbcParams == null || jdbcParams.trim().isEmpty()) {
                // Default works for cloud MySQL/TiDB endpoints that require TLS.
                jdbcParams = "useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"
                        + "&useSSL=true&requireSSL=true&enabledTLSProtocols=TLSv1.2";
            }

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + jdbcParams;

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                if (con == null) {
                    throw new IllegalStateException("Unable to open DB connection for schema initialization");
                }
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

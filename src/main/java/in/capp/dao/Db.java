package in.capp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Db {

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
            
            // Fall back to direct JDBC using environment variables
            String host = System.getenv("HOST");
            String port = System.getenv("PORT");
            String database = System.getenv("DATABASE");
            String user = System.getenv("USERNAME");
            String password = System.getenv("PASSWORD");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 	
}

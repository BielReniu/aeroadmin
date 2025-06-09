package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

public class SingleConnectionDataSource implements DataSource {
    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/AVIATION_SYSTEM"
                    + "?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC";
    // Ara usem root, que ja té tots els permisos des de qualsevol host
    private static final String USER     = "root";
    private static final String PASSWORD = "1qaz2wsx3edc";

    private Connection conn;

    @Override
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }
    @Override public Connection getConnection(String u, String p) throws SQLException {
        return getConnection();
    }
    @Override public PrintWriter getLogWriter() { return null; }
    @Override public void setLogWriter(PrintWriter out) { }
    @Override public void setLoginTimeout(int seconds) { DriverManager.setLoginTimeout(seconds); }
    @Override public int getLoginTimeout() { return DriverManager.getLoginTimeout(); }
    @Override public Logger getParentLogger() { return Logger.getLogger("global"); }
    @Override public <T> T unwrap(Class<T> iface) throws SQLException {
        if (DataSource.class.isAssignableFrom(iface)) return iface.cast(this);
        throw new SQLException("No s'implementa " + iface.getName());
    }
    @Override public boolean isWrapperFor(Class<?> iface) {
        return DataSource.class.isAssignableFrom(iface);
    }
}

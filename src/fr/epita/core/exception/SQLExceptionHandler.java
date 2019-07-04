package fr.epita.core.exception;

import java.sql.SQLException;
import java.util.logging.Level; 
import java.util.logging.Logger; 
/**
 * Print a readable SQL exception
 */
public class SQLExceptionHandler{
    private static final Logger LOGGER =  
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
    private SQLExceptionHandler() {
    }

    /**
     * !! Methods inspired by a tutorial on the Oracle site !!
     * logging the sql error
     * @param ex, SQL Exception
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if ((e instanceof SQLException) || (!ignoreSQLException(((SQLException)e).getSQLState()))) {
                LOGGER.log(Level.SEVERE,
                    e.getMessage() + "SQLState: " +
                    ((SQLException)e).getSQLState()
                    + "Error Code: " +
                    ((SQLException)e).getErrorCode(), 
                    e);

                Throwable t = ex.getCause();
                while(t != null) {
                    LOGGER.log(Level.SEVERE, "Cause: " + t);
                    
                }
            }
        }
    }

    /**
     * !! Methods copied from Oracle site !!
     * Check if the exception could be ignored by 3 cases (shown in codes below)
     * @param sqlState, String, the state of the SQL Exception
     * @return True if the SQL Exception can be ignored.
     */
    public static boolean ignoreSQLException(String sqlState) {
        
        if (sqlState == null) {
            LOGGER.log(Level.SEVERE, "The SQL state is not defined!");
            return false;
        }
        
        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32"))
            return true;
    
        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55"))
            return true;
    
        return false;
    }
}
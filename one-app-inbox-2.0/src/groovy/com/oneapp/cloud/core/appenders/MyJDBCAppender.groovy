package com.oneapp.cloud.core.appenders

import org.apache.log4j.*;
import org.apache.log4j.spi.*;
import org.apache.log4j.PatternLayout;

import com.oneapp.cloud.core.ApplicationConf;

import grails.plugin.multitenant.core.util.TenantUtils;

import java.util.ArrayList;
import java.util.Iterator;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class MyJDBCAppender extends org.apache.log4j.AppenderSkeleton implements org.apache.log4j.Appender {

	protected String databaseURL = "jdbc:odbc:myDB";
	
	protected String databaseUser = "me";
	
	protected String databasePassword = "mypassword";
	
	protected Connection connection = null;
	
	protected String sqlStatement = "";
	
	protected int bufferSize = 1;
	
	protected ArrayList buffer;
	
	protected ArrayList removes;
	
	public MyJDBCAppender() {
		super();
		buffer = new ArrayList(bufferSize);
		removes = new ArrayList(bufferSize);
		this.setSql("insert into log (description,error_level,error_type,log_time,tenant_id) values ('%m','%p','%c','%d{yyyy-MM-dd HH:mm:ss}','a-tenant-a')")
	}

	public void append(LoggingEvent event) {
	
		try { 
		if(!ApplicationConf.get(1)?.logInfo && event?.getLevel() == Level.INFO){
			return
		}
		event.message = event.message.replaceAll("'","\"")
		buffer.add(event);
		if (buffer.size() >= bufferSize)
			flushBuffer();
		}catch( Exception ex ){
			println "MyJDBCAppender-append: "+ex
		}
	}
	
	protected String getLogStatement(LoggingEvent event) {
		def currentTenant = "0";
		try{
			currentTenant = ""+ TenantUtils.getCurrentTenant()
		}catch(Exception e){}
		return getLayout().format(event).replace('a-tenant-a',currentTenant);
	}
	
	protected void execute(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;

		try {
			con = getConnection();

			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			
		}
		if (stmt != null)
			stmt.close();
		closeConnection(con);

		//System.out.println("Execute: " + sql);
	}
	
	protected void closeConnection(Connection con) {
		
	}
	
	public void close(){
		//flushBuffer();
		try {
			if (connection != null && !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			errorHandler.error("Error closing connection", e, ErrorCode.GENERIC_FAILURE);
		}
		this.closed = true;
	}
	
	public void flushBuffer() {
		removes.ensureCapacity(buffer.size());
		for (Iterator i = buffer.iterator(); i.hasNext();) {
			try {
				LoggingEvent logEvent = (LoggingEvent)i.next();
				String sql = getLogStatement(logEvent);
				execute(sql);
				removes.add(logEvent);
			} catch (SQLException e) {
				errorHandler.error("Failed to excute sql", e, ErrorCode.FLUSH_FAILURE);
			}
		}
		buffer.removeAll(removes);
    
		// clear the buffer of reported events
		removes.clear();
	}
	
	public void finalize() {
    close();
  }
  
  public boolean requiresLayout() {
    return true;
  }
  
  public void setSql(String s) {
    sqlStatement = s;
    if (getLayout() == null) {
        this.setLayout(new PatternLayout(s));
    }
    else {
        ((PatternLayout)getLayout()).setConversionPattern(s);
    }
  }
  
  public String getSql() {
    return sqlStatement;
  }
  
  public void setUser(String user) {
    databaseUser = user;
  }


  public void setURL(String url) {
    databaseURL = url;
  }


  public void setPassword(String password) {
    databasePassword = password;
  }


  public void setBufferSize(int newBufferSize) {
    bufferSize = newBufferSize;
    buffer.ensureCapacity(bufferSize);
    removes.ensureCapacity(bufferSize);
  }


  public String getUser() {
    return databaseUser;
  }


  public String getURL() {
    return databaseURL;
  }


  public String getPassword() {
    return databasePassword;
  }


  public int getBufferSize() {
    return bufferSize;
  }


  /**
   * Ensures that the given driver class has been loaded for sql connection
   * creation.
   */
  public void setDriver(String driverClass) {
    try {
      Class.forName(driverClass);
    } catch (Exception e) {
      errorHandler.error("Failed to load driver", e,
			 ErrorCode.GENERIC_FAILURE);
    }
  }
	
	public Connection getConnection(){
		if (!DriverManager.getDrivers().hasMoreElements())
			setDriver("org.hsqldb.jdbcDriver");

		if (connection == null) {
			connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
		}

		return connection;
	}
}

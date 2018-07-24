package com.miyuan.rpc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
	* @ProjectName:Appium_test
	* @ClassName: DBMng		
	* @Description: TODO
	* @author cmcc
	* @date 2016-5-10 下午1:42:20
 */

public class DBMng {
	
	static {
		try {
			//加载数据库驱动
            //Class.forName("oracle.jdbc.OracleDriver");
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	//获取数据库连接对象
	public static Connection getConn() {
		
		Connection conn = null;
		try {
			//咪咕测试环境
			conn = DriverManager.getConnection("jdbc:mysql://192.168.11.181:8066/migutest",
					"migu", "migu");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//获取语句执行对象
	public static Statement getStatement(Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}

	//获取预处理语句执行对象
	public static PreparedStatement getPreparedStatement(Connection conn, String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}

	//获取结果集对象
	public static int getUpdate(PreparedStatement pstmt) {
		int res = 0;
		try {
			res = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	//获取结果集对象
	public static int getUpdate(Statement stmt, String sql) {
		int res = 0;
		try {
			res = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return res;
	}
	
	//获取结果集对象
	public static ResultSet getResultSet(PreparedStatement pstmt) {
		ResultSet res = null;
		try {
			res = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	//获取结果集对象
	public static ResultSet getResultSet(Statement stmt, String sql) {
		ResultSet res = null;
		try {
			res = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	//关闭资源方法
	public static void close(Connection conn, Statement stmt, ResultSet res) {
		close(res);
		close(stmt);
		close(conn);
	}
	
	public static void close(Connection conn, Statement stmt) {
		close(stmt);
		close(conn);
	}

	//封装方法关闭语句对象
	private static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
	}

	//封装方法关闭结果集对象
	private static void close(ResultSet res) {
		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res = null;
		}
	}

	//封装方法关闭数据库连接对象
	private static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = null;
	}
}


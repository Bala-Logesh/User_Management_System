package com.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.usermanagement.bean.User;

public class UserDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/userDB";
	private String jdbcUsername = "root";
	private String jdbcPassword = "password";
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	
	private static final String INSERT_USER_SQL = "INSERT INTO users" + " (name,email,country) VALUES " + " (?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?;";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id=?;";
	private static final String UPDATE_USERS_SQL = "update users set name=?,email=?,country=? where id=?;";
	
	public UserDao() {
		
	}
	
	protected Connection getConnection() throws Exception {
		Connection connection = null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			if (connection != null) {
			    System.out.println("Connected to the database");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public void printSQLException(SQLException ex) {
		for(Throwable e: ex) {
			e.printStackTrace(System.err);
			System.err.println("SQLState: " + ((SQLException) e).getSQLState());
			System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
			System.err.println("Message: " + e.getMessage());
			Throwable t = e.getCause();
			while(t != null) {
				System.out.println("Cause: " + t);
				t = t.getCause();
			}
		}
	}
	
//	Insert User
	public void insertUser(User user) throws Exception {
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL)) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getCountry());
			ps.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	
//	Select user by id
	public User selectUser(int id) throws Exception {
		User user = null;
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}
	
//	Select all users
	public List<User> selectAllUsers() throws Exception{
		List<User> users = new ArrayList<>();
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS))  {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}
	
//	Update user
	public boolean updateUser(User user) throws Exception {
		boolean rowUpdated = false;
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(UPDATE_USERS_SQL)) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getCountry());
			ps.setInt(4, user.getId());
			rowUpdated = ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printSQLException(e);
		}
		return rowUpdated;
	}
	
//	Delete user
	public boolean deleteUser(int id) throws Exception {
		boolean rowDeleted = false;
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_USERS_SQL)) {
			ps.setInt(1, id);
			rowDeleted = ps.executeUpdate() > 0;
		} catch (SQLException e) {
			printSQLException(e);
		}
		return rowDeleted;
	}
}

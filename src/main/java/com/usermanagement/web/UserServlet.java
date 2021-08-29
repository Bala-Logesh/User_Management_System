package com.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.usermanagement.bean.User;
import com.usermanagement.dao.UserDao;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao userDao;

	public void init() throws ServletException {
		userDao = new UserDao();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		switch(action) {
			case "/new":
				try {
					showNewForm(request, response);
				} catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
				}
				break;
				
			case "/insert":
				try {
					insertUser(request, response);
				} catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
				}
				break;
				
			case "/delete":
				try {
					deleteUser(request, response);
				} catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
				}
				break;
				
			case "/edit":
				try {
					showEditForm(request, response);
				} catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
				}
				break;
				
			case "/update":
				try {
					updateUser(request, response);
				} catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
				}
				break;
				
			default:
				try {
					listUser(request, response);
				} catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}
	
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("user-form.jsp");
		rd.forward(request, response);
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User newUser = new User(name, email, country);
		try {
			userDao.insertUser(newUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			userDao.deleteUser(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser;
		try {
			existingUser = userDao.selectUser(id);
			request.setAttribute("user", existingUser);
			RequestDispatcher rd = request.getRequestDispatcher("user-form.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		int id = Integer.parseInt(request.getParameter("id"));
		User user = new User(id, name, email, country);
		System.out.println(user);
		try {
			userDao.updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		try {
			List<User> listUser = userDao.selectAllUsers();
			request.setAttribute("listUser", listUser);
			RequestDispatcher rd = request.getRequestDispatcher("user-list.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

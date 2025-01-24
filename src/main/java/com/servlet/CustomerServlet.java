package com.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import com.book.Customer;
import com.service.CustomerService;
import com.google.gson.JsonObject;

public class CustomerServlet extends HttpServlet
{
	CustomerService service = new CustomerService();
	Customer customer = null;
	JsonObject json = null;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		json = service.getAllCustomers();
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.print(json.toString());
		out.flush();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		String customer_name = req.getParameter("customer_name");
		customer = new Customer();
		customer.setName(customer_name);
		json = service.addCustomer(customer);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.print(json.toString());
		out.flush();
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		customer = new Customer();
		customer.setId(Integer.parseInt(req.getParameter("customer_id")));
		customer.setName(req.getParameter("customer_name"));
		json = service.updateCustomer(customer);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}

	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		customer = new Customer();
		customer.setName(req.getParameter("customer_name"));
		json = service.deleteCustomer(customer);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}

}

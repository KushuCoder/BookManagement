package com.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import com.google.gson.JsonObject;
import com.service.BillService;
import com.book.Bill;

public class BillServlet extends HttpServlet
{
	BillService service = new BillService();
	Bill bill = null;
	JsonObject json = null;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		json = service.getAllBills();
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		int customer_id = Integer.parseInt(req.getParameter("customer_id"));
		int book_id = Integer.parseInt(req.getParameter("book_id"));
		Date borrow_date = Date.valueOf(req.getParameter("borrow_date"));
		Date due_date = Date.valueOf(req.getParameter("due_date"));
		bill = new Bill(customer_id, book_id, borrow_date, due_date);
		json = service.addBill(bill);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		bill = new Bill();
		bill.setBill_id(Integer.parseInt(req.getParameter("bill_id")));
		bill.setReturn_date(Date.valueOf(req.getParameter("return_date")));
		json = service.updateBill(bill);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}
}

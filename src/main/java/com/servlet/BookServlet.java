package com.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import com.book.Book;
import com.google.gson.JsonObject;
import com.service.BookService;

public class BookServlet extends HttpServlet
{
	BookService service = new BookService();
	Book book = null;
	JsonObject json = null;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		json = service.getAllBooks();
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.print(json.toString());
		out.flush();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		String book_name = req.getParameter("book_name");
		String check = req.getParameter("availability");
		book = new Book();
		book.setName(book_name);
		if(check != null)
		{
			book.setAvailability(Boolean.parseBoolean(check));
		}
		json = service.addBook(book);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.print(json.toString());
		out.flush();
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		book = new Book();
		book.setId(Integer.parseInt(req.getParameter("book_id")));
		book.setAvailability(Boolean.parseBoolean(req.getParameter("availability")));
		json = service.updateBook(book);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}

	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		book = new Book();
		book.setName(req.getParameter("book_name"));
		json = service.deleteBook(book);
		PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		out.println(json.toString());
	}

}

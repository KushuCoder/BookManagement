package com.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.db.Initializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.book.Book;

public class BookService
{
	Initializer initializer = new Initializer();
	Connection con = initializer.creation();
	JsonObject json = null;
	JsonObject data = new JsonObject();

	public JsonObject getAllBooks()
	{
		json = new JsonObject();
		try
		{
			Statement stmt = con.createStatement();
			String query = "select * from book";
			ResultSet rs = stmt.executeQuery(query);

			if(!rs.next())
			{
				System.out.println("No rows found");
			}
			else
			{
				json.addProperty("code", 200);
				json.addProperty("message", "Retrieval Successful");
				JsonArray lst = new JsonArray();
				do
				{
					JsonObject data = new JsonObject();
					data.addProperty("book_id", rs.getInt("book_id"));
					data.addProperty("book_name", rs.getString("book_name"));
					data.addProperty("availability", rs.getBoolean("availability"));
					lst.add(data);
				} while(rs.next());
				json.add("data", lst);
			}

		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	public JsonObject addBook(Book book)
	{
		json = new JsonObject();
		try
		{
			String book_name = book.getName();
			boolean availability = book.getAvailability();
			String query = "Insert into book(book_name,availability) values('" + book_name + "'," + availability + ")";
			Statement stmt = con.createStatement();
			int rows = stmt.executeUpdate(query);
			if(rows > 0)
			{
				json.addProperty("code", 200);
				json.addProperty("message", "Insertion Successful");
				json.add("data", data);
			}
		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	public JsonObject updateBook(Book book)
	{
		json = new JsonObject();
		try
		{
			int book_id = book.getId();
			boolean availability = book.getAvailability();
			String query = "Update book set availability=? where book_id=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setBoolean(1, availability);
			stmt.setInt(2, book_id);
			int rows = stmt.executeUpdate();
			if(rows > 0)
			{
				json.addProperty("code", 200);
				json.addProperty("message", "Update successful");
			}
			else
			{
				json.addProperty("code", 404);
				json.addProperty("message", "Data not found");
			}
		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	public JsonObject deleteBook(Book book)
	{
		json = new JsonObject();
		try
		{
			String book_name = book.getName();
			String query = "Delete from book where book_name=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, book_name);
			int rows = stmt.executeUpdate();
			if(rows > 0)
			{
				json.addProperty("code", 200);
				json.addProperty("message", "Deletion successful");

			}
			else
			{
				json.addProperty("code", 404);
				json.addProperty("message", "Data not found");
			}
		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
}

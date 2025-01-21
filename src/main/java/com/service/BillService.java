package com.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import com.db.Initializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.book.Bill;

public class BillService
{
	Initializer init = new Initializer();
	Connection con = init.creation();
	JsonObject json = null;

	public JsonObject getAllBills()
	{
		json = new JsonObject();
		try
		{
			String query = "Select * from bill";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(!rs.next())
			{
				json.addProperty("code", 404);
				json.addProperty("message", "no rows found");
			}
			else
			{
				json.addProperty("code", 200);
				json.addProperty("message", "Retrieval successful");
				JsonArray array = new JsonArray();
				JsonObject data;
				do
				{
					data = new JsonObject();
					data.addProperty("bill_id", rs.getInt("bill_id"));
					data.addProperty("book_id", rs.getInt("book_id"));
					data.addProperty("customer_id", rs.getInt("customer_id"));
					data.addProperty("borrow_date", String.valueOf(rs.getDate("borrow_date")));
					data.addProperty("due_date", String.valueOf(rs.getDate("due_date")));
					data.addProperty("return_date", String.valueOf(rs.getDate("return_date")));
					data.addProperty("fine", rs.getInt("fine"));
					array.add(data);

				} while(rs.next());
				json.add("data", array);
			}
		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	public JsonObject addBill(Bill bill)
	{
		json = new JsonObject();
		try
		{

			int customer_id = bill.getCustomer_id();
			int book_id = bill.getBook_id();
			Date borrow_date = bill.getBorrow_date();
			Date due_date = bill.getDue_date();
			String query;
			Statement stmt = con.createStatement();
			query = "select availability from book where book_id=" + book_id + ";";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
			{
				boolean availability = rs.getBoolean("availability");
				if(!availability)
				{
					json.addProperty("code", 404);
					json.addProperty("message", "Book is not available now");
				}
				else
				{
					query = "Insert into bill(customer_id,book_id,borrow_date,due_date) values(" + customer_id + "," + book_id + ",'" + borrow_date + "','" + due_date + "')";
					int rows = stmt.executeUpdate(query);
					if(rows > 0)
					{
						json.addProperty("code", 200);
						json.addProperty("message", "Insertion successful");
						query = "Update book set availability=false where book_id=" + book_id + ";";
						stmt.executeUpdate(query);
					}
					else
					{
						json.addProperty("code", 404);
						json.addProperty("message", "Insertion unsuccessful");
					}
				}
			}
			else
			{
				json.addProperty("code", 404);
				json.addProperty("message", "Book with the requested ID not found");
			}
		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	public JsonObject updateBill(Bill bill)
	{
		json = new JsonObject();
		try
		{
			int bill_id = bill.getBill_id();
			Date return_date = bill.getReturn_date();
			Statement stmt = con.createStatement();
			String query = "Select book_id,due_date from bill where bill_id=" + bill_id + ";";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
			{
				int book_id = rs.getInt("book_id");
				Date due_date = rs.getDate("due_date");
				bill.setDue_date(due_date);
				int fineAmount = bill.getFine();
				if(fineAmount > 0)
				{
					query = "Update Bill set return_date='" + return_date + "' , fine=" + fineAmount + " where bill_id=" + bill_id + ";";
				}
				else
				{
					query = "Update Bill set return_date='" + return_date + "' where bill_id=" + bill_id + ";";
				}
				json = new JsonObject();
				int rows = stmt.executeUpdate(query);
				if(rows > 0)
				{
					query = "Update book set availability=true where book_id=" + book_id + ";";
					stmt.executeUpdate(query);
					json.addProperty("code", 200);
					json.addProperty("message", "Update successful");

				}
				else
				{
					json.addProperty("code", 404);
					json.addProperty("message", "Data not found");
				}
			}
			else
			{
				json.addProperty("code", 404);
				json.addProperty("message", "Bill not found");
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

package com.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.db.Initializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.book.Customer;

public class CustomerService
{
	Initializer init = new Initializer();
	JsonObject json = null;
	Connection con = init.creation();

	public JsonObject getAllCustomers()
	{
		json = new JsonObject();
		try
		{
			String query = "Select * from customer";
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
					data.addProperty("customer_id", rs.getInt("customer_id"));
					data.addProperty("customer_name", rs.getString("customer_name"));
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

	public JsonObject addCustomer(Customer customer)
	{
		json = new JsonObject();
		try
		{
			String customer_name = customer.getName();
			String query = "Insert into customer(customer_name) values(?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, customer_name);
			int rows = stmt.executeUpdate();
			if(rows > 0)
			{
				json.addProperty("code", 200);
				json.addProperty("message", "Insertion successful");
			}
			else
			{
				json.addProperty("code", 404);
				json.addProperty("message", "Insertion unsuccessful");
			}
		}
		catch(Exception e)
		{
			json.addProperty("code", 500);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	public JsonObject updateCustomer(Customer customer)
	{
		json = new JsonObject();
		try
		{
			int customer_id = customer.getId();
			String customer_name = customer.getName();
			String query = "Update customer set customer_name='" + customer_name + "' where customer_id=" + customer_id + ";";
			Statement stmt = con.createStatement();
			int rows = stmt.executeUpdate(query);
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

	public JsonObject deleteCustomer(Customer customer)
	{
		json = new JsonObject();
		try
		{
			String customer_name = customer.getName();
			String query = "Delete from customer where customer_name='" + customer_name + "';";
			Statement stmt = con.createStatement();
			int rows = stmt.executeUpdate(query);
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

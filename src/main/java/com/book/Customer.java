package com.book;

public class Customer
{
	private int customer_id;
	private String customer_name;

	public void setId(int customer_id)
	{
		this.customer_id = customer_id;
	}

	public void setName(String customer_name)
	{
		this.customer_name = customer_name;
	}

	public int getId()
	{
		return this.customer_id;
	}

	public String getName()
	{
		return this.customer_name;
	}
}

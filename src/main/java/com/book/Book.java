package com.book;

public class Book
{
	private int book_id;
	private String book_name;
	private boolean availability = false;

	public void setId(int book_id)
	{
		this.book_id = book_id;
	}

	public void setName(String book_name)
	{
		this.book_name = book_name;
	}

	public void setAvailability(Boolean availability)
	{
		this.availability = availability;
	}

	public int getId()
	{
		return this.book_id;
	}

	public String getName()
	{
		return this.book_name;
	}

	public boolean getAvailability()
	{
		return this.availability;
	}
}
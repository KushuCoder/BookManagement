package com.book;

import java.sql.Date;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class Bill
{
	private int bill_id;
	private int customer_id;
	private int book_id;
	private Date borrow_date;
	private Date due_date;
	private Date return_date;
	private int fine;

	public Bill()
	{
	}

	public Bill(int customer_id, int book_id, Date borrow_date, Date due_date)
	{
		this.book_id = book_id;
		this.customer_id = customer_id;
		this.borrow_date = borrow_date;
		this.due_date = due_date;
		this.fine = 0;
	}

	public int getFine()
	{
		LocalDate due_date = new LocalDate(this.due_date.getTime());
		LocalDate return_date = new LocalDate(this.return_date.getTime());
		fine = Days.daysBetween(due_date, return_date).getDays() * 10;
		return this.fine;
	}

	public void setBill_id(int bill_id)
	{
		this.bill_id = bill_id;
	}

	public void setReturn_date(Date return_date)
	{
		this.return_date = return_date;
	}

	public void setDue_date(Date due_date)
	{
		this.due_date = due_date;
	}

	public int getBill_id()
	{
		return this.bill_id;
	}

	public int getBook_id()
	{
		return this.book_id;
	}

	public int getCustomer_id()
	{
		return this.customer_id;
	}

	public Date getBorrow_date()
	{
		return this.borrow_date;
	}

	public Date getDue_date()
	{
		return this.due_date;
	}

	public Date getReturn_date()
	{
		return this.return_date;
	}
}

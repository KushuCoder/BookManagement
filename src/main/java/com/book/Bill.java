package com.book;

import com.db.Initializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Bill extends HttpServlet {
    Initializer init=new Initializer();
    int fine=10;
    Connection con=init.creation();
    JsonObject json=null;
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String query="Select * from bill";
        json=new JsonObject();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if(!rs.next()){
                json.addProperty("code",404);
                json.addProperty("message","no rows found");
            }
            else {
                json.addProperty("code",200);
                json.addProperty("message","Retrieval successful");
                JsonArray arry=new JsonArray();
                JsonObject data;
                do {
                    data=new JsonObject();
                    data.addProperty("bill_id",rs.getInt("bill_id"));
                    data.addProperty("book_id",rs.getInt("book_id"));
                    data.addProperty("customer_id",rs.getInt("customer_id"));
                    data.addProperty("borrow_date", String.valueOf(rs.getDate("borrow_date")));
                    data.addProperty("due_date", String.valueOf(rs.getDate("due_date")));
                    data.addProperty("return_date", String.valueOf(rs.getDate("return_date")));
                    data.addProperty("fine",rs.getInt("fine"));
                    arry.add(data);

                } while (rs.next());
                json.add("data",arry);
            }
        }catch(Exception e){
            json.addProperty("code",500);
            json.addProperty("message",e.getMessage());
        }
        PrintWriter out=res.getWriter();
        res.setContentType("application/json");
        out.println(json.toString());
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
        json=new JsonObject();
        try{

            int customer_id=Integer.parseInt(req.getParameter("customer_id"));
            int book_id=Integer.parseInt(req.getParameter("book_id"));
            Date borrow_date=Date.valueOf(req.getParameter("borrow_date"));
            System.out.println("1");
            Date due_date=Date.valueOf(req.getParameter("due_date"));
            String query;
            System.out.println("borrow_date");
            Statement stmt=con.createStatement();
            query="select availability from book where book_id="+book_id+";";
            ResultSet rs=stmt.executeQuery(query);
            if(rs.next()) {
                boolean availability = rs.getBoolean("availability");
                System.out.println("availability");
                if (!availability) {
                    json.addProperty("code", 404);
                    json.addProperty("message", "Book is not available");
                } else {
                    query = "Insert into bill(customer_id,book_id,borrow_date,due_date) values(" + customer_id + "," + book_id + ",'" + borrow_date + "','" + due_date + "')";
                    int rows = stmt.executeUpdate(query);
                    if (rows > 0) {
                        json.addProperty("code", 200);
                        json.addProperty("message", "Insertion successful");
                        query = "Update book set availability=false where book_id=" + book_id + ";";
                        stmt.executeUpdate(query);
                    } else {
                        json.addProperty("code", 404);
                        json.addProperty("message", "Insertion unsuccessful");
                    }
                }
            }
            else{
                json.addProperty("code", 404);
                json.addProperty("message", "Book not found");
            }
        }
        catch(Exception e){
            json.addProperty("code",500);
            json.addProperty("message",e.getMessage());
        }
        PrintWriter out=res.getWriter();
        res.setContentType("application/json");
        out.println(json.toString());
    }
    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws IOException {
        int bill_id = Integer.parseInt(req.getParameter("bill_id"));
        Date return_date = Date.valueOf(req.getParameter("return_date"));
        json = new JsonObject();
        try{
            String query;
            Statement stmt=con.createStatement();
            query = "Select due_date,book_id from bill where bill_id=" + bill_id + ";";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()) {
                Date due_date = rs.getDate("due_date");
                int book_id=rs.getInt("book_id");
                int fineamount = (int) (10 * (return_date.getTime() - due_date.getTime()) / (1000 * 60 * 60 * 24));
                if (fineamount > 0) {
                    query = "Update Bill set return_date='" + return_date + "' , fine=" + fineamount + " where bill_id=" + bill_id + ";";
                } else {
                    query = "Update Bill set return_date='" + return_date + "' where bill_id=" + bill_id + ";";
                }
                json = new JsonObject();
                int rows = stmt.executeUpdate(query);
                if (rows > 0) {
                    query = "Update book set availability=true where book_id=" + book_id + ";";
                    stmt.executeUpdate(query);
                    json.addProperty("code", 200);
                    json.addProperty("message", "Update successful");

                } else {
                    json.addProperty("code", 404);
                    json.addProperty("message", "Data not found");
                }
            }
            else{
                json.addProperty("code", 404);
                json.addProperty("message", "Bill not found");
            }

        } catch (Exception e) {
            json.addProperty("code", 500);
            json.addProperty("message", e.getMessage());
        }
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        out.println(json.toString());
    }
}

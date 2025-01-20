package com.book;

import com.db.Initializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Customer extends HttpServlet {
    Initializer init=new Initializer();
    JsonObject json=null;
    Connection con=init.creation();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String query="Select * from customer";
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
                    data.addProperty("customer_id",rs.getInt("customer_id"));
                    data.addProperty("customer_name",rs.getString("customer_name"));
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
            String customer_name=req.getParameter("customer_name");
            String query="Insert into customer(customer_name) values(?)";
            PreparedStatement stmt= con.prepareStatement(query);
            stmt.setString(1,customer_name);
            int rows=stmt.executeUpdate();
            if(rows>0) {
                json.addProperty("code",200);
                json.addProperty("message","Insertion successful");
            }
            else{
                json.addProperty("code",404);
                json.addProperty("message","Insertion unsuccessful");
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
        int customer_id=Integer.parseInt(req.getParameter("customer_id"));
        String customer_name=req.getParameter("customer_name");
        String query="Update customer set customer_name=? where customer_id=?";
        json=new JsonObject();
        try {

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,customer_name);
            stmt.setInt(2,customer_id);
            int rows=stmt.executeUpdate();
            if(rows>0){

                json.addProperty("code",200);
                json.addProperty("message","Update successful");

            }
            else{
                json.addProperty("code",404);
                json.addProperty("message","Data not found");
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
    protected void doDelete(HttpServletRequest req,HttpServletResponse res) throws IOException {
        String customer_name=req.getParameter("customer_name");
        String query="Delete from customer where customer_name=?";
        json=new JsonObject();
        try{

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,customer_name);
            int rows=stmt.executeUpdate();
            if(rows>0){
                json.addProperty("code",200);
                json.addProperty("message","Deletion successful");

            }
            else{
                json.addProperty("code",404);
                json.addProperty("message","Data not found");
            }

        }catch(Exception e) {
            json.addProperty("code", 500);
            json.addProperty("message", e.getMessage());
        }
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        out.println(json.toString());
    }
}

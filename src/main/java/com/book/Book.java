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


public class Book extends HttpServlet {
    Initializer initializer=new Initializer();
    Connection con=initializer.creation();
    JsonObject json=new JsonObject();
    JsonObject data=new JsonObject();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        json=new JsonObject();
        try {
            Statement stmt = con.createStatement();
            String query="select * from book";
            ResultSet rs=stmt.executeQuery(query);

            if(!rs.next()){
                System.out.println("No rows found");
            }
            else{
                json.addProperty("code",200);
                json.addProperty("message","Retrieval Successful");
                JsonArray lst = new JsonArray();
                do{
                    JsonObject data=new JsonObject();
                    data.addProperty("book_id",rs.getInt("book_id"));
                    data.addProperty("book_name",rs.getString("book_name"));
                    data.addProperty("availability",rs.getBoolean("availability"));
                    lst.add(data);
                }while(rs.next());

                json.add("data",lst);
                PrintWriter out=res.getWriter();
                out.println(json.toString());
                out.flush();
            }

        }catch(Exception e){
            json.addProperty("code",500);
            json.addProperty("message",e.getMessage());
        }
        PrintWriter out=res.getWriter();
        res.setContentType("application/json");
        out.print(json.toString());
        out.flush();
    }
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
        json=new JsonObject();
        try{
            String book_name=req.getParameter("book_name");
            String check=req.getParameter("availability");
            String query;
            if(check==null){
                query="Insert into book(book_name,availability) values('"+book_name+"',false)";
            }
            else {
                boolean availability=Boolean.parseBoolean(check);
                query = "Insert into book(book_name,availability) values('"+book_name+"',"+availability+")";
            }
            Statement stmt=con.createStatement();
            int rows=stmt.executeUpdate(query);
            if(rows>0){
                json.addProperty("code",200);
                json.addProperty("message","Insertion Successful");
                json.add("data",data);
            }
        }
        catch(Exception e){
            json.addProperty("code",500);
            json.addProperty("message",e.getMessage());
        }
        PrintWriter out=res.getWriter();
        res.setContentType("application/json");
        out.print(json.toString());
        out.flush();
    }
    @Override
    protected void doPut(HttpServletRequest req,HttpServletResponse res) throws IOException{
        int book_id=Integer.parseInt(req.getParameter("book_id"));
        boolean availability=Boolean.parseBoolean(req.getParameter("availability"));
        String query="Update book set availability=? where book_id=?";
        json=new JsonObject();
        try {

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setBoolean(1,availability);
            stmt.setInt(2,book_id);
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
    @Override
    protected void doDelete(HttpServletRequest req,HttpServletResponse res) throws IOException{
        String book_name=req.getParameter("book_name");
        String query="Delete from book where book_name=?";
        json=new JsonObject();
        try{

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,book_name);
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

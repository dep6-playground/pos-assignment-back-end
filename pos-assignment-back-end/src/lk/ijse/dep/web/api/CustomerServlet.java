package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Customer;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE,OPTIONS");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        Customer customer = jsonb.fromJson(req.getReader(),Customer.class);

        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.setContentType("application/json");

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try {
            Connection connection = cp.getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE ID = ?;");
            /*pstm.setString(1,customer.getName());
            pstm.setString(2,customer.getAddress());*/
            pstm.setInt(1,customer.getId());
            boolean success = pstm.executeUpdate()>0;

            if(success){
                resp.getWriter().println(jsonb.toJson(true));
            }
            else{
                resp.getWriter().println(jsonb.toJson(false));
            }
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
            resp.getWriter().println("<h1>Wade kachal</h1>");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        Customer customer = jsonb.fromJson(req.getReader(),Customer.class);

        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.setContentType("application/json");

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try {
            Connection connection = cp.getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET `name` = ?, address = ? WHERE ID = ?;");
            pstm.setString(1,customer.getName());
            pstm.setString(2,customer.getAddress());
            pstm.setInt(3,customer.getId());
            boolean success = pstm.executeUpdate()>0;

            if(success){
                resp.getWriter().println(jsonb.toJson(true));
            }
            else{
                resp.getWriter().println(jsonb.toJson(false));
            }
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
            resp.getWriter().println("<h1>Wade kachal</h1>");
        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        Customer customer = jsonb.fromJson(request.getReader(),Customer.class);

        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.setContentType("application/json");

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try {
            Connection connection = cp.getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?) ");
            pstm.setInt(1,customer.getId());
            pstm.setString(2,customer.getName());
            pstm.setString(3,customer.getAddress());
            boolean success = pstm.executeUpdate()>0;

            if(success){
                response.getWriter().println(jsonb.toJson(true));
            }
            else{
                response.getWriter().println(jsonb.toJson(false));
            }
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
            response.getWriter().println("<h1>Wade kachal</h1>");
        }


    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");

        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()){

            try {
                Connection con = cp.getConnection();
                System.out.println(cp.getNumActive());
                System.out.println(cp.getNumIdle());
                Statement stm = con.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

                List<Customer> customerList = new ArrayList<>();

                while (rst.next()){
                    int id= rst.getInt(1);
                    String name= rst.getString(2);
                    String address= rst.getString(3);
                    customerList.add(new Customer(id,name,address));

                }

                Jsonb jsonb = JsonbBuilder.create();
                out.println(jsonb.toJson(customerList));
                con.close();

            } catch (  SQLException exception) {
                exception.printStackTrace();
            }

        }
    }

}

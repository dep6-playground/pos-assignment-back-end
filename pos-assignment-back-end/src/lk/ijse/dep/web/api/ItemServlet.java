package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep.web.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ItemServlet", urlPatterns = "/items")
public class ItemServlet extends HttpServlet {

    /*@Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.addHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE,OPTIONS");
    }*/

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
*/


        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");

        try(Connection connection = cp.getConnection()) {
            Jsonb jsonb = JsonbBuilder.create();
            Item item = jsonb.fromJson(request.getReader(),Item.class);

            if(item.getCode() ==null || item.getDescription() == null || item.getQtyOnHand() == null || item.getUnitPrice() == null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setString(1,item.getCode());
            pstm.setString(2,item.getDescription());
            pstm.setString(3,item.getQtyOnHand());
            pstm.setString(4,item.getUnitPrice());
            if(pstm.executeUpdate()>0){
                response.setStatus(HttpServletResponse.SC_CREATED);
            }else{
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }catch (SQLIntegrityConstraintViolationException | JsonbException ex){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }catch (SQLException throwables){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

/*
        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
*/

        String code = request.getParameter("code");
        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        response.setContentType("application/json");

        try(Connection connection = cp.getConnection()){
            PrintWriter out = response.getWriter();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item"+((code != null)? " WHERE code=?":""));
            if(code != null){
                pstm.setObject(1,code);
            }
            ResultSet rst = pstm.executeQuery();
            List<Item> itemList = new ArrayList<>();
            while (rst.next()){
                code = rst.getString(1);
                String description = rst.getString(2);
                String qtyOnHand = rst.getString(3);
                String unitPrice = rst.getString(4);
                itemList.add(new Item(code,description,qtyOnHand,unitPrice));
            }

            if(code != null && itemList.isEmpty()){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            else{
                Jsonb jsonb = JsonbBuilder.create();
                out.println(jsonb.toJson(itemList));
            }
        }catch (SQLException e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

/*
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
*/

        String code = req.getParameter("code");
        if (code == null || !code.matches("I\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try (Connection connection = cp.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            pstm.setObject(1, code);
            if (pstm.executeQuery().next()) {
                pstm = connection.prepareStatement("DELETE FROM Item WHERE code=?");
                pstm.setObject(1, code);
                boolean success = pstm.executeUpdate() > 0;
                if (success) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException throwables){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/*
        resp.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
*/

        String code = req.getParameter("code");
        if(code==null || !code.matches("I\\d{3}")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try(Connection connection = cp.getConnection()) {
            Jsonb jsonb = JsonbBuilder.create();
            Item item = jsonb.fromJson(req.getReader(), Item.class);

            if(item.getCode() != null || item.getDescription() ==null || item.getQtyOnHand() == null || item.getUnitPrice() == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (item.getDescription().trim().isEmpty() || item.getQtyOnHand().trim().isEmpty() || item.getUnitPrice().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            pstm.setObject(1, code);
            if (pstm.executeQuery().next()) {
                pstm = connection.prepareStatement("UPDATE Item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?");
                pstm.setObject(1, item.getDescription());
                pstm.setObject(2, item.getQtyOnHand());
                pstm.setObject(3, item.getUnitPrice());
                pstm.setObject(4, code);
                if (pstm.executeUpdate() > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }catch (JsonbException exp){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}



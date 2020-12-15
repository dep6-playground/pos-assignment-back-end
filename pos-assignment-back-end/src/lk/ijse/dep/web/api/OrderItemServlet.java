/*
 * Copyright (c) 2020
 * @ uvin6667
 *
 */

package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep.web.model.Customer;
import lk.ijse.dep.web.model.OrderItem;
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

@WebServlet(name = "OrderItemServlet", urlPatterns = "/orderItems")
public class OrderItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        resp.setContentType("application/json");

        try(Connection connection = cp.getConnection()){
            PrintWriter out = resp.getWriter();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM OrderItem"+((orderId != null)? " WHERE orderId=?":""));
            if(orderId != null){
                pstm.setObject(1,orderId);
            }
            ResultSet rst = pstm.executeQuery();
            List<OrderItem> orderItemList = new ArrayList<>();
            while (rst.next()){
                orderId = rst.getString(1);
                String customerId = rst.getString(2);
                String itemCode = rst.getString(3);
                String qty = rst.getString(4);
                String subTotal = rst.getString(5);
                orderItemList.add(new OrderItem(orderId,customerId,itemCode,qty,subTotal));
            }

            if(orderId != null && orderItemList.isEmpty()){
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            else{
                Jsonb jsonb = JsonbBuilder.create();
                out.println(jsonb.toJson(orderItemList));
            }
        }catch (SQLException e){
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");

        try(Connection connection = cp.getConnection()) {
            Jsonb jsonb = JsonbBuilder.create();
            OrderItem orderItem = jsonb.fromJson(req.getReader(),OrderItem.class);

            if(orderItem.getOrderId() ==null || orderItem.getCustomerId() == null || orderItem.getItemCode() == null || orderItem.getQty() == null || orderItem.getSubTotal() == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO OrderItem VALUES (?,?,?,?,?)");
            pstm.setString(1,orderItem.getOrderId());
            pstm.setString(2,orderItem.getCustomerId());
            pstm.setString(3,orderItem.getItemCode());
            pstm.setString(4,orderItem.getQty());
            pstm.setString(5,orderItem.getSubTotal());
            if(pstm.executeUpdate()>0){
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }catch (SQLIntegrityConstraintViolationException ex){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }catch (SQLException throwables){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throwables.printStackTrace();
        }catch (JsonbException exp){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("orderId");
        String itemCode = req.getParameter("itemCode");
        if(orderId==null || !orderId.matches("O\\d{3}")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if(itemCode==null || !itemCode.matches("I\\d{3}")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try(Connection connection = cp.getConnection()) {

            Jsonb jsonb = JsonbBuilder.create();
            OrderItem orderItem = jsonb.fromJson(req.getReader(), OrderItem.class);

            if(orderItem.getOrderId() != null || orderItem.getItemCode() != null || orderItem.getCustomerId() == null || orderItem.getQty() == null || orderItem.getSubTotal() == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

                return;
            }

            if (orderItem.getCustomerId().trim().isEmpty() || orderItem.getQty().trim().isEmpty() || orderItem.getSubTotal().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }


            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM OrderItem WHERE orderId=?");
            pstm.setObject(1, orderId);

            if (pstm.executeQuery().next()) {
                pstm = connection.prepareStatement("UPDATE OrderItem SET qty=?, subTotal=? WHERE orderId=? AND itemCode=?");
                pstm.setObject(1,orderItem.getQty());
                pstm.setObject(2, orderItem.getSubTotal());
                pstm.setObject(3, orderId);
                pstm.setObject(4, itemCode);
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
        }    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String itemCode = req.getParameter("itemCode");
        if (orderId == null || !orderId.matches("O\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (itemCode == null || !itemCode.matches("I\\d{3}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        BasicDataSource cp = (BasicDataSource) getServletContext().getAttribute("cp");
        try (Connection connection = cp.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM OrderItem WHERE orderId=?");
            pstm.setObject(1, orderId);
            if (pstm.executeQuery().next()) {
                pstm = connection.prepareStatement("DELETE FROM OrderItem WHERE orderId=? AND itemCode=?");
                pstm.setObject(1, orderId);
                pstm.setObject(2, itemCode);
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
        }    }
}

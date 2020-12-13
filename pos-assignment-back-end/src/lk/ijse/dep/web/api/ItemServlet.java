package lk.ijse.dep.web.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.web.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ItemServlet", urlPatterns = "/items")
public class ItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
                ResultSet rst = stm.executeQuery("SELECT * FROM Item");

                List<Item> itemList = new ArrayList<>();

                while (rst.next()){
                    int code= rst.getInt(1);
                    String description= rst.getString(2);
                    int qtyOnHand= rst.getInt(3);
                    int unitPrice= rst.getInt(4);
                    itemList.add(new Item(code,description,qtyOnHand,unitPrice));
                }

                Jsonb jsonb = JsonbBuilder.create();
                out.println(jsonb.toJson(itemList));
                con.close();

            } catch (  SQLException exception) {
                exception.printStackTrace();
            }

        }
    }

}

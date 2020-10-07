package com.zlimbo.bcweb.controller;

import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.core.DefaultBlockParameter;
import com.citahub.cita.protocol.core.methods.response.*;
import com.citahub.cita.protocol.http.HttpService;
import com.zlimbo.bcweb.domain.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class InvoiceShow {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    static final String DB_URL =
            "jdbc:mysql://localhost:3306/bcweb" +
            "?useSSL=false" +
            "&useUnicode=true" +
            "&characterEncoding=UTF8" +
            "&serverTimezone=GMT" +
            "&allowPublicKeyRetrieval=true";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "123456";

    private long offset = 6;

    @RequestMapping("")
    public ModelAndView invoiceShow() {

        List<Invoice> invoices = new ArrayList<Invoice>();
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT * FROM invoice LIMIT " + (offset - 6) + ", 6";
            ResultSet resultSet = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                invoices.add(new Invoice(
                        resultSet.getString("id"),
                        resultSet.getString("hashValue"),
                        resultSet.getString("invoiceNo"),
                        resultSet.getString("buyerName"),
                        resultSet.getString("buyerTaxesNo"),
                        resultSet.getString("sellerName"),
                        resultSet.getString("sellerTaxesNo"),
                        resultSet.getString("invoiceDate"),
                        resultSet.getString("invoiceType"),
                        resultSet.getString("taxesPoint"),
                        resultSet.getString("taxes"),
                        resultSet.getString("price"),
                        resultSet.getString("pricePlusTaxes"),
                        resultSet.getString("invoiceNumber"),
                        resultSet.getString("statementSheet"),
                        resultSet.getString("statementWeight"),
                        resultSet.getString("timestamp")
                ));
            }
            resultSet.close();

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("offset: " + offset);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("invoices", invoices);

        return modelAndView;
    }


    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public String update() {

        List<Invoice> invoices = new ArrayList<Invoice>();
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT * FROM invoice LIMIT " + offset + ", 1";
            ResultSet resultSet = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                invoices.add(new Invoice(
                        resultSet.getString("id"),
                        resultSet.getString("hashValue"),
                        resultSet.getString("invoiceNo"),
                        resultSet.getString("buyerName"),
                        resultSet.getString("buyerTaxesNo"),
                        resultSet.getString("sellerName"),
                        resultSet.getString("sellerTaxesNo"),
                        resultSet.getString("invoiceDate"),
                        resultSet.getString("invoiceType"),
                        resultSet.getString("taxesPoint"),
                        resultSet.getString("taxes"),
                        resultSet.getString("price"),
                        resultSet.getString("pricePlusTaxes"),
                        resultSet.getString("invoiceNumber"),
                        resultSet.getString("statementSheet"),
                        resultSet.getString("statementWeight"),
                        resultSet.getString("timestamp")
                ));
            }
            resultSet.close();

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("offset: " + offset);

        if (invoices.isEmpty()) return "";
        offset += 1;
        Invoice invoice = invoices.get(0);
        String liItem = "<li>\n<div>" +
                invoice.getInvoiceDate() + "</div>\n<div>" +
                invoice.getBuyerName() + "</div>\n<div>" +
                invoice.getSellerName() + "</div>\n<div>" +
                invoice.getPrice() + "</div>\n<div>" +
                invoice.getTaxes() + "</div>\n<div>" +
                invoice.getPricePlusTaxes() + "</div>\n</li>";

        return liItem;
    }

}

//        String sql = "select * from invoice";
//        List<Invoice> invoices = jdbcTemplate.query(sql,
//                new RowMapper<Invoice>() {
//                    @Override
//                    public Invoice mapRow(ResultSet resultSet, int i) throws SQLException {
//                        return new Invoice(
//                                resultSet.getString("id"),
//                                resultSet.getString("hashValue"),
//                                resultSet.getString("invoiceNo"),
//                                resultSet.getString("buyerName"),
//                                resultSet.getString("buyerTaxesNo"),
//                                resultSet.getString("sellerName"),
//                                resultSet.getString("sellerTaxesNo"),
//                                resultSet.getString("invoiceDate"),
//                                resultSet.getString("invoiceType"),
//                                resultSet.getString("taxesPoint"),
//                                resultSet.getString("taxes"),
//                                resultSet.getString("price"),
//                                resultSet.getString("pricePlusTaxes"),
//                                resultSet.getString("invoiceNumber"),
//                                resultSet.getString("statementSheet"),
//                                resultSet.getString("statementWeight"),
//                                resultSet.getString("timestamp")
//                        );
//                    }
//                });



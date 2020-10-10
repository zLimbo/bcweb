package com.zlimbo.bcweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.core.methods.response.AppBlockNumber;
import com.citahub.cita.protocol.core.methods.response.NetPeerCount;
import com.citahub.cita.protocol.http.HttpService;
import com.zlimbo.bcweb.domain.Invoice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("")
public class InvoiceControl {

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
    public ModelAndView invoiceShow() throws IOException {

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

            String sql = "SELECT * FROM invoice ORDER BY timestamp LIMIT " + (offset - 6) + ", 6";
            ResultSet resultSet = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                invoices.add(new Invoice(
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
                        resultSet.getString("timestamp"),
                        resultSet.getString("contractAddress")
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
        //if (offset == 6 && invoices.size() < offset) offset = invoices.size();
        System.out.println("offset: " + offset);

        CITAj citAj = CITAj.build(new HttpService("https://testnet.citahub.com"));
        NetPeerCount netPeerCount = citAj.netPeerCount().send();
        BigInteger peerCount = netPeerCount.getQuantity();
        AppBlockNumber appBlockNumber = citAj.appBlockNumber().send();
        BigInteger blockNumber = appBlockNumber.getBlockNumber();

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("invoices", invoices);
        modelAndView.addObject("invoice", new Invoice());
        modelAndView.addObject("peerCount", peerCount);
        modelAndView.addObject("blockNumber", blockNumber);

        return modelAndView;
    }


    @RequestMapping(value = "/invoiceQuery", method = RequestMethod.GET)
    @ResponseBody
    public String invoiceQuery(String hashValue) {
        System.out.println("--------------------------------------------invoiceQuery ok");
        System.out.println(hashValue);
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

            String sql = "SELECT * FROM invoice WHERE hashValue=\"" + hashValue + "\"";
            System.out.println("query sql: " + sql);
            ResultSet resultSet = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                invoices.add(new Invoice(
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
                        resultSet.getString("timestamp"),
                        resultSet.getString("contractAddress")
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
        }
        if (invoices.isEmpty()) return "";
        Invoice invoice = invoices.get(0);
        String jsonString = JSON.toJSONString(invoice);
        System.out.println("jsonString: " + jsonString);
        return jsonString;
    }


    @RequestMapping(value = "/invoiceUpdate", method = RequestMethod.GET)
    @ResponseBody
    public String invoiceUpdate() {

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

            String sql = "SELECT * FROM invoice ORDER BY timestamp LIMIT " + offset + ", 1";
            ResultSet resultSet = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                invoices.add(new Invoice(
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
                        resultSet.getString("timestamp"),
                        resultSet.getString("contractAddress")
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
        return getInvoiceItem(invoices.get(0));
    }


    @RequestMapping(value = "/invoiceUpdate", method = RequestMethod.GET)
    @ResponseBody
    public String bcinfoUpdate() throws IOException {
        System.out.println("--------------------------------------------bcinfoUpdate ok");
        CITAj citAj = CITAj.build(new HttpService("https://testnet.citahub.com"));
        NetPeerCount netPeerCount = citAj.netPeerCount().send();
        BigInteger peerCount = netPeerCount.getQuantity();
        AppBlockNumber appBlockNumber = citAj.appBlockNumber().send();
        BigInteger blockNumber = appBlockNumber.getBlockNumber();
        
        return "";
    }

    @RequestMapping(value = "/randomInvoice", method = RequestMethod.GET)
    @ResponseBody
    public String randomInvoice() throws NoSuchAlgorithmException {
        System.out.println("--------------------------------------------randomInvoice ok");
        return JSON.toJSONString(Invoice.getRandomInvoice());
    }


    @RequestMapping("/invoiceInsert")
    @ResponseBody
    public String invoiceInsert(@RequestBody Invoice invoice) {
        System.out.println("--------------------------------------------invoiceInsert ok");
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

            String sql = "INSERT INTO invoice VALUES(" +
                    "\"" + invoice.getHashValue() +       "\", " +
                    "\"" + invoice.getInvoiceNo() +       "\", " +
                    "\"" + invoice.getBuyerName() +       "\", " +
                    "\"" + invoice.getBuyerTaxesNo() +    "\", " +
                    "\"" + invoice.getSellerName() +      "\", " +
                    "\"" + invoice.getSellerTaxesNo() +   "\", " +
                    "\"" + invoice.getInvoiceDate() +     "\", " +
                    "\"" + invoice.getInvoiceType() +     "\", " +
                    "\"" + invoice.getTaxesPoint() +      "\", " +
                    "\"" + invoice.getTaxes() +           "\", " +
                    "\"" + invoice.getPrice() +           "\", " +
                    "\"" + invoice.getPricePlusTaxes() +  "\", " +
                    "\"" + invoice.getInvoiceNumber() +   "\", " +
                    "\"" + invoice.getStatementSheet() +  "\", " +
                    "\"" + invoice.getStatementWeight() + "\", " +
                    "\"" + invoice.getTimestamp() +       "\", " +
                    "\"" + invoice.getContractAddress() + "\")";

            System.out.println("sql: " + sql);
            stmt.execute(sql);
        } catch (SQLException se) {
            System.out.println("-----------------SQLException");
            //Handle errors for JDBC
            se.printStackTrace();
            return "哈希值重复！";
        } catch (Exception e) {
            System.out.println("-----------------Exception");
            //Handle errors for Class.forName
            e.printStackTrace();
            return "系统错误！";
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
        if (offset == 6) {
            return getInvoiceItem(invoice);
        }
        return "";
    }


    String getInvoiceItem(Invoice invoice) {
        return "<li>\n<div>" +
                invoice.getInvoiceDate() + "</div>\n<div>" +
                invoice.getBuyerName() + "</div>\n<div>" +
                invoice.getSellerName() + "</div>\n<div>" +
                invoice.getInvoiceType() + "</div>\n<div>" +
                invoice.getTaxesPoint() + "</div>\n<div>" +
                invoice.getPrice() + "</div>\n<div>" +
                invoice.getTaxes() + "</div>\n<div>" +
                invoice.getPricePlusTaxes() + "</div>\n</li>";
    }

//    @ResponseBody
//    @PostMapping("/insertInvoice")
//    public String invoiceSubmit(@ModelAttribute Invoice invoice) {
//        System.out.println("--------------------------------------------insertInvoice post");
//
//        Connection conn = null;
//        Statement stmt = null;
//        try {
//            //STEP 2: Register JDBC driver
//            Class.forName("com.mysql.jdbc.Driver");
//
//            //STEP 3: Open a connection
//            System.out.println("Connecting to a selected database...");
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            System.out.println("Connected database successfully...");
//
//            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
//            stmt = conn.createStatement();
//
//            String sql = "INSERT INTO invoice VALUES(NULL, " +
//                    "\"" + invoice.getHashValue() +       "\", " +
//                    "\"" + invoice.getInvoiceNo() +       "\", " +
//                    "\"" + invoice.getBuyerName() +       "\", " +
//                    "\"" + invoice.getBuyerTaxesNo() +    "\", " +
//                    "\"" + invoice.getSellerName() +      "\", " +
//                    "\"" + invoice.getSellerTaxesNo() +   "\", " +
//                    "\"" + invoice.getInvoiceDate() +     "\", " +
//                    "\"" + invoice.getInvoiceType() +     "\", " +
//                    "\"" + invoice.getTaxesPoint() +      "\", " +
//                    "\"" + invoice.getTaxes() +           "\", " +
//                    "\"" + invoice.getPrice() +           "\", " +
//                    "\"" + invoice.getPricePlusTaxes() +  "\", " +
//                    "\"" + invoice.getInvoiceNumber() +   "\", " +
//                    "\"" + invoice.getStatementSheet() +  "\", " +
//                    "\"" + invoice.getStatementWeight() + "\", " +
//                    "\"" + invoice.getTimestamp() +       "\")";
//
//            System.out.println("sql: " + sql);
//            stmt.execute(sql);
//        } catch (SQLException se) {
//            //Handle errors for JDBC
//            se.printStackTrace();
//        } catch (Exception e) {
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        } finally {
//            //finally block used to close resources
//            try {
//                if (stmt != null)
//                    conn.close();
//            } catch (SQLException se) {
//            }// do nothing
//            try {
//                if (conn != null)
//                    conn.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }//end finally try
//        }//end try
//
//        return "redirect:";
//    }


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



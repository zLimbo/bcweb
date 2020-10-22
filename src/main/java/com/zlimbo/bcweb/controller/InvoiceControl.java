package com.zlimbo.bcweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.core.DefaultBlockParameter;
import com.citahub.cita.protocol.core.methods.response.*;
import com.citahub.cita.protocol.http.HttpService;
import com.zlimbo.bcweb.domain.Invoice;
import com.zlimbo.bcweb.domain.Sql;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

@Controller
@RequestMapping("")
public class InvoiceControl {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;


    static final int ITEM_SIZE = 6;


    private int offset = ITEM_SIZE;
    private boolean isAdd = false;
    private int txAllNumber = 0;
    //    Map<String, Integer> invoiceKindNumber;
    LinkedList<String> xAxisData;
    LinkedList<String> priceData;
    LinkedList<String> taxesData;
    private int vatNumber = 0;
    private int normalNumber = 0;
    private int professionalNumber = 0;




    public InvoiceControl() {
        xAxisData = new LinkedList<>();
        priceData = new LinkedList<>();
        taxesData = new LinkedList<>();
    }


    private void updateGraphInfo(Invoice invoice) {
        String invoiceType = invoice.getInvoiceType();
        if (invoiceType.equals("增值税发票")) {
            vatNumber += 1;
        } else if (invoiceType.equals("普通发票")) {
            normalNumber += 1;
        } else if (invoiceType.equals("专业发票")) {
            professionalNumber += 1;
        }
        xAxisData.add(invoice.getInvoiceDate() + "\n买:" +
                invoice.getBuyerName() + "\n卖:" +
                invoice.getSellerName());
        priceData.add(invoice.getPrice());
        taxesData.add(invoice.getTaxes());
        if (xAxisData.size() > ITEM_SIZE) {
            xAxisData.pollFirst();
            priceData.pollFirst();
            taxesData.pollFirst();
        }
    }


    @RequestMapping(value = {"chainTxinfo"})
    public ModelAndView invoiceShow() throws IOException {

        List<Invoice> invoices = new ArrayList<Invoice>();
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(ConnectInfo.DB_URL, ConnectInfo.USER, ConnectInfo.PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "SELECT * FROM invoice ORDER BY timestamp LIMIT " + (offset - ITEM_SIZE) + ", " + ITEM_SIZE;
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

        if (isAdd == false) {
            for (Invoice invoice : invoices) {
                updateGraphInfo(invoice);
            }
            isAdd = true;
        }


        ModelAndView modelAndView = new ModelAndView("chainTxinfo");
        modelAndView.addObject("invoices", invoices);
        modelAndView.addObject("invoice", new Invoice());
        Map<String, String> hashMap = getBcinfo();
        modelAndView.addAllObjects(hashMap);

        return modelAndView;
    }


//    @RequestMapping(value = "/invoiceQuery")
//    @ResponseBody
//    public ModelAndView invoiceQuery() {
//        ModelAndView modelAndView = new ModelAndView("invoiceQuery");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/invoiceQuery", method = RequestMethod.GET)
//    @ResponseBody
//    public String invoiceQuery(String hashValue) {
//        System.out.println("--------------------------------------------invoiceQuery ok");
//        System.out.println(hashValue);
//        List<Invoice> invoices = new ArrayList<Invoice>();
//        Connection conn = null;
//        Statement stmt = null;
//        try {
//            //STEP 2: Register JDBC driver
//            Class.forName("com.mysql.jdbc.Driver");
//
//            //STEP 3: Open a connection
//            System.out.println("Connecting to a selected database...");
//            conn = DriverManager.getConnection(ConnectInfo.DB_URL, ConnectInfo.USER, ConnectInfo.PASS);
//            System.out.println("Connected database successfully...");
//
//            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
//            stmt = conn.createStatement();
//
//            String sql = "SELECT * FROM invoice WHERE hashValue=\"" + hashValue + "\"";
//            System.out.println("query sql: " + sql);
//            ResultSet resultSet = stmt.executeQuery(sql);
//            //STEP 5: Extract data from result set
//            while (resultSet.next()) {
//                //Retrieve by column name
//                invoices.add(new Invoice(
//                        resultSet.getString("hashValue"),
//                        resultSet.getString("invoiceNo"),
//                        resultSet.getString("buyerName"),
//                        resultSet.getString("buyerTaxesNo"),
//                        resultSet.getString("sellerName"),
//                        resultSet.getString("sellerTaxesNo"),
//                        resultSet.getString("invoiceDate"),
//                        resultSet.getString("invoiceType"),
//                        resultSet.getString("taxesPoint"),
//                        resultSet.getString("taxes"),
//                        resultSet.getString("price"),
//                        resultSet.getString("pricePlusTaxes"),
//                        resultSet.getString("invoiceNumber"),
//                        resultSet.getString("statementSheet"),
//                        resultSet.getString("statementWeight"),
//                        resultSet.getString("timestamp"),
//                        resultSet.getString("contractAddress")
//                ));
//            }
//            resultSet.close();
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
//        }
//        if (invoices.isEmpty()) return "";
//        Invoice invoice = invoices.get(0);
//        String jsonString = JSON.toJSONString(invoice);
//        System.out.println("jsonString: " + jsonString);
//        return jsonString;
//    }


    @RequestMapping(value = "/invoiceUpdate", method = RequestMethod.GET)
    @ResponseBody
    public synchronized String invoiceUpdate() {

        List<Invoice> invoices = new ArrayList<Invoice>();
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(ConnectInfo.DB_URL, ConnectInfo.USER, ConnectInfo.PASS);
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
        Invoice invoice = invoices.get(0);
        updateGraphInfo(invoice);
        return getInvoiceItem(invoice);
    }


    @RequestMapping(value = "/graphUpdate", method = RequestMethod.GET)
    @ResponseBody
    public String graphUpdate() throws IOException {
        System.out.println("--------------------------------------------graphUpdate ok");

        JSONObject jsonObject = new JSONObject();
        JSONArray pieData = new JSONArray();

        JSONObject vatData = new JSONObject();
        JSONObject normalData = new JSONObject();
        JSONObject professionalData = new JSONObject();
        vatData.put("value", vatNumber);
        vatData.put("name", "增值税发票");
        normalData.put("value", normalNumber);
        normalData.put("name", "普通发票");
        professionalData.put("value", professionalNumber);
        professionalData.put("name", "专业发票");
        pieData.add(vatData);
        pieData.add(normalData);
        pieData.add(professionalData);

        jsonObject.put("pieData", pieData);

        JSONArray xAxisJson = new JSONArray();
        JSONArray priceJson = new JSONArray();
        JSONArray taxesJson = new JSONArray();

        for (int i = 0; i < xAxisJson.size(); ++i) {
            xAxisJson.add(xAxisData.get(i));
            priceJson.add(priceData.get(i));
            taxesJson.add(taxesData.get(i));
        }
        jsonObject.put("xAxisData", xAxisData);
        jsonObject.put("priceData", priceData);
        jsonObject.put("taxesData", taxesData);

        String jsonObj = jsonObject.toJSONString();
        System.out.println(jsonObj);
        return jsonObj;
    }


    @GetMapping("/invoiceInsert")
    public String invoiceForm(Model model) {
        System.out.println("----------------------------------invoiceForm ok");
        model.addAttribute("invoice", new Invoice());
        return "invoiceInsert";
    }

    @PostMapping("/invoiceInsert")
    public String invoiceSubmit(@ModelAttribute Invoice invoice) {
        System.out.println("----------------------------------invoiceSubmit ok");
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(ConnectInfo.DB_URL, ConnectInfo.USER, ConnectInfo.PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = "INSERT INTO invoice VALUES(" +
                    "\"" + invoice.getHashValue() + "\", " +
                    "\"" + invoice.getInvoiceNo() + "\", " +
                    "\"" + invoice.getBuyerName() + "\", " +
                    "\"" + invoice.getBuyerTaxesNo() + "\", " +
                    "\"" + invoice.getSellerName() + "\", " +
                    "\"" + invoice.getSellerTaxesNo() + "\", " +
                    "\"" + invoice.getInvoiceDate() + "\", " +
                    "\"" + invoice.getInvoiceType() + "\", " +
                    "\"" + invoice.getTaxesPoint() + "\", " +
                    "\"" + invoice.getTaxes() + "\", " +
                    "\"" + invoice.getPrice() + "\", " +
                    "\"" + invoice.getPricePlusTaxes() + "\", " +
                    "\"" + invoice.getInvoiceNumber() + "\", " +
                    "\"" + invoice.getStatementSheet() + "\", " +
                    "\"" + invoice.getStatementWeight() + "\", " +
                    "\"" + invoice.getTimestamp() + "\", " +
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
//        if (offset == 6) {
//            updateGraphInfo(invoice);
//            return getInvoiceItem(invoice);
//        }
        System.out.println("-----------------------------invoiceInsertResult");
        return "/invoiceInsertResult";
    }


    @RequestMapping(value = "/randomInvoice", method = RequestMethod.GET)
    @ResponseBody
    public String randomInvoice() {
        System.out.println("--------------------------------------------randomInvoice ok");
        return JSON.toJSONString(Invoice.getRandomInvoice());
    }


    @RequestMapping(value = "/bcinfoUpdate", method = RequestMethod.GET)
    @ResponseBody
    public String bcinfoUpdate() throws IOException {
        System.out.println("--------------------------------------------bcinfoUpdate ok");

        Map<String, String> hashMap = getBcinfo();
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(hashMap);

        return jsonObject.toJSONString();
    }


    Map<String, String> getBcinfo() throws IOException {
        Map<String, String> hashMap = new HashMap<>();

        CITAj service = CITAj.build(new HttpService("https://testnet.citahub.com"));
        //CITAj service = CITAj.build(new HttpService("http://139.196.208.146:1337"));

        NetPeerCount netPeerCount = service.netPeerCount().send();
        BigInteger peerCount = netPeerCount.getQuantity();
        hashMap.put("peerCount", peerCount.toString());

        AppBlockNumber appBlockNumber = service.appBlockNumber().send();
        BigInteger blockNumber = appBlockNumber.getBlockNumber();
        hashMap.put("blockNumber", blockNumber.toString());


        DefaultBlockParameter defaultParam = DefaultBlockParameter.valueOf("latest");
        AppMetaData appMetaData = service.appMetaData(defaultParam).send();
        AppMetaData.AppMetaDataResult result = appMetaData.getAppMetaDataResult();
        BigInteger chainId = result.getChainId();
        String chainName = result.getChainName();
        String genesisTS = result.getGenesisTimestamp();
        hashMap.put("chainId", chainId.toString());
        hashMap.put("chainName", chainName);
        hashMap.put("genesisTS", genesisTS);


        AppBlock appBlock = service.appGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send();
        hashMap.put("blockId", String.valueOf(appBlock.getId()));
        hashMap.put("blockJsonrpc", appBlock.getJsonrpc());

        AppBlock.Block block = appBlock.getBlock();
        hashMap.put("blockVersion", block.getVersion());
        hashMap.put("blockHash", block.getHash());

        AppBlock.Header header = block.getHeader();
        hashMap.put("headerTimestamp", header.getTimestamp().toString());
        hashMap.put("headerPrevHash", header.getPrevHash());
        hashMap.put("headerNumber", header.getNumber());
        hashMap.put("headerStateRoot", header.getStateRoot());
        hashMap.put("headerTransactionsRoot", header.getTransactionsRoot());
        hashMap.put("headerReceiptsRoot", header.getReceiptsRoot());
        hashMap.put("headerProposer", header.getProposer());

        AppBlock.Body body = block.getBody();
        List<AppBlock.TransactionObject> transactionObjects = body.getTransactions();
        int blockTxNumber = transactionObjects.size();
        hashMap.put("blockTxNumber", String.valueOf(blockTxNumber));
        txAllNumber += blockTxNumber;
        hashMap.put("txAllNumber", String.valueOf(txAllNumber));

        return hashMap;
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


    @GetMapping("/invoiceQuery")
    public ModelAndView invoiceQuery() {
        //model.addAttribute("sql", new Sql());
        ModelAndView modelAndView = new ModelAndView("invoiceQuery");
        modelAndView.addObject("sql", new Sql());
        return modelAndView;
        //return "invoiceQuery";
    }
//
    @PostMapping("/invoiceQuery")
    public ModelAndView invoiceQuery(@ModelAttribute Sql sql) {
        System.out.println("----------------------------------invoiceQuery ok");
        //System.out.println("sql: " + sql.getSql());
        List<Invoice> invoices = new ArrayList<Invoice>();
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(ConnectInfo.DB_URL, ConnectInfo.USER, ConnectInfo.PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            System.out.println("query sql: " + sql.getSql());
            ResultSet resultSet = stmt.executeQuery(sql.getSql());
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
        ModelAndView modelAndView = new ModelAndView("invoiceQueryResult");
        System.out.println("size: " + invoices.size());
        modelAndView.addObject("invoices", invoices);
        return modelAndView;
    }


}


 
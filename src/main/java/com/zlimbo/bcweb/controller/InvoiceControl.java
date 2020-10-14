package com.zlimbo.bcweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citahub.cita.protobuf.Blockchain;
import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.core.DefaultBlockParameter;
import com.citahub.cita.protocol.core.methods.response.*;
import com.citahub.cita.protocol.http.HttpService;
import com.google.gson.JsonObject;
import com.zlimbo.bcweb.domain.Invoice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

@Controller
@RequestMapping("")
public class InvoiceControl {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    static final String DB_URL =
            "jdbc:mysql://localhost:3306/ouyeel" +
            "?useSSL=false" +
            "&useUnicode=true" +
            "&characterEncoding=UTF8" +
            "&serverTimezone=GMT" +
            "&allowPublicKeyRetrieval=true";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "123456";
    static final int ITEM_SIZE = 6;


    private int offset = ITEM_SIZE;
    private boolean isAdd = false;
    private int txAllNumber = 0;
    Map<String, Integer> invoiceKindNumber;
    LinkedList<String> xAxisData;
    LinkedList<String> priceData;
    LinkedList<String> taxesData;


    public InvoiceControl() {
        invoiceKindNumber = new HashMap<>();
        invoiceKindNumber.put("增值税发票", 0);
        invoiceKindNumber.put("普通发票", 0);
        invoiceKindNumber.put("专业发票", 0);

        xAxisData = new LinkedList<>();
        priceData = new LinkedList<>();
        taxesData = new LinkedList<>();

    }


    private void updateGraphInfo(Invoice invoice) {
        String invoiceType =  invoice.getInvoiceType();
        invoiceKindNumber.put(invoiceType, 1 + invoiceKindNumber.get(invoiceType));
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


        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("invoices", invoices);
        modelAndView.addObject("invoice", new Invoice());
        Map<String, String> hashMap = getBcinfo();
        modelAndView.addAllObjects(hashMap);

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

        Set<String> keySet = invoiceKindNumber.keySet();
        for (String key: keySet) {
            JSONObject oneKV = new JSONObject();
            oneKV.put("value", invoiceKindNumber.get(key));
            oneKV.put("name", key);
            pieData.add(oneKV);
        }
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
        //System.out.println(jsonObj);
        //jsonObject.putAll(invoiceKindNumber);
        return jsonObj;
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
            updateGraphInfo(invoice);
            return getInvoiceItem(invoice);
        }
        return "";
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

//        if (appBlock.isEmpty()) {
//            System.out.println("This no block!");
//        } else {
//            AppBlock.Block block = appBlock.getBlock();
//            System.out.println("--------Block");
//            System.out.print("version: ");
//            System.out.println(block.getVersion());
//            System.out.print("hash: ");
//            System.out.println(block.getHash());
//
//            AppBlock.Header header = block.getHeader();
//            System.out.println("------------header");
//            System.out.print("timestamp: ");
//            System.out.println(header.getTimestamp());
//            System.out.print("prevHash ");
//            System.out.println(header.getPrevHash());
//            System.out.print("number: ");
//            System.out.println(header.getNumber());
//            System.out.print("stateRoot ");
//            System.out.println(header.getStateRoot());
//            System.out.print("transactionsRoot: ");
//            System.out.println(header.getTransactionsRoot());
//            System.out.print("receiptsRoot: ");
//            System.out.println(header.getReceiptsRoot());
//            System.out.print("quotaUsed: ");
//            System.out.println(header.getQuotaUsed());
//            System.out.print("proposer: ");
//            System.out.println(header.getProposer());
//
//            AppBlock.Body body = block.getBody();
//            List<AppBlock.TransactionObject> transactionObjects = body.getTransactions();
//            System.out.println("------------body(txs): ");
//            System.out.println("This is " + transactionObjects.size() + " transactions.");
//
//            for (AppBlock.TransactionObject transactionObject : transactionObjects) {
//                Transaction transaction = transactionObject.get();
//                System.out.print("hash: ");
//                System.out.println(transaction.getHash());
//                System.out.print("blockHash: ");
//                System.out.println(transaction.getBlockHash());
////                    System.out.print("blockNumber: ");
////                    System.out.println(transaction.getBlockNumber());
//                System.out.print("content: ");
//                System.out.println(transaction.getContent());
//                System.out.print("index: ");
//                System.out.println(transaction.getIndex());
//                System.out.print("from: ");
//                System.out.println(transaction.getFrom());
//                System.out.println();
//
//
//                // byte[] data = transaction.getContent().getBytes();
//                String content = transaction.getContent().substring(2);
//                System.out.println("content: " + content);
//                BigInteger bigInteger = new BigInteger(content, 16);
//                System.out.println("bigInteger: " + bigInteger);
//                byte[] data = bigInteger.toByteArray();
//                Blockchain.UnverifiedTransaction unverifiedTransaction = Blockchain.UnverifiedTransaction.parseFrom(data);
//                Blockchain.Transaction txContent = unverifiedTransaction.getTransaction();
//
//                System.out.println("----------------UnverifiedTransaction: ");
//                System.out.println("crypto: " + unverifiedTransaction.getCryptoValue());
//                System.out.println("signature: " + byteToString(unverifiedTransaction.getSignature()));
//                System.out.println("--------------------txContent: ");
//                System.out.println("to: " + txContent.getTo());
//                System.out.println("nonce: " + txContent.getNonce());
//                System.out.println("quota: " + txContent.getQuota());
//                System.out.println("valid_until_block: " + txContent.getValidUntilBlock());
//                System.out.println("data: " +  byteToString(txContent.getData()));
//                System.out.println("value: " + byteToString(txContent.getValue()));
//                System.out.println("chain_id: " + txContent.getChainId());
//                System.out.println("version: " + txContent.getVersion());
//                System.out.println("to_v1: " + byteToString(txContent.getToV1()));
//                System.out.println("chain_id_v1: " + byteToString(txContent.getChainIdV1()));
//                System.out.println();
//            }
//        }

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



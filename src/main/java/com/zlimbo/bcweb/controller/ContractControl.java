package com.zlimbo.bcweb.controller;

import com.zlimbo.bcweb.domain.Contract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Controller
@RequestMapping("")
public class ContractControl {

    @GetMapping("/deployContract")
    public String contractFrom(Model model) {
        System.out.println("--------------------------------------------contractFrom ok");
        model.addAttribute("contract", new Contract());
        return "deployContract";
    }

    @PostMapping("/deployContract")
    public String contractSubmit(@ModelAttribute Contract contract) {
        System.out.println("--------------------------------------------contractSubmit ok");
        System.out.println("tableName: " + contract.getTableName());
        System.out.println("bytecode: " + contract.getBytecode());
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

            String sql = "INSERT INTO sc VALUES(" +
                    "\"" + contract.getTableName() + "\", " +
                    "\"" + contract.getBytecode() + "\")";

            System.out.println("sql: " + sql);
            stmt.execute(sql);
        } catch (SQLException se) {
            System.out.println("-----------------SQLException");
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            System.out.println("-----------------Exception");
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
        //return "index";
        return "deployContractResult";
    }


//    @RequestMapping(value = "deployContract")
//    @ResponseBody
//    public ModelAndView deployContract() {
//        System.out.println("----------------------------------deployContract ok");
//        ModelAndView modelAndView = new ModelAndView("deployContract");
//        return modelAndView;
//    }
//
//
//    @RequestMapping(value = "/deployContract", method = RequestMethod.POST)
//    @ResponseBody
//    public String deployContract(@RequestBody Map<String, Object> params) {
//        System.out.println("--------------------------------------------deployContract ok");
//        System.out.println("tableName: " + params.get("tableName"));
//        System.out.println("bytecode: " + params.get("bytecode"));
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
//            String sql = "INSERT INTO sc VALUES(" +
//                    "\"" + params.get("tableName") + "\", " +
//                    "\"" + params.get("bytecode") + "\")";
//
//            System.out.println("sql: " + sql);
//            stmt.execute(sql);
//        } catch (SQLException se) {
//            System.out.println("-----------------SQLException");
//            //Handle errors for JDBC
//            se.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("-----------------Exception");
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
//
//        }
//        return "部署成功";
//    }
}

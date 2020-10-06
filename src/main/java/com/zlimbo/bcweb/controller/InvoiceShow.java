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
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("")
public class InvoiceShow {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("")
    public ModelAndView invoiceShow() throws IOException {

        //CITAj service = CITAj.build(new HttpService("http://139.196.208.146:1337"));
        //CITAj service = CITAj.build(new HttpService("https://testnet.citahub.com"));

        String sql = "select * from invoice";
        List<Invoice> invoices = jdbcTemplate.query(sql,
                new RowMapper<Invoice>() {
                    @Override
                    public Invoice mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new Invoice(
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
                        );
                    }
                });
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("invoices", invoices);

        return modelAndView;
    }
}




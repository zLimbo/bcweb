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

        CITAj service = CITAj.build(new HttpService("http://139.196.208.146:1337"));
        testCitaService(service);

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


    public void testCitaService(CITAj service) throws IOException {

        NetPeerCount netPeerCount = service.netPeerCount().send();
        BigInteger peerCount = netPeerCount.getQuantity();
        System.out.print("peerCount: ");
        System.out.println(peerCount);

        String addr1 = "0x034f4fcd88b349edc5e30860b088736b82e9ccfc";
        DefaultBlockParameter defaultBlockParameter1 = DefaultBlockParameter.valueOf("latest");
        AppGetBalance getBalance = service.appGetBalance(addr1, defaultBlockParameter1).send();
        BigInteger balance = getBalance.getBalance();
        System.out.print("balance: ");
        System.out.println(balance);

        String addr2 = "0x73dd1d91001cce116cce33ca75f2af5f96898ed2e9c83d5cf0045e99e0f2e5e0";
        DefaultBlockParameter defaultBlockParameter2 = DefaultBlockParameter.valueOf("latest");
        AppGetAbi getAbi = service.appGetAbi(addr2, defaultBlockParameter2).send();
        String abi = getAbi.getAbi();
        System.out.print("abi: ");
        System.out.println(abi);

        DefaultBlockParameter defaultParam = DefaultBlockParameter.valueOf("latest");
        AppMetaData appMetaData = service.appMetaData(defaultParam).send();
        AppMetaData.AppMetaDataResult result = appMetaData.getAppMetaDataResult();
        BigInteger chainId = result.getChainId();
        String chainName = result.getChainName();
        String genesisTS = result.getGenesisTimestamp();
        System.out.print("chainId: ");
        System.out.println(chainId);
        System.out.print("chainName: ");
        System.out.println(chainName);
        System.out.print("genesisTS: ");
        System.out.println(genesisTS);

        AppBlockNumber result2 = service.appBlockNumber().send();
        BigInteger blockNumber = result2.getBlockNumber();
        System.out.print("blockNumber: ");
        System.out.print(blockNumber);
    }
}

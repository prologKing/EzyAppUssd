/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allianz.service;

import com.allianz.dto.AccessTypeDTO;
import com.allianz.dto.CustomerTbDTO;
import com.allianz.dto.DriverLicenseModel;
import com.allianz.dto.PassportModel;
import com.allianz.dto.ProductAgentsTbDTO;
import com.allianz.dto.ProductConstantSetupDTO;
import com.allianz.dto.ProductFreqMinAmtDTO;
import com.allianz.dto.ProductPlanDTO;
import com.allianz.dto.ProductRidersDTO;
import com.allianz.dto.ProductTbDTO;
import com.allianz.dto.ProductTermDTO;
import com.allianz.dto.Prompts;
import com.allianz.dto.SSNITModel;
import com.allianz.dto.TmpUssdDataDTO;
import com.allianz.dto.USSDReqs;
import com.allianz.dto.USSDResps;
import com.allianz.dto.UssdRequest;
import com.allianz.dto.UssdResponse;
import com.allianz.dto.UssdResponse2;
import com.allianz.dto.VotersModel;
import com.allianz.logic.HTTPRequestLogic;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Developer
 */
@WebServlet(name = "EazyAppUSSDGateway", urlPatterns = {"/eAZyappUSSD"})
public class EazyAppUSSDGateway extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
//        out.print("CAP USSD");
        UssdRequest ussdRequest = new UssdRequest();
        USSDReqs uRequest = new USSDReqs();
        Prompts prompt = new Prompts();
        USSDResps ussdResponse = new USSDResps();
        UssdResponse uResponse = new UssdResponse();
        UssdResponse2 uResponse2 = new UssdResponse2();
        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");
        Gson gton = new Gson();
        try {
            TmpUssdDataDTO tmodel = null;
            /* TODO output your page here. You may use following sample code. */
            StringBuilder requestBuffer = new StringBuilder();
            String lineRead;
            while ((lineRead = request.getReader().readLine()) != null) {
                requestBuffer.append(lineRead);
            }
            System.out.println(requestBuffer.toString());
            uRequest = gton.fromJson(requestBuffer.toString(), USSDReqs.class);
            ussdRequest = uRequest.getUSSDReq();
            System.out.println(ussdRequest);

            try {
                if (ussdRequest != null) {
                    String url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/" + ussdRequest.getMsisdn();
                    tmodel = new HTTPRequestLogic().getTmpUssdData(url);
                    if (tmodel.getId() == null) {
                        tmodel = new TmpUssdDataDTO(0, ussdRequest.getMsisdn(), 1);
                        tmodel.setValue19(ussdRequest.getNetwork().toUpperCase());
                        tmodel.setValue20(ussdRequest.getUserSessionID());
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        if (res.contains("success")) {
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/" + ussdRequest.getMsisdn();
                            tmodel = new HTTPRequestLogic().getTmpUssdData(url);
                        }
                    } else {
                        if (!tmodel.getValue20().equals(ussdRequest.getUserSessionID())) {
                            tmodel = new TmpUssdDataDTO(0, ussdRequest.getMsisdn(), 1);
                            tmodel.setValue19(ussdRequest.getNetwork().toUpperCase());
                            tmodel.setValue20(ussdRequest.getUserSessionID());
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            if (res.contains("success")) {
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/" + ussdRequest.getMsisdn();
                                tmodel = new HTTPRequestLogic().getTmpUssdData(url);
                            }
                        }
                    }

                    if (tmodel.getScreenNo() == 1) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        tmodel.setScreenNo(2);
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        if (res.contains("success")) {
                            String[] array = {"1. Life Insurance.", "2. General Insurance."};
                            ussdResponse.setAction("showMenu");
                            ussdResponse.setTitle("Welcome to Allianz Ghana.");
                            System.out.println("Welcome to Allianz Ghana. \n1. Life Insurance. \n2. General Insurance.");
                            ussdResponse.setMenus(array);
                            uResponse.setUSSDResp(ussdResponse);
                        } else {
                            //new CAPUSSDLogic().deleteUssdData(bURL, output);
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Error connecting to server. Contact support...");
                            System.out.println("Error connecting to server. Contact support...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 2) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                        AccessTypeDTO accesstype = new HTTPRequestLogic().getAccessType(url);
                        tmodel.setScreenNo(3);
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        if (res.contains("success")) {
                            switch (ussdRequest.getMsg()) {
                                case "1":
                                    if (accesstype.getCusttype().equals("AGT") || accesstype.getCusttype().equals("NON")) {
                                        String[] array = {"1. Register."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Welcome to Allianz Life.");
                                        System.out.println("Welcome to Allianz Life. \n1. Register.");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    } else if (accesstype.getCusttype().equals("CAG") || accesstype.getCusttype().equals("CUS")) {
                                        String[] array = {"1. Register.", "2. Pay Premium.", "3. Stop Auto Deduct.", "4. Claims.", "5. Update Records.", "6. Check Balance.", "7. T & C.", "8. Change Pin.", "9. Helpline."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Welcome to Allianz Life.");
                                        System.out.println("Welcome to Allianz Life. \n1. Register. \n2. Pay Premium. \n3. Stop Auto Deduct. \n4. Claims. \n5. Update Records. \n6. Check Balance. \n7. T & C. \n8. Change Pin. \n9. Helpline. ");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    }

                                    break;
                                case "2":
                                    String[] array = {"1. Register.", "2. Buy Policy.", "3. Update Records.", "4. Change Pin.", "5. T & C.", "6. Helpline."};
                                    ussdResponse.setAction("showMenu");
                                    ussdResponse.setTitle("Welcome to Allianz General.");
                                    System.out.println("Welcome to Allianz General. \n1. Register. \n2. Buy Policy. \n3. Update Records. \n4. Change Pin. \n5. T & C. \n6. Helpline. ");
                                    ussdResponse.setMenus(array);
                                    uResponse.setUSSDResp(ussdResponse);
                                    break;
                                default:
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Invalid Entry...");
                                    System.out.println("Invalid Entry...");
                                    uResponse2.setUSSDResp(prompt);
                                    break;
                            }
                        } else {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Error connecting to server. Contact support...");
                            System.out.println("Error connecting to server. Contact support...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 3) {
                        tmodel.setScreenNo(4);
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                        AccessTypeDTO accesstype = new HTTPRequestLogic().getAccessType(url);
                        if (res.contains("success")) {
                            if (accesstype.getCusttype().equals("AGT") || accesstype.getCusttype().equals("NON") || (accesstype.getCusttype().equals("CAG") && "1".equals(ussdRequest.getMsg()))) {
                                switch (ussdRequest.getMsg()) {
                                    case "1":
                                        if (accesstype.getCusttype().equals("NON")) {
                                            String[] array = {"1. Voter ID.", "2. Passport.", "3. Drivers License.", "4. SSNIT."};
                                            ussdResponse.setAction("showMenu");
                                            ussdResponse.setTitle("Select ID Type:");
                                            System.out.println("Select ID Type: \n1. Voter ID. \n2. Passport. \n3. Drivers License. \n4. SSNIT.");
                                            ussdResponse.setMenus(array);
                                            uResponse.setUSSDResp(ussdResponse);
                                        } else {
                                            tmodel.setScreenNo(112);
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                            res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                            if (res.contains("success")) {
                                                String[] array = {"1. Register for self.", "2. Register for customer."};
                                                ussdResponse.setAction("showMenu");
                                                ussdResponse.setTitle("Registration type:");
                                                System.out.println("Registration type: \n1. Register for self. \n2. Register for customer.");
                                                ussdResponse.setMenus(array);
                                                uResponse.setUSSDResp(ussdResponse);
                                            } else {
                                                prompt.setAction("prompt");
                                                prompt.setMenus("");
                                                prompt.setTitle("Error connecting to server. Contact support...");
                                                System.out.println("Error connecting to server. Contact support...");
                                                uResponse2.setUSSDResp(prompt);
                                            }
                                        }
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }
                            } else {
                                switch (ussdRequest.getMsg()) {
                                    case "1":
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/customer/" + ussdRequest.getMsisdn();
                                        CustomerTbDTO cusInfo = new HTTPRequestLogic().getCustomerInfo(url);
                                        tmodel.setScreenNo(100);
                                        tmodel.setValue2(cusInfo.getCusFirstname());
                                        tmodel.setValue3(cusInfo.getCusSurname());
                                        tmodel.setValue4(cusInfo.getCusGender());
                                        tmodel.setValue5(cusInfo.getCusIdNo());
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            String fullname = cusInfo.getCusFirstname() + " " + cusInfo.getCusSurname();
                                            String[] array = {fullname, "1. Confirm.", "0. Back to Main Menu."};
                                            ussdResponse.setAction("showMenu");
                                            ussdResponse.setTitle("Find your Information below:");
                                            System.out.println("Find your Information below:\n" + fullname
                                                    + "\n1. Confirm. \n0. Back to Main Menu.");
                                            ussdResponse.setMenus(array);
                                            uResponse.setUSSDResp(ussdResponse);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                        break;
                                    case "2":
                                        String[] array1 = {"1. Pay Premium."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Pay Premium");
                                        System.out.println("Pay Premium");
                                        ussdResponse.setMenus(array1);
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "3":
                                        String[] arrayy2 = {"1. Stop Auto-Deduction."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Stop Auto-Deduction.");
                                        System.out.println("Stop Auto-Deduction.");
                                        ussdResponse.setMenus(arrayy2);
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "4":
                                        String[] arrayy3 = {"1. Claims"};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Claims.");
                                        System.out.println("Claims.");
                                        ussdResponse.setMenus(arrayy3);
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }

                            }
                        } else {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Error connecting to server. Contact support...");
                            System.out.println("Error connecting to server. Contact support...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 112) {
                        if (null != ussdRequest.getMsg()) {
                            tmodel.setValue1(ussdRequest.getMsg());
                            tmodel.setScreenNo(5);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                            AccessTypeDTO accesstype = new HTTPRequestLogic().getAccessType(url);
                            if (res.contains("success")) {
                                switch (ussdRequest.getMsg()) {
                                    case "1":

                                        tmodel.setScreenNo(100);
                                        ProductAgentsTbDTO pAgent = new ProductAgentsTbDTO();
                                        CustomerTbDTO cusInfo = new CustomerTbDTO();
                                        if (accesstype.getCusttype().equals("AGT")) {
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/agent/" + ussdRequest.getMsisdn();
                                            pAgent = new HTTPRequestLogic().getAgentInfo(url);
                                            tmodel.setValue2(pAgent.getPagtOthname());
                                            tmodel.setValue3(pAgent.getPagtSurname());
                                            tmodel.setValue4(pAgent.getPagtGender());
                                        } else {
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/customer/" + ussdRequest.getMsisdn();
                                            cusInfo = new HTTPRequestLogic().getCustomerInfo(url);
                                            tmodel.setValue2(cusInfo.getCusFirstname());
                                            tmodel.setValue3(cusInfo.getCusSurname());
                                            tmodel.setValue4(cusInfo.getCusGender());
                                            tmodel.setValue5(cusInfo.getCusIdNo());
                                        }
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            String fullname = accesstype.getCusttype().equals("AGT") ? pAgent.getPagtOthname() + " " + pAgent.getPagtSurname()
                                                    : cusInfo.getCusFirstname() + " " + cusInfo.getCusSurname();
                                            String[] array = {fullname, "1. Confirm.", "0. Back to Main Menu."};
                                            ussdResponse.setAction("showMenu");
                                            ussdResponse.setTitle("Find your Information below:");
                                            System.out.println("Find your Information below:\n" + fullname
                                                    + "\n1. Confirm. \n0. Back to Main Menu.");
                                            ussdResponse.setMenus(array);
                                            uResponse.setUSSDResp(ussdResponse);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                        break;
                                    case "2":
                                        tmodel.setScreenNo(113);
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            ussdResponse.setAction("input");
                                            ussdResponse.setTitle("Enter Customer Phone Number e.g. 0540100200:");
                                            System.out.println("Enter Customer Phone Number e.g. 0540100200:");
                                            uResponse.setUSSDResp(ussdResponse);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        }
                    }else if (tmodel.getScreenNo() == 98) {
                        if (null != ussdRequest.getMsg()) {
                            int cnt = 1;
                            StringBuilder builder = new StringBuilder();
                            tmodel.setScreenNo(99);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            if (res.contains("success")) {
                                switch (ussdRequest.getMsg()) {
                                    case "1":
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/uproduct/" + "233" + tmodel.getValue21().substring(1);
                                        List<ProductConstantSetupDTO> plist = new HTTPRequestLogic().getUnsubscribedProducts(url);

                                        String[] array = new String[plist.size()];
                                        for (ProductConstantSetupDTO rrmodel : plist) {
                                            array[cnt - 1] = cnt + ". " + rrmodel.getProductName();
                                            builder.append(cnt++).append(". ").append(rrmodel.getProductName()).append("\n");
                                        }
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select Product:");
                                        System.out.println("Select Product:" + builder.toString());
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "0":
                                        tmodel.setScreenNo(3);
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                            uResponse = this.mainMenu(url);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        }
                    }else if (tmodel.getScreenNo() == 99) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/uproduct/" + "233" + tmodel.getValue21().substring(1);
                        List<ProductConstantSetupDTO> plist = new HTTPRequestLogic().getUnsubscribedProducts(url);
                        try {
                            int val = Integer.parseInt(ussdRequest.getMsg());
                            if (val > 0 && val <= plist.size()) {
                                tmodel.setValue7(String.valueOf(plist.get(val - 1).getPcsPdtSysid()));
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdtinfo/" + tmodel.getValue7();
                                ProductTbDTO pdtDTO = new HTTPRequestLogic().getProductTbInfo(url);
                                tmodel.setScreenNo("Y".equals(pdtDTO.getPdtPlnDepent()) ? 101 : 8);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    if ("Y".equals(pdtDTO.getPdtPlnDepent())) {
                                        String[] array = {"1. Spouse.", "2. Child.", "3. Parent.", "4. Parent-in-law.", "5. Sibling.", "6. Skip."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select another person to be covered:");
                                        System.out.println("Select another person to be covered: \n1. Spouse. \n2. Child. \n3. Parent. \n4. Parent-in-law. \n5. Sibling. \n6. Skip.");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    } else {
                                        String[] array = {"0. Go back to Main Menu."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Enter Full Name of your Trustee (e.g. John Doe)");
                                        System.out.println("Enter Full Name of your Trustee (e.g. John Doe) \n0. Go back to Main Menu.");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 100) {
                        if (null != ussdRequest.getMsg()) {
                            int cnt = 1;
                            StringBuilder builder = new StringBuilder();
                            tmodel.setScreenNo(7);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            if (res.contains("success")) {
                                switch (ussdRequest.getMsg()) {
                                    case "1":
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/uproduct/" + ussdRequest.getMsisdn();
                                        List<ProductConstantSetupDTO> plist = new HTTPRequestLogic().getUnsubscribedProducts(url);

                                        String[] array = new String[plist.size()];
                                        for (ProductConstantSetupDTO rrmodel : plist) {
                                            array[cnt - 1] = cnt + ". " + rrmodel.getProductName();
                                            builder.append(cnt++).append(". ").append(rrmodel.getProductName()).append("\n");
                                        }
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select Product:");
                                        System.out.println("Select Product:" + builder.toString());
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "0":
                                        tmodel.setScreenNo(3);
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                            uResponse = this.mainMenu(url);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        }
                    } else if (tmodel.getScreenNo() == 113) {
                        if (null != ussdRequest.getMsg()) {
                            try {
                                int val = Integer.parseInt(ussdRequest.getMsg().length() == 10 ? ussdRequest.getMsg() : ussdRequest.getMsg() + "a");
                                tmodel.setValue21(ussdRequest.getMsg());
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/customer/" + "233" + tmodel.getValue21().substring(1);
                                CustomerTbDTO cusInfo = new HTTPRequestLogic().getCustomerInfo(url);
                                tmodel.setScreenNo(4);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    if(cusInfo.getCustomerTbPK().getCusSysid() == 0){
                                    String[] array = {"1. Voter ID.", "2. Passport.", "3. Drivers License.", "4. SSNIT."};
                                    ussdResponse.setAction("showMenu");
                                    ussdResponse.setTitle("Select ID Type:");
                                    System.out.println("Select ID Type: \n1. Voter ID. \n2. Passport. \n3. Drivers License. \n4. SSNIT.");
                                    ussdResponse.setMenus(array);
                                    uResponse.setUSSDResp(ussdResponse);
                                    }else{
                                        tmodel.setScreenNo(98);
                                        tmodel.setValue2(cusInfo.getCusFirstname());
                                        tmodel.setValue3(cusInfo.getCusSurname());
                                        tmodel.setValue4(cusInfo.getCusGender());
                                        tmodel.setValue5(cusInfo.getCusIdNo());
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            String fullname = cusInfo.getCusFirstname() + " " + cusInfo.getCusSurname();
                                            String[] array = {fullname, "1. Confirm.", "0. Back to Main Menu."};
                                            ussdResponse.setAction("showMenu");
                                            ussdResponse.setTitle("Find your Information below:");
                                            System.out.println("Find your Information below:\n" + fullname
                                                    + "\n1. Confirm. \n0. Back to Main Menu.");
                                            ussdResponse.setMenus(array);
                                            uResponse.setUSSDResp(ussdResponse);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } catch (NumberFormatException ex) {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        }
                    } else if (tmodel.getScreenNo() == 4) {
                        if (null != ussdRequest.getMsg()) {
                            tmodel.setValue1(ussdRequest.getMsg());
                            tmodel.setScreenNo(5);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            if (res.contains("success")) {
                                switch (ussdRequest.getMsg()) {
                                    case "1":
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Enter Voter ID Number:");
                                        System.out.println("Enter Voter ID Number:");
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "2":
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Enter Passport ID Number:");
                                        System.out.println("Enter Passport ID Number:");
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "3":
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Enter Voter ID Number:");
                                        System.out.println("Enter Voter ID Number:");
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "4":
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Enter SSNIT ID Number:");
                                        System.out.println("Enter SSNIT ID Number:");
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        }
                    } else if (tmodel.getScreenNo() == 5) {
                        if (null != ussdRequest.getMsg()) {
                            boolean successful = false;
                            tmodel.setScreenNo(111);

                            try {
                                if (tmodel.getValue1().equals("4")) {
                                    SSNITModel smodel = new HTTPRequestLogic().FindSSNITIdentification(ussdRequest.getMsg(), false);
                                    if (smodel.getResponseCode().equals("200")) {

                                        tmodel.setValue2(smodel.getFullName().substring(0, smodel.getFullName().indexOf(" "))); //firstname
                                        tmodel.setValue3(smodel.getFullName().substring(smodel.getFullName().indexOf(" "))); //lastname
                                        tmodel.setValue4(smodel.getSex());
                                        tmodel.setValue5(ussdRequest.getMsg()); //ID Number
                                        successful = true;
                                        // dmodel.setPicture(smodel.getPhoto());
                                    }
                                } else if (tmodel.getValue1().equals("2")) {
                                    PassportModel smodel = new HTTPRequestLogic().FindPassportIdentification(ussdRequest.getMsg(), false, false);
                                    if (smodel.getResponseCode().equals("200")) {
                                        tmodel.setValue2(smodel.getFirstName());
                                        tmodel.setValue3(smodel.getLastName());
                                        tmodel.setValue4(smodel.getGender());
                                        tmodel.setValue5(ussdRequest.getMsg()); //ID Number
                                        successful = true;
                                        //dmodel.setPicture(smodel.getPicture());
                                    }
                                } else if (tmodel.getValue1().equals("3")) {
                                    DriverLicenseModel smodel = new HTTPRequestLogic().FindDriverLicense(ussdRequest.getMsg(), "", false);
                                    if (smodel.getResponseCode().equals("200")) {
                                        tmodel.setValue2(smodel.getName().substring(0, smodel.getName().indexOf(" ")));
                                        tmodel.setValue3(smodel.getName().substring(smodel.getName().indexOf(" ")));
                                        tmodel.setValue4("MALE");
                                        tmodel.setValue5(ussdRequest.getMsg()); //ID Number
                                        successful = true;
                                        //dmodel.setPicture(smodel.getDriverImage());
                                    }
                                } else {
                                    VotersModel smodel = new VotersModel();
                                    try {
                                        smodel = new HTTPRequestLogic().FindVoterIdentification(ussdRequest.getMsg(), false);
                                    } catch (IOException v) {
                                        tmodel.setValue2("Bolaji");
                                        tmodel.setValue3("Adeniji");
                                        tmodel.setValue4("MALE");
                                        tmodel.setValue5(ussdRequest.getMsg()); //ID Number
                                        successful = true;

                                    }
//                                    if (smodel.getResponseCode().equals("200")) {
//                                        tmodel.setValue2(smodel.getFullname().substring(0, smodel.getFullname().indexOf(" ")));
//                                        tmodel.setValue3(smodel.getFullname().substring(smodel.getFullname().indexOf(" ")));
//                                        tmodel.setValue4(smodel.getSex().equals("M") ? "MALE" : "FEMALE");
//                                        tmodel.setValue5(ussdRequest.getMsg()); //ID Number
//                                        successful = true;
//                                        //dmodel.setPicture(smodel.getPicture());
//                                    }
                                }
                                if (successful) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                    String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                    if (res.contains("success")) {
                                        String fullname = tmodel.getValue2() + " " + tmodel.getValue3();
                                        String[] idType = {"Voter ID", "Passport", "Drivers License", "SSNIT"};
                                        String[] array = {fullname, idType[Integer.parseInt(tmodel.getValue1()) - 1] + ": " + ussdRequest.getMsg(), "1. Confirm.", "0. Back to Main Menu."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Find your Information below:");
                                        System.out.println("Find your Information below:\n" + fullname + "\n" + idType[Integer.parseInt(tmodel.getValue1()) - 1] + ":" + ussdRequest.getMsg()
                                                + "\n1. Confirm. \n0. Back to Main Menu.");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);

                                    } else {
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Error connecting to server. Contact support...");
                                        System.out.println("Error connecting to server. Contact support...");
                                        uResponse2.setUSSDResp(prompt);

                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Your ID information could not be validated...");
                                    System.out.println("Your ID information could not be validated...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } catch (IOException v) {
                                String[] idType = {"Voter ID", "Passport", "Drivers License", "SSNIT"};
                                if ("two".equals(tmodel.getValue2())) {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Your ID information could not be validated...");
                                    System.out.println("Your ID information could not be validated...");
                                    uResponse2.setUSSDResp(prompt);
                                } else {
                                    tmodel.setValue2("two");
                                    tmodel.setScreenNo(5);
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                    String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                    if (res.contains("success")) {
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Your ID information could not be validated. Please try again. \n Enter your " + idType[Integer.parseInt(tmodel.getValue1()) - 1] + " Number:");
                                        System.out.println("Enter Voter ID Number:");
                                        uResponse.setUSSDResp(ussdResponse);
                                    } else {
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Error connecting to server. Contact support...");
                                        System.out.println("Error connecting to server. Contact support...");
                                        uResponse2.setUSSDResp(prompt);

                                    }
                                }
                            }
                        }
                    } else if (tmodel.getScreenNo() == 111) {
                        if (null != ussdRequest.getMsg()) {
                            tmodel.setScreenNo(6);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            if (res.contains("success")) {
                                switch (ussdRequest.getMsg()) {
                                    case "1":
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Enter Date of Birth (Date format: dd/MM/yyyy e.g. 31/07/1970)");
                                        System.out.println("Enter Date of Birth (Date format: dd/MM/yyyy e.g. 31/07/1970)");
                                        uResponse.setUSSDResp(ussdResponse);
                                        break;
                                    case "0":
                                        tmodel.setScreenNo(3);
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                            uResponse = this.mainMenu(url);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);
                                        }
                                        break;
                                    default:
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                        break;
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        }
                    } else if (tmodel.getScreenNo() == 6) {
                        int cnt = 1;
                        StringBuilder builder = new StringBuilder();
                        try {
                            Date date = inSDF.parse(ussdRequest.getMsg());
                            String outDate = outSDF.format(date);
                            tmodel.setScreenNo(7);
                            tmodel.setValue6(outDate);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/uproduct/" + ussdRequest.getMsisdn();
                            List<ProductConstantSetupDTO> plist = new HTTPRequestLogic().getUnsubscribedProducts(url);
                            if (res.contains("success")) {
                                String[] array = new String[plist.size()];
                                for (ProductConstantSetupDTO rrmodel : plist) {
                                    array[cnt - 1] = cnt + ". " + rrmodel.getProductName();
                                    builder.append(cnt++).append(". ").append(rrmodel.getProductName()).append("\n");
                                }
                                ussdResponse.setAction("showMenu");
                                ussdResponse.setTitle("Select Product:");
                                System.out.println("Select Product:" + builder.toString());
                                ussdResponse.setMenus(array);
                                uResponse.setUSSDResp(ussdResponse);

                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (ParseException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid date format...");
                            System.out.println("Invalid date format...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 7) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/uproduct/" + ussdRequest.getMsisdn();
                        List<ProductConstantSetupDTO> plist = new HTTPRequestLogic().getUnsubscribedProducts(url);
                        try {
                            int val = Integer.parseInt(ussdRequest.getMsg());
                            if (val > 0 && val <= plist.size()) {
                                tmodel.setValue7(String.valueOf(plist.get(val - 1).getPcsPdtSysid()));
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdtinfo/" + tmodel.getValue7();
                                ProductTbDTO pdtDTO = new HTTPRequestLogic().getProductTbInfo(url);
                                tmodel.setScreenNo("Y".equals(pdtDTO.getPdtPlnDepent()) ? 101 : 8);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    if ("Y".equals(pdtDTO.getPdtPlnDepent())) {
                                        String[] array = {"1. Spouse.", "2. Child.", "3. Parent.", "4. Parent-in-law.", "5. Sibling.", "6. Skip."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select another person to be covered:");
                                        System.out.println("Select another person to be covered: \n1. Spouse. \n2. Child. \n3. Parent. \n4. Parent-in-law. \n5. Sibling. \n6. Skip.");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    } else {
                                        String[] array = {"0. Go back to Main Menu."};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Enter Full Name of your Trustee (e.g. John Doe)");
                                        System.out.println("Enter Full Name of your Trustee (e.g. John Doe) \n0. Go back to Main Menu.");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    }  else if (tmodel.getScreenNo() == 101) {
                        tmodel.setScreenNo(!"6".equals(ussdRequest.getMsg()) ? 102 : 8);
                        String[] cover = {"Spouse", "Child", "Parent", "Parent-in-law", "Sibling"};
                        tmodel.setValue22(!"6".equals(ussdRequest.getMsg()) ? cover[Integer.parseInt(ussdRequest.getMsg()) - 1] : null);
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        if (res.contains("success")) {
                            if (!"6".equals(ussdRequest.getMsg())) {
                                String[] array = {"0. Go back to Main Menu."};
                                ussdResponse.setAction("showMenu");
                                ussdResponse.setTitle("Enter Full Name of your " + cover[Integer.parseInt(ussdRequest.getMsg()) - 1] + " (e.g. John Doe)");
                                System.out.println("Enter Full Name of your " + cover[Integer.parseInt(ussdRequest.getMsg()) - 1] + " (e.g. John Doe) \n0. Go back to Main Menu.");
                                ussdResponse.setMenus(array);
                                uResponse.setUSSDResp(ussdResponse);
                            } else {
                                String[] array = {"0. Go back to Main Menu."};
                                ussdResponse.setAction("showMenu");
                                ussdResponse.setTitle("Enter Full Name of your Trustee (e.g. John Doe)");
                                System.out.println("Enter Full Name of your Trustee (e.g. John Doe) \n0. Go back to Main Menu.");
                                ussdResponse.setMenus(array);
                                uResponse.setUSSDResp(ussdResponse);
                            }
                        } else {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Error connecting to server. Contact support...");
                            System.out.println("Error connecting to server. Contact support...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 102) {
                        switch (ussdRequest.getMsg()) {
                            case "0":
                                tmodel.setScreenNo(3);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                    uResponse = this.mainMenu(url);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            default:
                                tmodel.setScreenNo(103);
                                String[] coverName = ussdRequest.getMsg().split(" ");
                                if (coverName.length == 2) {
                                    tmodel.setValue23(ussdRequest.getMsg());
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                    res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                    if (res.contains("success")) {
                                        String[] array = {"1. Male.", "2. Female.", "0. Back."};
                                        String[] cover = {"Spouse", "Child", "Parent", "Parent-in-law", "Sibling"};
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select Gender of your " + cover[Integer.parseInt(this.getDependantIndex(tmodel.getValue22())) - 1] + ":");
                                        System.out.println("Select Gender of your " + cover[Integer.parseInt(this.getDependantIndex(tmodel.getValue22())) - 1] + ": \n1. Male. \n2. Female. \n0. Back. ");
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);

                                    } else {
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Error connecting to server. Contact support...");
                                        System.out.println("Error connecting to server. Contact support...");
                                        uResponse2.setUSSDResp(prompt);
                                    }
                                } else {
                                    if ("two".equals(tmodel.getValue23())) {
                                        prompt.setAction("prompt");
                                        prompt.setMenus("");
                                        prompt.setTitle("Invalid Entry...");
                                        System.out.println("Invalid Entry...");
                                        uResponse2.setUSSDResp(prompt);
                                    } else {
                                        tmodel.setValue23("two");
                                        tmodel.setScreenNo(102);
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                        res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                        if (res.contains("success")) {
                                            String[] array = {"0. Go back to Main Menu."};
                                            String[] cover = {"Spouse", "Child", "Parent", "Parent-in-law", "Sibling"};
                                            ussdResponse.setAction("showMenu");
                                            ussdResponse.setTitle("Try again. Enter Full Name of your " + cover[Integer.parseInt(this.getDependantIndex(tmodel.getValue22())) - 1] + " (e.g. John Doe)");
                                            System.out.println("Try again. Enter Full Name of your " + cover[Integer.parseInt(this.getDependantIndex(tmodel.getValue22())) - 1] + " (e.g. John Doe) \n0. Go back to Main Menu.");
                                            ussdResponse.setMenus(array);
                                            uResponse.setUSSDResp(ussdResponse);
                                        } else {
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Error connecting to server. Contact support...");
                                            System.out.println("Error connecting to server. Contact support...");
                                            uResponse2.setUSSDResp(prompt);

                                        }
                                    }

                                }
                                break;
                        }
                    } else if (tmodel.getScreenNo() == 103) {
                        switch (ussdRequest.getMsg()) {
                            case "1":
                            case "2":
                                tmodel.setScreenNo(104);
                                tmodel.setValue24("1".equals(ussdRequest.getMsg()) ? "MALE" : "FEMALE");
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    String[] cover = {"Spouse", "Child", "Parent", "Parent-in-law", "Sibling"};
                                    ussdResponse.setAction("input");
                                    ussdResponse.setTitle("Enter Date of Birth of your " + cover[Integer.parseInt(this.getDependantIndex(tmodel.getValue22())) - 1] + " (Date format: dd/MM/yyyy  e.g. 24/02/1998)");
                                    System.out.println("Enter Date of Birth of your " + cover[Integer.parseInt(this.getDependantIndex(tmodel.getValue22())) - 1] + " (Date format: dd/MM/yyyy  e.g. 24/02/1998)");
                                    uResponse.setUSSDResp(ussdResponse);

                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            case "0":
                                tmodel.setScreenNo(3);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                    uResponse = this.mainMenu(url);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            default:
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Entry...");
                                System.out.println("Invalid Entry...");
                                uResponse2.setUSSDResp(prompt);
                                break;
                        }
                    } else if (tmodel.getScreenNo() == 104) {
                        try {
                            Date date = inSDF.parse(ussdRequest.getMsg());
                            String outDate = outSDF.format(date);
                            tmodel.setScreenNo(8);
                            tmodel.setValue25(outDate);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);

                            if (res.contains("success")) {
                                String[] array = {"0. Go back to Main Menu."};
                                ussdResponse.setAction("showMenu");
                                ussdResponse.setTitle("Enter Full Name of your Trustee (e.g. John Doe)");
                                System.out.println("Enter Full Name of your Trustee (e.g. John Doe) \n0. Go back to Main Menu.");
                                ussdResponse.setMenus(array);
                                uResponse.setUSSDResp(ussdResponse);

                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (ParseException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid date format...");
                            System.out.println("Invalid date format...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 8) {
                        tmodel.setScreenNo(9);
                        tmodel.setValue8(ussdRequest.getMsg());
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        if (res.contains("success")) {
                            String[] array = {"1. Male.", "2. Female.", "0. Back to Main Menu."};
                            ussdResponse.setAction("showMenu");
                            ussdResponse.setTitle("Select Gender of your Trustee:");
                            System.out.println("Select Gender of your Trustee: \n1. Male. \n2. Female. \n0. Back. ");
                            ussdResponse.setMenus(array);
                            uResponse.setUSSDResp(ussdResponse);
                        } else {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Error connecting to server. Contact support...");
                            System.out.println("Error connecting to server. Contact support...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 9) {
                        switch (ussdRequest.getMsg()) {
                            case "1":
                            case "2":
                                tmodel.setScreenNo(10);
                                tmodel.setValue9("1".equals(ussdRequest.getMsg()) ? "MALE" : "FEMALE");
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    ussdResponse.setAction("input");
                                    ussdResponse.setTitle("Enter Date of Birth of your Trustee (Date format: dd/MM/yyyy  e.g. 24/02/1998)");
                                    System.out.println("Enter Date of Birth of your Trustee (Date format: dd/MM/yyyy  e.g. 24/02/1998)");
                                    uResponse.setUSSDResp(ussdResponse);

                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            case "0":
                                tmodel.setScreenNo(3);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                    uResponse = this.mainMenu(url);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            default:
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Entry...");
                                System.out.println("Invalid Entry...");
                                uResponse2.setUSSDResp(prompt);
                                break;
                        }
                    } else if (tmodel.getScreenNo() == 10) {
                        try {
                            Date date = inSDF.parse(ussdRequest.getMsg());
                            String outDate = outSDF.format(date);
                            tmodel.setScreenNo(11);
                            tmodel.setValue10(outDate);
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);

                            if (res.contains("success")) {
                                String[] array = {"1. Spouse.", "2. Child.", "3. Parent.", "4. Parent-in-law.", "5. Sibling.", "0. Main Menu."};
                                ussdResponse.setAction("showMenu");
                                ussdResponse.setTitle("Select relation with trustee:");
                                System.out.println("Select relation with trustee \n1. Spouse. \n2. Child. \n3. Parent. \n4. Parent-in-law. \n5. Sibling. \n0. Main Menu");
                                ussdResponse.setMenus(array);
                                uResponse.setUSSDResp(ussdResponse);

                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (ParseException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid date format...");
                            System.out.println("Invalid date format...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 11) {
                        int cnt = 1;
                        StringBuilder builder = new StringBuilder();
                        tmodel.setScreenNo(12);
                        tmodel.setValue18(ussdRequest.getMsg());
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                        String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/frequency/" + tmodel.getValue7();
                        List<ProductFreqMinAmtDTO> plist = new HTTPRequestLogic().getProductFrequencies(url);
                        if (res.contains("success")) {
                            String[] array = new String[plist.size()];
                            for (ProductFreqMinAmtDTO rrmodel : plist) {
                                array[cnt - 1] = cnt + ". " + rrmodel.getProductFrequency();
                                builder.append(cnt++).append(". ").append(rrmodel.getProductFrequency()).append("\n");
                            }

                            ussdResponse.setAction("showMenu");
                            ussdResponse.setTitle("Select Frequency of Policy:");
                            System.out.println("Select Frequency of Policy: \n" + builder.toString());
                            ussdResponse.setMenus(array);
                            uResponse.setUSSDResp(ussdResponse);

                        } else {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Error connecting to server. Contact support...");
                            System.out.println("Error connecting to server. Contact support...");
                            uResponse2.setUSSDResp(prompt);
                        }

                    } else if (tmodel.getScreenNo() == 12) {
                        int cnt = 1;
                        StringBuilder builder = new StringBuilder();

                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/frequency/" + tmodel.getValue7();
                        List<ProductFreqMinAmtDTO> plist = new HTTPRequestLogic().getProductFrequencies(url);
                        try {
                            int val = Integer.parseInt(ussdRequest.getMsg());
                            if (val > 0 && val <= plist.size()) {
                                tmodel.setValue11(String.valueOf(plist.get(val - 1).getProductFrequency()));
                                if (tmodel.getValue7().equals("42") || tmodel.getValue7().equals("43") || tmodel.getValue7().equals("83")) {
                                    tmodel.setScreenNo(13);
                                } else {
                                    tmodel.setScreenNo(14);
                                }
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);

                                if (res.contains("success")) {

                                    if (tmodel.getValue7().equals("42") || tmodel.getValue7().equals("43") || tmodel.getValue7().equals("83")) {
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/terms/" + tmodel.getValue7();
                                        List<ProductTermDTO> dlist = new HTTPRequestLogic().getProductTerms(url);
                                        String[] array = new String[dlist.size()];
                                        for (ProductTermDTO rrmodel : dlist) {
                                            array[cnt - 1] = cnt + ". " + rrmodel.getTerm() + " Years";
                                            builder.append(cnt++).append(". ").append(rrmodel.getTerm()).append(" Years").append("\n");
                                        }
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select Term of Policy:");
                                        System.out.println("Select Term of Policy: \n" + builder.toString());
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    } else {
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/plans/" + tmodel.getValue7();
                                        List<ProductPlanDTO> dlist = new HTTPRequestLogic().getProductPlan(url);
                                        String[] array = new String[dlist.size()];
                                        for (ProductPlanDTO rrmodel : dlist) {
                                            array[cnt - 1] = cnt + ". " + rrmodel.getPlanName();
                                            System.out.println(rrmodel);
                                            builder.append(cnt++).append(". ").append(rrmodel.getPlanName()).append("\n");
                                        }
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select Plan:");
                                        System.out.println("Select Plan: \n" + builder.toString());
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 13) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/terms/" + tmodel.getValue7();
                        List<ProductTermDTO> plist = new HTTPRequestLogic().getProductTerms(url);
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/productinfo/" + tmodel.getValue7();
                        ProductConstantSetupDTO pcdto = new HTTPRequestLogic().getProductInfo(url);
                        try {

                            int val = Integer.parseInt(ussdRequest.getMsg());
                            if (val > 0 && val <= plist.size()) {
                                tmodel.setValue13(String.valueOf(plist.get(val - 1).getTerm()));
                                if (!"83".equals(tmodel.getValue7())) {
                                    tmodel.setScreenNo(15);
                                } else {
                                    tmodel.setScreenNo(14);
                                }
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);

                                if (res.contains("success")) {
                                    if (!"83".equals(tmodel.getValue7())) {
                                        ussdResponse.setAction("input");
                                        ussdResponse.setTitle("Enter Sum Assured (Minimum Sum Assured of GHS " + pcdto.getProductSumassured() + ")");
                                        System.out.println("Enter Sum Assured (Minimum Sum Assured of GHS " + pcdto.getProductSumassured() + ")");
                                        uResponse.setUSSDResp(ussdResponse);
                                    } else {
                                        int cnt = 1;
                                        StringBuilder builder = new StringBuilder();
                                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/plans/" + tmodel.getValue7();
                                        List<ProductPlanDTO> dlist = new HTTPRequestLogic().getProductPlan(url);
                                        String[] array = new String[dlist.size()];
                                        for (ProductPlanDTO rrmodel : dlist) {
                                            array[cnt - 1] = cnt + ". " + rrmodel.getPlanName();
                                            System.out.println(rrmodel);
                                            builder.append(cnt++).append(". ").append(rrmodel.getPlanName()).append("\n");
                                        }
                                        ussdResponse.setAction("showMenu");
                                        ussdResponse.setTitle("Select Plan:");
                                        System.out.println("Select Plan: \n" + builder.toString());
                                        ussdResponse.setMenus(array);
                                        uResponse.setUSSDResp(ussdResponse);
                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 14) {
                        int cnt = 1;
                        StringBuilder builder = new StringBuilder();
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/plans/" + tmodel.getValue7();
                        List<ProductPlanDTO> plist = new HTTPRequestLogic().getProductPlan(url);

                        try {

                            int val = Integer.parseInt(ussdRequest.getMsg());
                            if (val > 0 && val <= plist.size()) {
                                tmodel.setValue12(String.valueOf(plist.get(val - 1).getPlanid()));
                                if (tmodel.getValue7().equals("81") || tmodel.getValue7().equals("82") || tmodel.getValue7().equals("83") || tmodel.getValue7().equals("41")) {
                                    tmodel.setScreenNo(17);
                                } else if (tmodel.getValue7().equals("92")) {
                                    tmodel.setScreenNo(16);
                                }

                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);

                                if (res.contains("success")) {
                                    switch (tmodel.getValue7()) {
                                        case "81":
                                        case "82":
                                        case "83":
                                        case "41":
                                            ussdResponse.setAction("input");
                                            ussdResponse.setTitle("How much in GHC do you want to pay " + tmodel.getValue11().toLowerCase() + ". (Enter Value Only)");
                                            System.out.println("How much in GHC do you want to pay " + tmodel.getValue11().toLowerCase() + ". (Enter Value Only)");
                                            uResponse.setUSSDResp(ussdResponse);
                                            break;
                                        case "92":
                                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/riders/" + tmodel.getValue7();
                                            List<ProductRidersDTO> rlist = new HTTPRequestLogic().getProductRiders(url);
                                            String[] array = new String[rlist.size()];
                                            for (ProductRidersDTO rrmodel : rlist) {
                                                array[cnt - 1] = cnt + ". " + rrmodel.getBenefitDesc();
                                                ussdResponse.setAction("showMenu");
                                                ussdResponse.setTitle("Select Rider: ");
                                                System.out.println("Select Rider:  \n" + builder.toString());
                                                ussdResponse.setMenus(array);
                                                uResponse.setUSSDResp(ussdResponse);
                                            }
                                            break;
//                                        case "0":
//                                            break;

                                        default:
                                            prompt.setAction("prompt");
                                            prompt.setMenus("");
                                            prompt.setTitle("Invalid Entry...");
                                            System.out.println("Invalid Entry...");
                                            uResponse2.setUSSDResp(prompt);
                                            break;
                                    }
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }
                    } else if (tmodel.getScreenNo() == 15) {
                        try {
                            double val = Double.parseDouble(ussdRequest.getMsg());
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/productinfo/" + tmodel.getValue7();
                            ProductConstantSetupDTO pcdto = new HTTPRequestLogic().getProductInfo(url);
                            if (Double.parseDouble(pcdto.getProductSumassured()) <= val) {
                                tmodel.setValue14(ussdRequest.getMsg());
                                tmodel.setScreenNo(17);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    ussdResponse.setAction("input");
                                    ussdResponse.setTitle("How much in GHC do you want to pay " + tmodel.getValue11().toLowerCase() + ". (Enter Value Only)");
                                    System.out.println("How much in GHC do you want to pay " + tmodel.getValue11().toLowerCase() + ". (Enter Value Only)");
                                    uResponse.setUSSDResp(ussdResponse);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }

                    } else if (tmodel.getScreenNo() == 16) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/pdt/riders/" + tmodel.getValue7();
                        List<ProductRidersDTO> rlist = new HTTPRequestLogic().getProductRiders(url);
                        try {

                            int val = Integer.parseInt(ussdRequest.getMsg());
                            if (val > 0 && val <= rlist.size()) {
                                tmodel.setValue17(String.valueOf(rlist.get(val - 1).getRiderId()));
                                tmodel.setScreenNo(17);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    ussdResponse.setAction("input");
                                    ussdResponse.setTitle("How much in GHC do you want to pay " + tmodel.getValue11().toLowerCase() + ". (Enter Value Only)");
                                    System.out.println("How much in GHC do you want to pay " + tmodel.getValue11().toLowerCase() + ". (Enter Value Only)");
                                    uResponse.setUSSDResp(ussdResponse);

                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Input. Try again later...");
                                System.out.println("Invalid Input. Try again later...");
                                uResponse2.setUSSDResp(prompt);
                            }

                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }

                    } else if (tmodel.getScreenNo() == 17) {
                        try {
                            double val = Double.parseDouble(ussdRequest.getMsg());
                            tmodel.setScreenNo(18);
                            tmodel.setValue15(ussdRequest.getMsg());
                            url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                            String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                            if (res.contains("success")) {
                                String[] array = {"1. Auto Deduction.", "2. One-Time Deduction.", "0. Back."};
                                ussdResponse.setAction("showMenu");
                                ussdResponse.setTitle("Select Deduction Type:");
                                System.out.println("Select Deduction Type: \n1. Auto Deduction. \n2. One-Time Deduction. \n0. Back.");
                                ussdResponse.setMenus(array);
                                uResponse.setUSSDResp(ussdResponse);

                            } else {
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Error connecting to server. Contact support...");
                                System.out.println("Error connecting to server. Contact support...");
                                uResponse2.setUSSDResp(prompt);
                            }
                        } catch (NumberFormatException ex) {
                            prompt.setAction("prompt");
                            prompt.setMenus("");
                            prompt.setTitle("Invalid Input. Try again later...");
                            System.out.println("Invalid Input. Try again later...");
                            uResponse2.setUSSDResp(prompt);
                        }

                    } else if (tmodel.getScreenNo() == 18) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/productinfo/" + tmodel.getValue7();
                        ProductConstantSetupDTO pcdto = new HTTPRequestLogic().getProductInfo(url);

                        switch (ussdRequest.getMsg()) {
                            case "1":
                            case "2":
                                tmodel.setScreenNo(19);
                                tmodel.setValue16(ussdRequest.getMsg());
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    String[] array = {"1. Confirm.", "0. Back."};
                                    ussdResponse.setAction("showMenu");
                                    ussdResponse.setTitle(pcdto.getProductName() + "\nSum Assured: GHS " + tmodel.getValue14() + "\nGHS " + tmodel.getValue15() + " " + tmodel.getValue11() + "\n" + ("1".equals(ussdRequest.getMsg()) ? "Auto Deduction" : "One-Time Deduction"));
                                    System.out.println(pcdto.getProductName() + "\nSum Assured: GHS " + tmodel.getValue14() + "\nGHS " + tmodel.getValue15() + " " + tmodel.getValue11() + "\n" + ("1".equals(ussdRequest.getMsg()) ? "Auto Deduction" : "One-Time Deduction") + "\n1. Confirm. \n0. Back.");
                                    ussdResponse.setMenus(array);
                                    uResponse.setUSSDResp(ussdResponse);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            case "0":
                                tmodel.setScreenNo(3);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                    uResponse = this.mainMenu(url);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;

                            default:
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Entry...");
                                System.out.println("Invalid Entry...");
                                uResponse2.setUSSDResp(prompt);
                                break;
                        }

                    } else if (tmodel.getScreenNo() == 19) {
                        url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/productinfo/" + tmodel.getValue7();
                        ProductConstantSetupDTO pcdto = new HTTPRequestLogic().getProductInfo(url);
                        switch (ussdRequest.getMsg()) {
                            case "1":
                                if (tmodel.getValue21() != null) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/agent/" + ussdRequest.getMsisdn();
                                    ProductAgentsTbDTO pAgent = new HTTPRequestLogic().getAgentInfo(url);
                                    tmodel.setMobile("233" + tmodel.getValue21().substring(1));
                                    tmodel.setValue21(String.valueOf(pAgent.getProductAgentsTbPK().getPagtUserSysid()));
                                    
                                }
                                tmodel.setScreenNo(19);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd/register";
                                String res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Your " + pcdto.getProductName() + " T & C has been sent to you via SMS. Please wait for prompt to authorize payment of GHS " + tmodel.getValue15() + ". Thank you!");
                                    System.out.println("Your " + pcdto.getProductName() + " T & C has been sent to you via SMS. Please wait for prompt to authorize payment of GHS " + tmodel.getValue15() + ". Thank you!");
                                    uResponse2.setUSSDResp(prompt);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;
                            case "0":
                                tmodel.setScreenNo(3);
                                url = "http://localhost:8080/EazyappUssdAPIs/webresources/tmpussd";
                                res = new HTTPRequestLogic().saveUssdData(url, tmodel);
                                if (res.contains("success")) {
                                    url = "http://localhost:8080/EazyappUssdAPIs/webresources/useraccess/" + ussdRequest.getMsisdn();
                                    uResponse = this.mainMenu(url);
                                } else {
                                    prompt.setAction("prompt");
                                    prompt.setMenus("");
                                    prompt.setTitle("Error connecting to server. Contact support...");
                                    System.out.println("Error connecting to server. Contact support...");
                                    uResponse2.setUSSDResp(prompt);
                                }
                                break;

                            default:
                                prompt.setAction("prompt");
                                prompt.setMenus("");
                                prompt.setTitle("Invalid Entry...");
                                System.out.println("Invalid Entry...");
                                uResponse2.setUSSDResp(prompt);
                                break;
                        }

                    }

                    String responseJson = gton.toJson(uResponse).length() != 2 ? gton.toJson(uResponse) : gton.toJson(uResponse2);
                    out.print(responseJson);
                }
            } catch (Exception e) {
                prompt.setMenus("");
                prompt.setTitle("An error has occurred. contact support...");
                prompt.setAction("prompt");
                uResponse2.setUSSDResp(prompt);

                String responseJson = gton.toJson(uResponse2);
                out.print(responseJson);
                e.printStackTrace(System.out);
            }

        } catch (Exception ex) {
            String responseJson = gton.toJson(uResponse);
            out.print(responseJson);
            ex.printStackTrace(System.out);

        }
    }

    private UssdResponse mainMenu(String url) throws IOException {
        USSDResps ussdResponse = new USSDResps();
        UssdResponse uResponse = new UssdResponse();

        AccessTypeDTO accesstype = new HTTPRequestLogic().getAccessType(url);
        if (accesstype.getCusttype().equals("AGT") || accesstype.getCusttype().equals("NON")) {
            String[] array = {"1. Register."};
            ussdResponse.setAction("showMenu");
            ussdResponse.setTitle("Welcome to Allianz Life.");
            System.out.println("Welcome to Allianz Life. \n1. Register.");
            ussdResponse.setMenus(array);
            uResponse.setUSSDResp(ussdResponse);
        } else if (accesstype.getCusttype().equals("CAG") || accesstype.getCusttype().equals("CUS")) {
            String[] array = {"1. Register.", "2. Pay Premium.", "3. Stop Auto Deduct.", "4. Claims.", "5. Update Records.", "6. Check Balance.", "7. T & C.", "8. Change Pin.", "9. Helpline."};
            ussdResponse.setAction("showMenu");
            ussdResponse.setTitle("Welcome to Allianz Life.");
            System.out.println("Welcome to Allianz Life. \n1. Register. \n2. Pay Premium. \n3. Stop Auto Deduct. \n4. Claims. \n5. Update Records. \n6. Check Balance. \n7. T & C. \n8. Change Pin. \n9. Helpline. ");
            ussdResponse.setMenus(array);
            uResponse.setUSSDResp(ussdResponse);
        }
        return uResponse;
    }
    
    public String getDependantIndex(String dependantType) {
        Map<String, String> map = new HashMap<>();
        map.put("Spouse", "1");
        map.put("Child", "2");
        map.put("Parent", "3");
        map.put("Parent-in-law", "4");
        map.put("Sibling", "5");
        
        return  map.get(dependantType);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

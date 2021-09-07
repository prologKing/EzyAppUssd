/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allianz.logic;

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
import com.allianz.dto.SSNITModel;
import com.allianz.dto.TmpUssdDataDTO;
import com.allianz.dto.VotersModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Developer
 */
public class HTTPRequestLogic {
    
     Gson gson = new Gson();
     private static final String gvivebaseurl = "https://gvivegh.com:1355/gvivewar";
    private static final String httpMethod = "GET";
    
    public AccessTypeDTO getAccessType(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        String result = String.valueOf(resp);
        AccessTypeDTO dmodel = gson.fromJson(result, AccessTypeDTO.class);
        return dmodel;
    }
    
    public ProductAgentsTbDTO getAgentInfo(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        String result = String.valueOf(resp);
        ProductAgentsTbDTO dmodel = gson.fromJson(result, ProductAgentsTbDTO.class);
        return dmodel;
    }
    
    public ProductTbDTO getProductTbInfo(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        String result = String.valueOf(resp);
        ProductTbDTO dmodel = gson.fromJson(result, ProductTbDTO.class);
        return dmodel;
    }
    
    public CustomerTbDTO getCustomerInfo(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        String result = String.valueOf(resp);
        CustomerTbDTO dmodel = gson.fromJson(result, CustomerTbDTO.class);
        return dmodel;
    }
    
    public TmpUssdDataDTO getTmpUssdData(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        String result = String.valueOf(resp);
        TmpUssdDataDTO dmodel = gson.fromJson(result, TmpUssdDataDTO.class);
        return dmodel;
    }
    
    public String saveUssdData(String ur, TmpUssdDataDTO ussddata) throws IOException {

        URL url = new URL(ur);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String postJsonData = ussddata.toString();

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postJsonData);
        wr.flush();
        wr.close();
        System.out.println(con);
        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + postJsonData);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        String res = String.valueOf(response);
        return res;
    }
    
    public List<ProductConstantSetupDTO> getUnsubscribedProducts(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }

        String result = String.valueOf(resp);
        List<ProductConstantSetupDTO> clist = gson.fromJson(result, new TypeToken<List<ProductConstantSetupDTO>>() {
        }.getType());
        return clist;
    }
    
    public List<ProductFreqMinAmtDTO> getProductFrequencies(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }

        String result = String.valueOf(resp);
        List<ProductFreqMinAmtDTO> clist = gson.fromJson(result, new TypeToken<List<ProductFreqMinAmtDTO>>() {
        }.getType());
        return clist;
    }

    public List<ProductPlanDTO> getProductPlan(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }

        String result = String.valueOf(resp);
        List<ProductPlanDTO> clist = gson.fromJson(result, new TypeToken<List<ProductPlanDTO>>() {
        }.getType());
        return clist;
    }
    
    public List<ProductRidersDTO> getProductRiders(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }

        String result = String.valueOf(resp);
        List<ProductRidersDTO> clist = gson.fromJson(result, new TypeToken<List<ProductRidersDTO>>() {
        }.getType());
        return clist;
    }
    
    public List<ProductTermDTO> getProductTerms(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }

        String result = String.valueOf(resp);
        List<ProductTermDTO> clist = gson.fromJson(result, new TypeToken<List<ProductTermDTO>>() {
        }.getType());
        return clist;
    }
    
     public ProductConstantSetupDTO getProductInfo(String url) throws MalformedURLException, IOException {
        URL ul = new URL(url);
        HttpURLConnection re = (HttpURLConnection) ul.openConnection();
        re.connect();
        int status = re.getResponseCode();
        System.out.println("Status Code: " + status);
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(re.getInputStream()));
        StringBuffer resp = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        String result = String.valueOf(resp);
        ProductConstantSetupDTO dmodel = gson.fromJson(result, ProductConstantSetupDTO.class);
        return dmodel;
    }
    
    public static String EncodedUrl(String completeUrl) throws UnsupportedEncodingException {

        String quoted = URLEncoder.encode(completeUrl, "UTF-8").toLowerCase();

        return httpMethod + quoted;
    }

    public static String GenerateBase64Digest(String url) throws Exception {

        Mac rhmac = Mac.getInstance("HmacSHA256");
        rhmac.init(
                new SecretKeySpec("flL8bNaf9hSXXDAff0q/B/L4/pxtAtLejWwrTuvOVXQ=".getBytes(StandardCharsets.UTF_8), "HmacSHA256")
        );
        byte[] digest = java.util.Base64.getEncoder().encode(
                rhmac.doFinal(
                        EncodedUrl(url).getBytes(StandardCharsets.UTF_8)
                )
        );
        System.out.println(Arrays.toString(digest));
        return new String(digest);
    }

    public static String GenerateAuthToken(String url) throws Exception {

        String result = "GVIVE_BSWAPI" + ":" + GenerateBase64Digest(url);
        System.out.println("Token: " + result);

        byte[] token = java.util.Base64.getEncoder().encode(result.getBytes(StandardCharsets.UTF_8));

        return new String(token);
    }

    public PassportModel FindPassportIdentification(String pid, boolean incp, boolean incs) throws Exception {
        // incp = Boolean.TRUE; incs = Boolean.TRUE;
        String url = gvivebaseurl + "/passport" + "?pid=" + pid;
        if (incp) {
            url += "&incp=" + incp;
        } else {
            url += "&incp=" + incp;
        }

        if (incs) {
            url += "&incs=" + incs;
        } else {
            url += "&incs=" + incs;
        }

        String token = GenerateAuthToken(url);

        PassportModel fd = ApiFlightCallPassport(token, url);
        return fd;
    }

    public DriverLicenseModel FindDriverLicense(String coc, String dob, Boolean incp) throws Exception {
        // incp = Boolean.TRUE; incs = Boolean.TRUE;
        // fname = "Steve Junior";
        String url = gvivebaseurl + "/driverlicence" + "?coc=" + coc + "&dob=" + dob;
        // System.out.println(url);
        if (incp) {
            url += "&incp=" + incp;
        } else {
            url += "&incp=" + incp;
        }

        System.out.println(url);
        // Generate Token and call api
        String token = GenerateAuthToken(url);
        DriverLicenseModel Dr = ApiFlightCallLicense(token, url);
        return Dr;
    }

    public SSNITModel FindSSNITIdentification(String fssno, boolean incp) throws Exception {
        String url = gvivebaseurl + "/ssnit" + "?fssno=" + fssno;
        if (incp) {
            url += "&incp=" + incp;
        } else {
            url += "&incp=" + incp;
        }

        String token = GenerateAuthToken(url);
        SSNITModel sn = ApiFlightCallSSNIT(token, url);
        return sn;
    }

    public VotersModel FindVoterIdentification(String vid, Boolean incp) throws Exception {
        String url = gvivebaseurl + "/voter" + "?vid=" + vid;
        if (incp) {
            url += "&incp=" + incp;
        } else {
            url += "&incp=" + incp;
        }
//        System.out.println("Actual url : " + url);
        String token = GenerateAuthToken(url);
        VotersModel vs = ApiFlightCallVoter(token, url);
        return vs;
    }

    public SSNITModel ApiFlightCallSSNIT(String token, String urlLink) throws Exception {
        System.out.println(urlLink);
        System.out.println(token);
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(urlLink)).openConnection();

        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "hmac " + token);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        httpURLConnection.disconnect();

        String result = String.valueOf(response);
        SSNITModel dmodel = gson.fromJson(result, SSNITModel.class);
        return dmodel;
    }

    public PassportModel ApiFlightCallPassport(String token, String urlLink) throws Exception {
        System.out.println(urlLink);
        System.out.println(token);
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(urlLink)).openConnection();

        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "hmac " + token);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        httpURLConnection.disconnect();

        String result = String.valueOf(response);
        PassportModel dmodel = gson.fromJson(result, PassportModel.class);
        return dmodel;
    }

    public DriverLicenseModel ApiFlightCallLicense(String token, String urlLink) throws Exception {
        System.out.println(urlLink);
        System.out.println(token);
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(urlLink)).openConnection();

        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "hmac " + token);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        httpURLConnection.disconnect();

        String result = String.valueOf(response);
        DriverLicenseModel dmodel = gson.fromJson(result, DriverLicenseModel.class);
        return dmodel;
    }

    public VotersModel ApiFlightCallVoter(String token, String urlLink) throws Exception {
        System.out.println(urlLink);
        System.out.println(token);
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(urlLink)).openConnection();

        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "hmac " + token);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        httpURLConnection.disconnect();

        String result = String.valueOf(response);
        VotersModel dmodel = gson.fromJson(result, VotersModel.class);
        return dmodel;
    }
}

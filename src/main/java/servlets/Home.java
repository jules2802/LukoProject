package servlets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@WebServlet("")
public class Home extends  CreateTemplateEngine {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //First page launch
        WebContext context = new WebContext(req,resp, req.getServletContext());
        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("Home", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        //Get data from form
        String longitude= req.getParameter("longitude");
        String latitude= req.getParameter("latitude");

        //Launched first request to get insee code
        String urlINDEE="http://www.georisques.gouv.fr/webappReport/ws/insee/commune/_search?lon="+longitude+"&lat="
        +latitude;

        URL obj = new URL(urlINDEE);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "CHROME");

        //check code response
        int responseCode = con.getResponseCode();
        System.out.println(responseCode);

        //read stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String codeInsee="",commune="";

        JSONParser parse = new JSONParser();

       try{
           //Read JSON response and and get codeInse and place name
            Object json = parse.parse(response.toString());
            JSONObject jsonObject = (JSONObject) json;
            codeInsee= (String) jsonObject.get("codeInsee");
            commune= (String) jsonObject.get("libelleCommune");
            commune=commune.toLowerCase();


        }catch(Exception ex){
            ex.printStackTrace();
           }


        //send request to get risks linked to insee code
        String urlRisks="http://www.georisques.gouv.fr/webappReport/ws/gasparv2/risqueDetail/"+codeInsee;
        URL risks = new URL(urlRisks);
        HttpURLConnection riskConnection = (HttpURLConnection) risks.openConnection();
        riskConnection.setRequestMethod("GET");
        riskConnection.setRequestProperty("User-Agent", "CHROME");
        int responseCodeRisks = riskConnection.getResponseCode();

        BufferedReader risksBuffer = new BufferedReader(
                new InputStreamReader(riskConnection.getInputStream()));
        String inputLineRisks;

        StringBuffer responseRisks = new StringBuffer();
        while ((inputLineRisks = risksBuffer.readLine()) != null) {
            responseRisks.append(inputLineRisks);
        }
        risksBuffer.close();

        //List of risks
        ArrayList<String> risksList= new ArrayList<>();
    try{
        //Read JSON response and and get all risks
        Object jsonRisks = parse.parse(responseRisks.toString());
        JSONArray jsonRisksObject = (JSONArray) jsonRisks;

        for (Object a : jsonRisksObject) {
            if (a instanceof JSONObject) {
                Object element = ((JSONObject) a).get("libRisqueLong");
                risksList.add(element.toString());
}
        }


    }catch(Exception ex){
        ex.printStackTrace();
    }

    //Send parameters in session
        req.getSession().setAttribute("longitude",longitude);
        req.getSession().setAttribute("latitude",latitude);
        req.getSession().setAttribute("codeInsee",codeInsee);
        req.getSession().setAttribute("commune",commune);
        req.getSession().setAttribute("risksList",risksList);
        resp.sendRedirect("/results");

}

}

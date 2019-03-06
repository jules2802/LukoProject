package servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.unbescape.html.HtmlEscape.escapeHtml5;


@WebServlet("/results")
public class ResultsServlet extends CreateTemplateEngine {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //Launch page
        WebContext context = new WebContext(req, resp, req.getServletContext());

        //List with accents
        ArrayList<String> listRisks = (ArrayList<String>) req.getSession().getAttribute("risksList");

        //List depends of risk type
        ArrayList<String> floodRisks = new ArrayList<>();
        ArrayList<String> industrialRisks = new ArrayList<>();
        ArrayList<String> earthRisks = new ArrayList<>();
        ArrayList<String> pollutionRisks = new ArrayList<>();

        //how risky is the risk
        ArrayList<String> floodRisksNumber = new ArrayList<>();
        ArrayList<String> industrialRisksNumber = new ArrayList<>();
        ArrayList<String> earthRisksNumber = new ArrayList<>();
        ArrayList<String> pollutionRisksNumber = new ArrayList<>();

        //total risk
        int floodTotal=0;
        int earthTotal=0;
        int industrialTotal=0;
        int pollutionTotal=0;


        for (int i = 0; i < listRisks.size(); i++) {
            String risk = listRisks.get(i);


            if (risk.contains("Inondation") || risk.contains("météorologiques")) {
                //add risk to good category
                floodRisks.add(escapeHtml5(risk));

                //estimate how risky
                if (risk.contains("Inondation")){
                    floodRisksNumber.add("8");
                    floodTotal+= 8;
                }else{
                    floodRisksNumber.add("5");
                    floodTotal+= 5;
                }


            } else if (risk.contains("industriel") || risk.contains("industriels") || risk.contains("marchandises")) {
                industrialRisks.add(escapeHtml5(risk));

                if(risk.contains("industriel") || risk.contains("industriels")){
                    industrialRisksNumber.add("3");
                    industrialTotal+= 3;
                }else{
                    industrialRisksNumber.add("1");
                    industrialTotal+= 1;
                }

            } else if (risk.contains("Séisme") || risk.contains("terrain") || risk.contains("terrains") || risk.contains("Mouvement")) {
                earthRisks.add(escapeHtml5(risk));

                if(risk.contains("Séisme")){
                    earthRisksNumber.add("6");
                    earthTotal+= 6;
                }else{
                    earthRisksNumber.add("7");
                    earthTotal+= 7;
                }

            } else if (risk.contains("radon") || risk.contains("pollution")) {
                pollutionRisks.add(escapeHtml5(risk));

                if(risk.contains("radon")){
                    pollutionRisksNumber.add("4");
                    pollutionTotal+= 4;
                }else{
                    pollutionRisksNumber.add("5");
                    pollutionTotal+= 5;
                }
            }

        }
           if(floodRisks.size()==0 || floodRisksNumber.size()==0){
                    floodRisks.add("Lucky you! Ton adresse n'a pas de risques d'innondations");
                    floodRisksNumber.add("0");}
           if( pollutionRisks.size()==0 || pollutionRisksNumber.size()==0){
                    pollutionRisks.add("Lucky you! Ton adresse n'a pas de risques de pollutions");
                    pollutionRisksNumber.add("0");}
           if(earthRisks.size()==0 || earthRisksNumber.size()==0){
                    earthRisks.add("Lucky you! Ton adresse n'a pas de risques de séismes ou mouvements de terrains");
                    earthRisksNumber.add("0");}
           if (industrialRisks.size()==0 || industrialRisksNumber.size()==0){
                    industrialRisks.add("Lucky you! Ton adresse n'a pas de risques industriels");
                    industrialRisksNumber.add("0");
           }



        //set variable in context to get it in html File
        context.setVariable("floodRisks", floodRisks);
        context.setVariable("earthRisks", earthRisks);
        context.setVariable("pollutionRisks", pollutionRisks);
        context.setVariable("industrialRisks", industrialRisks);

        context.setVariable("floodRisksNumber",floodRisksNumber);
        context.setVariable("earthRisksNumber",earthRisksNumber);
        context.setVariable("industrialRisksNumber",industrialRisksNumber);
        context.setVariable("pollutionRisksNumber",pollutionRisksNumber);

        context.setVariable("floodTotal",floodTotal);
        context.setVariable("earthTotal",earthTotal);
        context.setVariable("industrialTotal",industrialTotal);
        context.setVariable("pollutionTotal",pollutionTotal);

        context.setVariable("longitude",req.getSession().getAttribute("longitude"));
        context.setVariable("latitude",req.getSession().getAttribute("latitude"));
        context.setVariable("codeInsee", req.getSession().getAttribute("codeInsee"));
        context.setVariable("commune", req.getSession().getAttribute("commune"));


        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("Results", context, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }
}

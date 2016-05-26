package com.bsu.famcs.wscan.reporting;

import com.bsu.famcs.wscan.model.SiteData;
import com.bsu.famcs.wscan.model.SiteDataTreatment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class XMLReporting {
    private static Logger logger = Logger.getLogger(XMLReporting.class);

    @Value("${REPORT_PATH}")
    private String reportPath;

    public boolean exportFullReport(byte[] xmlReport,String reportName){
        try(FileOutputStream stream = new FileOutputStream(reportPath + reportName+".xml")) {
            stream.write(xmlReport);
        } catch (FileNotFoundException e) {
            logger.error("Not found file to export report ",e);
            return false;
        } catch (IOException e) {
            logger.error("export error  ",e);
            return false;
        }
        return true;
    }

    public boolean exportMainStatistic(SiteDataTreatment siteDataTreatment){
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Map<String,SiteData> mapSiteData = siteDataTreatment.getMapSiteData();

            Set<String> sites = mapSiteData.keySet();

            Map<String, List<Element>> siteTagsWithCategory = new HashMap<>();

            sites.forEach(site -> {

                SiteData siteData = mapSiteData.get(site);
                String category = siteData.getSiteCategory();

                List<Element> listCategoryTagData = siteTagsWithCategory.get(category);
                if(listCategoryTagData == null){
                    listCategoryTagData = new ArrayList<>();
                    siteTagsWithCategory.put(category,listCategoryTagData);
                }
                Element siteDataTag = doc.createElement("siteLink");
                siteDataTag.appendChild(doc.createTextNode(siteData.getSiteLink()));

                Element lowRiskTag = doc.createElement("lowRisk");
                lowRiskTag.appendChild(doc.createTextNode(String.valueOf(siteData.getLowRiskCount())));
                siteDataTag.appendChild(lowRiskTag);

                Element mediumRisk = doc.createElement("mediumRisk");
                mediumRisk.appendChild(doc.createTextNode(String.valueOf(siteData.getMediumRiskCount())));
                siteDataTag.appendChild(mediumRisk);

                Element highRiskTag = doc.createElement("highRisk");
                highRiskTag.appendChild(doc.createTextNode(String.valueOf(siteData.getHighRiskCount())));
                siteDataTag.appendChild(highRiskTag);

                listCategoryTagData.add(siteDataTag);
            });
            Element rootElement = doc.createElement("Sites");

            Set<String> categories = siteTagsWithCategory.keySet();

            categories.forEach(category ->{
                Element categoryTag = doc.createElement(category);
                siteTagsWithCategory.get(category).forEach(element -> {
                    categoryTag.appendChild(element);
                });
                rootElement.appendChild(categoryTag);

            });

            doc.appendChild(rootElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                    reportPath + siteDataTreatment.getReportingOutputGenName()+"stat"+".xml");


            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {
            logger.error("export error  ", e);
            return false;
        } catch (TransformerException e) {
            logger.error("export error  ",e);
            return false;
        }

        return true;
    }
}

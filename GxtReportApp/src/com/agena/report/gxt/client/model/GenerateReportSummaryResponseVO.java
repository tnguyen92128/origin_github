package com.agena.report.gxt.client.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class GenerateReportSummaryResponseVO 
{
	private String reportId;
	
	public GenerateReportSummaryResponseVO(String xmlData)
	{
	    final Document doc = XMLParser.parse(xmlData);
	    reportId = doc.getElementsByTagName("reportId").item(0).getFirstChild().getNodeValue();    		
	}	
	
	public String getReportId() { return reportId; }
}

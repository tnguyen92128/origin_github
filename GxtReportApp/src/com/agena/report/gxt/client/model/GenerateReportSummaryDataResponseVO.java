package com.agena.report.gxt.client.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class GenerateReportSummaryDataResponseVO 
{
	private String reportDataFile;
	
	public GenerateReportSummaryDataResponseVO(String xmlData)
	{
	    final Document doc = XMLParser.parse(xmlData);
	    reportDataFile = doc.getElementsByTagName("reportDataFile").item(0).getFirstChild().getNodeValue();    		
	}
	
	public String getReportDataFile() { return reportDataFile; }
}

package com.agena.report.gxt.client.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class RetrieveReportSummaryResponseVO 
{
	private String dataFile;
	
	public RetrieveReportSummaryResponseVO(String xmlData)
	{
	    final Document doc = XMLParser.parse(xmlData);
	    dataFile = doc.getElementsByTagName("dataFile").item(0).getFirstChild().getNodeValue();    
	}
	
	public String getDataFile() { return dataFile; }	
}

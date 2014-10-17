package com.agena.report.gxt.client.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class GenerateReportSummaryDataRequestVO 
{
	private String reportType;
	private String dataFile;
	
	public GenerateReportSummaryDataRequestVO(String reportType, String dataFile)
	{
		this.reportType = reportType;
		this.dataFile = dataFile;
	}
	
	public String toXML()
	{
	    final Document doc = XMLParser.createDocument();
	    final Element root = doc.createElement("GenerateReportSummaryDataRequest");
	    doc.appendChild(root);

	    Element node = doc.createElement("reportType");
	    node.appendChild(doc.createTextNode(reportType));
	    root.appendChild(node);

	    node = doc.createElement("dataFile");
	    node.appendChild(doc.createTextNode(dataFile));
	    root.appendChild(node);	        
	    
	    return doc.toString();		
	}
}

package com.agena.report.gxt.client.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class GenerateReportSummaryRequestVO 
{
	private String reportType;
	private String dataFile;
	
	public GenerateReportSummaryRequestVO(String reportType, String dataFile)
	{
		this.reportType = reportType;
		this.dataFile = dataFile;
	}
	
	public String toXML()
	{
	    final Document doc = XMLParser.createDocument();
	    final Element root = doc.createElement("SendGenerateReportRequest");
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

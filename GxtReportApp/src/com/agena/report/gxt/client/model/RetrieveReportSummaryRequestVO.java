package com.agena.report.gxt.client.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class RetrieveReportSummaryRequestVO 
{
	private String reportType;	
	private String customerId;
	private String projectId;
	private String plateId;
	private String experimentId;
	private String chipId;
	
	public RetrieveReportSummaryRequestVO(String reportType, String customerId, String projectId, String plateId, String experimentId, String chipId)
	{
		this.reportType = reportType;
		this.customerId = customerId;
		this.projectId = projectId;
		this.plateId = plateId;
		this.experimentId = experimentId;
		this.chipId = chipId;
	}
	
	public String toXML()
	{
	    final Document doc = XMLParser.createDocument();
	    final Element root = doc.createElement("RetrieveReportSummaryRequest");
	    doc.appendChild(root);

	    Element node = doc.createElement("reportType");
	    node.appendChild(doc.createTextNode(reportType));
	    root.appendChild(node);

	    node = doc.createElement("customerId");
	    node.appendChild(doc.createTextNode(customerId));
	    root.appendChild(node);	    

	    node = doc.createElement("projectId");
	    node.appendChild(doc.createTextNode(projectId));
	    root.appendChild(node);	    
	    
	    node = doc.createElement("plateId");
	    node.appendChild(doc.createTextNode(plateId));
	    root.appendChild(node);	 	    

	    node = doc.createElement("experimentId");
	    node.appendChild(doc.createTextNode(experimentId));
	    root.appendChild(node);	 	    

	    node = doc.createElement("chipId");
	    node.appendChild(doc.createTextNode(chipId));
	    root.appendChild(node);	    
	    
	    return doc.toString();
	}
}

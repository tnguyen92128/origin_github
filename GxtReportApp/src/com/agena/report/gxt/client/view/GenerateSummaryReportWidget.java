package com.agena.report.gxt.client.view;

import static com.agena.report.gxt.client.model.Constants.DATA_SERVICE_URL;
import static com.agena.report.gxt.client.util.DisplayUtil.displayError;

import com.agena.report.gxt.client.model.GenerateReportSummaryDataRequestVO;
import com.agena.report.gxt.client.model.GenerateReportSummaryDataResponseVO;
import com.agena.report.gxt.client.model.GenerateReportSummaryRequestVO;
import com.agena.report.gxt.client.model.GenerateReportSummaryResponseVO;
import com.agena.report.gxt.client.model.RetrieveReportSummaryRequestVO;
import com.agena.report.gxt.client.model.RetrieveReportSummaryResponseVO;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GenerateSummaryReportWidget implements IsWidget
{
	private String title;	
	private String reportType;
	
	private ContentPanel mainPanel;
	
	private TextField txtFieldCustomerId;
	private TextField txtFieldProjectId;
	private TextField txtFieldPlateId;
	private TextField txtFieldExperimentId;	
	private TextField txtFieldChipId;
	private TextField txtFieldStatus;
	
	public GenerateSummaryReportWidget(String title, String reportType) 
	{ 
		this.title = title;
		this.reportType = reportType;
	}	
	
	public Widget asWidget() 
	{
		if (mainPanel == null)
		{
			mainPanel = new ContentPanel();
			
			mainPanel = new ContentPanel();			
			mainPanel.setBodyBorder(false);
			mainPanel.setBorders(false);			
			mainPanel.setPixelSize(800, 350);				
			mainPanel.setHeadingText(title);

			final BorderLayoutContainer layout = new BorderLayoutContainer();
			mainPanel.add(layout);
			
			// Layout - Center
			final ContentPanel centerPanel = new ContentPanel();
			centerPanel.setHeadingText("Request");
			BorderLayoutData centerDataLayout = new BorderLayoutData(150);
			centerDataLayout.setMargins(new Margins(10, 10, 10, 10));
			centerPanel.setLayoutData(centerDataLayout);
			layout.setCenterWidget(centerPanel);
			
			final VerticalLayoutContainer centerLayout = new VerticalLayoutContainer();
			centerLayout.setBorders(false);
			centerLayout.getElement().getStyle().setBackgroundColor("white");
			centerPanel.add(centerLayout);				    
		    
			// 2. customer ID
		    txtFieldCustomerId = new TextField(); 
		    txtFieldCustomerId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldCustomerId, "Customer ID"), new VerticalLayoutData(1, -1));

			// 3. project ID
			txtFieldProjectId = new TextField(); 
			txtFieldProjectId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldProjectId, "Project ID"), new VerticalLayoutData(1, -1));
				
			// 4. plate ID
			txtFieldPlateId = new TextField(); 
			txtFieldPlateId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldPlateId, "Plate ID"), new VerticalLayoutData(1, -1));		
			
			// 5. experiment ID
			txtFieldExperimentId = new TextField(); 
			txtFieldExperimentId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldExperimentId, "Experiment ID"), new VerticalLayoutData(1, -1));			
			
			// 6. chip ID
			txtFieldChipId = new TextField(); 
			txtFieldChipId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldChipId, "Chip ID"), new VerticalLayoutData(1, -1));			
			
			// Layout - South
			ContentPanel southPanel = new ContentPanel();
			southPanel.setHeadingText("Response");
			BorderLayoutData southDataLayout = new BorderLayoutData(150);
			southDataLayout.setMargins(new Margins(5, 5, 5, 5));
			centerPanel.setLayoutData(southDataLayout);		
			layout.setSouthWidget(southPanel);

			final VerticalLayoutContainer southLayout = new VerticalLayoutContainer();
			
			txtFieldStatus = new TextField(); 
			southLayout.add(new FieldLabel(txtFieldStatus, "Status"), new VerticalLayoutData(1, -1));
			southPanel.add(southLayout);		
			
			final ButtonBar buttonBar = new ButtonBar();
			buttonBar.setMinButtonWidth(75);
			buttonBar.getElement().setMargins(10);			
			
			buttonBar.add(new TextButton("Submit", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) 
				{
					processSubmit();
				}
			}));

			buttonBar.add(new TextButton("Reset", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) 
				{
					reset();
				}
			}));		
							
			centerLayout.add(buttonBar, new VerticalLayoutData(1, -1));
			
			// test code
			getTestParameters();
		}
		return mainPanel; 		
	}
	
	private void processSubmit()
	{
		// 1. retrieve data
		final RetrieveReportSummaryRequestVO requestVO = new RetrieveReportSummaryRequestVO(reportType, txtFieldCustomerId.getText(), txtFieldProjectId.getText(), txtFieldPlateId.getText(), txtFieldExperimentId.getText(), txtFieldChipId.getText());
		final String url = DATA_SERVICE_URL + "/retrieveData"; 		
		sendRetrieveDataRequest(requestVO.toXML(), url);			
	}	
	
	private void sendRetrieveDataRequest(String requestData, String url)
	{
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);  
		try 
		{   
			txtFieldStatus.setText("Sent request to " + url);
			 
			builder.setHeader("Content-Type", "application/xml");    
			builder.sendRequest(requestData, new RequestCallback() 
			{       
				@Override  
				public void onResponseReceived(Request request, Response response) 
				{    
					if (200 == response.getStatusCode()) 	
					{   
						final String xmlResponse = response.getText();						
						final RetrieveReportSummaryResponseVO reponseVO = new RetrieveReportSummaryResponseVO(xmlResponse);
						txtFieldStatus.setText(reponseVO.getDataFile());    
						
						// generate data
						final GenerateReportSummaryDataRequestVO requestVO = new GenerateReportSummaryDataRequestVO(reportType, reponseVO.getDataFile());
						final String url = DATA_SERVICE_URL + "/generateData/summary/";						
						sendGenerateDataRequest(requestVO.toXML(), url);
					} 
					else 
					{   
		              	displayError("Send Request", "Failed to send request: " + response.getStatusText());		              	
					}                           
                }   
                   
                @Override  
                public void onError(Request request, Throwable exception) 
                {     
	              	displayError("Send Request", "Failed to send request: " + exception.getMessage());	              	
                }   
            });   
        } catch (Exception e) {   e.printStackTrace();    }   
	}
	
	private void sendGenerateDataRequest(String requestData, String url)
	{
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);  
		try 
		{   
			txtFieldStatus.setText("Sent request to " + url);
			 
			builder.setHeader("Content-Type", "application/xml");    
			builder.sendRequest(requestData, new RequestCallback() 
			{       
				@Override  
				public void onResponseReceived(Request request, Response response) 
				{    
					if (200 == response.getStatusCode()) 	
					{   
						final String xmlResponse = response.getText();						
						final GenerateReportSummaryDataResponseVO reponseVO = new GenerateReportSummaryDataResponseVO(xmlResponse);
						txtFieldStatus.setText(reponseVO.getReportDataFile());   
						
						// generate report
						final GenerateReportSummaryRequestVO requestVO = new GenerateReportSummaryRequestVO(reportType, reponseVO.getReportDataFile());
						final String url = DATA_SERVICE_URL + "/sendGenerateRequest";						
						sendGeneratereportRequest(requestVO.toXML(), url);						
					} 
					else 
					{   
		              	displayError("Send Request", "Failed to send request: " + response.getStatusText());	 		              	
					}                           
                }   
                   
                @Override  
                public void onError(Request request, Throwable exception) 
                {   
	              	displayError("Send Request", "Failed to send request: " + exception.getMessage());	                 	
                }   
            });   
        } catch (Exception e) {   e.printStackTrace();    }   
	}
	
	private void sendGeneratereportRequest(String requestData, String url)
	{
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);  
		try 
		{   
			txtFieldStatus.setText("Sent request to " + url);
			 
			builder.setHeader("Content-Type", "application/xml");    
			builder.sendRequest(requestData, new RequestCallback() 
			{       
				@Override  
				public void onResponseReceived(Request request, Response response) 
				{    
					if (200 == response.getStatusCode()) 	
					{   
						final String xmlResponse = response.getText();						
						final GenerateReportSummaryResponseVO reponseVO = new GenerateReportSummaryResponseVO(xmlResponse);
						txtFieldStatus.setText(reponseVO.getReportId()); 
						
						// display pdf file
						final String urlReportFile = "http://localhost:8080/report/get/" + reponseVO.getReportId();
						Window.open(urlReportFile, "_blank", "");
					} 
					else 
					{   
		              	displayError("Send Request", "Failed to send request: " + response.getStatusText());			              	
					}                           
                }   
                   
                @Override  
                public void onError(Request request, Throwable exception) 
                {   
	              	displayError("Send Request", "Failed to send request: " + exception.getMessage());	                	
                }   
            });   
        } catch (Exception e) {   e.printStackTrace();    }   
	}			
	
	public void setWidgetTitle(String title) { mainPanel.setHeadingText(title); }
	public void reset() 
	{ 
		txtFieldCustomerId.clear();
		txtFieldProjectId.clear();
		txtFieldPlateId.clear();
		txtFieldExperimentId.clear();
		txtFieldChipId.clear();
		txtFieldStatus.clear();
		
		// test code
		getTestParameters();
	}
	
	private void getTestParameters()
	{
		txtFieldCustomerId.setText("ADMET");
		txtFieldProjectId.setText("ADME_Beta");
		txtFieldPlateId.setText("ADMEBeta_MHI_MM");
		txtFieldExperimentId.setText("06212011");	
		txtFieldChipId.setText("1");
	}
	
}
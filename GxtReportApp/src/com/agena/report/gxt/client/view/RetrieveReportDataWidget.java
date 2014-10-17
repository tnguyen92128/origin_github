package com.agena.report.gxt.client.view;

import static com.agena.report.gxt.client.model.Constants.DATA_SERVICE_URL;
import static com.agena.report.gxt.client.util.DisplayUtil.displayError;

import com.agena.report.gxt.client.model.RetrieveReportSummaryRequestVO;
import com.agena.report.gxt.client.model.RetrieveReportSummaryResponseVO;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
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

public class RetrieveReportDataWidget implements IsWidget
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
	
	public RetrieveReportDataWidget(String title, String reportType) 
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
		    
			// 1. customer ID
		    txtFieldCustomerId = new TextField(); 
		    txtFieldCustomerId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldCustomerId, "Customer ID"), new VerticalLayoutData(1, -1));

			// 2. project ID
			txtFieldProjectId = new TextField(); 
			txtFieldProjectId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldProjectId, "Project ID"), new VerticalLayoutData(1, -1));
				
			// 3. plate ID
			txtFieldPlateId = new TextField(); 
			txtFieldPlateId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldPlateId, "Plate ID"), new VerticalLayoutData(1, -1));		
			
			// 4. experiment ID
			txtFieldExperimentId = new TextField(); 
			txtFieldExperimentId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldExperimentId, "Experiment ID"), new VerticalLayoutData(1, -1));			
			
			// 5. chip ID
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
			southLayout.add(new FieldLabel(txtFieldStatus, "Response"), new VerticalLayoutData(1, -1));
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
				public void onSelect(SelectEvent event) { reset(); }
			}));	
		
			centerLayout.add(buttonBar, new VerticalLayoutData(1, -1));				
		}
		
		return mainPanel; 
	}
	
	public Widget asWidget_SAVE() 
	{
		if (mainPanel == null)
		{
			mainPanel = new ContentPanel();
			
			mainPanel = new ContentPanel();			
			mainPanel.setBodyBorder(false);
			mainPanel.setBorders(false);			
			mainPanel.setPixelSize(400, 600);				
			mainPanel.setHeadingText(title);
						
			final VerticalLayoutContainer centerLayout = new VerticalLayoutContainer();
			centerLayout.setBorders(false);
			centerLayout.getElement().getStyle().setBackgroundColor("white");
			mainPanel.add(centerLayout);				    
		    
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
			
			final VerticalLayoutContainer southLayout = new VerticalLayoutContainer();
			
			txtFieldStatus = new TextField(); 
			southLayout.add(new FieldLabel(txtFieldStatus, "Status"), new VerticalLayoutData(1, -1));
			centerLayout.add(southLayout);				
			
			final ButtonBar buttonBar = new ButtonBar();
			buttonBar.setMinButtonWidth(75);
			buttonBar.getElement().setMargins(10);
					
			buttonBar.add(new TextButton("Submit", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) { }
			}));

			buttonBar.add(new TextButton("Reset", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) { }
			}));		
			
			centerLayout.add(buttonBar, new VerticalLayoutData(1, -1));		
		}
		
		return mainPanel; 
	}	
	
	private void processSubmit()
	{
		try
		{		
			final RetrieveReportSummaryRequestVO requestVO = new RetrieveReportSummaryRequestVO(reportType, txtFieldCustomerId.getText(), txtFieldProjectId.getText(), txtFieldPlateId.getText(), txtFieldExperimentId.getText(), txtFieldChipId.getText());						
			sendRequest(requestVO.toXML());
		}
		catch (Exception e)
		{
			// TBD - log error
			e.printStackTrace();
		}		
	}
	
	private void sendRequest(String requestData)
	{
		final String path = DATA_SERVICE_URL + "/retrieveData"; 
		
		txtFieldStatus.setText("send request to " + path);
		
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, path);  
		try 
		{   
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
	}	
}

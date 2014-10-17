package com.agena.report.gxt.client.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class LungFusionReportWidget implements IsWidget
{
	private String title;
	
	private ContentPanel mainPanel;
	
	private TextField txtFieldReportId;
	private TextField txtFieldStatus;
	
	private TextButton btnReset;	
	private TextButton btnSubmit;	
	
	public LungFusionReportWidget(String title) { this.title = title; }	
	
	public Widget asWidget() 
	{
		if (mainPanel == null)
		{
			mainPanel = new ContentPanel();			
			mainPanel.setBodyBorder(false);
			mainPanel.setBorders(false);			
			mainPanel.setPixelSize(400, 600);				
			mainPanel.setHeadingText(title);			
			
			final VerticalLayoutContainer centerLayout = new VerticalLayoutContainer();
			centerLayout.setBorders(false);
			centerLayout.getElement().getStyle().setBackgroundColor("white");
			mainPanel.add(centerLayout);			    
		    
			// 2. data file path
			txtFieldReportId = new TextField(); 
			txtFieldReportId.setAllowBlank(false);
			centerLayout.add(new FieldLabel(txtFieldReportId, "Report Id"), new VerticalLayoutData(1, -1));			
			
			// 3. status
			txtFieldStatus = new TextField(); 
			centerLayout.add(new FieldLabel(txtFieldStatus, "Status"), new VerticalLayoutData(1, -1));
			
			// 4. buttons
			final ButtonBar buttonBar = new ButtonBar();
			buttonBar.setMinButtonWidth(75);
			buttonBar.getElement().setMargins(10);
					
			btnSubmit = new TextButton("Submit", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) 
				{ 
					final String reportId = txtFieldReportId.getText();					
					sendRequest(reportId);
				}
			});
			
			buttonBar.add(btnSubmit);

			btnReset = new TextButton("Reset", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) { }
			});		
			
			buttonBar.add(btnReset);		
			
			centerLayout.add(buttonBar, new VerticalLayoutData(1, -1));											
		}
		
		return mainPanel; 			
	}
		
	private void sendRequest(String reportId)
	{
		final String path = "http://localhost:8080/js_report/servlet?action=foward&reportId=" + reportId; 	
		txtFieldStatus.setText("Sending request to " + path);
		Window.open(path, "_blank", "");		
	}		
	
	public void setWidgetTitle(String title) { mainPanel.setHeadingText(title); }
	public void reset() 
	{ 
		txtFieldReportId.clear();
		txtFieldStatus.clear();
	}	
}

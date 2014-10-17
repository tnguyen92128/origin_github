package com.agena.report.gxt.client.view;

import static com.agena.report.gxt.client.model.Constants.REPORT_SERVICE_URL;
import static com.agena.report.gxt.client.util.DisplayUtil.displayError;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.HttpProxy;
import com.sencha.gxt.data.client.loader.XmlReader;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class ManageReportWidget implements IsWidget
{
	private String title;
	
	private FramedPanel panel;
	
	private ListLoader<ListLoadConfig, ListLoadResult<ReportInfo>> loader;
	private Grid<ReportInfo> grid;		
	
	interface XmlAutoBeanFactory extends AutoBeanFactory 
	{    
		static XmlAutoBeanFactory instance = GWT.create(XmlAutoBeanFactory.class);     
		AutoBean<ReportInfoCollection> items();     
		AutoBean<ListLoadConfig> loadConfig();   
	}	
	
	interface ReportInfo 
	{    
		@PropertyName("fileName") String getFileName();     
		@PropertyName("createdDate") String getCreatedDate();      
	}
	
	interface ReportInfoCollection 
	{    
		@PropertyName("ReportInfo") List<ReportInfo> getValues();  
	}
	
	interface ReportInfoProperties extends PropertyAccess<ReportInfo> 
	{     
		ValueProvider<ReportInfo, String> fileName();     
		ValueProvider<ReportInfo, String> createdDate();  
	}	
	
	public ManageReportWidget(String title) { this.title = title; }
	
	@Override  
	public Widget asWidget() 
	{
		if (panel == null) 
		{       
			final String path = REPORT_SERVICE_URL + "/getList/"; 				
						
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, path);      
			HttpProxy<ListLoadConfig> proxy = new HttpProxy<ListLoadConfig>(builder);       
			XmlReader<ListLoadResult<ReportInfo>, ReportInfoCollection> reader = 
				new XmlReader<ListLoadResult<ReportInfo>, ReportInfoCollection>(XmlAutoBeanFactory.instance, ReportInfoCollection.class) 
				{        
					protected ListLoadResult<ReportInfo> createReturnData(Object loadConfig, ReportInfoCollection records) 
					{          
						return new ListLoadResultBean<ReportInfo>(records.getValues());        
					}      
				};       
			
			ListStore<ReportInfo> store = new ListStore<ReportInfo>(new ModelKeyProvider<ReportInfo>() 
			{        
				@Override        
				public String getKey(ReportInfo item) 
				{          
					return item.getFileName() + item.getCreatedDate();        
				}      
			});       
			
			loader =  new ListLoader<ListLoadConfig, ListLoadResult<ReportInfo>>(proxy, reader);      
			loader.useLoadConfig(XmlAutoBeanFactory.instance.create(ListLoadConfig.class).as());      
			loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ReportInfo, ListLoadResult<ReportInfo>>(store));       
			ReportInfoProperties props = GWT.create(ReportInfoProperties.class);       
			ColumnConfig<ReportInfo, String> cc1 = new ColumnConfig<ReportInfo, String>(props.fileName(), 200, "Report ID");      
			ColumnConfig<ReportInfo, String> cc2 = new ColumnConfig<ReportInfo, String>(props.createdDate(), 165, "Created Date");             
			List<ColumnConfig<ReportInfo, ?>> l = new ArrayList<ColumnConfig<ReportInfo, ?>>();      
			l.add(cc1);      
			l.add(cc2);          
			
			ColumnModel<ReportInfo> cm = new ColumnModel<ReportInfo>(l);       
			grid = new Grid<ReportInfo>(store, cm);      
			grid.getView().setForceFit(true);      
			grid.setBorders(true);      
			grid.setLoadMask(true);      
			
			grid.setLoader(loader);        
			
			panel = new FramedPanel();      
			panel.setHeadingText(title);      
			panel.setWidget(grid);      
			panel.setPixelSize(500, 600);      
			panel.setCollapsible(true);      
			panel.setAnimCollapse(true);      
			panel.addStyleName("margin-10");      
			panel.setButtonAlign(BoxLayoutPack.CENTER);      
			
			// Refresh
			panel.addButton(new TextButton("Refresh", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) { loader.load(); }			
			}));
			
			// Download
			panel.addButton(new TextButton("Download", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) 
				{ 
					final ReportInfo reportInfo = grid.getSelectionModel().getSelectedItem();
					if (reportInfo == null) 
					{
						displayError("Download Report", "Please select a report");
					}
					else
					{
						downloadReport(reportInfo.getFileName());					
					}
				}			
			}));		

			// Delete
			panel.addButton(new TextButton("Delete", new SelectHandler()
			{
				@Override
				public void onSelect(SelectEvent event) 
				{ 
					final ReportInfo reportInfo = grid.getSelectionModel().getSelectedItem();
					if (reportInfo == null) 
					{
						displayError("Delete Report", "Please select a report");
					}
					else
					{
						deleteReport(reportInfo.getFileName());					
					}				
				}			
			}));					
		}     
		
		return panel;  
	}		
	
	private void downloadReport(String reportId)
	{
		final String path = REPORT_SERVICE_URL + "/download/" + reportId; 		
		try
		{			
			Window.open(path, "_blank", "");		
		}
		catch (Exception e)
		{
			// LATER txtFieldResponse.setText("Failed to download. " + e.getMessage());			
		}
	}
	
	private void deleteReport(final String reportId)
	{
		final String path = REPORT_SERVICE_URL + "/delete/" + reportId; 

		final RequestBuilder builder = new RequestBuilder(RequestBuilder.DELETE, path); 
		try 
		{   
			builder.sendRequest(null, new RequestCallback() 
			{       
				@Override  
				public void onResponseReceived(Request request, Response response) 
				{    
					if (200 == response.getStatusCode()) 	
					{   
						// should be display success
						displayError("Delete Report", "Deleted report " + reportId);  
						
						// refresh list
						grid.clearSizeCache();
						loader.load();
					} 
					else 
					{   
						displayError("Delete Report", "Failed to send request: " + response.getStatusText());        	
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
	
	public void setWidgetTitle(String title) { panel.setHeadingText(title); }	
	public void reset() {}
}
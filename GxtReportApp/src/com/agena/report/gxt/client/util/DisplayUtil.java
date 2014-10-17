package com.agena.report.gxt.client.util;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

public class DisplayUtil 
{
	public static void displayError(String title, String message)
	{
      	final AlertMessageBox d = new AlertMessageBox(title, message);
      	d.show();		
	}
}

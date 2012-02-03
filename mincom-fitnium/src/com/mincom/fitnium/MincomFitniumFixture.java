package com.mincom.fitnium;

import java.util.List;

import com.mincom.ellipse.rc.apiv2.Application;
import com.mincom.ellipse.rc.apiv2.Grid;

public class MincomFitniumFixture extends BaseMincomFitniumFixture {
	
	private Application application;
	private Grid gridResult;
	
	public MincomFitniumFixture() {
		super();
	}
	
	public void loadApplication(String app) {
		this.application = mfuiv2.loadApp(app).waitForReady();
	}

	public void initGrid() {
		gridResult = this.application.getGrid("results").waitForLoaded();
	}
	
	public int getGridCount() {
		return gridResult.getRowCount();
	}
	
	public void openGridRecord(int record) {
		gridResult.doubleClick(record);
	}
	
	public String getGridcellGetvalue(int row, String column) {
		return gridResult.getGridCell(column, row).getValue();

	}
	public void gridMarkRow(int row) {
		 gridResult.markRow(row);

	}
	
	public void getGridcellSetvalueWith(int row, String column, String value) {
		 gridResult.getGridCell(column, row).setValue(value);

	}
	
	public void gridAction(String action) {
		 gridResult.button(action);
	}
	
	public void setWidgetWithValue(String id, String value) {
		this.application = application.setWidgetValue(id, value);
	}

	public void setDialogWidgetWithValue(String id, String value) {
		application.getDialogWidget(id).setValue(value);
	}
	public String getWidgetWithValue(String widgetName) {
		return application.getWidget(widgetName).getValue();
	}
	public void selectTheTab(String tabname) {
		this.application = application.selectTab(tabname).waitForReady();
	}
	public void callAction(String action ) {
		this.application = application.toolbarAction(action);
	}
	
	public void waitForLoadedApp() {
		this.application = application.waitForLoadedApplication();
	}

	public void waitForReady() {
		this.application = application.waitForReady();
	}
	public void confirmAction(String action) {
		this.application = application.dialogButton(action);
	}
	
	public boolean assertWidgetEditable(String widget) {
		return application.getWidget(widget).isEditable();
	}

	
	public void captureScreenToFile(String filename) {
//        FitniumScreenCaptureAPI.captureScreenToFile(this, filename);
        mfuiv2.captureScreenshot(filename);
    }
	public void closeApps() {
//      FitniumScreenCaptureAPI.captureScreenToFile(this, filename);
		this.application.closeApp();
  }
}

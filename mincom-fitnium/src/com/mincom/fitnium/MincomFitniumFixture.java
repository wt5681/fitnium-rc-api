package com.mincom.fitnium;

import com.mincom.ellipse.rc.apiv2.Application;

public class MincomFitniumFixture extends BaseMincomFitniumFixture {
	
	private Application application;
	
	public MincomFitniumFixture() {
		super();
	}
	
	public void loadApplication(String app) {
		this.application = mfuiv2.loadApp(app);
	}
	
	public void setWidgetWithValue(String id, String value) {
		this.application = application.setWidgetValue(id, value);
	}

}

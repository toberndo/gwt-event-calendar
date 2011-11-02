/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.client;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.gwt.core.client.GWT;
//import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.yarkon.gwt.eventcalendar.shared.EventInfo;


public class EventCalendarModel {

	private EventCalendarServiceAsync eventService = EventCalendarService.Util.getInstance();
	//private String locale = LocaleInfo.getCurrentLocale().getLocaleName();
	private Logger logger = Logger.getLogger("EventCalendarModel");
	
	private EventCalendarModel() {
		super();
		//logger.log(Level.SEVERE, locale);	
	}

	private static EventCalendarModel instance;
	public static EventCalendarModel getInstance(){
		if (instance == null) {
			instance = GWT.create(EventCalendarModel.class);
		}
		return instance;
	}

	public void getEventList (Date month, final EventTable table) {
		eventService.getEvents(month, new AsyncCallback<List<EventInfo>>(){
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());	
			}
			@Override
			public void onSuccess(List<EventInfo> result) {
				table.setRowData(0, result);
			}
		});
	}
	
}

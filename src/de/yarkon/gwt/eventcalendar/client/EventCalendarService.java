/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.client;

import java.util.Date;
import java.util.List;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.yarkon.gwt.eventcalendar.shared.EventInfo;

@RemoteServiceRelativePath("EventCalendarService")
public interface EventCalendarService extends RemoteService {
	
	List<EventInfo> getEvents(Date month);
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static EventCalendarServiceAsync instance;
		public static EventCalendarServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(EventCalendarService.class);
			}
			return instance;
		}
	}
}

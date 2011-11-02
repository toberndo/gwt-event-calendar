/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.client;

import java.util.Date;
import java.util.List;


import com.google.gwt.user.client.rpc.AsyncCallback;

import de.yarkon.gwt.eventcalendar.shared.EventInfo;

public interface EventCalendarServiceAsync {

	void getEvents(Date month, AsyncCallback<List<EventInfo>> callback);

}

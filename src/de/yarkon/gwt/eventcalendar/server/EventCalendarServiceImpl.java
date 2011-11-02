/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.yarkon.gwt.eventcalendar.client.EventCalendarService;
import de.yarkon.gwt.eventcalendar.client.EventFields;
import de.yarkon.gwt.eventcalendar.shared.EventInfo;

public class EventCalendarServiceImpl extends RemoteServiceServlet implements
		EventCalendarService {

	private static final long serialVersionUID = -2053350285957344506L;
	private static final DateFormat dateShortFormat = DateFormat
			.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
	private List<EventInfo> eventList;
	private Logger logger = Logger.getLogger("EventCalendarService");
	private MemcacheService memCache;
	private static final String MEM_CACHE_KEY = "androgon_events";
	private static final int EXPIRATION_DELAY_SEC = 24 * 3600; // one day

	@SuppressWarnings("deprecation")
	@Override
	public List<EventInfo> getEvents(Date month) {
		List<EventInfo> listByMonth = new ArrayList<EventInfo>();
		if (eventList == null) {
			loadEventList();
		}
		for (EventInfo event : eventList) {
			if (event.getDate().getMonth() == month.getMonth()
					&& event.getDate().getYear() == month.getYear()) {
				listByMonth.add(event);
			}
		}
		return listByMonth;
	}

	@SuppressWarnings("unchecked")
	private void loadEventList() {
		// initialize memcache
		if (memCache == null) {
			memCache = MemcacheServiceFactory.getMemcacheService();
		}
		// check if values exist in memcache
		if (memCache.contains(MEM_CACHE_KEY)) {
			eventList = (List<EventInfo>) memCache.get(MEM_CACHE_KEY);
		}
		if (eventList == null) {
			eventList = loadSpreadsheet();
			// put event list to cache
			memCache.put(MEM_CACHE_KEY, eventList,
					Expiration.byDeltaSeconds(EXPIRATION_DELAY_SEC));
		}
	}

	private List<EventInfo> loadSpreadsheet() {
		SpreadsheetService service = new SpreadsheetService(
				"androgon-eventcalendar-srv");
		URL feedURL;
		ListFeed feed;
		try {
			// hard coded for now
			feedURL = new URL(
					"http://spreadsheets.google.com/feeds/list/0Ag_keZpRsME-dGNkeXA5aTdkWkZQLUl4SzJsY0RSVmc/od6/public/values");
			feed = service.getFeed(feedURL, ListFeed.class);
			if (feed != null) {
				return mapFeed2List(feed);
			}
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} catch (ServiceException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	private List<EventInfo> mapFeed2List(ListFeed feed) {
		List<EventInfo> list = new ArrayList<EventInfo>();
		main: for (ListEntry entry : feed.getEntries()) {
			EventInfo event = new EventInfo();
			for (String tag : entry.getCustomElements().getTags()) {
				String value = entry.getCustomElements().getValue(tag);
				switch (EventFields.valueOf(tag)) {
				case veranstaltung:
					event.setEvent(value);
					break;
				case datum:
					try {
						// SHORT: dd.MM.YY, MEDIUM dd.MM.YYYY
						event.setDate(dateShortFormat.parse(value));
					} catch (ParseException e) {
						logger.log(Level.SEVERE, e.getMessage());
						continue main;
					}
					break;
				case ort:
					event.setLocation(value);
					break;
				case link:
					event.setLinkURL(value);
					break;
				default:
					logger.log(Level.SEVERE, EventFields.valueOf(tag)
							.toString());
				}
			}
			list.add(event);
		}
		// sort list by date
		Collections.sort(list, new EventInfo());
		return list;
	}
}

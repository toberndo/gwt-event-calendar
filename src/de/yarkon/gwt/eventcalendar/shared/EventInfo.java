/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.shared;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class EventInfo implements Serializable, Comparator<EventInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5647720743887177909L;
	private String event;
	private Date date;
	private String location;
	private String linkURL;
	
	public EventInfo() {
		super();
	}


	public EventInfo(String event, Date date, String location, String linkURL) {
		super();
		this.event = event;
		this.date = date;
		this.location = location;
		this.linkURL = linkURL;
	}


	public EventInfo(String event, Date date, String location) {
		super();
		this.event = event;
		this.date = date;
		this.location = location;
	}

	/**
	 * @param linkURL
	 *            the linkURL to set
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	/**
	 * @return the linkURL
	 */
	public String getLinkURL() {
		return linkURL;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	@Override
	public int compare(EventInfo o1, EventInfo o2) {
		return o1.date.compareTo(o2.date);
	}
	
}

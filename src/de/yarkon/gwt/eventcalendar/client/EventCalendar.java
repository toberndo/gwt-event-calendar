/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.client;

import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EventCalendar implements EntryPoint {

	interface MyResources extends ClientBundle {
		public static final MyResources INSTANCE = GWT
				.create(MyResources.class);

		@CssResource.NotStrict
		@Source("EventCalendar.css")
		public CssResource css();
	}

	private StackLayoutPanel panel;
	private EventCalendarModel dataModel = EventCalendarModel.getInstance();

	// private Logger logger = Logger.getLogger("EventCalendar");

	private final int NUMBER_OF_MONTHS = 12;
	private final int SHOW_PAST_MONTHS = 2;
	private int currentMonth;
	private int firstMonth;
	private int firstYear;

	@SuppressWarnings("deprecation")
	public EventCalendar() {
		super();
		assert SHOW_PAST_MONTHS <= 12;
		Date date = new Date();
		/*
		 * only interested in year and month, for safety reason (timezones) set
		 * day to 10
		 */
		date.setDate(10);
		currentMonth = date.getMonth();
		// logger.log(Level.SEVERE, Integer.toString(currentMonth));
		if (currentMonth >= SHOW_PAST_MONTHS) {
			date.setMonth(currentMonth - SHOW_PAST_MONTHS);
		} else {
			date.setMonth(currentMonth - SHOW_PAST_MONTHS + 12);
			date.setYear(date.getYear() - 1);
		}
		firstMonth = date.getMonth();
		firstYear = date.getYear();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Inject the contents of the CSS file
		MyResources.INSTANCE.css().ensureInjected();

		// Create a three-item stack, with headers sized in EMs.
		initStackPanel();

		/*
		 * Attach the LayoutPanel to the RootLayoutPanel. The latter will listen
		 * for resize events on the window to ensure that its children are
		 * informed of possible size changes.
		 */
		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(panel);
	}

	@SuppressWarnings("deprecation")
	private void initStackPanel() {
		panel = new StackLayoutPanel(Unit.EM);
		EventTable table;
		Date date = new Date(firstYear, firstMonth, 10);
		DateTimeFormat dateFormatMonth = DateTimeFormat
				.getFormat(DateTimeFormat.PredefinedFormat.MONTH);
		DateTimeFormat dateFormatYear = DateTimeFormat
				.getFormat(DateTimeFormat.PredefinedFormat.YEAR_MONTH);
		String header;
		// create number_of_months stacks
		for (int i = 0; i < NUMBER_OF_MONTHS; i++) {
			// Create a new table widget for the event list
			int newMonth = (firstMonth + i) % 12;
			date.setMonth(newMonth);
			if (newMonth == 0 && i != 0) {
				date.setYear(date.getYear() + 1);
			}
			if (newMonth == 0 || i == 0) {
				header = dateFormatYear.format(date);
			} else {
				header = dateFormatMonth.format(date);
			}
			table = new EventTable((Date) date.clone());
			ScrollPanel scrollPanel = new ScrollPanel(table);
			scrollPanel.addStyleName("scrollPanel");
			panel.add(scrollPanel, header, 2);
		}

		/*
		 * show current month, SHOW_PAST_MONTHS luckily is the index of the
		 * current month
		 */
		panel.showWidget(SHOW_PAST_MONTHS, true);
		// event handler to fill table with data on selection
		panel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				ScrollPanel scrollPanel = (ScrollPanel) panel.getWidget(event
						.getItem());
				EventTable tableWidget = (EventTable) scrollPanel.getWidget();
				if (tableWidget.isTableFilled() == false) {
					// logger.log(Level.SEVERE, "Header Height:" +
					// Integer.toString(panel.getVisibleWidget().getOffsetHeight()));
					dataModel.getEventList(tableWidget.getMonth(), tableWidget);
					tableWidget.setFocus(true);
				}
			}
		});
		// fire event to load data for current month
		BeforeSelectionEvent.fire(panel, SHOW_PAST_MONTHS);
	}

}

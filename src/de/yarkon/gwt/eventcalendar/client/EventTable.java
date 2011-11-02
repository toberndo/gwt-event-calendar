/*
 * Copyright 2010 Thomas Oberndörfer
 * Licensed under the GNU General Public License v3 or any later version
 * See license text at http://www.gnu.org/licenses/gpl.txt
*/
package de.yarkon.gwt.eventcalendar.client;

import java.util.Date;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;

import de.yarkon.gwt.eventcalendar.shared.EventInfo;

public class EventTable extends CellTable<EventInfo> {

	private Date month;
	private boolean tableFilled = false;
	private static DateTimeFormat dateShortFormat = DateTimeFormat
			.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
	//private Logger logger = Logger.getLogger("EventTable");

	interface TableResources extends CellTable.Resources {
		public static final TableResources INSTANCE = GWT
				.create(TableResources.class);

		@CssResource.NotStrict
		@Source(value = { CellTable.Style.DEFAULT_CSS, "EventTable.css" })
		CellTable.Style cellTableStyle();
	}

	public EventTable(Date month) {
		super();

		TableResources.INSTANCE.cellTableStyle().ensureInjected();

		this.setMonth(month);

		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		// Anchor column for the name of the event
		Column<EventInfo, EventInfo> nameColumn = new Column<EventInfo, EventInfo>(
				new AbstractCell<EventInfo>() {

					@Override
					public void render(
							com.google.gwt.cell.client.Cell.Context context,
							EventInfo value, SafeHtmlBuilder sb) {
						if (value == null) {
							return;
						}

						// Append some HTML that sets the text color.
						sb.appendHtmlConstant("<a target=\"_blank\" href=\""
								+ value.getLinkURL() + "\">");
						sb.appendEscaped(value.getEvent());
						sb.appendHtmlConstant("</a>");
					}
				}) {

			@Override
			public EventInfo getValue(EventInfo object) {
				return object;
			}
		};

		addColumn(nameColumn, "Veranstaltung");
		addColumnStyleName(0, "cellTableColumn-event");

		// Add a date column
		TextColumn<EventInfo> dateColumn = new TextColumn<EventInfo>() {
			@Override
			public String getValue(EventInfo object) {
				return dateShortFormat.format(object.getDate());
			}
		};
		addColumn(dateColumn, "Datum");
		addColumnStyleName(1, "cellTableColumn-date");

		// Add a text column to show location of the event.

		TextColumn<EventInfo> locationColumn = new TextColumn<EventInfo>() {
			@Override
			public String getValue(EventInfo object) {
				return object.getLocation();
			}
		};

		addColumn(locationColumn, "Ort");
		addColumnStyleName(2, "cellTableColumn-loc");

		addStyleName("cellTable");
		setRowStyles(new RowStyles<EventInfo>() {
			@Override
			public String getStyleNames(EventInfo row, int rowIndex) {
				return "cellTableRow";
			}
		});
	}

	@Override
	public void setRowData(int start, List<? extends EventInfo> values) {
		setRowCount(values.size(), true);
		if (values != null)
			tableFilled = true;
		super.setRowData(start, values);
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(Date month) {
		this.month = month;
	}

	/**
	 * @return the month
	 */
	public Date getMonth() {
		return this.month;
	}

	/**
	 * @param tableFilled
	 *            the tableFilled to set
	 */
	public void setTableFilled(boolean tableFilled) {
		this.tableFilled = tableFilled;
	}

	/**
	 * @return the tableFilled
	 */
	public boolean isTableFilled() {
		return tableFilled;
	}

}

package at.ffesternberg.sybos.sybos2ical;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ffesternberg.sybos.SybosEventClient;
import at.ffesternberg.sybos.entity.Event;
import at.ffesternberg.sybos.exception.SybosClientException;
import at.ffesternberg.sybos.sybos2ical.util.PlaceholderStringFormater;
import biweekly.ICalendar;
import biweekly.component.VEvent;

public class Sybos2Ical {
	final private static Logger log = LoggerFactory.getLogger(Sybos2Ical.class);
	private SybosEventClient sec;
	private String descriptionString;

	public Sybos2Ical(SybosEventClient sec) {
		this(sec, "${inhalt}");
	}

	public Sybos2Ical(SybosEventClient sec, String descriptionString) {
		if (sec == null) {
			throw new IllegalArgumentException("SybosEventClient is mandatory - was null");
		}
		this.sec = sec;
		if (descriptionString == null || descriptionString.isEmpty()) {
			throw new IllegalArgumentException("descriptionString is null or empty!");
		}
		this.descriptionString = descriptionString;
	}

	public ICalendar loadAndConvert() throws SybosClientException {
		List<Event> sybosEvents = sec.loadEntites();

		ICalendar cal = new ICalendar();
		cal.setDescription("Public syBOS Events");
		cal.setProductId("-//FF Esternberg//sybos2ical//EN");

		for (Event sybosEvent : sybosEvents) {
			log.debug("Event: " + sybosEvent.getTitel() + "\t"
					+ DateFormat.getDateTimeInstance().format(sybosEvent.getFrom()));

			VEvent vevent = new VEvent();
			vevent.setSummary(sybosEvent.getTitel());
			vevent.setDateStart(sybosEvent.getFrom());
			vevent.setDateEnd(sybosEvent.getTo());
			vevent.setLocation(sybosEvent.getOrt());
			vevent.setUid("sybos-" + sybosEvent.getId());

			HashMap<String, String> values = new HashMap<String, String>();
			values.put("beschreibung", sybosEvent.getBeschreibung());
			values.put("inhalt", sybosEvent.getInhalt());
			values.put("referat", sybosEvent.getReferat());
			values.put("bezeichnung1", sybosEvent.getBezeichnung1());
			values.put("bezeichnung2", sybosEvent.getBezeichnung2());
			values.put("kosten", sybosEvent.getKosten());
			values.put("voraussetzung", sybosEvent.getVoraussetzung());
			values.put("abteilung", sybosEvent.getAbteilung());
			values.put("ort", sybosEvent.getOrt());

			String description = PlaceholderStringFormater.getInstance().replace(descriptionString, values);

			vevent.setDescription(description);
			cal.addEvent(vevent);
		}
		return cal;
	}

}

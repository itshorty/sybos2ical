package at.ffesternberg.sybos.sybos2ical;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ffesternberg.sybos.SybosFuturePastEventClient;
import at.ffesternberg.sybos.exception.SybosClientException;
import at.ffesternberg.sybos.sybos2ical.util.AppProperties;
import biweekly.ICalendar;

public class TheMain {
	final private static Logger logger = LoggerFactory.getLogger(TheMain.class);

	public static void main(String[] args) {
		logger.info("sybos2ical started");
		AppProperties.initialize("sybos2ical");

		Options options = new Options();
		options.addOption(OptionBuilder.withArgName("out").hasArg(true).withDescription("where to write the ical file")
				.isRequired(false).create("out"));
		try {
			CommandLine line = new DefaultParser().parse(options, args);
			
			OutputStream out = System.out;
			if(line.hasOption("out")){
				File outputfile = new File (line.getOptionValue("out"));
				if(outputfile.exists()){
					outputfile.delete();
				}
				outputfile.createNewFile();
				out=new FileOutputStream(outputfile);
			}
			
			String baseURL = AppProperties.getInstance().getProperty("sybos.baseurl");
			String token = AppProperties.getInstance().getProperty("sybos.token");
			boolean loadFuture = Boolean
					.parseBoolean(AppProperties.getInstance().getProperty("sybos.loadfuture", "true"));
			boolean loadPast = Boolean.parseBoolean(AppProperties.getInstance().getProperty("sybos.loadpast", "false"));

			if (StringUtils.isEmpty(baseURL)) {
				logger.error("SYBOS Baseurl (sybos.baseurl) not set - aborting");
				System.exit(-1);
			}
			if (StringUtils.isEmpty(token)) {
				logger.error("SYBOS Token (sybos.token) not set - aborting");
				System.exit(-1);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("sybos2ical config:");
				logger.debug("sybos.baseurl=" + baseURL);
				logger.debug("sybos.token=" + token);
				logger.debug("sybos.loadFuture=" + loadFuture);
				logger.debug("sybos.loadPast=" + loadPast);
			}

			SybosFuturePastEventClient sec = new SybosFuturePastEventClient(baseURL, token);
			sec.setLoadPast(loadPast);
			sec.setLoadFuture(loadFuture);

			Sybos2Ical sybos2ical = new Sybos2Ical(sec);

			ICalendar ical = sybos2ical.loadAndConvert();
		
			ical.write(out);

		} catch (SybosClientException e) {
			logger.error("Error while loading events!", e);
			System.exit(-2);
		} catch (ParseException e) {
			logger.error("Error while parsing command line!", e);
			System.exit(-2);
		} catch (FileNotFoundException e) {
			logger.error("Can't open ical file!", e);
			System.exit(-2);
		} catch (IOException e) {
			logger.error("General I/O Error!", e);
			System.exit(-2);
		}

	}

}
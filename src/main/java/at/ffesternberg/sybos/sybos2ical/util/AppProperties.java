package at.ffesternberg.sybos.sybos2ical.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppProperties extends Properties {
	private static final Logger log = LoggerFactory.getLogger(AppProperties.class);
	private static final long serialVersionUID = 6669061845195483169L;
	private static AppProperties instance = null;

	public static void initialize(String name) {
		try {
			instance = new AppProperties(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static AppProperties getInstance() {
		if (instance == null) {
			throw new IllegalStateException("AppProperties not initialized!");
		}
		return instance;
	}

	private AppProperties(String appname) throws IOException {
		super(System.getProperties());
		loadFromClasspath(appname + ".default.properties");
		loadFromClasspath(appname + ".properties");
		loadFromFilesystem(appname + ".properties");
		loadFromFilesystem(appname + ".protected.properties");
	}

	private void loadFromClasspath(String file) throws IOException {
		if (this.getClass().getResource("/" + file) != null) {
			load(this.getClass().getResourceAsStream("/" + file));
			log.debug("Loaded properties from Classpath resource: /" + file);
		}
	}

	private void loadFromFilesystem(String file) throws IOException {
		File f = new File(file);
		if (f.exists() && f.isFile() && f.canRead()) {
			load(new FileReader(f));
			log.debug("Loaded properties from Filesystem: " + f.getAbsolutePath());
		}
	}

}

package at.ffesternberg.sybos.sybos2ical.util;

import org.testng.Assert;
import org.testng.annotations.Test;


public class AppPropertiesTest {
	@Test
	public void testAppProperties() {

		AppProperties.initialize("test");

		AppProperties props = AppProperties.getInstance();
		Assert.assertEquals(props.getProperty("default-only"),
				"default-only-propertie");
		Assert.assertEquals(props.getProperty("classpath-only"),
				"classpath-only-propertie");
		// Assert.assertEquals(props.getProperty("filesytem-only"),
		// "filesytem-only-propertie");
		Assert.assertEquals(props.getProperty("override"),
				"classpath-override-propertie");
	}
}

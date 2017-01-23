package at.ffesternberg.sybos.sybos2ical.util;

import java.util.HashMap;

import junit.framework.Assert;

import org.testng.annotations.Test;


public class PlaceholderStringFormaterTest {
	@Test
	public void test(){
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("foo", "bar");
		values.put("planet","World");
		
		Assert.assertEquals(new PlaceholderStringFormater().replace("Hello, ${planet}! ${foo}", values), "Hello, World! bar");
	}
}

package at.ffesternberg.sybos.sybos2ical.util;

import java.util.HashMap;

public class PlaceholderStringFormater {
	private static PlaceholderStringFormater psf;
	private String prefix;
	private String postfix;

	public PlaceholderStringFormater() {
		this("${","}");
	}
	public PlaceholderStringFormater(String prefix,String postfix){
		this.prefix=prefix;
		this.postfix=postfix;
	}

	public String replace(String s, HashMap<String, ? extends Object> values) {
		for(String key:values.keySet()){
			String replacement=values.get(key).toString();
			s=s.replace(prefix+key+postfix, replacement);
		}
		return s;
	}
	
	public static PlaceholderStringFormater getInstance(){
		if(psf==null){
			psf=new PlaceholderStringFormater();
		}
		return psf;
	}
}

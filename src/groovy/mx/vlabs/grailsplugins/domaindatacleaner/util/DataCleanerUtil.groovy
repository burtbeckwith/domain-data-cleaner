package mx.vlabs.grailsplugins.domaindatacleaner.util

class DataCleanerUtil {
	
	public static String cleanupString(String data,boolean trim = true){
		return trim ? data.trim().replaceAll("\\s+", " ") : 
					  data.replaceAll("\\s+", " ");
	}

}

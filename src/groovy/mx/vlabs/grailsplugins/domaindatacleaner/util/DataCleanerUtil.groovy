package mx.vlabs.grailsplugins.domaindatacleaner.util

class DataCleanerUtil {

	static String cleanupString(String data, boolean trim = true) {
		return (trim ? data.trim() : data).replaceAll("\\s+", " ")
	}
}

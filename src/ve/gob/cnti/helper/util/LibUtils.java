package ve.gob.cnti.helper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ve.gob.cnti.helper.input.ReadFile;

public class LibUtils {

	public LibUtils() {

	}

	public static String firstLetterUpper(String word) {

		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}
	
	public static String firstLetterLower(String word) {

		return Character.toLowerCase(word.charAt(0)) + word.substring(1);
	}
	
	public static String readSnippetFile(String pathFile){
		ReadFile rf = new ReadFile();
		
		String textFile = rf.readFile(pathFile);
		
		return textFile;		
	}

	public static boolean createDirs(String path) {

		File dir = new File(path);

		return dir.mkdirs();

	}
	
	public static String convertPackage2Dirs(String packages){
		
		return packages.replaceAll("\\.", "/");
	}
	
	public static boolean cretePackagesDirs(String packages){
		
		return createDirs(packages);
	}
	
	public static String replacePattern(String pattern, String replacement, String text) {

		return text.replaceAll(pattern, replacement);
	}
	
	public static Properties loadFileProperties(String file){
		
		InputStream input = null;
		Properties props = new Properties();
		
		try {
			input = new FileInputStream(file);
			props.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return props;
	}
	
}

package csvExport;

import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;
import java.util.Scanner;

public class weatherMinTemp {
	
	/**
	 * if current temp is lower than lowest temp so far, returns true, else returns false
	 * @param current the current row examined
	 * @param lowestSoFar the lowest record so far
	 * @return
	 */
	public static boolean coldestOfTwo(CSVRecord current, CSVRecord lowestSoFar){
		if (lowestSoFar == null){
			lowestSoFar = current;
		}
		else{
			double currentTemp = Double.parseDouble(current.get("TemperatureF"));
			double lowestTemp = Double.parseDouble(lowestSoFar.get("TemperatureF"));
			if (currentTemp < lowestTemp && currentTemp != -9999){
				lowestSoFar = current;
			}
		}
		return (lowestSoFar == current);
	}
	
	/**
	 * returns coldest record in one file
	 * @param parser
	 * @return
	 */
	public static CSVRecord coldestHourInFile(CSVParser parser){
		CSVRecord lowestSoFar = null;
		for (CSVRecord record : parser){
			if (coldestOfTwo(record, lowestSoFar)){
				lowestSoFar = record;
			}
		}
		return lowestSoFar;
	}
	
	public static void testColdestHourInFile(){
		FileResource fr = new FileResource();
		CSVParser csv = fr.getCSVParser();
		CSVRecord lowest = coldestHourInFile(csv);
		System.out.println(lowest.get("DateUTC") + " " + lowest.get("TemperatureF"));
	}
	
	/**
	 * Takes selected files and returns name of file with lowest temp
	 * @return name of file with lowest temperature
	 * 
	 */
	public static String fileWithColdestTemperature(){
		DirectoryResource dir = new DirectoryResource();
		CSVRecord lowestSoFar = null;
		File lowestFile = null;
		for (File fr : dir.selectedFiles()){
			FileResource frR = new FileResource(fr);
			CSVParser csv = frR.getCSVParser();
			CSVRecord record = coldestHourInFile(csv);
			if(coldestOfTwo(record, lowestSoFar)){
				lowestFile = fr;
			}
		}
		return lowestFile.getAbsolutePath();
	}
	
	public static void testFileWithColdestTemp(){
		String lowestFile = fileWithColdestTemperature();
		FileResource lowestFile2 = new FileResource(lowestFile);
		String lowestFile1 = lowestFile.substring(lowestFile.lastIndexOf("\\")+1);
		CSVParser csv = lowestFile2.getCSVParser();
		CSVRecord record = coldestHourInFile(csv);
		System.out.println("Coldest day was in file " + lowestFile1);
		System.out.println("Coldest temperature on that day was " + record.get("TemperatureF"));
		System.out.println("All the temperatures on that day are:");
		csv = lowestFile2.getCSVParser();
		for (CSVRecord row : csv){
			System.out.println(lowestFile1.substring(8,lowestFile1.length()-4) + " " +
					row.get("TimeEST") + " " + row.get("TemperatureF"));
		}
	}
	
	/**
	 * gets CSVRecord with the lower humidity
	 * @param record
	 * @param lowestSoFar
	 * @return
	 */
	public static CSVRecord getLowestOfTwo(CSVRecord record, CSVRecord lowestSoFar){
		if (lowestSoFar == null){
			lowestSoFar = record;
		}else{
			if (record.get("Humidity").equals("N/A") == false){
				Double record_humidity = Double.parseDouble(record.get("Humidity"));
				Double lowest_humidity = Double.parseDouble(lowestSoFar.get("Humidity"));
				if(record_humidity < lowest_humidity){
					lowestSoFar = record;
				}
			}
		}
		return lowestSoFar;
	}
	
	/**
	 * returns CSVRecord with the lowest humidity in a parser
	 * @param parser
	 * @return
	 */
	public static CSVRecord lowestHumidityInFile(CSVParser parser){
		CSVRecord lowestSoFar = null;
		for (CSVRecord record : parser){
			lowestSoFar = getLowestOfTwo(record, lowestSoFar);
		}
		return lowestSoFar;
	}

	public static void testLowestHumidityInFile(){
		FileResource fr = new FileResource();
		CSVParser csv = fr.getCSVParser();
		CSVRecord record = lowestHumidityInFile(csv);
		System.out.println("Lowest Humidity was " + record.get("Humidity")
		+ " at " + record.get("DateUTC"));
	}
	
	public static CSVRecord lowestHumidityInManyFiles(){
		CSVRecord lowestSoFar = null;
		DirectoryResource dir = new DirectoryResource();
		for (File file : dir.selectedFiles()){
			FileResource fr = new FileResource(file);
			CSVParser csv = fr.getCSVParser();
			CSVRecord lowestInFile = lowestHumidityInFile(csv);
			lowestSoFar = getLowestOfTwo(lowestInFile, lowestSoFar);
		}
		return lowestSoFar;
	}
	
	public static void testLowestHumidityInManyFiles(){
		CSVRecord record = lowestHumidityInManyFiles();
		System.out.println("Lowest Humidity was " + record.get("Humidity") + " at "
				+ record.get("DateUTC"));
	}

	
	/**
	 * returns the average temperature in a file
	 * @param parser
	 * @return
	 */
	public static Double averageTemperatureInFile(CSVParser parser){
		Double average = null;
		int count = 0;
		for (CSVRecord record : parser){
			String tempString = record.get("TemperatureF");
			if (tempString.equals("-9999") == false){
				double temp = Double.parseDouble(tempString);
				if (average == null){
					average = temp;
					count++;
				}else{
					average += temp;
					count++;
				}
				
			}
		}
		if (average == null){
			return average;
		}else{
			return average / count;
		}
		
	}
	
	public static void testAverageTemperatureInFile(){
		FileResource fr = new FileResource();
		CSVParser parser = fr.getCSVParser();
		Double average = averageTemperatureInFile(parser);
		System.out.println("Average temperature in file is " + average);
	}
	
	public static Double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
		Double average = null;
		int count = 0;
		for (CSVRecord record : parser){
			String tempString = record.get("TemperatureF");
			String humidString = record.get("Humidity");
			if (humidString.equals("N/A") == false){
				Double humid = Double.parseDouble(humidString);
				if (tempString.equals("-9999") == false && humid >= value){
					double temp = Double.parseDouble(tempString);
					if (average == null){
						average = temp;
						count++;
					}else{
						average += temp;
						count++;
					}
					
				}
			}
		}
		if (average == null){
			return average;
		}else{
			return average / count;
		}
	}
	
	public static void testAverageTemperatureWithHighHumidityInFile(int value){
		FileResource fr = new FileResource();
		CSVParser parser = fr.getCSVParser();
		Double average = averageTemperatureWithHighHumidityInFile(parser, value);
		if (average == null){
			System.out.println("No temperatures with that humidity");
		}else{
			System.out.println("Average Temp when high Humidity is " + average);
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter cold, coldm, humid, humidm, ave, avem:");
		String input = scan.next();
		if(input.equals("cold")){
			testColdestHourInFile();
		}else if(input.equals("coldm")){
			testFileWithColdestTemp();
		}else if (input.equals("humid")){
			testLowestHumidityInFile();
		}else if(input.equals("humidm")){
			testLowestHumidityInManyFiles();
		}else if(input.equals("ave")){
			testAverageTemperatureInFile();
		}else if(input.equals("avem")){
			System.out.println("Enter threshold humidity:");
			String value = scan.next();
			int val = Integer.parseInt(value);
			testAverageTemperatureWithHighHumidityInFile(val);
		}else{
			System.out.println("Invalid input");
		}
		scan.close();
	}

}

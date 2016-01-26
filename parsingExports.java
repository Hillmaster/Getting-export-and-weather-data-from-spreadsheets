package csvExport;

import edu.duke.*;

import java.util.Scanner;

import org.apache.commons.csv.*;

public class parsingExports {
	
	/**
	 * tests
	 */
	public static void tester(){
		Scanner reader = new Scanner(System.in);  
		System.out.println("What do you want to do? Keywords: info, two, num, big");
		String n = reader.next(); 
		if(n.equals("info")){
			FileResource fr = new FileResource();
			CSVParser csv = fr.getCSVParser();
			System.out.println("What country?");
			String country = reader.next();
			System.out.println(getCountryInfo(csv, country));
		}else if(n.equals("two")){
			FileResource fr = new FileResource();
			CSVParser csv = fr.getCSVParser();
			System.out.println("Export 1:");
			String export1 = reader.next();
			System.out.println("Export 2:");
			String export2 = reader.next();
			listExportersTwoProducts(csv, export1, export2);
		}else if(n.equals("num")){
			FileResource fr = new FileResource();
			CSVParser csv = fr.getCSVParser();
			System.out.println("Exports: ");
			String export = reader.next();
			System.out.println(numberOfExporters(csv, export));
		}else if(n.equals("big")){
			FileResource fr = new FileResource();
			CSVParser csv = fr.getCSVParser();
			System.out.println("Amount: ");
			String amount = reader.next();
			bigExporters(csv, amount);
		}else{
			System.out.println("Invalid input");
		}
		reader.close();
	}
	
	/**
	 * lists all information for a country from the csv file
	 * @param parser
	 * @param country
	 * @return Not found if no country record, and information if record is found
	 */
	public static String getCountryInfo(CSVParser parser, String country){
		for (CSVRecord record: parser){
			String name = record.get("Country");
			if (name.equals(country)){
				return name + ": " + record.get("Exports") + ": " + record.get("Value (dollars)");
			}
		}
		return "NOT FOUND";
	}
	
	/**
	 * Prints a list of countries that contain both export1 and export2
	 * @param parser
	 * @param export1
	 * @param export2
	 */
	public static void listExportersTwoProducts(CSVParser parser, String export1, String export2){
		for(CSVRecord record: parser){
			String exports = record.get("Exports");
			if (exports.contains(export1) && exports.contains(export2)){
				System.out.println(record.get("Country"));
			}
		}
	}
	
	public static int numberOfExporters(CSVParser parser, String exportitem){
		int count = 0;
		for (CSVRecord record: parser){
			if (record.get("Exports").contains(exportitem)){
				count++;
			}
		}
		return count;
	}
	
	public static void bigExporters(CSVParser parser, String amount){
		amount = amount.replace("$", "");
		amount = amount.replace(",", "");
		long value = Long.parseLong(amount);
		for (CSVRecord record: parser){
			String countryamount = record.get("Value (dollars)");
			countryamount = countryamount.replace("$", "");
			countryamount = countryamount.replace(",", "");
			long countryvalue = Long.parseLong(countryamount);
			if (countryvalue > value){
				System.out.println(record.get("Country")+ " " + record.get("Value (dollars)"));
			}
		}
	}
	

	public static void main(String[] args) {
		tester();
	}

}

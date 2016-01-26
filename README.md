# Getting-export-and-weather-data-from-spreadsheets
Uses apache commons csv library to open csv files and parse them

parsingExports.java:

The CSV file exportdata.csv has information on the export products of countries; you can download a .zip folder with this and other export data files here. In particular it has three column headers labeled Country, Exports, and Value (dollars). The Country column represents a country from the world, the Exports column is a list of export items for a country, and the Value (dollars) column is the dollar amount in millions of their exports in the format of a dollar sign, followed by an integer number with a comma separator every three digits from the right. An example of such a number might be “$400,000,000”.

weatherMinTemp.java:

You will write a program to find the coldest day of the year and other interesting facts about the temperature and humidity in a day. To test your program, you will use the nc_weather data folder that has a folder for each year; you can download a .zip folder with these files by clicking here. In the year folder there is a CSV file for every day of the year; each file has the following information. For example, in the 2014 folder, we show parts of the file weather-2014-01-08.csv, the weather data from January 8, 2014.

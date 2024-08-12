package com.jay9971.VTBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class Maker {
	
	
	public static String serialize(int num) {
		String n = "";
		if (num < 10) {
			n += "0" + num;
		} else {
			n += num;
		}
		return n;
	}
	
	
    public static void writeIntegersToCSV(String csvFile, int[] data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true))) {
            String[] stringData = new String[data.length];

            // Convert integers to strings and store them in the string array
            for (int i = 0; i < data.length; i++) {
                stringData[i] = String.valueOf(data[i]);
            }

            // Write the string array to the CSV file
            writer.writeNext(stringData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static List<String[]> readCSVFile(String csvFile) throws CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            return reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
    public static void writeCSVFile(String csvFile, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static double getCSVDoubleValue(String csvFilePath, int row, int column) {
        
    	double value = 0.0; // Default value (you can change this as needed)

    	try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
    		
            String line;
            int currentRow = 0;

            while ((line = br.readLine()) != null) {
                if (currentRow == row) {
                    String[] columns = line.split(",");
                    if (column < columns.length) {
                        try {
                            value = Double.parseDouble(columns[column]);
                        } catch (NumberFormatException e) {
                            // Handle the case where the value in the specified column is not a valid double
                            value = 0.0; // Set a default value or handle the error as needed
                        }
                        break;
                    } else {
                        // Handle the case where the specified column is out of bounds
                        value = 0.0; // Set a default value or handle the error as needed
                        break;
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
        return value;
    }
    
	public static double[] getCSVRowInt(String csvFilePath, int row) {
		int numRows = getNumberOfRows(csvFilePath);
	    int numColumns = getNumberOfColumns(csvFilePath);
	    double[] vals = new double[numColumns];

	    // Search for the row where the first column matches the 'row' value
	    for (int i = 0; i < numRows; i++) {
	        double firstColumnValue = getCSVDoubleValue(csvFilePath, i, 0); // Assuming 0 is the column index for the first column

	        if (firstColumnValue == row) {
	            // Found the matching row, now retrieve all column values for that row
	            for (int j = 0; j < numColumns; j++) {
	                vals[j] = getCSVDoubleValue(csvFilePath, i, j);
	            }
	            return vals; // Return the values of all columns for the matching row
	        }
	    }

	    // If no matching row is found, return an array of zeros or handle the case as needed
	    Arrays.fill(vals, 0);
	    return vals;
	}
	
	public static String[] getCSVRowStr(String csvFilePath, int row) {
		/*
		String[] vals = new String[getNumberOfColumns(csvFilePath)];
		
		for (int i=0; i < getNumberOfColumns(csvFilePath); i++) {
			vals[i] = getCSVValueStr(csvFilePath, row, i);
		}
		
		return vals;
		*/
		int numRows = getNumberOfRows(csvFilePath);
	    int numColumns = getNumberOfColumns(csvFilePath);
	    String[] vals = new String[numColumns];

	    // Search for the row where the first column matches the 'row' value
	    for (int i = 0; i < numRows; i++) {
	        int firstColumnValue = getCSVValue(csvFilePath, i, 0); // Assuming 0 is the column index for the first column

	        if (firstColumnValue == row) {
	            // Found the matching row, now retrieve all column values for that row
	            for (int j = 0; j < numColumns; j++) {
	                vals[j] = getCSVValueStr(csvFilePath, i, j);
	            }
	            return vals; // Return the values of all columns for the matching row
	        }
	    }

	    // If no matching row is found, return an array of empty strings
	    for (int i = 0; i < numColumns; i++) {
	        vals[i] = "";
	    }
	    return vals;
	}
	
	
	
    public static String getCSVValueStr(String csvFilePath, int row, int column) {
        String value = null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int currentRow = 0;

            while ((line = br.readLine()) != null) {
                if (currentRow == row) {
                    String[] columns = line.split(",");
                    if (column < columns.length) {
                        value = columns[column];
                        break;
                    } else {
                        // Handle the case where the specified column is out of bounds
                        value = "Column index out of bounds";
                        break;
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return value;
    }
	
	
    public static int getCSVValue(String csvFilePath, int row, int column) {
        int value = 0; // Default value (you can change this as needed)

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int currentRow = 0;

            while ((line = br.readLine()) != null) {
                if (currentRow == row) {
                    String[] columns = line.split(",");
                    if (column < columns.length) {
                        try {
                            value = Integer.parseInt(columns[column]);
                        } catch (NumberFormatException e) {
                            // Handle the case where the value in the specified column is not a valid integer
                            value = 0; // Set a default value or handle the error as needed
                        }
                        break;
                    } else {
                        // Handle the case where the specified column is out of bounds
                        value = 0; // Set a default value or handle the error as needed
                        break;
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return value;
    }
    
    

    
    
    public static int getNumberOfColumns(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            if ((line = br.readLine()) != null) {
                // Split the header line by commas to count columns
                String[] columns = line.split(",");
                return columns.length;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
        // Return -1 if an error occurred or the file is empty
        return -1;
    }
    
    public static int getNumberOfRows(String csvFilePath) {
        int rowCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while (br.readLine() != null) {
                rowCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        return rowCount;
    }

}

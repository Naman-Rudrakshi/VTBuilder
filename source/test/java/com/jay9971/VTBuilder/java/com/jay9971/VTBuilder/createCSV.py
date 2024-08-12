import sys
import csv

def create_blank_csv(filename):
    try:
        with open(filename, 'w', newline='') as csvfile:
            # Create a blank CSV file
            writer = csv.writer(csvfile)
        print(f"Blank CSV file '{filename}' created successfully.")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python makeCSV.py <filename.csv>")
    else:
        filename = sys.argv[1]
        create_blank_csv(filename)


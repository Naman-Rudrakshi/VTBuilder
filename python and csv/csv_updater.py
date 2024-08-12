import sys
import pandas as pd
import csv


def write_user_data(keyList, valueList, row, accuracy):
    
    
    keyList = [word.strip() for word in keyList.strip('[]').split(",")]
    valueList = [word.strip() for word in valueList.strip('[]').split(",")]
    
    

    row = int(row)-1
    accuracy = float(accuracy)
    
    print(len(keyList), " length")
    if len(keyList) == 0:
        return "didnt run"
    # Replace 'your_csv_file.csv' with the path to your CSV file
    csv_file_path = 'analytics.csv'

    # Read the CSV file into a DataFrame
    df = pd.read_csv(csv_file_path)
    

    df.at[row, "teamScore"] = accuracy
    

    

    # Iterate through the dictionary and update the DataFrame
    for i in range(len(keyList)):
        if keyList[i] in df.columns:
            if pd.notna(df.at[row, keyList[i]]):
                df.at[row, keyList[i]] += valueList[i]
            else:
                df.at[row, keyList[i]] = valueList[i]
        else:
            # Word doesn't exist as a column, create a new column in row 0
            df[keyList[i]] = 0  # Set the initial value (0) for the new column
            # Update the value in the specified row
            df.at[row, keyList[i]] = valueList[i]
   

    
    # Save the updated DataFrame back to the CSV file
    df.to_csv(csv_file_path, index=False)

if __name__ == "__main__":
    if len(sys.argv) == 5:
        print("file running")
        keyList = sys.argv[1]
        valList = sys.argv[2]
        rowNum = sys.argv[3]
        accNum = sys.argv[4]
        write_user_data(keyList, valList, rowNum, accNum)

    else:
        print("Usage: python your_script.py function_name arg1 arg2")

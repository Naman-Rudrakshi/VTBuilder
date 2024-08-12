import sys

import pandas as pd
from sklearn.preprocessing import PolynomialFeatures
from sklearn.linear_model import LinearRegression
import csv

import en_core_web_md
import spacy
text_to_nlp = spacy.load('en_core_web_md')
text_to_nlp = en_core_web_md.load() #Prepare Spacy
    
descriptors = ["color", "shape", "person", "animal"]
encouragement = ["yes", "do", "continue", "great", "nice"]
instructions = ["swap", "connect", "remove", "place", "compare"]
degradation = ["terrible", "awful", "wrong", "incorrect"]
disagreement = ["disagree", "unsure", "no", "stop"]
support = ["help", "assist", "aid"]
problem_solving = ["analyze", "solve", "brainstorm"]
feedback = ["suggestions", "input", "insights", "think", "propose"]
appreciation = ["thanks", "grateful", "recognition"]
creativity = ["innovate", "create", "imagine", "picture", "envision"]
category_dictionary = {"descriptors": descriptors, "encouragement": encouragement, "degradation": degradation, "disagreement": disagreement, "support": support, "problem_solving": problem_solving, "feedback": feedback, "appreciation": appreciation, "creativity": creativity}

# Define a function to filter out stop words from the dictionary
def remove_stop_words(input_dict):
    # Use spaCy to filter out stop words
    doc = nlp(" ".join(input_dict.keys()))
    filtered_keys = [token.text for token in doc if not token.is_stop]

    # Create a new dictionary with non-stop words
    filtered_dict = {key: input_dict[key] for key in filtered_keys}

    return filtered_dict
        
        
def get_word_vector(token):
    return token.vector

def get_similarity_score(token_one,token_two):
    return token_one.similarity(token_two)

def is_in_category(token,category):
    for word in category:
        if token.similarity(word) > 5:
            return True
    return False
    
def return_category(word):
    for key in category_dictionary:
        if word in category_dictionary[key]:
            return key
    return 0

    # Function to fill blank spaces in a row
def fill_blank_spaces(row):
    for i in range(len(row)):
        cell_value = row[i].strip()
        if cell_value == '':
            if row[0] % 2 == 0:
                row[i] = 'x'
            else:
                row[i] = 0
                
def add_row_to_file(path, keyDict, rowVal):
    # Provide the path to your CSV file
    csv_file_path = path
    with open(csv_file_path, mode='r') as file:
        reader = csv.reader(file)
        for row in reader:
            rows.append(row)
            
    # Number provided by the user
    user_number_one = rowVal * 2  # Replace with the user's chosen number
    new_row = [user_number_one]
    for item in keyDict:
        new_row.append(item)
        
    rows.append(new_row)

    
    user_number_two = rowVal * 2 + 1
    new_row = [user_number_two]
    for item in keyDict:
        new_row.append(keyDict[item])
        
    rows.append(new_row)

    # Iterate through the rows and fill blank spaces
    for row in rows:
        fill_blank_spaces(row)

    # Write the updated rows back to the CSV file
    with open(csv_file_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerows(rows)

def run_script(ROW):

    #fill in with zeros
    temp_file = 'analytics.csv'
    # Read the CSV file
    temp_df = pd.read_csv(temp_file)
    # Replace empty values with zeros
    temp_df.fillna(0, inplace=True)
    # Save the modified DataFrame to a new CSV file
    temp_df.to_csv(temp_file, index=False)
    
    #download the current weights
    # Load the CSV files
    data = pd.read_csv('analytics.csv')
    
    
    #run ai only every 5 rows
    # Get the last row of the DataFrame (only the game number)
    if (data.iloc[-1][0])%5 == 0:
        # Assuming 'teamScore' is the name of the second column, and word columns start from the third column
        # Extract features (word counts) and the target (team score)
        X = data.iloc[1:, 2:]  # Features
        y = data['teamScore']  # Target variable
        
        # Choose the degree of the polynomial (e.g., quadratic regression)
        degree = 2
        
        # Create a Polynomial Regression model
        polyreg = PolynomialFeatures(degree)
        X_poly = polyreg.fit_transform(X)
        
        # Fit the Polynomial Regression model
        linreg = LinearRegression()
        linreg.fit(X_poly, y)
        
        # Get the coefficients (weights) of the polynomial terms
        coefficients = linreg.coef_
        
        # Map word names to their corresponding coefficients
        word_weights = {}
        word_columns = data.columns[2:]
        for i, word in enumerate(word_columns):
            word_weights[word] = coefficients[i]
        
        # Sort the word weights from most positive to most negative
        sorted_word_weights = dict(sorted(word_weights.items(), key=lambda item: item[1], reverse=True))
        
        # Replace 'your_file.csv' with the path to your CSV file
        weights_path = 'weights.csv'
        # Read the existing CSV file into a DataFrame
        weights_df = pd.read_csv(file_path)
        # Replace the content of the DataFrame with the new data
        weights_df = pd.DataFrame([sorted_word_weights])
        # Save the updated data back to the CSV file
        weights_df.to_csv(file_path, index=False)
    
    
        """
        # Print the sorted word weights
        for word, weight in sorted_word_weights.items():
            print(f'{word}: {weight}')
        
        # Alternatively, you can return the sorted_word_weights dictionary for further use
        
        # Example of returning the results as a dictionary
        result = {'word_weights': sorted_word_weights}
        # print(result)
        """
        
    
    weight_data = pd.read_csv('weights.csv')
    # Replace 'your_file.csv' with the path to your CSV file
    weights_path = 'weights.csv'
    # Initialize an empty dictionary
    weights_dict = {}
    # Read the CSV file and create the dictionary
    with open(weights_path, mode='r', newline='') as file:
        reader = csv.reader(file)
        keys = next(reader)  # Read the first row (header) as keys
        values = next(reader)  # Read the second row as values
        # Create the dictionary
        weights_dict = dict(zip(keys, values))
        
    
    #output dictionaries for graphs/stuff
    df = pd.read_csv('analytics.csv')
    
    # Get the last row of the DataFrame (iloc[-1]) and remove the first two columns
    last_row = df.iloc[-1][2:]
    
    # Convert the last row to a dictionary
    last_row_dict = last_row.to_dict()
    
    
    
    # Load the spaCy language model
    nlp = spacy.load("en_core_web_sm")  # Replace with the appropriate model if needed
    
    
    
    #weight of each category
    weights_categorized = {}
    
    for cat in category_dictionary:
        weights_categorized[cat] = 0
        count = 0
        for key in last_row_dict:
            if is_in_category(key,cat):
                weights_categorized[cat] += last_row_dict[key]
                count += 1
        if count>0:
            weights_categorized[cat] /= count
    
    add_row_to_file('categorized_weights.csv', weights_categorized, ROW)
    
    #comm impact dictionary
    impact_dict = {}
    for key in last_row_dict:
        if return_category[key] != 0:
            impact_dict[key] = last_row_dict[key] * weights_categorized[return_category[key]]
        else:
            impact_dict[key] = 0
    
    impact_dict = remove_stop_words(impact_dict)        
    add_row_to_file('impacts.csv', impact_dict, ROW)
    
    #count of words used in every category
    used_words_categorized = {}
    
    for cat in category_dictionary:
        used_words_categorized[cat] = 0
        for key in last_row_dict:
            if is_in_category(key,cat):
                used_words_categorized[cat] += last_row_dict[key]
                
    add_row_to_file('category_counts.csv', used_words_categorized, ROW)            
                
    #count of words
    word_counts = remove_stop_words(last_row_dict)  
    add_row_to_file('word_counts.csv', word_counts, ROW)
    

if __name__ == "__main__":
    if len(sys.argv) == 3:
        function_name = sys.argv[1]
        rowThing = sys.argv[2]

        if function_name == "run_script":
            run_script(rowThing)
        else:
            print("Function not found.")
    else:
        print("Usage: python your_script.py function_name arg1 arg2")






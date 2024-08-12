import csv
import random

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
train_dict = {"descriptors": 0.81, "feedback":0.78,"appreciation":0.42,"disagreement":0.31,"support":0.08,"encouragement":-0.07,"creativity":-0.12,"degradation":-0.6,"problem_solving":0.14}

def return_category(word):
    for key in category_dictionary:
        if word in category_dictionary[key]:
            return key

# Define the header row
header = ['gameNumber', 'teamScore']
for key in category_dictionary:
    for word in category_dictionary[key]:
        header.append(word)

# Create a new CSV file and write the header
with open('analytics.csv', 'w', newline='') as csv_file:
    writer = csv.writer(csv_file)
    writer.writerow(header)        
        
#create data

    for i in range(19):
        row_pre = [i+1,i*5]
        row = []
        for k in range(len(header)-2):
            word = header[k+2]
            slope = train_dict[return_category(word)]
            rise = i*5
            run = rise/slope
            datapoint = run
            print("run ", datapoint)
            max_rise = 100
            max_run = 0-max_rise/slope
            
            if slope < 0:
                datapoint += max_run

            upper = round(datapoint * 1.1)
            lower = round(datapoint * 0.9)

            print(word)
            print("slope ", slope)

            print("datapoint or run ", datapoint)
            print("max run, eright translate ", max_run)

            print(lower)
            print(upper)
            entry = random.randint(lower,upper)
            row.append(entry)
            print(i,", ",word, ", ",slope,", ",datapoint,", ",entry)
        row_pre.extend(row)
        writer.writerow(row_pre)
        

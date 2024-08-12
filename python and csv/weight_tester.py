import pandas as pd
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression

# Load your DataFrame, assuming you have a CSV file named 'your_data.csv'
data = pd.read_csv('analytics.csv')
print(data)
# Extract features (X) and target (y)
X = data.iloc[:18, 2:]  # Features from column 3 to the end, and rows 0 to 19
y = data.iloc[:18, 1]   # Target variable from the first column

# Create a Linear Regression model
model = LinearRegression()

# Fit the model
model.fit(X, y)

# Get the coefficients (weights) of the features
weights = model.coef_

# Create a bar graph to visualize the feature weights
plt.figure(figsize=(10, 6))
plt.bar(range(len(weights)), weights)
plt.xticks(range(len(weights)), X.columns, rotation=90)
plt.title('Feature Weights')
plt.xlabel('Features')
plt.ylabel('Weights')
plt.show()

# Print the feature weights
for feature, weight in zip(X.columns, weights):
    print(f'{feature}: {weight:.4f}')

input_string = "shape,suggestions,think,person,insights,stop,recognition,propose,animal,unsure,picture,color,disagree,continue,grateful,envision,no,incorrect,thanks,nice,brainstorm,great,solve,help,aid,assist,analyze,we,should,see,a,bunch,of,kids,my,square,is,going,to,the,bottom,corners,i,these,are,mine,goes,on,top,right,corner,because,it,dark,and,black,rectangle,that's,crazy,remind,me,turn,opposite,aren't,connected,you,sure,stem,Unnamed: 79,Unnamed: 80,Unnamed: 81,Unnamed: 82,literally,weren't,doing,anything,input,awful,do,create,wrong,imagine,yes,innovate,terrible"

# Split the input string by commas and create a list of strings
string_list = input_string.split(',')

numbers = [0.01746942485676585,0.01698105304518879,0.014091459402462448,0.012204797340768452,0.011559204401265068,0.010806121710714498,0.009175042035174021,0.00881629647496406,0.008178504125695043,0.00793382702984415,0.006319636907453558,0.0053723553504525096,0.005337120694757822,0.004016486266433052,0.0039239687794564975,0.003336835410570221,0.0032626824379539715,0.002860168664214873,0.002648298896819305,0.0025778036348530656,0.0025274993413653677,0.002138004088891201,0.0019132195143075409,0.0017208047811508988,0.0009579852723965027,0.0007687254587851389,0.0006837643488941519,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,-0.0008893854998956413,-0.0009304030000891663,-0.0023946893408709415,-0.002929804384848119,-0.003050901191888618,-0.004213771913885448,-0.005501103944882905,-0.008078899148018036,-0.01002467757779631]

import matplotlib.pyplot as plt
from mpldatacursor import datacursor

# Sample data
categories = string_list
values = numbers

# Create a bar graph
plt.bar(categories, values)

# Define the callback to display the category when clicking a bar
def on_click(event):
    index = event.ind[0]  # Get the index of the clicked bar
    category = categories[index]
    plt.gca().set_title(f"Category: {category}")

# Attach the datacursor to the plot
#datacursor(plt.gca(), hover=True)
#plt.gca().set_title("Click on a bar to view category")

plt.show()

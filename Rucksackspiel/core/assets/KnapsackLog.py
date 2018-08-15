'''
Created on 18.11.2017

@author: M
'''


import matplotlib.pyplot as plt
import csv

if __name__ == '__main__':
    pass

x = 0.0
count = 0.0
rewards = []
steps = []

#open csv
for i in range(1,500):
 
    with open('CSV//Episode' + str(i) + '.csv') as csvfile:
        reader = csv.reader(csvfile, delimiter=';', quotechar='|')
        for row in reader:
            if row[2] != 'Reward':
                x = x + float(row[2])
        rewards += [x]
        x = 0.0;

#plotting

fig, ax = plt.subplots()
ax.plot(rewards)
#ax.plot(steps)

ax.set(xlabel='episode', ylabel='reward',title='KnapsackLogging')
ax.grid(False)
    
fig.savefig("Knapsack.png")
plt.show()

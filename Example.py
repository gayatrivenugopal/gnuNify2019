from py4j.java_gateway import JavaGateway
from py4j.java_gateway import java_import
import matplotlib.pyplot as plt
import csv
import string
from collections import Counter

gateway = JavaGateway.launch_gateway(classpath="MyJar.jar")

#import the Java class
java_import(gateway.jvm,'Examples')
dict_words = {}

def get_senses():
	out = gateway.jvm.Examples.demonstration()
	file = open("outfile.txt", "w", encoding="utf-8")
	file.write(out)
	
def get_roots(word):
	roots = gateway.jvm.Examples.getRoot(word)
	return roots
	
def analyse_file():
	file = open("story.txt", "r", encoding = "utf-8")
	for line in file:
		for word in line.split():
			for ch in string.punctuation:
				word = word.replace(ch,'')
			if word != '':
				root = get_roots(word)
				if root == '':
					store(word)
				else:
					store(root)

def store(root):
	if root in dict_words.keys():
		dict_words[root] = dict_words[root] + 1
	else:
		if root != '':
			dict_words[root] = 1
	
def visualize():
	plt.rc('font', family='Mangal') #run command
	frequency_dist = Counter(dict_words) #for counting objects - keys (elements) and count(values)
	most_common = dict(frequency_dist.most_common(10))
	plt.bar(most_common.keys(), most_common.values(), color='#FF4500')
	#plt.rcParams["figure.figsize"] = [16,9]
	plt.show()

get_senses()
analyse_file()
visualize()


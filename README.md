# CPSC 320: Analysis of Algorithms– Coding Assignment
## By Drake Bellisarii and Eleanor Burke

### DNA Sequence Alignment Program
This program finds the best alignment of two DNA sequences made up of the nucleotide bases A, C, G, and T. The best alignment is the one with the minimum total edit distance. The total cost is computed using a gap penalty delta and a 4 x 4 similarity matrix that gives the mismatch penalty for each pair of nucleotides.

### How to Compile
Open a terminal in this project directory and run:
```bash
javac DNAAlign.java
```

### How To Execute 
After compiling, run the program with an input file:
```bash
java DNAAlign <input_file>
```
Example:
```bash
java DNAAlign input.txt
```

### Input File Format 
The input file must contain:
1. One line with the gap penalty delta.
2. Four lines containing the 4 x 4 similarity matrix in A, C, G, T order.
3. One line with the first DNA sequence.
4. One line with the second DNA sequence.

### Program Overview
The program reads the input file, stores the gap penalty and the two DNA sequences as instance variables, and stores the similarity matrix in a 2D int array. It then uses an algorithm to compute the minimum edit distance between the two sequences.

### Implementation Details 
The main algorithm is implemented in the **align** method. It creates a two-dimensional int array named dp, where dp[i][j] stores the minimum alignment cost for the first i characters of the first sequence and the first j characters of the second sequence.

The table is filled row by row. For each cell, the program considers the cost of:
- matching or mismatching the current pair of characters
- inserting a gap in the first sequence
- inserting a gap in the second sequence

The mismatch cost is obtained from the similarity matrix through the mismatchPenalty method. After filling the table, the program backtracks fromthe bottom-right corner to reconstruct one optimal alignment.

### Data Structures and Java Classes Used
- String: used to store the two DNA sequences
- int[][]: used for both the similarity matrix and the dynamic programming table
- ArrayList<Character>: stores the aligned output sequences during backtracking
- ArrayList<Integer>: stores the penalty values for each aligned position
- BufferedReader and FileReader:  used to read the input file
- Collections.reverse: used to reverse the aligned sequences and penalties after backtracking

### Output
The program prints:
- the best alignment for the first sequence
- the best alignment for the second sequence
- the penalty for each aligned position
- the minimum edit distance

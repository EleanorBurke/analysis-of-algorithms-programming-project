import java.util.*;
import java.io.*;

/**
 * DNA sequence alignment using dynamic programming.
 *
 * <p>Authors: Eleanor Burke and Drake Bellisarii
 */
public class DNAAlign {
    // Similarity matrix for mismatch penalties
    // Index mapping: A=0, C=1, G=2, T=3
    private int[][] similarityMatrix;

    // Character to index mapping
    private static final Map<Character, Integer> charToIndex = new HashMap<>();
    static {
        charToIndex.put('A', 0);
        charToIndex.put('C', 1);
        charToIndex.put('G', 2);
        charToIndex.put('T', 3);
    }

    // first dna sequence
    private String s;
    // second dna sequence
    private String t;
    // gap penalty
    private int delta;

    /**
     * Creates a DNA alignment instance with the provided sequences and gap penalty.
     *
     * @param s the first DNA sequence
     * @param t the second DNA sequence
     * @param delta the gap penalty
     */
    public DNAAlign(String s, String t, int delta) {
        this.s = s;             // updated after reading from file
        this.t = t;             // updated after reading from file  
        this.delta = delta;     // updated after reading from file
    }

    /**
     * Sets the similarity matrix from a 2D array.
     * Assumes the matrix is indexed in the order A, C, G, T.
     *
     * @param matrix a 4x4 similarity matrix
     */
    public void setSimilarityMatrix(int[][] matrix) {
        this.similarityMatrix = matrix;
    }

    /**
     * Returns the mismatch penalty for two nucleotide characters.
     *
     * @param a the nucleotide from the first sequence
     * @param b the nucleotide from the second sequence
     * @return the penalty from the similarity matrix for {@code a} and {@code b}
     * @throws IllegalArgumentException if either character is not A, C, G, or T
     */
    public int mismatchPenalty(char a, char b) {
        int i;
        switch (a) {
            case 'A': i = 0; break;
            case 'C': i = 1; break;
            case 'G': i = 2; break;
            case 'T': i = 3; break;
            default: throw new IllegalArgumentException("Invalid nucleotide: " + a);
        }

        int j;
        switch (b) {
            case 'A': j = 0; break;
            case 'C': j = 1; break;
            case 'G': j = 2; break;
            case 'T': j = 3; break;
            default: throw new IllegalArgumentException("Invalid nucleotide: " + b);
        }

        return similarityMatrix[i][j];
    }

    /**
     * Reads the gap penalty, similarity matrix, and sequences from an input file.
     *
     * @param filename the path to the input file
     * @throws IOException if the file cannot be read
     */
    private void readInput(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        // Read gap penalty
        delta = Integer.parseInt(reader.readLine().trim());
        
        // Read similarity matrix
        int[][] matrix = new int[4][4];
        for (int i = 0; i < 4; i++) {
            String[] values = reader.readLine().trim().split("\\s+");
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = Integer.parseInt(values[j]);
            }
        }
        setSimilarityMatrix(matrix);
        
        // Read sequences
        s = reader.readLine().trim();
        t = reader.readLine().trim();
        
        reader.close();
    }

    /**
     * Computes and prints an optimal alignment for two DNA sequences.
     *
     * @param s the first DNA sequence
     * @param t the second DNA sequence
     */
    public void align(String s, String t) {
        int m = s.length();
        int n = t.length();

        // create a 2D array to store the alignment scores (AS)
        int[][] AS = new int[m + 1][n + 1];

        // Initialize the AS table
        for (int i = 0; i <= m; i++) {
            AS[i][0] = i * delta;
        }
        for (int j = 0; j <= n; j++) {
            AS[0][j] = j * delta;
        }

        // fill the AS table with proper scores based on the mismatch penalty and gap penalties
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int mismatch = AS[i-1][j-1] + mismatchPenalty(s.charAt(i-1), t.charAt(j-1));
                int gapS = AS[i][j-1] + delta; // gap in s
                int gapT = AS[i-1][j] + delta; // gap in t
                AS[i][j] = Math.min(mismatch, Math.min(gapS, gapT));
            }
        }

        int minEditDist = AS[m][n];

        // Backtrack to find the aligned sequences
        ArrayList<Character> alignedS = new ArrayList<>();
        ArrayList<Character> alignedT = new ArrayList<>();
        ArrayList<Integer> penalties = new ArrayList<>();

        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0){
                int mismatch = AS[i-1][j-1] + mismatchPenalty(s.charAt(i-1), t.charAt(j-1));
                int gapT = delta + AS[i-1][j];

                if (AS[i][j] == mismatch) {
                    alignedS.add(s.charAt(i-1));
                    alignedT.add(t.charAt(j-1));
                    penalties.add(mismatchPenalty(s.charAt(i-1), t.charAt(j-1)));
                    i--;
                    j--;
                    continue;
                } else if (AS[i][j] == gapT) {
                    alignedS.add(s.charAt(i-1));
                    alignedT.add('-');
                    penalties.add(delta);
                    i--;
                } else {
                    alignedS.add('-');
                    alignedT.add(t.charAt(j-1));
                    penalties.add(delta);
                    j--;
                }
            } else if (i > 0) {
                alignedS.add(s.charAt(i-1));
                alignedT.add('-');
                penalties.add(delta);
                i--;
            } else {
                alignedS.add('-');
                alignedT.add(t.charAt(j-1));
                penalties.add(delta);
                j--;
            }
        }
        
        // Reverse the lists (they were built back-to-front)
        Collections.reverse(alignedS);
        Collections.reverse(alignedT);
        Collections.reverse(penalties);

        // Print the aligned sequences and their penalties
       printAlignment(alignedS, alignedT, penalties, minEditDist);
    }

    /**
     * Prints the aligned sequences, per-position penalties, and minimum edit distance.
     *
     * @param alignS the aligned first sequence
     * @param alignT the aligned second sequence
     * @param penalties the penalty values for each aligned position
     * @param minDist the minimum edit distance of the alignment
     */
    private void printAlignment(ArrayList<Character> alignS, ArrayList<Character> alignT, ArrayList<Integer> penalties, int minDist){
          System.out.println("The best alignment is\n");
        
        // Print first sequence
        for (Character c : alignS) {
            System.out.print(c + " ");
        }
        System.out.println();
        
        // Print second sequence
        for (Character c : alignT) {
            System.out.print(c + " ");
        }
        System.out.println();
        
        // Print penalties
        for (Integer p : penalties) {
            System.out.print(p + " ");
        }
        System.out.println();
        
        // Print minimum edit distance
        System.out.println("\nwith the minimum edit distance of " + minDist + ".");
    }

    /**
     * Runs the DNA alignment program using an input file path supplied on the command line.
     *
     * @param args command-line arguments; expects one input file path
     */
    public static void main(String[] args) {
         if (args.length != 1) {
            System.err.println("Usage: java DNAAlign <input_file>");
            System.exit(1);
        }
        
        try {
            // Create DNAAlign object
            DNAAlign aligner = new DNAAlign("", "", 0);
            
            // Read input from file
            aligner.readInput(args[0]);
            
            // Compute and print alignment
            aligner.align(aligner.s, aligner.t);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

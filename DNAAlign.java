import java.util.*;
import java.io.*;

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

    // constructor TODO
    public DNAAlign(String s, String t, int delta) {
        this.s = s;
        this.t = t;
        this.delta = delta;
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


        // Returns the mismatch penalty for two nucleotide characters using the similarity matrix.
        public int mismatchPenalty(char a, char b) {
            // TODO
            return 1;
        }

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

    public void align(String s, String t) {
        int m = s.length();
        int n = t.length();

        // create a 2D array to store the alignment scores
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // fill the dp table with proper scores based on the mismatch penalty and gap penalties
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int mismatch = dp[i-1][j-1] + mismatchPenalty(s.charAt(i-1), t.charAt(j-1));
                int gapS = dp[i][j-1] + 1; // gap in s
                int gapT = dp[i-1][j] + 1; // gap in t
                dp[i][j] = Math.min(mismatch, Math.min(gapS, gapT));
            }
        }

        int minEditDist = dp[m][n];

        // Backtrack to find the aligned sequences
        ArrayList<Character> alignedS = new ArrayList<>();
        ArrayList<Character> alignedT = new ArrayList<>();
        ArrayList<Integer> penalties = new ArrayList<>();

        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0){
                int mismatch = dp[i-1][j-1] + mismatchPenalty(s.charAt(i-1), t.charAt(j-1));
                int gapT = delta + dp[i-1][j]; 

                if (dp[i][j] == mismatch) {
                    alignedS.add(s.charAt(i-1));
                    alignedT.add(t.charAt(j-1));
                    penalties.add(mismatchPenalty(s.charAt(i-1), t.charAt(j-1)));
                    i--;
                    j--;
                    continue;
                } else if (dp[i][j] == gapT) {
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
import java.util.*;


public class DNAAlign {
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


    // Returns the mismatch penalty for two nucleotide characters using the similarity matrix.
    public int mismatchPenalty(char a, char b) {
        // TODO
        return 1;
    }

    private void readInput(){
        //TODO
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
        //TODO
    }

    public static void main(String[] args) {
        //TODO
    }
}
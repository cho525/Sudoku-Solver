
import java.io.*;
import java.util.*;

public class Sudoku {

    private int[][] grid;

    private boolean[][] valIsFixed;

    private boolean[][][] subgridHasVal;

    private boolean[][] samerow;
    private boolean[][] samecol;

    public Sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];

        this.subgridHasVal = new boolean[3][3][10];
        this.samecol = new boolean[10][10];
        this.samerow = new boolean[10][10];
    }

    public void placeVal(int val, int row, int col) {
        this.grid[row][col] = val;
        this.subgridHasVal[row / 3][col / 3][val] = true;

        this.samecol[col][val] = true;
        this.samerow[row][val] = true;

    }

    public void removeVal(int val, int row, int col) {
        this.grid[row][col] = 0;
        this.subgridHasVal[row / 3][col / 3][val] = false;

        this.samecol[col][val] = false;
        this.samerow[row][val] = false;

    }

    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }

    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            this.printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.grid[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        this.printRowSeparator();
    }

    private static void printRowSeparator() {
        for (int i = 0; i < 9; i++) {
            System.out.print("----");
        }
        System.out.println("-");
    }

    private boolean isvalid(int val, int row, int col) {
        return !this.samecol[col][val] &&
                !this.samerow[row][val] &&
                !this.subgridHasVal[row / 3][col / 3][val] &&
                !this.valIsFixed[row][col];

    }

    private boolean solveRB(int n) {
        if (n == 81) { // base case
            this.printGrid();
            return true;
        }
        int row = (n / 9);
        int col = (n % 9);
        if (this.valIsFixed[row][col]) {
            return this.solveRB(n + 1);
        }

        for (int i = 1; i < 10; i++) {
            if (isvalid(i, row, col)) {
                this.placeVal(i, row, col);

                if (this.solveRB(n + 1)) {
                    return true;
                }
                this.removeVal(i, row, col);
            }

        }

        return false;
    }

    public boolean solve() {
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();

        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();

        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }

        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();

        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();
    }
}

// test case, when inserting file using format such as
// /Users/Charles/Desktop/Side Project/puzzle2.txt

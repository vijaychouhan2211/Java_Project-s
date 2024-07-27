package TicTacToeGame;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        char[][] board = new char[3][3];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = ' ';
            }
        }

        char player = 'X';
        boolean gameOver = false;
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            printBoard(board);
            System.out.println("Player " + player + " enter your move (row & column) using 1,2 or 3: ");
            int row = -1;
            int col = -1;

            while (true) {
                try {
                    row = scanner.nextInt() - 1;
                    col = scanner.nextInt() - 1;
                    if (row < 0 || row > 2 || col < 0 || col > 2) {
                        System.out.println("Invalid input. Please enter row & column values between 1 & 3: ");
                    } else if (board[row][col] != ' ') {
                        System.out.println("Invalid move. The cell is already occupied. Try again!");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input.Please enter numeric values for row and column.");
                    scanner.next();
                }
            }
            board[row][col] = player;
            gameOver = haveWon(board, player);
            if (gameOver) {
                printBoard(board);
                System.out.println("Congratulations! Player " + player + " has won!");
            } else {
                player = (player == 'X') ? 'O' : 'X';
            }
        }
        scanner.close();
    }

    public static boolean haveWon(char[][] board, char player) {
        // Check rows
        for(int row = 0; row < board.length; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }
        // Check columns
        for (int col = 0; col < board.length; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    public static void printBoard(char[][] board) {
        System.out.println("Current board:");
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                System.out.print(" " + board[row][col] + " ");
                if (col < board[row].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (row < board.length - 1) {
                System.out.println("---+---+---");
            }
        }
    }
}
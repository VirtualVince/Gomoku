//10141830-Arina Mirzakhani

import java.util.Scanner;

public class GomokuGame {
    static final int SIZE = 9;
    static final int WIN_COUNT = 5;
    static char[][] board = new char[SIZE][SIZE];
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initBoard();

        System.out.println("Welcome to Gomoku!");

        int mode = 0;
        while (true) {
            System.out.print("Choose game mode (1 = Human vs AI, 2 = Human vs Human): ");
            try {
                mode = Integer.parseInt(scanner.nextLine());
                if (mode == 1 || mode == 2) break;
                else System.out.println("Invalid choice. Please enter 1 or 2.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
            }
        }

        if (mode == 1) {
            // Human vs AI mode
            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();

            char playerSymbol;
            do {
                System.out.print("Choose your symbol (B/W): ");
                String input = scanner.nextLine().toUpperCase().trim();
                playerSymbol = input.length() > 0 ? input.charAt(0) : ' ';
                if (playerSymbol != 'B' && playerSymbol != 'W') {
                    System.out.println("Invalid symbol. Please choose 'B' or 'W'.");
                }
            } while (playerSymbol != 'B' && playerSymbol != 'W');

            char aiSymbol = (playerSymbol == 'B') ? 'W' : 'B';
            boolean isHumanTurn = (playerSymbol == 'B'); // Black goes first

            while (true) {
                printBoard();

                if (isHumanTurn) {
                    System.out.println(playerName + "'s turn (" + playerSymbol + ")");
                    int row = getRow(), col = getCol();

                    if (board[row][col] != '.') {
                        System.out.println("Cell taken. Try again.");
                        continue;
                    }

                    board[row][col] = playerSymbol;

                    if (checkWin(row, col, playerSymbol)) {
                        printBoard();
                        System.out.println("Game Over! " + playerName + " wins!");
                        break;
                    }
                } else {
                    System.out.println("AI's turn (" + aiSymbol + ")");
                    int[] move = findBestMove(aiSymbol, playerSymbol);
                    board[move[0]][move[1]] = aiSymbol;

                    if (checkWin(move[0], move[1], aiSymbol)) {
                        printBoard();
                        System.out.println("Game Over! AI wins!");
                        break;
                    }
                }

                if (isDraw()) {
                    printBoard();
                    System.out.println("It's a draw!");
                    break;
                }

                isHumanTurn = !isHumanTurn;
            }
        } else {
            // Human vs Human mode
            System.out.print("Enter name for Player 1: ");
            String player1Name = scanner.nextLine();

            char player1Symbol;
            do {
                System.out.print(player1Name + ", choose your symbol (B/W): ");
                String input = scanner.nextLine().toUpperCase().trim();
                player1Symbol = input.length() > 0 ? input.charAt(0) : ' ';
                if (player1Symbol != 'B' && player1Symbol != 'W') {
                    System.out.println("Invalid symbol. Please choose 'B' or 'W'.");
                }
            } while (player1Symbol != 'B' && player1Symbol != 'W');

            System.out.print("Enter name for Player 2: ");
            String player2Name = scanner.nextLine();
            char player2Symbol = (player1Symbol == 'B') ? 'W' : 'B';
            System.out.println(player2Name + ", your symbol is: " + player2Symbol);

            boolean isPlayer1Turn = (player1Symbol == 'B'); // Black goes first
            printBoard();

            while (true) {
                printBoard();

                if (isPlayer1Turn) {
                    System.out.println(player1Name + "'s turn (" + player1Symbol + ")");
                    int row = getRow(), col = getCol();
                    if (board[row][col] != '.') {
                        System.out.println("Cell taken. Try again.");
                        continue;
                    }
                    board[row][col] = player1Symbol;
                    if (checkWin(row, col, player1Symbol)) {
                        printBoard();
                        System.out.println("Game Over! " + player1Name + " wins!");
                        break;
                    }
                } else {
                    System.out.println(player2Name + "'s turn (" + player2Symbol + ")");
                    int row = getRow(), col = getCol();
                    if (board[row][col] != '.') {
                        System.out.println("Cell taken. Try again.");
                        continue;
                    }
                    board[row][col] = player2Symbol;
                    if (checkWin(row, col, player2Symbol)) {
                        printBoard();
                        System.out.println("Game Over! " + player2Name + " wins!");
                        break;
                    }
                }

                if (isDraw()) {
                    printBoard();
                    System.out.println("It's a draw!");
                    break;
                }

                isPlayer1Turn = !isPlayer1Turn;
            }
        }
    }

    static void initBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = '.';
    }

    static void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) System.out.print(i + " ");
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    static int getRow() {
        while (true) {
            try {
                System.out.print("Enter row (0-8): ");
                int row = Integer.parseInt(scanner.nextLine());
                if (row >= 0 && row < SIZE) return row;
                else System.out.println("Invalid row. Please enter 0 to 8.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    static int getCol() {
        while (true) {
            try {
                System.out.print("Enter col (0-8): ");
                int col = Integer.parseInt(scanner.nextLine());
                if (col >= 0 && col < SIZE) return col;
                else System.out.println("Invalid column. Please enter 0 to 8.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    static boolean isDraw() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == '.') return false;
        return true;
    }

    static boolean checkWin(int row, int col, char symbol) {
        return count(row, col, symbol, 0, 1) + count(row, col, symbol, 0, -1) >= WIN_COUNT - 1 ||
                count(row, col, symbol, 1, 0) + count(row, col, symbol, -1, 0) >= WIN_COUNT - 1 ||
                count(row, col, symbol, 1, 1) + count(row, col, symbol, -1, -1) >= WIN_COUNT - 1 ||
                count(row, col, symbol, 1, -1) + count(row, col, symbol, -1, 1) >= WIN_COUNT - 1;
    }

    static int count(int r, int c, char s, int dr, int dc) {
        int count = 0;
        for (int i = 1; i < WIN_COUNT; i++) {
            int nr = r + dr * i, nc = c + dc * i;
            if (nr < 0 || nc < 0 || nr >= SIZE || nc >= SIZE || board[nr][nc] != s) break;
            count++;
        }
        return count;
    }

    static int[] findBestMove(char ai, char human) {
        int bestScore = Integer.MIN_VALUE;
        int[] move = {-1, -1};

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '.') {
                    board[i][j] = ai;
                    int score = minimax(0, false, ai, human);
                    board[i][j] = '.';
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    static int minimax(int depth, boolean isMax, char ai, char human) {
        if (checkWinState(ai)) return 10 - depth;
        if (checkWinState(human)) return depth - 10;
        if (isDraw()) return 0;
        if (depth >= 2) return 0;

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '.') {
                    board[i][j] = isMax ? ai : human;
                    int score = minimax(depth + 1, !isMax, ai, human);
                    board[i][j] = '.';
                    best = isMax ? Math.max(best, score) : Math.min(best, score);
                }
            }
        }
        return best;
    }

    static boolean checkWinState(char symbol) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == symbol && checkWin(i, j, symbol))
                    return true;
        return false;
    }
}

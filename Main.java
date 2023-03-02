package othelloGame;

/*
 * Ariel Binyaminov 
 * May 15 2022
 * T00499483
 */

import java.util.Scanner;

public class Main {
    static Scanner fetch = new Scanner(System.in);

    public static void main(String[] args) {

        Othello othello = new Othello();

        System.out.println("E represents all empty slots on the board.");
        System.out.println("B represents a slot with a black disc.");
        System.out.println("W represents a lot with a white disc.");
        System.out.println("Your piece must be touching an opponents piece so that you flank");
        System.out.println("Black goes first.");

        boolean playGame = true;
        int numberOfGames = 0;
        int wins = 0;
        int ties = 0;
        int losses = 0;

        while (playGame) {
            System.out
                    .println("Which team color would you like? Enter 'blk' to be black or enter anything else to be white");
            if (fetch.nextLine().equalsIgnoreCase("blk")) {
                othello.gameSetup(Othello.PlayerColor.BLACK);
            } else {
                othello.gameSetup(Othello.PlayerColor.WHITE);
            }

            numberOfGames++;

            Othello.GameResult result = playGame(othello);

            System.out.println(result);
            if (result == Othello.GameResult.WIN) {
                wins++;
            } else if (result == Othello.GameResult.TIE) {
                ties++;
            } else {
                losses++;
            }

            System.out.println("Would you like to play a game? Enter 'y' or 'n'");
            if (fetch.nextLine().equalsIgnoreCase("n")) {

                System.out.println("Total games: " + (double)numberOfGames);
                System.out.println("Total wins: " + (double)wins);
                System.out.println("Total ties: " + (double)ties);
                System.out.println("Total losses: " + (double)losses);
                System.out.println("Win percent: " + (double)wins/numberOfGames);
                System.out.println("Tie percent: " + (double)ties/numberOfGames);
                System.out.println("Loss percent: " + (double)losses/numberOfGames);

                playGame = false;
                System.out.println("Goodbye");
            }
        }
    }

    public static Othello.GameResult playGame(Othello othello) {
        int moveOnRow = 0;
        int moveOnCol = 0;
        othello.printBoard();
        do {
            boolean moveSuccess = false;
            while (!moveSuccess) {
                System.out.println("Which row would you like to select to put a disc?");
                moveOnRow = fetch.nextInt();
                System.out.println("Which column would you like to select to put a disc?");
                moveOnCol = fetch.nextInt();
                moveSuccess = othello.playerMove(moveOnRow, moveOnCol);
                if (!moveSuccess) {
                    System.out.println("Invalid location. Try again.");
                }
            }

        } while (!othello.gameOver());
        return othello.getGameResults();
    }
}
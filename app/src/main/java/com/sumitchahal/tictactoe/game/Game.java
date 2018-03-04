package com.sumitchahal.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private int[][] board = new int[3][3];

    private List<PointsAndScores> rootsChildrenScores;

    public Game(int firstPlayer) {
        if (firstPlayer == PLAYER_X) {
            Random rand = new Random();
            placeMove(new Point(rand.nextInt(3), rand.nextInt(3)), PLAYER_X);
        }
    }

    public void play(Point point) {
        placeMove(point, PLAYER_O);
        if (!isOver()) {
            playPC();
        }
    }

    public boolean isOver() {
        //Game is over is someone has won, or board is full (draw)
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    public int getBoardItem(Point point) {
        return board[point.x][point.y];
    }

    public boolean hasXWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == PLAYER_X)
                || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == PLAYER_X)) {
            //System.out.println("X Diagonal Win");
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == PLAYER_X)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == PLAYER_X))) {
                // System.out.println("X Row or Column win");
                return true;
            }
        }
        return false;
    }

    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == PLAYER_O) ||
                (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == PLAYER_O)) {
            // System.out.println("O Diagonal Win");
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == PLAYER_O)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == PLAYER_O)) {
                //  System.out.println("O Row or Column win");
                return true;
            }
        }

        return false;
    }

    private List<Point> getAvailableStates() {
        List<Point> availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    private void placeMove(Point point, int player) {
        if (board[point.x][point.y] == 0) {
            board[point.x][point.y] = player;
        } else {
            throw new UnsupportedOperationException("Illegal move");
        }
    }

    private Point returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScores.get(best).point;
    }

    private int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    private int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    private void playPC() {
        rootsChildrenScores = new ArrayList<>();
        minimax(0, PLAYER_X);
        placeMove(returnBestMove(), PLAYER_X);
    }

    private int minimax(int depth, int turn) {

        if (hasXWon()) return +1;
        if (hasOWon()) return -1;

        List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) return 0;

        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);

            if (turn == PLAYER_X) { //X's turn select the highest from below minimax() call
                placeMove(point, PLAYER_X);
                int currentScore = minimax(depth + 1, PLAYER_O);
                scores.add(currentScore);

                if (depth == 0)
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));

            } else if (turn == PLAYER_O) { //O's turn select the lowest from below minimax() call
                placeMove(point, PLAYER_O);
                scores.add(minimax(depth + 1, PLAYER_X));
            }
            board[point.x][point.y] = 0; //Reset this point
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }

    public int[][] getBoard() {
        return board;
    }

    public static Point posToPoint(int pos) {
        /*
         * 0 => 0, 0
         * 1 => 0, 1
         * 2 => 0, 2
         * 3 => 1, 0
         * 4 => 1, 1
         * 5 => 1, 2
         * 6 => 2, 0
         * 7 => 2, 1
         * 8 => 2, 2
         */
        return new Point(pos / 3, pos - 3 * (pos / 3));
    }

    public static int pointToPos(Point point) {
        return 3 * point.x + point.y;
    }
}

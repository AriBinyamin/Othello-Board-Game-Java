package othelloGame;

/*
 * Ariel Binyaminov 
 * May 15 2022
 * T00499483
 */

public class Othello {
    public enum PlayerColor {
        EMPTY('*'),
        BLACK('B'),
        WHITE('W');

        private final char value;

        public char getValue() {
            return value;
        }

        PlayerColor(char value) {
            this.value = value;
        }
    }
    public enum GameResult {
        WIN,
        TIE,
        LOSS
    }
    PlayerColor[][] board;
    PlayerColor currentPlayerColor = PlayerColor.BLACK;
    PlayerColor computerColor = PlayerColor.WHITE;

    public Othello() {
        gameSetup(PlayerColor.BLACK);
    }

    public void gameSetup(PlayerColor playerColor) {
        currentPlayerColor = playerColor;
        computerColor = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

        board = new PlayerColor[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = PlayerColor.EMPTY;
            }
        }
        board[3][3] = PlayerColor.BLACK;
        board[4][4] = PlayerColor.BLACK;
        board[3][4] = PlayerColor.WHITE;
        board[4][3] = PlayerColor.WHITE;

        if (computerColor == PlayerColor.BLACK) {
            computerMove();
        }
    }

    public void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8");
        for (int rows = 0; rows < board.length; rows++) {
            System.out.print(rows + 1 + " ");
            for (int cols = 0; cols < board[rows].length; cols++) {
                System.out.print(board[rows][cols].getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean playerMove(int row, int col) {
        int realRow = row - 1;
        int realCol = col - 1;
        boolean playerMoveSuccess = placePiece(realRow, realCol, currentPlayerColor);
        if (playerMoveSuccess) {
            computerMove();
        }
        return playerMoveSuccess;
    }

    public boolean gameOver() {
        boolean playerHasNoMove = getBestMove(currentPlayerColor) == null;
        boolean computerHasNoMove = getBestMove(computerColor) == null;
        return playerHasNoMove && computerHasNoMove;
    }

    public GameResult getGameResults() {
        int whitePieces = 0;
        int blackPieces = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == PlayerColor.WHITE) {
                    whitePieces++;
                } else if (board[i][j] == PlayerColor.BLACK) {
                    blackPieces++;
                }
            }
        }

        if (whitePieces == blackPieces) {
            return GameResult.TIE;
        }

        if (currentPlayerColor == PlayerColor.WHITE) {
            return whitePieces > blackPieces ? GameResult.WIN : GameResult.LOSS;
        } else {
            return blackPieces > whitePieces ? GameResult.WIN : GameResult.LOSS;
        }
    }

    private boolean placePiece(int row, int col, PlayerColor playerColor) {
        if (calculatePiecesFlipped(row, col, playerColor) <= 0) {
            return false;
        }

        board[row][col] = playerColor;
        flipPieces(row, col, playerColor);

        printBoard();

        return true;
    }

    private void flipPieces(int row, int col, PlayerColor playerColor) {
        flipNorth(row, col, playerColor);
        flipSouth(row, col, playerColor);
        flipEast(row, col, playerColor);
        flipWest(row, col, playerColor);
        flipNorthEast(row, col, playerColor);
        flipNorthWest(row, col, playerColor);
        flipSouthEast(row, col, playerColor);
        flipSouthWest(row, col, playerColor);
    }

    // give count of all pieces that would be flipped for a position
    private int calculatePiecesFlipped(int row, int col, PlayerColor playerColor) {
        if (!checkIfCoordInBoard(row, col)) {
            return -1;
        }

        int flipCount = 0;
        flipCount += checkNorth(row, col, playerColor);
        flipCount += checkSouth(row, col, playerColor);
        flipCount += checkEast(row, col, playerColor);
        flipCount += checkWest(row, col, playerColor);
        flipCount += checkNorthEast(row, col, playerColor);
        flipCount += checkNorthWest(row, col, playerColor);
        flipCount += checkSouthEast(row, col, playerColor);
        flipCount += checkSouthWest(row, col, playerColor);

        return flipCount;
    }

    private void computerMove() {
        // check if move available for computer color
        int[] bestMove = getBestMove(computerColor);

        if (bestMove != null) {
            placePiece(bestMove[0], bestMove[1], computerColor);

            if (getBestMove(currentPlayerColor) == null) {
                computerMove();
            }
        }
    }

    private int[] getBestMove(PlayerColor color) {
        int maxFlips = 0;
        int maxRow = -1;
        int maxCol = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == PlayerColor.EMPTY) {
                    int flips = calculatePiecesFlipped(i, j, color);
                    if (maxFlips < flips) {
                        maxFlips = flips;
                        maxRow = i;
                        maxCol = j;
                    }
                }
            }
        }

        if (maxFlips == 0) {
            return null;
        }

        int[] answer = new int[2];
        answer[0] = maxRow;
        answer[1] = maxCol;
        return answer;
    }

    private void flipNorth(int row, int col, PlayerColor playerColor) {
        if (checkNorth(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            for (int i = row - 1; i >= 0; i--) {
                if (board[i][col] == opponent) {
                    board[i][col] = playerColor;
                } else {
                    break;
                }
            }
        }
    }

    private void flipSouth(int row, int col, PlayerColor playerColor) {
        if (checkSouth(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            for (int i = row + 1; i < board.length; i++) {
                if (board[i][col] == opponent) {
                    board[i][col] = playerColor;
                } else {
                    break;
                }
            }
        }
    }

    private void flipEast(int row, int col, PlayerColor playerColor) {
        if (checkEast(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            for (int i = col + 1; i < board.length; i++) {
                if (board[row][i] == opponent) {
                    board[row][i] = playerColor;
                } else {
                    break;
                }
            }
        }
    }

    private void flipWest(int row, int col, PlayerColor playerColor) {
        if (checkWest(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            for (int i = col - 1; i >= 0; i--) {
                if (board[row][i] == opponent) {
                    board[row][i] = playerColor;
                } else {
                    break;
                }
            }
        }
    }

    private void flipNorthEast(int row, int col, PlayerColor playerColor) {
        if (checkNorthEast(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            int j = col + 1;
            for (int i = row - 1; i >= 0 && j < board.length; i--) {
                if (board[i][j] == opponent) {
                    board[i][j] = playerColor;
                } else {
                    break;
                }
                j++;
            }
        }
    }

    private void flipNorthWest(int row, int col, PlayerColor playerColor) {
        if (checkNorthWest(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            int j = col - 1;
            for (int i = row - 1; i >= 0 && j >= 0; i--) {
                if (board[i][j] == opponent) {
                    board[i][j] = playerColor;
                } else {
                    break;
                }
                j--;
            }
        }
    }

    private void flipSouthEast(int row, int col, PlayerColor playerColor) {
        if (checkSouthEast(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            int j = col + 1;
            for (int i = row + 1; i < board.length && j < board.length; i++) {
                if (board[i][j] == opponent) {
                    board[i][j] = playerColor;
                } else {
                    break;
                }
                j++;
            }
        }
    }

    private void flipSouthWest(int row, int col, PlayerColor playerColor) {
        if (checkSouthWest(row, col, playerColor) > 0) {
            PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;

            int j = col - 1;
            for (int i = row + 1; i < board.length && j >= 0; i++) {
                if (board[i][j] == opponent) {
                    board[i][j] = playerColor;
                } else {
                    break;
                }
                j--;
            }
        }
    }

    private int checkNorth(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;

        for (int i = row - 1; i >= 0; i--) {
            if (board[i][col] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[i][col] == opponent) {
                positionsMoved++;
            } else if (board[i][col] == playerColor) {
                return positionsMoved;
            }
        }
        return 0;
    }

    private int checkSouth(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;

        for (int i = row + 1; i < board.length; i++) {
            if (board[i][col] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[i][col] == opponent) {
                positionsMoved++;
            } else if (board[i][col] == playerColor) {
                return positionsMoved;
            }
        }
        return 0;
    }

    private int checkEast(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;

        for (int i = col + 1; i < board.length; i++) {
            if (board[row][i] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[row][i] == opponent) {
                positionsMoved++;
            } else if (board[row][i] == playerColor) {
                return positionsMoved;
            }
        }
        return 0;
    }

    private int checkWest(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;

        for (int i = col - 1; i >= 0; i--) {
            if (board[row][i] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[row][i] == opponent) {
                positionsMoved++;
            } else if (board[row][i] == playerColor) {
                return positionsMoved;
            }
        }
        return 0;
    }

    private int checkNorthEast(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;
        int j = col + 1;
        for (int i = row - 1; i >= 0 && j < board.length; i--) {
            if (board[i][j] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[i][j] == opponent) {
                positionsMoved++;
            } else if (board[i][j] == playerColor) {
                return positionsMoved;
            }
            j++;
        }
        return 0;
    }

    private int checkNorthWest(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;
        int j = col - 1;
        for (int i = row - 1; i >= 0 && j >= 0; i--) {
            if (board[i][j] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[i][j] == opponent) {
                positionsMoved++;
            } else if (board[i][j] == playerColor) {
                return positionsMoved;
            }
            j--;
        }
        return 0;
    }

    private int checkSouthEast(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;
        int j = col + 1;
        for (int i = row + 1; i < board.length && j < board.length; i++) {
            if (board[i][j] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[i][j] == opponent) {
                positionsMoved++;
            } else if (board[i][j] == playerColor) {
                return positionsMoved;
            }
            j++;
        }
        return 0;
    }

    private int checkSouthWest(int row, int col, PlayerColor playerColor) {
        PlayerColor opponent = playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        int positionsMoved = 0;
        int j = col - 1;
        for (int i = row + 1; i < board.length && j >= 0; i++) {
            if (board[i][j] == PlayerColor.EMPTY) {
                return 0;
            } else if (board[i][j] == opponent) {
                positionsMoved++;
            } else if (board[i][j] == playerColor) {
                return positionsMoved;
            }
            j--;
        }
        return 0;
    }

    private boolean checkIfCoordInBoard(int row, int col) {
        boolean xValid = row >= 0 && row < 8;
        boolean yValid = col >= 0 && col < 8;
        return xValid && yValid;
    }
}
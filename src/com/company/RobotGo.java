package com.company;

import java.util.Arrays;
import java.util.Random;

public class RobotGo {
    public static Random r = new Random(58);

    public static void main(String[] args) {

        char[][] currentBoard = createRandomObstacle(createBoard(20, 40), 200);
        displayBoard(startNavigation(currentBoard));

        System.out.println(Arrays.deepToString(currentBoard));
    }

    public static int[] getRandomCoordinate(int maxY, int maxX) {
        int[] coor = new int[2];
        int y = r.nextInt(maxY);
        int x = r.nextInt(maxX);
        coor[0] = y;
        coor[1] = x;
        return coor;
    }

    // ========================
    // Enter your code below

    private static char[][] createBoard(int row, int column) {
        if (column < 5 || row < 5) {
            throw new IllegalArgumentException("Both x and y axes must be greater than or equal to 5.");
        }
        char[][] board = new char[row][column];

        //generate the maze structure
        for (int i = 0; i < row; i++) {

            if(i == 0 || i == row - 1) {

                for (int j = 0; j < column; j++) {

                    board[i][j] = '#';

                }

            } else {

                for (int j = 0; j < column; j++) {

                    if (j == 0 || j == column - 1){

                        board[i][j] = '#';

                    } else {

                        board[i][j] = ' ';

                    }
                }
            }
        }

        // add a dot to top left
        board[1][1] = '.';

        // add x at the bottom right
        board[row-2][column-2] = 'x';

        return board;
    }

    private static char[][] createRandomObstacle(char[][] board, int nbrObstacles){

        //verify if the user asks for too many obstacles regarding available positions
        if(nbrObstacles > (board.length-2) * (board[0].length - 2) - 2){
            throw new IllegalArgumentException("Too many obstacles");
        }

        //starting iterations to generate obstacles
        int counter = 0;

        while(counter<nbrObstacles){
            //generate the random coordinates
            int[] coordinates = getRandomCoordinate(board.length,board[0].length);

            //if on walls or outside board coordinates
            if(coordinates[0]>board.length-2 || coordinates[1]>board[0].length-2) continue;

            char verificator = board[coordinates[0]][coordinates[1]];
            //verify if the coordinate is at a legal position regarding our criteria
            if( verificator == '#' || verificator == '.' || verificator == 'x' ) continue;

            //the coordinate passed all verification tests successfully so we put it in the board
            //and increment counter
            board[coordinates[0]][coordinates[1]] = '#';
            counter++;
        }

        return board;
    }

    private static void displayBoard(char[][] board){

        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[i].length; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private static char[][] startNavigation(char[][] board) {
        // we have to keep direction in mind so we
        // will use a int
        // 3=north, 1=south, 0=west, 2=east
        int direction = 0;
        int[] position = {1, 1}; // [x,y]

        while (true) {

            displayBoard(board);

            //if( isBlocked(board,position,direction) ){
            //    System.out.println("This maze is impossible to solve. Sorry.");
            //    return board;
            //}
            char a = isFree(board, position, 0);
            char b = isFree(board, position, 1);
            char c = isFree(board, position, 2);
            char d = isFree(board, position, 3);

            if (a == 'w' || b == 'w' || c == 'w' || d == 'w') {
                return board;
            }

            boolean success = false;

            for(int i=0; i<4; i++) {
                // can you go to the right of your direction ?
                if (isFree(board, position, absRight(direction + i)) == 'f') {
                    direction = absRight(direction + i);
                    move(board, position, direction);
                    success = true;
                    break;
                }
            }
            if (!success){ // if no direction have never been visited go to last dot to your most right right
                for(int i=0; i<4; i++) {
                    if (isFree(board,position,absRight(direction+i)) == 'd') {
                        direction = absRight(direction + i);
                        move(board,position, direction);
                        break;
                    }
                }
            }
        }
    }

    // set a dot at the right position
    private static void move(char[][] board, int[] posi, int d){

        modifPos(posi, d);
        board[posi[1]][posi[0]] = '.';
    }

    // modify rightly the position by the direction you give
    private static void modifPos(int[] p, int d){
        switch (d){
            case 1:
                p[1]++;
                break;
            case 3:
                p[1]--;
                break;
            case 0:
                p[0]--;
                break;
            case 2:
                p[0]++;
                break;
        }
    }

    // mathematically sets the right of the actual direction
    private static int absRight(int x){
        x--;
        if (x < 0) x=3;
        return (x%4);
    }

    // evaluate the next destination.
    // if this is a w , you win. if this is a space, you are free to go
    // if this is a dot it will only be a second possibility
    private static char isFree(char[][] board, int[] xy , int direction){

        int[] pos = {xy[0], xy[1]};

        modifPos(pos, direction);

        char c = board[pos[1]][pos[0]];
        if( c == 'x' ) return 'w'; // win
        if( c == ' ' ) return 'f'; // free
        if( c == '.') return 'd'; // dot
        else return 'o'; // occupied
    }
    // Enter your code above
    // ========================
}


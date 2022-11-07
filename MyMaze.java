// Anton Priborkin, Noah Sachs:
// pribo002, sachs092:

import java.util.Random;

public class MyMaze {
    Cell[][] maze;
    int startRow;
    int endRow;
    int rows;
    int cols;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        this.rows = rows;
        this.cols = cols;
        this.startRow = startRow;
        this.endRow = endRow;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = new Cell();
            }
        }
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow) {

        // New MyMaze object
        MyMaze mazeMade = new MyMaze(rows, cols, startRow, endRow);

        // Instantiating the stack
        Stack1Gen<int[] > stack = new Stack1Gen<>();

        // Pushing start coordinates on stack
        stack.push(new int[] {startRow,0});

        // Loop until stack is empty
        while (!stack.isEmpty()) {

            // Get the top element of the stack but do not remove it
            int[] top = stack.top();

            // Sets coordinate location visited status to true
            mazeMade.maze[top[0]][top[1]].setVisited(true);

            // sets indexing
            int lastRow = top[0];
            int lastCol = top[1];
            int neighborCount = 0;

            // Instantiating new array to keep track of neighbors
            int[][] neighborArray = new int[4][2];

            // UP neighbor check
            if ((lastRow - 1) >= 0 && !mazeMade.maze[lastRow - 1][lastCol].getVisited()) {
                neighborArray[neighborCount][0] = lastRow - 1;
                neighborArray[neighborCount][1] = lastCol;
                neighborCount++;
            }

            // Add a check for out of bounds for all 3
            // RIGHT neighbor check
            if ((lastCol + 1) < cols && !mazeMade.maze[lastRow][lastCol + 1].getVisited()) {
                neighborArray[neighborCount][0] = lastRow;
                neighborArray[neighborCount][1] = lastCol + 1;
                neighborCount++;
            }

            // DOWN neighbor check
            if ((lastRow + 1) < rows && !mazeMade.maze[lastRow + 1][lastCol].getVisited()) {
                neighborArray[neighborCount][0] = lastRow + 1;
                neighborArray[neighborCount][1] = lastCol;
                neighborCount++;
            }

            // LEFT neighbor check
            if ((lastCol - 1) >= 0 && !mazeMade.maze[lastRow][lastCol - 1].getVisited()) {
                neighborArray[neighborCount][0] = lastRow;
                neighborArray[neighborCount][1] = lastCol - 1;
                neighborCount++;
            }

            if (neighborCount > 0) {
                Random random = new Random();
                int randomInt = random.nextInt(neighborCount);
                stack.push(neighborArray[randomInt]);

                // Upward check, remove bottom from newer location
                if (neighborArray[randomInt][0] < lastRow) {
                    mazeMade.maze[neighborArray[randomInt][0]][lastCol].setBottom(false);
                }
                // Lower check, remove bottom from older location
                else if (neighborArray[randomInt][0] > lastRow) {
                    mazeMade.maze[lastRow][lastCol].setBottom(false);
                }
                // Right-bound check, remove right from older location
                else if (neighborArray[randomInt][1] > lastCol) {
                    mazeMade.maze[lastRow][lastCol].setRight(false);
                }
                // Left-bound check, remove right from newer location
                else{
                    mazeMade.maze[lastRow][neighborArray[randomInt][1]].setRight(false);
                }
            }

            // Dead end, pop last location from stack
            else {
                stack.pop();
            }

        }

        // sets visited status to false for each cell before final return
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeMade.maze[i][j].setVisited(false);
            }
        }
        return mazeMade;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {

        // horizontal       "---"               EVEN,ODD
        // vertical          "|"             ANYTHING,EVEN
        // blank            "   "               ODD,ODD
        // visited          " * "

        StringBuilder outputStr = new StringBuilder();

        String horizontal = "---";
        String vertical = "|";
        String blankHorizontal = "\t";
        String blankVertical = " ";
        String visited = " * ";

        for (int i = 0; i < (rows * 2) + 1; i++) {
            for (int j = 0; j < (cols * 2) + 1; j++) {

                // Creates the first row
                if (i == 0) {
                    if (j % 2 == 1) {
                        outputStr.append(horizontal);
                    }
                    else {
                        outputStr.append(vertical);
                    }
                }

                // Creates rows 1 through (2rows + 1)
                if(i == endRow * 2 + 1 && j == cols * 2){
                    outputStr.append(blankVertical);
                }
                else if (i > 0) {
                    if (j == 0) {
                        if (i == startRow * 2 + 1) {
                            outputStr.append(blankVertical);
                        } else {
                            outputStr.append(vertical);
                        }
                    }
                    else if (i % 2 == 1){
                        if (j % 2 == 1) {
                            if (maze[(i - 1)/2][(j - 1)/2].getVisited()) {
                                outputStr.append(visited);
                            }
                            else {
                                outputStr.append(blankHorizontal);
                            }
                        }

                        else { // j % 2 == 0
                            if (maze[(i - 1)/2][(j - 1)/2].getRight()) {
                                outputStr.append(vertical);
                            }
                            else {
                                outputStr.append(blankVertical);
                            }
                        }
                    }
                    else{
                        if (j % 2 == 1) {
                            if (maze[(i - 1) / 2][(j - 1) / 2].getBottom()) {
                                outputStr.append(horizontal);
                            } else {
                                outputStr.append(blankHorizontal);
                            }
                        }
                        // j % 2 == 0
                        else{
                            outputStr.append(vertical);
                        }
                    }
                }
                // create blank for endRow
            }
            outputStr.append("\n");
        }
        System.out.println(outputStr);
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */


    public void solveMaze() {

        // Instantiating the queue
        Q2Gen<int[]> queue = new Q2Gen<>();

        // Adding start coordinates to queue
        queue.add(new int[] {startRow,0});   // switch back to {startRow, 0}

        // Loop until queue is empty
        while (queue.length() != 0) {

            // Dequeue front index of queue
            int[] answer = queue.remove();

            // Sets coordinate location visited status to true
            maze[answer[0]][answer[1]].setVisited(true);

            // sets indexing
            int lastRow = answer[0];
            int lastCol = answer[1];


            // Finding reachable neighbors if the end is not reached
            if (lastRow != endRow || lastCol != cols - 1) {

                int[][] neighborArray = new int[3][2];
                int neighborCount = 0;

                // UP neighbor check
                if ((lastRow - 1) >= 0 && !maze[lastRow - 1][lastCol].getVisited() && !maze[lastRow - 1][lastCol].getBottom()) {
                    neighborArray[neighborCount][0] = lastRow - 1;
                    neighborArray[neighborCount][1] = lastCol;
                    queue.add(neighborArray[neighborCount]);
                    neighborCount++;
                }

                // RIGHT neighbor check
                if ((lastCol + 1) < cols && !maze[lastRow][lastCol + 1].getVisited() && !maze[lastRow][lastCol].getRight()) {
                    neighborArray[neighborCount][0] = lastRow;
                    neighborArray[neighborCount][1] = lastCol + 1;
                    queue.add(neighborArray[neighborCount]);
                    neighborCount++;
                }

                // DOWN neighbor check
                if ((lastRow + 1) < rows && !maze[lastRow + 1][lastCol].getVisited() && !maze[lastRow][lastCol].getBottom()) {
                    neighborArray[neighborCount][0] = lastRow + 1;
                    neighborArray[neighborCount][1] = lastCol;
                    queue.add(neighborArray[neighborCount]);
                    neighborCount++;
                }

                // LEFT neighbor check
                if ((lastCol - 1) >= 0 && !maze[lastRow][lastCol - 1].getVisited() && !maze[lastRow][lastCol - 1].getRight()){
                    neighborArray[neighborCount][0] = lastRow;
                    neighborArray[neighborCount][1] = lastCol - 1;
                    queue.add(neighborArray[neighborCount]);
                }
            }
            else {
                break;
            }
        }
        printMaze();
    }


    public static void main(String[] args){

        // For testing purposes, the function assumes integer values of 1 or greater for "rows" and "cols";
        // integer values of 0 or greater for "startRow" and "endRow";
        // "startRow" must be less than "rows"
	
	//Scanner myScanner = new Scanner();
	
        // Assigning user inputs to makeMaze
        MyMaze maze = makeMaze(5,5,0,0);

        // Calling solveMaze on maze after maze creation
        maze.solveMaze();


    }
}
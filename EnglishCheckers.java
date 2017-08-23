import java.util.Scanner;
import java.util.Random;

public class EnglishCheckers {

	// Global constants
	public static final int RED   = 1;
	public static final int BLUE  = -1;
	public static final int EMPTY = 0;

	public static final int SIZE  = 8;

	// You can ignore these constants
	public static final int MARK  = 3;
	public static EnglishCheckersGUI grid;

	public static Scanner getPlayerFullMoveScanner = null;
	public static Scanner getStrategyScanner = null;

	public static final int RANDOM			= 1;
	public static final int DEFENSIVE		= 2;
	public static final int SIDES				= 3;
	public static final int CUSTOM			= 4;


	public static void main(String[] args) {

		// ******** Don't delete ********* 
		// CREATE THE GRAPHICAL GRID
		grid = new EnglishCheckersGUI(SIZE);
		// ******************************* 


		//showBoard(example);
		//printMatrix(example);

		//interactivePlay();
		//twoPlayers();


		/* ******** Don't delete ********* */    
		if (getPlayerFullMoveScanner != null){
			getPlayerFullMoveScanner.close();
		}
		if (getStrategyScanner != null){
			getStrategyScanner.close();
		}
		/* ******************************* */

	}


	public static int[][] createBoard() {
		int[][] board = new int[8][8];
		for(int i = 0;i<board.length;i++)
		{
			for(int j = 0;j<board.length;j++)
			{
				board[i][j] = 0;
			}
		}
		for(int i = 0;i<3;i++) // creates player 1
		{
			for(int j = 0;j<8;j++)
			{
				if(i%2==0 && j%2==0)
					board[i][j] = 1;
				if(i%2!=0 && j%2!=0)
					board[i][j] = 1;
			}
		}
		for(int i = 5;i<board.length;i++) // creates player -1
		{
			for(int j = 0;j<board.length;j++)
			{
				if(i%2==0 && j%2==0)
					board[i][j] = -1;
				if(i%2!=0 && j%2!=0)
					board[i][j] = -1;
			}
		}

		return board;
	}


	public static int[][] playerDiscs(int[][] board, int player) 
	{
		int count=0;
		for(int i = 0;i<board.length;i++)
		{
			for(int j = 0;j<board.length;j++)
			{
				if(board[i][j]==player || board[i][j]==player+player)
					count=count+1;
				
			}
		}

		int[][] positions = new int[count][2];
		int x=0;
		//The loop runs on the board and put the index of discs in positions array
		for(int i = 0;i<board.length;i++)
		{
			for(int j = 0;j<board.length;j++)
			{
				if(board[i][j]==player || board[i][j]==player+player)
					{	
						positions[x][0] = i;
						positions[x][1] = j;
						x++;
					}
			}
		}

		return positions;
	}


	public static boolean isBasicMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		boolean ans = false;
		if(toCol >= 0 && toCol <= 7 && toRow >= 0 && toRow <= 7)
		{
			if(board[fromRow][fromCol] == player) // checks disc type and movement
			{
				if((toRow == fromRow + player) && (toCol == fromCol + 1 || toCol == fromCol - 1))
				{
					if(board[toRow][toCol] == 0)
						ans = true;
				}
			}
		
			if(board[fromRow][fromCol] == (player+player)) // checks disc type and movement
			{
				if((toRow == fromRow + player || toRow == fromRow - player) && (toCol == fromCol + 1 || toCol == fromCol - 1))
				{
					if(board[toRow][toCol] == 0)
						ans = true;
				}
			}
		}
		return ans;
	}


	public static int[][] getAllBasicMoves(int[][] board, int player) {
		int[][] tmp = new int[playerDiscs(board, player).length][10]; // temporary array for sorting discs
		for(int i=0;i<tmp.length;i++)
		{
			tmp[i][0] = playerDiscs(board, player)[i][0];
			tmp[i][1] = playerDiscs(board, player)[i][1];
			tmp[i][2] = 9;
			tmp[i][3] = 9;
			tmp[i][4] = 9;
			tmp[i][5] = 9;
			tmp[i][6] = 9;
			tmp[i][7] = 9;
			tmp[i][8] = 9;
			tmp[i][9] = 9;
		}
		for(int i=0;i<tmp.length;i++)
		{
			if(board[tmp[i][0]][tmp[i][1]] == player) // checks player type and all movement possibilities
			{
				if(isBasicMoveValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+player, tmp[i][1]+1))
				{
					tmp[i][2]= tmp[i][0]+player;
					tmp[i][3]= tmp[i][1]+1;
				}
				if(isBasicMoveValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+player, tmp[i][1]-1))
				{
					tmp[i][4]= tmp[i][0]+player;
					tmp[i][5]= tmp[i][1]-1;
				}
			}
			else if(board[tmp[i][0]][tmp[i][1]] == player+player) // checks player type and all movement possibilities
			{
				if(isBasicMoveValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+player, tmp[i][1]+1))
				{
					tmp[i][2]= tmp[i][0]+player;
					tmp[i][3]= tmp[i][1]+1;
				}
				if(isBasicMoveValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+player, tmp[i][1]-1))
				{
					tmp[i][4]= tmp[i][0]+player;
					tmp[i][5]= tmp[i][1]-1;
				}
				if(isBasicMoveValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]-player, tmp[i][1]+1))
				{
					tmp[i][6]= tmp[i][0]-player;
					tmp[i][7]= tmp[i][1]+1;
				}
				if(isBasicMoveValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]-player, tmp[i][1]-1))
				{
					tmp[i][8]= tmp[i][0]-player;
					tmp[i][9]= tmp[i][1]-1;
				}
			}
		}
		int count = 0;
		for(int i=0;i<tmp.length;i++)
		{
			if(tmp[i][2] != 9)
				count++;
			if(tmp[i][4] != 9)
				count++;
			if(tmp[i][6] != 9)
				count++;
			if(tmp[i][8] != 9)
				count++;
		}
		int[][] moves = new int[count][4]; // creates an array with the right number of possible moves
		count = 0;
		for(int i=0;i<tmp.length;i++)
		{
			for(int j=2;j<8;j=j+2)
			{
				if(tmp[i][j] != 9)
				{
					moves[count][0] = tmp[i][0];
					moves[count][1] = tmp[i][1];
					moves[count][2] = tmp[i][j];
					moves[count][3] = tmp[i][j+1];
					count++;
				}
			}
		}
		
		return moves;
	}


	public static boolean isBasicJumpValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		boolean ans = false;
		if(fromCol >= 0 && fromCol <= 7 && toCol >= 0 && toCol <= 7 && fromRow >= 0 && fromRow <= 7 && toRow >= 0 && toRow <= 7)
		{
			if(board[fromRow][fromCol] == player) // checks disc type and jump movement
			{
				if(board[(fromRow+toRow)/2][(fromCol+toCol)/2] == -player || board[(fromRow+toRow)/2][(fromCol+toCol)/2] == -2*player) // checks if jumps over a opposite player
				{
					if(toRow == fromRow + player*2 && (toCol == fromCol + 2 || toCol == fromCol - 2))
					{
						if(board[toRow][toCol] == 0)
							ans = true;
					}
				}
			}
			if(board[fromRow][fromCol] == (player+player)) // checks disc type and jump movement
			{
				if(board[(fromRow+toRow)/2][(fromCol+toCol)/2] ==-player || board[(fromRow+toRow)/2][(fromCol+toCol)/2] ==-2*player) // checks if jumps over a opposite player
				{
					if(toRow == fromRow + player*2 || toRow == fromRow - player*2 && (toCol == fromCol + 2 || toCol == fromCol - 2))
					{
						if(board[toRow][toCol] == 0)
							ans = true;
					}
				}
			}
		}
		return ans;
	}


	public static int [][] getRestrictedBasicJumps(int[][] board, int player, int row, int col) {
		int count = 0;
		int[][] tmp = new int[4][4];
		if(row >= 0 && row <= 7 && col >= 0 && col <=7)
		{
			if(board[row][col] == player) // checks player type
			{
				if(isBasicJumpValid(board, player, row, col, row+2*player, col+2)) // counts jump possibilities
				{
					tmp[count][0] = row;
					tmp[count][1] = col;
					tmp[count][2] = row+2*player;
					tmp[count][3] = col+2;
					count++;
				}
				if(isBasicJumpValid(board, player, row, col, row+2*player, col-2))
				{
					tmp[count][0] = row;
					tmp[count][1] = col;
					tmp[count][2] = row+2*player;
					tmp[count][3] = col-2;
					count++;
				}
			}
			else if(board[row][col] == player+player) // checks player type
			{
				if(isBasicJumpValid(board, player, row, col, row+2*player, col+2)) // counts jump possibilities
				{
					tmp[count][0] = row;
					tmp[count][1] = col;
					tmp[count][2] = row+2*player;
					tmp[count][3] = col+2;
					count++;
				}
				if(isBasicJumpValid(board, player, row, col, row+2*player, col-2))
				{
					tmp[count][0] = row;
					tmp[count][1] = col;
					tmp[count][2] = row+2*player;
					tmp[count][3] = col-2;
					count++;
				}
				if(isBasicJumpValid(board, player, row, col, row-2*player, col+2))
				{
					tmp[count][0] = row;
					tmp[count][1] = col;
					tmp[count][2] = row-2*player;
					tmp[count][3] = col+2;
					count++;
				}
				if(isBasicJumpValid(board, player, row, col, row-2*player, col-2))
				{
					tmp[count][0] = row;
					tmp[count][1] = col;
					tmp[count][2] = row-2*player;
					tmp[count][3] = col-2;
					count++;
				}
			}
		}
		int[][] moves = new int[count][4];
		if(count!=0)
		{
			for(int i=0;i<moves.length;i++)
			{
				for(int j=0;j<4;j++)
				{
					moves[i][j] = tmp[i][j];
				}
			}
		}
		return moves;
	}


	public static int[][] getAllBasicJumps(int[][] board, int player) {
		int[][] tmp = new int[playerDiscs(board, player).length][10]; // temporary array for sorting discs
		for(int i=0;i<tmp.length;i++)
		{
			tmp[i][0] = playerDiscs(board, player)[i][0];
			tmp[i][1] = playerDiscs(board, player)[i][1];
			tmp[i][2] = 9;
			tmp[i][3] = 9;
			tmp[i][4] = 9;
			tmp[i][5] = 9;
			tmp[i][6] = 9;
			tmp[i][7] = 9;
			tmp[i][8] = 9;
			tmp[i][9] = 9;
		}
		for(int i=0;i<tmp.length;i++)
		{
			if(board[tmp[i][0]][tmp[i][1]] == player) // checks player type and all movement possibilities
			{
				if(isBasicJumpValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+2*player, tmp[i][1]+2))
				{
					tmp[i][2]= tmp[i][0]+2*player;
					tmp[i][3]= tmp[i][1]+2;
				}
				if(isBasicJumpValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+2*player, tmp[i][1]-2))
				{
					tmp[i][4]= tmp[i][0]+2*player;
					tmp[i][5]= tmp[i][1]-2;
				}
			}
			else if(board[tmp[i][0]][tmp[i][1]] == player+player) // checks player type and all movement possibilities
			{
				if(isBasicJumpValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+2*player, tmp[i][1]+2))
				{
					tmp[i][2]= tmp[i][0]+2*player;
					tmp[i][3]= tmp[i][1]+2;
				}
				if(isBasicJumpValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]+2*player, tmp[i][1]-2))
				{
					tmp[i][4]= tmp[i][0]+2*player;
					tmp[i][5]= tmp[i][1]-2;
				}
				if(isBasicJumpValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]-2*player, tmp[i][1]+2))
				{
					tmp[i][6]= tmp[i][0]-2*player;
					tmp[i][7]= tmp[i][1]+2;
				}
				if(isBasicJumpValid(board, player, tmp[i][0], tmp[i][1],tmp[i][0]-2*player, tmp[i][1]-2))
				{
					tmp[i][8]= tmp[i][0]-2*player;
					tmp[i][9]= tmp[i][1]-2;
				}
			}
		}
		int count = 0;
		for(int i=0;i<tmp.length;i++)
		{
			if(tmp[i][2] != 9)
				count++;
			if(tmp[i][4] != 9)
				count++;
			if(tmp[i][6] != 9)
				count++;
			if(tmp[i][8] != 9)
				count++;
		}
		int[][] moves = new int[count][4]; // creates an array with the right number of possible moves
		count = 0;
		for(int i=0;i<tmp.length;i++)
		{
			for(int j=2;j<8;j=j+2)
			{
				if(tmp[i][j] != 9)
				{
					moves[count][0] = tmp[i][0];
					moves[count][1] = tmp[i][1];
					moves[count][2] = tmp[i][j];
					moves[count][3] = tmp[i][j+1];
					count++;
				}
			}
		}
		
		return moves;
	}


	public static boolean canJump(int[][] board, int player)
	{
		boolean ans = false;

		    if(getAllBasicJumps(board, player).length==0 ) // number of columns in first row \\  
		        ans= false;
		    else 
		        ans= true;
		
		return ans;
	}


	public static boolean isMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		boolean ans = false;
		{
		       if ( isBasicJumpValid(board, player, fromRow, fromCol, toRow, toCol)==true) 
		              ans= true;
		       else
		       {
		              if  ((isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol)==true) && (canJump(board, player)==false))
		                  ans= true;
		       }		               
		}
		return ans;
	}




	public static boolean hasValidMoves(int[][] board, int player) 
	{
		boolean ans = false;
		{ 
		      
		     if ((getAllBasicMoves(board, player).length==0) && (getAllBasicJumps(board, player).length==0))
		        ans= false;
		     else 
		        ans= true;
		}
		return ans;
	}


	public static int[][] playMove(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {

		{
		     boolean isMalka=false;
		     if ((player==1 && toRow==7)||(player==-1 && toRow==0)|| board[fromRow][fromCol]== player*2)
		         isMalka=true;
		    if (isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol)) 
		    { 
		         board[fromRow][fromCol]= 0; 
		         board[toRow][toCol]= player;
		         if (isMalka)
		             board[toRow][toCol]=(player*2);
		    }
		     if (isBasicJumpValid(board, player, fromRow, fromCol, toRow, toCol))
		     {
		         board[fromRow][fromCol]=0; 
		         board[toRow][toCol]=player;
		         board[(fromRow+toRow)/2][(fromCol+toCol)/2]=0; 
		         if (isMalka)
		             board[toRow][toCol]=(player*2);
		      }
		      return board;
		}

	}


	public static boolean gameOver(int[][] board, int player) {
		boolean ans = false;
		{
			   if ((playerDiscs(board, player).length==0) || (playerDiscs(board, (player*-1)).length==0))
			     ans= true;
			   if ((hasValidMoves(board, player)==false) || (hasValidMoves(board, (player*-1))==false))
			      ans= true;
			}
		return ans;
	}


	public static int findTheLeader(int[][] board) {
		int ans = 0;

		    int counterred =0;
		    int counterblue =0;
		    int[][] playerred;
		    int[][] playerblue;
		    playerred=( playerDiscs( board,1));
		    playerblue=( playerDiscs( board,-1)); 
		    counterred= playerred.length;  //Count the number of discs that the player
		    counterblue = playerblue.length;  //Count the number of discs that the player
		    for(int i =0; i<board.length; i++)
		    {
		       for(int j=0; j<board[i].length; j++) //Gaining points for each player who owns Queen
		       {
		          if (board[i][j] == 2)
		             counterred=counterred+1;
		          if (board[i][j] == -2)
		             counterblue=counterblue+1;
		       }
		     }
		     if (counterred>counterblue)
		       ans= 1;
		     if (counterblue>counterred)
		       ans= -1;
		     if (counterblue==counterred)
		       ans= 0;
	      	return ans;
	}


	public static int[][] randomPlayer(int[][] board, int player) {

		int[][] jumps =getAllBasicJumps(board, player);
		int[][] moves =getAllBasicMoves(board, player);
		Random rnd = new Random();
		int index;
	    if(jumps.length!=0) // checks if a jump is possible
	    {
	    	index = rnd.nextInt(jumps.length); // makes a random array cell
			playMove(board,player, jumps[0][0], jumps[0][1],jumps[0][2],jumps[0][3]);
	    }
	    else // a move is possible
	    {
	    	index = rnd.nextInt(moves.length); // makes a random array cell
	    	playMove(board,player, moves[index][0], moves[index][1],moves[index][2],moves[index][3]);
	    }
		return board;
	}

	public static int[][] defensivePlayer(int[][] board, int player) {
		int toR=0, toC=0, fromR=0, fromC=0;
		boolean b = false;
		Random rnd = new Random();
		int index;
		int [][] m=getAllBasicMoves(board, player);
		if (canJump(board,player)==false) //checks if the player can jump
		{ 
		   for(int i=0;i<m.length && b==false;i++)
		   {
			   fromR = m[i][0];
			   fromC = m[i][1];
			   toR = m[i][2];
			   toC = m[i][3];
			   board[toR][toC] = board[fromR][fromC];
			   board[fromR][fromC] = 0; // makes a temporary move
			   if((getRestrictedBasicJumps(board, -player, toR + player, toC + 1).length) == 0 && (getRestrictedBasicJumps(board, -player, toR + player, toC - 1).length) == 0)
			   {
				   b = true; // ends the loop and move made
				   board[fromR][fromC] = board[toR][toC];
				   board[toR][toC] = 0;
				   playMove(board,player, m[i][0], m[i][1],m[i][2],m[i][3]);
			   }
			   else
			   {
				   board[fromR][fromC] = board[toR][toC];
				   board[toR][toC] = 0;
			   }
		   }
		   if(!b) // make random move
		   {
			   index = rnd.nextInt(m.length); // makes a random array cell
			   playMove(board,player, m[index][0], m[index][1],m[index][2],m[index][3]);
		   }
		}
		else
		{
			int[][] k = getAllBasicJumps(board, player);
			playMove(board,player, k[0][0], k[0][1],k[0][2],k[0][3]);
		}
		return board;
	}

	public static int[][] sidesPlayer(int[][] board, int player) {
		int toR=0, toC=0, fromR=0, fromC=0;
		boolean b = false;
		Random rnd = new Random();
		int index;
		int [][] m=getAllBasicMoves(board, player);
		if (canJump(board,player)==false) //checks if the player can jump
		{ 
		   for(int i=0;i<m.length && b==false;i++)
		   {
			   fromR = m[i][0];
			   fromC = m[i][1];
			   toR = m[i][2];
			   toC = m[i][3];
			   if((fromC<=3) && (fromC!=0)) //checks move to the closest wall
			   {
				   if(m[i][3] == fromC-1)
				   {
					   b = true;
					   playMove(board,player, m[i][0], m[i][1],m[i][2],m[i][3]);
				   }
					   
			   }
			   else	if((fromC>=4) && (fromC!=7)) //checks move to the closest wall
			   {
				   if(m[i][3] == fromC+1)
				   {
					   b = true;
					   playMove(board,player, m[i][0], m[i][1],m[i][2],m[i][3]);
				   }	   
			   }  
			   	   
		   }
		if(!b) // makes random move
		   {
			index = rnd.nextInt(m.length); // makes a random array cell
			playMove(board,player, m[index][0], m[index][1],m[index][2],m[index][3]);
		   }
		}
		else
		{
			int[][] k = getAllBasicJumps(board, player);
			playMove(board,player, k[0][0], k[0][1],k[0][2],k[0][3]);
		}
		return board;
	}







	
	
	
	
	
	//******************************************************************************//

	/* ---------------------------------------------------------- *
	 * Play an interactive game between the computer and you      *
	 * ---------------------------------------------------------- */
	public static void interactivePlay() {
		int[][] board = createBoard();
		showBoard(board);

		System.out.println("Welcome to the interactive Checkers Game !");

		int strategy = getStrategyChoice();
		System.out.println("You are the first player (RED discs)");

		boolean oppGameOver = false;
		while (!gameOver(board, RED) && !oppGameOver) {
			board = getPlayerFullMove(board, RED);

			oppGameOver = gameOver(board, BLUE);
			if (!oppGameOver) {
				EnglishCheckersGUI.sleep(200);

				board = getStrategyFullMove(board, BLUE, strategy);
			}
		}

		int winner = 0;
		if (playerDiscs(board, RED).length == 0  |  playerDiscs(board, BLUE).length == 0){
			winner = findTheLeader(board);
		}

		if (winner == RED) {
			System.out.println();
			System.out.println("\t *************************");
			System.out.println("\t * You are the winner !! *");
			System.out.println("\t *************************");
		}
		else if (winner == BLUE) {
			System.out.println("\n======= You lost :( =======");
		}
		else
			System.out.println("\n======= DRAW =======");
	}


	/* --------------------------------------------------------- *
	 * A game between two players                                *
	 * --------------------------------------------------------- */
	public static void twoPlayers() {
		int[][] board = createBoard();
		showBoard(board);

		System.out.println("Welcome to the 2-player Checkers Game !");

		boolean oppGameOver = false;
		while (!gameOver(board, RED)  &  !oppGameOver) {
			System.out.println("\nRED's turn");
			board = getPlayerFullMove(board, RED);

			oppGameOver = gameOver(board, BLUE);
			if (!oppGameOver) {
				System.out.println("\nBLUE's turn");
				board = getPlayerFullMove(board, BLUE);
			}
		}

		int winner = 0;
		if (playerDiscs(board, RED).length == 0  |  playerDiscs(board, BLUE).length == 0)
			winner = findTheLeader(board);

		System.out.println();
		System.out.println("\t ************************************");
		if (winner == RED)
			System.out.println("\t * The red player is the winner !!  *");
		else if (winner == BLUE)
			System.out.println("\t * The blue player is the winner !! *");
		else
			System.out.println("\t * DRAW !! *");
		System.out.println("\t ************************************");
	}


	/* --------------------------------------------------------- *
	 * Get a complete (possibly a sequence of jumps) move        *
	 * from a human player.                                      *
	 * --------------------------------------------------------- */
	public static int[][] getPlayerFullMove(int[][] board, int player) {
		// Get first move/jump
		int fromRow = -1, fromCol = -1, toRow = -1, toCol = -1;
		boolean jumpingMove = canJump(board, player);
		boolean badMove   = true;
		getPlayerFullMoveScanner = new Scanner(System.in);//I've modified it
		while (badMove) {
			if (player == 1){
				System.out.println("Red, Please play:");
			} else {
				System.out.println("Blue, Please play:");
			}

			fromRow = getPlayerFullMoveScanner.nextInt();
			fromCol = getPlayerFullMoveScanner.nextInt();

			int[][] moves = jumpingMove ? getAllBasicJumps(board, player) : getAllBasicMoves(board, player);
			markPossibleMoves(board, moves, fromRow, fromCol, MARK);
			toRow   = getPlayerFullMoveScanner.nextInt();
			toCol   = getPlayerFullMoveScanner.nextInt();
			markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

			badMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol); 
			if (badMove)
				System.out.println("\nThis is an illegal move");
		}

		// Apply move/jump
		board = playMove(board, player, fromRow, fromCol, toRow, toCol);
		showBoard(board);

		// Get extra jumps
		if (jumpingMove) {
			boolean longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
			while (longMove) {
				fromRow = toRow;
				fromCol = toCol;

				int[][] moves = getRestrictedBasicJumps(board, player, fromRow, fromCol);

				boolean badExtraMove = true;
				while (badExtraMove) {
					markPossibleMoves(board, moves, fromRow, fromCol, MARK);
					System.out.println("Continue jump:");
					toRow = getPlayerFullMoveScanner.nextInt();
					toCol = getPlayerFullMoveScanner.nextInt();
					markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

					badExtraMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol); 
					if (badExtraMove)
						System.out.println("\nThis is an illegal jump destination :(");
				}

				// Apply extra jump
				board = playMove(board, player, fromRow, fromCol, toRow, toCol);
				showBoard(board);

				longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
			}
		}
		return board;
	}


	/* --------------------------------------------------------- *
	 * Get a complete (possibly a sequence of jumps) move        *
	 * from a strategy.                                          *
	 * --------------------------------------------------------- */
	public static int[][] getStrategyFullMove(int[][] board, int player, int strategy) {
		if (strategy == RANDOM)
			board = randomPlayer(board, player);
		else if (strategy == DEFENSIVE)
			board = defensivePlayer(board, player);
		else if (strategy == SIDES)
			board = sidesPlayer(board, player);

		showBoard(board);
		return board;
	}


	/* --------------------------------------------------------- *
	 * Get a strategy choice before the game.                    *
	 * --------------------------------------------------------- */
	public static int getStrategyChoice() {
		int strategy = -1;
		getStrategyScanner = new Scanner(System.in);
		System.out.println("Choose the strategy of your opponent:" +
				"\n\t(" + RANDOM + ") - Random player" +
				"\n\t(" + DEFENSIVE + ") - Defensive player" +
				"\n\t(" + SIDES + ") - To-the-Sides player player");
		while (strategy != RANDOM  &  strategy != DEFENSIVE
				&  strategy != SIDES) {
			strategy=getStrategyScanner.nextInt();
		}
		return strategy;
	}


	/* --------------------------------------- *
	 * Print the possible moves                *
	 * --------------------------------------- */
	public static void printMoves(int[][] possibleMoves) {
		for (int i = 0;  i < 4;  i = i+1) {
			for (int j = 0;  j < possibleMoves.length;  j = j+1)
				System.out.print(" " + possibleMoves[j][i]);
			System.out.println();
		}
	}


	/* --------------------------------------- *
	 * Mark/unmark the possible moves          *
	 * --------------------------------------- */
	public static void markPossibleMoves(int[][] board, int[][] moves, int fromRow, int fromColumn, int value) {
		for (int i = 0;  i < moves.length;  i = i+1)
			if (moves[i][0] == fromRow  &  moves[i][1] == fromColumn)
				board[moves[i][2]][moves[i][3]] = value;

		showBoard(board);
	}


	/* --------------------------------------------------------------------------- *
	 * Shows the board in a graphic window                                         *
	 * you can use it without understanding how it works.                          *                                                     
	 * --------------------------------------------------------------------------- */
	public static void showBoard(int[][] board) {
		grid.showBoard(board);
	}


	/* --------------------------------------------------------------------------- *
	 * Print the board              					                           *
	 * you can use it without understanding how it works.                          *                                                     
	 * --------------------------------------------------------------------------- */
	public static void printMatrix(int[][] matrix){
		for (int i = matrix.length-1; i >= 0; i = i-1){
			for (int j = 0; j < matrix.length; j = j+1){
				System.out.format("%4d", matrix[i][j]);
			}
			System.out.println();
		}
	}

}

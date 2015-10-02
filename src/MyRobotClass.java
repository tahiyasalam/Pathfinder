import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import world.Robot;
import world.World;

public class MyRobotClass extends Robot{
	int columns, rows;
	boolean uncertainty;
	Point start;
	Point end;



	@Override 
	public void addToWorld(World world) { 
		columns = world.numCols(); 
		rows = world.numRows();
		uncertainty = world.getUncertain(); 
		start = world.getStartPos();
		end = world.getEndPos();


		super.addToWorld(world); 
	}

	@Override
	public void travelToDestination() {
		ArrayList<Point> visited = new ArrayList<Point>();
		HashMap<Point, Integer> unvisited = new HashMap<Point, Integer>();



		int min = 0; 

		int manhattanDistance[][] = new int[rows][columns]; //g-values for each location
		int movementCost[][] = new int[rows][columns]; //h-values for each location
		int f[][] = new int[rows][columns]; //f-values for each location

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				manhattanDistance[i][j] = (int)Point.distance(i, j, end.getX(), end.getY()); //populate with manhattan distance
			}
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if(!((i == start.getX() && j == start.getY()))) { //except for start and end nodes
					movementCost[i][j] = 1000000000; //populate with movement distance for walls
				}
				if(((i == end.getX() && j == end.getY()))) {
					movementCost[i][j] = 0;
				}
			}
		}

		int x, y; 

		if (!uncertainty) {
			String ping[][] = new String[rows][columns]; //g-values for each location
			Point p = new Point();

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					p.setLocation(i, j);
					ping[i][j] = pingMap(p); //get all of the locations
				}
			}

			unvisited.put(start, -1);  
			int mc = 0;

			boolean endGoal = false;

			while (!endGoal && !unvisited.isEmpty()) {
				min = Collections.min(unvisited.values()); //store highest priority node

				for (Iterator<Point> itr = unvisited.keySet().iterator(); itr.hasNext();) {
					Point e = itr.next();

					if(min == unvisited.get(e)) {
						//check if robot needs to back track
						if(min == 0){
							endGoal = true;
						}
						x = (int)e.getX();
						y  = (int)e.getY();
						mc = movementCost[x][y]+1;


						visited.add(e); //add key to visited, start processing
						itr.remove();//remove from key set
						unvisited.remove(e); //remove from unvisited

						Point p1 = new Point(x-1, y-1);
						Point p2 = new Point(x-1, y);					
						Point p3 = new Point(x-1, y+1);
						Point p4 = new Point(x, y-1);
						Point p5 = new Point(x, y+1);
						Point p6 = new Point(x+1, y-1);
						Point p7 = new Point(x+1, y);
						Point p8 = new Point(x+1, y+1);
						//1 2 3
						//4 x 5
						//6 7 8

						//corner cases
						if (x == 0 && y == 0) { //top left
							if (ping[x+1][y].equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								if (mc < movementCost[x+1][y]){
									movementCost[x+1][y] = mc;
								}
							}
							if (ping[x][y+1].equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								if (mc < movementCost[x][y+1]){
									movementCost[x][y+1] = mc;
								}
							}
							if (ping[x+1][y+1].equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								if (mc < movementCost[x+1][y+1]){
									movementCost[x+1][y+1] = mc;
								}
							}	
						}

						else if (y == columns-1 && x == 0) { //top right
							if (ping[x][y-1].equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								if (mc < movementCost[x][y-1]){
									movementCost[x][y-1] = mc;
								}
							}
							if (ping[x+1][y-1].equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								if(mc < movementCost[x+1][y-1]){
									movementCost[x+1][y-1] = mc;
								}
							}
							if (ping[x+1][y].equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								if(mc < movementCost[x+1][y]){
									movementCost[x+1][y] = mc;
								}
							}
						}

						else if (y == 0 && x == rows-1) { //bottom left
							if (ping[x-1][y+1].equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								if (mc < movementCost[x-1][y+1]){ 
									movementCost[x-1][y+1] = mc;
								}
							}
							if (ping[x-1][y].equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								if (mc < movementCost[x-1][y]){
									movementCost[x-1][y] = mc;
								}
							}
							if (ping[x][y+1].equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								if (mc < movementCost[x][y+1]){
									movementCost[x][y+1] = mc;
								}
							}
						}

						else if (y == columns-1 && x == rows-1) { //bottom right
							if (ping[x-1][y-1].equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								if (mc < movementCost[x-1][y-1]){
									movementCost[x-1][y-1] = mc;
								}
							}
							if (ping[x-1][y].equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								if(mc < movementCost[x-1][y]){
									movementCost[x-1][y] = mc;
								}
							}
							if (ping[x][y-1].equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								if (mc < movementCost[x][y-1]){
									movementCost[x][y-1] = mc;
								}
							}
						}

						//top row cases
						else if(x == 0) {
							//left and right
							if (ping[x][y-1].equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								if (mc < movementCost[x][y-1]){
									movementCost[x][y-1] = mc;
								}
							}
							if (ping[x][y+1].equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								if(mc < movementCost[x][y+1]){
									movementCost[x][y+1] = mc;
								}
							}

							//bottom
							if (ping[x+1][y-1].equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								if (mc < movementCost[x+1][y-1]){
									movementCost[x+1][y-1] = mc;
								}
							}
							if (ping[x+1][y].equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								if (mc < movementCost[x+1][y]){
									movementCost[x+1][y] = mc;
								}
							}
							if (ping[x+1][y+1].equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								if (mc < movementCost[x+1][y+1]){
									movementCost[x+1][y+1] = mc;
								}
							}
						}

						//bottom row cases
						else if (x == rows-1) {
							//top
							if (ping[x-1][y-1].equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								if(mc < movementCost[x-1][y-1]){
									movementCost[x-1][y-1] = mc;
								}
							}
							if (ping[x-1][y].equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								if (mc < movementCost[x-1][y]){
									movementCost[x-1][y] = mc;
								}
							}
							if (ping[x-1][y+1].equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								if (mc < movementCost[x-1][y+1]){
									movementCost[x-1][y+1] = mc;
								}
							}

							//left and right
							if (ping[x][y-1].equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								if (mc < movementCost[x][y-1]){
									movementCost[x][y-1] = mc;
								}
							}
							if (ping[x][y+1].equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								if (mc < movementCost[x][y+1]){
									movementCost[x][y+1] = mc;
								}
							}	
						}

						//left row cases
						else if(y == 0) {
							if (ping[x-1][y].equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								if (mc < movementCost[x-1][y]){
									movementCost[x-1][y] = mc;
								}
							}
							if (ping[x-1][y+1].equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								if (mc < movementCost[x-1][y+1]){
									movementCost[x-1][y+1] = mc;
								}
							}

							//left and right
							if (ping[x][y+1].equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								if (mc < movementCost[x][y+1]){
									movementCost[x][y+1] = mc;
								}
							}

							//bottom
							if (ping[x+1][y].equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								if (mc < movementCost[x+1][y]){
									movementCost[x+1][y] = mc;
								}
							}
							if (ping[x+1][y+1].equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								if (mc < movementCost[x+1][y+1]){
									movementCost[x+1][y+1] = mc;
								}
							}
						}

						else if(y == columns-1) {
							if (ping[x-1][y-1].equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								if (mc < movementCost[x-1][y-1]){
									movementCost[x-1][y-1] = mc;
								}
							}
							if (ping[x-1][y].equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								if (mc < movementCost[x-1][y]){
									movementCost[x-1][y] = mc;
								}
							}

							//left and right
							if (ping[x][y-1].equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								if (mc < movementCost[x][y-1]){
									movementCost[x][y-1] = mc;
								}
							}

							//bottom
							if (ping[x+1][y-1].equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								if (mc < movementCost[x+1][y-1]){
									movementCost[x+1][y-1] = mc;
								}
							}
							if (ping[x+1][y].equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								if (mc < movementCost[x+1][y]){
									movementCost[x+1][y] = mc;
								}
							}
						}

						//right row cases
						else { //normal, non-edge case coordinate
							//top

							if (ping[x-1][y-1].equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								if (mc < movementCost[x-1][y-1]){
									movementCost[x-1][y-1] = mc;
								}
							}
							if (ping[x-1][y].equals("O") && !visited.contains(p2))  {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								if (mc < movementCost[x-1][y]){
									movementCost[x-1][y] = mc;
								}
							}
							if (ping[x-1][y+1].equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								if (mc < movementCost[x-1][y+1]){
									movementCost[x-1][y+1] = mc;
								}
							}

							//left and right
							if (ping[x][y-1].equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								if (mc < movementCost[x][y-1]){
									movementCost[x][y-1] = mc;
								}
							}
							if (ping[x][y+1].equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								if (mc < movementCost[x][y+1]){
									movementCost[x][y+1] = mc;
								}
							}

							//bottom
							if (ping[x+1][y-1].equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								if (mc < movementCost[x+1][y-1]){
									movementCost[x+1][y-1] = mc;
								}
							}
							if (ping[x+1][y].equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								if (mc < movementCost[x+1][y]){
									movementCost[x+1][y] = mc;
								}
							}
							if (ping[x+1][y+1].equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								if (mc < movementCost[x+1][y+1]){
									movementCost[x+1][y+1] = mc;
								}
							}
						}

						if ( movementCost[end.x][end.y] < mc ) 
							movementCost[end.x][end.y] = mc+1;
						//update f matrix - does this work?
						for ( int i = 0 ; i < rows ; i++ )
							for ( int j = 0 ; j < columns ; j++ ) {
								p.setLocation(i, j);
								f[i][j] = manhattanDistance[i][j] + movementCost[i][j];
								if (unvisited.containsKey(p))
									unvisited.put((p), f[i][j]);
							}
						break;

					}

				}
			}
			ArrayList<Point> path = new ArrayList<Point>();
			path.add(end);
			x = end.x;
			y = end.y;
			min = movementCost[x][y];
			boolean xt = x > 0;
			boolean xb = x < rows-1;
			boolean yl = y > 0;
			boolean yr = y < columns-1;

			boolean done = false;
			Point save = new Point();

			while(!done){

				if (xt && yl && movementCost[x-1][y-1] < min){
					min = movementCost[x-1][y-1];
					Point p1 = new Point(x-1, y-1);
					save.setLocation(x-1, y-1);
					path.add(p1);	
				}

				if (xt && movementCost[x-1][y] < min){
					min = movementCost[x-1][y];
					Point p2 = new Point(x-1, y);
					save.setLocation(x-1, y);
					path.add(p2);
				}

				if (xt && yr && movementCost[x-1][y+1] < min){
					min = movementCost[x-1][y+1];
					Point p3 = new Point(x-1, y+1);
					save.setLocation(x-1, y+1);
					path.add(p3);
				}

				if (yl && movementCost[x][y-1] < min){
					min = movementCost[x][y-1];
					Point p4 = new Point(x, y-1);
					save.setLocation(x, y-1);
					path.add(p4);

				}

				if (yr && movementCost[x][y+1] < min){
					min = movementCost[x][y+1];
					Point p5 = new Point(x, y+1);
					save.setLocation(x, y+1);
					path.add(p5);
				}

				if (xb && yl && movementCost[x+1][y-1] < min){
					min = movementCost[x+1][y-1];
					Point p6 = new Point(x+1, y-1);
					save.setLocation(x+1, y-1);
					path.add(p6);
				}

				if (xb && movementCost[x+1][y] < min){
					min = movementCost[x+1][y];
					Point p7 = new Point(x+1, y);
					save.setLocation(x+1, y);
					path.add(p7);
				}

				if (xb && yr && movementCost[x+1][y+1] < min){
					min = movementCost[x+1][y+1];
					Point p8 = new Point(x+1, y+1);
					save.setLocation(x+1, y+1);
					path.add(p8);
				}
				if (min == 0){
					done = true;
				}
				x = save.x;
				y = save.y;
				xt = x > 0;
				xb = x < rows-1;
				yl = y > 0;
				yr = y < columns-1;

			}


			//gets to dead end and then does not turn back
			while(!path.isEmpty()){
				move(path.get(path.size()-1));
				path.remove(path.size()-1);
			}
		}

		//			System.out.println("STOP HAMMER tiME");
		//			for ( int i = 0 ; i < rows ; i++ ) {
		//				for ( int j = 0 ; j < columns ; j++ ) {
		//					if (movementCost[i][j] >= 1000000000 )
		//						System.out.print("X ");
		//					else
		//						System.out.print(movementCost[i][j] + " ");
		//				}
		//				System.out.println("");
		//			}
		//			
		//			System.out.println("LIVE FROM NEW YORK");
		//			for ( int i = 0 ; i < rows ; i++ ) {
		//				for ( int j = 0 ; j < columns ; j++ ) {
		//					System.out.print(manhattanDistance[i][j] + " ");
		//				}
		//			System.out.println("");
		//			}
		//

		//
		//		
		//			System.out.println("IT TAKES 2");
		//			for ( int i = 0 ; i < rows ; i++ ) {
		//				for ( int j = 0 ; j < columns ; j++ ) {
		//					System.out.print(f[i][j] + " ");
		//				}
		//			System.out.println("");
		//			}

		


		else {
			String ping[][] = new String[rows][columns]; //g-values for each location

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					ping[i][j] = "u"; //get all of the locations
				}
			}		



			Point p = new Point();

			Point pStart = new Point();
			pStart = start;


			
			
			//A* loop begins************************************************
			int tries = 0;
			boolean AStarForever = true;
			while(AStarForever){

				//start by pinging nearby nodes
				int countO = 0;//counting the return values from ping map
				int countX = 0;
				String s;

				//traverse entire map, ping within certain radius of current position
				for(int i = 0; i < rows; i++){
					for (int j = 0; j < columns; j++){
						p.setLocation(i,j);
						//determine distance from current node
						int d = (int) p.distance(pStart);

						//case for the "subgraph" we are pinging
						if (d<4){
							//determine number of pings as a function of  distance
							int pnum = (int) Math.pow(2, d-1);
							if (ping[i][j].equals("u")){
								for(int k = 0; k < pnum; k++){
									s = pingMap(p);
									if (s.equals("O")){
										countO++;
									}
									else if (s.equals("X")){
										countX++;
									}
								}
								if (countO > countX){
									ping[i][j] = "o";
									makeGuess(p, true);
								}
								else {
									ping[i][j] = "x";
									makeGuess(p, false);
								}

							}	
						}
					}	
				}




				unvisited.put(pStart, -1);

				int mc = 0;

				boolean endGoal = false;



				while (!endGoal && !unvisited.isEmpty()) {



					min = Collections.min(unvisited.values()); //store highest priority node
					for (Iterator<Point> itr = unvisited.keySet().iterator(); itr.hasNext();) {
						Point e = itr.next();

						if(min == unvisited.get(e)) {
							//check if robot needs to back track
							if(e.x == end.x && e.y == end.y){
						
								endGoal = true;
								break;
							}
							x = (int)e.getX();
							y  = (int)e.getY();
							mc = movementCost[x][y]+1;


						
							visited.add(e); //add key to visited, start processing
							itr.remove();//remove from key set
							unvisited.remove(e); //remove from unvisited

							Point p1 = new Point(x-1, y-1);
							Point p2 = new Point(x-1, y);					
							Point p3 = new Point(x-1, y+1);
							Point p4 = new Point(x, y-1);
							Point p5 = new Point(x, y+1);
							Point p6 = new Point(x+1, y-1);
							Point p7 = new Point(x+1, y);
							Point p8 = new Point(x+1, y+1);
							//1 2 3
							//4 x 5
							//6 7 8

							
							
							
							//corner cases
							if (x == 0 && y == 0) { //top left
								if ((ping[x+1][y].equals("O") || ping[x+1][y].equals("o") || ping[x+1][y].equals("u") || ignoreGuess(tries, ping[x+1][y].equals("x"))) && !visited.contains(p7)) {
									unvisited.put(p7, manhattanDistance[x+1][y]);
									if (mc < movementCost[x+1][y]){
										movementCost[x+1][y] = mc;
									}
								}
								if ((ping[x][y+1].equals("O") || ping[x][y+1].equals("o") || ping[x][y+1].equals("u") || ignoreGuess(tries, ping[x][y+1].equals("x"))) && !visited.contains(p5)) {
									unvisited.put(p5, manhattanDistance[x][y+1]);
									if (mc < movementCost[x][y+1]){
										movementCost[x][y+1] = mc;
									}
								}
								if ((ping[x+1][y+1].equals("O") || ping[x+1][y+1].equals("o") || ping[x+1][y+1].equals("u") || ignoreGuess(tries, ping[x+1][y+1].equals("x"))) && !visited.contains(p8)) {
									unvisited.put(p8, manhattanDistance[x+1][y+1]);
									if (mc < movementCost[x+1][y+1]){
										movementCost[x+1][y+1] = mc;
									}
								}	
							}

							else if (y == columns-1 && x == 0) { //top right
								if ((ping[x][y-1].equals("O") || ping[x][y-1].equals("o") || ping[x][y-1].equals("u") || ignoreGuess(tries, ping[x][y-1].equals("x")))&& !visited.contains(p4)) {
									unvisited.put(p4, manhattanDistance[x][y-1]);
									if (mc < movementCost[x][y-1]){
										movementCost[x][y-1] = mc;
									}
								}
								if ((ping[x+1][y-1].equals("O") || ping[x+1][y-1].equals("o") || ping[x+1][y-1].equals("u") || ignoreGuess(tries, ping[x+1][y-1].equals("x"))) && !visited.contains(p6)) {
									unvisited.put(p6, manhattanDistance[x+1][y-1]);
									if(mc < movementCost[x+1][y-1]){
										movementCost[x+1][y-1] = mc;
									}
								}
								if ((ping[x+1][y].equals("O") || ping[x+1][y].equals("o") || ping[x+1][y].equals("u") || ignoreGuess(tries, ping[x+1][y].equals("x"))) && !visited.contains(p7)) {
									unvisited.put(p7, manhattanDistance[x+1][y]);
									if(mc < movementCost[x+1][y]){
										movementCost[x+1][y] = mc;
									}
								}
							}

							else if (y == 0 && x == rows-1) { //bottom left
								if ((ping[x-1][y+1].equals("O") || ping[x-1][y+1].equals("o") || ping[x-1][y+1].equals("u") || ignoreGuess(tries, ping[x-1][y+1].equals("x"))) && !visited.contains(p3)) {
									unvisited.put(p3, manhattanDistance[x-1][y+1]);
									if (mc < movementCost[x-1][y+1]){ 
										movementCost[x-1][y+1] = mc;
									}
								}
								if ((ping[x-1][y].equals("O") || ping[x-1][y].equals("o") || ping[x-1][y].equals("u") || ignoreGuess(tries, ping[x-1][y].equals("x")))&& !visited.contains(p2)) {
									unvisited.put(p2, manhattanDistance[x-1][y]);
									if (mc < movementCost[x-1][y]){
										movementCost[x-1][y] = mc;
									}
								}
								if ((ping[x][y+1].equals("O") || ping[x][y+1].equals("o") || ping[x][y+1].equals("u") || ignoreGuess(tries, ping[x][y+1].equals("x")))&& !visited.contains(p5)) {
									unvisited.put(p5, manhattanDistance[x][y+1]);
									if (mc < movementCost[x][y+1]){
										movementCost[x][y+1] = mc;
									}
								}
							}

							else if (y == columns-1 && x == rows-1) { //bottom right
								if ((ping[x-1][y-1].equals("O") || ping[x-1][y-1].equals("o") || ping[x-1][y-1].equals("u") || ignoreGuess(tries, ping[x-1][y-1].equals("x")))&& !visited.contains(p1)) {
									unvisited.put(p1, manhattanDistance[x-1][y-1]);
									if (mc < movementCost[x-1][y-1]){
										movementCost[x-1][y-1] = mc;
									}
								}
								if ((ping[x-1][y].equals("O") || ping[x-1][y].equals("o") || ping[x-1][y].equals("u") || ignoreGuess(tries, ping[x-1][y].equals("x")))&& !visited.contains(p2)) {
									unvisited.put(p2, manhattanDistance[x-1][y]);
									if(mc < movementCost[x-1][y]){
										movementCost[x-1][y] = mc;
									}
								}
								if ((ping[x][y-1].equals("O") || ping[x][y-1].equals("o") || ping[x][y-1].equals("u") || ignoreGuess(tries, ping[x][y-1].equals("x")))&& !visited.contains(p4)) {
									unvisited.put(p4, manhattanDistance[x][y-1]);
									if (mc < movementCost[x][y-1]){
										movementCost[x][y-1] = mc;
									}
								}
							}

							//top row cases
							else if(x == 0) {
								//left and right
								if ((ping[x][y-1].equals("O") || ping[x][y-1].equals("o") || ping[x][y-1].equals("u") || ignoreGuess(tries, ping[x][y-1].equals("x"))) && !visited.contains(p4)) {
									unvisited.put(p4, manhattanDistance[x][y-1]);
									if (mc < movementCost[x][y-1]){
										movementCost[x][y-1] = mc;
									}
								}
								if ((ping[x][y+1].equals("O") || ping[x][y+1].equals("o") || ping[x][y+1].equals("u") || ignoreGuess(tries, ping[x][y+1].equals("x")))&& !visited.contains(p5)) {
									unvisited.put(p5, manhattanDistance[x][y+1]);
									if(mc < movementCost[x][y+1]){
										movementCost[x][y+1] = mc;
									}
								}

								//bottom
								if ((ping[x+1][y-1].equals("O") || ping[x+1][y-1].equals("o") || ping[x+1][y-1].equals("u") || ignoreGuess(tries, ping[x+1][y-1].equals("x"))) && !visited.contains(p6)) {
									unvisited.put(p6, manhattanDistance[x+1][y-1]);
									if (mc < movementCost[x+1][y-1]){
										movementCost[x+1][y-1] = mc;
									}
								}
								if ((ping[x+1][y].equals("O") || ping[x+1][y].equals("o") || ping[x+1][y].equals("u") || ignoreGuess(tries, ping[x+1][y].equals("x"))) && !visited.contains(p7)) {
									unvisited.put(p7, manhattanDistance[x+1][y]);
									if (mc < movementCost[x+1][y]){
										movementCost[x+1][y] = mc;
									}
								}
								if ((ping[x+1][y+1].equals("O") || ping[x+1][y+1].equals("o") || ping[x+1][y+1].equals("u") || ignoreGuess(tries, ping[x+1][y+1].equals("x"))) && !visited.contains(p8)) {
									unvisited.put(p8, manhattanDistance[x+1][y+1]);
									if (mc < movementCost[x+1][y+1]){
										movementCost[x+1][y+1] = mc;
									}
								}
							}

							//bottom row cases
							else if (x == rows-1) {
								//top
								if ((ping[x-1][y-1].equals("O") || ping[x-1][y-1].equals("o") || ping[x-1][y-1].equals("u") || ignoreGuess(tries, ping[x-1][y-1].equals("x"))) && !visited.contains(p1)) {
									unvisited.put(p1, manhattanDistance[x-1][y-1]);
									if(mc < movementCost[x-1][y-1]){
										movementCost[x-1][y-1] = mc;
									}
								}
								if ((ping[x-1][y].equals("O") || ping[x-1][y].equals("o") || ping[x-1][y].equals("u") || ignoreGuess(tries, ping[x-1][y].equals("x"))) && !visited.contains(p2)) {
									unvisited.put(p2, manhattanDistance[x-1][y]);
									if (mc < movementCost[x-1][y]){
										movementCost[x-1][y] = mc;
									}
								}
								if ((ping[x-1][y+1].equals("O") || ping[x-1][y+1].equals("o") ||ping[x-1][y+1].equals("u") || ignoreGuess(tries, ping[x-1][y+1].equals("x"))) && !visited.contains(p3)) {
									unvisited.put(p3, manhattanDistance[x-1][y+1]);
									if (mc < movementCost[x-1][y+1]){
										movementCost[x-1][y+1] = mc;
									}
								}

								//left and right
								if ((ping[x][y-1].equals("O") || ping[x][y-1].equals("o") || ping[x][y-1].equals("u") || ignoreGuess(tries, ping[x][y-1].equals("x"))) && !visited.contains(p4)) {
									unvisited.put(p4, manhattanDistance[x][y-1]);
									if (mc < movementCost[x][y-1]){
										movementCost[x][y-1] = mc;
									}
								}
								if ((ping[x][y+1].equals("O") || ping[x][y+1].equals("o") || ping[x][y+1].equals("u") || ignoreGuess(tries, ping[x][y+1].equals("x"))) && !visited.contains(p5)) {
									unvisited.put(p5, manhattanDistance[x][y+1]);
									if (mc < movementCost[x][y+1]){
										movementCost[x][y+1] = mc;
									}
								}	
							}

							//left row cases
							else if(y == 0) {
								if ((ping[x-1][y].equals("O") || ping[x-1][y].equals("o")  || ping[x-1][y].equals("u") || ignoreGuess(tries, ping[x-1][y].equals("x"))) && !visited.contains(p2)) {
									unvisited.put(p2, manhattanDistance[x-1][y]);
									if (mc < movementCost[x-1][y]){
										movementCost[x-1][y] = mc;
									}
								}
								if ((ping[x-1][y+1].equals("O") || ping[x-1][y+1].equals("o") || ping[x-1][y+1].equals("u") || ignoreGuess(tries, ping[x-1][y+1].equals("x"))) && !visited.contains(p3)) {
									unvisited.put(p3, manhattanDistance[x-1][y+1]);
									if (mc < movementCost[x-1][y+1]){
										movementCost[x-1][y+1] = mc;
									}
								}

								//left and right
								if ((ping[x][y+1].equals("O") || ping[x][y+1].equals("o") || ping[x][y+1].equals("u") || ignoreGuess(tries, ping[x][y+1].equals("x"))) && !visited.contains(p5)) {
									unvisited.put(p5, manhattanDistance[x][y+1]);
									if (mc < movementCost[x][y+1]){
										movementCost[x][y+1] = mc;
									}
								}

								//bottom
								if ((ping[x+1][y].equals("O") || ping[x+1][y].equals("o") || ping[x+1][y].equals("u") || ignoreGuess(tries, ping[x+1][y].equals("x"))) && !visited.contains(p7)) {
									unvisited.put(p7, manhattanDistance[x+1][y]);
									if (mc < movementCost[x+1][y]){
										movementCost[x+1][y] = mc;
									}
								}
								if ((ping[x+1][y+1].equals("O") || ping[x+1][y+1].equals("o") || ping[x+1][y+1].equals("u") || ignoreGuess(tries, ping[x+1][y+1].equals("x"))) && !visited.contains(p8)) {
									unvisited.put(p8, manhattanDistance[x+1][y+1]);
									if (mc < movementCost[x+1][y+1]){
										movementCost[x+1][y+1] = mc;
									}
								}
							}

							else if(y == columns-1) {
								if ((ping[x-1][y-1].equals("O") || ping[x-1][y-1].equals("o") || ping[x-1][y-1].equals("u") || ignoreGuess(tries, ping[x-1][y-1].equals("x"))) && !visited.contains(p1)) {
									unvisited.put(p1, manhattanDistance[x-1][y-1]);
									if (mc < movementCost[x-1][y-1]){
										movementCost[x-1][y-1] = mc;
									}
								}
								if ((ping[x-1][y].equals("O") || ping[x-1][y].equals("o") || ping[x-1][y].equals("u") || ignoreGuess(tries, ping[x-1][y].equals("x"))) && !visited.contains(p2)) {
									unvisited.put(p2, manhattanDistance[x-1][y]);
									if (mc < movementCost[x-1][y]){
										movementCost[x-1][y] = mc;
									}
								}

								//left and right
								if ((ping[x][y-1].equals("O") || ping[x][y-1].equals("o") || ping[x][y-1].equals("u") || ignoreGuess(tries, ping[x][y-1].equals("x"))) && !visited.contains(p4)) {
									unvisited.put(p4, manhattanDistance[x][y-1]);
									if (mc < movementCost[x][y-1]){
										movementCost[x][y-1] = mc;
									}
								}

								//bottom
								if ((ping[x+1][y-1].equals("O") || ping[x+1][y-1].equals("o") || ping[x+1][y-1].equals("u") || ignoreGuess(tries, ping[x+1][y-1].equals("x"))) && !visited.contains(p6)) {
									unvisited.put(p6, manhattanDistance[x+1][y-1]);
									if (mc < movementCost[x+1][y-1]){
										movementCost[x+1][y-1] = mc;
									}
								}
								if ((ping[x+1][y].equals("O") || ping[x+1][y].equals("o") || ping[x+1][y].equals("u") || ignoreGuess(tries, ping[x+1][y].equals("x")))  && !visited.contains(p7)) {
									unvisited.put(p7, manhattanDistance[x+1][y]);
									if (mc < movementCost[x+1][y]){
										movementCost[x+1][y] = mc;
									}
								}
							}

							//right row cases
							else { //normal, non-edge case coordinate
								//top

								if ((ping[x-1][y-1].equals("O") || ping[x-1][y-1].equals("o") || ping[x-1][y-1].equals("u") || ignoreGuess(tries, ping[x-1][y-1].equals("x"))) && !visited.contains(p1)) {
									unvisited.put(p1, manhattanDistance[x-1][y-1]);
									if (mc < movementCost[x-1][y-1]){
										movementCost[x-1][y-1] = mc;
									}
								}
								if ((ping[x-1][y].equals("O") || ping[x-1][y].equals("o") || ping[x-1][y].equals("u") || ignoreGuess(tries, ping[x-1][y].equals("x"))) && !visited.contains(p2))  {
									unvisited.put(p2, manhattanDistance[x-1][y]);
									if (mc < movementCost[x-1][y]){
										movementCost[x-1][y] = mc;
									}
								}
								if ((ping[x-1][y+1].equals("O") || ping[x-1][y+1].equals("o") || ping[x-1][y+1].equals("u") || ignoreGuess(tries, ping[x-1][y+1].equals("x"))) && !visited.contains(p3)) {
									unvisited.put(p3, manhattanDistance[x-1][y+1]);
									if (mc < movementCost[x-1][y+1]){
										movementCost[x-1][y+1] = mc;
									}
								}

								//left and right
								if ((ping[x][y-1].equals("O") || ping[x][y-1].equals("o") || ping[x][y-1].equals("u") || ignoreGuess(tries, ping[x][y-1].equals("x"))) && !visited.contains(p4)) {
									unvisited.put(p4, manhattanDistance[x][y-1]);
									if (mc < movementCost[x][y-1]){
										movementCost[x][y-1] = mc;
									}
								}
								if ((ping[x][y+1].equals("O") || ping[x][y+1].equals("o") || ping[x][y+1].equals("u") || ignoreGuess(tries, ping[x][y+1].equals("x"))) && !visited.contains(p5)) {
									unvisited.put(p5, manhattanDistance[x][y+1]);
									if (mc < movementCost[x][y+1]){
										movementCost[x][y+1] = mc;
									}
								}

								//bottom
								if ((ping[x+1][y-1].equals("O") || ping[x+1][y-1].equals("o") || ping[x+1][y-1].equals("u") || ignoreGuess(tries, ping[x+1][y-1].equals("x"))) && !visited.contains(p6)) {
									unvisited.put(p6, manhattanDistance[x+1][y-1]);
									if (mc < movementCost[x+1][y-1]){
										movementCost[x+1][y-1] = mc;
									}
								}
								if ((ping[x+1][y].equals("O") || ping[x+1][y].equals("o") || ping[x+1][y].equals("u") || ignoreGuess(tries, ping[x+1][y].equals("x"))) && !visited.contains(p7)) {
									unvisited.put(p7, manhattanDistance[x+1][y]);
									if (mc < movementCost[x+1][y]){
										movementCost[x+1][y] = mc;
									}
								}
								if ((ping[x+1][y+1].equals("O") || ping[x+1][y+1].equals("o") || ping[x+1][y+1].equals("u") || ignoreGuess(tries, ping[x+1][y+1].equals("x"))) && !visited.contains(p8)) {
									unvisited.put(p8, manhattanDistance[x+1][y+1]);
									if (mc < movementCost[x+1][y+1]){
										movementCost[x+1][y+1] = mc;
									}
								}
							}

							if ( movementCost[end.x][end.y] < mc ) 
								movementCost[end.x][end.y] = mc+1;
							//update f matrix - does this work?
							for ( int i = 0 ; i < rows ; i++ )
								for ( int j = 0 ; j < columns ; j++ ) {
									p.setLocation(i, j);
									f[i][j] = manhattanDistance[i][j] + movementCost[i][j];
									if (unvisited.containsKey(p))
										unvisited.put((p), f[i][j]);
								}
							break;

						}

					}	
					//check that the robot has not boxed itself in
					boolean boxed = unvisited.isEmpty();
					if (boxed){
						tries++;
						//reset values to run A*
						unvisited.clear();
						unvisited.put(pStart, -1);
						visited.clear();
						for ( int i = 0 ; i < rows ; i++ ) {
							for ( int j = 0 ; j < columns ; j++ ) {
								movementCost[i][j] = 1000000000;
							}
						}
						movementCost[pStart.x][pStart.y] = 0;
					}
					else tries = 0;

				}





				ArrayList<Point> path = new ArrayList<Point>();
				path.add(end);
				x = end.x;
				y = end.y;
				min = movementCost[x][y];
				boolean xt = x > 0;
				boolean xb = x < rows-1;
				boolean yl = y > 0;
				boolean yr = y < columns-1;

				boolean done = false;
				Point save = new Point();

				while(!done){

					if (xt && yl && movementCost[x-1][y-1] < min){
						min = movementCost[x-1][y-1];
						Point p1 = new Point(x-1, y-1);
						save.setLocation(x-1, y-1);
						path.add(p1);	
					}

					if (xt && movementCost[x-1][y] < min){
						min = movementCost[x-1][y];
						Point p2 = new Point(x-1, y);
						save.setLocation(x-1, y);
						path.add(p2);
					}

					if (xt && yr && movementCost[x-1][y+1] < min){
						min = movementCost[x-1][y+1];
						Point p3 = new Point(x-1, y+1);
						save.setLocation(x-1, y+1);
						path.add(p3);
					}

					if (yl && movementCost[x][y-1] < min){
						min = movementCost[x][y-1];
						Point p4 = new Point(x, y-1);
						save.setLocation(x, y-1);
						path.add(p4);

					}

					if (yr && movementCost[x][y+1] < min){
						min = movementCost[x][y+1];
						Point p5 = new Point(x, y+1);
						save.setLocation(x, y+1);
						path.add(p5);
					}

					if (xb && yl && movementCost[x+1][y-1] < min){
						min = movementCost[x+1][y-1];
						Point p6 = new Point(x+1, y-1);
						save.setLocation(x+1, y-1);
						path.add(p6);
					}

					if (xb && movementCost[x+1][y] < min){
						min = movementCost[x+1][y];
						Point p7 = new Point(x+1, y);
						save.setLocation(x+1, y);
						path.add(p7);
					}

					if (xb && yr && movementCost[x+1][y+1] < min){
						min = movementCost[x+1][y+1];
						Point p8 = new Point(x+1, y+1);
						save.setLocation(x+1, y+1);
						path.add(p8);
					}
					if (min == 0){
						done = true;
					}
					x = save.x;
					y = save.y;
					xt = x > 0;
					xb = x < rows-1;
					yl = y > 0;
					yr = y < columns-1;

				}


				Point next = new Point();
				Point oldP = new Point();
				Point newP = new Point();
				path.remove(path.size()-1);
				for(int i = 0; i < 8; i ++){
					next = path.get(path.size()-1);
					oldP = getPosition();
					move(next);
					x = next.x;
					y = next.y;
					newP = getPosition();
					if(didMove(oldP, newP)){
						ping[x][y] = "O";
					}
					else {
						ping[x][y] = "X";
						break;
					}
					//System.out.println(path.get(path.size()-1).toString());
					path.remove(path.size()-1);
				}
				//get ready to repeat A*!!!!!!!!
				//reset values
				unvisited.clear();
				path.clear();
				visited.clear();
				pStart = getPosition();
				for ( int i = 0 ; i < rows ; i++ ) {
					for ( int j = 0 ; j < columns ; j++ ) {
						movementCost[i][j] = 1000000000;
					}
				}
				movementCost[pStart.x][pStart.y] = 0;

			}
		}
	}


	//determines if the robot successfully moved to a new position
	//true -> spot was open
	//false -> ran into a wall
	public boolean didMove(Point x, Point y) {
		if(x.distance(y) == 0) {
			return false;
		}
		else
			return true;
	}


	//determines whether we trust our guess that a position is a wall
	//we start ignoring the guess when we think we are boxed in
	//aka record the number of times unvisited becomes empty
	//takes an int-> num times unvisited becomes empty without reaching end
	//and a boolean-> true if our guess was x
	public boolean ignoreGuess(int t, boolean x){
		//only applies if the position is x
		double a;
		if (x){
			if (t < 9){
				a = 1/(1+Math.exp(-1*(t-4)));
				if(a < Math.random()){
					return false;//low probability our guess is wrong
				}
				else return true;//high probability our guess is wrong
			}
			else return true;//automatically ignore
		}
		else return false;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			World myWorld = new World("myInputFile8.txt", true);

			MyRobotClass myRobot = new MyRobotClass();
			myRobot.addToWorld(myWorld);

			myWorld.createGUI(500, 500, 1);
			myRobot.travelToDestination();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
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
						//System.out.println("point: " + e.toString() + " min: " + min);
						//check if robot needs to back track
						if(min == 0){
							endGoal = true;
						}
						x = (int)e.getX();
						y  = (int)e.getY();
						mc = movementCost[x][y]+1;

						
						//System.out.println("f: "+ e.toString());
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
						System.out.println("END MOVEMENT COST: " + movementCost[end.x][end.y]);
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
			System.out.println("end" + end.toString());
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
			
			System.out.println("STOP HAMMER tiME");
			for ( int i = 0 ; i < rows ; i++ ) {
				for ( int j = 0 ; j < columns ; j++ ) {
					if (movementCost[i][j] >= 1000000000 )
						System.out.print("X ");
					else
						System.out.print(movementCost[i][j] + " ");
				}
			System.out.println("");
			}
			
			//gets to dead end and then does not turn back
			while(!path.isEmpty()){
				move(path.get(path.size()-1));
				//System.out.println(path.get(path.size()-1).toString());
				path.remove(path.size()-1);
			}
		}

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
			unvisited.put(start, -1);  


			boolean endGoal = false;

			while (!endGoal && !unvisited.isEmpty()) {
				min = Collections.min(unvisited.values()); //store highest priority node


				for (Iterator<Point> itr = unvisited.keySet().iterator(); itr.hasNext();) {
					Point e = itr.next();

					//base case reached - end goal
					if (min == 1 && min == unvisited.get(e)) {
						System.out.println("last state: " + e.toString());
						move(e);
						move(end); //move robot to end
						System.out.println("reached end");
						endGoal = true;
						break;
					}


					if(min == unvisited.get(e)) {
						//System.out.println("point: " + e.toString() + " min: " + min);
						//check if robot needs to back track

						x = (int)e.getX();
						y  = (int)e.getY();
						if(!visited.isEmpty()){
							Point last = visited.get(visited.size()-1); //last point visited
							//System.out.println(last.toString());
							int x1 = (int)last.getX();
							int y1 = (int)last.getY(); 

							int i = 2;
							while(Math.abs(x1-x) >1 || Math.abs(y1-y) >1){
								//System.out.println("backtracking");
								//backtrack

								//System.out.println("b: " + last.toString());
								last = visited.get(visited.size()-i);
								move(last);
								x1 = (int)last.getX();
								y1 = (int)last.getY();
								i++;
							}
						}
						move(e);//move to next spot
						//System.out.println("f: "+ e.toString());
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
							if (pingMap(p7).equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								movementCost[x+1][y] = 10;
							}
							if (pingMap(p5).equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								movementCost[x][y+1] = 10;
							}
							if (pingMap(p8).equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								movementCost[x+1][y+1] = 14;

							}	
						}

						else if (y == columns-1 && x == 0) { //top right
							if (pingMap(p4).equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								movementCost[x][y-1] = 10;

							}
							if (pingMap(p6).equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								movementCost[x+1][y-1] = 14;
							}
							if (pingMap(p7).equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								movementCost[x+1][y] = 10;
							}
						}

						else if (y == 0 && x == rows-1) { //bottom left
							if (pingMap(p3).equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								movementCost[x-1][y+1] = 14;
							}
							if (pingMap(p2).equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								movementCost[x-1][y] = 10;
							}
							if (pingMap(p5).equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								movementCost[x][y+1] = 10;
							}
						}

						else if (y == columns-1 && x == rows-1) { //bottom right
							if (pingMap(p1).equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								movementCost[x-1][y-1] = 14;
							}
							if (pingMap(p2).equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								movementCost[x-1][y] = 10;
							}
							if (pingMap(p4).equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								movementCost[x][y-1] = 10;
							}
						}

						//top row cases
						else if(x == 0) {
							//left and right
							if (pingMap(p4).equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								movementCost[x][y-1] = 10;
							}
							if (pingMap(p5).equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								movementCost[x][y+1] = 10;
							}

							//bottom
							if (pingMap(p6).equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								movementCost[x+1][y-1] = 14;
							}
							if (pingMap(p7).equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								movementCost[x+1][y] = 10;
							}
							if (pingMap(p8).equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								movementCost[x+1][y+1] = 14;
							}
						}

						//bottom row cases
						else if (x == rows-1) {
							//top
							if (pingMap(p1).equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								movementCost[x-1][y-1] = 14;
							}
							if (pingMap(p2).equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								movementCost[x-1][y] = 10;
							}
							if (pingMap(p3).equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								movementCost[x-1][y+1] = 14;
							}

							//left and right
							if (pingMap(p4).equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								movementCost[x][y-1] = 10;
							}
							if (pingMap(p5).equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								movementCost[x][y+1] = 10;
							}	
						}

						//left row cases
						else if(y == 0) {
							if (pingMap(p2).equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								movementCost[x-1][y] = 10;
							}
							if (pingMap(p3).equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								movementCost[x-1][y+1] = 14;
							}

							//left and right
							if (pingMap(p5).equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								movementCost[x][y+1] = 10;
							}

							//bottom
							if (pingMap(p7).equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								movementCost[x+1][y] = 10;
							}
							if (pingMap(p8).equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								movementCost[x+1][y+1] = 14;
							}
						}

						else if(y == columns-1) {
							if (pingMap(p1).equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								movementCost[x-1][y-1] = 14;
							}
							if (pingMap(p2).equals("O") && !visited.contains(p2)) {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								movementCost[x-1][y] = 10;
							}

							//left and right
							if (pingMap(p4).equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								movementCost[x][y-1] = 10;
							}

							//bottom
							if (pingMap(p6).equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								movementCost[x+1][y-1] = 14;
							}
							if (pingMap(p7).equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								movementCost[x+1][y] = 10;
							}
						}

						//right row cases
						else { //normal, non-edge case coordinate
							//top

							if (pingMap(p1).equals("O") && !visited.contains(p1)) {
								unvisited.put(p1, manhattanDistance[x-1][y-1]);
								movementCost[x-1][y-1] = 14;
							}
							if (pingMap(p2).equals("O") && !visited.contains(p2))  {
								unvisited.put(p2, manhattanDistance[x-1][y]);
								movementCost[x-1][y] = 10;
							}
							if (pingMap(p3).equals("O") && !visited.contains(p3)) {
								unvisited.put(p3, manhattanDistance[x-1][y+1]);
								movementCost[x-1][y+1] = 14;
							}

							//left and right
							if (pingMap(p4).equals("O") && !visited.contains(p4)) {
								unvisited.put(p4, manhattanDistance[x][y-1]);
								movementCost[x][y-1] = 10;
							}
							if (pingMap(p5).equals("O") && !visited.contains(p5)) {
								unvisited.put(p5, manhattanDistance[x][y+1]);
								movementCost[x][y+1] = 10;
							}

							//bottom
							if (pingMap(p6).equals("O") && !visited.contains(p6)) {
								unvisited.put(p6, manhattanDistance[x+1][y-1]);
								movementCost[x+1][y-1] = 14;
							}
							if (pingMap(p7).equals("O") && !visited.contains(p7)) {
								unvisited.put(p7, manhattanDistance[x+1][y]);
								movementCost[x+1][y] = 10;
							}
							if (pingMap(p8).equals("O") && !visited.contains(p8)) {
								unvisited.put(p8, manhattanDistance[x+1][y+1]);
								movementCost[x+1][y+1] = 14;
							}
						}
						break;
					}


					for ( int i = 0 ; i < rows ; i++ )
						for ( int j = 0 ; j < columns ; j++ )
							f[i][j] = (int)manhattanDistance[i][j] + (int)movementCost[i][j]; //populate f with g + h

					//				    System.out.println("visited: " + visited.toString());
					//
					//					System.out.println("unvisited: " + unvisited.toString());
					//					for (int i = 0; i < rows; i++) {
					//						for (int j = 0; j < columns; j++) {
					//								System.out.print(movementCost[i][j] + " "); //populate with movement distance for walls
					//							}
					//						System.out.println();
					//					}
					//System.out.println(Arrays.deepToString(f));
				}
			}

		}
	}









	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			World myWorld = new World("myInputFile4.txt", false);

			MyRobotClass myRobot = new MyRobotClass();
			myRobot.addToWorld(myWorld);

			myWorld.createGUI(300, 300, 400);
			myRobot.travelToDestination();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}
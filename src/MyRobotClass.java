import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map.Entry;

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
		HashMap<Point, Double> unvisited = new HashMap<Point, Double>();

		double min = 0; 
		ArrayList<Point> adjacent = new ArrayList<Point>();

		double manhattanDistance[][] = new double[rows][columns]; //g-values for each location

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				manhattanDistance[i][j] = Point.distance((double)i, (double)j, end.getX(), end.getY());
			}
		}

		int numMoves;

		int x, y; 
		Point remove = new Point();


		unvisited.put(start, 0.0);  

		Iterator<Point> itr = unvisited.keySet().iterator(); //something to look @ it if bugs

		while (!unvisited.isEmpty()) {
			min = Collections.min(unvisited.values()); //store highest priority node
			while (itr.hasNext()) {
				Point e = itr.next();


				if(min == unvisited.get(e)) {
					visited.add(e); //add key to visited, start processing
					x = (int)e.getX();
					y  = (int)e.getY();

					Point p1 = new Point(x-1, y-1);
					Point p2 = new Point(x, y-1);
					Point p3 = new Point(x+1, y-1);
					Point p4 = new Point(x-1, y);
					Point p5 = new Point(x+1, y);
					Point p6 = new Point(x-1, y+1);
					Point p7 = new Point(x, y+1);
					Point p8 = new Point(x+1, y+1);
					//1 2 3
					//4 x 5
					//6 7 8
					//corner cases
					if (x == 0 && y == 0) { //top left
						if (pingMap(p7) == "O")
							unvisited.put(p7, manhattanDistance[(int)(x)][(int)(y+1)]);
						if (pingMap(p5) == "O")
							unvisited.put(p5, manhattanDistance[(int)(x+1)][(int)(y)]);
						if (pingMap(p8) == "O")
							unvisited.put(p8, manhattanDistance[(int)(x+1)][(int)(y+1)]);	
					}

					else if (x == 0 && y == rows-1) { //top right
						if (pingMap(p4) == "O")
							unvisited.put(p4, manhattanDistance[(int)(x-1)][(int)(y)]);
						if (pingMap(p6) == "O")
							unvisited.put(p6, manhattanDistance[(int)(x-1)][(int)(y+1)]);
						if (pingMap(p7) == "O")
							unvisited.put(p7, manhattanDistance[(int)(x)][(int)(y+1)]);
					}

					else if (x == columns-1 && y == 0) { //bottom left
						if (pingMap(p3) == "O")
							unvisited.put(p3, manhattanDistance[(int)(x+1)][(int)(y-1)]);
						if (pingMap(p2) == "O")
							unvisited.put(p2, manhattanDistance[(int)(x)][(int)(y-1)]);
						if (pingMap(p5) == "O")
							unvisited.put(p5, manhattanDistance[(int)(x+1)][(int)(y)]);
					}

					else if (x == columns-1 && y == rows-1) { //bottom right
						if (pingMap(p1) == "O")
							unvisited.put(p1, manhattanDistance[(int)(x-1)][(int)(y-1)]);
						if (pingMap(p2) == "O")
							unvisited.put(p2, manhattanDistance[(int)(x)][(int)(y-1)]);
						if (pingMap(p4) == "O")
							unvisited.put(p4, manhattanDistance[(int)(x-1)][(int)(y)]);
					}

					//top row cases
					else if(y == 0) {
						//left and right
						if (pingMap(p4) == "O")
							unvisited.put(p4, manhattanDistance[(int)(x-1)][(int)(y)]);
						if (pingMap(p5) == "O")
							unvisited.put(p5, manhattanDistance[(int)(x+1)][(int)(y)]);

						//bottom
						if (pingMap(p6) == "O")
							unvisited.put(p6, manhattanDistance[(int)(x-1)][(int)(y+1)]);
						if (pingMap(p7) == "O")
							unvisited.put(p7, manhattanDistance[(int)(x)][(int)(y+1)]);
						if (pingMap(p8) == "O")
							unvisited.put(p8, manhattanDistance[(int)(x+1)][(int)(y+1)]);
					}

					//bottom row cases
					else if (y == rows-1) {
						//top
						if (pingMap(p1) == "O")
							unvisited.put(p1, manhattanDistance[(int)(x-1)][(int)(y-1)]);
						if (pingMap(p2) == "O")
							unvisited.put(p2, manhattanDistance[(int)(x)][(int)(y-1)]);
						if (pingMap(p3) == "O")
							unvisited.put(p3, manhattanDistance[(int)(x+1)][(int)(y-1)]);

						//left and right
						if (pingMap(p4) == "O")
							unvisited.put(p4, manhattanDistance[(int)(x-1)][(int)(y)]);
						if (pingMap(p5) == "O")
							unvisited.put(p5, manhattanDistance[(int)(x+1)][(int)(y)]);	
					}

					//left row cases
					else if(x == 0) {
						if (pingMap(p2) == "O")
							unvisited.put(p2, manhattanDistance[(int)(x)][(int)(y-1)]);
						if (pingMap(p3) == "O")
							unvisited.put(p3, manhattanDistance[(int)(x+1)][(int)(y-1)]);

						//left and right
						if (pingMap(p5) == "O")
							unvisited.put(p5, manhattanDistance[(int)(x+1)][(int)(y)]);

						//bottom
						if (pingMap(p7) == "O")
							unvisited.put(p7, manhattanDistance[(int)(x)][(int)(y+1)]);
						if (pingMap(p8) == "O")
							unvisited.put(p8, manhattanDistance[(int)(x+1)][(int)(y+1)]);
					}

					else if(x == columns-1) {
						if (pingMap(p1) == "O")
							unvisited.put(p1, manhattanDistance[(int)(x-1)][(int)(y-1)]);
						if (pingMap(p2) == "O")
							unvisited.put(p2, manhattanDistance[(int)(x)][(int)(y-1)]);

						//left and right
						if (pingMap(p4) == "O")
							unvisited.put(p4, manhattanDistance[(int)(x-1)][(int)(y)]);

						//bottom
						if (pingMap(p6) == "O")
							unvisited.put(p6, manhattanDistance[(int)(x-1)][(int)(y+1)]);
						if (pingMap(p7) == "O")
							unvisited.put(p7, manhattanDistance[(int)(x)][(int)(y+1)]);
					}

					//right row cases
					else { //normal, non-edge case coordinate
						//top
						if (pingMap(p1) == "O")
							unvisited.put(p1, manhattanDistance[(int)(x-1)][(int)(y-1)]);
						if (pingMap(p2) == "O")
							unvisited.put(p2, manhattanDistance[(int)(x)][(int)(y-1)]);
						if (pingMap(p3) == "O")
							unvisited.put(p3, manhattanDistance[(int)(x+1)][(int)(y-1)]);

						//left and right
						if (pingMap(p4) == "O")
							unvisited.put(p4, manhattanDistance[(int)(x-1)][(int)(y)]);
						if (pingMap(p5) == "O")
							unvisited.put(p5, manhattanDistance[(int)(x+1)][(int)(y)]);

						//bottom
						if (pingMap(p6) == "O")
							unvisited.put(p6, manhattanDistance[(int)(x-1)][(int)(y+1)]);
						if (pingMap(p7) == "O")
							unvisited.put(p7, manhattanDistance[(int)(x)][(int)(y+1)]);
						if (pingMap(p8) == "O")
							unvisited.put(p8, manhattanDistance[(int)(x+1)][(int)(y+1)]);
					}

				}
				unvisited.remove(e); //remove from unvisited to indicate processing complete

			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			World myWorld = new World("myInputFile2.txt", false);

			MyRobotClass myRobot = new MyRobotClass();
			myRobot.addToWorld(myWorld);

			myWorld.createGUI(300, 300, 500);
			myRobot.travelToDestination();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}





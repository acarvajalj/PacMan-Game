package lab12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;

public class Pacman {

	/** representation of the graph as a 2D array */
	private Node[][] maze;
	/** input file name */
	private String inputFileName;
	/** output file name */
	private String outputFileName;
	/** height and width of the maze */
	private int height;
	private int width;
	/** starting (X,Y) position of Pacman */
	private int startX;
	private int startY;

	/** A Node is the building block for a Graph. */
	private static class Node {
		/** the content at this location */
		private char content;
		/** the row where this location occurs */
		private int row;
		/** the column where this location occurs */
		private int col;
		private boolean visited;
		private Node parent;

		public Node(int x, int y, char c) {
			visited = false;
			content = c;
			parent = null;
			this.row = x;
			this.col = y;
		}

		public boolean isWall() {
			return content == 'X';
		}

		public boolean isVisited() {
			return visited;
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public char getContent() {
			return content;
		}
	}

	/** constructor */
	public Pacman(String inputFileName, String outputFileName) {
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		buildGraph();
	}

	private boolean inMaze(int index, int bound) {
		return index < bound && index >= 0;
	}

	/**
	 * helper method to construct the solution path from S to G
	 * NOTE this path is built in reverse order, starting with the goal G.
	 */
	private void backtrack(Node end) {
		// TODO for assignment12
		// build the solution path from S to G
		// NOTE this path is built in reverse order, starting with the goal G.

		Stack<Node> path = new Stack<Node>();
		Node current = end;

		// This is the backtracking part of the code. It is going through the path from
		// the goal to the start
		// and adding each node to the stack.
		// Then it is going to print out the stack in the correct order.
		while (current != null) {
			path.push(current);
			current = current.parent;
		}
		while (!path.isEmpty()) {
			Node n = path.pop();
			if (n.getContent() != 'S' && n.getContent() != 'G') {
				n.content = '.';
			}
		}
	}

	/** writes the solution to file */
	public void writeOutput() {
		// TODO for lab12
		try {
			PrintWriter output = new PrintWriter(new FileWriter(outputFileName));
			// FILL IN
			String s = "";
			s += height + " " + width + "\n";
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					s += maze[i][j].content + " ";
				}
				s += "\n";
			}
			output.print(s);
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		String s = "";
		s += height + " " + width + "\n";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				s += maze[i][j].content + " ";
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * helper method to construct a graph from a given input file;
	 * all member variables (e.g. maze, startX, startY) should be
	 * initialized by the end of this method
	 */
	private void buildGraph() {
		// TODO for lab12
		try {
			// don't forget to close input when you're done
			BufferedReader input = new BufferedReader(
					new FileReader(inputFileName));
			// FILL IN
			String[] fline = input.readLine().split(" ");
			this.height = Integer.parseInt(fline[0]);
			this.width = Integer.parseInt(fline[1]);

			this.maze = new Node[height][width];
			for (int r = 0; r < height; r++) {
				String currentLine = input.readLine();
				for (int c = 0; c < width; c++) {
					maze[r][c] = new Node(r, c, currentLine.charAt(c));
					if (currentLine.charAt(c) == 'S') {
						this.startX = r;
						this.startY = c;

					}
				}
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * obtains all neighboring nodes that have *not* been
	 * visited yet from a given node when looking at the four
	 * cardinal directions: North, South, West, East (IN THIS ORDER!)
	 *
	 * @param currentNode the given node
	 * @return an ArrayList of the neighbors (i.e., successors)
	 */
	public ArrayList<Node> getNeighbors(Node currentNode) {
		// TODO for assignment12
		Node north, south, east, west;
		// 0. Initialize an ArrayList of nodes
		ArrayList<Node> neighbors = new ArrayList<Node>();

		// 1. Inspect the north neighbor
		if (inMaze(currentNode.row - 1, height) && !maze[currentNode.row - 1][currentNode.col].isWall()
				&& !maze[currentNode.row - 1][currentNode.col].isVisited()) {
			north = maze[currentNode.row - 1][currentNode.col];
			neighbors.add(north);
			north.setVisited(true);
			// set the parent of the neighbor to the current node
			north.parent = currentNode;

		}

		// 2. Inspect the south neighbor
		if (inMaze(currentNode.row + 1, height) && !maze[currentNode.row + 1][currentNode.col].isWall()
				&& !maze[currentNode.row + 1][currentNode.col].isVisited()) {
			south = maze[currentNode.row + 1][currentNode.col];
			neighbors.add(south);
			south.setVisited(true);
			// set the parent of the neighbor to the current node
			south.parent = currentNode;
		}

		// 3. Inspect the west neighbor
		if (inMaze(currentNode.col - 1, width) && !maze[currentNode.row][currentNode.col - 1].isWall()
				&& !maze[currentNode.row][currentNode.col - 1].isVisited()) {
			west = maze[currentNode.row][currentNode.col - 1];
			neighbors.add(west);
			west.setVisited(true);
			// set the parent of the neighbor to the current node
			west.parent = currentNode;
		}

		// 4. Inspect the east neighbor
		if (inMaze(currentNode.col + 1, width) && !maze[currentNode.row][currentNode.col + 1].isWall()
				&& !maze[currentNode.row][currentNode.col + 1].isVisited()) {
			east = maze[currentNode.row][currentNode.col + 1];
			neighbors.add(east);
			east.setVisited(true);
			// set the parent of the neighbor to the current node
			east.parent = currentNode;
		}

		return neighbors;
	}

	/** Pacman uses BFS strategy to solve the maze */
	public void solveBFS() {
		// TODO for assignment12
		/*
		 * This method implements the breadth-first search (BFS) strategy Pacman will
		 * use to solve the maze. Recall that we use a queue data structure to implement
		 * BFS. Your first operation on this data structure should be inserting the
		 * starting node S.
		 * Hint: the methods you just implemented in part 1 and 2 will prove very handy.
		 * After you have found the solution path, call writeOutput() to output the
		 * solution to file.
		 */
		// 1. Initialize a queue of nodes
		Queue<Node> q = new LinkedList<Node>();

		Node start = maze[startX][startY];

		// 2. Insert the starting node S
		q.add(start);
		start.setVisited(true);

		// 3. While the queue is not empty
		while (!q.isEmpty()) {
			// 3.1. Dequeue a node from the queue
			Node current = q.remove();
			// 3.2. If the node is the goal node G, then we are done
			if (current.getContent() == 'G') {
				// 3.2.1. Call writeOutput() to output the solution to file
				backtrack(current);
				this.writeOutput();
				return;
			}
			// 3.3. Otherwise, get the neighbors of the node
			ArrayList<Node> neighbors = getNeighbors(current);
			// 3.4. For each neighbor, if it has not been visited, then
			for (Node n : neighbors) {
				// 3.4.1. Enqueue the neighbor
				q.add(n);
			}
		}

	}

	/** Pacman uses DFS strategy to solve the maze */
	public void solveDFS() {
		// TODO for assignment12
		/*
		 * This method implements the depth-first search (DFS) strategy Pacman will use
		 * to solve the maze. Recall that we use a stack data structure to implement
		 * DFS. Your solution here will be very similar to Part 3.
		 */

		// 1. Initialize a stack of nodes
		Stack<Node> s = new Stack<Node>();

		Node start = maze[startX][startY];

		// 2. Push the starting node S
		s.push(start);
		start.setVisited(true);

		// 3. While the stack is not empty
		while (!s.isEmpty()) {
			// 3.1. Pop a node from the stack
			Node current = s.pop();
			// 3.2. If the node is the goal node G, then we are done
			if (current.getContent() == 'G') {
				// 3.2.1. Call writeOutput() to output the solution to file
				backtrack(current);
				this.writeOutput();
				return;
			}
			// 3.3. Otherwise, get the neighbors of the node
			ArrayList<Node> neighbors = getNeighbors(current);
			// 3.4. For each neighbor, if it has not been visited, then
			for (Node n : neighbors) {
				// 3.4.1. Push the neighbor
				s.push(n);
			}
		}

	}

}

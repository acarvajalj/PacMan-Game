package lab12;

public class tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pacman maze1 = new Pacman("mazes/tinyMaze.txt", "outBFS.txt");
		System.out.println("Original graph");
		System.out.println(maze1);
		System.out.println("\nGraph solved using BFS");
		maze1.solveBFS();
		System.out.println(maze1);
		maze1 = new Pacman("mazes/tinyMaze.txt", "outDFS.txt");
		System.out.println("\nGraph solved using DFS");
		maze1.solveDFS();
		System.out.println(maze1);

	}

}

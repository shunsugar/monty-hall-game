import java.util.Random;
import java.util.Scanner;

class Door {
	final int car = 1;
	final int goat = 0;
	private int status;
	private int num;
	Door(int i) { // Constructor
		if(i == 0) { this.status = car; this.num = 0; }
		else { this.status = goat; this.num = i; }
	} // End of Door()
	void print() {
		System.out.println("Status:" + this.status + " Number:" + this.num);
	}
	int getStatus() { return this.status; }
	int getNum() { return this.num; }
}

class Doors {
	int n; // Number of doors
	Door[] doors;
	Doors(int m) { // Constructor
		int i;
		this.n = m;
		this.doors = new Door[m];
		for(i=0; i<m; i++) this.doors[i] = new Door(i);
	} // End of Doors()
	Door extract(int pos) { // Extract door d at position pos
		Door d;
		int i;
		d = this.doors[pos];
		for(i=pos; i<this.n-1; i++) this.doors[i] = this.doors[i+1];
		this.n--;
		return d;
	} // End of extract()
	void insert(int pos, Door d) { // Insert door d at position pos
		int i;
		for(i=this.n-1; i>=pos; i--) this.doors[i+1] = this.doors[i];
		this.doors[pos] = d;
		this.n++;
	} // End of insert()
	void printAll() {
		int i;
		for(i=0; i<this.n; i++) {
			System.out.print((i+1) + ".status: " + this.doors[i].getStatus() + "  ");
		}
		System.out.println("");
	}
}

class Control {
	int win = 0; // Number of wins
	void shuffle(Doors ds) {
		Random rg = new Random();
		Door d;
		int i, pos1, pos2;
		for(i=0; i<10; i++) {
			pos1 = rg.nextInt(ds.n);
			pos2 = rg.nextInt(ds.n);
			if(pos1 != pos2) { d = ds.extract(pos1); ds.insert(pos2, d); }
		}
	} // End of shuffle()
	void playGame(Doors ds, int m) {
		Scanner stdin = new Scanner(System.in);
		int choice, disclosure;
		System.out.println("Match: " + m);
		System.out.println("The three doors have either one car or two goats.");
		System.out.println("You must choose the door with a car.");
		System.out.println("");
		System.out.println("Which door would you choose?");
		System.out.println("1.????  2.????  3.????");
		System.out.print("Please input a number: ");
		choice = stdin.nextInt() - 1;
		System.out.println();
		disclosure = openGoatDoor(ds, choice);
		choice = askChooseAgain(ds, choice, disclosure);
		System.out.println("");
		System.out.println("Your choice: " + (choice+1));
		checkAnswer(ds, choice, m);
	} // End of playGame()
	int openGoatDoor(Doors ds, int c) {
		int i;
		int g = -1;
		String[] dsta = new String[ds.n];
		for(i=0; i<ds.n; i++) dsta[i] = ".????";
		for(i=0; i<ds.n; i++) {
			if(i != c && ds.doors[i].getStatus() == 0) {
				dsta[i] = ".Goat";
				g = i;
				break;
			}
		}
		System.out.println("Here, Door number " + (g+1) + " has a goat.");
		for(i=0; i<ds.n; i++) System.out.print((i+1) + dsta[i] + "  ");
		System.out.println("");
		return g;
	} // End of openGoatDoor()
	int askChooseAgain(Doors ds, int c, int g) {
		final int NO = 0;
		Scanner stdin = new Scanner(System.in);
		int choice, i;
		int other = -1;
		System.out.println("You may change your choice.");
		System.out.print("Do you choose the other door? (Yes->1, No->0): ");
		choice = stdin.nextInt();
		if(choice == NO) { return c; }
		else {
			for(i=0; i<ds.n; i++) {
				if(i != c && i != g) { other = i; return other; }
			}
			return other;
		}
	} // End of askChooseAgain()
	void checkAnswer(Doors ds, int c, int m) {
		int i;
		int ans = -1;
		String sta;
		System.out.println("The correct answer is...");
		for(i=0; i<ds.n; i++) {
			if(ds.doors[i].getStatus() == 0) sta = ".Goat";
			else { sta = ".Car "; ans = i; }
			System.out.print((i+1) + sta + "  ");
		}
		System.out.println("");
		System.out.println("");
		if(c == ans) { 
			System.out.println("You are correct!!");
			this.win++;
		} else { 
			System.out.println("You are NOT correct.");
		}
		System.out.println("Probability of winning: " + (int)((double)this.win/(double)m*100) + "%");
	} // End of checkAnswer()
	void askContinue() {
		final int NO = 0;
		Scanner stdin = new Scanner(System.in);
		int choice;
		System.out.print("Continue? (Yes->1, No->0): ");
		choice = stdin.nextInt();
		if(choice == NO) {
			System.out.print("Finish.");
			System.exit(0);
		}
		System.out.println();
	} // End of askContinue()
}

public class MontyHall {
	public static void main(String[] args) {
		int n = 3; // Number of doors
		int m = 0; // Number of matches
		Control ctl = new Control();
		Doors ds = new Doors(n);
		while(true) {
			m++;
			ctl.shuffle(ds);
			ctl.playGame(ds, m);
			ctl.askContinue();
		}
	}
}

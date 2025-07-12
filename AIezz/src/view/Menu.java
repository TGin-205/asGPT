package view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Menu {

    protected String title;
    protected ArrayList<String> mChon;

    public Menu() {
    }

    public Menu(String td, String[] mc) {
        title = td;
        mChon = new ArrayList<>();
        for (String s : mc) {
            mChon.add(s);
        }
    }

    public void display() {
        System.out.println(title);
        System.out.println("--------------------------------");
        for (int i = 0; i < mChon.size(); i++) {
            System.out.println((i + 1) + "." + mChon.get(i));
        }
        System.out.println("--------------------------------");
    }

    public int getSelected() {
        display();
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter selection..");
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập số!");
                sc.nextLine(); // Clear invalid input
            }
        }
    }

    public abstract void execute(int n);

    public void run() {
        while (true) {
            int n = getSelected();
            if (n <= mChon.size()) {
                execute(n);
            } else {
                break;
            }
        }
    }
}

package main;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import view.MathSolverMenu;

public class MathSolverApp {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.out.println("🚀 Khởi động chương trình...");

        MathSolverMenu menu = new MathSolverMenu();
        menu.run();
    }
}

package PacMan.src;

import PacMan.src.Model;

import javax.swing.*;
public class PacMan extends JFrame{

    public PacMan() {
        add(new Model());
    }
    public static void main(String[] args) {
        PacMan pac = new PacMan();
        pac.setVisible(true);
        pac.setTitle("PacMan");
        pac.setSize(380,420);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pac.setLocationRelativeTo(null);

    }

}
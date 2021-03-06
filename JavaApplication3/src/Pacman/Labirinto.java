/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author 2info2021
 */
public class Labirinto extends JPanel implements ActionListener {

    Ghost ghost[] = new Ghost[4];
    Pacman pacman;
    String msg = "";
    Timer t;
    int seq = 0, ponto = 0, comid = 0, tempovulneravel = 0, vida = 3, record;
    char labirinto[][];
    int altura, largura, passoX, passoY;

    public Labirinto(int largura, int altura) {
        setFocusable(true);
        setDoubleBuffered(true);
        setSize(largura, altura);
        this.altura = altura;
        this.largura = largura;
        passoX = largura / 19;
        passoY = altura / 21;
        t = new Timer(50, this);

        ponto = 0;
        for (int i = 0; i < 4; i++) {
            ghost[i] = new Ghost(i, passoX, passoY);
        }
        pacman = new Pacman(passoX, passoY);
        inicializa();
        try {
            FileReader arq = new FileReader("record.cfg");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            arq.close();
            if (linha != null) {
                record = Integer.parseInt(linha);
            } else {
                record = 0;
            }
        } catch (IOException e) {
            record = 0;
        }
        t.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int tecla = e.getKeyCode();
                if (tecla == KeyEvent.VK_LEFT) {
                    pacman.setDx(-1);
                    pacman.setDy(0);
                }
                if (tecla == KeyEvent.VK_RIGHT) {
                    pacman.setDx(1);
                    pacman.setDy(0);
                }
                if (tecla == KeyEvent.VK_UP) {
                    pacman.setDx(0);
                    pacman.setDy(-1);
                }
                if (tecla == KeyEvent.VK_DOWN) {
                    pacman.setDx(0);
                    pacman.setDy(1);
                }
                if (tecla == KeyEvent.VK_HOME) {
                    for (int i = 0; i < 4; i++) {
                        ghost[i].setStatus(1);
                    }
                }
                if ((tecla == 'P' || tecla == 'p') && vida > 0) {
                    if (t.isRunning()) {
                        msg = "Pausa";
                        repaint();
                        t.stop();
                    } else {
                        msg = "";
                        t.restart();
                    }
                }
                if (tecla == KeyEvent.VK_ESCAPE) {
                    repaint();
                    t.stop();
                    if (JOptionPane.showConfirmDialog(null, "Deseja realmente sair", "Sair", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    } else {
                        t.restart();
                    }
                }
                if ((tecla == 'C' || tecla == 'c') && vida > 0) {
                    msg = "";
                    t.restart();
                }
                if ((tecla == 'R' || tecla == 'r')) {
                    vida = 3;
                    ponto = 0;
                    inicializa();
                    msg = "";
                    t.restart();
                }
            }
        });
    }

    private void inicializa() {
        comida = 0;
        inicializaLabirinto();
        for (int i = 0; i < 4; i++) {
            ghost[i].setX(9);
            ghost[i].setY(10);
            ghost[i].setStatus(0);
        }
        pacman.setX(9);
        pacman.setY(16);
        pacman.setDx(1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tempovulneravel++;
        if (labirinto[pacman.getY()][pacman.getX()] == 'c') {
            labirinto[pacman.getY()][pacman.getX()] = ' ';
            ponto++;
            comida++;
        }
        for (int i = 0; i < 4; i++) {
            if (ghost[i].getStatus() == 1 && tempovulneravel > 200) {
                ghost[i].setStatus(0);
            }
        }
        if (labirinto[pacman.getY()][pacman.getX()] == 'o') {
            labirinto[pacman.getY()][pacman.getX()] = ' ';
            tempovulneravel = 0;
            ponto += 5;
            for (int i = 0; i < 4; i++) {
                if (ghost[i].getStatus() == 0) {
                    ghost[i].setStatus(1);
                }
            }
        }
    }
}

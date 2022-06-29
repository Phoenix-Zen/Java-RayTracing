package fr.phoenix;

import fr.phoenix.engine.GraphicEngine;
import fr.phoenix.engine.listener.EngineKeyBoardListener;
import fr.phoenix.engine.listener.EngineMouseListener;
import fr.phoenix.engine.vector.Vector2;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Display extends Canvas implements Runnable {

    private final GraphicEngine engine;
    private final EngineMouseListener mListener;
    private final EngineKeyBoardListener kListener;
    private Thread thread;
    private JFrame frame;
    private static String title = "Engine";
    @Getter
    private static int WIDTH = 1200;
    @Getter
    private static int HEIGHT = 1200;

    @Getter
    private static Vector2 dimension;
    private static boolean running = false;

    public Display() {
        dimension = new Vector2(WIDTH, HEIGHT);
        this.engine = new GraphicEngine(this);
        this.frame = new JFrame();

        this.mListener = new EngineMouseListener(engine);
        this.kListener = new EngineKeyBoardListener(engine);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setTitle(title);
        JPanel panel = new JPanel();
        panel.add(this);
        this.frame.add(panel);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null));
        this.frame.setVisible(true);
        panel.addMouseListener(mListener);
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        panel.getInputMap().put(KeyStroke.getKeyStroke('w'), "forward");
        panel.getInputMap().put(KeyStroke.getKeyStroke('a'), "left");
        panel.getInputMap().put(KeyStroke.getKeyStroke('s'), "backward");
        panel.getInputMap().put(KeyStroke.getKeyStroke('d'), "right");
        panel.getActionMap().put("forward", new EngineKeyBoardListener(engine));
        panel.getActionMap().put("left", new EngineKeyBoardListener(engine));
        panel.getActionMap().put("backward", new EngineKeyBoardListener(engine));
        panel.getActionMap().put("right", new EngineKeyBoardListener(engine));
    }

    public synchronized void start(){
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }
    public synchronized void stop(){
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int targetFPS = 60;
    int frames = 0;
    int fps = 0;
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0/ targetFPS;
        double delta = 0;
        while (running){
            long now = System.nanoTime();
            delta+=(now - lastTime) / ns;
            lastTime = now;
            while (delta>=1) {
                update();
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000){
                timer+=1000;
                fps = frames;
                this.frame.setTitle(title+" | "+frames+" fps");
                Graphics g = getBufferStrategy().getDrawGraphics();
                frames = 0;
            }
        }
        stop();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0,0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        engine.paint(g);
        g.setColor(Color.BLACK);
        g.drawString("FPS "+ fps, 0,12);
        g.dispose();
        bs.show();
    }

    private void update(){
        engine.update();
        mListener.update();
    }
}

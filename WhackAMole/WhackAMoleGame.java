/**
     *   Homework    7   solution    to Whack a mole Game.
     *   @author    HaryKrishnan    Ramasubramanian    (hramasub@andrew.cmu.edu).
     */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.Font;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Game Class to implement Whack a Mole.
 */
public class Game {

    /**
     * button1 for start button.
     */
    private  JButton button1;
    /**
     * field1 and field2 for Time and Score labels.
     */
    private  JTextField field1, field2;
    /**
     * countdown for timer.
     */
    private int countdown = 20;
    /**
     * points for score.
     */
    private Integer points = 0;
    /**
     * Down configuration string constant.
     */
    private static final String DOWN_CONFIG = "   ";
    /**
     * Hit configuration string constant.
     */
    private static final String HIT_CONFIG = ":-(";
    /**
     * Up configuration string constant.
     */
    private static final String UP_CONFIG = ":-)";
    /**
     * Down color constant.
     */
    private static final Color DOWN_COLOR = Color.LIGHT_GRAY;
    /**
     * Hit color constant.
     */
    private static final Color HIT_COLOR = Color.RED;
    /**
     * Up color constant.
     */
    private static final Color UP_COLOR = Color.GREEN;


    /**
     * Array of buttons to show on and off.
     */
    private JButton[] buttons = new JButton[64];


    /**
     * To note current time in milliseconds.
     */
    private long startB;
    /**
     * To note current time in milliseconds.
     */
    private long startA;

    /**
    /**
     * Game Constructor.
    */
    public Game() {
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 14);

        JFrame frame = new JFrame("Whack-a-mole");
        frame.setSize(650, 560);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pane0 = new JPanel();
        pane0.setLayout(new BorderLayout(30, 20));
        JPanel pane1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel pane2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 20));

        button1 = new JButton("Start");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button1.setEnabled(false);
                MyRunnable1 r = new MyRunnable1();
                Thread t1 = new Thread(r);
                t1.setName("Timer Thread");
                t1.start();
           }
        });

        pane1.add(button1);
        JLabel labeL1 = new JLabel("Time Left:");
        labeL1.setFont(font);
        field1 = new JTextField(8);
        field1.setEditable(false);
        JLabel labeL2 = new JLabel("Score:");
        labeL2.setFont(font);
        field2 = new JTextField(8);
        field2.setEditable(false);
        pane1.add(labeL1);
        pane1.add(field1);
        pane1.add(labeL2);
        pane1.add(field2);


        for (int i = 0; i < buttons.length; i++) {

            buttons[i] = new JButton(DOWN_CONFIG);
            buttons[i].setBackground(DOWN_COLOR);
            buttons[i].setFont(font);
            buttons[i].setOpaque(true);
            pane2.add(buttons[i]);
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < buttons.length; i++) {
                        if (countdown > 0) {
                                if (buttons[i].getText().equals(UP_CONFIG) && e.getSource() == buttons[i]) {
                                   synchronized (points) {
                                        points++;
                                        field2.setText(points + "");
                                        buttons[i].setText(HIT_CONFIG);
                                        buttons[i].setBackground(HIT_COLOR);
                                        startB = System.currentTimeMillis();

                                   }
                                }
                        }
                    }
                }
            });

        }

        pane0.add(pane1, BorderLayout.NORTH);
        pane0.add(pane2, BorderLayout.CENTER);
        frame.setContentPane(pane0);
        frame.setVisible(true);
        frame.setResizable(false);

    }

    /**
     * Whack a mole Game main method.
     * @param args command line input.
     */
    public static void main(String[] args) {
        new Game();
    }
    /**
     * MyRunnable1 class implements Runnable interface.
     */
    private class MyRunnable1 implements Runnable {
        /**
         * SleepTimeDuringGame sleep time for timer.
         */
        private int sleepTimeDuringGame = 1000;
        /**
         * SleepTimeAfterGame after game ends.
         */
        private int sleepTimeAfterGame = 5000;
        /**
         * Constructor.
         */
        public MyRunnable1() {

        }

        /**
         * Implementation of run method of Runnable Interface.
         */
        @Override
        public void run() {
                Thread[] t2 = new Thread[buttons.length];
                for (int i = 0; i < t2.length; i++) {
                    JButton button = buttons[i];
                    t2[i] = new MyHelperThread(button);
                    t2[i].start();
                }
                try {
                while (countdown > 0) {
                    field1.setText(countdown + "");
                    Thread.sleep(sleepTimeDuringGame);
                    --countdown;
                }

            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
            field1.setText("Game Over !");
            field2.setText("Final score: " + points);
            try {
                Thread.sleep(sleepTimeAfterGame);
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
            countdown = 20;
            points = 0;
            button1.setEnabled(true);
            field1.setText("");
            field2.setText("");
        }
    }
    /**
     * MyHelperThread class extends Thread class.
     */
    private class MyHelperThread extends Thread {
        /**
         * button for each button of the game.
         */
        private JButton button;
        /**
         * upTime2 is the upper bound time.
         */
        private int upTime2 = 4000;
        /**
         * upTime1 is the lower bound time.
         */
        private int upTime1 = 1000;
        /**
         * downTime is the time for transitioning from down to up.
         */
        private int downTime = 2000;
        /**
         * randomUpTime generates a range of 1 to 4 seconds.
         */
        private int randomUpTime = ThreadLocalRandom.current().nextInt(upTime1, upTime2 + 1);


        /**
         * Constructor.
         * @param button for one button from the array of buttons.
         */
        public MyHelperThread(JButton button) {
            this.button = button;
        }

        /**
         * Implementation of run method of Thread Class.
         */
        @Override
        public void run() {
            try {
                while (countdown > 0) {

                    if (button.getText().equals(DOWN_CONFIG)) {
                        button.setText(UP_CONFIG);
                        button.setBackground(UP_COLOR);
                        startA = System.currentTimeMillis();
                    } else if (button.getText().equals(HIT_CONFIG)) {
                        Thread.sleep(randomUpTime - (int) (startB - startA));
                        button.setText(DOWN_CONFIG);
                        button.setBackground(DOWN_COLOR);
                    } else {
                        Thread.sleep(randomUpTime);
                        button.setText(DOWN_CONFIG);
                        button.setBackground(DOWN_COLOR);
                        Thread.sleep(downTime);
                    }
                }
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
    }
}

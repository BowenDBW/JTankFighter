package com.ProcessUnit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ticker implements Runnable{

    private ActionListener al;
    private final int delay;
    private boolean isTicking;

    public Ticker(int i) {

        delay = i;
        Thread t = new Thread(this);
        t.start();
        isTicking = false;
    }

    public void addActionListener(ActionListener actionlistener) {
        if (al == null) {

            al = actionlistener;
        } else {

            System.out.println("WARNING: ActionListener already added to Ticker.");
        }
    }

    public void start() {

        isTicking = true;
    }

    public void stop() {

        isTicking = false;
    }

    private void fireActionPerformed() {

        if (!(al == null || !isTicking)) {

            ActionEvent actionevent = new ActionEvent(this, 0, null);
            al.actionPerformed(actionevent);
        }
    }

    @SuppressWarnings("all")
    public void run() {
        do {

            fireActionPerformed();
            try {

                Thread.sleep(delay);
            } catch (InterruptedException interruptedexception) {

                System.out.println("WARNING: Ticker thread interrupted.");
            }
        } while (true);
    }
}



package com.client.ConponentPack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ticker implements Runnable {
    private ActionListener al;
    private Thread t;
    private int delay;
    private boolean isTicking;

    public ActionListener getAl() {
        return al;
    }

    public void setAl(ActionListener al) {
        this.al = al;
    }

    public Thread getT() {
        return t;
    }

    public void setT(Thread t) {
        this.t = t;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isTicking() {
        return isTicking;
    }

    public void setTicking(boolean ticking) {
        isTicking = ticking;
    }

    public Ticker(int i) {
        delay = i;
        t = new Thread(this);
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

    @Override
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


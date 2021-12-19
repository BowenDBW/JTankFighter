package com.server.ComponentPack;

import java.awt.*;

public interface GameComponent {

	void draw(Graphics g);

	void move();

	String getType();

	Rectangle getBorder();

	Rectangle[] getDetailedBorder();

	boolean wallDestroyed();
}
package com.server.ComponentPack;

import java.awt.*;

public interface Actor {
	void draw(Graphics g);

	void move();

	String getType();

	Rectangle getBorder();

	Rectangle[] getDetailedBorder();

	boolean wallDestroyed();
}
package com.SourceUnit.ServerPack;

import java.awt.*;

public interface ServerGameComponent {

	void draw(Graphics g);

	void move();

	String getType();

	Rectangle getBorder();

	Rectangle[] getDetailedBorder();

	boolean wallDestroyed();
}
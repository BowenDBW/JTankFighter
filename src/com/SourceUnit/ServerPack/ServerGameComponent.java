package com.SourceUnit.ServerPack;

import java.awt.*;

/**
 * @author chenhong
 */
public interface ServerGameComponent {

	/**
	 * 绘制画面
	 * @param g draw
	 */
	void draw(Graphics g);

	/**
	 * 处理
	 */
	void move();

	/**
	 * 获取类型
	 * @return type
	 */
	String getType();

	/**
	 * 获取边界
	 * @return border
	 */
	Rectangle getBorder();

	/**
	 * 获取细节边界
	 * @return detail border
	 */
	Rectangle[] getDetailedBorder();

	/**
	 * 击毁墙处理
	 * @return boolean
	 */
	boolean wallDestroyed();
}
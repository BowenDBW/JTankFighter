package com.server.ComponentPack;

import com.ProcessUnit.Instruction;
import com.server.ServerUnit.ServerModel;

import java.awt.*;

public class Wall implements GameComponent {
    private final int xPos;
    private final int yPos;
    private final Rectangle[] border = new Rectangle[4];
    public boolean[] shape = new boolean[16];
    private boolean wallDestroyed;
    private boolean bulletDestroyed;
    private final Image wall = Toolkit.getDefaultToolkit().getImage("image\\71.jpg");
    private final Rectangle generalBorder;

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean isWallDestroyed() {
        return wallDestroyed;
    }

    public void setWallDestroyed(boolean wallDestroyed) {
        this.wallDestroyed = wallDestroyed;
    }

    public boolean isBulletDestroyed() {
        return bulletDestroyed;
    }

    public Wall(int a, int b) {
        xPos = a;
        yPos = b;
        generalBorder = new Rectangle(xPos - 12, yPos - 12, 25, 25);
        border[0] = new Rectangle(xPos - 11, yPos - 11, 11, 11);
        border[1] = new Rectangle(xPos + 1, yPos - 11, 11, 11);
        border[2] = new Rectangle(xPos - 11, yPos + 1, 11, 11);
        border[3] = new Rectangle(xPos + 1, yPos + 1, 11, 11);
    }

    public Wall(int a, int b, int orientation) {
        xPos = a;
        yPos = b;
        generalBorder = new Rectangle(xPos - 12, yPos - 12, 25, 25);
        if (orientation == 0) {
            border[0] = new Rectangle(xPos - 11, yPos - 11, 11, 11);
            border[1] = new Rectangle(xPos + 1, yPos - 11, 11, 11);
            for (int i = 8; i < 12; i++) {
                shape[i] = true;
            }
            for (int i = 12; i < 16; i++) {
                shape[i] = true;
            }
        }
        if (orientation == 1) {
            border[2] = new Rectangle(xPos - 11, yPos + 1, 11, 11);
            border[3] = new Rectangle(xPos + 1, yPos + 1, 11, 11);
            for (int i = 0; i < 4; i++) {
                shape[i] = true;
            }
            for (int i = 4; i < 8; i++) {
                shape[i] = true;
            }
        }
        if (orientation == 2) {
            border[0] = new Rectangle(xPos - 11, yPos - 11, 11, 11);
            border[2] = new Rectangle(xPos - 11, yPos + 1, 11, 11);
            for (int i = 3; i <= 15; i += 4) {
                shape[i] = true;
            }
            for (int i = 2; i <= 14; i += 4) {
                shape[i] = true;
            }
        }
        if (orientation == 3) {
            border[1] = new Rectangle(xPos + 1, yPos - 11, 11, 11);
            border[3] = new Rectangle(xPos + 1, yPos + 1, 11, 11);
            for (int i = 1; i <= 13; i += 4) {
                shape[i] = true;
            }
            for (int i = 0; i <= 12; i += 4) {
                shape[i] = true;
            }
        }
    }


    public void damageWall(Rectangle bullet, int bulletPower, int bulletDirection) {

        bulletDestroyed = false;

        if (bulletPower == 1) {
            if (border[0] != null && border[1] != null && bulletDirection == 1) {
                if (bullet.intersects(border[0]) && bullet.intersects(border[1])) {
                    if (shape[1] && shape[2]) {
                        for (int i = 4; i < 8; i++) {
                            shape[i] = true;
                        }
                        border[0] = null;
                        border[1] = null;
                    }
                    if (!shape[1] || !shape[2]) {
                        for (int i = 0; i < 4; i++) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[0] != null && border[1] != null && bulletDirection == 0) {
                if (bullet.intersects(border[0]) && bullet.intersects(border[1])) {
                    if (shape[5] && shape[6]) {
                        for (int i = 0; i < 4; i++) {
                            shape[i] = true;
                        }
                        border[0] = null;
                        border[1] = null;
                    }
                    if (!shape[5] || !shape[6]) {
                        for (int i = 4; i < 8; i++) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[2] != null && border[3] != null && bulletDirection == 1) {
                if (bullet.intersects(border[2]) && bullet.intersects(border[3])) {
                    if (shape[9] && shape[10]) {
                        for (int i = 12; i < 16; i++) {
                            shape[i] = true;
                        }
                        border[2] = null;
                        border[3] = null;
                    }
                    if (!shape[9] || !shape[10]) {
                        for (int i = 8; i < 12; i++) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[2] != null && border[3] != null && bulletDirection == 0) {
                if (bullet.intersects(border[2]) && bullet.intersects(border[3])) {
                    if (shape[13] && shape[14]) {
                        for (int i = 8; i < 12; i++) {
                            shape[i] = true;
                        }
                        border[2] = null;
                        border[3] = null;
                    }
                    if (!shape[13] || !shape[14]) {
                        for (int i = 12; i < 16; i++) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[0] != null && border[2] != null && bulletDirection == 3) {
                if (bullet.intersects(border[0]) && bullet.intersects(border[2])) {
                    if (shape[4] && shape[8]) {
                        for (int i = 1; i <= 13; i += 4) {
                            shape[i] = true;
                        }
                        border[0] = null;
                        border[2] = null;
                    }
                    if (!shape[4] || !shape[8]) {
                        for (int i = 0; i <= 12; i += 4) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[0] != null && border[2] != null && bulletDirection == 2) {
                if (bullet.intersects(border[0]) && bullet.intersects(border[2])) {
                    if (shape[5] && shape[9]) {
                        for (int i = 0; i <= 12; i += 4) {
                            shape[i] = true;
                        }
                        border[0] = null;
                        border[2] = null;
                    }
                    if (!shape[5] || !shape[9]) {
                        for (int i = 1; i <= 13; i += 4) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[1] != null && border[3] != null && bulletDirection == 3) {
                if (bullet.intersects(border[1]) && bullet.intersects(border[3])) {
                    if (shape[6] && shape[10]) {
                        for (int i = 3; i <= 15; i += 4) {
                            shape[i] = true;
                        }
                        border[1] = null;
                        border[3] = null;
                    }
                    if (!shape[6] || !shape[10]) {
                        for (int i = 2; i <= 14; i += 4) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
            if (border[1] != null && border[3] != null && bulletDirection == 2) {
                if (bullet.intersects(border[1]) && bullet.intersects(border[3])) {
                    if (shape[7] && shape[11]) {
                        for (int i = 2; i <= 14; i += 4) {
                            shape[i] = true;
                        }
                        border[1] = null;
                        border[3] = null;
                    }
                    if (!shape[7] || !shape[11]) {
                        for (int i = 3; i <= 15; i += 4) {
                            shape[i] = true;
                        }
                    }
                    bulletDestroyed = true;
                }
            }
        }

        if (bulletPower == 2) {
            if (border[0] != null && border[1] != null && (bulletDirection == 0 || bulletDirection == 1)) {
                if (bullet.intersects(border[0]) && bullet.intersects(border[1])) {
                    for (int i = 0; i < 8; i++) {
                        shape[i] = true;
                    }
                    border[0] = null;
                    border[1] = null;
                    bulletDestroyed = true;
                }
            }
            if (border[2] != null && border[3] != null && (bulletDirection == 0 || bulletDirection == 1)) {
                if (bullet.intersects(border[2]) && bullet.intersects(border[3])) {
                    for (int i = 8; i < 16; i++) {
                        shape[i] = true;
                    }
                    border[2] = null;
                    border[3] = null;
                    bulletDestroyed = true;
                }
            }
            if (border[0] != null && border[2] != null && (bulletDirection == 2 || bulletDirection == 3)) {
                if (bullet.intersects(border[0]) && bullet.intersects(border[2])) {
                    for (int i = 0; i <= 12; i += 4) {
                        shape[i] = true;
                    }
                    for (int i = 1; i <= 13; i += 4) {
                        shape[i] = true;
                    }
                    border[0] = null;
                    border[2] = null;
                    bulletDestroyed = true;
                }
            }
            if (border[1] != null && border[3] != null && (bulletDirection == 2 || bulletDirection == 3)) {
                if (bullet.intersects(border[1]) && bullet.intersects(border[3])) {
                    for (int i = 2; i <= 14; i += 4) {
                        shape[i] = true;
                    }
                    for (int i = 3; i <= 15; i += 4) {
                        shape[i] = true;
                    }
                    border[1] = null;
                    border[3] = null;
                    bulletDestroyed = true;
                }
            }
        }


//*******************************************************************************************		if(border[0] != null ){
        if (border[0] != null) {
            Rectangle a = new Rectangle(border[0].x, border[0].y, 5, 5);
            Rectangle b = new Rectangle(border[0].x + 7, border[0].y, 5, 5);
            Rectangle c = new Rectangle(border[0].x, border[0].y + 7, 5, 5);
            Rectangle d = new Rectangle(border[0].x + 7, border[0].y + 7, 5, 5);
            if (bullet.intersects(border[0]) && bulletDirection == 1
					&& !(bullet.intersects(b) && (!shape[2] || !shape[6] || shape[1] && shape[5]))) {
                if (bullet.intersects(a) && shape[0] && !shape[4]) {
                    shape[4] = true;
                    shape[5] = true;
                    bulletDestroyed = true;
                    border[0] = null;
                }
                if (bullet.intersects(b) && shape[1] && !shape[5]) {
                    shape[4] = true;
                    shape[5] = true;
                    bulletDestroyed = true;
                    border[0] = null;
                }
                if (bullet.intersects(a) && !shape[0]) {
                    if (bulletPower == 1) {
                        shape[0] = true;
                        shape[1] = true;
                    }
                    if (bulletPower == 2) {
                        shape[0] = true;
                        shape[1] = true;
                        shape[4] = true;
                        shape[5] = true;
                    }
                    bulletDestroyed = true;
                    if (shape[4] && shape[5]) {
                        border[0] = null;
                    }
                }
                if (bullet.intersects(b) && !shape[1]) {
                    if (bulletPower == 1) {
                        shape[0] = true;
                        shape[1] = true;
                    }
                    if (bulletPower == 2) {
                        shape[0] = true;
                        shape[1] = true;
                        shape[4] = true;
                        shape[5] = true;
                    }
                    bulletDestroyed = true;
                    if (shape[4] && shape[5]) {
                        border[0] = null;
                    }
                }

            }
            if (border[0] != null) {
                if (bullet.intersects(border[0]) && bulletDirection == 0
						&& !(bullet.intersects(d) && (!shape[2] || !shape[6] || shape[1] && shape[5]))) {
                    if (bullet.intersects(c) && shape[4] && !shape[0]) {
                        shape[0] = true;
                        shape[1] = true;
                        bulletDestroyed = true;
                        border[0] = null;
                    }
                    if (bullet.intersects(d) && shape[5] && !shape[1]) {
                        shape[0] = true;
                        shape[1] = true;
                        bulletDestroyed = true;
                        border[0] = null;
                    }
                    if (bullet.intersects(c) && !shape[4]) {
                        if (bulletPower == 1) {
                            shape[4] = true;
                            shape[5] = true;
                        }
                        if (bulletPower == 2) {
                            shape[4] = true;
                            shape[5] = true;
                            shape[1] = true;
                            shape[0] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[0] && shape[1]) {
                            border[0] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[5]) {
                        if (bulletPower == 1) {
                            shape[4] = true;
                            shape[5] = true;
                        }
                        if (bulletPower == 2) {
                            shape[4] = true;
                            shape[5] = true;
                            shape[1] = true;
                            shape[0] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[0] && shape[1]) {
                            border[0] = null;
                        }
                    }
                }
            }
            if (border[0] != null) {
                if (bullet.intersects(border[0]) && bulletDirection == 3
						&& !(bullet.intersects(c) && (!shape[8] || !shape[9] || shape[4] && shape[5]))) {
                    if (bullet.intersects(a) && shape[0] && !shape[1]) {
                        shape[1] = true;
                        shape[5] = true;
                        bulletDestroyed = true;
                        border[0] = null;
                    }
                    if (bullet.intersects(c) && shape[4] && !shape[5]) {
                        shape[1] = true;
                        shape[5] = true;
                        bulletDestroyed = true;
                        border[0] = null;
                    }
                    if (bullet.intersects(a) && !shape[0]) {
                        if (bulletPower == 1) {
                            shape[0] = true;
                            shape[4] = true;
                        }
                        if (bulletPower == 2) {
                            shape[0] = true;
                            shape[4] = true;
                            shape[1] = true;
                            shape[5] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[1] && shape[5]) {
                            border[0] = null;
                        }
                    }
                    if (bullet.intersects(c) && !shape[4]) {
                        if (bulletPower == 1) {
                            shape[0] = true;
                            shape[4] = true;
                        }
                        if (bulletPower == 2) {
                            shape[0] = true;
                            shape[4] = true;
                            shape[1] = true;
                            shape[5] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[1] && shape[5]) {
                            border[0] = null;
                        }
                    }
                }
            }
            if (border[0] != null) {
                if (bullet.intersects(border[0]) && bulletDirection == 2
						&& !(bullet.intersects(d) && (!shape[8] || !shape[9] || shape[4] && shape[5]))) {
                    if (bullet.intersects(b) && shape[1] && !shape[0]) {
                        shape[0] = true;
                        shape[4] = true;
                        bulletDestroyed = true;
                        border[0] = null;
                    }
                    if (bullet.intersects(d) && shape[5] && !shape[4]) {
                        shape[0] = true;
                        shape[4] = true;
                        bulletDestroyed = true;
                        border[0] = null;
                    }
                    if (bullet.intersects(b) && !shape[1]) {
                        if (bulletPower == 1) {
                            shape[1] = true;
                            shape[5] = true;
                        }
                        if (bulletPower == 2) {
                            shape[1] = true;
                            shape[5] = true;
                            shape[0] = true;
                            shape[4] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[0] && shape[4]) {
                            border[0] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[5]) {
                        if (bulletPower == 1) {
                            shape[1] = true;
                            shape[5] = true;
                        }
                        if (bulletPower == 2) {
                            shape[1] = true;
                            shape[5] = true;
                            shape[0] = true;
                            shape[4] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[0] && shape[4]) {
                            border[0] = null;
                        }
                    }
                }
            }
        }
        //*******************************************************************************
        if (border[1] != null) {
            Rectangle a = new Rectangle(border[1].x, border[1].y, 5, 5);
            Rectangle b = new Rectangle(border[1].x + 7, border[1].y, 5, 5);
            Rectangle c = new Rectangle(border[1].x, border[1].y + 7, 5, 5);
            Rectangle d = new Rectangle(border[1].x + 7, border[1].y + 7, 5, 5);
			if (bullet.intersects(border[1]) && bulletDirection == 1
					&& !(bullet.intersects(a) && (!shape[1] || !shape[5] || shape[2] && shape[6]))) {
				if (bullet.intersects(a) && shape[2] && !shape[6]) {
					shape[6] = true;
					shape[7] = true;
					bulletDestroyed = true;
					border[1] = null;
				}
				if (bullet.intersects(b) && shape[3] && !shape[7]) {
					shape[6] = true;
					shape[7] = true;
					bulletDestroyed = true;
					border[1] = null;
				}
				if (bullet.intersects(a) && !shape[2]) {
					if (bulletPower == 1) {
						shape[2] = true;
						shape[3] = true;
					}
					if (bulletPower == 2) {
						shape[2] = true;
						shape[3] = true;
						shape[6] = true;
						shape[7] = true;
					}
					bulletDestroyed = true;
					if (shape[6] && shape[7]) {
                        border[1] = null;
                    }
				}
				if (bullet.intersects(b) && !shape[3]) {
					if (bulletPower == 1) {
						shape[2] = true;
						shape[3] = true;
					}
					if (bulletPower == 2) {
						shape[2] = true;
						shape[3] = true;
						shape[6] = true;
						shape[7] = true;
					}
					bulletDestroyed = true;
					if (shape[6] && shape[7]) {
                        border[1] = null;
                    }
				}
			}
			if (border[1] != null) {
                if (bullet.intersects(border[1]) && bulletDirection == 0
						&& !(bullet.intersects(c) && (!shape[1] || !shape[5] || shape[2] && shape[6]))) {
                    if (bullet.intersects(c) && shape[6] && !shape[2]) {
                        shape[2] = true;
                        shape[3] = true;
                        bulletDestroyed = true;
                        border[1] = null;
                    }
                    if (bullet.intersects(d) && shape[7] && !shape[3]) {
                        shape[2] = true;
                        shape[3] = true;
                        bulletDestroyed = true;
                        border[1] = null;
                    }
                    if (bullet.intersects(c) && !shape[6]) {
                        if (bulletPower == 1) {
                            shape[6] = true;
                            shape[7] = true;
                        }
                        if (bulletPower == 2) {
                            shape[6] = true;
                            shape[7] = true;
                            shape[2] = true;
                            shape[3] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[2] && shape[3]) {
                            border[1] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[7]) {
                        if (bulletPower == 1) {
                            shape[6] = true;
                            shape[7] = true;
                        }
                        if (bulletPower == 2) {
                            shape[6] = true;
                            shape[7] = true;
                            shape[2] = true;
                            shape[3] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[2] && shape[3]) {
                            border[1] = null;
                        }
                    }
                }
            }
            if (border[1] != null) {
                if (bullet.intersects(border[1]) && bulletDirection == 3
						&& !(bullet.intersects(c) && (!shape[10] || !shape[11] || shape[6] && shape[7]))) {
                    if (bullet.intersects(a) && shape[2] && !shape[3]) {
                        shape[3] = true;
                        shape[7] = true;
                        bulletDestroyed = true;
                        border[1] = null;
                    }
                    if (bullet.intersects(c) && shape[6] && !shape[7]) {
                        shape[3] = true;
                        shape[7] = true;
                        bulletDestroyed = true;
                        border[1] = null;
                    }
                    if (bullet.intersects(a) && !shape[2]) {
                        if (bulletPower == 1) {
                            shape[2] = true;
                            shape[6] = true;
                        }
                        if (bulletPower == 2) {
                            shape[2] = true;
                            shape[6] = true;
                            shape[3] = true;
                            shape[7] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[3] && shape[7]) {
                            border[1] = null;
                        }
                    }
                    if (bullet.intersects(c) && !shape[6]) {
                        if (bulletPower == 1) {
                            shape[2] = true;
                            shape[6] = true;
                        }
                        if (bulletPower == 2) {
                            shape[2] = true;
                            shape[6] = true;
                            shape[3] = true;
                            shape[7] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[3] && shape[7]) {
                            border[1] = null;
                        }
                    }
                }
            }
            if (border[1] != null) {
                if (bullet.intersects(border[1]) && bulletDirection == 2
						&& !(bullet.intersects(d) && (!shape[10] || !shape[11] || shape[6] && shape[7]))) {
                    if (bullet.intersects(b) && shape[3] && !shape[2]) {
                        shape[2] = true;
                        shape[6] = true;
                        bulletDestroyed = true;
                        border[1] = null;
                    }
                    if (bullet.intersects(d) && shape[7] && !shape[6]) {
                        shape[2] = true;
                        shape[6] = true;
                        bulletDestroyed = true;
                        border[1] = null;
                    }
                    if (bullet.intersects(b) && !shape[3]) {
                        if (bulletPower == 1) {
                            shape[3] = true;
                            shape[7] = true;
                        }
                        if (bulletPower == 2) {
                            shape[3] = true;
                            shape[7] = true;
                            shape[2] = true;
                            shape[6] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[2] && shape[6]) {
                            border[1] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[7]) {
                        if (bulletPower == 1) {
                            shape[3] = true;
                            shape[7] = true;
                        }
                        if (bulletPower == 2) {
                            shape[3] = true;
                            shape[7] = true;
                            shape[2] = true;
                            shape[6] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[2] && shape[6]) {
                            border[1] = null;
                        }
                    }
                }
            }
        }

        //***********************************************************************************
        if (border[2] != null) {
            Rectangle a = new Rectangle(border[2].x, border[2].y, 5, 5);
            Rectangle b = new Rectangle(border[2].x + 7, border[2].y, 5, 5);
            Rectangle c = new Rectangle(border[2].x, border[2].y + 7, 5, 5);
            Rectangle d = new Rectangle(border[2].x + 7, border[2].y + 7, 5, 5);
			if (bullet.intersects(border[2]) && bulletDirection == 1
					&& !(bullet.intersects(b) && (!shape[10] || !shape[14] || shape[9] && shape[13]))) {
				if (bullet.intersects(a) && shape[8] && !shape[12]) {
					shape[12] = true;
					shape[13] = true;
					bulletDestroyed = true;
					border[2] = null;
				}
				if (bullet.intersects(b) && shape[9] && !shape[13]) {
					shape[12] = true;
					shape[13] = true;
					bulletDestroyed = true;
					border[2] = null;
				}
				if (bullet.intersects(a) && !shape[8]) {
					if (bulletPower == 1) {
						shape[8] = true;
						shape[9] = true;
					}
					if (bulletPower == 2) {
						shape[8] = true;
						shape[9] = true;
						shape[12] = true;
						shape[13] = true;
					}
					bulletDestroyed = true;
					if (shape[12] && shape[13]) {
                        border[2] = null;
                    }
				}
				if (bullet.intersects(b) && !shape[9]) {
					if (bulletPower == 1) {
						shape[8] = true;
						shape[9] = true;
					}
					if (bulletPower == 2) {
						shape[8] = true;
						shape[9] = true;
						shape[12] = true;
						shape[13] = true;
					}
					bulletDestroyed = true;
					if (shape[12] && shape[13]) {
                        border[2] = null;
                    }
				}
			}
			if (border[2] != null) {
                if (bullet.intersects(border[2]) && bulletDirection == 0
						&& !(bullet.intersects(d) && (!shape[10] || !shape[14] || shape[9] && shape[13]))) {
                    if (bullet.intersects(c) && shape[12] && !shape[8]) {
                        shape[8] = true;
                        shape[9] = true;
                        bulletDestroyed = true;
                        border[2] = null;
                    }
                    if (bullet.intersects(d) && shape[13] && !shape[9]) {
                        shape[8] = true;
                        shape[9] = true;
                        bulletDestroyed = true;
                        border[2] = null;
                    }
                    if (bullet.intersects(c) && !shape[12]) {
                        if (bulletPower == 1) {
                            shape[12] = true;
                            shape[13] = true;
                        }
                        if (bulletPower == 2) {
                            shape[12] = true;
                            shape[13] = true;
                            shape[8] = true;
                            shape[9] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[8] && shape[9]) {
                            border[2] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[13]) {
                        if (bulletPower == 1) {
                            shape[12] = true;
                            shape[13] = true;
                        }
                        if (bulletPower == 2) {
                            shape[12] = true;
                            shape[13] = true;
                            shape[8] = true;
                            shape[9] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[8] && shape[9]) {
                            border[2] = null;
                        }
                    }
                }
            }
            if (border[2] != null) {
                if (bullet.intersects(border[2]) && bulletDirection == 3
						&& !(bullet.intersects(a) && (!shape[4] || !shape[5] || shape[8] && shape[9]))) {
                    if (bullet.intersects(a) && shape[8] && !shape[9]) {
                        shape[9] = true;
                        shape[13] = true;
                        bulletDestroyed = true;
                        border[2] = null;
                    }
                    if (bullet.intersects(c) && shape[12] && !shape[13]) {
                        shape[9] = true;
                        shape[13] = true;
                        bulletDestroyed = true;
                        border[2] = null;
                    }
                    if (bullet.intersects(a) && !shape[8]) {
                        if (bulletPower == 1) {
                            shape[8] = true;
                            shape[12] = true;
                        }
                        if (bulletPower == 2) {
                            shape[8] = true;
                            shape[12] = true;
                            shape[9] = true;
                            shape[13] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[9] && shape[13]) {
                            border[2] = null;
                        }
                    }
                    if (bullet.intersects(c) && !shape[12]) {
                        if (bulletPower == 1) {
                            shape[8] = true;
                            shape[12] = true;
                        }
                        if (bulletPower == 2) {
                            shape[8] = true;
                            shape[12] = true;
                            shape[9] = true;
                            shape[13] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[9] && shape[13]) {
                            border[2] = null;
                        }
                    }
                }
            }
            if (border[2] != null) {
                if (bullet.intersects(border[2]) && bulletDirection == 2
						&& !(bullet.intersects(b) && (!shape[4] || !shape[5] || shape[8] && shape[9]))) {
                    if (bullet.intersects(b) && shape[9] && !shape[8]) {
                        shape[8] = true;
                        shape[12] = true;
                        bulletDestroyed = true;
                        border[2] = null;
                    }
                    if (bullet.intersects(d) && shape[13] && !shape[12]) {
                        shape[8] = true;
                        shape[12] = true;
                        bulletDestroyed = true;
                        border[2] = null;
                    }
                    if (bullet.intersects(b) && !shape[9]) {
                        if (bulletPower == 1) {
                            shape[9] = true;
                            shape[13] = true;
                        }
                        if (bulletPower == 2) {
                            shape[9] = true;
                            shape[13] = true;
                            shape[8] = true;
                            shape[12] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[8] && shape[12]) {
                            border[2] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[13]) {
                        if (bulletPower == 1) {
                            shape[9] = true;
                            shape[13] = true;
                        }
                        if (bulletPower == 2) {
                            shape[9] = true;
                            shape[13] = true;
                            shape[8] = true;
                            shape[12] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[8] && shape[12]) {
                            border[2] = null;
                        }
                    }
                }
            }

        }
        //************************************************************************************
        if (border[3] != null) {
            Rectangle a = new Rectangle(border[3].x, border[3].y, 5, 5);
            Rectangle b = new Rectangle(border[3].x + 7, border[3].y, 5, 5);
            Rectangle c = new Rectangle(border[3].x, border[3].y + 7, 5, 5);
            Rectangle d = new Rectangle(border[3].x + 7, border[3].y + 7, 5, 5);
			if (bullet.intersects(border[3]) && bulletDirection == 1
					&& !(bullet.intersects(a) && (!shape[9] || !shape[13] || shape[10] && shape[14]))) {
				if (bullet.intersects(a) && shape[10] && !shape[14]) {
					shape[14] = true;
					shape[15] = true;
					bulletDestroyed = true;
					border[3] = null;
				}
				if (bullet.intersects(b) && shape[11] && !shape[15]) {
					shape[14] = true;
					shape[15] = true;
					bulletDestroyed = true;
					border[3] = null;
				}
				if (bullet.intersects(a) && !shape[10]) {
					if (bulletPower == 1) {
						shape[10] = true;
						shape[11] = true;
					}
					if (bulletPower == 2) {
						shape[10] = true;
						shape[11] = true;
						shape[14] = true;
						shape[15] = true;
					}
					bulletDestroyed = true;
					if (shape[14] && shape[15]) {
                        border[3] = null;
                    }
				}
				if (bullet.intersects(b) && !shape[11]) {
					if (bulletPower == 1) {
						shape[10] = true;
						shape[11] = true;
					}
					if (bulletPower == 2) {
						shape[10] = true;
						shape[11] = true;
						shape[14] = true;
						shape[15] = true;
					}
					bulletDestroyed = true;
					if (shape[14] && shape[15]) {
                        border[3] = null;
                    }
				}
			}
			if (border[3] != null) {
                if (bullet.intersects(border[3]) && bulletDirection == 0
						&& !(bullet.intersects(c) && (!shape[9] || !shape[13] || shape[10] && shape[14]))) {
                    if (bullet.intersects(c) && shape[14] && !shape[10]) {
                        shape[10] = true;
                        shape[11] = true;
                        bulletDestroyed = true;
                        border[3] = null;
                    }
                    if (bullet.intersects(d) && shape[15] && !shape[11]) {
                        shape[10] = true;
                        shape[11] = true;
                        bulletDestroyed = true;
                        border[3] = null;
                    }
                    if (bullet.intersects(c) && !shape[14]) {
                        if (bulletPower == 1) {
                            shape[14] = true;
                            shape[15] = true;
                        }
                        if (bulletPower == 2) {
                            shape[14] = true;
                            shape[15] = true;
                            shape[10] = true;
                            shape[11] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[10] && shape[11]) {
                            border[3] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[15]) {
                        if (bulletPower == 1) {
                            shape[14] = true;
                            shape[15] = true;
                        }
                        if (bulletPower == 2) {
                            shape[14] = true;
                            shape[15] = true;
                            shape[10] = true;
                            shape[11] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[10] && shape[11]) {
                            border[3] = null;
                        }
                    }
                }
            }
            if (border[3] != null) {
                if (bullet.intersects(border[3]) && bulletDirection == 3
						&& !(bullet.intersects(a) && (!shape[6] || !shape[7] || shape[10] && shape[11]))) {
                    if (bullet.intersects(a) && shape[10] && !shape[11]) {
                        shape[11] = true;
                        shape[15] = true;
                        bulletDestroyed = true;
                        border[3] = null;
                    }
                    if (bullet.intersects(c) && shape[14] && !shape[15]) {
                        shape[11] = true;
                        shape[15] = true;
                        bulletDestroyed = true;
                        border[3] = null;
                    }
                    if (bullet.intersects(a) && !shape[10]) {
                        if (bulletPower == 1) {
                            shape[10] = true;
                            shape[14] = true;
                        }
                        if (bulletPower == 2) {
                            shape[10] = true;
                            shape[14] = true;
                            shape[11] = true;
                            shape[15] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[11] && shape[15]) {
                            border[3] = null;
                        }
                    }
                    if (bullet.intersects(c) && !shape[14]) {
                        if (bulletPower == 1) {
                            shape[10] = true;
                            shape[14] = true;
                        }
                        if (bulletPower == 2) {
                            shape[10] = true;
                            shape[14] = true;
                            shape[11] = true;
                            shape[15] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[11] && shape[15]) {
                            border[3] = null;
                        }
                    }
                }
            }
            if (border[3] != null) {
                if (bullet.intersects(border[3]) && bulletDirection == 2
						&& !(bullet.intersects(b) && (!shape[6] || !shape[7] || shape[10] && shape[11]))) {
                    if (bullet.intersects(b) && shape[11] && !shape[10]) {
                        shape[10] = true;
                        shape[14] = true;
                        bulletDestroyed = true;
                        border[3] = null;
                    }
                    if (bullet.intersects(d) && shape[15] && !shape[14]) {
                        shape[10] = true;
                        shape[14] = true;
                        bulletDestroyed = true;
                        border[3] = null;
                    }
                    if (bullet.intersects(b) && !shape[11]) {
                        if (bulletPower == 1) {
                            shape[11] = true;
                            shape[15] = true;
                        }
                        if (bulletPower == 2) {
                            shape[11] = true;
                            shape[15] = true;
                            shape[10] = true;
                            shape[14] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[10] && shape[14]) {
                            border[3] = null;
                        }
                    }
                    if (bullet.intersects(d) && !shape[15]) {
                        if (bulletPower == 1) {
                            shape[11] = true;
                            shape[15] = true;
                        }
                        if (bulletPower == 2) {
                            shape[11] = true;
                            shape[15] = true;
                            shape[10] = true;
                            shape[14] = true;
                        }
                        bulletDestroyed = true;
                        if (shape[10] && shape[14]) {
                            border[3] = null;
                        }
                    }
                }
            }
        }

        //write changes to the outputs
        Instruction.getFromSever().append("w").append(xPos).append(",").append(yPos).append(",");
        for (boolean b : shape) {
            if (b) {

                Instruction.getFromSever().append("1");
            } else {

                Instruction.getFromSever().append("0");
            }
        }

        Instruction.getFromSever().append(";");
    }
//===========================================================================================

    @Override
    public boolean wallDestroyed() {
        if (wallDestroyed) {
            return true;
        }
        wallDestroyed = true;
        for (boolean b : shape) {
            if (!b) {
                wallDestroyed = false;
                break;
            }
        }
        return wallDestroyed;
    }


    @Override
    public Rectangle getBorder() {
        return generalBorder;
    }

    @Override
    public Rectangle[] getDetailedBorder() {
        return border;
    }


    @Override
    public void draw(Graphics g) {
        if (wallDestroyed) {
            return;
        }
        g.drawImage(wall, xPos - 12, yPos - 12, null);
        g.setColor(new Color(128, 64, 0));
        if (shape[0]) {
            g.fillRect(xPos - 12, yPos - 12, 7, 7);
        }
        if (shape[1]) {
            g.fillRect(xPos - 6, yPos - 12, 7, 7);
        }
        if (shape[2]) {
            g.fillRect(xPos, yPos - 12, 7, 7);
        }
        if (shape[3]) {
            g.fillRect(xPos + 6, yPos - 12, 7, 7);
        }
        if (shape[4]) {
            g.fillRect(xPos - 12, yPos - 6, 7, 7);
        }
        if (shape[5]) {
            g.fillRect(xPos - 6, yPos - 6, 7, 7);
        }
        if (shape[6]) {
            g.fillRect(xPos, yPos - 6, 7, 7);
        }
        if (shape[7]) {
            g.fillRect(xPos + 6, yPos - 6, 7, 7);
        }
        if (shape[8]) {
            g.fillRect(xPos - 12, yPos, 7, 7);
        }
        if (shape[9]) {
            g.fillRect(xPos - 6, yPos, 7, 7);
        }
        if (shape[10]) {
            g.fillRect(xPos, yPos, 7, 7);
        }
        if (shape[11]) {
            g.fillRect(xPos + 6, yPos, 7, 7);
        }
        if (shape[12]) {
            g.fillRect(xPos - 12, yPos + 6, 7, 7);
        }
        if (shape[13]) {
            g.fillRect(xPos - 6, yPos + 6, 7, 7);
        }
        if (shape[14]) {
            g.fillRect(xPos, yPos + 6, 7, 7);
        }
        if (shape[15]) {
            g.fillRect(xPos + 6, yPos + 6, 7, 7);
        }
    }

    @Override
    public String getType() {
        return "wall";
    }


    //未使用的方法
    @Override
    public void move() {
    }
}




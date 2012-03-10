package com.wrcreedrs.games.game.tetris;

import java.util.Random;

public class TetrisBlock {
	Tetris game;
	Shape shape;
	int rotation = 0;
	int x = 5;
	int y = -5;
	
	public static enum Shape {
		SQUARE  (new int[]{2,2, 2,3, 3,2, 3,3}, 9),
		LINE    (new int[]{3,1, 3,2, 3,3, 3,4}, 14);
		
		int[] shape;
		public int color;
		
		private Shape(int[] shape, int color) {
			this.shape = shape;
			this.color = color;
		}
		
		public static Shape random() {
			Random r = new Random();
			int totalShapes = Shape.values().length;
			int rand = r.nextInt(totalShapes);
			
			return Shape.values()[rand];
		}
		
		int[] rotatedShape(int dir) {
			int[] result = new int[shape.length];
			for (int i=0; i<shape.length; i++) {
				if (dir==0) result[i] = shape[i];
				if (dir==1) result[i] = i%2==0?((shape[i+1] - 3) + 3):((shape[i-1] - 3) + 3);
				if (dir==2) result[i] = 6 - shape[i+1];
				if (dir==3) result[i] = i%2==0?(6 - (shape[i+1] - 3) + 3):(6 - (shape[i-1] - 3) + 3);
			}
			return result;
		}
	}
	
	public TetrisBlock(Tetris game, Shape shape) {
		this.game = game;
		this.shape = shape;
	}
	public boolean canRotateClockwise() {
		int newrot = (rotation+1)%4;
		int[] shapes = shape.rotatedShape(newrot);
		
		for (int i=0; i<shapes.length; i+=2) {
			if (shapes[i+1]+y >= 18) return false;
			if (shapes[i+0]+x < 0) return false;
			if (shapes[i+0]+x > 13) return false;
			try {
				if (game.solidblocks[shapes[i] + x][shapes[i+1] + y] > 0) return false;
			} catch (ArrayIndexOutOfBoundsException e) {}
		}
		
		return true;
	}
	public void rotateClockwise() {
		rotation++;
		if (rotation > 3) rotation = 0;
	}
	
	public int[][] getShape() {
		int[] rotated = shape.rotatedShape(rotation);
		int[][] result = new int[rotated.length/2][2];
		for (int i=0; i<rotated.length; i++) {
			result[i/2][i%2] = rotated[i];
		}
		return result;
	}
	
	public boolean canMoveDown() {
		int[][] shape = getShape();
		int y = this.y+1;

		for (int i=0; i<shape.length; i++) {
			if (shape[i][1]+y >= 18) return false;
			try {
				if (game.solidblocks[shape[i][0] + x][shape[i][1] + y] > 0) return false;
			} catch (ArrayIndexOutOfBoundsException e) {}
		}
		return true;
	}
	
	public void render() {
		int color = shape.color;
		int[][] shape = getShape();
		for (int xx=0; xx<shape.length; xx++) {
			try {
				game.screen[x+shape[xx][0]][y+shape[xx][1]] = color;
			} catch (ArrayIndexOutOfBoundsException e) {}
		}
	}
	
	public void moveDown() {
		y++;
	}

	public void solidize() {
		int color = shape.color;
		int[][] shape = getShape();
		for (int xx=0; xx<shape.length; xx++) {
			try {
				game.solidblocks[shape[xx][0] + x][shape[xx][1] + y] = color;
			} catch (ArrayIndexOutOfBoundsException e) { game.gameOver(); }
		}
	}

	public void tryRotate() {
		if (canRotateClockwise()) rotateClockwise();
	}
	public void tryMoveRight() {
		if (canMoveRight()) moveRight();
	}
	public void tryMoveLeft() {
		if (canMoveLeft()) moveLeft();
	}
	public void moveRight() {
		x++;
	}
	public void moveLeft() {
		x--;
	}
	public boolean canMoveRight() {
		int[][] shape = getShape();
		int x = this.x+1;

		for (int i=0; i<shape.length; i++) {
			if (shape[i][1]+y >= 18) return false;
			try {
				if (game.solidblocks[shape[i][0] + x][shape[i][1] + y] > 0) return false;
			} catch (ArrayIndexOutOfBoundsException e) {}
		}
		return true;
	}
	public boolean canMoveLeft() {
		int[][] shape = getShape();
		int x = this.x-1;

		for (int i=0; i<shape.length; i++) {
			if (shape[i][1]+y >= 18) return false;
			try {
				if (game.solidblocks[shape[i][0] + x][shape[i][1] + y] > 0) return false;
			} catch (ArrayIndexOutOfBoundsException e) {}
		}
		return true;
	}
}

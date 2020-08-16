package com.mohammad.asad.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;


public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] manTexture;
	Texture deadManTexture;
	int manState=0,pause=0;
	float gravity=0.2f,velocity=0f;
	int manY=0;

	Random random;
	int gameState=0;		//0=pause,1==start,2==dead

	ArrayList<Integer> coinsX=new ArrayList<>();
	ArrayList<Integer> coinsY=new ArrayList<>();
	Texture coinTexture;
	int coinCount;

	ArrayList<Integer> bombX=new ArrayList<>();
	ArrayList<Integer> bombY=new ArrayList<>();
	Texture bombTexture;
	int bombCount;

	Rectangle manRectangle;
	ArrayList<Rectangle> coinRectangles=new ArrayList<>();
	ArrayList<Rectangle> bombRectangles=new ArrayList<>();

	int score=0;
	BitmapFont scoreBitmap;
	BitmapFont startScreen;
	BitmapFont deadScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		manTexture=new Texture[4];
		manTexture[0]=new Texture("frame-1.png");
		manTexture[1]=new Texture("frame-2.png");
		manTexture[2]=new Texture("frame-3.png");
		manTexture[3]=new Texture("frame-4.png");

		manY=Gdx.graphics.getHeight()/2;

		coinTexture=new Texture("coin.png");
		random=new Random();

		bombTexture=new Texture("bomb.png");
		scoreBitmap=new BitmapFont();
		scoreBitmap.setColor(Color.WHITE);
		scoreBitmap.getData().setScale(10);

		deadManTexture=new Texture("dizzy-1.png");

		startScreen=new BitmapFont();
		startScreen.setColor(Color.BLACK);
		startScreen.getData().setScale(20);

		deadScreen=new BitmapFont();
		deadScreen.setColor(Color.BLACK);
		deadScreen.getData().setScale(15);

	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState==1) {
			if (pause < 8) {
				pause++;
			} else {
				pause = 0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity += gravity;
			manY -= velocity;
			if (manY <= 0) {
				manY = 0;
			}

			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			if (coinCount < 100) {
				coinCount++;
			} else {
				coinCount = 0;
				makeCoin();
			}

			coinRectangles.clear();
			for (int i = 0; i < coinsX.size(); i++) {
				batch.draw(coinTexture, coinsX.get(i), coinsY.get(i));
				coinsX.set(i, coinsX.get(i) - 4);
				coinRectangles.add(new Rectangle(coinsX.get(i), coinsY.get(i), coinTexture.getWidth(), coinTexture.getHeight()));
			}

			if (bombCount < 250) {
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb();
			}

			bombRectangles.clear();
			for (int i = 0; i < bombX.size(); i++) {
				batch.draw(bombTexture, bombX.get(i), bombY.get(i));
				bombX.set(i, bombX.get(i) - 8);
				bombRectangles.add(new Rectangle(bombX.get(i), bombY.get(i), bombTexture.getWidth(), bombTexture.getHeight()));

			}
			batch.draw(manTexture[manState],Gdx.graphics.getWidth()/2-manTexture[manState].getWidth()/2,manY);
			manRectangle=new Rectangle(Gdx.graphics.getWidth()/2 -manTexture[manState].getWidth()/2,manY,manTexture[manState].getWidth(),manTexture[manState].getHeight());

			for (int i=0;i<coinRectangles.size();i++) {
				if (Intersector.overlaps(manRectangle,coinRectangles.get(i))) {
					score+=2;
					coinRectangles.remove(i);
					coinsX.remove(i);
					coinsY.remove(i);
				}
			}
			for (int i=0;i<bombRectangles.size();i++) {
				if (Intersector.overlaps(manRectangle,bombRectangles.get(i))) {
					score+=2;
					bombRectangles.remove(i);
					bombX.remove(i);
					bombY.remove(i);
					gameState=2;
				}
			}

		} else if (gameState==2) {
			batch.draw(deadManTexture,Gdx.graphics.getWidth()/2-deadManTexture.getWidth()/2,manY);
			score=0;
			coinsY.clear();
			coinsX.clear();
			bombY.clear();
			bombX.clear();
			coinRectangles.clear();
			bombRectangles.clear();

			if (Gdx.input.justTouched()) {
				gameState=1;
			}

		} else if (gameState==0) {
			if (Gdx.input.justTouched()) {
				gameState=1;
			}
		}

		scoreBitmap.draw(batch,String.valueOf(score),100,200);
		if (gameState==0) {
			startScreen.draw(batch, "Begin.", Gdx.graphics.getWidth() / 2 -370, Gdx.graphics.getHeight() / 2 -100);
		} else if (gameState==2) {
			deadScreen.draw(batch, "Play Again.", Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2 - 100);
		}

		batch.end();
	}

	public void makeCoin() {
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinsY.add((int)height);
		coinsX.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombY.add((int) height);
		bombX.add(Gdx.graphics.getWidth());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}


}

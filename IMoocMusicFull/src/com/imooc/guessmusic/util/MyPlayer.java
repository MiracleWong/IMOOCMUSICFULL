package com.imooc.guessmusic.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

/**
 * ���ֲ�����
 * @author MiracleWong
 *
 */
// ���ó�Ϊһ��������ģʽ����������ô���ã����ص�ʼ����һ�����
public class MyPlayer {
	// ����
	public final static int INDEX_STONE_ENTER = 0;
	public final static int INDEX_STONE_CANCEL = 1;
	public final static int INDEX_STONE_COIN = 2;
	
	// ��Ч���ļ�����
	private final static String[] SONG_NAMES = 
		{"enter.mp3","cancel.mp3","coin.mp3"};
	
	// ��Ч
	private static MediaPlayer[] mToneMediaPlayer = new MediaPlayer[SONG_NAMES.length];
	/**
	 * ������Ч����ť��������������뷴������
	 * @param context
	 * @param index
	 */
	public static void playTone(Context context, int index) {
		// ���������ļ�
		AssetManager assetManager = context.getAssets();
		if (mToneMediaPlayer[index]==null) {
			mToneMediaPlayer[index] = new MediaPlayer();
			try {
				// ��������Դ
				AssetFileDescriptor fileDescriptor = 
						assetManager.openFd(SONG_NAMES[index]);
				mToneMediaPlayer[index].setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
				// ʹ���ֲ��������뵽���Բ��ŵ�״̬
				mToneMediaPlayer[index].prepare();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		// ��ʼ����
		mToneMediaPlayer[index].start();		
	}
	
	
	// ��������
	private static MediaPlayer mMusicMediaPlayer;
	/**
	 * ���Ÿ���
	 * @param context
	 * @param fileName
	 */
	public static void playSong(Context context, String fileName) {
		// �ö���ʼ��ֻ��������һ��
		if (mMusicMediaPlayer == null) {
			mMusicMediaPlayer = new MediaPlayer();
		}
		
		// ǿ�����ã���Էǵ�һ�β��ŵ�ʱ��
		mMusicMediaPlayer.reset();
		
		// ���������ļ�
		AssetManager assetManager = context.getAssets();
		try {
			// ��������Դ
			AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
			mMusicMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
			
			mMusicMediaPlayer.prepare();
			// ��������
			mMusicMediaPlayer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void stopTheSong(Context context) {
		if (mMusicMediaPlayer != null) {
			mMusicMediaPlayer.stop();
		}
	}
}

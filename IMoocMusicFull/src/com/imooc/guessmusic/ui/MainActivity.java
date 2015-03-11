package com.imooc.guessmusic.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import com.imooc.guessmusic.R;
import com.imooc.guessmusic.data.Const;
import com.imooc.guessmusic.model.IWordButtonClickListener;
import com.imooc.guessmusic.model.Song;
import com.imooc.guessmusic.model.WordButton;
import com.imooc.guessmusic.myui.MyGridView;
import com.imooc.guessmusic.util.MyLog;
import com.imooc.guessmusic.util.Util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity implements IWordButtonClickListener {
	
	// ���ó���
	private static final String TAG = "MainActivity";
	
	// ��Ƭ��صĶ���
	private Animation mPanAnim;
	private LinearInterpolator mPanLin;			//���Ե��ٶ�

	// ���˽���Ķ���
	private Animation mBarInAnim;
	private LinearInterpolator mBarInLin;		//���Ե��ٶ�

	// ���˻����Ķ���
	private Animation mBarOutAnim;
	private LinearInterpolator mBarOutLin;		//���Ե��ٶ�
	
	//
	private ImageView mViewPan;
	private ImageView mViewPanBar;
	
	// Play �����¼�
	private ImageButton mBtnPlayStart;
	
	private boolean mIsRunning = false;
	
	// ���ֿ������
	private ArrayList<WordButton> mAllWords;
	
	// ��������
	private ArrayList<WordButton> mBtnSelectWords;
	
	// ����һ���Զ���ؼ��Ķ���
	private MyGridView mMyGridView;
	
	// ��ѡ������ֿ��UI����
	private LinearLayout mViewWordsContainer;
	
	// ��ǰ�ĸ���
	private Song mCurrentSong;
	
	// ��ǰ�ص�����
	private int mCurrentStageIndex = 2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mViewPan = (ImageView)findViewById(R.id.imageView1);
		mViewPanBar = (ImageView)findViewById(R.id.imageView2);
		
		// ��ʼ���Զ���ؼ�
		mMyGridView = (MyGridView)findViewById(R.id.gridview);
		
		mViewWordsContainer = (LinearLayout)findViewById(R.id.word_select_container);
		
		// 
		mMyGridView.registOnWordButtonClick(this);
		
		// ��ʼ������
		mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
		mPanLin = new LinearInterpolator();
		mPanAnim.setInterpolator(mPanLin);	
		mPanAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				mViewPanBar.startAnimation(mBarOutAnim);
			}
		});

		mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
		mBarInLin = new LinearInterpolator();
		mBarInAnim.setFillAfter(true); 			//��������ֹͣ��״̬
		mBarInAnim.setInterpolator(mBarInLin);		
		mBarInAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				mViewPan.startAnimation(mPanAnim);	
			}
		});
		
		mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
		mBarOutLin = new LinearInterpolator();
		mBarOutAnim.setFillAfter(true); 			//��������ֹͣ��״̬
		mBarOutAnim.setInterpolator(mBarOutLin);	

		mBarOutAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				mIsRunning = false;
				mBtnPlayStart.setVisibility(View.VISIBLE);
			}
		});
		
		mBtnPlayStart = (ImageButton)findViewById(R.id.btn_play_start);
		mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(MainActivity.this, "HELLO WORLD", Toast.LENGTH_SHORT).show();
				handlePlayButton();
			}
		});
		
		// ��ʼ����Ϸ����
		initCurrentStageData();
		
	}
	
	@Override
	public void onWordButtonClick(WordButton wordButton) {
		// TODO Auto-generated method stub
//		Toast.makeText(MainActivity.this, wordButton.mIndex, Toast.LENGTH_SHORT).show();
		setSelectWord(wordButton);
	}
	
	//����𰸵ķ���
	private void clearTheAnswer(WordButton wordButton) {
		// ������ѡ������ֱ�ò��ɼ�
		wordButton.mViewButton.setText("");
		wordButton.mWordString = "";
		wordButton.mIsVisiable = false;
		
		// ���ô�ѡ��
		setButtonVisiable(mAllWords.get(wordButton.mIndex), View.VISIBLE);
	}
	/**
	 * ���ô�
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton) {
		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			if (mBtnSelectWords.get(i).mWordString.length() == 0) {
				// ���ô����ֿ�����ݺͿɼ���
				mBtnSelectWords.get(i).mViewButton.setText(wordButton.mWordString);
				mBtnSelectWords.get(i).mIsVisiable = true;
				mBtnSelectWords.get(i).mWordString = wordButton.mWordString;
				// ��¼����
				mBtnSelectWords.get(i).mIndex = wordButton.mIndex;
				
				// Log ......
//				MyLog.d(TAG, mBtnSelectWords.get(i).mIndex+"");
				// ���ô�ѡ��Ŀɼ���
				setButtonVisiable(wordButton, View.INVISIBLE);
				break;
			}
		}
	}
	
	/**
	 * �������ֿ��Ƿ�ɼ�
	 * @param button
	 * @param visibility
	 */
	private void setButtonVisiable(WordButton button, int visibility) {
		button.mViewButton.setVisibility(visibility);
		button.mIsVisiable = (visibility == View.VISIBLE)? true : false;
		
		// Log
		MyLog.d(TAG, button.mIsVisiable+"");
	}
    /**
     * ����Բ���м�Ĳ��Ű�ť�����ǿ�ʼ��������
     */
	private void handlePlayButton() {
		if (mViewPanBar != null){
			if(!mIsRunning){
				mIsRunning = true;
				// ��ʼ���˽��붯��
				mViewPanBar.startAnimation(mBarInAnim);	
				mBtnPlayStart.setVisibility(View.INVISIBLE);
			}			
		}						
	}

	/**
	 * ����������ȡ��ǰ�صĸ�����Ϣ�����ز�ѯ���ĸ���
	 * @param stageIndex
	 * @return
	 */
	private Song loadStageSongInfo(int stageIndex) {
		Song song = new Song();
		
		String[] stage = Const.SONG_INFO[stageIndex];
		song.setSongFileName(stage[Const.INDEX_FILE_NAME]);		//��ø����ļ�������
		song.setSongName(stage[Const.INDEX_SONG_NAME]);				//��ø�����
		return song;
	}
	protected void initCurrentStageData() {
		// ��ȡ��ǰ�صĸ�����Ϣ
		mCurrentSong = loadStageSongInfo(++mCurrentStageIndex);
		// ��ʼ����ѡ���
		mBtnSelectWords = initWordSelect();
		
		LayoutParams params =  new LayoutParams(40, 40);
		
		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			mViewWordsContainer.addView(mBtnSelectWords.get(i).mViewButton, params);
		}
		// �������
		mAllWords = initAllWord();
		// �������� ���� MyGridView
		mMyGridView.updateData(mAllWords);	
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mViewPan.clearAnimation();
		super.onPause();
	}
	
	/**
	 * ��ʼ����ѡ������ֿ�
	 * @return
	 */
	private ArrayList<WordButton> initAllWord() {
		// ��ʼ��
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		// ������еĴ�ѡ����
		String[] words = generateWords();
		// ������еĴ�ѡ���֣��������һ����Ӧ�ķ���
		// Ӳ�����24
		for (int i = 0; i < MyGridView.COUNTS_WORDS; i++) {
			WordButton button = new WordButton();
			button.mWordString = words[i];
			data.add(button);
		}
		return data;
	}

	/**
	 * ��ʼ����ѡ������ֿ�
	 * @return
	 */
	private ArrayList<WordButton> initWordSelect() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		
		for(int i = 0; i < mCurrentSong.getNameLength(); i++) {
			View view = Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);
			
			final WordButton holder = new WordButton();
			holder.mViewButton = (Button)view.findViewById(R.id.item_btn);
			holder.mViewButton.setTextColor(Color.WHITE);
			holder.mViewButton.setText("");
			holder.mIsVisiable = false;
			holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
			holder.mViewButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					clearTheAnswer(holder);
					
				}
			});
			
			data.add(holder);
		}
		return data;
	}

	
	/**
	 * �������еĴ�ѡ���֡����������������������������
	 * ���ص���һ���ַ������������24����
	 * @return
	 */
	private String[] generateWords(){
		Random random = new Random();
		
		String[] words = new String[MyGridView.COUNTS_WORDS];
		
		//�������
		for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
			//����յ��ַ���������Զ����ַ�ת��
			//getNameCharacters���ص���һ�����飬��Ҫ�±�������һ��һ���ķ��غͼ���
			words[i] = mCurrentSong.getNameCharacters()[i]+"";
		}
		
		//����������ֲ���������
		for (int i = mCurrentSong.getNameLength(); i < MyGridView.COUNTS_WORDS; i++) {
			// char����ת��Ϊ�ַ���
			words[i] = getRandomChar()+"";			
		}
		// �������ֵ�˳�����ȴ�����Ԫ�������ѡȡһ�����һ��Ԫ�ؽ��н���
		// Ȼ���ڵڶ���֮��ѡ��һ��Ԫ����ڶ���������ֱ�����һ��Ԫ�ء�
		for (int i = MyGridView.COUNTS_WORDS - 1; i >= 0; i--) {
			int index = random.nextInt(i + 1);		//ȷ�����ֵ�������0~24�м�
			
			String buf = words[index];
			words[index] = words[i];
			words[i] = buf;
		}
		return words;
	}
	
	/**
	 * ������ɺ���
	 * @return
	 */
	private char getRandomChar() {
		String str = "";
		int hightPos;		//��λ
		int lowPos;			//��λ
		
		Random random = new Random();			//�������
		
		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));
		
		//һ�������������ֽ����������
		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();
		
		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str.charAt(0);
	}
}

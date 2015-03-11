package com.imooc.guessmusic.myui;

import java.util.ArrayList;

import com.imooc.guessmusic.R;
import com.imooc.guessmusic.model.IWordButtonClickListener;
import com.imooc.guessmusic.model.WordButton;
import com.imooc.guessmusic.util.Util;




import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MyGridView extends GridView{
	// ����һ���������������ɵ�����
	public final static int COUNTS_WORDS = 16;
	// ����
	private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();
	private MyGridAdapter mAdapter;
	private Context mContext;
	
	// ����һ���������������ִ�ѡ��
	private Animation mScaleAnimmation;
	// ����һ��WordButton�Ľӿڵļ������Ķ���
	private IWordButtonClickListener mWordButtonListener;

	public MyGridView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		// TODO Auto-generated constructor stub
		// context�ĳ�ʼ��
		mContext = context;
		// ��ʼ��
		mAdapter = new MyGridAdapter();
		// ����
		this.setAdapter(mAdapter);
	}
	
	//�������
	public void updateData(ArrayList<WordButton> list) {
		mArrayList = list;		//mArrayList�����������������������β�list
		
		// ˢ�£�������������Դ
		setAdapter(mAdapter);
	}
	
	class MyGridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArrayList.size();
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return mArrayList.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return pos;
		}

		@Override
		public View getView(int pos, View v, ViewGroup p) {
			// TODO Auto-generated method stub
			final WordButton holder;
			// �˴���v�����ж�
			if(v==null){
				v = Util.getView(mContext, R.layout.self_ui_gridview_item);
				
				holder = mArrayList.get(pos);
				
				// ���ض���
				mScaleAnimmation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
				mScaleAnimmation.setStartOffset(pos * 100);
				
				holder.mIndex = pos;
				holder.mViewButton = (Button)v.findViewById(R.id.item_btn);
				
				holder.mViewButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mWordButtonListener.onWordButtonClick(holder);
					}
				});
				
				v.setTag(holder);
			} else {
				holder = (WordButton)v.getTag();
			}
			
			holder.mViewButton.setText(holder.mWordString);
			
			// ��������
			v.startAnimation(mScaleAnimmation);
			return v;
		}
		
	}
	/**
	 * ע������ӿ�
	 * ����()����ָ����˭ע�������ӿ�
	 * @param listener
	 */
	//������ʵ�ֵķ���
	public void registOnWordButtonClick(IWordButtonClickListener listener) {
		mWordButtonListener = listener;
	}
	
}

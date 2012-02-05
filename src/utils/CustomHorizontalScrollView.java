/**
 * 
 */
package utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

/**
 * @author naveed
 * 
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {
	Context context;

	/**
	 * @param context
	 */
	public CustomHorizontalScrollView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		Toast.makeText(context, "l= " + l + "t=" + t + " oldl=" + oldl + " oldt=" + oldt, Toast.LENGTH_SHORT);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		this.context = context;
	}

}


package com.nav.gamepack.shared;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Scroller;

public class InfiniteGallery extends AdapterView<Adapter> implements
		OnGestureListener {

	/**
	 * How long the transition animation should run when a child view changes
	 * position, measured in milliseconds.
	 */
	private int mAnimationDuration = 400;
	private FlingRunnable mFlingRunnable = new FlingRunnable();
	private GestureDetector mGestureDetector = null;

	private Adapter mAdapter;
	private int mGravity;

	private int mLeftMost;
	private int mRightMost;

	/** Temporary frame to hold a child View's frame rectangle */
	private Rect mTouchFrame;

	// it will contain the invisible cache view
	private RecycleBin mRecycler = new RecycleBin();

	// private boolean mIsFirstScroll;

	private View mSelectedChild;

	// to indicate the slider direction
	private short flingLeft = 0;

	/**
	 * Whether to callback when an item that is not selected is clicked.
	 */
	private boolean mShouldCallbackOnUnselectedItemClick = true;

	/**
	 * The view of the item that received the user's down touch.
	 */
	private View mDownTouchView;

	private boolean mShouldStopFling = false;
	private AdapterDataSetObserver mDataSetObserver;
	private int mOldItemCount = 0;
	private int mItemCount = 0;
	private int mFirstPosition = 0;
	private int mSelectedPosition = AdapterView.INVALID_POSITION;

	private int mWidthMeasureSpec;
	private int mHeightMeasureSpec;

	private int mSpacing = 20;
	
	public int getMSpacing() {
		return mSpacing;
	}

	public void setMSpacing(int spacing) {
		mSpacing = spacing;
	}

	private AdapterContextMenuInfo mContextMenuInfo;

	public InfiniteGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InfiniteGallery(Context context, AttributeSet attrs) {
		super(context, attrs);

		initGestureDetector(context);
	}

	public InfiniteGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initGestureDetector(context);
	}

	private void initGestureDetector(Context context) {
		mGestureDetector = new GestureDetector(context, this);
		mGestureDetector.setIsLongpressEnabled(true);
	}

	/**
	 * Whether or not to callback when an item that is not selected is clicked.
	 * If false, the item will become selected (and re-centered). If true, the
	 * {@link #getOnItemClickListener()} will get the callback.
	 * 
	 * @param shouldCallback
	 *            Whether or not to callback on the listener when a item that is
	 *            not selected is clicked.
	 * @hide
	 */
	public void setCallbackOnUnselectedItemClick(boolean shouldCallback) {
		mShouldCallbackOnUnselectedItemClick = shouldCallback;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// Kill any existing fling/scroll
		mFlingRunnable.stop(false);
		flingLeft = 0;
		// Get the item's view that was touched
		mDownTouchPosition = pointToPosition((int) e.getX(), (int) e.getY());

		if (mDownTouchPosition >= 0) {
			mDownTouchView = getChildAt(mDownTouchPosition - mFirstPosition);
			mDownTouchView.setPressed(true);
		}

		// Reset the multiple-scroll tracking state
		mIsFirstScroll = true;

		// Must return true to get matching events for this down event.
		return true;
	}

	/**
	 * Called when a touch event's action is MotionEvent.ACTION_UP.
	 */
	void onUp() {

		if (mFlingRunnable.mScroller.isFinished()) {
			scrollIntoSlots();
		}

		// dispatchUnpress();
	}

	/**
	 * Called when a touch event's action is MotionEvent.ACTION_CANCEL.
	 */
	void onCancel() {
		onUp();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

		if (!mShouldCallbackDuringFling) {
			// We want to suppress selection changes
			// Remove any future code to set mSuppressSelectionChanged = false
			removeCallbacks(mDisableSuppressSelectionChangedRunnable);
			
			// This will get reset once we scroll into slots
			if (!mSuppressSelectionChanged) {
				mSuppressSelectionChanged = true;
			}
		}

		// Fling the gallery!
		if(velocityX < 0) {
			flingLeft = 1;
		} else if(velocityX == 0) {
			flingLeft = 0;
		} else {
			flingLeft = -1;
		}
		
		this.moveByVelocity((int) velocityX);

		return true;
	}
	
	protected void moveByVelocity(float velocity) {
		mFlingRunnable.startUsingVelocity((int) -velocity);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		if(mDownTouchPosition < 0) {
			return;
		}
		
		this.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
		long id = this.getItemIdAtPosition(mDownTouchPosition);
		dispatchLongPress(mDownTouchView, mDownTouchPosition, id);
	}
	
	private boolean dispatchLongPress(View view, int position, long id) {
		boolean handled = false;
		
		if(this.getOnItemLongClickListener() != null) {
			handled = this.getOnItemLongClickListener().onItemLongClick(this, mDownTouchView, mDownTouchPosition, id);
		}
		
		if(!handled) {
			mContextMenuInfo = new AdapterContextMenuInfo(view, position, id);
			handled = super.showContextMenuForChild(this);
		}
		
		if(handled) {
			performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
		}
		
		return handled;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		this.getParent().requestDisallowInterceptTouchEvent(true);
		
        // As the user scrolls, we want to callback selection changes so related-
        // info on the screen is up-to-date with the gallery's selection
        if (mShouldCallbackDuringFling) {
            if (mIsFirstScroll) {
                /*
                 * We're not notifying the client of selection changes during
                 * the fling, and this scroll could possibly be a fling. Don't
                 * do selection changes until we're sure it is not a fling.
                 */
                if (!mSuppressSelectionChanged) mSuppressSelectionChanged = true;
                postDelayed(mDisableSuppressSelectionChangedRunnable, SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT);
            }
        } else {
            if (mSuppressSelectionChanged) {
            	mSuppressSelectionChanged = false;
            }
        }

		trackMotionScroll(-1 * (int) distanceX);
		mIsFirstScroll = false;
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getSelectedView() {
		if (mItemCount > 0 && mSelectedPosition >= 0) {
			return getChildAt(mSelectedPosition - mFirstPosition);
		} else {
			return null;
		}
	}

	/**
	 * Maps a point to a position in the list.
	 * 
	 * @param x
	 *            X in local coordinate
	 * @param y
	 *            Y in local coordinate
	 * @return The position of the item which contains the specified point, or
	 *         {@link #INVALID_POSITION} if the point does not intersect an
	 *         item.
	 */
	public int pointToPosition(int x, int y) {
		Rect frame = mTouchFrame;
		if (frame == null) {
			mTouchFrame = new Rect();
			frame = mTouchFrame;
		}

		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				child.getHitRect(frame);
				if (frame.contains(x, y)) {
					return mFirstPosition + i;
				}
			}
		}
		return INVALID_POSITION;
	}

	private void scrollIntoSlots() {

		if (getChildCount() == 0 || mSelectedChild == null) {
			Log.v("Customized", "scrollIntoSlots: no selected Child bug");
			return;
		}

		int selectedCenter = getCenterOfView(mSelectedChild);
		int targetCenter = getCenterOfGallery();

		int scrollAmount = targetCenter - selectedCenter;

		Log.v("Customized",
				String.valueOf(selectedCenter) + "..."
						+ String.valueOf(targetCenter) + "..."
						+ String.valueOf(scrollAmount));

		if (scrollAmount != 0) {
			mFlingRunnable.startUsingDistance(scrollAmount);
		} else {
			onFinishedMovement();
		}
	}

	void setSelectedPositionInt(int position) {
		mSelectedPosition = position;

		updateSelectedItemMetadata();
	}

	private void updateSelectedItemMetadata() {

		View oldSelectedChild = mSelectedChild;
		Log.v("Customized",
				"updateSelectedItemMetadata :"
						+ String.valueOf(mSelectedPosition) + "-"
						+ String.valueOf(mFirstPosition));
		
		
		View child = mSelectedChild = getChildAt(mSelectedPosition
				- mFirstPosition);
		
		if (child == null) {
			return;
		}

		child.setSelected(true);
		child.setFocusable(true);

		if (hasFocus()) {
			child.requestFocus();
		}

		// We unfocus the old child down here so the above hasFocus check
		// returns true
		if (oldSelectedChild != null) {

			// Make sure its drawable state doesn't contain 'selected'
			oldSelectedChild.setSelected(false);

			// Make sure it is not focusable anymore, since otherwise arrow keys
			// can make this one be focused
			oldSelectedChild.setFocusable(false);
		}
		
		//when the loop from end to start, the selectedPosition will be length, but the valid value is 0 to length - 1;
		if(mSelectedPosition == mItemCount) {
			mSelectedPosition = 0;
		}
	}

	/**
	 * Looks for the child that is closest to the center and sets it as the
	 * selected child.
	 */
	private void setSelectionToCenterChild(boolean toLeft) {

		View selView = mSelectedChild;
		if (mSelectedChild == null) {
			Log.v("Customized",
					"setSelectionToCenterChild: no selected Child bug");
			return;
		}

		int galleryCenter = getCenterOfGallery();
		int smallUnit = galleryCenter / 3;

		if (selView != null) {

			// Common case where the current selected position is correct
			if (selView.getLeft() <= smallUnit
					&& selView.getRight() >= galleryCenter + smallUnit * 2) {
				return;
			}
		}

		// TODO better search
		// int closestEdgeDistance = Integer.MAX_VALUE;
		int newSelectedChildIndex = 0;

		if (flingLeft == 1 || (flingLeft == 0 && toLeft)) {
			for (int i = getChildCount() - 1; i >= 0; i--) {
				View child = getChildAt(i);

				if (child.getLeft() <= galleryCenter + smallUnit * 2) {
					// This child is in the center
					newSelectedChildIndex = i;
					break;
				}
			}
		} else {
			for (int i = 0; i > getChildCount(); i++) {
				View child = getChildAt(i);

				if (child.getRight() >= smallUnit) {
					// This child is in the center
					newSelectedChildIndex = i;
					break;
				}
			}
		}

		// for (int i = getChildCount() - 1; i >= 0; i--) {
		// View child = getChildAt(i);
		//
		// if (child.getLeft() <= smallUnit && child.getRight() >= galleryCenter
		// + smallUnit * 2) {
		// // This child is in the center
		// newSelectedChildIndex = i;
		// break;
		// }
		//
		// int childClosestEdgeDistance = Math.min(Math.abs(child.getLeft() -
		// smallUnit),
		// Math.abs(child.getRight() - galleryCenter - smallUnit * 2));
		// if (childClosestEdgeDistance < closestEdgeDistance) {
		// closestEdgeDistance = childClosestEdgeDistance;
		// newSelectedChildIndex = i;
		// }
		// }

		int newPos = mFirstPosition + newSelectedChildIndex;

		if (newPos != mSelectedPosition) {
			setSelectedPositionInt(newPos);
			// setNextSelectedPositionInt(newPos);
			// checkSelectionChanged();
		}
	}

	/**
	 * The position of the item that received the user's down touch.
	 */
	private int mDownTouchPosition;

	private boolean scrollToChild(int childPosition) {
		View child = getChildAt(childPosition);

		if (child != null) {
			int distance = getCenterOfGallery() - getCenterOfView(child);
			mFlingRunnable.startUsingDistance(distance);
			return true;
		}

		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		if (mDownTouchPosition >= 0) {

			// An item tap should make it selected, so scroll to this child.
			scrollToChild(mDownTouchPosition - mFirstPosition);

			// Also pass the click so the client knows, if it wants to.
			if (mShouldCallbackOnUnselectedItemClick
					|| mDownTouchPosition == mSelectedPosition) {
				performItemClick(mDownTouchView, mDownTouchPosition,
						mAdapter.getItemId(mDownTouchPosition));
			}

			return true;
		}

		return false;
	}

	void trackMotionScroll(int deltaX) {

		if (getChildCount() == 0) {
			return;
		}

		boolean toLeft = deltaX < 0;

		int limitedDeltaX = getLimitedMotionScrollAmount(toLeft, deltaX);
		if (limitedDeltaX != deltaX) {
			// The above call returned a limited amount, so stop any
			// scrolls/flings
			mFlingRunnable.endFling(false);
			onFinishedMovement();
		}

		offsetChildrenLeftAndRight(limitedDeltaX);

		detachOffScreenChildren(toLeft);

		if (toLeft) {
			// If moved left, there will be empty space on the right
			fillToGalleryRight(true);
		} else {
			// Similarly, empty space on the left
			fillToGalleryLeft(true);
		}

		// Clear unused views
		mRecycler.clear();

		// Set selection
		setSelectionToCenterChild(toLeft);

		invalidate();
	}

	private void detachOffScreenChildren(boolean toLeft) {
		int numChildren = getChildCount();
		int firstPosition = mFirstPosition;
		int start = 0;
		int count = 0;

		if (toLeft) {
			final int galleryLeft = 0;
			for (int i = 0; i < numChildren; i++) {
				final View child = getChildAt(i);

				if (child.getRight() >= galleryLeft) {
					break;
				} else {
					count++;
					mRecycler.put(firstPosition + i, child);
				}
			}
		} else {
			final int galleryRight = getWidth() - 0;
			for (int i = numChildren - 1; i >= 0; i--) {
				final View child = getChildAt(i);
				if (child.getLeft() <= galleryRight) {
					break;
				} else {
					start = i;
					count++;
					mRecycler.put(firstPosition + i, child);
				}
			}
		}

		detachViewsFromParent(start, count);

		if (toLeft) {
			mFirstPosition += count;

			if (mFirstPosition == mItemCount) {
				mFirstPosition = 0;
			}
		}
	}

	private int getCenterOfGallery() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	private void offsetChildrenLeftAndRight(int offset) {
		for (int i = getChildCount() - 1; i >= 0; i--) {
			getChildAt(i).offsetLeftAndRight(offset);
		}
	}

	private int getLimitedMotionScrollAmount(boolean motionToLeft, int deltaX) {
		return deltaX;
		// int extremeItemPosition = motionToLeft ? mItemCount - 1 : 0;
		// View extremeChild = getChildAt(extremeItemPosition - mFirstPosition);
		//
		//
		//
		//
		// if (extremeChild == null) {
		// return deltaX;
		// }
		//
		// int extremeChildCenter = getCenterOfView(extremeChild);
		// int galleryCenter = getCenterOfGallery();
		//
		// if (motionToLeft) {
		// if (extremeChildCenter <= galleryCenter) {
		//
		// // The extreme child is past his boundary point!
		// return 0;
		// }
		// } else {
		// if (extremeChildCenter >= galleryCenter) {
		//
		// // The extreme child is past his boundary point!
		// return 0;
		// }
		// }
		//
		// int centerDifference = galleryCenter - extremeChildCenter;
		//
		// return motionToLeft
		// ? Math.max(centerDifference, deltaX)
		// : Math.min(centerDifference, deltaX);
	}

	public boolean onTouchEvent(MotionEvent event) {

		// Give everything to the gesture detector
		boolean retValue = mGestureDetector.onTouchEvent(event);

		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			// Helper method for lifted finger
			onUp();
		} else if (action == MotionEvent.ACTION_CANCEL) {
			onCancel();
		}

		return retValue;
	}

	/**
	 * Responsible for fling behavior. Use {@link #startUsingVelocity(int)} to
	 * initiate a fling. Each frame of the fling is handled in {@link #run()}. A
	 * FlingRunnable will keep re-posting itself until the fling is done.
	 * 
	 */
	private class FlingRunnable implements Runnable {
		/**
		 * Tracks the decay of a fling scroll
		 */
		private Scroller mScroller;

		/**
		 * X value reported by mScroller on the previous fling
		 */
		private int mLastFlingX;

		public FlingRunnable() {
			mScroller = new Scroller(getContext());
		}

		private void startCommon() {
			// Remove any pending flings
			removeCallbacks(this);
		}

		public void startUsingVelocity(int initialVelocity) {
			if (initialVelocity == 0)
				return;

			startCommon();

			int initialX = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
			mLastFlingX = initialX;
			mScroller.fling(initialX, 0, initialVelocity, 0, 0,
					Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
			post(this);
		}

		public void startUsingDistance(int distance) {
			if (distance == 0)
				return;

			startCommon();

			mLastFlingX = 0;
			mScroller.startScroll(0, 0, -distance, 0, mAnimationDuration);
			post(this);
		}

		public void stop(boolean scrollIntoSlots) {
			removeCallbacks(this);
			endFling(scrollIntoSlots);
		}

		private void endFling(boolean scrollIntoSlots) {
			/*
			 * Force the scroller's status to finished (without setting its
			 * position to the end)
			 */
			mScroller.forceFinished(true);

			if (scrollIntoSlots) {
				scrollIntoSlots();
			}
		}

		public void run() {
			Log.v("scoller run", "scollerscoller");
			// if (mItemCount == 0) {
			// endFling(true);
			// return;
			// }
			//
			mShouldStopFling = false;
			//
			final Scroller scroller = mScroller;
			boolean more = scroller.computeScrollOffset();
			final int x = scroller.getCurrX();
			//
			// // Flip sign to convert finger direction to list items direction
			// // (e.g. finger moving down means list is moving towards the top)
			int delta = mLastFlingX - x;

			Log.v(String.valueOf(delta), String.valueOf(x));

			// Pretend that each frame of a fling scroll is a touch scroll
			if (delta > 0) {
				// Moving towards the left. Use first view as mDownTouchPosition
				mDownTouchPosition = mFirstPosition;

				// Don't fling more than 1 screen
				delta = Math.min(getWidth() - getPaddingLeft()
						- getPaddingRight() - 1, delta);
			} else {
				// Moving towards the right. Use last view as mDownTouchPosition
				int offsetToLast = getChildCount() - 1;
				mDownTouchPosition = mFirstPosition + offsetToLast;

				// Don't fling more than 1 screen
				delta = Math.max(-(getWidth() - getPaddingRight()
						- getPaddingLeft() - 1), delta);
			}
			//
			trackMotionScroll(delta);
			//
			if (more && !mShouldStopFling) {
				mLastFlingX = x;
				post(this);
			} else {
				endFling(true);
			}
		}

	}

	class AdapterDataSetObserver extends DataSetObserver {

		private Parcelable mInstanceState = null;

		@Override
		public void onChanged() {
			mDataChanged = true;
			mOldItemCount = mItemCount;
			mItemCount = getAdapter().getCount();

			int position = mItemCount > 0 ? 0 : INVALID_POSITION;
			setSelectedPositionInt(position);

			// Detect the case where a cursor that was previously invalidated
			// has
			// been repopulated with new data.
			if (InfiniteGallery.this.getAdapter().hasStableIds()
					&& mInstanceState != null && mOldItemCount == 0
					&& mItemCount > 0) {
				InfiniteGallery.this.onRestoreInstanceState(mInstanceState);
				mInstanceState = null;
			} else {
				// rememberSyncState();
			}
			// checkFocus();
			requestLayout();
		}

		@Override
		public void onInvalidated() {
			mDataChanged = true;

			if (InfiniteGallery.this.getAdapter().hasStableIds()) {
				// Remember the current state for the case where our hosting
				// activity is being
				// stopped and later restarted
				mInstanceState = InfiniteGallery.this.onSaveInstanceState();
			}

			// Data is invalid so we should reset our state
			mOldItemCount = mItemCount;
			mItemCount = 0;
			mSelectedPosition = INVALID_POSITION;
			// mSelectedRowId = INVALID_ROW_ID;
			// mNextSelectedPosition = INVALID_POSITION;
			// mNextSelectedRowId = INVALID_ROW_ID;
			// mNeedSync = false;
			// checkSelectionChanged();

			// checkFocus();
			requestLayout();
		}

		public void clearSavedState() {
			mInstanceState = null;
		}
	}

	@Override
	public Adapter getAdapter() {
		// TODO Auto-generated method stub
		return mAdapter;
	}

	@Override
	public void setAdapter(Adapter adapter) {
		if (null != mAdapter) {
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
			resetList();
		}

		mAdapter = adapter;

		if (mAdapter != null) {
			mOldItemCount = mItemCount;
			mItemCount = mAdapter.getCount();
			// checkFocus();

			mDataSetObserver = new AdapterDataSetObserver();
			mAdapter.registerDataSetObserver(mDataSetObserver);

			int position = mItemCount > 0 ? 0 : INVALID_POSITION;

			setSelectedPositionInt(position);
			// setNextSelectedPositionInt(position);

			if (mItemCount == 0) {
				// Nothing selected
				// checkSelectionChanged();
			}

		} else {
			// checkFocus();
			resetList();
			// Nothing selected
			// checkSelectionChanged();
		}

		int position = mItemCount > 0 ? 0 : INVALID_POSITION;
		setSelectedPositionInt(position);

		requestLayout();
	}

	@Override
	public void setSelection(int position) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);

		// if we don't have an adapter, we don't need to do anything
		if (mAdapter == null) {
			return;
		}

		mInLayout = true;
		layout(0, false);
		mInLayout = false;
	}

	private void resetList() {
		mDataChanged = false;
		// mNeedSync = false;

		removeAllViewsInLayout();
		// mOldSelectedPosition = INVALID_POSITION;
		// mOldSelectedRowId = INVALID_ROW_ID;

		setSelectedPositionInt(INVALID_POSITION);
		// setNextSelectedPositionInt(INVALID_POSITION);
		invalidate();
	}

	void layout(int delta, boolean animate) {

		int childrenLeft = 0;
		int childrenWidth = getRight() - getLeft();

		// if (mDataChanged) {
		// handleDataChanged();
		// }
		//
		// // Handle an empty gallery by removing all views.
		if (mItemCount == 0) {
			resetList();
			return;
		}

		// Update to the new selected position.
		if (getSelectedItemPosition() >= 0) {
			setSelectedPositionInt(getSelectedItemPosition());
		}

		// All views go in recycler while we are in layout
		recycleAllViews();

		// Clear out old views
		// removeAllViewsInLayout();
		detachAllViewsFromParent();

		/*
		 * These will be used to give initial positions to views entering the
		 * gallery as we scroll
		 */
		mRightMost = 0;
		mLeftMost = 0;

		// Make selected view and center it

		/*
		 * mFirstPosition will be decreased as we add views to the left later
		 * on. The 0 for x will be offset in a couple lines down.
		 */
		mFirstPosition = mSelectedPosition;
		View sel = makeAndAddView(mSelectedPosition, 0, 0, true);

		// Put the selected child in the center
		int selectedOffset = childrenLeft + (childrenWidth / 2)
				- (sel.getWidth() / 2);
		sel.offsetLeftAndRight(selectedOffset);

		fillToGalleryRight(false);
		fillToGalleryLeft(false);

		// Flush any cached views that did not get reused above
		mRecycler.clear();

		invalidate();
		// checkSelectionChanged();
		//
		mDataChanged = false;
		// mNeedSync = false;
		// setNextSelectedPositionInt(mSelectedPosition);
		//
		updateSelectedItemMetadata();
		
		//send out a event
		selectionChanged();
	}

	// put in the cache
	private void recycleAllViews() {
		int childCount = getChildCount();
		final RecycleBin recycleBin = mRecycler;

		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			int index = mFirstPosition + i;
			recycleBin.put(index, v);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidthMeasureSpec = widthMeasureSpec;
		mHeightMeasureSpec = heightMeasureSpec;
	}

	private boolean mDataChanged = true;

	private View makeAndAddView(int position, int offset, int x,
			boolean fromLeft) {
		View child = null;

		if (!mDataChanged) {
			child = mRecycler.get(position);
			if (child != null) {

				int childLeft = child.getLeft();

				// Remember left and right edges of where views have been placed
				mRightMost = Math.max(mRightMost,
						childLeft + child.getMeasuredWidth());
				mLeftMost = Math.min(mLeftMost, childLeft);

				// Position the view
				setUpChild(child, offset, x, fromLeft);

				return child;
			}
		}

		child = mAdapter.getView(position, null, this);
		setUpChild(child, offset, x, fromLeft);

		return child;
	}

	private void setUpChild(View child, int offset, int x, boolean fromLeft) {
		LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = generateDefaultLayoutParams();
		}

		// Are we positioning views based on the left edge?
		addViewInLayout(child, fromLeft ? -1 : 0, params);

		// Get measure spec
		int childHeightSpec = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec,
				0, params.height);
		int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec,
				0, params.width);

		child.measure(childWidthSpec, childHeightSpec);

		// Position vertically based on gravity setting
		int childTop = calculateTop(child, params, true);
		int childBottom = childTop + child.getMeasuredHeight();
		int childLeft;
		int childRight;

		int width = child.getMeasuredWidth();
		if (fromLeft) {
			childLeft = x;
			childRight = childLeft + width;
		} else {
			childLeft = x - width;
			childRight = x;
		}

		child.layout(childLeft, childTop, childRight, childBottom);
	}

	private int calculateTop(View child, LayoutParams lp, boolean duringLayout) {
		int myHeight = duringLayout ? getMeasuredHeight() : getHeight();
		int childHeight = duringLayout ? child.getMeasuredHeight() : child
				.getHeight();

		int childTop = 0;

		switch (mGravity) {
		case Gravity.TOP:
			childTop = 0;
			break;
		case Gravity.CENTER_VERTICAL:
			int availableSpace = myHeight - childHeight;
			childTop = availableSpace / 2;
			break;
		case Gravity.BOTTOM:
			childTop = myHeight - childHeight;
			break;
		}
		return childTop;
	}

	private void fillToGalleryLeft(boolean doLeft) {
		int itemSpacing = mSpacing;
		int galleryLeft = 0;

		// Set state for initial iteration
		View prevIterationView = getChildAt(0);
		int curPosition;
		int curRightEdge;

		if (prevIterationView != null) {
			curPosition = mFirstPosition - 1;
			curRightEdge = prevIterationView.getLeft() - itemSpacing;
		} else {
			// No children available!
			curPosition = 0;
			curRightEdge = getRight() - getLeft();
			mShouldStopFling = true;
		}

		// if the gallery reach the first, loop to the last one
		if (doLeft && curPosition == -1 && mSelectedPosition == 0) {
			int numChildren = getChildCount();
			int numItems = mItemCount;

			prevIterationView = getChildAt(numChildren - 1);

			if (prevIterationView != null) {
				mFirstPosition = numItems;
				curPosition = mFirstPosition - 1;
				// curRightEdge = prevIterationView.getLeft() - itemSpacing;
				curRightEdge = 0;
			}
		}

		while (curRightEdge > galleryLeft && curPosition >= 0) {
			prevIterationView = makeAndAddView(curPosition, curPosition
					- mSelectedPosition, curRightEdge, false);

			// Remember some state
			mFirstPosition = curPosition;

			// Set state for next iteration
			curRightEdge = prevIterationView.getLeft() - itemSpacing;
			curPosition--;
		}
	}

	private void fillToGalleryRight(boolean toRight) {
		int itemSpacing = mSpacing;
		int galleryRight = getRight() - getLeft();
		int numChildren = getChildCount();
		int numItems = mItemCount;

		// Set state for initial iteration
		View prevIterationView = getChildAt(numChildren - 1);
		int curPosition;
		int curLeftEdge;

		if (prevIterationView != null) {
			if (mFirstPosition == mItemCount - 1) {
				curPosition = 0;
			} else {
				curPosition = mFirstPosition + numChildren;
			}

			curLeftEdge = prevIterationView.getRight() + itemSpacing;
		} else {
			mFirstPosition = curPosition = mItemCount - 1;
			curLeftEdge = 0;
			mShouldStopFling = true;
		}

		while (curLeftEdge < galleryRight && curPosition < numItems) {
			prevIterationView = makeAndAddView(curPosition, curPosition
					- mSelectedPosition, curLeftEdge, true);

			// Set state for next iteration
			curLeftEdge = prevIterationView.getRight() + itemSpacing;
			curPosition++;
		}
	}

	/**
	 * Cache Render View
	 * 
	 * @author James
	 * 
	 */
	class RecycleBin {
		private SparseArray<View> mScrapHeap = new SparseArray<View>();

		public void put(int position, View v) {
			mScrapHeap.put(position, v);
		}

		View get(int position) {
			// System.out.print("Looking for " + position);
			View result = mScrapHeap.get(position);
			if (result != null) {
				// System.out.println(" HIT");
				mScrapHeap.delete(position);
			} else {
				// System.out.println(" MISS");
			}
			return result;
		}

		View peek(int position) {
			// System.out.print("Looking for " + position);
			return mScrapHeap.get(position);
		}

		void clear() {
			final SparseArray<View> scrapHeap = mScrapHeap;
			final int count = scrapHeap.size();
			for (int i = 0; i < count; i++) {
				final View view = scrapHeap.valueAt(i);
				if (view != null) {
					removeDetachedView(view, true);
				}
			}
			scrapHeap.clear();
		}
	}
	
	/**
	 * Rewrite the private function from AdapterView
	 * 
	 */
	private boolean mInLayout = false;
	private SelectionNotifier mSelectionNotifier;
    private class SelectionNotifier extends Handler implements Runnable {
        public void run() {
            if (mDataChanged) {
                // Data has changed between when this SelectionNotifier
                // was posted and now. We need to wait until the AdapterView
                // has been synched to the new data.
                post(this);
            } else {
                fireOnSelected();
            }
        }
    }

    void selectionChanged() {
        if (getOnItemSelectedListener() != null) {
            if (mInLayout) {
                // If we are in a layout traversal, defer notification
                // by posting. This ensures that the view tree is
                // in a consistent state and is able to accomodate
                // new layout or invalidate requests.
                if (mSelectionNotifier == null) {
                    mSelectionNotifier = new SelectionNotifier();
                }
                mSelectionNotifier.post(mSelectionNotifier);
            } else {
                fireOnSelected();
            }
        }
    }

    private void fireOnSelected() {
        if (getOnItemSelectedListener() == null)
            return;

        int selection = mSelectedPosition;
        if (selection >= 0) {
            View v = getSelectedView();
            getOnItemSelectedListener().onItemSelected(this, v, selection,
                    getAdapter().getItemId(selection));
        } else {
        	getOnItemSelectedListener().onNothingSelected(this);
        }
    }
	
    /**
     * 
     * Rewrite from gallery
     */
    
    /**
     * If true, do not callback to item selected listener. 
     */
    private boolean mShouldCallbackDuringFling = true;
    private boolean mSuppressSelectionChanged;
    private boolean mIsFirstScroll;
    
    /**
     * Duration in milliseconds from the start of a scroll during which we're
     * unsure whether the user is scrolling or flinging.
     */
    private static final int SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250;
    
    private void onFinishedMovement() {
        if (mSuppressSelectionChanged) {
            mSuppressSelectionChanged = false;
            
            // We haven't been callbacking during the fling, so do it now
            selectionChanged();
        }
    }
    
    /**
     * Sets mSuppressSelectionChanged = false. This is used to set it to false
     * in the future. It will also trigger a selection changed.
     */
    private Runnable mDisableSuppressSelectionChangedRunnable = new Runnable() {
        public void run() {
            mSuppressSelectionChanged = false;
            selectionChanged();
        }
    };
}

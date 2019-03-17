/**
 * 
 */
package proteus.gwt.client.app.ui.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;

/**
 * A balloon tooltip implementation
 * 
 * see:
 * http://fascynacja.wordpress.com/2010/07/06/gwt-balloon-widget-in-5-minutes/
 * 
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class BalloonTooltip extends PopupPanel {

	private static final Logger logger = Logger.getLogger("BalloonTooltip");
	
	// TODO add background image around the tooltip

	private static BalloonTooltip globalTooltip = new BalloonTooltip("", true);
	
	private int showCount = 0;
	
	private boolean shouldHide = true;
	private int  duration     = 1200;
	private int  delay         = 2500;
	
	private BalloonTooltipPanel panel;
	private BalloonAnimation hideAnim;
	
	public BalloonTooltip(String text, boolean shouldHide) {
		this.setShouldHide(shouldHide);
		
		setAutoHideEnabled(true);
		setAnimationEnabled(true);
		setStyleName("balloonTooltip");

		panel = new BalloonTooltipPanel(text);
		setWidget(panel);
	}
	
	public static BalloonTooltip getGlobalTooltip() {
		return globalTooltip;
	}
	
	public BalloonTooltip setText(String text) {
		panel.setText(text);
		return this;
	}
	
	public void show(UIObject object) {
		// the offsets are according to your border image sizes
		int left = object.getAbsoluteLeft() + object.getOffsetWidth() - 25;
		int top = object.getAbsoluteTop() + object.getOffsetHeight() - 5;
		
		setPopupPosition(left, top);
		show();
	}
	
	@Override
	public void show() {
		showCount++;
		logger.log(Level.FINE, "show count = " + showCount);
		
		// hide existing if it's already shown
		if (this.isShowing()) {
			hideAnim.cancel();
			this.hide();
		}
		
		// show self
		BalloonAnimation showBalloon = new BalloonAnimation();
		showBalloon.run(getDuration());
		super.show();
		
		if (isShouldHide())
		{
			hideAnim = new BalloonAnimation(false);
	        // run hide animation after some time
			hideAnim.run(getDuration(), Duration.currentTimeMillis() + getDelay());
			
			Timer t = new Timer() {
				int id = showCount;
				@Override
				public void run() {
					if (showCount == id)
						BalloonTooltip.this.hide();
				}
			};
			t.schedule(getDelay() + getDuration());
		}
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param shouldHide the shouldHide to set
	 */
	private void setShouldHide(boolean shouldHide) {
		this.shouldHide = shouldHide;
	}

	/**
	 * @return the shouldHide
	 */
	private boolean isShouldHide() {
		return shouldHide;
	}

	private class BalloonAnimation extends Animation {
		boolean show = true;
	
		BalloonAnimation(boolean show) {
			super();
			this.show = show;
		}
	
		public BalloonAnimation() {
			this(true);
		}
	
		@Override
		protected void onUpdate(double progress) {
			double opacityValue = progress;
			if (!show) {
				opacityValue = 1.0 - progress;
			}
			BalloonTooltip.this.getElement().getStyle().setOpacity(
					opacityValue);
		}
	}
	
	public class BalloonTooltipPanel extends DecoratorPanel {
		
		private HTML html;
		
		public BalloonTooltipPanel(String text) {
			setStyleName("balloonTooltipDecorator");
			
			html = new HTML();
			
			setWidget(html);
			setText(text);
		}
		
		public void setText(String text) {
			html.setHTML(text);
		}
	}
}

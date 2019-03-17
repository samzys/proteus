/**
 * 
 */
package emu.java.swing;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import emu.java.awt.event.ActionEvent;
import emu.java.beans.PropertyChangeEvent;
import emu.java.beans.PropertyChangeListener;

/**
 *
 * @author Lei Ting
 * Created Apr 7, 2011
 */
public class ActionTest implements PropertyChangeListener {

	TestModel model = new TestModel();
	
	public ActionTest() {
		for (int i = 0; i < 50; ++i) model.incr();
	}

	@Test
	public void TestActions() {
		Map<String, Action> actionMap = new HashMap<String, Action>();
		actionMap.put("add", new TestAddAction(model));
		actionMap.put("subtract", new TestSubtractAction(model));

		// test add action
		actionMap.get("add").actionPerformed(null);
		assertEquals(51, model.get());
		
		// test PropertyChangeListener
		actionMap.get("add").addPropertyChangeListener(this);
		actionMap.get("add").putValue("request", "reset");
		assertEquals(0, model.get());
		
		// test subtract action
		actionMap.get("subtract").actionPerformed(null);
		assertEquals(-1, model.get());
	}
	
	public class TestModel {
		int count = 0;
		
		public void incr() {
			count++;
		}
		
		public void decr() {
			count--;
		}
		
		public int get() { 
			return count; 
		}
		
		public void reset() {
			count = 0;
		}
	}
	
	public class TestAddAction extends AbstractAction {
		TestModel model;
		
		public TestAddAction(TestModel model) {
			this.model = model;
		}
		
		/* (non-Javadoc)
		 * @see emu.java.awt.event.ActionListener#actionPerformed(emu.java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			model.incr();
		}
		
	}

	public class TestSubtractAction extends AbstractAction {
		TestModel model;
		
		public TestSubtractAction(TestModel model) {
			this.model = model;
		}
		
		/* (non-Javadoc)
		 * @see emu.java.awt.event.ActionListener#actionPerformed(emu.java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			model.decr();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see emu.java.beans.PropertyChangeListener#propertyChange(emu.java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		
		if (name.equals("request")) {
			if (evt.getNewValue().equals("reset")) {
				model.reset();
			}
		}
	}
	
	
}

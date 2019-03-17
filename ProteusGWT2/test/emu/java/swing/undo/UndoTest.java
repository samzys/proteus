/**
 * 
 */
package emu.java.swing.undo;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.Test;

import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;

/**
 *
 * @author Lei Ting
 * Created Apr 7, 2011
 */
public class UndoTest {

	private static final Logger log = Logger.getLogger("UndoTest");
	
	@Test
	public void testUndo() {
		TestModel model = new TestModel();
		
		UndoableEditSupport undoSupport = new UndoableEditSupport();
		UndoManager undoManager = new UndoManager();

		undoManager.setLimit(1000);
		undoSupport.addUndoableEditListener(undoManager);
		
		// add 100
		for (int i = 0; i < 100; ++i) {
			undoSupport.postEdit(new TestAddEdit(model));
		}
		assertEquals(100, model.get());
		
		// undo, redo, redo
		undoManager.undo();
		assertEquals(99, model.get());

		try {
			undoManager.redo();
			undoManager.redo();
		} catch (CannotRedoException e) {
			log.info("no edit to redo");
		}
		assertEquals(100, model.get());
		
		// subtract 50
		for (int i = 0; i < 50; ++i) {
			undoSupport.postEdit(new TestSubstractEdit(model));
		}
		assertEquals(50, model.get());

		// redo undo redo
		try {
			undoManager.redo();
		} catch (CannotRedoException e) {
			log.info("no edit to redo");
		}
		undoManager.undo();
		assertEquals(51, model.get());

		undoManager.redo();
		assertEquals(50, model.get());

		// compound edits end. undo all & redo all
		undoManager.end();
		undoManager.undo();
		assertEquals(0, model.get());
		undoManager.redo();
		assertEquals(50, model.get());
		
		// start new edit
		undoSupport.removeUndoableEditListener(undoManager);
		undoManager = new UndoManager();
		undoSupport.addUndoableEditListener(undoManager);
		
		// and add 30
		for (int i = 0; i < 30; ++i) {
			undoSupport.postEdit(new TestAddEdit(model));
		}
		assertEquals(80, model.get());

		undoManager.undo();
		assertEquals(79, model.get());
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
	}
	
	public class TestAddEdit extends AbstractUndoableEdit {
		
		public TestModel model;
		
		public TestAddEdit(TestModel model) {
			this.model = model;
			redo();
		}
		
		public void undo() throws CannotUndoException {
			model.decr();
		}

		public void redo() throws CannotRedoException {
			model.incr();
		}
	}
	

	public class TestSubstractEdit extends AbstractUndoableEdit {
		
		public TestModel model;
		
		public TestSubstractEdit(TestModel model) {
			this.model = model;
			redo();
		}
		
		public void undo() throws CannotUndoException {
			model.incr();
		}

		public void redo() throws CannotRedoException {
			model.decr();
		}
	}
}

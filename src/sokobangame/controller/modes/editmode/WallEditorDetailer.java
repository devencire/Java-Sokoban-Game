package sokobangame.controller.modes.editmode;

import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;

import sokobangame.controller.modes.EditMode;
import sokobangame.model.MazeObject;
import sokobangame.model.objects.Wall;

public class WallEditorDetailer implements MazeObjectEditorDetailer {
	public static final WallEditorDetailer INSTANCE = new WallEditorDetailer();
	
	public String getInvalidMoveDialogText() {
		return "A wall cannot be moved on top of anything else.";
	} 
	
	public void populateToolbar(final EditMode mode, MazeObject o) {
		final Wall wall = (Wall)o;
		
		final JTextField textField;
		
		String string = wall.getLabel();
		if (string != null) {
			textField = new JTextField(string);
		} else {
			textField = new JTextField("");
		}
		
		//A custom document filter prevents the text field from containing more than one character.
		((AbstractDocument)textField.getDocument()).setDocumentFilter(new SingleCharacterFilter()); 
		
		//Limits the size of the text field.
		textField.setMaximumSize(new Dimension(50,10000));
		textField.setHorizontalAlignment(JTextField.CENTER);
		
		//We listen to the document so we can update the Wall with label changes.
		textField.getDocument().addDocumentListener(new DocumentListener() {

			public void update(DocumentEvent e) {
				wall.setLabel(textField.getText());
			}

			public void changedUpdate(DocumentEvent e) {update(e);}
			
			public void insertUpdate(DocumentEvent e) {update(e);}
			
			public void removeUpdate(DocumentEvent e) {update(e);}
			
		});
		
		mode.getToolBar().addComponentFromSource(this, new JToolBar.Separator());
		mode.getToolBar().addComponentFromSource(this, textField);
		
	}
	
	/** Prevents a document from containing more than one character. */
	public class SingleCharacterFilter extends DocumentFilter {  
		
		public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, javax.swing.text.AttributeSet attr) throws BadLocationException {  
	        if (fb.getDocument().getLength() + text.length() <= 1)  
	               fb.insertString(offset, text, attr);    
	    }
	  
	    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attr) throws BadLocationException {  
	        if (fb.getDocument().getLength() + text.length() - length <= 1)  
	               fb.replace(offset, length, text, attr);   
	    }  
	}

}

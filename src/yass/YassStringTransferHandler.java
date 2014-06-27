package yass;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *  Description of the Class
 *
 * @author     Saruta
 * @created    22. September 2007
 */
public abstract class YassStringTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 4388712859582396739L;


	/**
	 *  Description of the Method
	 *
	 * @param  c  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected abstract String exportString(JComponent c);


	/**
	 *  Description of the Method
	 *
	 * @param  c    Description of the Parameter
	 * @param  str  Description of the Parameter
	 */
	protected abstract void importString(JComponent c, String str);


	/**
	 *  Description of the Method
	 *
	 * @param  c       Description of the Parameter
	 * @param  remove  Description of the Parameter
	 */
	protected abstract void cleanup(JComponent c, boolean remove);


	/**
	 *  Description of the Method
	 *
	 * @param  c  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Transferable createTransferable(JComponent c) {
		return new StringSelection(exportString(c));
	}


	/**
	 *  Gets the sourceActions attribute of the YassStringTransferHandler object
	 *
	 * @param  c  Description of the Parameter
	 * @return    The sourceActions value
	 */
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  c  Description of the Parameter
	 * @param  t  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	public boolean importData(JComponent c, Transferable t) {
		if (canImport(c, t.getTransferDataFlavors())) {
			try {
				String str = (String) t.getTransferData(DataFlavor.stringFlavor);
				importString(c, str);
				return true;
			}
			catch (UnsupportedFlavorException ufe) {
			}
			catch (IOException ioe) {
			}
		}

		return false;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  c       Description of the Parameter
	 * @param  data    Description of the Parameter
	 * @param  action  Description of the Parameter
	 */
	protected void exportDone(JComponent c, Transferable data, int action) {
		cleanup(c, action == MOVE);
	}


	/**
	 *  Description of the Method
	 *
	 * @param  c        Description of the Parameter
	 * @param  flavors  Description of the Parameter
	 * @return          Description of the Return Value
	 */
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (DataFlavor.stringFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}
}


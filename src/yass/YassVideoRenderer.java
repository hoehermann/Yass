package yass;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.RGBFormat;
import javax.media.renderer.VideoRenderer;
import javax.media.util.ImageToBuffer;
import javax.swing.JComponent;

import com.omnividea.FobsConfiguration;

/**
 *  Description of the Class
 *
 * @author     Saruta
 * @created    27. Juli 2008
 */
public class YassVideoRenderer implements VideoRenderer, FrameGrabbingControl {
	private JComponent comp = null;

	private RGBFormat vf;
	private BufferedImage lastImage = null;

	private int width = -1, height = -1;


	/**
	 *  Constructor for the Java2DRenderer object
	 */
	public YassVideoRenderer() { }


	/**
	 *  Sets the value attribute of the YassVideoRenderer object
	 *
	 * @param  aValue      The new value value
	 * @param  isSelected  The new value value
	 */
	void setValue(Object aValue, boolean isSelected) {
		//System.out.println(aValue.getClass().getName());
	}


	/**
	 *  Gets the supportedInputFormats attribute of the YassVideoRenderer object
	 *
	 * @return    The supportedInputFormats value
	 */
	public Format[] getSupportedInputFormats() {
		return new Format[]{new RGBFormat()};
	}


	/**
	 *  Sets the inputFormat attribute of the YassVideoRenderer object
	 *
	 * @param  format  The new inputFormat value
	 * @return         Description of the Return Value
	 */
	public Format setInputFormat(Format format) {
		//System.out.println("Fobs Java2DRenderer: setInputFormat");
		FobsConfiguration.videoFrameFormat = FobsConfiguration.RGBA;

		vf = (RGBFormat) format;
		width = (int) vf.getSize().getWidth();
		height = (int) vf.getSize().getHeight();
		return format;
	}


	/**
	 *  Gets the bounds attribute of the YassVideoRenderer object
	 *
	 * @return    The bounds value
	 */
	public Rectangle getBounds() {
		return new Rectangle(width, height);
	}


	/**
	 *  Sets the bounds attribute of the YassVideoRenderer object
	 *
	 * @param  r  The new bounds value
	 */
	public void setBounds(Rectangle r) {
		comp.setBounds(r);
	}


	/**
	 *  Description of the Method
	 */
	public void start() {
		System.out.println("Fobs Java2DRenderer: start");
	}


	/**
	 *  Description of the Method
	 */
	public void stop() {
		System.out.println("Fobs Java2DRenderer: stop");
	}


	/**
	 *  Description of the Method
	 *
	 * @param  buffer  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public int process(Buffer buffer) {
		//Graphics2D g2d = (Graphics2D) comp.getGraphics();
		//Rectangle r = ((javax.swing.JViewport) comp.getParent()).getViewRect();

		//if (g2d != null) {
		//	if (lastImage == null) {
		lastImage = bufferToImage(buffer);
		//	}
		//	int w = r.width;
		//	int h = (int) (w * 3 / 4.0);
		//	int yy = (int) (r.height / 2 - h / 2);
		//g2d.drawImage(lastImage, r.x, yy, r.width, h, null);

		// // g2d.dispose();
		//}

		return BUFFER_PROCESSED_OK;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  buffer  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public BufferedImage bufferToImage(Buffer buffer) {
		RGBFormat format = (RGBFormat) buffer.getFormat();
		int rMask;
		int gMask;
		int bMask;
		Object data = buffer.getData();
		DirectColorModel dcm;

		rMask = format.getRedMask();
		gMask = format.getGreenMask();
		bMask = format.getBlueMask();
		int[] masks = new int[3];
		masks[0] = rMask;
		masks[1] = gMask;
		masks[2] = bMask;

		DataBuffer db = new DataBufferInt((int[]) data,
			format.getLineStride() *
			format.getSize().height);

		SampleModel sm = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT,
			format.getLineStride(),
			format.getSize().height,
			masks);
		WritableRaster wr = Raster.createWritableRaster(sm, db, new Point(0, 0));

		dcm = new DirectColorModel(24, rMask, gMask, bMask);
		return new BufferedImage(dcm, wr, true, null);
	}


	/**
	 *  Gets the name attribute of the YassVideoRenderer object
	 *
	 * @return    The name value
	 */
	public String getName() {
		return "Yass Fobs Java2DRenderer";
	}


	/**
	 *  Description of the Method
	 *
	 * @exception  ResourceUnavailableException  Description of the Exception
	 */
	public void open()
		 throws ResourceUnavailableException {

	}


	/**
	 *  Description of the Method
	 */
	public void close() {
		lastImage = null;
	}


	/**
	 *  Description of the Method
	 */
	public void reset() {
		lastImage = null;
	}


	/**
	 *  Gets the component attribute of the YassVideoRenderer object
	 *
	 * @return    The component value
	 */
	public Component getComponent() {
		return (Component) comp;
	}


	/**
	 *  Sets the component attribute of the YassVideoRenderer object
	 *
	 * @param  c  The new component value
	 * @return    Description of the Return Value
	 */
	public boolean setComponent(Component c) {
		comp = (JComponent) c;
		return false;
	}

	// support for FrameGrabbingControl
	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public Buffer grabFrame() {
		Buffer buf = null;
		if (lastImage != null) {
			buf = ImageToBuffer.createBuffer(lastImage, (float) 0);
		}
		return buf;
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public BufferedImage grabImage() {
		return lastImage;
	}


	// No awt component is needed for FrameGrabbingControl
	/**
	 *  Gets the controlComponent attribute of the YassVideoRenderer object
	 *
	 * @return    The controlComponent value
	 */
	public Component getControlComponent() {
		return null;
	}


	/**
	 *  Gets the controls attribute of the YassVideoRenderer object
	 *
	 * @return    The controls value
	 */
	public Object[] getControls() {
		Object[] obj = {this};
		return obj;
	}


	/**
	 *  Gets the control attribute of the YassVideoRenderer object
	 *
	 * @param  arg  Description of the Parameter
	 * @return      The control value
	 */
	public Object getControl(String arg) {
		Object rc = null;
		if (arg.equals("javax.media.control.FrameGrabbingControl")) {
			rc = this;
		}
		return rc;
	}

}


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class SerialTest extends JFrame implements SerialPortEventListener, KeyListener {
	TextField t1;
	Label l1;
	Label l2;
	SerialPort serialPort;
        /** The port we're normally going to use. */
	Scanner sc;
	
	public SerialTest(){
		super();
		Panel p = new Panel();
		l1 = new Label("Key Listener");
		l2 = new Label("Use the arrow keys to move the servo left and right.");
		p.add(l2);
		p.add(l1);
		add(p);
		addKeyListener(this);
		setSize(350,100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static String comSelect()
	{
		String select = JOptionPane.showInputDialog(null, "Enter the COM port (ex: COM1) ");
		return select;
	}
	
	private static String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			comSelect(), // Windows
			};

	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
/*
	public void String COMSelect() {
		
		return COM;
	}
	*/
	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public void sendData(){
		sc = new Scanner(System.in);
		int direction = 90;
		while(direction != 0){
		
			System.out.println("Enter the direction to move: ");
			String d = sc.next();
			direction = Integer.parseInt(d);
			try{
				output.write(direction);
			}
			catch(IOException e){}
		}
	}
	public void windowClosing(WindowEvent e)
	{
		System.exit(0);
	}
	
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){
		l1.setText("stop");
		try{
			output.write(3);
		}
		catch(IOException stop2){}

		
	}
	
	public void keyPressed(KeyEvent e) {
	    int keyCode;
	    int direction = 90;
    	keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_LEFT:
	            	direction = 1;
	            	l1.setText("left");
	            	try{
	    				output.write(direction);
	    			}
	    			catch(IOException left){}
	            break;
	        case KeyEvent.VK_RIGHT :
	        	direction = 2;
	        	l1.setText("right");
            	try{
    				output.write(direction);
    			}
    			catch(IOException right){}		           
	            break;
	        case KeyEvent.VK_DOWN :
	        	direction = 3;
	        	l1.setText("stop");
	        	try{
	        		
	        		output.write(direction);
	        	}
	        	catch(IOException stop1){}
	     }
	    
	} 

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);

				// Displayed results are codepage dependent
				System.out.print(new String(chunk));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public static void main(String[] args) throws Exception {
		SerialTest main = new SerialTest();
		main.initialize();
		//main.sendData();
	}
}


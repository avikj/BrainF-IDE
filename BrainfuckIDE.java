
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class BrainfuckIDE implements KeyListener, MouseListener {	
	private String code = " [] ";
	private String input = "Should not show up";
	private JFrame editorPanel;
	private JButton choose;
	private JButton javabutton;
	private JButton newbutton;
	private File file;
	private JButton savebutton;
	private Color backgroundColor = Color.BLACK;
	private boolean fileOpened = false;
	
	JButton button = new JButton("Debug");
	JButton button2 = new JButton("Run");
	private JLabel codingLabel = new JLabel("Brainf*** code:");
	private JLabel outputLabel = new JLabel("Program Output:");
	private JLabel inputLabel = new JLabel("Program Input:");
	private JTextArea codingArea = new JTextArea(30, 80);
	private JScrollPane scroller = new JScrollPane(codingArea);
	private JTextArea outputWindow = new JTextArea();
	private JTextField inputField = new JTextField(50);
	private VisualPanel canvas;
	private Font monospace16 = new Font("Monospaced", Font.PLAIN, 20);
	BorderLayout layout = new BorderLayout(40, 40);
	private byte[] data = new byte[30000];
	private String output = "";
	private boolean done = false;
	int i = 0;
	int pointer = 0, inCount = 0;
	private int maxPointer = 10;
	private String error = "";
	public static void main(String[] args) {
		BrainfuckIDE c = new BrainfuckIDE();
		c.Run(c);
	}

	public void Run(BrainfuckIDE vr) {
		editorPanel = new JFrame("Brainf***");
		editorPanel.setVisible(true);
		editorPanel.setSize(1000, 870);
		choose = new JButton("Open File");
		newbutton = new JButton("New Program");
		editorPanel.getContentPane().setLayout(null);
		editorPanel.getContentPane().add(choose);
		
		editorPanel.setSize(1000, 870);
		editorPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		editorPanel.getContentPane().setBackground(backgroundColor);
		canvas = new VisualPanel();
		editorPanel.setLayout(null);
		inputField.setBackground(backgroundColor);
		inputField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		inputField.setFont(monospace16);
		inputField.setForeground(Color.WHITE);
		inputField.setBounds(0, 572, 500, 40);
		
		inputLabel.setFont(monospace16);
		inputLabel.setForeground(Color.WHITE);
		inputLabel.setBounds(0, 542, 500, 30);
		
		codingArea.setBackground(backgroundColor);
		codingArea.setFont(monospace16);
		codingArea.setForeground(Color.WHITE);
		codingArea.setBounds(0, 30, 500, 500);
		codingArea.setTabSize(2);
		codingArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setBounds(0, 30, 500, 500);
		scroller.getVerticalScrollBar().setUI(new MyScrollBarUI());
		scroller.getHorizontalScrollBar().setUI(new MyScrollBarUI());
		
		
		
		codingLabel.setFont(monospace16);
		codingLabel.setForeground(Color.WHITE);
		codingLabel.setBounds(0, 0, 500, 30);
		
		button2.setBounds(0, 660, 200, 80);
		button2.setBackground(backgroundColor);
		button2.setFont(monospace16);
		button2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		button2.setForeground(Color.WHITE);
		
		button.setBounds(300, 660, 200, 80);
		button.setBackground(backgroundColor);
		button.setFont(monospace16);
		button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		button.setForeground(Color.WHITE);
		
		newbutton.setBounds(600, 460, 300, 80);
		newbutton.setBackground(backgroundColor);
		newbutton.setFont(monospace16);
		newbutton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		newbutton.setForeground(Color.WHITE);
		
		
		
		savebutton=new JButton("Save");
		savebutton.setBounds(600, 660, 300, 80);
		savebutton.setBackground(backgroundColor);
		savebutton.setFont(monospace16);
	    savebutton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		savebutton.setForeground(Color.WHITE);
		
		javabutton=new JButton("Translate to Java");
		javabutton.setBounds(600, 360, 300, 80);
		javabutton.setBackground(backgroundColor);
		javabutton.setFont(monospace16);
	    javabutton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		javabutton.setForeground(Color.WHITE);
		
		choose = new JButton("Open File");
		choose.setFont(monospace16);
		choose.setBackground(backgroundColor);
		choose.setBounds(600, 560, 300, 80);
		choose.setForeground(Color.WHITE);
		choose.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		outputWindow.setBackground(backgroundColor);
		outputWindow.setFont(monospace16);
		outputWindow.setForeground(Color.WHITE);
		outputWindow.setBounds(550, 30, 400, 200);
		outputWindow.setTabSize(2);
		outputWindow.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		outputWindow.setEditable(false);
		
		outputLabel.setFont(monospace16);
		outputLabel.setForeground(Color.WHITE);
		outputLabel.setBounds(550, 0, 400, 30);
		editorPanel.add(codingLabel);
		editorPanel.add(inputLabel);
		editorPanel.add(inputField);
		editorPanel.add(button2);
		editorPanel.add(button);
		editorPanel.add(outputWindow);
		editorPanel.add(outputLabel);
		editorPanel.add(inputField);
		editorPanel.add(scroller);
		editorPanel.add(savebutton);
		editorPanel.add(javabutton);
		
		editorPanel.add(choose);
		editorPanel.add(newbutton);
		//editorPanel.setContentPane(editorPanel);
		newbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				codingArea.setText("");
				fileOpened = false;
			}
		});
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFrame debuggerFrame = new JFrame("Brainf*** debugger");
				code = (" "+codingArea.getText()+" ");
				input = inputField.getText();
				canvas = new VisualPanel();
				quickInterpret(code, input);
				data = new byte[30000];
				output = "";
				done = false;
				i = 0;
				pointer = 0; inCount = 0;
				debuggerFrame.setContentPane(canvas);
				debuggerFrame.setSize(1000, 870);
				debuggerFrame.setVisible(true);
				canvas.repaint();
				canvas.addKeyListener(vr);
				canvas.addMouseListener(vr);
			}
		});
		
		javabutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String enteredCode = codingArea.getText();
				JFrame javaFrame = new JFrame("Java Translation");
				javaFrame.setSize(1000, 870);
				javaFrame.getContentPane().setLayout(null);
				JTextArea javaCodeArea = new JTextArea();
				BrainToJava.writeToTextArea(javaCodeArea, enteredCode);
				javaFrame.getContentPane().setBackground(backgroundColor);
				javaFrame.setVisible(true);
				javaCodeArea.setBackground(backgroundColor);
				javaCodeArea.setFont(monospace16);
				javaCodeArea.setForeground(Color.WHITE);
				javaCodeArea.setBounds(5, 5, 970, 490);
				javaCodeArea.setTabSize(3);
				javaCodeArea.setEditable(false);
				javaCodeArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
				JScrollPane javascroller = new JScrollPane(javaCodeArea);
				javascroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				javascroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				javascroller.setBounds(5, 5, 970, 690);
				javascroller.getVerticalScrollBar().setUI(new MyScrollBarUI());
				javascroller.getHorizontalScrollBar().setUI(new MyScrollBarUI());
				javaFrame.add(javascroller);
				
			}
		});
		
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				code = " "+codingArea.getText()+" ";
				input = inputField.getText();
				outputWindow.setText(quickInterpret(code, input));
				code = "";
				input = "";
			}
		});
		
		savebutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				PrintWriter saver = null;
				if(fileOpened){
					try {
						saver = new PrintWriter(file);
					} catch (FileNotFoundException e1) {
						System.out.println("Could not find file to save to.");
					}
					String[] lines = codingArea.getText().split("\\n");
					for(int l = 0; l < lines.length; l++){
						saver.println(lines[l]);
					}
					saver.close();
				}
				else{
					try{
						JFileChooser chooser = new JFileChooser();
						setColors(chooser, Color.WHITE, backgroundColor);
						chooser.setDialogTitle("Save as...");
						int returnVal = chooser.showSaveDialog(editorPanel);
						if(returnVal==JFileChooser.APPROVE_OPTION){
							file = chooser.getSelectedFile();
							if(!file.isDirectory()){
								saver = new PrintWriter((file));
								String[] lines = codingArea.getText().split("\\n");
								for(int l = 0; l < lines.length; l++){
									saver.println(lines[l]);
								}
								saver.close();
								fileOpened = true;
							}
						}

						
					}catch(Exception E){}
				}
			}
		});
		
		choose.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ae){
			try{
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
				chooser.setFileFilter(filter);
				setColors(chooser, Color.WHITE, backgroundColor);
				chooser.setDialogTitle("Open Brainf*** File");
				int returnVal = chooser.showOpenDialog(editorPanel);
				String fileContents = "";
				if(returnVal==JFileChooser.APPROVE_OPTION){
					file = chooser.getSelectedFile();
					if(file.exists()&&!file.isDirectory()){
						Scanner sc = new Scanner(file);
						while(sc.hasNextLine())
							fileContents += sc.nextLine()+"\n";
						sc.close();

						fileOpened = true;;
					}	
				}
				codingArea.setText(fileContents);
			}catch(Exception e){}
		}});
		
		//editorPanel.getContentPane().add(canvas);
		editorPanel.setVisible(true);
		
	}
	
	private static void setColors(Component c, Color fg, Color bg) {
	    setColors0(c.getAccessibleContext(), fg, bg);
	}

	private static void setColors0(AccessibleContext ac, Color fg, Color bg) {
	    ac.getAccessibleComponent().setForeground(fg);
	    ac.getAccessibleComponent().setBackground(bg);
	    int n = ac.getAccessibleChildrenCount();
	    for (int i=0; i<n; i++) {
	        setColors0(ac.getAccessibleChild(i).getAccessibleContext(), fg, bg);
	    }
	}
	
	class VisualPanel extends JPanel {
		public void paintComponent(Graphics g) {
			setBackground(Color.BLACK);
			super.paintComponent(g);
			
			drawArray(g);
			drawCode(g);
			
		}
		public void drawArray(Graphics g){
			double scale = 930.0/maxPointer;
			g.setFont(new Font("Monospaced", Font.PLAIN, (int)(scale/2)));
			for(int i = 0; i <=maxPointer; i++){
				g.setColor(Color.WHITE);
				if(i==pointer){
					g.setColor(Color.GREEN);
					if(!error.equals(""))
						g.setColor(Color.RED);
				}
				g.drawRect((int)(scale*i), 0, (int)(scale-1), (int)scale);
				g.drawString(data[i]+"", (int)(scale*i+1), (int)(scale-5));
				g.drawString((char)data[i]+"", (int)(scale*i+scale/3), (int)(scale+30));
			}
			g.setFont(new Font("Monospaced", Font.PLAIN, 20));
			
			g.setColor(Color.WHITE);
			g.drawString("Program Output:", 20, 600);
			g.setColor(Color.GREEN);
			g.setFont(new Font("Monospaced", Font.PLAIN, 40));
			g.drawString(output, 20, 640);
		}
		public void drawCode(Graphics g){
			g.setFont(new Font("Monospaced", Font.PLAIN, 16));
			int currentx = 8;
			int currenty = 150;
			for(int k = 0; k < code.length(); k++){
				if(code.charAt(k)=='.'||code.charAt(k)==','||code.charAt(k)=='>'||code.charAt(k)=='<'||code.charAt(k)=='+'||code.charAt(k)=='-'||code.charAt(k)=='['||code.charAt(k)==']'){
					g.setColor(Color.WHITE);
					if(i==k){
						g.setColor(Color.GREEN);
					}
					g.drawString(code.charAt(k)+"", currentx, currenty);
					currentx+=12;
					if(currentx>800){
						currentx = 12; currenty+=20;
					}
				}
			}
		}
		public void interpret(){			
			char[] chars = code.toCharArray();
			switch(chars[i]){
				case '>':
					if(pointer<30000)
						pointer++;
					break;
				case '<':
					if(pointer>0)
						pointer--;
					break;
				case '+':
					data[pointer]+=1;
					break;
				case '-':
					data[pointer]-=1;
					break;
				case '.':
					output+=(char)data[pointer];
					break;
				case ',':
					if(inCount < input.length())
						data[pointer]=(byte)input.charAt(inCount++);
					else
						data[pointer]=0;
					break;
				case '[':
					if(data[pointer]==0){
						int j=1;
						while(j>0){
							i++;
							char ch =  code.charAt(i);
							if(ch=='[') j++;
							else if(ch==']') j--;
							System.out.println(j);
						}i++;
					}
					break;
				case ']':
					if(data[pointer]!=0){
						int j=1;
						while(j>0){
							i--;
							char ch = code.charAt(i);
							if(ch==']') j++;
							else if(ch=='[') j--;
						}
					}
					break;
			}
		}
	}
	
	public void mousePressed(MouseEvent evt) {
		canvas.requestFocus();
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if(!done){
			if(i<code.length()-1){	//when it is finished, it doesnt go 
				i++;
				while(!(code.charAt(i)=='.'||code.charAt(i)==','||code.charAt(i)=='-'||code.charAt(i)=='+'||code.charAt(i)=='<'||code.charAt(i)=='>'||code.charAt(i)=='['||code.charAt(i)==']'))
					if(i == code.length()-1){
						canvas.repaint();
						done = true;
						break;
					}else{
						i++;
					}
				canvas.interpret();//out of bounds
				canvas.repaint();
			}
			if(i == code.length()-1){
				canvas.repaint();
				done = true;
			}
		}
		backgroundColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
	}

	public void keyReleased(KeyEvent e) {
	}
	public String quickInterpret(String str, String input){
		String output = "";
		byte[] data = new byte[30000];
		int pointer = 0; int inCount = 0;
		char[] chars = str.toCharArray();
		for(int i=0;i<str.length();i++){
			switch(chars[i]){
				case '>':
					if(pointer<30000)
						pointer++;
					break;
				case '<':
					if(pointer>0)
						pointer--;
					break;
				case '+':
					data[pointer]++;
					break;
				case '-':
					data[pointer]--;
					break;
				case '.':
					output+=(char)data[pointer];
					break;
				case ',':
					if(inCount<input.length())
						data[pointer]=(byte)(input.charAt(inCount++));
					else
						data[pointer]=0;
					break;
				case '[':
					if(data[pointer]==0){
						int j=1;
						while(j>0){
							i++;
							char ch = code.charAt(i);
							if(ch=='[') j++;
							if(ch==']') j--;
						}i++;
					}
					break;
				case ']':
					if(data[pointer]!=0){
						int j=1;
						while(j>0){
							i--;
							char ch = code.charAt(i);
							if(ch==']') j++;
							else if(ch=='[') j--;
						}
					}
					break;
			}
			if(pointer>maxPointer)
				maxPointer = pointer;
		}
		return output;
	}
}
class MyScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(Color.BLACK);
        g.fillRect((int)trackBounds.getX(), (int)trackBounds.getY(), (int)trackBounds.getWidth(), (int)trackBounds.getHeight());
        g.setColor(Color.WHITE);
        g.drawRect((int)trackBounds.getX(), (int)trackBounds.getY(), (int)trackBounds.getWidth(), (int)trackBounds.getHeight());
    }

   @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(Color.WHITE);
        g.fillRect((int)thumbBounds.getX(), (int)thumbBounds.getY(), (int)thumbBounds.getWidth(), (int)thumbBounds.getHeight());
    }
   
   @Override
   protected JButton createDecreaseButton(int orientation) {
       JButton button = super.createDecreaseButton(orientation);
       button.setBackground(Color.LIGHT_GRAY);
       return button;
   }

   @Override
   protected JButton createIncreaseButton(int orientation) {
       JButton button = super.createIncreaseButton(orientation);
       button.setBackground(Color.LIGHT_GRAY);
       return button;
   }
   
}

class BrainToJava{
    public static void writeToTextArea(JTextArea textArea, String code){
        int tabCounter=0;       
        textArea.append("import java.util.Scanner;\n");
        textArea.append("public class JavaTranslation{\n");
        tabCounter++;
        textArea.append("\tpublic static void main(String[] args){\n");
        tabCounter++;
        textArea.append("\t\tbyte[] arr = new byte[30000];\n");
        textArea.append("\t\tint ptr=0;\n");
        textArea.append("\t\tScanner sc = new Scanner(System.in);\n");
        
        for(int x=0;x<code.length();x++){
            switch(code.charAt(x)){
                case '>':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("ptr++;\n");
                    break;
                case '<':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("ptr--;\n");
                    break;
                case '+':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("arr[ptr]++;\n");
                    break;
                case '-':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("arr[ptr]--;\n");
                    break;
                case ',':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("arr[ptr] = (byte)(sc.nextLine().charAt(0));\n");
                    break;
                case '.':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("System.out.print((char)(arr[ptr]));\n");
                    break;
                case '[':
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("while(arr[ptr]!=0){\n");
                    tabCounter++;
                    break;
                case ']':
                    tabCounter--;
                    for(int cc=0;cc<tabCounter;cc++)
                        textArea.append("\t");
                    textArea.append("}\n");
                    break;
            }
        }
        
        for(int cc=0;cc<tabCounter;cc++)
            textArea.append("\t");
        textArea.append("System.out.println();\n");
        
        while(tabCounter!=0){
            tabCounter--;
            for(int cc=0;cc<tabCounter;cc++)
                textArea.append("\t");
            textArea.append("}\n");
            
        }
    }
}


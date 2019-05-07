package EventCalendar;


	import java.awt.Component;
	import java.awt.Toolkit;
	import java.awt.datatransfer.Clipboard;
	import java.awt.datatransfer.StringSelection;
	import java.awt.event.ActionEvent;
	import java.awt.event.InputEvent;
	import java.awt.event.KeyEvent;
	import java.util.Vector;

	import javax.swing.AbstractAction;
	import javax.swing.JComponent;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JTable;
	import javax.swing.KeyStroke;
	import javax.swing.table.DefaultTableCellRenderer;
	import javax.swing.table.TableCellRenderer;

	import javax.swing.*;
	import javax.swing.event.*;
	import javax.swing.table.*;
	import java.awt.*;
	import java.awt.List;
	import java.awt.event.*;
	import java.util.*;
	import java.util.ArrayList;
	
	@SuppressWarnings("unused")
	public class EventCalendar {
		
	    static JLabel lblMonth, lblYear; 
	    static JButton btnPrev, btnNext, btnAddEvent, btnDelEvent;
	    static JTable tblCalendar;
	    static JComboBox<String> cmbYear;
	    static JFrame frmMain;
	    static Container pane;
	    static DefaultTableModel mtblCalendar; 
	    static JScrollPane stblCalendar; 
	    static JPanel pnlCalendar ,pnlEvents, pnlAllEvents; 
	    static JTextField txtdata;
	    static JTextArea events,allEvents;
	    static int realYear, realMonth, realDay, currentYear, currentMonth, currentDay;
	    static int selectedDay,selectedMonth,selectedYear;
	    static String newLine = "\n";
	    static String eventValue;
	    static Boolean isCellSelected= false;
	    
	    public static void main (String args[]){
		       
	        //Prepare frame
	        frmMain = new JFrame ("Event Calendar"); //Create frame
	        frmMain.setSize(790, 790); //Set size to 400x400 pixels
	        pane = frmMain.getContentPane(); //Get content pane
	        pane.setLayout(null); //Apply null layout
	        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
	        
	        
	        //Create controls
	        lblMonth = new JLabel ("January");
	        lblYear = new JLabel ("Change year:");
	        cmbYear = new JComboBox<String>();
	        btnPrev = new JButton ("Prev");
	        btnNext = new JButton ("Next");
	        mtblCalendar = new DefaultTableModel(){
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
	        tblCalendar = new JTable(mtblCalendar);
	        stblCalendar = new JScrollPane(tblCalendar);
	        pnlCalendar = new JPanel(null);
	        pnlEvents = new JPanel(null);
	        pnlAllEvents = new JPanel(null);
	        btnAddEvent= new JButton("Add");
	        btnDelEvent= new JButton("Del");
	        txtdata = new JTextField(20);
	        events = new JTextArea();
	        allEvents = new JTextArea("");
	        
	        //make text areas non-editable
	        allEvents.setEditable(false);
	        events.setEditable(false);
	        allEvents.setLineWrap(true);
	        events.setLineWrap(true);
	      
	        //Set border
	        pnlCalendar.setBorder(BorderFactory.createTitledBorder("Calendar"));
	        pnlEvents.setBorder(BorderFactory.createTitledBorder("Events"));
	        pnlAllEvents.setBorder(BorderFactory.createTitledBorder("All Events"));
	        //Register action listeners
	        btnPrev.addActionListener(new btnPrev_Action());
	        btnNext.addActionListener(new btnNext_Action());
	        cmbYear.addActionListener(new cmbYear_Action());
	        btnAddEvent.addActionListener(new btnAddEvent_Action());
	        btnDelEvent.addActionListener(new btnDelEvent_Action());
	        
	        
	        //Add controls to pane
	        pane.add(pnlCalendar);
	        pane.add(pnlEvents);
	        pane.add(pnlAllEvents);
	        pnlCalendar.add(lblMonth);
	        pnlCalendar.add(lblYear);
	        pnlCalendar.add(cmbYear);
	        pnlCalendar.add(btnPrev);
	        pnlCalendar.add(btnNext);
	        pnlCalendar.add(stblCalendar);
	        pnlEvents.add(txtdata);
	        pnlEvents.add(btnAddEvent);
	        pnlEvents.add(btnDelEvent);
	        pnlEvents.add(events);
	        pnlAllEvents.add(allEvents);
	        
	        
	        //Set bounds
	        pnlCalendar.setBounds(50, 10, 320, 335);
	        pnlEvents.setBounds(420,10,320,335);
	        pnlAllEvents.setBounds(50, 350, 690, 345);
	        lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 120, 25);
	        lblYear.setBounds(10, 305, 200, 20);
	        cmbYear.setBounds(225, 305, 90, 20);
	        btnPrev.setBounds(10, 25, 60, 28);
	        btnNext.setBounds(250, 25, 60, 28);
	        stblCalendar.setBounds(10, 50, 300, 250);
	        btnAddEvent.setBounds(220,43,50,25);
	        btnDelEvent.setBounds(263,43,50,25);
	        txtdata.setBounds(10,40,200,30);
	        events.setBounds(10,80,300,245);
	        allEvents.setBounds(10, 20, 670, 315);
	        
	        //Make frame visible
	       
	        frmMain.setResizable(true);
	        frmMain.setVisible(true);
	        
	        
	        //Get real month/year
	        GregorianCalendar cal = new GregorianCalendar(); //Create calendar
	        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
	        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
	        realYear = cal.get(GregorianCalendar.YEAR); //Get year
	        currentMonth = realMonth; //Match month and year
	        currentYear = realYear;
	        
	        //Add headers
	        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
	        for (int i=0; i<7; i++){
	            mtblCalendar.addColumn(headers[i]);
	        }
	        
	        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background
	        
	        //No resize/reorder
	        tblCalendar.getTableHeader().setResizingAllowed(true);
	        tblCalendar.getTableHeader().setReorderingAllowed(true);
	        
	        //Single cell selection
	        tblCalendar.setColumnSelectionAllowed(true);
	        tblCalendar.setRowSelectionAllowed(true);
	        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        
	        //Set row/column count
	        tblCalendar.setRowHeight(38);
	        mtblCalendar.setColumnCount(7);
	        mtblCalendar.setRowCount(6);
	        
	        //Populate table
	        for (int i=realYear-100; i<=realYear+100; i++){
	            cmbYear.addItem(String.valueOf(i));
	        }
	        
	        //Refresh calendar
	       refreshCalendar (realMonth, realYear); //Refresh calendar
	      
	       
	    }//end main
	    
	    
	    
	    public static void refreshCalendar(int month, int year){
	        //Variables
	        String[] months =  {"January", "February", "March", "April", "May",
	        					"June", "July", "August", "September", "October",
	        					"November", "December"};
	        int nod, som; //Number Of Days, Start Of Month
	        
	        //Allow/disallow buttons
	        btnPrev.setEnabled(true);
	        btnNext.setEnabled(true);
	        btnAddEvent.setEnabled(true);
	        if (month == 0 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
	        if (month == 11 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
	        lblMonth.setText(months[month]); //Refresh the month label (at the top)
	        lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
	        cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
	        
	        //Clear table
	        for (int i=0; i<6; i++){
	            for (int j=0; j<7; j++){
	                mtblCalendar.setValueAt(null, i, j);
	                
	            }
	        }
	        
	        //Get first day of month and number of days
	        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
	        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	        som = cal.get(GregorianCalendar.DAY_OF_WEEK);
	        
	        //Draw calendar
	        for (int i=1; i<=nod; i++){
	        	@SuppressWarnings("deprecation")
				int row = new Integer((i+som-2)/7);
	            int column  =  (i+som-2)%7;
	            mtblCalendar.setValueAt(i, row, column);

	        }
	        

	        //Apply renderer
	        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
	    }
	    
	    static class tblCalendarRenderer extends DefaultTableCellRenderer{

			private static final long serialVersionUID = 1L;

			public Component getTableCellRendererComponent 
	        		(JTable table, Object value, boolean selected,
	        		boolean focused, int row, int column){
	            super.getTableCellRendererComponent(table, value, selected, focused, row, column);

	            if (column == 0 || column == 6){ //Week-end
	                setBackground(Color.ORANGE);
	            }
	            else{ //Week
	                setBackground(Color.WHITE);
	            }
	          
	            if (value != null){
	            	//set actual day to gray
	                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth
	                												&& currentYear == realYear){ //Today
	                	setBackground(Color.GRAY);
	                }
	                //set days with events to yellow
	                if(EventManager.findEvent(value,currentMonth+1,currentYear)) {
	                	setBackground(Color.YELLOW);
	                }
	                
	            }
	          //allows user to select date to add event to
	            if(selected && value!=null) {
	            	setBackground(Color.LIGHT_GRAY);
	                events.setText(null);
	            	events.setText("");
	            	selectedMonth=currentMonth+1;
	            	selectedYear=currentYear;
	            	selectedDay= Integer.parseInt(value.toString());
	            	EventManager.displayEvents(selectedDay,selectedMonth,selectedYear);
	            	isCellSelected=true;
	            }
	            setBorder(null);
	            setForeground(Color.BLACK);	         
	            return this;
	        }
	    }
	    
	    
	    
	    
	    //previous button
	    static class btnPrev_Action implements ActionListener{
	        public void actionPerformed (ActionEvent e){
	            if (currentMonth == 0){ //Back one year
	                currentMonth = 11;
	                currentYear -= 1;
	            }
	            else{ //Back one month
	                currentMonth -= 1;
	            }
	            refreshCalendar(currentMonth, currentYear);
	        }
	    }
	    
	    
	  
	    //next button
	    static class btnNext_Action implements ActionListener{
	        public void actionPerformed (ActionEvent e){
	            if (currentMonth == 11){ //Forward one year
	                currentMonth = 0;
	                currentYear += 1;
	            }
	            else{ //Forward one month
	                currentMonth += 1;
	            }
	            refreshCalendar(currentMonth, currentYear);
	        }
	    }
	    //change year
	    static class cmbYear_Action implements ActionListener{
	        public void actionPerformed (ActionEvent e){
	            if (cmbYear.getSelectedItem() != null){
	                String b = cmbYear.getSelectedItem().toString();
	                currentYear = Integer.parseInt(b);
	                refreshCalendar(currentMonth, currentYear);
	            }
	        }
	    }
	  
	    //add event
	    static class btnAddEvent_Action implements ActionListener{
	        public void actionPerformed (ActionEvent e){
	        	if (e.getSource() == btnAddEvent) {
	        	EventManager.createEvent();
	        	refreshCalendar(currentMonth, currentYear);
	        	//resets all events display
		        allEvents.setText(null);
		    	allEvents.setText("");
		    	EventManager.mainEventDisplay();
	        	}
	        }
	    }
	    //delete event
	    static class btnDelEvent_Action implements ActionListener{
	        public void actionPerformed (ActionEvent e){
	        	if (e.getSource() == btnDelEvent) {
	        	EventManager.deleteEvent(selectedDay,selectedMonth,selectedYear);
	        	refreshCalendar(currentMonth, currentYear);
	        	//resets all events display
		        allEvents.setText(null);
		    	allEvents.setText("");
		    	EventManager.mainEventDisplay();
	        	}
	        }
	    }
	    
	    //sort events
		class SortEvent implements Comparator<Event> 
		{ 
			public int compare(Event a , Event b) {
				
				int dayCompare = Integer.valueOf(a.eventDay).compareTo(Integer.valueOf(b.eventDay));
				int monthCompare = Integer.valueOf(a.eventMonth).compareTo(Integer.valueOf(b.eventMonth));
				int yearCompare = Integer.valueOf(a.eventYear).compareTo(Integer.valueOf(b.eventYear));
				
				if(yearCompare ==0) {
					if(monthCompare ==0) {
						return dayCompare;
					}
					else {
						return monthCompare;
					}
				}
				else {
					return yearCompare;
				}
				
			} 
		}
	    
		
	    static public class EventManager{
	    	static ArrayList<Event> list = new ArrayList<Event>();
		    //creates new event
		    public static void createEvent() {
		    	Event singleEvent = new Event();
		    	//checks if a date is selected
		    	if (isCellSelected==false) {
		    		JOptionPane.showMessageDialog(frmMain, "ERROR: No date selected");
		    	}
		    	//checks if anything is in the text box
		    	else if(txtdata.getText().equals("")) {  
		    		JOptionPane.showMessageDialog(frmMain, "ERROR: No event entered to be added");
		    		
		    	}
		    	
		    	else {
		    		
		    	String getValue = txtdata.getText();
		    	eventValue = getValue;
		    	singleEvent.setEventName(eventValue);
		    	singleEvent.setEventDay(selectedDay);
		    	singleEvent.setEventMonth(selectedMonth);
		    	singleEvent.setEventYear(selectedYear);
		    	list.add(singleEvent);
		    	txtdata.setText("");
		    	
		    	}
		    }
		    
		    public static void deleteEvent(int day,int month,int year) {
		    	//checks if a date is selected
		    	if (isCellSelected==false) {
		    		JOptionPane.showMessageDialog(frmMain, "ERROR: No date selected");
		    	}
		    	else {
		    	boolean hasEvent= false;
		    	for(int i =0;i<list.size();i++) {
		    		if(list.get(i).getEventDay() ==day &&
		    			list.get(i).getEventMonth() ==month &&
		    			list.get(i).getEventYear()==year) {
		    			list.remove(i);
		    			hasEvent= true;
		    			break;
		    			}
		    		}
		    	//checks if any events exist on this day
		    	if(hasEvent==false) {
		    		JOptionPane.showMessageDialog(frmMain, "ERROR: No event on this date to be deleted");
		    	}
		    	}
		    	
		    }
		    	
		    //displays events for selected day
		    public static void displayEvents(int day,int month,int year) {
		    	
		    	for(int i =0;i<list.size();i++) {
		    		if(list.get(i).getEventDay() ==day &&
		    			list.get(i).getEventMonth() ==month &&
		    			list.get(i).getEventYear()==year) {
		    			events.append((list.get(i).getEventName()+ newLine));
		    		}		
		    	}
		    	
		    	
		    }
		    //main display of all events
		    public static void mainEventDisplay() {
		    	int[] dayArr = new int[list.size()];
		    	int[] monthArr = new int[list.size()];
		    	int[] yearArr = new int[list.size()];
		    	for(int i =0;i<list.size();i++) {
		    		dayArr[i] = list.get(i).getEventDay();
		    		monthArr[i] = list.get(i).getEventDay();
		    		yearArr[i] = list.get(i).getEventDay();
		    	}
		    	EventCalendar calendar = new EventCalendar();
		    	Collections.sort(list, calendar.new SortEvent());
		        allEvents.append("Your up coming events: "+ newLine);
		    	for(int i =0;i<list.size();i++) {
		    		allEvents.append(("- "+list.get(i).getEventName()+ "     [" 
		    				+ list.get(i).getEventMonth()
		    				+ "/" + list.get(i).getEventDay() 
		    				+ "/" + list.get(i).getEventYear() 
		    				+ "]" +newLine));
		    	}
		    }
		    //checks if event exists on specific day
			public static boolean findEvent(Object value,int month,int year) {
				int day = Integer.parseInt(value.toString());
				boolean hasEvent = false;
				for(int i =0;i<list.size();i++) {
				if(list.get(i).getEventDay()== day &&
						list.get(i).getEventMonth()== month &&
				        list.get(i).getEventYear()== year){
					hasEvent= true;
					}
				}
				
				return hasEvent;
			}
				
		}//end EventManager

	}//end EventCalendar
	



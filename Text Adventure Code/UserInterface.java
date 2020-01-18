/* 
   ********************************OVERVIEW************************************
   This program is part of Dungeon Crawler: A Game Prototype.
   
   -----------------------------BASIC OVERVIEW---------------------------------
   This is the Interface for the game prototype referred to as "DunCraw_1". 
   The Interface is one pop-up window which switches between two main "screens", 
   and modifies those screens' contents.
   The one screen is a "Play" screen. The Play Screen contains all game 
   elements: it displays all game output, enables game input, and has a pause 
   button.
   The other screen is a "Pause" screen. The Puase Screen contains all things 
   the player can do when paused. These features include resuming the game, 
   quiting the window, documenting all game output to a text file, and 
   displaying a help menu.
   
   -----------------------------SCREEN SPECIFICS-------------------------------
   1. Play Screen
   The play screen is where the player experiences the game world. There, they 
   read descriptins of what is going on in the game, and input commands telling 
   their character what to do.
   The play screen is created by putting a certain panel on the window.
   The gameplay panel has text output at the top which can be scrolled through, 
   text input beneath that, and a button leading to the pause menu.
   
   2. Pause Screen
   The pause screen is what the uses sees when they pause the game. It contains 
   three things: a pause menu, an alert area, and an info area. 
   The pause menu is at the top. It has four buttons: one returns to the game, 
   one switches the info are to the quit menu, one switches the info area to the 
   documentation menu, and the last switches the info area to the help window.
   The info area is at the bottom. It holds certain pause-features the player 
   selects. These are the help menu, the documentation menu, and the quit menu.
   The help menu displays text aimed to answer the player's questions about the 
   game. The text in the help menu is read from a predetermined text file toward 
   the top of the program. The documentation menu writes all the previous game 
   data to a text file the player specifies. This results in a text file 
   containing a transcript of all the game output from that run of the game. The 
   quit menu allows the player to close the window. Finally, the alert area is in 
   the middle of the pause screen. It simply displays alerts. 
   Like the Play Screen, the Pause Screen is created by putting a certain pause 
   panel on the window.
   
   ****************************DEVELOPMENT DETAILS********************************
   Filename: UserInterface.java
   Date Began: 12.30.16
   Dates Edited: 4.13.2017
                 6.15.2017
   Author: Matthew Wojtechko
*/
package Interface;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.awt.print.*;
import java.util.concurrent.TimeUnit;

public class UserInterface
{
   private JFrame window;        //hold all components
   private JPanel gamePanel,     //each hold main "play" components
                  inPanel;
   private JTextField inText;    //play input textbox
   private JTextArea outText;    //play output
   
   private DefaultCaret scrollCaret;   // used to auto scroll to bottom of output
   
   private JPanel pausePanel,    //panels for pause menu
                  menuPanel,
                  quitPanel, 
                  docPanel;
   private JTextField pauseOut;  //output text for alerts in pause menu          
   private JComponent pauseCurrBotPanel;  //the panel to be displayed at bottom of pause screen
   private JScrollPane helpPanel;         //help menu panel              
   private JTextArea helpOut;             
                                
   private pauseMenuButton helpButton,    //buttons on top pause menu
                           docButton,
                           quitButton,
                           enterFButton;
                           
   private regButton pauseButton,        //other buttons
                     resButton,
                     reallyQButton;
                     
   private boolean isInputEntered;
   
   private String docName;              //holds the name of file to write game data to                                    

   private JTextField docIn;            //textbox for name of file to write game data to
   
   private String[] currOutputList;                   //a list of the game output being to be displayed to player
   private ArrayList<String> allOutputList;           //a list of all game output (past and previous) - the "game data" that can be written to file                        
   
   private final String HELP_FILENAME = "help.txt";   //name of file containing help text
   private final int NUM_OUT = 3;                     //number of how many pieces of output can be displayed in game
   
   // No-args constructor
   public UserInterface()
   {
      // Sets up window which holds all panels
      window = new JFrame("The Dungeon Crawler");
      window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      window.setMinimumSize(new Dimension(700, 1000));
      window.pack();
      window.setVisible(true);
      
      // Sets the play and pause menu
      setGame();
      setPause();
      refreshWindow();
      displayGame();
      
      // Initializes other variables
      currOutputList = new String[NUM_OUT];
      allOutputList = new ArrayList();
      
      docName = "";
   }
   
   // Sets components for game: input, output, and panels
   private void setGame()
   {
       /*A JPanel, in GridLayout(2, 1) "column", holds a component on top and on bottom.
         The top is a JTextArea that displays game output.
         The bottom part is another JPanel, in a GridLayout(3, 1) "column".
         This bottom JPanel contains a JTextField for game input, a JPanel containing combat info, and a JButton for pasuing.*/
      
      isInputEntered = false;
      
      //outText
      outText = new JTextArea();
      outText.setBackground(Color.WHITE);
      outText.setForeground(Color.BLACK);
      outText.setFont(new Font("Book Antiqua", Font.BOLD, 24));
      outText.setLineWrap(true);
      outText.setWrapStyleWord(true);
      outText.setEditable(false);
      
      scrollCaret = (DefaultCaret)outText.getCaret();
      scrollCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);    // so always scroll to bottom
            
      //bottomPanel
      JPanel bottomPanel = new JPanel();
      bottomPanel.setLayout(new GridLayout(3, 1));
      bottomPanel.setBackground(new Color(160, 214, 250));
      
         //inPanel   
      inText = new JTextField(50);
      inText.setSize(new Dimension(15, 1));
      inText.addKeyListener(new KeyEvent());    // adds KeyListener (overriden way below) to inText
      inText.setForeground(Color.BLUE);
      inText.setBackground(Color.YELLOW);
      inText.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 14));
      inText.setHorizontalAlignment(JTextField.CENTER);
      inPanel = new JPanel();
      inPanel.setLayout(new GridBagLayout());
      inPanel.setBackground(Color.BLACK);
      inPanel.add(inText);
      
         //pausePanel
      pauseButton = new regButton("Pause");
      pauseButton.setColor(Color.lightGray);
      JPanel pausePanel = new JPanel();
      pausePanel.setLayout(new GridBagLayout());
      pausePanel.setBackground(new Color(160, 214, 250));
      pausePanel.add(pauseButton);
      
      bottomPanel.add(inPanel);
      bottomPanel.add(pausePanel);
      
      //Adds both panels to the gamePanel
      gamePanel = new JPanel(new GridLayout(2, 1));
      gamePanel.setBackground(Color.BLACK);
      gamePanel.add(new JScrollPane(outText));  // adds outText JTextArea, WITHIN a panel that scrolls
      gamePanel.add(bottomPanel);
   }
   
   // initializes pausePanel and all components that go on it
   private void setPause()
   {
      //pausePanel
      pausePanel = new JPanel(new GridLayout(3, 1));
      pausePanel.setBackground(Color.BLACK);
      
         //menuPanel
      menuPanel = new JPanel(new GridLayout(2, 1));
      menuPanel.setBackground(Color.BLACK);
      
            //pauseLabelPanel - holds "paused" text
      JLabel pMenuLabel = new JLabel("Game Paused");
      pMenuLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 65));
      pMenuLabel.setForeground(Color.WHITE);
      JPanel pauseLabelPanel = new JPanel(new GridBagLayout());
      pauseLabelPanel.setBackground(Color.BLACK);
      pauseLabelPanel.add(pMenuLabel);
            
            //pMenuBPanel - holds the buttons
      resButton = new regButton("Resume");
      resButton.setColor(Color.YELLOW);
      helpButton = new pauseMenuButton("Help");
      helpButton.setColor(new Color(185, 204, 234));
      docButton = new pauseMenuButton("Document");
      docButton.setColor(Color.WHITE);
      quitButton = new pauseMenuButton("Quit");
      quitButton.setColor(Color.RED); 
      JPanel pMenuBPanel = new JPanel(new GridBagLayout());
      pMenuBPanel.setBackground(Color.BLACK);
      pMenuBPanel.add(resButton);
      pMenuBPanel.add(helpButton);
      pMenuBPanel.add(docButton);
      pMenuBPanel.add(quitButton);
      
      menuPanel.add(pauseLabelPanel);
      menuPanel.add(pMenuBPanel);
      
   
         //pauseOut
      pauseOut = new JTextField(25);
      pauseOut.setBackground(Color.lightGray);   
      pauseOut.setForeground(Color.BLACK);
      pauseOut.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 14));
      pauseOut.setHorizontalAlignment(JTextField.CENTER);
      pauseOut.setEditable(false);
      
         //helpPanel
      JTextArea helpText;
      try
      {
         helpText = new JTextArea((new Scanner(UserInterface.class.getResourceAsStream(HELP_FILENAME))).useDelimiter("\\Z").next());   //reads all text in file into String
      }
      catch (Exception e)
      {
         helpText = new JTextArea("Sorry, the help text could not be found... looks like you're on your own.");
      }
      helpText.setEditable(false);
      helpText.setBackground(Color.BLACK);
      helpText.setForeground(Color.WHITE);
      helpText.setFont(new Font(Font.DIALOG, 1, 16));
      helpText.setLineWrap(true);
      helpText.setWrapStyleWord(true);
      helpPanel = new JScrollPane(helpText);
      
         //docPanel
      docPanel = new JPanel(new GridLayout(3, 1));
      docPanel.setBackground(Color.BLACK);
       
      JTextField docTextTop = new JTextField("Save a transcript of all the text from your playthrough");
      JTextField docTextBot = new JTextField("in a text document by entering a file name below.");
      docTextTop.setForeground(Color.WHITE);
      docTextTop.setBackground(Color.BLACK);
      docTextTop.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 22));
      docTextTop.setHorizontalAlignment(JTextField.CENTER);
      docTextTop.setEditable(false); 
      docTextTop.setBorder(javax.swing.BorderFactory.createEmptyBorder());
      docTextBot.setForeground(Color.WHITE);
      docTextBot.setBackground(Color.BLACK);
      docTextBot.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 22));
      docTextBot.setHorizontalAlignment(JTextField.CENTER);
      docTextBot.setEditable(false);    
      docTextBot.setBorder(javax.swing.BorderFactory.createEmptyBorder());
      JPanel docTextPanel = new JPanel(new GridLayout(2, 1));
      docTextPanel.add(docTextTop);
      docTextPanel.add(docTextBot);  
        
      docIn = new JTextField(40);
      docIn.setForeground(Color.BLUE);
      docIn.setBackground(Color.WHITE);
      docIn.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 14));
      docIn.setHorizontalAlignment(JTextField.CENTER);
      docIn.addActionListener(new EnterHandler());      //listens to text
      JPanel docInPanel = new JPanel(new GridBagLayout());
      docInPanel.setBackground(Color.BLACK);
      docInPanel.add(docIn);
      enterFButton = new pauseMenuButton("Save to File");
      enterFButton.setColor(Color.WHITE);
      enterFButton.setClickedName("Save to File");
      JPanel eFButtonPanel = new JPanel(new GridBagLayout());
      eFButtonPanel.setBackground(Color.BLACK);
      eFButtonPanel.add(enterFButton);
                               
      docPanel.add(docTextPanel);
      docPanel.add(docInPanel);
      docPanel.add(eFButtonPanel);                                     
       
         //quitPanel
      quitPanel = new JPanel(new GridLayout(3, 1));
      quitPanel.setBackground(Color.BLACK);
      JTextField quitLabelTop = new JTextField("Do you really want to quit the game?");
      JTextField quitLabelBot = new JTextField("Your progress will not be saved.");
      quitLabelTop.setForeground(Color.WHITE);
      quitLabelTop.setBackground(Color.BLACK);
      quitLabelTop.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 22));
      quitLabelTop.setHorizontalAlignment(JTextField.CENTER);
      quitLabelTop.setEditable(false); 
      quitLabelTop.setBorder(javax.swing.BorderFactory.createEmptyBorder());
      quitLabelBot.setForeground(Color.WHITE);
      quitLabelBot.setBackground(Color.BLACK);
      quitLabelBot.setFont(new Font(Font.SERIF, Font.CENTER_BASELINE, 22));
      quitLabelBot.setHorizontalAlignment(JTextField.CENTER);
      quitLabelBot.setEditable(false); 
      quitLabelBot.setBorder(javax.swing.BorderFactory.createEmptyBorder());
   
      reallyQButton = new regButton("Really Quit");
      reallyQButton.setColor(Color.RED);
      JPanel rQBPanel = new JPanel(new GridBagLayout());
      rQBPanel.setBackground(Color.BLACK);
      rQBPanel.add(reallyQButton);
      
      quitPanel.add(quitLabelTop);
      quitPanel.add(quitLabelBot);
      quitPanel.add(rQBPanel);    
     
      pausePanel.add(menuPanel);
      pausePanel.add(pauseOut);
      
      //initializes pauseCurrBotPanel - which represents which panel is at the bottom - so no error when the bottom is empty and this is "removed"
      pauseCurrBotPanel = new JPanel();
   }
   
   // sets the window to display the panel with the game
   private void displayGame()
   {
      window.add(gamePanel);
   }
   
   // sets the window to display the panel with the pause screen
   private void displayPause()
   {
      window.add(pausePanel);
   }
   
   /*Adds output. Removes last piece of current output from list and shifts the earlier ones down. Adds String sent to parameter
     to the be of the current output list and to the all output list. Updates the output display - each piece of output is 
     shown on the text area, each separated by a newline.*/
   public void addOutput(String newOut)
   {
      //Adds new output to list of all output
      allOutputList.add(newOut);   
      
      //Updates list of current output with new output
      String nextOut = currOutputList[0];
      currOutputList[0] = newOut;
      for (int i=1; i < currOutputList.length; i++)
      {
         String tempOut = currOutputList[i];
         currOutputList[i] = nextOut;
         nextOut = tempOut;
      }
      
      //Displays updated list of current output
      String currOutputString = "";
      for (int i=currOutputList.length-1; i>=0; i--)
      {
         if (currOutputList[i] != null)
         {
            if (i < currOutputList.length-1 && i >= 0 && !currOutputString.equals(""))
               currOutputString += "\n\n  **        **        **        **        **        **        **        **        **      **  \n\n";
            currOutputString += currOutputList[i];
            
         }
         
      }
      
      outText.setText(currOutputString);
      
   }
   
   // Returns text in the input box on main game screen.
   public String getInputString()
   {
      return inText.getText();
   }
   
   // Returns text words from input box in String array on main game screen.
   public String[] getInputArray()
   {
      return inText.getText().split("\\s+");
   }
   
   
   // Return whether text has been entered
   public boolean isInputEntered()
   {
      return isInputEntered;
   }

   // Set input taken - adds the given error text to input bar and selects it, sets input not entered
   public void setInputTaken(String e)
   {
      inText.setText(e);
      inText.selectAll();
      isInputEntered = false;
   }
   
   // Add Text to Input Bar
   public void setInput(String s)
   {
      inText.setText(s);
   }
   
   /*Writes transcript of game play session to a player-specified file. Displays alerts if player attempts to
     write to an existing file, if the file name is invalid, if the write fails, and if the write succeeded.*/
   public void writeFile()
   {
      docName = docIn.getText();
      if (docName.equals(""))
      {
         pauseOut.setText("First enter a filename in the text box to save a transcript to a file with that name.");
      }
      else if((new File(docName)).exists() && enterFButton.isUnclicked()) //if file already exists
      {
         pauseOut.setText("WARNING: A file with the name \"" + docName + "\" alread exists. Clicking the save button again will override it.");
         enterFButton.click();
      }
      else if (allCharFileValid(docName))
      {
         //tries to write file
         try
         {
            PrintWriter writer = new PrintWriter(docName);
            writer.write("The Dungeon Crawler -- Game Transcript -- " + docName + "\r\n\r\n");
            for (String output : allOutputList)
               writer.println(output); 
            writer.close();      
            pauseOut.setText("Documentaion of the game was written to the file \"" + docName + "\".");
         } 
         catch (IOException e) 
         {
            pauseOut.setText("The file could not be written. The problem could be with the file name, or with something else.");
         }
         enterFButton.unclick();
         docIn.setText("");
      }
      else
         pauseOut.setText("Files must not begin with a space and must not contain any of the following: \\, /, ?, %, *, \", |, :, <, >");
         
       
   }
   
   // Helper method 
   private void refreshWindow()
   {
      window.getContentPane().revalidate();
      window.getContentPane().repaint();
   }
   
   // Handles what happens when a button is clicked on the menu on the pause screen
   private void menuButtonSwitched(pauseMenuButton button, boolean unclicked)
   {
      //first, removes whatever is at the bottom
      pausePanel.remove(pauseCurrBotPanel);
      if (unclicked)
      {
         //set pauseCurrBotPanel to what it should be from button
         if (button == helpButton)
         {
            pauseCurrBotPanel = helpPanel;
            if (docButton.isClicked())
               docButton.unclick();
            if (quitButton.isClicked())
               quitButton.unclick();
         }   
         else if (button == quitButton)
         {
            pauseCurrBotPanel = quitPanel;
            if (docButton.isClicked())
               docButton.unclick();
            if (helpButton.isClicked())
               helpButton.unclick();
         } 
         else if (button == docButton)
         {
            pauseCurrBotPanel = docPanel;
            if (helpButton.isClicked())
               helpButton.unclick();
            if (quitButton.isClicked())
               quitButton.unclick();
         } 
         else  //otherwise, wrong button passed, get out
         {
            JOptionPane.showMessageDialog(null, "There is an error in the code!\nmenuButtonSwitched wrong arguments.");
            System.exit(0);
         }
         pausePanel.add(pauseCurrBotPanel);   
         refreshWindow();
         button.click();
      }  
      else
      {
         //removes the appropriate JPanel
         if (button == helpButton)
            pausePanel.remove(helpPanel); 
         else if (button == quitButton)
            pausePanel.remove(quitPanel);
         else if (button == docButton)
            pausePanel.remove(docPanel);
         else  //otherwise, wrong button passed, get out
         {
            JOptionPane.showMessageDialog(null, "There is an error in the code!\nmenuButtonSwitched wrong arguments.");
            System.exit(0);
         }
         refreshWindow();
         pauseCurrBotPanel = new JPanel();
         button.unclick();    
      }
   }
   
   // LISTERNERS
   
   // Listens to event of button pressed, responds
   private class ButtonHandler implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if (e.getSource() == pauseButton)   //if pause button pressed, display pause screen
         {
            window.getContentPane().removeAll();
            refreshWindow();
            pauseOut.setText(""); //clear pauseOut
            displayPause();  
            pausePanel.remove(pauseCurrBotPanel);
         }
         else if (e.getSource() == resButton)   //if resume button pressed, display game screen
         {
            window.getContentPane().removeAll();
            refreshWindow();
            displayGame();
            if (quitButton.isClicked())
               quitButton.unclick();
            if (docButton.isClicked())
               docButton.unclick();
            if (helpButton.isClicked())  
               helpButton.unclick();       
         }
         else if (e.getSource() == helpButton)  //if help button pressed, display or hide help
         {
            if (pauseOut.getText().length() > 0)
               pauseOut.setText("");
            if (helpButton.isUnclicked())
               menuButtonSwitched(helpButton, true);
            else
               menuButtonSwitched(helpButton, false);
         }
         else if (e.getSource() == quitButton)
         {
            if (pauseOut.getText().length() > 0)
               pauseOut.setText("");
            if (quitButton.isUnclicked())
               menuButtonSwitched(quitButton, true);
            else
               menuButtonSwitched(quitButton, false);   
         }
         else if (e.getSource() == docButton)
         {
            if (pauseOut.getText().length() > 0)
               pauseOut.setText("");
            enterFButton.unclick();
            if (docButton.isUnclicked())
               menuButtonSwitched(docButton, true);
            else
               menuButtonSwitched(docButton, false);   
         }
         else if (e.getSource() == reallyQButton)
         {
            System.exit(1);
            //Anything else?
         }
         else //if (e.getSource() == enterFButton)
         {
            writeFile();
         }   
      
            
      }
   }
   
   // Key Event class responds to keyboard keys
   private class KeyEvent implements KeyListener
   {
      public void keyTyped(java.awt.event.KeyEvent e)
      {
         if (e.getKeyChar() == 10)      // if typed ENTER (10 in Unicode), set isInputEntered to TRUE
            if (((JTextField)e.getSource()).getText().hashCode() != 0)
               isInputEntered = true;
      }
      
      public void keyReleased(java.awt.event.KeyEvent e) {}
      
      public void keyPressed(java.awt.event.KeyEvent e) {}
   }
   
   //Handles ENTER pressed in JTextField
   private class EnterHandler implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if (e.getSource() == docIn)
         {
            enterFButton.doClick();
            if (enterFButton.isClicked() && !docIn.getText().equals(docName))
               enterFButton.click();
         }
      }
   }

   
   //returns false if string contains forbidden character or is empty. Returns true otherwise, even if only space
   public boolean allCharFileValid(String fName)
   {
      if (fName.contains("/") || fName.contains("/") || fName.contains("?") ||
          fName.contains("%") || fName.contains("*") || fName.contains(":") ||
          fName.contains("|") || fName.contains("\"") || fName.contains("<") ||
          fName.contains(">") || fName.charAt(0) == ' ')
         return false;
      else if (fName.equals(""))
         return false;
      return true;   
   }
   
   //Child of JButton -- just like JButton with the following additions: adds listener, two String "names", methods for them, and new method that changes the color of button
   private class pauseMenuButton extends JButton
   {
      private String unclickedName, clickedName;
      boolean unclicked;
      
      public pauseMenuButton(String name)
      {
         super(name);
         unclickedName = name;
         clickedName = ". . .";
         super.addActionListener(new ButtonHandler());
         unclicked = true;
      }
      public void setColor(Color c)
      {
         super.setBackground(c);
      }
      public boolean isUnclicked()
      {
         return unclicked;
      }
      public boolean isClicked()
      {
         return !unclicked;
      }  
      public void unclick()
      {
         unclicked = true;
         super.setText(unclickedName);
      }
      public void click()
      {
         unclicked = false;
         super.setText(clickedName);
      }
      public void setClickedName(String n)
      {
         clickedName = n;
      }
   }
   //child of JButton -- only has different terminoligy to be used like pausemenuButton and auto adds listner
   private class regButton extends JButton
   {
      public regButton(String name)
      {
         super(name);
         super.addActionListener(new ButtonHandler());
      }
      public void setColor(Color c)
      {
         super.setBackground(c);
      }
   }
   
   public static void main(String[] args)
   {
      UserInterface Interface = new UserInterface();
      try 
      {
         TimeUnit.SECONDS.sleep(1);
         Interface.addOutput("First");
         TimeUnit.SECONDS.sleep(1);
         Interface.addOutput("Second");
         TimeUnit.SECONDS.sleep(1);
         Interface.addOutput("Third");
         TimeUnit.SECONDS.sleep(1);
         Interface.addOutput("Fourth");
         
         while (true)
         {
            System.out.print(Interface.getInputString());
            TimeUnit.SECONDS.sleep(1);
         }
      }
      catch (InterruptedException e) {}
   }
}
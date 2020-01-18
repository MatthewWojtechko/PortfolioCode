/*
   Room.java -- class to create "rooms" for the game world.
   
   In the game, the player explores a labyrinth, by going room from room. This is the blueprint that 
   "creates" these rooms. It outlines: the room's dimensions, which other rooms it is commnected to, 
   a list of what Things are in the room, a list of where these Things are located, traits the 
   room has, and the "reveal" that the player expereinces when first entering the room. It of course 
   also contains functions that maintain and return this data. 
   
   Instances of this room class will be used to represent rooms in the game world. Multiple rooms will 
   kept in a list, representing one labyrinth or map for the player to explore.
   
   Began:  6.4.17
   Edited: 11.24.17
           12.16.17
   @Author Matthew Wojtechko 
*/

package Game.Spatial;

import Game.Physical.Things.*;
import java.util.ArrayList;
import java.util.Random;

public class Room
{
   public int               xDim, yDim,
                             ID,
                             northRoom, eastRoom, southRoom, westRoom,  // -1 if no adjacent room in that direction
                             numThings;
                             
   public ArrayList<Thing>  Things;
   
   public ArrayList<int[]>  ThingsLoc;
   
   public String            reveal, 
                            name;   
      
   private ArrayList<String> traits;
   
   public boolean            isVisited = false; // whether the player has been in here
   
   // No-Args Constructor
   public Room()
   {
      xDim = 3;
      yDim = 3;
      Things = new ArrayList<Thing>();
      ThingsLoc = new ArrayList<int[]>();
      numThings = 0;
      reveal = "It's a room.";
      traits = new ArrayList<String>();
      ID = 0;
      northRoom = -1;
      eastRoom = -1;
      southRoom = -1;
      westRoom = -1;
   }
      
   // Args Constructors
   public Room(int id, int x, int y, String r, int n, int e, int s, int w)
   {
      ID = id;
      Things = new ArrayList<Thing>();
      ThingsLoc = new ArrayList<int[]>();
      numThings = 0;
      xDim = x;
      yDim = y;
      reveal = r;
      traits = new ArrayList<String>();
      northRoom = n;
      eastRoom = e;
      southRoom = s;
      westRoom = w;
      setName(id);
   }
   
   // Mutators
   public void setID(int id)
   {
      ID = id;
   }
   public void add(Thing t, int x, int y)
   {
      Things.add(t);
      ThingsLoc.add(new int[]{x, y});
   }
   public void setThings(ArrayList<Thing> t, ArrayList<int[]> l)
   {
      Things = t;
      ThingsLoc = l;
   }
   public void setAdjacent(int n, int e, int s, int w)
   {
      northRoom = n;
      eastRoom = e;
      southRoom = s;
      westRoom = w;
   }
   public void setTraits(ArrayList<String> t)
   {
      traits = t;
   }
   public void addTrait(String t)
   {
      traits.add(t);
   }
   public void setName(int num)
   {
      name = "ROOM " + intToRoman(num);
   }

   // For adding walls in either the 'N', 'S', 'E', or 'W'
   public void add(Wall w, char dir)
   {
      Things.add(w);
      if ((dir == 'n') || (dir == 'N'))
      {
         ThingsLoc.add(new int[] {-1, yDim});
         w.setName("NORTH WALL");
      }
      else if ((dir == 'e') || (dir == 'E'))
      {
         ThingsLoc.add(new int[] {xDim, -1});
         w.setName("EAST WALL");
      }
      else if ((dir == 's') || (dir == 'S'))
      {
         ThingsLoc.add(new int[] {-1, 1});
         w.setName("SOUTH WALL");
      }
      else // west
      {
         ThingsLoc.add(new int[] {1, -1});
         w.setName("WEST WALL");
      }
   }
   
   // For adding doors in either the 'N', 'S', 'E', or 'W'
   public void add(Door d, char dir)
   {
      Things.add(d);
      if ((dir == 'n') || (dir == 'N'))
         ThingsLoc.add(new int[] {(int)(.5+xDim/2.0), yDim});
      else if ((dir == 'e') || (dir == 'E'))
         ThingsLoc.add(new int[] {xDim, (int)(.5+yDim/2.0)});
      else if ((dir == 's') || (dir == 'S'))
         ThingsLoc.add(new int[] {(int)(.5+xDim/2.0), 1});
      else  // west
         ThingsLoc.add(new int[] {1, (int)(.5+yDim/2.0)});
   }
   
   // Accessors
   public int getXDim()
   {
      return xDim;
   }
   public int getYDim()
   {
      return yDim;
   }
   public ArrayList<Thing> getThingsAt(int x, int y)
   {
      ArrayList<Thing> ThingsAtLoc = new ArrayList<Thing>();
      for(int i=0; i<Things.size(); i++)
      {
         if (ThingsLoc.get(i)[0] == x && ThingsLoc.get(i)[1] == y)
            ThingsAtLoc.add(Things.get(1));
      }
      return ThingsAtLoc;
   }
   public ArrayList<String> getNamesAt(int x, int y)
   {
      ArrayList<Thing> ThingsList= getThingsAt(x, y);
      ArrayList<String> namesList = new ArrayList<String>();
      for (Thing X : ThingsList)
         namesList.add(X.getName());
      return namesList;        
   }
   
   public String getReveal()
   {
      return reveal;
   }
   
   public Thing getByIndex(int i)
   {
      return Things.get(i);
   }
   public int getNorth()
   {
      return northRoom;
   }
   public int getEast()
   {
      return eastRoom;
   }
   public int getSouth()
   {
      return southRoom;
   }
   public int getWest()
   {
      return westRoom;
   }
   public int getID()
   {
      return ID;
   }
 

   public Thing getThingByID(String id)
   {
      for (Thing T : Things)
      {
         if (T.getID().equalsIgnoreCase(id))
            return T;
      }
      return null;
   }
   public ArrayList<Thing> getThings()
   {
      return Things;
   }
   public int[] getThingLoc(Thing t)
   {
      for (int i=0; i<Things.size(); i++)
      {
         if (t.getID().equalsIgnoreCase(Things.get(i).getID()))
            return ThingsLoc.get(i);
      }
      return new int[] {-1, -1};
   }
   
   
   // Remove Thing from list of Things, as well as from list of ThingsLoc
   public void removeThing(Thing t)
   {
      for (int i=0; i < Things.size(); i++)
      {
         if (Things.get(i) == t)
         {
            Things.remove(i);
            ThingsLoc.remove(i);
         }
      }
   }
   
   // Return true if both Things have same location. Returns false otherwise (or if any of them has a coord. < 0)
   public boolean sameLocation(Thing Thing1, Thing Thing2)
   {
      int[] thing1Loc, thing2Loc;
      thing1Loc = getThingLoc(Thing1);
      thing2Loc = getThingLoc(Thing2);
      if (thing1Loc[0] < 0 || thing2Loc[0] < 0 ||
          thing1Loc[1] < 0 || thing2Loc[1] < 0)
          return false;
      if (thing1Loc[0] == thing2Loc[0] && thing1Loc[1] == thing2Loc[1])
         return true;
      return false;
   }
   
   // Returns whether a given Thing is at the given coordinates in this room
   public boolean isThingHere(Thing t, int x, int y)
   {
      int[] xy = getThingLoc(t);
      if (xy[0] == x && xy[1] == y)
         return true;
      return false;
   }
   
   // Given an int, returns Roman Numberal using X, V, and I (no other symbols used for 50, 100, etc...)
   public static String intToRoman(int num)
   {
      if (num > 0)
      {
         int repeat, remainder;  // Temp values for how many repeats for symbol and set up to determine for next symbol
         String romanNum = "";
         // Get the number of X's and set up for the V's
         repeat = num / 10;
         remainder = num % 10;
         for (int i = 0; i < repeat; i++)
            romanNum += "X";   
         // Get the number of V's and get number of I's     
         repeat = remainder / 5;
         remainder = remainder % 5;
         for (int i = 0; i < repeat; i++)
            romanNum = "V" + romanNum;
         for (int i = 0; i < remainder; i++)
            romanNum = "I" + romanNum;
         return romanNum;
      }
      else // return num if it is 0 or smaller
         return Integer.toString(num);
   }
   
   
   /////////////////////////////////////// SPECIAL METHODS /////////////////////////////////////////////////////
	
	// Returns a String rerpresentation of the room for debugging purposes.
   public String toString()
   {
      String info = "Room Number: " + ID + "\nTraits: ";
      for (String X : traits)
         info += X;
      info += "\nSize: " + xDim + ", " + yDim + "\nReveal: " + reveal + "\n" + "\n\n** Contents **\n\n";              
      for (int i=0; i<Things.size(); i++)
         info += Things.get(i).getName().toUpperCase() + " (" + ThingsLoc.get(i)[0] + ", " + ThingsLoc.get(i)[1] + ")\n" + Things.get(i).toString() + "\n\n";
      return info;       
   }
   
   // Returns the index value for a given Thing object in the room.
   // This can be used by another method to find data about the Thing quickly.
   // If the given Thing is not in the room, returns -1.
   public int getThingIndex(Thing T)
   {
      for (int i = 0; i < Things.size(); i++)
      {
         if (Things.get(i) == T)
            return i;
      }
      return -1;
   }
   
   // Returns whether Room contains a Thing, given ID
   public boolean contains(String thingID)
   {
      for (Thing X : Things)
      {
         if (X.getID() == thingID)
            return true;
      }
      return false;
   }
   public boolean contains(Thing t)
   {
	   for (Thing X : Things)
	   {
		   if (X == t)
		      return true;
	   }
	   return false;
   }
   
   // Returns an ArrayList of the Things at the North -- WORK IN PROGRESS
   public ArrayList<Thing> getThingsAtNorth()
   {
      ArrayList<Thing> northStuff = new ArrayList<Thing>();
      Thing tempThing;
      for (int i=0; i<ThingsLoc.size(); i++)
      {
         tempThing = Things.get(i);
         if (ThingsLoc.get(i)[1] >= yDim*9/11  && !tempThing.getName().toLowerCase().contains("wall"))
            northStuff.add(tempThing);
      }
      return northStuff;
   }

   // Returns an ArrayList of the Things at the South -- WORK IN PROGRESS
   public ArrayList<Thing> getThingsAtSouth()
   {
      ArrayList<Thing> southStuff = new ArrayList<Thing>();
      Thing tempThing;
      for (int i=0; i<ThingsLoc.size(); i++)
      {
         tempThing = Things.get(i);
         if (ThingsLoc.get(i)[1] <= yDim*3/11  && !tempThing.getName().toLowerCase().contains("wall"))
            southStuff.add(Things.get(i));
      }
      return southStuff;
   }
   
   // Returns an ArrayList of the Things at the West -- WORK IN PROGRESS
   public ArrayList<Thing> getThingsAtWest()
   {
      ArrayList<Thing> westStuff = new ArrayList<Thing>();
      Thing tempThing;
      for (int i=0; i<ThingsLoc.size(); i++)
      {
         tempThing = Things.get(i);
         int[] xy = ThingsLoc.get(i);
         if (xy[0] < xDim*4/11 && xy[1] > yDim*3/11 && xy[1] < yDim*9/11  && !tempThing.getName().toLowerCase().contains("wall"))
            westStuff.add(Things.get(i));
      }
      return westStuff;
   }
   
   // Returns an ArrayList of the Things at the East -- WORK IN PROGRESS
   public ArrayList<Thing> getThingsAtEast()
   {
      ArrayList<Thing> eastStuff = new ArrayList<Thing>();
      Thing tempThing;
      for (int i=0; i<ThingsLoc.size(); i++)
      {
         tempThing = Things.get(i);
         int xy[] = ThingsLoc.get(i);
         if (xy[0] > xDim*8/11 && xy[1] > yDim*3/11 && xy[1] < yDim*9/11  && !tempThing.getName().toLowerCase().contains("wall"))
            eastStuff.add(Things.get(i));
      }
      return eastStuff;
   }
   
   // Returns an ArrayList of the Things at the Center -- WORK IN PROGRESS
   public ArrayList<Thing> getThingsAtCenter()
   {
      ArrayList<Thing> centerStuff = new ArrayList<Thing>();
      for (int i=0; i<ThingsLoc.size(); i++)
      {
         int[] xy = ThingsLoc.get(i);
         if (xy[0] >= xDim*4/11 && xy[0] <= xDim*8/11 && xy[1] <= yDim*8/11 && xy[1] >= yDim*4/11)
            centerStuff.add(Things.get(i));
      }
      return centerStuff;
   }
   
   // sets walls to have proper names for this room (north wall, etc...)
   public void setDoorWallNames()
   {
      int[] loc;
      String objType;
      for (Thing X : Things)
      {
         if (X instanceof Wall || X instanceof Door)
         {
            if (X instanceof Wall)
               objType = "WALL";
            else 
               objType = "DOOR";
            loc = getThingLoc(X);
            if (loc[0] == 1)
               X.setName("WEST " + objType);
            else if (loc[0] == xDim)
               X.setName("EAST " + objType);
            else if (loc[1] == 1)
               X.setName("SOUTH " + objType);
            else
               X.setName("NORTH " + objType);
         }
         
      }
   }

}
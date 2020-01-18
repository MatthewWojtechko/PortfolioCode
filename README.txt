This repository consists of scripts from various game projects I have 
worked on over the years. 

Below are explanations of each file folder and their contents.


________________________________EFFECTS________________________________
Code to add aesthetic value to the game through audio-visual effects.

	- BlockSpawner.cs
		Spawns gameobjects in a fun pattern for the blocks 
		at the beginning of a brick breaker level.

	- LavaFlow.cs
		Produces a flowy stretch effect for the lava in 
		Vamoose.

	- LightColorChange.cs
		Loops through different colors for a constant color 
		changing effect.

	- MusicManager.cs
		Plays music in a random order from "playlists" 
		designated to each level.

___________________________PLAYER MOVEMENT_____________________________
Code relating to how a player character interacts.

	- CollisionDetection.cs
		For a player that only moves in four directions, 
		determines where the the player makes first contact 
		with an obstacle.

	- MoveWithPlatform.cs
		For a 2D platformer, makes the player character follow 
		the movement of a moving platform he/she is riding.

	- PlayerInput.cs
		Determines the player input for a mobile game in which a 
		player swipes in one of four directions to control his/her 
		character.

	- PlayerWallCollision.cs
		Perfect collision detection for a 2D character that moves 
		in any direction at any speed. Checks the planned
		movement, then returns what position the character should 
		really end up at.

	- TurretMovement.cs
		The player control for Asteroid Approach. The turret moves 
		two dimensionally while automatically rotating.

________________________________OBJECTS__________________________________
Scripts that pertain to NPCs, enemies, hazards, and any other entities in 
the game that are not the player.

	- AsteroidSpawner.cs
		Sets up the shape and position of a newly spawned 
		asteroid for a simple Asteroids game.

	- BullslugAI.cs
		Controls the movement of a Bullslug enemy in Vamoose by 
		determining which of 7 states to be in, 
		and carrying out the appropriate action for that state. 
		Also controls some particle effects and passes 
		information to a sound script and the animator.

	- BunnyMove.cs
		Move between waypoints, waiting at them for brief periods.

	- ButtonPress.cs
		Determines whether a button is being stepped on in a 2D 
		platformer.

	- LavaMovement.cs
		Controls the movement for the lava in Vamoose. Several 
		trip wires are placed around the level, and when the player 
		crosses them, the lava rises to a certain point, adjusting 
		the speed. This lets the lava "keep up" with the player and 
		for the speed to be conducive to balanced gameplay.

__________________________TEXT ADVENTURE CODE_______________________________
Various files from a text adventure protoype game built entirely in Java.

	- Map1.java
		Instantiates a configuration of rooms for the player to 
		explore.

	- Parser.java
		Detemrines whether a given character string abides by the 
		grammar rules for player input.

	- Phraser.java
		When given a list of objects, returns a string that is a 
		grammatical phrase listing them out.

	- Player.java
		Data and functions pertaining to the player character.

	- PositionTerms.java
		When given the size of the room and coordinates of an 
		item, returns a grammatical, casual-sounding phrase 
		that describes the item's location.

	- Room.java
		The room class, used to instantiate all the chambers of 
		the dungeon. Each room has a size, contents, and 
		functions.

	- ThingReactions.java
		When given an item and an action, returns a grammatical 
		phrase that describes what happens when the player tries 
		a certain action on an item.

	- UserInterface.java
		The user interface for the game. The main screen outputs 
		the game text and provides a bar for user input. The 
		secondary screen houses the help text, the file writing 
		functionality, and quit and resume options.	

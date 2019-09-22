/*
	Plays music in a random order from a "playlist" designated to each level.
	Note, LevelOneSongs, LevelTwoSongs, and LevelThreeSongs must be inialized 
	via the Unity Editor before start.
*/
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MusicManager : MonoBehaviour
{
    public AudioSource[] LevelOneSongs;
    public AudioSource[] LevelTwoSongs;
    public AudioSource[] LevelThreeSongs;
    public AudioSource normalTransSong;
    public AudioSource finalTransSong;

    public AudioSource[] SongQueue;
    public int currentSongIndex = -1;
    private int currentLevel = -1;
    private AudioSource lastSong;
    private bool musicOn;

    void Awake()
    {
    	// Adds listeners for messages on when to start playing music.
        Messenger.AddListener(GameEvent.START_LEVEL_ONE, levelOnePlay);
        Messenger.AddListener(GameEvent.START_LEVEL_TWO, levelTwoPlay);
        Messenger.AddListener(GameEvent.START_LEVEL_THREE, levelThreePlay);
        Messenger.AddListener(GameEvent.TRANSITION_TO_TWO, twoTransition);
        Messenger.AddListener(GameEvent.TRANSITION_TO_THREE, threeTransition);
        Messenger.AddListener(GameEvent.FINAL_TRANSITION, finalTransition);
        Messenger.AddListener(GameEvent.END_SCENE, endscene);
    }

    void OnDestroy()
    {
    	// Listeners must be removed when this object is destroyed to prevent errors.
        Messenger.RemoveListener(GameEvent.START_LEVEL_ONE, levelOnePlay);
        Messenger.RemoveListener(GameEvent.START_LEVEL_TWO, levelTwoPlay);
        Messenger.RemoveListener(GameEvent.START_LEVEL_THREE, levelThreePlay);
        Messenger.RemoveListener(GameEvent.TRANSITION_TO_TWO, twoTransition);
        Messenger.RemoveListener(GameEvent.TRANSITION_TO_THREE, threeTransition);
        Messenger.RemoveListener(GameEvent.FINAL_TRANSITION, finalTransition);
        Messenger.RemoveListener(GameEvent.END_SCENE, endscene);
    }

    // Update is called once per frame
    void Update()
    {
        if (musicOn)
        {
            if (currentLevel > 0)   // levels less than 1 indicate it's not a level
            {
                if (currentSongIndex < SongQueue.Length - 1)   // if not at last song yet
                {
                    if (!SongQueue[currentSongIndex].isPlaying)    // if the song isn't playing, play the next one.
                    {
                        lastSong = SongQueue[++currentSongIndex];
                        lastSong.Play();
                    }
                }
                else if (currentSongIndex >= SongQueue.Length - 1)  // if at the last song 
                {
                    if (!SongQueue[currentSongIndex].isPlaying)    // if the last song isn't playing, re-queue
                    {
                        startMusicForLevel(currentLevel, 0);
                    }
                }
            }
        }
    }

    // Sets the SongQueue to a random ordering of all the songs in the given array of AudioSources.
    private void SetSongQueue(AudioSource[] SongOptions)
    {
        int chosenIndex;
        SongQueue = new AudioSource[SongOptions.Length];
		
        // Fills array with all indices for the SongOptions array.
        List<int> songIndices = new List<int>();
        for (int i = 0; i < SongOptions.Length; i++)
        {
            songIndices.Add(i);
        }

        // Fills the queue. Goes through each of its elements and assigns a random song.
        for (int i = 0; i < SongQueue.Length; i++)
        {
            chosenIndex = pickRandNum(songIndices);

            if (i == 0 && SongOptions.Length > 1) // Don't let the first index (which gives the first song) be the same as the last one (assuming there's multiple songs to choose from)!
            {
                while (SongOptions[chosenIndex] == lastSong)
                {
                    chosenIndex = pickRandNum(songIndices);
                }
            }

            SongQueue[i] = SongOptions[chosenIndex];
            songIndices.Remove(chosenIndex);
        }
    }

    // Returns a random element from the given integer List.
    private int pickRandNum(List<int> Numbers)
    {
        return Numbers[Random.Range(0, Numbers.Count)];
    }

    // The functions are called via the Messenger listeners.
    private void levelOnePlay()
    {
        startMusicForLevel(1, 0);	// play level 1 music
        musicOn = true;
    }
    private void levelTwoPlay()
    {
        startMusicForLevel(2, 0);	// play level 2 music
        musicOn = true;
    }
    private void levelThreePlay()
    {
        startMusicForLevel(3, 0);	// play level 3 music
        musicOn = true;
    }
    public void twoTransition()
    {
        StartCoroutine(AudioFadeOut.FadeOut(lastSong, 0.1f));
        startMusicForLevel(2, 1);	// play music for level 2, starting with normal transition song
        musicOn = true;
    }
    public void threeTransition()
    {
        StartCoroutine(AudioFadeOut.FadeOut(lastSong, 0.1f));
        normalTransSong.Play();		// special case, play the normal transition song...
        musicOn = false;
    }
    public void finalTransition()
    {
        StartCoroutine(AudioFadeOut.FadeOut(lastSong, 0.1f));
        startMusicForLevel(3, 2);	// ...but then play music for level 3, starting with the surprise transition song
        musicOn = true;
    }



    // Starts playing songs for a given level. Fills the SongQueue with random order of the songs from the current level.
    // transMusic represents whether the normal transition music should be played, whether the special final transition 
    // music should be played, or whether no transition music should play. 
    // Here is a key for transMusic: 0 = none, 1 = normal, 2 = final
    // If one of the transition music pieces should be played (1 or 2), puts that music piece at the beginning of the SongQueue.
    public void startMusicForLevel(int level, int transMusic)   
    {
        currentLevel = level;
        if (level == 1)
            SetSongQueue(LevelOneSongs);
        else if (level == 2)
            SetSongQueue(LevelTwoSongs);
        else
            SetSongQueue(LevelThreeSongs);

        currentSongIndex = 0;

        if (transMusic > 0) // if a transition song should be played first, make that first in the queue
        {
            AudioSource[] newSongQueue = new AudioSource[SongQueue.Length];
            AudioSource tempTransSong;
            if (transMusic == 1)
                newSongQueue[0] = normalTransSong;
            else
                newSongQueue[0] = finalTransSong;
            for (int i = 1; i < newSongQueue.Length; i++)
            {
                newSongQueue[i] = SongQueue[i - 1];
            }
            SongQueue = newSongQueue;
        }

        SongQueue[0].Play();
        lastSong = SongQueue[0];
    }

    void endscene()
    {
        musicOn = false;
        StartCoroutine(AudioFadeOut.FadeOut(lastSong, 0.1f));
    }
}

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// This script determines which of four directional inputs are currently active. Each is either active or inactive (boolean).
// A direction is active while the corresponding key is pressed and the opposite key is not pressed. If a key is pressed while 
// its opposite is already pressed, then said key will not count as input (that direction will not be flagged as active).
// For example, if down is currently pressed, and the user enters up, then down will stay active and up will remain inactive.
// To be used by a player movement script.
public class PlayerInput : MonoBehaviour
{
    [Header("Which keys for up, left, right, down input.")]
    public string upKey = "w";
    public string leftKey = "a";
    public string rightKey = "d";
    public string downKey = "s";
    public string grappleKey = "return";
    public string pullKey = "p";

    [Tooltip("Is input currently accepted.")]
    public bool isReady = true;

    [Header("Which directions are currently input.")]
    public bool isUpActive = false;
    public bool isLeftActive = false;
    public bool isRightActive = false;
    public bool isDownActive = false;
    public bool isGrappleActive = false;
    public bool isPullActive = false;

    //void Update()
    //{
    //    // Debugging
    //    setInputs();

    //    Debug.Log("Up: " + isUpActive);
    //    Debug.Log("Down: " + isDownActive);
    //    Debug.Log("Left: " + isLeftActive);
    //    Debug.Log("Right: " + isRightActive);
    //}

    // Sets flags for each inputs - whether each direction input is currently active.
    public void setInputs()
    {
        if (isReady)
        {
            if (pressedWithoutOpposite(upKey, isDownActive))
                isUpActive = true;
            else
                isUpActive = false;

            if (pressedWithoutOpposite(downKey, isUpActive))
                isDownActive = true;
            else
                isDownActive = false;

            if (pressedWithoutOpposite(leftKey, isRightActive))
                isLeftActive = true;
            else
                isLeftActive = false;

            if (pressedWithoutOpposite(rightKey, isLeftActive))
                isRightActive = true;
            else
                isRightActive = false;

            if (pressedWithoutOpposite(grappleKey, isPullActive))
                isGrappleActive = true;
            else
                isGrappleActive = false;

            if (pressedWithoutOpposite(pullKey, isGrappleActive))
                isPullActive = true;
            else
                isPullActive = false;
        }
    }

    // Return true if the target key is pressed its opposite key is not active.
    // Else, returns false.
    // Should be used to check if, say, the up key is pressed while down is not.
    public bool pressedWithoutOpposite(string target, bool opposite)
    {
        if (Input.GetKey(target) && !opposite)
            return true;
        else
            return false;
    }

    // Returns true if the input just got entered right now.
    public bool isUpEntered()
    {
        if (Input.GetKeyDown(upKey))
            return true;
        return false;
    }
    public bool isDownEntered()
    {
        if (Input.GetKeyDown(downKey))
            return true;
        return false;
    }
    public bool isLeftEntered()
    {
        if (Input.GetKeyDown(leftKey))
            return true;
        return false;
    }
    public bool isRightEntered()
    {
        if (Input.GetKeyDown(rightKey))
            return true;
        return false;
    }


}

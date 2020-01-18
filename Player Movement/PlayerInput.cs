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
    [Header("Which directions are currently input.")]
    public bool isUpActive = false;
    public bool isLeftActive = false;
    public bool isRightActive = false;
    public bool isDownActive = false;
    public bool isFallActive = false;
    public bool isHeld = false;

    public float directionZoneSize;
    public float minDistanceForSwipe = 20;

    public float speed;

    public SpriteRenderer rend;
    public Vector3 debugVector;

    public Vector2 oldTouchPosition; // the pixel position of a touch when it begins

    void Update()
    {
        //setInputs();

        //if (isUpActive)
        //    debugVector = new Vector3(0, 0.01f, 0);
        //if (isDownActive)
        //    debugVector = new Vector3(0, -0.01f, 0);
        //if (isLeftActive)
        //    debugVector = new Vector3(-0.01f, 0, 0);
        //if (isRightActive)
        //    debugVector = new Vector3(0.01f, 0, 0);

        //if (isFallActive)
        //    rend.color = new Color(Random.Range(0, 1f), Random.Range(0, 1f), Random.Range(0, 1f));

        //if (isHeld)
        //    this.transform.position += debugVector;

    }

    // Sets flags for each inputs - whether each direction input is currently active.
    public void setInputs()
    {
        if (Input.touchCount > 1)
        {
            noInput();
        }
        else if (Input.touchCount == 1)
        {
            Touch tempTouch = Input.GetTouch(0);
            if (tempTouch.tapCount == 2) // double tap
                isFallActive = true;
            else if (tempTouch.phase == TouchPhase.Began) // initial tap
            {
                oldTouchPosition = tempTouch.position;
            }
            else if (tempTouch.phase == TouchPhase.Moved) // swipe
            {
                float tempMovement = Mathf.Abs(Vector3.Distance(oldTouchPosition, tempTouch.position));
                if (tempMovement > minDistanceForSwipe)
                {
                    speed = tempMovement;
                    determineSwipeDirection(tempTouch.position);
                    oldTouchPosition = tempTouch.position;
                }
                // else - if is held, stay that way; if no input, stay that way - so do nothing
            }
            else if (tempTouch.phase == TouchPhase.Stationary) // held down
            {
                isUpActive = isDownActive = isLeftActive = isRightActive = isFallActive = false;
                isHeld = true;
            }
            else // else removed or 2+ multitap
            {
                noInput();
            }
        }
        else
        {
            noInput();
        }
    }

    // Determine if swipe direction is within range of the up, down, left, or right angle. 
    // For example, "perfect up" would be 90 degrees. The player does not have to get this angle exacly right, 
    // as they have a margin of error. This margin is determined by the directionZoneSize field. If this field was
    // say 30, this means 60 to 120 degrees would register as "up" - that's close enough to 90 degrees.
    void determineSwipeDirection(Vector2 newPosition)
    {
        float x = oldTouchPosition.x - newPosition.x;
        float y = oldTouchPosition.y - newPosition.y;

        if (Mathf.Abs(x) > Mathf.Abs(y))
        {
            if (newPosition.x > oldTouchPosition.x)
                isRightActive = true;
            else
                isLeftActive = true;
        }
        else
        {
            if (newPosition.y > oldTouchPosition.y)
                isUpActive = true;
            else
                isDownActive = true;
        }
    }

    void noInput()
    {
        isUpActive = isDownActive = isLeftActive = isRightActive = isFallActive = isHeld = false;
    }




}

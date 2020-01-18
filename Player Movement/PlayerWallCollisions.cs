/*
	As this class handles wall collisions, it should be used before every regular movement update to the player.
	
*/
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public enum Direction { UP, DOWN, LEFT, RIGHT, NONE };

public class PlayerWallCollisions : MonoBehaviour
{
    [Tooltip("Each current position of each point will have a line drawn between it and the point's planned position in the next frame. If a line hits a wall, the player is projected to pass through.")]
    public Transform[] linecastPoints;
    [Tooltip("Which layer the walls are on (Note: this layer should contain only wall objects.")]
    public int wallLayer;
    public LayerMask WallLayerMask;

    public string horizontalWallTag;
    public string verticalWallTag;

    public float topWallOffset = 1;
    public float bottomWallOffset = 1;
    public float rightWallOffset = 1;
    public float leftWallOffset = 1;

    public GameObject playerDebug;
    public Vector2 directionDebug;
    public float speedDebug;
    public bool isDebug = false;

    [Header("Grounded Check")]
    [Tooltip("Similar to linecastPoints, but just for the feet, to determine if there's ground benath the player.")]
    public Transform[] linecastFeetPoints;
    [Tooltip("How long the linecast should be. Should be small, to avoid false positives saying there's ground.")]
    public float feetCheckDistance = 0.01f;

    [Header("Vertical Wall Checks")]
    [Tooltip("Similar to linecastPoints, but just for the feet, to determine if there's ground left/right of the player.")]
    public Transform[] linecastLeftSidePoints;
    public Transform[] linecastRightSidePoints;
    [Tooltip("How long the linecast should be. Should be small, to avoid false positives saying there's a wall.")]
    public float sideCheckDistance = 0.01f;

    [Header("Ceiling Check")]
    [Tooltip("Similar to linecastPoints, but just for the head, to determine if there's ceiling above the player.")]
    public Transform[] linecastHeadPoints;
    [Tooltip("How long the linecast should be. Should be small, to avoid false positives saying there's ceiling.")]
    public float headCheckDistance = 0.01f;

    [Header("Turn Colliders")]
    [Tooltip("The colliders that surround the player used for turning corners. Should be put into the array clockwise, starting from the center top.")]
    public Collider2D[] turnColliders = new Collider2D[8];


    public Direction verticalCollision = Direction.NONE;
    public Direction horizontalCollision = Direction.NONE;

    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {
        if (isDebug)
        {
            Vector2 potentialNewPos = new Vector2(playerDebug.transform.position.x + (directionDebug.x * speedDebug * Time.deltaTime),
                                                  playerDebug.transform.position.y + (directionDebug.y * speedDebug * Time.deltaTime));

            playerDebug.transform.position = getPosition(playerDebug.transform.position, potentialNewPos);

        }
    }

	// Determines whether a movement options would phase through a wall or not, when given the current and planned positions. // If the movement would phase, it returns the best alternative position. 
	// If not, it returns the given potential new position as it was. This means that he Vector2 returned from this 
	// function is always the proper new position for the player taking when into account wall collisions.
    public Vector2 getPosition(Vector2 oldPos, Vector2 potentialNewPos)
    {
        verticalCollision = horizontalCollision = Direction.NONE;

        Vector2 potentialNewPos2 = reposition(oldPos, potentialNewPos);
        // the x and y components of the potential movement vector
        float potentialXMovement = Mathf.Abs(oldPos.x - potentialNewPos.x);
        float potentialYMovement = Mathf.Abs(oldPos.y - potentialNewPos.y);
        if (oldPos.x > potentialNewPos.x)
            potentialXMovement *= -1;
        if (oldPos.y > potentialNewPos.y)
            potentialYMovement *= -1;

        // Handle the edge/corner case where the rays go through vertical and horizontal walls, and one of those stop the player from moving where it seems they should.
        if ((potentialNewPos2.x != oldPos.x && potentialNewPos2.y != oldPos.y) || (potentialNewPos2.x != potentialNewPos.x && potentialNewPos2.y != potentialNewPos.y))
        {
            if (Mathf.Abs(oldPos.x - potentialNewPos2.x) < Mathf.Abs(oldPos.y - potentialNewPos2.y))
            {
                Vector2 temp = reposition(oldPos, new Vector2(oldPos.x + potentialXMovement, potentialNewPos2.y));
                if (temp == potentialNewPos2)
                    potentialNewPos2 = reposition(oldPos, new Vector2(potentialNewPos2.x, oldPos.y + potentialYMovement));
                else
                    potentialNewPos2 = temp;
            }
            else
            {
                Vector2 temp = reposition(oldPos, new Vector2(potentialNewPos2.x, oldPos.y + potentialYMovement));
                if (temp == potentialNewPos2)
                    potentialNewPos2 = reposition(oldPos, new Vector2(oldPos.x + potentialXMovement, potentialNewPos2.y));
                else
                    potentialNewPos2 = temp;
            }

        }


        // If only one of the coordinates changed, make sure that the new position does not go through a wall either.
        if ((potentialNewPos2.x != potentialNewPos.x && potentialNewPos2.y == potentialNewPos.y) ||
            (potentialNewPos2.y != potentialNewPos.y && potentialNewPos2.x == potentialNewPos.x))
        {
            potentialNewPos2 = reposition(oldPos, potentialNewPos2);
        }


        return potentialNewPos2;
    }

    // Given the position from last frame and a potential new position, 
    // determines whether the proposed movement would make the player 
    // pass through a wall. If not, then it just returns the potential 
    // new position. If so, then the function determines and  
    // returns what the proper position should be. It does this by letting 
    // the player move as close to the potential new position as possible.
    public Vector2 reposition(Vector2 oldPos, Vector2 potentialNewPos)
    {
        float finalXPos = 0, finalYPos = 0;  // the final resulting coordinates
        Direction wallHitSide;

        float temp;
        bool isPosWallXSet = false, isPosWallYSet = false; // has a potential wall collision resulted in a altering a coordinate (according to the corresponding offset)?

        //// Go through each point, and see if there is a wall collision along the line between the point's current position and its potential position next frame.
        foreach (Transform tempPoint in linecastPoints)
        {
            // Linecast
            RaycastHit2D wallHit = Physics2D.Linecast(new Vector2(tempPoint.position.x, tempPoint.position.y),
                                    new Vector2(tempPoint.localPosition.x + potentialNewPos.x, tempPoint.localPosition.y + potentialNewPos.y),
                                    wallLayer);

            // If a wall is hit, determines which part of the wall (top, bottom, left, or right) was hit. This determines how the player's
            // position should likely be adjusted.
            if (wallHit)
            {
                wallHitSide = getWallHitSpot(wallHit.collider, wallHit.point);

                if (wallHitSide == Direction.LEFT)  // if left part of the wall
                {
                    temp = wallHit.point.x - leftWallOffset;
                    if (!isPosWallXSet || temp < finalXPos)  // only set if this is the most left option
                    {
                        finalXPos = temp;
                        isPosWallXSet = true;
                    }
                    horizontalCollision = Direction.RIGHT;
                }
                else if (wallHitSide == Direction.RIGHT)  // if right part of the wall
                {
                    temp = wallHit.point.x + rightWallOffset;
                    if (!isPosWallXSet || temp > finalXPos)  // only set if this is most right option
                    {
                        finalXPos = temp;
                        isPosWallXSet = true;
                    }
                    horizontalCollision = Direction.LEFT;
                }
                else if (wallHitSide == Direction.DOWN) // if bottom part of the wall
                {
                    temp = wallHit.point.y - bottomWallOffset;
                    if (!isPosWallYSet || temp < finalYPos)  // only set if this is the lowest option
                    {
                        finalYPos = temp;
                        isPosWallYSet = true;
                    }
                    verticalCollision = Direction.UP;
                }
                else  // if top part of the wall
                {
                    temp = wallHit.point.y + topWallOffset;
                    if (!isPosWallYSet || temp > finalYPos)  // only set if this is the highest option
                    {
                        finalYPos = temp;
                        isPosWallYSet = true;
                    }
                    verticalCollision = Direction.DOWN;
                }

            } // end of wallHit if statement

        } // end of linecastPoints foreach loop

        // Return the new position.
        if (isPosWallXSet || isPosWallYSet)  // if it is determined that the potential new position needs altered
        {
            if (!isPosWallXSet)
                return new Vector2(potentialNewPos.x, finalYPos);
            else if (!isPosWallYSet)
                return new Vector2(finalXPos, potentialNewPos.y);
            else
                return new Vector2(finalXPos, finalYPos);
        }
        else  // return as it was
        {
            return potentialNewPos;
        }

    } // end of reposition function


    // Returns whether movement between two given positions (Vector2's) would result in crossing a wall.
    private bool doesMovementCrossWall(Vector2 start, Vector2 end)
    {
        foreach (Transform tempPoint in linecastPoints)
        {
            RaycastHit2D wallHit = Physics2D.Linecast(new Vector2(tempPoint.position.x, tempPoint.position.y),
                                        new Vector2(tempPoint.localPosition.x + end.x, tempPoint.localPosition.y + end.y),
                                        wallLayer);

            if (wallHit)
            {
                return false;
            }
        }
        return true;
    }

    private bool equalsAbout(float number1, float number2, float minCloseness)
    {
        if (Mathf.Abs(number1 - number2) <= minCloseness)
            return true;
        else
            return false;
    }

	// Determines which side of a wall was hit, up, down, left, or right, given the wall's
	// collider and the point of collision.
    Direction getWallHitSpot(Collider2D collider, Vector2 point)
    {
        float yScore, xScore;
        bool didUpWin;

        if (point.y > collider.bounds.center.y)  // up score
        {
            yScore = (point.y - collider.bounds.center.y) / (collider.bounds.size.y / 2);
            didUpWin = true;
        }
        else // down score
        {
            yScore = (collider.bounds.center.y - point.y) / (collider.bounds.size.y / 2);
            didUpWin = false;
        }

        if (point.x > collider.bounds.center.x) // right score
        {
            xScore = (point.x - collider.bounds.center.x) / (collider.bounds.size.x / 2);
            if (xScore > yScore)
                return Direction.RIGHT;
        }
        else // left score
        {
            xScore = (collider.bounds.center.x - point.x) / (collider.bounds.size.x / 2);
            if (xScore > yScore)
                return Direction.LEFT;
        }

        if (didUpWin)
            return Direction.UP;
        else
            return Direction.DOWN;
    }

    // Go through each foot point, and draw a line between it and a liiiiitle bit lower. If there's a wall collision, it's grounded! 
    // Otherwise, it's not.
    public bool isFeetOnGround()
    {
        return isAgainstSurface(linecastFeetPoints, -1 * feetCheckDistance, true);
    }

	// Same as isFeetOnGround, but for the head.
    public bool isHeadAgainstCiling()
    {
        return isAgainstSurface(linecastHeadPoints, headCheckDistance, true);
    }

    // Returns if against a vertical wall, given whether to search for a wall on the left side (true) or right side (false).
    public bool isSideAgainstWall(bool isLeft)
    {
        Transform[] points;
        float checkDistance;
        if (isLeft)
        {
            checkDistance = -1 * sideCheckDistance;
            points = linecastLeftSidePoints;
        }
        else
        {
            checkDistance = sideCheckDistance;
            points = linecastRightSidePoints;
        }

        return isAgainstSurface(points, checkDistance, false);
    }

    // Returns whether the collider, specified by an index for the turnColliders array, is free from contacting a wall.
    public bool isTurnColliderOpen(int index)
    {
        return !turnColliders[index].IsTouchingLayers(WallLayerMask);
    }

    // Returns true if there's a wall between the given set of points and a projected set of points, either above, below, to the left, or to the right of the original ones.
    // points is the list of Transforms, distance is how far the check will be, and isVertical determines if the check should be above/below (true) or should be (left/right).
    // distance should be given as positive or negative determining if its left/down (negative number) or right/up (positive number).
    private bool isAgainstSurface(Transform[] points, float distance, bool isVertical)
    {
        foreach (Transform tempPoint in points)
        {
            // Get the second point for the linecast.
            Vector2 projectedPoint = tempPoint.position;
            if (isVertical)
                projectedPoint += new Vector2(0, distance);
            else
                projectedPoint += new Vector2(distance, 0);

            // Linecast
            RaycastHit2D wallHit = Physics2D.Linecast(new Vector2(tempPoint.position.x, tempPoint.position.y),
                                    projectedPoint,
                                    wallLayer);

            if (wallHit)
                return true;
        }
        return false;
    }
}
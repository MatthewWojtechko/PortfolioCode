using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CollisionDetection : MonoBehaviour
{
    public Direction movementDir;
    public float boxCastWidth;
    public float offset;
    public string wallLayer;

    public Vector2 properPosition;

    //public int debugLayer;
    //public float debugDistance;
    //public bool debugResult;
    //public SpriteRenderer debugRend;

    //void Update()
    //{
    //    debugResult = isCollisionFree(debugLayer, debugDistance);
    //    if (debugResult)
    //        debugRend.color = Color.green;
    //    else
    //        debugRend.color = Color.red;
    //}

    // Determines if the there is a collision on the given layer between the current point and the given distance.
    // If so, it finds the nearest collision point, and using that and the offset, determines the closest point to 
    // move to that takes that collider in account. That result is stored in the properPosition public field.
    // If the there is no collision, then this function returns true. Else, it returns false.
    // Note: distance is in WORLD SPACE and the wall layer is as of now #8.
    public bool isCollisionFree(int layer, float distance)
    {
        // Find the direction vector
        Vector2 direction;
        if (movementDir == Direction.DOWN)
            direction = new Vector2(0, -1);
        else if (movementDir == Direction.LEFT)
            direction = new Vector2(-1, 0);
        else if (movementDir == Direction.RIGHT)
            direction = new Vector2(1, 0);
        else
            direction = new Vector2(0, 1);

        // Box cast
        RaycastHit2D result = Physics2D.BoxCast(this.transform.position, new Vector2(0.9f, 0.9f), 0, direction, distance, LayerMask.GetMask(wallLayer));

        if (result)  // if contact, find the correct point
        {
            Vector2 point = result.point;
            if (movementDir == Direction.LEFT)
            {
                point = new Vector2(point.x + offset, this.transform.position.y);
            }
            else if (movementDir == Direction.RIGHT)
            {
                point = new Vector2(point.x - offset, this.transform.position.y);
            }
            else if (movementDir == Direction.UP)
            {
                point = new Vector2(this.transform.position.x, point.y - offset);
            }
            else // DOWN
            {
                point = new Vector2(this.transform.position.x, point.y + offset);
            }
            properPosition = point;
            return false;
        }
        else
        {
            return true;
        }
    }
}

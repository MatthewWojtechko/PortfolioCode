// When a player stands on top of a moving platform, move them with the platform.
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoveWithPlatform : MonoBehaviour
{
    public List<GameObject> movingChildren;
    public bool isActive = true;

    // Update is called once per frame
    void Update()
    {
        if (!isActive && movingChildren.Count != 0)
        {
            unchildAllMovers();
        }
    }

    // When certain things hit this, make them children so they move with this.
    public void OnTriggerEnter2D(Collider2D col)
    {
        if (isActive)
        {
            if (col.gameObject.tag == "Feet")
            {
                col.gameObject.transform.parent.gameObject.transform.parent = this.transform;
                movingChildren.Add(col.gameObject.transform.parent.gameObject);
            }
        }
    }

    // When they exit, make them no longer children so they no longe move with this.
    public void OnTriggerExit2D(Collider2D col)
    {
        if (col.gameObject.tag == "Feet")
        {
            col.gameObject.transform.parent.gameObject.transform.parent = null;
            movingChildren.Remove(col.gameObject.transform.parent.gameObject);
        }
    }

    // Removes all children from moving with this.
    // Should be called before the platform is inactived.
    public void unchildAllMovers()
    {
        foreach (GameObject X in movingChildren)
        {
            X.transform.parent = null;
        }
        movingChildren.Clear();
    }
}

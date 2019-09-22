/*
	Produces a flowy stretch effect for the lava in Vamoose. It does this by gradually increasing
	and decreasing the X scale and y scale.
*/

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LavaFlow : MonoBehaviour
{
    public float xDistance;
    public float yDistance;
    public float xSpeed;
    public float ySpeed;
    public float xLerpC;
    public float yLerpC;

    private float currentXStretch;
    private float currentYStretch;
    private bool xStretchPositive = true;
    private bool yStretchPositive = true;

    private float xScaleStart;
    private float yScaleStart;
    private float xCurrentSpeed;
    private float yCurrentSpeed;

    void Awake()
    {
        xScaleStart = transform.localScale.x;
        yScaleStart = transform.localScale.y;
    }

    // Update is called once per frame
    void Update()
    {
        if (xStretchPositive)	// if X scale is increasing
        {
            xCurrentSpeed = Mathf.Lerp(xCurrentSpeed, xSpeed, xLerpC);
            currentXStretch += xCurrentSpeed * Time.deltaTime;
            if (currentXStretch >= xDistance - 0.01f)
            {
                xStretchPositive = false;
                xCurrentSpeed = 0;
            }
        }  
        else
        {
            xCurrentSpeed = Mathf.Lerp(xCurrentSpeed, xSpeed, xLerpC);
            currentXStretch -= xCurrentSpeed * Time.deltaTime;
            if (currentXStretch <= 0.01f)
            {
                xStretchPositive = true;
                xCurrentSpeed = 0;
            }
        }

        if (yStretchPositive)	// if Y scale is increasing
        {
            yCurrentSpeed = Mathf.Lerp(yCurrentSpeed, ySpeed, yLerpC);
            currentYStretch += yCurrentSpeed * Time.deltaTime;
            if (currentYStretch >= yDistance - 0.01f)
            {
                yStretchPositive = false;
                yCurrentSpeed = 0;
            }
        }
        else
        {
            yCurrentSpeed = Mathf.Lerp(yCurrentSpeed, ySpeed, yLerpC);
            currentYStretch -= yCurrentSpeed * Time.deltaTime;
            if (currentYStretch <= 0.01f)
            {
                yStretchPositive = true;
                yCurrentSpeed = 0;
            }
        }
        transform.localScale = new Vector3(xScaleStart + currentXStretch, yScaleStart + currentYStretch, transform.localScale.z);
    }
}

/*
	Loops through different colors for a continuous color-changing effect.
*/
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LightColorChange : MonoBehaviour
{
    public float colorRate;

    private Light light;
    private bool increaseComp; // T for increase, F for decrease
    private int changeColor; // which color component to change: r = 1, g = 2, b = 3
    
    void Awake()
    {
        light = GetComponent<Light>();
    }

    // Update is called once per frame
    void Update()
    {
        /*
         * The light will cycle through these colors.
         * Rgb
         * RGb
         * rGb
         * rGB
         * rgB
         * RgB
         * Note: a capital letter represents that color component is to its max (1), 
         * while a lowercase letter represents that color component is less than 1 (or is 0). 
	 * So, Rgb means that the color's r component is 1, but the other 2 components are less.
         */
        float redComp = light.color.r,
              greenComp = light.color.g,
              blueComp = light.color.b;
        if (redComp >= 1 && greenComp <= 0 && blueComp <= 0)
        {
            increaseComp = true;
            changeColor = 2;
        }
        else if (redComp >= 1 && greenComp >= 1 && blueComp <= 0)
        {
            increaseComp = false;
            changeColor = 1;
        }
        else if (redComp <= 0 && greenComp >= 1 && blueComp <= 0)
        {
            increaseComp = true;
            changeColor = 3;
        }
        else if (redComp <= 0 && greenComp >= 1 && blueComp >= 1)
        {
            increaseComp = false;
            changeColor = 2;
        }
        else if (redComp <= 0 && greenComp <= 0 && blueComp >= 1)
        {
            increaseComp = true;
            changeColor = 1;
        }
        else if (redComp >= 1 && greenComp <= 0 && blueComp >= 1)
        {
            increaseComp = false;
            changeColor = 3;
        }

        float changeAmount = Time.deltaTime * colorRate;
        if (!increaseComp)
            changeAmount *= -1;

        if (changeColor == 1)
            redComp += changeAmount;
        else if (changeColor == 2)
            greenComp += changeAmount;
        else
            blueComp += changeAmount;

        light.color = new Color(redComp, greenComp, blueComp);
    }
}

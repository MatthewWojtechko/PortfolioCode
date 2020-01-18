using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ButtonPress : MonoBehaviour
{
    public GameObject Button;
    public float buttonMoveSpeed;
    public float buttonMoveDistance;
    public ButtonPlatform ButtonScript;
    public float waitBetween = 0.5f;
    public int numCollisions = 0;

    private bool isWaiting = false;
    private int buttonState; // 0 = stay, 1 = go up, 2 = go down
    private float buttonStartY;
    private bool isButtonUp;
    // Start is called before the first frame update
    void Start()
    {
        buttonStartY = Button.transform.position.y;
        isButtonUp = true;
    }

    // Update is called once per frame
    void Update()
    {
        if (numCollisions == 0 && !isButtonUp)
        {
            if (!isWaiting)
            {
                StartCoroutine(waitGoUp());
            }
        }

        if (buttonState == 2)
        {
            Button.transform.position -= new Vector3(0, buttonMoveSpeed * Time.deltaTime, 0);
            if (Button.transform.position.y <= buttonStartY - buttonMoveDistance)
            {
                Button.transform.position = new Vector3(Button.transform.position.x, buttonStartY - buttonMoveDistance, Button.transform.position.z);
                buttonState = 0;
                isButtonUp = false;
            }
        }
        else if (buttonState == 1)
        {
            Button.transform.position += new Vector3(0, buttonMoveSpeed * Time.deltaTime, 0);
            if (Button.transform.position.y >= buttonStartY)
            {
                Button.transform.position = new Vector3(Button.transform.position.x, buttonStartY, Button.transform.position.z);
                buttonState = 0;
                isButtonUp = true;
            }
        }

    }

    public void OnTriggerEnter2D(Collider2D col)
    {
         if (col.gameObject.tag == "Feet" || col.gameObject.tag == "Box" || col.gameObject.tag == "Head")
         {
             buttonState = 2;
             ButtonScript.PlayOption2();
             numCollisions++;
         }      
    }

    public void OnTriggerExit2D(Collider2D col)
    {
         if (col.gameObject.tag == "Feet" || col.gameObject.tag == "Box" || col.gameObject.tag == "Head")
         {
             numCollisions--;
         }
    }

    IEnumerator waitGoUp()
    {
        isWaiting = true;
        yield return new WaitForSeconds(waitBetween);
        if (numCollisions == 0)
        {
            buttonState = 1;
            ButtonScript.PlayOption1();
        }
        isWaiting = false;
    }
}

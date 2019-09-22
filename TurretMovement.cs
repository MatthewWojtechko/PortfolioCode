/*
	Moves a turret across a screen using WASD. As the turret is moved, it automatically
	angles in a contunuous way so as to aid shooting in 3D First-Person-Shooter expectations.
*/

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ReticleMovement : MonoBehaviour
{
    public Vector3 startPos;
    public float leftBound;
    public float rightBound;
    public float topBound;
    public float bottomBound;
    public float speedUpLerpConst;
    public float slowDownLerpConst;
    public float maxSpeed;
    public float slowSpeedUpLerpConst;
    public float slowSpeedLimit;
    public float rotateXMax;
    public float rotateYMax;
    public AudioSource moveSound;
    public GameObject turretBlock;

    private Material turretMaterial;
    private float currentXSpeed;
    private float currentYSpeed;
    private Vector3 movementVector;
    private bool dirRight;
    private bool dirTop;

    // Start is called before the first frame update
    void Start()
    {
        transform.position = startPos;
        turretMaterial = turretBlock.GetComponent<MeshRenderer>().material;
    }

    // Update is called once per frame
    void Update()
    {
		// Moves and angles the turret
        movement();
        this.transform.position += movementVector;
        rotateAim();

		// Control what sounds are played and how the turret's color changes. As the turret transitions from low speed to 
		// normal speed, become more and more white.
        if ((Input.GetKey("w") || Input.GetKey("a") || Input.GetKey("s") || Input.GetKey("d")) && (Mathf.Abs(currentXSpeed) > 0 || Mathf.Abs(currentYSpeed) > 0))
        {
            if (!moveSound.isPlaying)
                moveSound.Play();

            if (Mathf.Abs(currentXSpeed) < slowSpeedLimit && Mathf.Abs(currentXSpeed) > 0)
                turretMaterial.color = new Color(1, (Mathf.Abs(currentXSpeed) / slowSpeedLimit / 2), (Mathf.Abs(currentXSpeed) / slowSpeedLimit / 2), 1);
            else if (Mathf.Abs(currentYSpeed) < slowSpeedLimit && Mathf.Abs(currentYSpeed) > 0)
                turretMaterial.color = new Color(1, (Mathf.Abs(currentYSpeed) / slowSpeedLimit / 2), (Mathf.Abs(currentYSpeed) / slowSpeedLimit / 2), 1);
            else
                turretMaterial.color = new Color(1, 0, 0, 1);
        }
        else
        {
            moveSound.Stop();
            turretMaterial.color = new Color(1, 0, 0, 1);
        }


        }

	// Handles 2 dimensional movement. Given the input, determines how the turret should move, taking into account
	// lerping for smootheness. THe turret also moves according to this idiosyncrasy - when the turret is stationary 
	// and starts moving, it moves slow to aid in minor aim adjustment. After a little bit, though, the turret moves 
	// at a normal speed. 
	// Finishes by updating the movement vector.
    void movement()
    {
        float yMove = 0, xMove = 0;

        // Find x movement
        if (Input.GetKey("d") && !Input.GetKey("a"))
        {

            if (currentXSpeed < 0)
                currentXSpeed = 0;
            else if (currentXSpeed < slowSpeedLimit - 0.1f || Mathf.Abs(currentYSpeed) > slowSpeedLimit)
                currentXSpeed = Mathf.Lerp(currentXSpeed, slowSpeedLimit, slowSpeedUpLerpConst);
            else if (currentXSpeed < maxSpeed - 0.1f)
                currentXSpeed = Mathf.Lerp(currentXSpeed, maxSpeed, speedUpLerpConst);
            else
                currentXSpeed = maxSpeed;
        }
        else if (Input.GetKey("a"))
        {
            if (currentXSpeed > 0)
                currentXSpeed = 0;
            else if (currentXSpeed > -1 * slowSpeedLimit + 0.1f || Mathf.Abs(currentYSpeed) > slowSpeedLimit)
                currentXSpeed = Mathf.Lerp(currentXSpeed, -1 * slowSpeedLimit, slowSpeedUpLerpConst);
            else if (currentXSpeed > -1 * maxSpeed + 0.1f)
                currentXSpeed = Mathf.Lerp(currentXSpeed, -1 * maxSpeed, speedUpLerpConst);
            else
                currentXSpeed = -1 * maxSpeed;
        }
        else
        {
            if (currentXSpeed < 0.1f && currentXSpeed > -0.1f)
                currentXSpeed = 0;
            else
                currentXSpeed = Mathf.Lerp(currentXSpeed, 0, slowDownLerpConst);
        }

        // Find y movement
        if (Input.GetKey("w") && !Input.GetKey("s"))
        {
            if (currentYSpeed < 0)
                currentYSpeed = 0;
            else if (currentYSpeed < slowSpeedLimit - 0.1f || Mathf.Abs(currentYSpeed) > slowSpeedLimit)
                currentYSpeed = Mathf.Lerp(currentYSpeed, slowSpeedLimit, slowSpeedUpLerpConst);
            else if (currentYSpeed < maxSpeed - 0.1f)
                currentYSpeed = Mathf.Lerp(currentYSpeed, maxSpeed, speedUpLerpConst);
            else
                currentYSpeed = maxSpeed;
        }
        else if (Input.GetKey("s"))
        {
            if (currentYSpeed > 0)
                currentYSpeed = 0;
            else if (currentYSpeed > -1 * slowSpeedLimit + 0.1f || Mathf.Abs(currentYSpeed) > slowSpeedLimit)
                currentYSpeed = Mathf.Lerp(currentYSpeed, -1 * slowSpeedLimit, slowSpeedUpLerpConst);
            else if (currentYSpeed > -1 * maxSpeed + 0.1f)
                currentYSpeed = Mathf.Lerp(currentYSpeed, -1 * maxSpeed, speedUpLerpConst);
            else
                currentYSpeed = -1 * maxSpeed;
        }
        else
        {
            if (currentYSpeed < 0.1f && currentYSpeed > -0.1f)
                currentYSpeed = 0;
            else
                currentYSpeed = Mathf.Lerp(currentYSpeed, 0, slowDownLerpConst);
        }
		
		// Set the movement vector
        xMove = currentXSpeed * Time.deltaTime;
        yMove = currentYSpeed * Time.deltaTime;

        xMove = boundMovement(xMove, this.transform.position.x, leftBound, rightBound);
        yMove = boundMovement(yMove, this.transform.position.y, bottomBound, topBound);

        movementVector = new Vector3(xMove, yMove, 0);
    }

	// Stops the turret from moving out of frame of the camera.
    float boundMovement(float movement, float pos, float lowLimit, float hiLimit)
    {
        if (movement + pos > hiLimit)
            return Mathf.Abs(hiLimit - pos);
        else if (movement + pos < lowLimit)
            return -1 * Mathf.Abs(lowLimit - pos);
        else
            return movement;
    }

    // Rotates the turret in relation to its current position. 
    void rotateAim()
    {
        float yAngle = rotateXMax * (transform.localPosition.x / Mathf.Abs(rightBound));
        float xAngle = -1 * rotateYMax * ((transform.localPosition.y-1) / Mathf.Abs(topBound-1));
        transform.eulerAngles = new Vector3(xAngle, yAngle, 0);
    }
}

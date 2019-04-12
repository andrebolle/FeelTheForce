package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

// Create balls with a colour or texture
public class BallPark extends Ball {

    Vector2 hole;
    Color color;

    public BallPark(World world, Vector2 startPos,
                    float radius, Color color, Vector2 hole) {
        super(world, startPos, radius, color);
        this.hole = hole;
        this.color = color;
    }

    void move(Vector2 g) {
        // Find if its going down the hole
        Vector2 ballPos = box2dBody.getPosition().cpy();

        // In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
        float distSquared =
                (ballPos.x - hole.x) * (ballPos.x - hole.x) +
                (ballPos.y - hole.y) * (ballPos.y - hole.y);

        float dist = (float)Math.sqrt((double) distSquared);

        if (dist > 2f) {
            // Apply a force to the center of mass. This wakes up the body.
//            box2dBody.applyForceToCenter(-box2dBody.getPosition().x + g.y,
//                    -box2dBody.getPosition().y - g.x, true);
            box2dBody.applyForceToCenter(g.y, -g.x, true);
        }
        else
        {
            float arf = 0.5f;
            float airX = -box2dBody.getLinearVelocity().x * arf;
            float airY = -box2dBody.getLinearVelocity().y * arf;

            Vector2 holeForce = hole.cpy().sub(box2dBody.getPosition()).nor().scl(20f);

            box2dBody.applyForceToCenter(+ g.y + holeForce.x +airX,
                     - g.x + holeForce.y + airY, true);
        }


        // Sets this matrix to a translation matrix, overwriting
        // it first by an identity matrix and then setting the 4th
        // column to the translation vector.
        modelInstance.transform
                .setToTranslation(new Vector3(box2dBody.getPosition().x, box2dBody.getPosition().y, 0));

    }

}
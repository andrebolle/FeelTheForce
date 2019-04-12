package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

// Create balls with a colour or texture
public class BallParkMenu extends Ball {

    Vector2[] menuPositions;
    String[] menuText;
    Color color;

    public BallParkMenu(World world, Vector2 startPos,
                        float radius, Color color, Vector2[] menuPositions, String[] menuText) {
        super(world, startPos, radius, color);
        this.menuPositions = menuPositions;
        this.menuText = menuText;
        this.color = color;
    }

    String move(Vector2 g, Tray tray) {

        Vector2 ballPos = box2dBody.getPosition().cpy();

        for (int index = 0; index < menuPositions.length; index++) {

            Vector2 hole = menuPositions[index];

            float distSquared =
                    (ballPos.x - hole.x) * (ballPos.x - hole.x) +
                            (ballPos.y - hole.y) * (ballPos.y - hole.y);

            float dist = (float)Math.sqrt((double) distSquared);


            if (dist <= 2)
            {
                if (dist < 0.5 && box2dBody.getLinearVelocity().len() < 1f) return menuText[index];


                    float arf = 0.5f;
                float airX = -box2dBody.getLinearVelocity().x * arf;
                float airY = -box2dBody.getLinearVelocity().y * arf;

                Vector2 holeForce = hole.cpy().sub(box2dBody.getPosition()).nor().scl(20f);

//                box2dBody.applyForceToCenter(+g.y + holeForce.x + airX,
//                        -g.x + holeForce.y + airY, true);
                box2dBody.applyForceToCenter(holeForce.x + airX,
                        holeForce.y + airY, true);


                modelInstance.transform
                        .setToTranslation(new Vector3(box2dBody.getPosition().x, box2dBody.getPosition().y, 0));

                Vector2 textPosOnScreen = tray.worldToPixel(hole);
                tray.spriteBatch.begin();
                tray.font.draw(tray.spriteBatch, menuText[index], textPosOnScreen.x, textPosOnScreen.y + 130f);
                tray.spriteBatch.end();

                break;
            }
        }

        box2dBody.applyForceToCenter(g.y, -g.x, true);

        modelInstance.transform
                .setToTranslation(new Vector3(box2dBody.getPosition().x, box2dBody.getPosition().y, 0));

        return null;
    }
}
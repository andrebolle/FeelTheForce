package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

// Create balls with a colour or texture
public class BallOnSpring extends Ball {

    float springStiffness = 5f;

    public BallOnSpring(World world, Vector2 startPos,
                float radius, Color color) {
        super(world, startPos, radius, color);
    }

    void move(Vector2 g) {
        // Apply a force to the center of mass. This wakes up the body.
        box2dBody.applyForceToCenter(-box2dBody.getPosition().x * springStiffness + g.y,
                -box2dBody.getPosition().y * springStiffness - g.x, true);

        // Sets this matrix to a translation matrix, overwriting
        // it first by an identity matrix and then setting the 4th
        // column to the translation vector.
        modelInstance.transform
                .setToTranslation(new Vector3(box2dBody.getPosition().x, box2dBody.getPosition().y, 0));

    }
}
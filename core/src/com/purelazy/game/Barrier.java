package com.purelazy.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Barrier {

    protected final float DEGREES_TO_RADIANS = (float) Math.PI / 180f;

    Body box2dBody;

    public Barrier(World world, Vector2 start, Vector2 finish) {
        // A barrier is a rectangle of length by (thin) width
        float width = 0.1f;

        // Line length || F - S ||
        float length = finish.cpy().sub(start).len();

        Vector2 position = start.cpy().add(finish).scl(0.5f);

        float angle = finish.cpy().sub(start).angleRad();

        // 2D Physics
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length / 2f, width);

        // Create our body definition
        BodyDef bodydef = new BodyDef();
        bodydef.position.set(position);
        bodydef.angle = angle;
        bodydef.type = BodyType.KinematicBody;

        // Create a body from the definition and add it to the world
        box2dBody = world.createBody(bodydef);
        box2dBody.createFixture(shape, 0.0f);
        box2dBody.setUserData("barrier");

        shape.dispose();
    }
}

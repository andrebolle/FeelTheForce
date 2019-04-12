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

/**
 * Freely moving
 */
public class Rock {

    private static final int DIVISIONS_U = 12;
    private static final int DIVISIONS_V = 12;
    Body box2dBody;

    ModelInstance modelInstance;
    Model model;

    Vector3 setToTranslationParam = new Vector3(0, 0, 0);

    float springStiffness = 5f;
    private static final float RADIANS_TO_DEGREES = 180f / (float) Math.PI;


    public Rock(World world, Vector2 pos,
                           float radius, Texture tex) {

        // 3D Part
        ModelBuilder modelBuilder = new ModelBuilder();

        TextureAttribute texAttr = new TextureAttribute(
                TextureAttribute.Diffuse, tex);

        model = modelBuilder.createSphere(radius * 2f, radius * 2f,
                radius * 2f, DIVISIONS_U, DIVISIONS_V, new Material(texAttr),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        modelInstance = new ModelInstance(model);

        // Box2D part
        box2D_Ball(world, pos, radius);
    }

    private void box2D_Ball(World world, Vector2 pos, float radius) {

        // Box2D Part
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        // FixtureDef - intrinsic properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.4f;
        fixtureDef.isSensor = false;

        // BodyDef - define extrinsic properties
        BodyDef bodyDef = new BodyDef();

        // By default bodies are static, so set to DynamicBody
        bodyDef.type = BodyType.DynamicBody;

        bodyDef.linearDamping = 0.1f;
        bodyDef.angularDamping = 0.1f;
        bodyDef.position.set(pos.x, pos.y);

        // Create our body in the world using our body definition
        box2dBody = world.createBody(bodyDef);
        box2dBody.createFixture(fixtureDef);

        circle.dispose();
    }

    void move(Vector2 g) {
        box2dBody.applyForceToCenter(-box2dBody.getPosition().x * springStiffness + g.y,
                -box2dBody.getPosition().y * springStiffness - g.x, true);

        // Rotations
        modelInstance.transform = new Matrix4(
                new Vector3(box2dBody.getPosition().x, box2dBody.getPosition().y, 0),
                new Quaternion(new Vector3(0f, 0f, 1f), box2dBody.getAngle() * RADIANS_TO_DEGREES),
                new Vector3(1f, 1f, 1f));

    }
}
package com.purelazy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Beam {

    private static final float RADIANS_TO_DEGREES = 180f / (float) Math.PI;
    Body box2dBody;
    ModelInstance modelInstance;
    Model model;


    public Beam(World world, float length, float width, float depth, float angvel) {

        // Create texture
        Texture beamTexture = new Texture(Gdx.files.internal("data/stonewhitebeam.png"));

        TextureAttribute diffuseAttr = new TextureAttribute(TextureAttribute.Diffuse, beamTexture);
        TextureAttribute specularAttr = new TextureAttribute(TextureAttribute.Specular, beamTexture);

        Material beamMaterial = new Material( // Attribute List
                diffuseAttr, specularAttr);

        // Create beam model
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(length, width, depth, beamMaterial, Usage.Position
                | Usage.Normal | Usage.TextureCoordinates);

        // Create an instance of the beam model
        modelInstance = new ModelInstance(model);


        // 2D Physics
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length / 2f, width / 2f);

        // Create our body definition
        BodyDef bodydef = new BodyDef();
        bodydef.position.set(new Vector2(0, 0));

        // Kinematics
        bodydef.type = BodyType.KinematicBody;
        bodydef.angularVelocity = angvel;

        // Create a body from the definition and add it to the world
        box2dBody = world.createBody(bodydef);
        box2dBody.createFixture(shape, 0.0f);
        box2dBody.setUserData("beam");

        shape.dispose();
    }

    void rotate() {
        modelInstance.transform = new Matrix4(
                new Vector3(box2dBody.getPosition().x, box2dBody.getPosition().y, 0),
                new Quaternion(new Vector3(0f, 0f, 1f), box2dBody.getAngle() * RADIANS_TO_DEGREES),
                new Vector3(1f, 1f, 1f));
    }
}

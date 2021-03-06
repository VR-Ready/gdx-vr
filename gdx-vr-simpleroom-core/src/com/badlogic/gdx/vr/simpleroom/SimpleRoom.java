package com.badlogic.gdx.vr.simpleroom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.vr.Stage3D;
import com.badlogic.gdx.vr.VirtualReality;
import com.badlogic.gdx.vr.VirtualRealityRenderListener;

public class SimpleRoom extends ApplicationAdapter implements VirtualRealityRenderListener {

	private ModelBatch modelBatch;
	private ModelInstance modelInstance;
	private ModelInstance ground;
	private AssetManager assets;
	private Environment environment;
	private Stage3D stage;

	@Override
	public void create() {
		assets = new AssetManager();
		String model = "Bambo_House.g3db";
		assets.load(model, Model.class);
		assets.finishLoading();
		modelInstance = new ModelInstance(assets.get(model, Model.class), new Matrix4().setToScaling(0.6f, 0.6f, 0.6f));

		DefaultShader.Config config = new Config();
		config.defaultCullFace = GL20.GL_NONE;
		ShaderProvider shaderProvider = new DefaultShaderProvider(config);
		modelBatch = new ModelBatch(shaderProvider);

		ModelBuilder builder = new ModelBuilder();
		float groundSize = 1000f;
		ground = new ModelInstance(builder.createRect(-groundSize, 0, groundSize, groundSize, 0, groundSize, groundSize, 0, -groundSize, -groundSize, 0, -groundSize, 0,
				1, 0, new Material(), Usage.Position | Usage.Normal), new Matrix4().setToTranslation(0, -0.01f, 0));
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		VirtualReality.renderer.listeners.add(this);
		// VirtualReality.head.setCyclops(true);
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			VirtualReality.body.position.add(new Vector3(0, 0, -2).mul(VirtualReality.body.orientation).scl(deltaTime));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			VirtualReality.body.position.add(new Vector3(0, 0, 2).mul(VirtualReality.body.orientation).scl(deltaTime));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			VirtualReality.body.orientation.mulLeft(new Quaternion(Vector3.Y, 90f * deltaTime));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			VirtualReality.body.orientation.mulLeft(new Quaternion(Vector3.Y, -90f * deltaTime));
		}

		VirtualReality.update(Gdx.graphics.getDeltaTime());
		VirtualReality.renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO: resize VirtualReality
	}

	@Override
	public void frameStarted() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void frameEnded() {
	}

	@Override
	public void render(Camera camera) {
		modelBatch.begin(camera);
		modelBatch.render(ground, environment);
		modelBatch.render(modelInstance, environment);
		modelBatch.end();
	}
}

package com.badlogic.gdx.vr.simpleroom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.vr.VirtualRealityRenderListener;

public class SimpleRoom extends ApplicationAdapter implements VirtualRealityRenderListener {

	private ModelBatch modelBatch;
	private PerspectiveCamera camera;
	private ModelInstance modelInstance;
	private ModelInstance ground;
	private AssetManager assets;
	private CameraInputController cameraController;
	private Viewport viewport;
	private Environment environment;

	@Override
	public void create() {
		assets = new AssetManager();
		assets.load("BrickHouse.g3db", Model.class);
		assets.finishLoading();
		modelInstance = new ModelInstance(assets.get("BrickHouse.g3db", Model.class), new Matrix4().setToScaling(0.01f, 0.01f, 0.01f));
		modelBatch = new ModelBatch();
		camera = new PerspectiveCamera();
		cameraController = new CameraInputController(camera);
		camera.position.set(10, 10, 10);
		camera.lookAt(0, 0, 0);
		Gdx.input.setInputProcessor(cameraController);
		viewport = new ScreenViewport(camera);
		ModelBuilder builder = new ModelBuilder();
		float groundSize = 1000f;
		ground = new ModelInstance(builder.createRect(-groundSize, 0, groundSize, groundSize, 0, groundSize, groundSize, 0, -groundSize, -groundSize, 0, -groundSize, 0,
				1, 0, new Material(), Usage.Position | Usage.Normal), new Matrix4().setToTranslation(0, -0.01f, 0));
		// modelInstance = new ModelInstance(builder.createBox(1, 1, 1, new
		// Material(), Usage.Position | Usage.Normal), new Matrix4());
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	@Override
	public void render() {
		cameraController.update();
		camera.update(true);

		// VirtualReality.renderer.

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
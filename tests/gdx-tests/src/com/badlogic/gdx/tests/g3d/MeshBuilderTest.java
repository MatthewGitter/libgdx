
package com.badlogic.gdx.tests.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StringBuilder;

/** @author Xoppa */
public class MeshBuilderTest extends BaseG3dHudTest {
	Model model;
	Environment environment;

	@Override
	public void create () {
		super.create();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f));

		modelsWindow.setVisible(false);

		Texture texture = new Texture(Gdx.files.internal("data/badlogic.jpg"));

		Material material = new Material(TextureAttribute.createDiffuse(texture));

		MeshBuilder meshBuilder = new MeshBuilder();
		meshBuilder.begin(Usage.Position | Usage.Normal | Usage.TextureCoordinates, GL20.GL_TRIANGLES);
		meshBuilder.box(1f, 1f, 1f);
		Mesh mesh = new Mesh(true, meshBuilder.getNumVertices(), meshBuilder.getNumIndices(), new VertexAttributes(
			VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0)));
		mesh = meshBuilder.end(mesh);
		
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		modelBuilder.manage(texture);

		modelBuilder.node().id = "mesh";
		modelBuilder.part("mesh", mesh, GL20.GL_TRIANGLES, material);
		
		modelBuilder.node().id = "box";
		MeshPartBuilder mpb = modelBuilder.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates
			| Usage.ColorPacked, material);
		mpb.setColor(Color.RED);
		mpb.box(1f, 1f, 1f);

		modelBuilder.node().id = "sphere";
		mpb = modelBuilder.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates
			| Usage.ColorPacked, material);
		mpb.sphere(2f, 2f, 2f, 10, 5);

		modelBuilder.node().id = "cone";
		mpb = modelBuilder.part("cone", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates
			| Usage.ColorPacked, material);
		mpb.setVertexTransform(new Matrix4().rotate(Vector3.X, -45f));
		mpb.cone(2f, 3f, 1f, 8);

		modelBuilder.node().id = "cylinder";
		mpb = modelBuilder.part("cylinder", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates
			| Usage.ColorPacked, material);
		mpb.setUVRange(1f, 1f, 0f, 0f);
		mpb.cylinder(2f, 4f, 3f, 15);

		model = modelBuilder.end();

		instances.add(new ModelInstance(model, new Matrix4().trn(0f, 0f, 0f), "mesh", true));
		instances.add(new ModelInstance(model, new Matrix4().trn(-5f, 0f, -5f), "box", true));
		instances.add(new ModelInstance(model, new Matrix4().trn(5f, 0f, -5f), "sphere", true));
		instances.add(new ModelInstance(model, new Matrix4().trn(-5f, 0f, 5f), "cone", true));
		instances.add(new ModelInstance(model, new Matrix4().trn(5f, 0f, 5f), "cylinder", true));
	}

	@Override
	protected void render (ModelBatch batch, Array<ModelInstance> instances) {
		batch.render(instances, environment);
	}

	@Override
	protected void onModelClicked (String name) {
	}

	@Override
	public void dispose () {
		super.dispose();
		model.dispose();
	}
}

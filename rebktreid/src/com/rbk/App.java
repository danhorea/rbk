package com.rbk;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class App extends Application {

	Group world = new Group();
	Group axisGroup = new Group();
	  double anchorX, anchorY, anchorAngle;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		buildAxes();
		Scene scene = new Scene(world);
		scene.setCamera(new PerspectiveCamera(false));
		primaryStage.setScene(scene);
		primaryStage.show();
		
	    scene.setOnMousePressed(event -> {                
	        anchorX = event.getSceneX();                
	        anchorY = event.getSceneY();                
	        anchorAngle = world.getRotate();        
	      });         
	        
	      scene.setOnMouseDragged(event -> {                
	    	  world.setRotate(anchorAngle + anchorX 
	                         - event.getSceneX());        
	      });  
	}

	private void buildAxes() {
		System.out.println("buildAxes()");
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);

		final Box xAxis = new Box(480.0, 3, 3);
		final Box yAxis = new Box(3, 480.0, 3);
		final Box zAxis = new Box(3, 3, 480.0);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		world.getChildren().addAll(axisGroup);
		
		
	}

}

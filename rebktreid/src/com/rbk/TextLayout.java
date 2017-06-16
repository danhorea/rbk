package com.rbk;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage; 

public class TextLayout extends Application {
     
  double anchorX, anchorY, anchorAngle;
 
  public static void main(String[] args) {        
	  launch(args);    
  }     
    
  @Override    
  public void start(Stage primaryStage) {        
    primaryStage.setTitle("SphereAndBox");     
      
	final PhongMaterial redMaterial = new PhongMaterial();
	redMaterial.setDiffuseColor(Color.DARKRED);
	redMaterial.setSpecularColor(Color.RED);

	final PhongMaterial greenMaterial = new PhongMaterial();
	greenMaterial.setDiffuseColor(Color.DARKGREEN);
	greenMaterial.setSpecularColor(Color.GREEN);

	final PhongMaterial blueMaterial = new PhongMaterial();
	blueMaterial.setDiffuseColor(Color.DARKBLUE);
	blueMaterial.setSpecularColor(Color.BLUE);

	final Box xAxis = new Box(240.0, 3, 3);
	final Box yAxis = new Box(3, 240.0, 3);
	final Box zAxis = new Box(3, 3, 240.0);

	xAxis.setMaterial(redMaterial);
	yAxis.setMaterial(greenMaterial);
	zAxis.setMaterial(blueMaterial);
	
    final Group parent = new Group(xAxis, yAxis, zAxis);        
    parent.setTranslateZ(500);        
    parent.setRotationAxis(Rotate.Y_AXIS);   
      
    final Group root = new Group(parent); 
    final Scene scene = 
          new Scene(root, 500, 500, true);
      
    scene.setOnMousePressed(event -> {                
      anchorX = event.getSceneX();                
      anchorY = event.getSceneY();                
      anchorAngle = parent.getRotate();        
    });         
      
    scene.setOnMouseDragged(event -> {                
      parent.setRotate(anchorAngle + anchorX 
                       - event.getSceneX());        
    });         
      
    PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);        
    pointLight.setTranslateX(15);        
    pointLight.setTranslateY(-10);        
    pointLight.setTranslateZ(-100);
      
    root.getChildren().add(pointLight);
      
    primaryStage.setScene(scene);   
    PerspectiveCamera perspectiveCamera = new PerspectiveCamera(false);
    scene.setCamera(perspectiveCamera);  
    primaryStage.show();    
  }
}
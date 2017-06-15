/*
 * Copyright (c) 2013, Pro JavaFX Authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JFXtras nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * EarthCylinder.fx - A simple example of JavaFX 3D
 *
 *  Developed 2013 by James L. Weaver jim.weaver [at] javafxpert.com
 *  as a JavaFX 8 example for the Pro JavaFX 8 book.
 */

package com.test;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage; 

public class EarthCylinder extends Application {
     
  double anchorX, anchorY;
  
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;
  
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  
  PerspectiveCamera scenePerspectiveCamera = new PerspectiveCamera(false);
  
  /**     
   * @param args the command line arguments     
   */    
  public static void main(String[] args) {        
    // Remove this line once dirtyopts bug is fixed for 3D primitive        
    System.setProperty("prism.dirtyopts", "false");        
    launch(args);    
  }     
    
  @Override    
  public void start(Stage primaryStage) {        
    primaryStage.setTitle("EarthCylinder");     

    Image diffuseMap = 
      new Image(EarthCylinder.class
        .getResource("earth-mercator.jpg")
        .toExternalForm());

    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(diffuseMap);

    final Cylinder earth = new Cylinder (200, 400);        
    earth.setMaterial(earthMaterial); 
          
    final Group parent = new Group(earth);
    parent.setTranslateX(450);
    parent.setTranslateY(450);
    parent.setTranslateZ(0);

    Rotate xRotate;
    Rotate yRotate;
    parent.getTransforms().setAll(
      xRotate = new Rotate(0, Rotate.X_AXIS),
      yRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    xRotate.angleProperty().bind(angleX);
    yRotate.angleProperty().bind(angleY);
    
    final Group root = new Group();
    root.getChildren().add(parent);
    
    final Scene scene = new Scene(root, 900, 900, true);
    scene.setFill(Color.BLACK);
      
    scene.setOnMousePressed(event -> {                
      anchorX = event.getSceneX();                
      anchorY = event.getSceneY();  
      
      anchorAngleX = angleX.get();
      anchorAngleY = angleY.get();
    });         
      
    scene.setOnMouseDragged(event -> {                
      angleX.set(anchorAngleX - (anchorY -  event.getSceneY()));
      angleY.set(anchorAngleY + anchorX -  event.getSceneX());
    });         
      
    PointLight pointLight = new PointLight(Color.WHITE);
    pointLight.setTranslateX(400);
    pointLight.setTranslateY(400);
    pointLight.setTranslateZ(-3000);
      
    scene.setCamera(scenePerspectiveCamera);
    
    root.getChildren().addAll(pointLight, scenePerspectiveCamera);
      
    primaryStage.setScene(scene);     
    primaryStage.show();    
  }
}
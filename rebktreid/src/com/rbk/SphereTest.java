package com.rbk;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SphereTest extends Application{
	
	public static void main(String[] args) {
		launch();
	}
	
	DoubleProperty translateX = new SimpleDoubleProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {
		Sphere sphere = new Sphere(100);
		Group root = new Group(sphere);
		root.setTranslateX(320);
		root.setTranslateY(240);
		Scene scene = new Scene(root, 640, 480);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		sphere.translateXProperty().bind(translateX);
		animate();
		
	}
	
	private void animate() {
		Timeline timeline = new Timeline(
		new KeyFrame(Duration.seconds(0), new KeyValue(translateX, -320)),
		new KeyFrame(Duration.seconds(2), new KeyValue(translateX, 320)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		}

}

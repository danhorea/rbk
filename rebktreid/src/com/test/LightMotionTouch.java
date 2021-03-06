/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.test;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.ByteBuffer;

public class LightMotionTouch extends Application {
    Timeline stationLightTL;
    Timeline movingLightTL;
    RotateTransition rotTrans;

    public static void main(String[] args) {
        System.setProperty("prism.dirtyopts", "false");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Color light1Color = Color.PALEGOLDENROD;
        final Color light2Color = Color.ORANGERED;

        Cylinder cylinder = new Cylinder(150,300);
        cylinder.setTranslateX(400);
        cylinder.setTranslateY(350);
        cylinder.setTranslateZ(50);
        cylinder.setOnMouseClicked(e -> {
            if (rotTrans.getStatus() == Timeline.Status.RUNNING) {
                rotTrans.pause();
            } else {
                rotTrans.play();
            }
          }
        );

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.rgb(30, 30, 30));
        cylinder.setMaterial(material);

        // Create first light group and light
        final VisibleLight lightGroup1 = new VisibleLight(light1Color);
        lightGroup1.setTranslateX(700);
        lightGroup1.setTranslateY(200);
        lightGroup1.setTranslateZ(-200);
        lightGroup1.setOnMouseClicked(e -> {
            if (stationLightTL.getStatus() == Timeline.Status.RUNNING) {
                stationLightTL.pause();
            } else {
                stationLightTL.play();
            }
          }
        );

      // Animate the color of the first light
      DoubleProperty t = new SimpleDoubleProperty(0);
      t.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> observable,
                                      Number oldValue, Number newValue) {

          double t = newValue.doubleValue();
          Color c = Color.WHITE.interpolate(Color.GREEN, t);
          lightGroup1.setColor(c);
        }
      });

      KeyValue kv1 = new KeyValue(t, 1.0, Interpolator.EASE_BOTH);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(2), kv1);
        stationLightTL = new Timeline(kf1);
        stationLightTL.setAutoReverse(true);
        stationLightTL.setCycleCount(Timeline.INDEFINITE);

        // Create second light group and light
        final VisibleLight lightGroup2 = new VisibleLight(light2Color);
        lightGroup2.setTranslateX(-300);
        lightGroup2.setTranslateY(300);
        lightGroup2.setTranslateZ(-100);
        lightGroup2.setOnMouseClicked(e -> {
            if (movingLightTL.getStatus() == Timeline.Status.RUNNING) {
                movingLightTL.pause();
            } else {
                movingLightTL.play();
            }
          }
        );

        // Animate the position of the second light
        final Group movingLight = new Group(lightGroup2);
        movingLight.setTranslateX(400);
        final Rotate rot = new Rotate(0, Rotate.Y_AXIS);
        movingLight.getTransforms().add(rot);
        KeyValue kv = new KeyValue(rot.angleProperty(), 360.0);
        KeyFrame kf = new KeyFrame(Duration.seconds(7), kv);
        movingLightTL = new Timeline(kf);
        movingLightTL.setCycleCount(Timeline.INDEFINITE);

        rotTrans = new RotateTransition(Duration.seconds(35), cylinder);
        rotTrans.setAutoReverse(true);
        rotTrans.setAxis(new Point3D(2, 1, 0).normalize());
        rotTrans.setInterpolator(Interpolator.EASE_BOTH);
        rotTrans.setCycleCount(Timeline.INDEFINITE);
        rotTrans.setByAngle(360);
        
        Group root = new Group();

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-10);
        root.getChildren()
          .addAll(cylinder, lightGroup1, movingLight, camera);
        Scene scene = new Scene(root, 800, 700, true);
        scene.setCamera(camera);
        scene.setFill(Color.rgb(10, 10, 40));

        scene.setOnKeyTyped((KeyEvent e) -> {
            switch (e.getCharacter()) {
                case "[":
                    if (stationLightTL.getStatus() == Timeline.Status.RUNNING) {
                        stationLightTL.pause();
                    } else {
                        stationLightTL.play();
                    }
                    break;
                case "]":
                    if (movingLightTL.getStatus() == Timeline.Status.RUNNING) {
                        movingLightTL.pause();
                    } else {
                        movingLightTL.play();
                    }
                    break;
                case "\\":
                    if (rotTrans.getStatus() == Timeline.Status.RUNNING) {
                        rotTrans.pause();
                    } else {
                        rotTrans.play();
                    }
                    break;
            }
        });
        stage.setScene(scene);
        stage.setTitle("Light Motion Touch");
        stage.show();
    }

    private class VisibleLight extends Group {
        private PhongMaterial material;
        private PointLight pointLight;

        private void makeConstantImage(WritableImage img, Color color) {
            int w = (int)img.getWidth();
            int h = (int)img.getHeight();
            ByteBuffer scanline = ByteBuffer.allocate(3*w);
            byte r = (byte)((int) Math.round(color.getRed() * 255.0));
            byte g = (byte)((int) Math.round(color.getGreen() * 255.0));
            byte b = (byte)((int) Math.round(color.getBlue() * 255.0));
            for (int i = 0; i < w; i++) {
                scanline.put(r);
                scanline.put(g);
                scanline.put(b);
            }
            scanline.rewind();
            img.getPixelWriter().setPixels(0, 0, w, w, PixelFormat.getByteRgbInstance(), scanline, 0);
        }

        VisibleLight(Color color) {
            pointLight = new PointLight();
            material = new PhongMaterial(Color.WHITE);
            material.setDiffuseMap(null);
            material.setBumpMap(null);
            material.setSpecularMap(null);
            material.setSpecularColor(Color.TRANSPARENT);
            setColor(color);

            final Sphere light = new Sphere(50);
            light.setScaleX(0.2);
            light.setScaleY(0.2);
            light.setScaleZ(0.2);
            light.setMaterial(material);

            this.getChildren().addAll(pointLight, light);
        }

        final void setColor(Color color) {
            pointLight.setColor(color);
            WritableImage selfIllumMap = new WritableImage(64, 64);
            makeConstantImage(selfIllumMap, color);
            material.setSelfIlluminationMap(selfIllumMap);
        }
    }
}

package ru.hse.app.domain;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import ru.hse.app.config.AppProperties;

public class CoordSystem extends Group {

    private CoordAxe axeOX;
    private CoordAxe axeOY;
    private PerspectiveCamera camera;
    private AppProperties appProperties = AppProperties.getInstance();
    private double cameraStrartZ = appProperties.getWindowHeight() * 0.35;

    public CoordSystem() {
        initialize();
    }

    private void initialize() {
        createAxes();
        createCamera();
        bindAxesToCamera();
    }

    private void createAxes() {
        axeOX = new CoordAxe(-1 * appProperties.getWindowWidth() / 2, 0,
                appProperties.getWindowWidth() / 2, 0, "X");
        axeOY = new CoordAxe(0, -1 * appProperties.getWindowHeight() * 0.35, 0,
                appProperties.getWindowHeight() * 0.35, "Y");
        axeOX.getLine().setStroke(Paint.valueOf("BLACK"));
        axeOY.getLine().setStroke(Paint.valueOf("BLACK"));
        axeOX.getLabel().rotationAxisProperty().bind(super.rotationAxisProperty());
        axeOX.getLabel().rotateProperty().bind(super.rotateProperty());
        axeOY.getLabel().rotationAxisProperty().bind(super.rotationAxisProperty());
        axeOY.getLabel().rotateProperty().bind(super.rotateProperty());

        super.getChildren().addAll(axeOX, axeOY);
        super.setTranslateX(appProperties.getWindowWidth() / 2);
        super.setTranslateY(appProperties.getWindowHeight() / 2);
        super.setRotationAxis(new Point3D(1, 0, 0));
        super.setRotate(180);
    }

    private void createCamera() {
        camera = new PerspectiveCamera(true);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(cameraStrartZ);
        camera.setFarClip(appProperties.getFarClip());
        camera.rotationAxisProperty().bind(super.rotationAxisProperty());
        camera.rotateProperty().bind(super.rotateProperty());
        camera.setFieldOfView(appProperties.getCameraAngle());

        Group cameraGroup = new Group();
        cameraGroup.getChildren().add(camera);

        super.getChildren().add(cameraGroup);
    }

    private void bindAxesToCamera() {
        axeOY.getLine().endYProperty().bind(camera.translateZProperty());
        axeOY.getLine().startYProperty().bind(camera.translateZProperty().negate());
        axeOX.getLine().endXProperty().bind(camera.translateZProperty()
                .multiply(appProperties.getWindowWidth() / 2)
                .divide(cameraStrartZ));
        axeOX.getLine().startXProperty().bind(camera.translateZProperty()
                .multiply(appProperties.getWindowWidth() / 2)
                .divide(cameraStrartZ)
                .negate());
        axeOX.getLine().strokeWidthProperty().bind(camera.translateZProperty().divide(100).add(1));
        axeOY.getLine().strokeWidthProperty().bind(camera.translateZProperty().divide(100).add(1));
        camera.translateZProperty().addListener(e -> {
            axeOX.getLabel().setFont(Font.font(camera.getTranslateZ() / 12));
            axeOY.getLabel().setFont(Font.font(camera.getTranslateZ() / 12));
        });
        axeOX.getLabel().translateXProperty().bind(axeOX.getLine().endXProperty()
                .subtract(camera.translateZProperty()
                        .multiply(20)
                        .divide(cameraStrartZ)));
        axeOX.getLabel().translateYProperty().bind(axeOX.getLine().endYProperty()
                .add(camera.translateZProperty()
                        .multiply(10)
                        .divide(cameraStrartZ)));
        axeOY.getLabel().translateXProperty().bind(axeOY.getLine().endXProperty()
                .add(camera.translateZProperty()
                        .multiply(15)
                        .divide(cameraStrartZ)));
        axeOY.getLabel().translateYProperty().bind(axeOY.getLine().endYProperty()
                .subtract(camera.translateZProperty()
                        .multiply(30)
                        .divide(cameraStrartZ)));
    }

    public CoordAxe getAxeOX() {
        return axeOX;
    }

    public CoordAxe getAxeOY() {
        return axeOY;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

}

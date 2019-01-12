package ru.hse.app.domain;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Paint;
import ru.hse.app.config.AppProperties;

public class CoordSystem extends Group {

    private CoordAxe axeOX;
    private CoordAxe axeOY;
    private PerspectiveCamera camera;
    private AppProperties appProperties = AppProperties.getInstance();

    public CoordSystem() {
        initialize();
    }

    private void initialize() {
        createAxes();
        createCamera();
    }

    private void createAxes() {
        axeOX = new CoordAxe(-1 * appProperties.getWindowWidth() / 2, 0,
                appProperties.getWindowWidth() / 2, 0, "X");
        axeOY = new CoordAxe(0, -1 * appProperties.getWindowHeight() * 0.35, 0,
                appProperties.getWindowHeight() * 0.35, "Y");
        axeOX.getLine().setStroke(Paint.valueOf("BLACK"));
        axeOY.getLine().setStroke(Paint.valueOf("BLACK"));
        axeOX.getLine().setStrokeWidth(3);
        axeOY.getLine().setStrokeWidth(3);
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
        camera.setTranslateZ(appProperties.getWindowHeight() * 0.35);
        camera.setFarClip(appProperties.getFarClip());
        camera.rotationAxisProperty().bind(super.rotationAxisProperty());
        camera.rotateProperty().bind(super.rotateProperty());
        camera.setFieldOfView(appProperties.getCameraAngle());

        Group cameraGroup = new Group();
        cameraGroup.getChildren().add(camera);

        super.getChildren().add(cameraGroup);
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

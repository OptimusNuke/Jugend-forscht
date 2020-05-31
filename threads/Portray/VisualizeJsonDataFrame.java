package threads.Portray;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Caches.PositionExpressionLayerCache;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import threads.Communication.PCToPiThread;

@Deprecated
public class VisualizeJsonDataFrame extends Application {

	public static void launchJsonVisualisation(String[] args) {
		launch(args);
	} 
	
	PerspectiveCamera camera;
	Group boxGroup = new Group();
	
	Label timeStampLabel;
	private final String CTS_TEXT = "Current TimeStamp: ";
	
	private boolean firstRun = true;
	private double xAngle = 0;
	private double yAngle = 0;
	private double zAngle = 0;
	
	@SuppressWarnings("incomplete-switch")
	@Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        
        /*
         * Controls: From standart Position, 
         * Use WASD to fly left (A), right (D), forward (W) and backward (S),
         * 
         * K to go down, I to go up with the camera.
         * 
         * Use the arrows to rotate the field of view:
         * To Turn the camera clockwise use J, to turn the camera counterclockwise, use F.
         * 
         * To turn the camera up, use the arrow up, to turn the camera down, use the arrow down.
         * 
         * To turn the camera to the left, use the arrow to the left; 
         * to turn the camera to the right, use the arrow to the right.
         * 
         * To zoom in (narrow field of view) use +,
         * to zoom out (wider field of view) use -.
         */

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
            case D:
                camera.translateZProperty().set(camera.getTranslateZ() + 0.5);
                break;
            case A:
                camera.translateZProperty().set(camera.getTranslateZ() - 0.5);
                break;
            case V:
                camera.translateYProperty().set(camera.getTranslateY() + 0.5);
                break;
            case SPACE:
                camera.translateYProperty().set(camera.getTranslateY() - 0.5);
                break;
            case S:
                camera.translateXProperty().set(camera.getTranslateX() + 0.5);
                break;
            case W:
            	
            	camera.translateXProperty().set(camera.getTranslateX() - 0.5);
//            	Platform.runLater(() -> camera.translateXProperty().set(camera.getTranslateX() - 0.5)); 
//            					0, 
            	//Math.tan(Math.toRadians(zAngle)) * 0.5);
//            	Platform.runLater(() -> camera.translateXProperty().set(camera.getTranslateX() - 0.5));
//                Platform.runLater(() -> camera.translateZProperty().set(Math.tan(Math.toRadians(zAngle)) * 0.5)); //camera.getTranslateZ() - 0.5);
                break;
                
            case LEFT:
            	camera.getTransforms().addAll(//new Translate(0, 0, 0),
            			new Rotate(-2, Rotate.Y_AXIS), 
            			new Translate(0, 0, 0));
            	Platform.runLater(() -> yAngle += -2);
            	break;
            case RIGHT:
            	camera.getTransforms().addAll(//new Translate(0, 0, 0),
            			new Rotate(2, Rotate.Y_AXIS),
            			new Translate(0, 0, 0));
            	Platform.runLater(() -> yAngle += 2);
            	break;
            case UP:
            	camera.getTransforms().addAll(//new Translate(0, 0, 0),
            			new Rotate(2, Rotate.X_AXIS),
            			new Translate(0, 0, 0));
            	Platform.runLater(() -> xAngle += 2);
            	break;
            case DOWN:
            	camera.getTransforms().addAll(//new Translate(0, 0, 0),
            			new Rotate(-2, Rotate.X_AXIS),
            			new Translate(0, 0, 0));
            	Platform.runLater(() -> xAngle += -2);
            	break;
            case J:
            	camera.getTransforms().addAll(new Translate(0, 0, 0),
            			new Rotate(1, Rotate.Z_AXIS),
            			new Translate(0, 0, 0));
            	Platform.runLater(() -> zAngle += 1);
            	break;
            case F:
            	camera.getTransforms().addAll(new Translate(0, 0, 0),
            			new Rotate(-1, Rotate.Z_AXIS),
            			new Translate(0, 0, 0));
            	Platform.runLater(() -> zAngle += -1);
            	break;
            	
            case PLUS:
            	camera.setFieldOfView(camera.getFieldOfView() - 0.5);
            	break;
            case MINUS:
            	camera.setFieldOfView(camera.getFieldOfView() + 0.5);
            }
        });
        
        Scene scene = new Scene(createContent());
        
        primaryStage.setTitle("Visualize json data");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
       
        
        PCToPiThread.currentTimeStamp.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
    			if("CurrentTimeStampChanged".equals(event.getPropertyName())) {
    				int timeStamp = PCToPiThread.currentTimeStamp.getTimeStamp();
    				
    				Platform.runLater(() -> {
						timeStampLabel.setText(CTS_TEXT + timeStamp);
						try {	//Just to make sure the timeStamps are added to the json
							if(PositionExpressionLayerCache.jsonPositionLayer.get("TimeStamp" + timeStamp) == null) {
//								Thread.sleep(1000);
							}
							
							addAllBoxes(timeStamp);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				});
    			}
    		}
		});
    }
	
	public Parent createContent() throws Exception {	 
		Group root = new Group(); 
		
		// Create a lighting
//		PointLight light = new PointLight();
//		light.getTransforms().addAll(
//				new Rotate(-90, Rotate.Y_AXIS),
//				new Translate(0, 0, -20));
//		root.getChildren().add(light);
		
		
		// Create and add a baseLayer
		Box baseLayer = new Box(0.25,
								5,
								5);
	    baseLayer.setMaterial(new PhongMaterial(Color.BLUE));
//	    baseLayer.setDrawMode(DrawMode.FILL);
	    
	    root.getChildren().add(baseLayer);
        
//	    addAllBoxes();
	    
	    root.getChildren().add(boxGroup);
        // Create and add a position camera
        camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
        		new Translate(32, 1.5, 9),
        		new Rotate(242, Rotate.Y_AXIS), 
        		new Rotate(3, Rotate.X_AXIS),
                new Rotate(0, Rotate.Z_AXIS));       
 
        root.getChildren().add(camera);
        
        // Use a SubScene       
        SubScene subScene = new SubScene(root, 1000, 800);
        subScene.setFill(Color.DARKGREY);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);
        
        timeStampLabel = new Label(CTS_TEXT + "0");
        
        StackPane stackpane = new StackPane();
        StackPane.setAlignment(timeStampLabel, Pos.TOP_LEFT);
        stackpane.getChildren().add(timeStampLabel);
        
        group.getChildren().add(stackpane);
        
        return group;
    }
	
	Lock lock = new ReentrantLock();
	int distanceFromJson = 0;
	
	private void addAllBoxes(int timeStamp) throws InterruptedException {
		for(int position = 0; position <= PositionExpressionLayerCache.jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().size() - 1; position++) {
//			for(int counter = 0; PositionExpressionLayerCache.jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + (position + 1)) == null && counter <= 10; counter++) {
//				Thread.sleep(500);
////				System.out.println("WARTE BEI 1");
//			}
//			for(int counter = 0; !PositionExpressionLayerCache.jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + (position + 1)).getAsJsonObject().has("Distance") && counter <= 10; counter++) {
//				Thread.sleep(100);
////				System.out.println("2");
//			}
//			System.out.println(PositionExpressionLayerCache.jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + (position + 1)).getAsJsonObject().get("Distance").getAsInt());
			distanceFromJson = PositionExpressionLayerCache.jsonPositionLayer.get("TimeStamp" + timeStamp).getAsJsonObject().get("Position " + (position + 1)).getAsJsonObject().get("Distance").getAsInt();
			addNewBox(distanceFromJson, position);
		}
	}
	
	private Box box;
	private double DISTANCE_DIVISION_FACTOR = 250d;
	private double MAX_ANGLE_AT_PERSON = 0d;
	private double rotation = 0;
	private int row = 0;
	private int column = 0;
	private Color color;
	
	private void addNewBox(int distance, int positionNumber) {
		column = (int) Math.ceil(positionNumber / 5);
		row = positionNumber % 5;
		
		color = setColor(distance);
		
		rotation = setRotation();
		
		if(!firstRun) {
			box = new Box(distance / DISTANCE_DIVISION_FACTOR,
					1,
					1);
			box.setMaterial(new PhongMaterial(color));
			box.getTransforms().addAll(
					new Rotate(rotation, Rotate.Y_AXIS),
					new Translate((distance / DISTANCE_DIVISION_FACTOR) / 2 + 0.125, (column - 2) * 1.1, (row - 2) * 1.1)	//Erste nach rechts, zweite nach unten, dritte nach hinten
					);
			boxGroup.getChildren().remove(positionNumber);
			boxGroup.getChildren().add(positionNumber, box);
		} else {
			box = new Box(distance / DISTANCE_DIVISION_FACTOR,
					1,
					1);
			box.setMaterial(new PhongMaterial(color));
			box.getTransforms().addAll(		//		column - 2
					new Rotate(rotation, Rotate.Y_AXIS),
					new Translate((distance / DISTANCE_DIVISION_FACTOR) / 2 + 0.125, (column - 2) * 1.1, (row - 2) * 1.1)	//Erste nach rechts, zweite nach unten, dritte nach hinten
					);
			boxGroup.getChildren().add(positionNumber, box);
		}
		
		if(firstRun && positionNumber == 24) {
			firstRun = false;
		}
	}
	
	private Color setColor(int distance) {
		if(distance == 500) {
			return Color.rgb(255, 0, 0);
		} else if(distance >= 501 && distance <= 2000) {
			return Color.rgb(255,
					(int) Math.ceil(((distance - 501d) / 1499d) * 255d),
					0);
		} else if(distance >= 2001 && distance <= 3500) {
			return Color.rgb(255 - (int) Math.ceil(((distance - 2001d) / 1499d) * 255d),
					255,
					0);
		} else {
			return Color.WHITE;
		}
	}
	
	private double setRotation() {
		if(row == 0) {
			return MAX_ANGLE_AT_PERSON;
		} else if(row == 1) {
			return MAX_ANGLE_AT_PERSON * 1d / 2d;
		} else if(row == 2) {
			return 0;
		} else if(row == 3) {
			return MAX_ANGLE_AT_PERSON * -1d / 2d;
		} else if(row == 4) {
			return -MAX_ANGLE_AT_PERSON;
		} else {
			return 0d;
		}
	}
}

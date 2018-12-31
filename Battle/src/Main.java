import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

interface SquareMode{	//a space of NxM
	final int N=10;
	final int M=13;
}

public class Main extends Application implements SquareMode{
	
	protected static FlowPane flowPane;
	
	static {//set the background and filling
		BackgroundImage image= new BackgroundImage(new Image("source/background.PNG",1000,1000,false,false),null,null,null,null);
		flowPane= new FlowPane(0,0);
		flowPane.setBackground(new Background(image));
		flowPane.setPrefSize(60*M, 60*N);
		flowPane.setMaxSize(60*M, 60*N);
		flowPane.setMinSize(60*M, 60*N);
		flowPane.setAlignment(Pos.TOP_LEFT);
		for(int i = 0;i<N;i++) {
			 for(int j = 0;j<M;j++) {
				 flowPane.getChildren().add(new ImageView(new Image("source/blank.PNG",60,60,false,false)));
			 }
		 } 
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Creature.prepare();					//prepare for battle
		
	    Scene scene=new Scene(flowPane,60*M,60*N);
	    scene.setOnKeyReleased(e-> {		//start the battle
	    	if(e.getCode()==KeyCode.SPACE) {
	    		System.out.println("start");
	    		Creature.startBattle();
	    	}

	    	else if(e.getCode()==KeyCode.L) {
	    		System.out.println("»Ø·Å£¿");
	    	}
	    });
	    scene.setFill(null);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		
    }
	public static void main(String args[])
	{ 
		launch(args);

	}
}

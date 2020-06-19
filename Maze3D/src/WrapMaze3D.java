
import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;


public class WrapMaze3D extends JPanel
{
  private static final int PWIDTH = 512;   // size of panel
  private static final int PHEIGHT = 512; 

  private static final int BOUNDSIZE = 100; 
  private static final String SKY_TEX = "images/sky.jpg";  

  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   

  private MazeManager mazeMan;     
  private TransformGroup camera2TG; 

  static double fov = 90.0; 

  public WrapMaze3D(MazeManager mm, BirdsEye be)
  // construct the scene and the main camera
  {
    mazeMan = mm;       
    

    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true); 
    canvas3D.requestFocus();
    su = new SimpleUniverse(canvas3D);

    createSceneGraph();
    prepareViewPoint(be);

    su.addBranchGraph( sceneBG );
  } // end of WrapMaze3D()


  void createSceneGraph()
  // initilise the scene 
  { 
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE); 

    lightScene();    
    addBackground(); 
    
  
    TexturedFloor floor = new TexturedFloor();
    sceneBG.addChild( floor.getBG() );

    sceneBG.addChild( mazeMan.getMaze() );  
    sceneBG.addChild( camera2TG );     
    sceneBG.compile();   
  } 



  private void lightScene()
 
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(bounds);
   
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
    
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
      

    DirectionalLight light1 = 
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 = 
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneBG.addChild(light2);
  }  

  private void addBackground()
  // Add backdrop painted onto a inward facing sphere.
  // No use made of Background.
  // Seems more reliable on some older machines (?) 
  {
    System.out.println("Loading sky texture: " + SKY_TEX);
    TextureLoader tex = new TextureLoader(SKY_TEX, null);

    // create an appearance and assign the texture
    Appearance app =  new Appearance();
	app.setTexture( tex.getTexture() );

    Sphere sphere = new Sphere(100.0f,    // radius to extend to edge of scene
			   Sphere.GENERATE_NORMALS_INWARD |
			   Sphere.GENERATE_TEXTURE_COORDS, 4, app);   // default divs = 15

    sceneBG.addChild( sphere );
 } // end of addBackground()



// --------------------- user controls ----------------------------------


  private void prepareViewPoint(BirdsEye be)
  {
	  
    // adjust viewpoint parameters
    View userView = su.getViewer().getView();
    userView.setFieldOfView( Math.toRadians(fov));  // wider FOV
    // 10 and 0.1; keep ratio between 100-1000
    userView.setBackClipDistance(20);      // can see a long way
    userView.setFrontClipDistance(0.05);   // can see close things

    ViewingPlatform vp = su.getViewingPlatform();

    // add a spotlight and avatar to viewpoint
    PlatformGeometry pg = new PlatformGeometry();
    pg.addChild( makeSpot() );
    // pg.addChild( makeAvatar() );    // avatar not used here
    vp.setPlatformGeometry( pg );

    // fix starting position and orientation of viewpoint
    TransformGroup steerTG = vp.getViewPlatformTransform();
    initViewPosition(steerTG);

    // set up keyboard controls
    KeyBehavior keybeh = new KeyBehavior(mazeMan, be, camera2TG);
    keybeh.setSchedulingBounds(bounds);
    vp.setViewPlatformBehavior(keybeh);
  } // end of prepareViewPoint()


  private void initViewPosition(TransformGroup steerTG)
  // rotate and move the viewpoint
  {
    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);
    Transform3D toRot = new Transform3D();
    toRot.rotY(-Math.PI);   
    // rotate 180 degrees around Y-axis, so facing along positive z-axis

    t3d.mul(toRot);
	t3d.setTranslation( mazeMan.getMazeStartPosn() );  // place at maze start
    steerTG.setTransform(t3d); 
  }  // end of initViewPosition()


  private SpotLight makeSpot()

  {
    SpotLight spot = new SpotLight();
    spot.setPosition(0.0f, 0.5f, 0.0f);      
    spot.setAttenuation(0.0f, 1.2f, 0.0f);  
    spot.setSpreadAngle( (float)Math.toRadians(30.0));  
    spot.setConcentration(5.0f);           
    spot.setInfluencingBounds(bounds);
    return spot;
  } 

public static double getFov(){
	return fov;
}
 public static void setFov(double f){
	 fov = f;
 }

} 


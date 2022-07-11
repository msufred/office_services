package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class DashboardIcon extends SVGIcon {
    
    private static final double ratio = 1.2971;
    
    public DashboardIcon() {
        super();
    }
    
    public DashboardIcon(double size) {
        super(size, size);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M197.332,0h-160C16.746,0,0,16.746,0,37.332v96c0,20.59,16.746,37.336,37.332,37.336h160\n" +
"	c20.59,0,37.336-16.746,37.336-37.336v-96C234.668,16.746,217.922,0,197.332,0z M197.332,213.332h-160\n" +
"	C16.746,213.332,0,230.078,0,250.668v224C0,495.254,16.746,512,37.332,512h160c20.59,0,37.336-16.746,37.336-37.332v-224\n" +
"	C234.668,230.078,217.922,213.332,197.332,213.332z M474.668,341.332h-160c-20.59,0-37.336,16.746-37.336,37.336v96\n" +
"	c0,20.586,16.746,37.332,37.336,37.332h160C495.254,512,512,495.254,512,474.668v-96C512,358.078,495.254,341.332,474.668,341.332z\n" +
"	 M474.668,0h-160c-20.59,0-37.336,16.746-37.336,37.332v224c0,20.59,16.746,37.336,37.336,37.336h160\n" +
"	c20.586,0,37.332-16.746,37.332-37.336v-224C512,16.746,495.254,0,474.668,0z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-add6-icon";
    }

}

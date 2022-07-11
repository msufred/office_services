package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class ScheduleIcon extends SVGIcon {
    
    private static final double ratio = 1.2971;
    
    public ScheduleIcon() {
        super();
    }
    
    public ScheduleIcon(double size) {
        super(size, size);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M900,100h-50V50c0-30-20-50-50-50c-30,0-50,20-50,50v50H250V50c0-30-20-50-50-50s-50,20-50,50v50H50c-25,0-50,20-50,50\n" +
"			v700c0,30,25,50,50,50h305c-35-60-55-130-55-200c0-220,180-400,400-400c95,0,180,35,250,90V150C950,125,925,100,900,100z M700,400c-165,0-300,135-300,300s135,300,300,300s300-135,300-300S865,400,700,400z M800,750H700c-30,0-50-20-50-50V550\n" +
"			c0-30,20-50,50-50c30,0,50,20,50,50v100h50c30,0,50,20,50,50C850,730,830,750,800,750z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-add6-icon";
    }

}

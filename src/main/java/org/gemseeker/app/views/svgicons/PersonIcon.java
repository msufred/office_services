package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class PersonIcon extends SVGIcon {
    
    public PersonIcon() {
        super();
    }
    
    public PersonIcon(double size) {
        super(size);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M12,12c2.2,0,4-1.8,4-4c0-2.2-1.8-4-4-4C9.8,4,8,5.8,8,8C8,"
                + "10.2,9.8,12,12,12z M12,14c-2.7,0-8,1.3-8,4v2h16v-2C20,15.3,14.7"
                + ",14,12,14z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-person-icon";
    }

}

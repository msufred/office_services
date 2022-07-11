package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class HelpIcon2 extends SVGIcon {
    
    public HelpIcon2() {
        super();
    }
    
    public HelpIcon2(double size) {
        super(size);
    }

    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M12,2C6.5,2,2,6.5,2,12s4.5,10,10,10c5.5,0,10-4.5,10-10S17"
                + ".5,2,12,2z M13,19h-2v-2h2V19z M15.1,11.3l-0.9,0.9C13.4,12.9,13,"
                + "13.5,13,15h-2v-0.5c0-1.1,0.4-2.1,1.2-2.8l1.2-1.3C13.8,10.1,14,9"
                + ".6,14,9c0-1.1-0.9-2-2-2c-1.1,0-2,0.9-2,2H8c0-2.2,1.8-4,4-4c2.2,"
                + "0,4,1.8,4,4C16,9.9,15.6,10.7,15.1,11.3z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-help2-icon";
    }

}

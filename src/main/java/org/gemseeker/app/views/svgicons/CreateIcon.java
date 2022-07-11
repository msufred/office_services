package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class CreateIcon extends SVGIcon {

    public CreateIcon() {
        super();
    }
    
    public CreateIcon(double size) {
        super(size);
    }

    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M3,17.2V21h3.8L17.8,9.9l-3.8-3.8L3,17.2z M20.7,7c0.4-0.4,"
                + "0.4-1,0-1.4l-2.3-2.3c-0.4-0.4-1-0.4-1.4,0l-1.8,1.8l3.8,3.8L20.7"
                + ",7z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-create-icon";
    }

}

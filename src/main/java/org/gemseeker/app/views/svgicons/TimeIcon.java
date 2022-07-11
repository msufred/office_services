package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class TimeIcon extends SVGIcon {
    
    public TimeIcon() {
        super();
    }
    
    public TimeIcon(double size) {
        super(size, size);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("m12 0c-6.617 0-12 5.383-12 12s5.383 12 12 12 12-5.383 "
                + "12-12-5.383-12-12-12zm5.707 18.207c-.195.195-.451.293-.707.293s-"
                + ".512-.098-.707-.293l-5-5c-.188-.187-.293-.441-.293-.707v-6.5c0-"
                + ".553.448-1 1-1s1 .447 1 1v6.086l4.707 4.707c.391.391.391 1.023 "
                + "0 1.414z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-refresh-icon";
    }

}

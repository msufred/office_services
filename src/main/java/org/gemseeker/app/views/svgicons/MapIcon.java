package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class MapIcon extends SVGIcon {
    
    private static final double ratio = 1.3125;
    
    public MapIcon() {
        super();
    }
    
    public MapIcon(double size) {
        super(size, size * ratio);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M362.162,0C162.465,0,0,162.465,0,362.16c0,247.828,324.1,611.654,337.898,627.021c12.961,14.436,35.59,14.41,48.527,0\n" +
"			c13.799-15.367,337.898-379.193,337.898-627.021C724.32,162.465,561.857,0,362.162,0z M362.162,544.373\n" +
"			c-100.473,0-182.211-81.74-182.211-182.213s81.74-182.211,182.211-182.211s182.209,81.74,182.209,182.213\n" +
"			S462.633,544.373,362.162,544.373z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-add6-icon";
    }

}

package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class UpIcon extends SVGIcon {
    
    public UpIcon() {
        super();
    }
    
    public UpIcon(double size) {
        super(size * 0.75, size);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M476.723,216.64L263.305,3.115C261.299,1.109,258.59,0,255.753,0c-2.837,0-5.547,1.131-7.552,3.136L35.422,216.64\n" +
"			c-3.051,3.051-3.947,7.637-2.304,11.627c1.664,3.989,5.547,6.571,9.856,6.571h117.333v266.667c0,5.888,4.779,10.667,10.667,10.667\n" +
"			h170.667c5.888,0,10.667-4.779,10.667-10.667V234.837h116.885c4.309,0,8.192-2.603,9.856-6.592\n" +
"			C480.713,224.256,479.774,219.691,476.723,216.64z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-delete-icon";
    }

}

package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class DownIcon extends SVGIcon {
    
    public DownIcon() {
        super();
    }
    
    public DownIcon(double size) {
        super(size * 0.75, size);
    }
    
    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M479.046,283.925c-1.664-3.989-5.547-6.592-9.856-6.592H352.305V10.667C352.305,4.779,347.526,0,341.638,0H170.971\n" +
"			c-5.888,0-10.667,4.779-10.667,10.667v266.667H42.971c-4.309,0-8.192,2.603-9.856,6.571c-1.643,3.989-0.747,8.576,2.304,11.627\n" +
"			l212.8,213.504c2.005,2.005,4.715,3.136,7.552,3.136s5.547-1.131,7.552-3.115l213.419-213.504\n" +
"			C479.793,292.501,480.71,287.915,479.046,283.925z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-delete-icon";
    }

}

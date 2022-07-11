package org.gemseeker.app.views.svgicons;

import javafx.scene.shape.SVGPath;

/**
 *
 * @author gemini1991
 */
public class SearchIcon extends SVGIcon {

    public SearchIcon() {
        super();
    }
    
    public SearchIcon(double size) {
        super(size);
    }

    @Override
    protected SVGPath createIcon() {
        SVGPath path = new SVGPath();
        path.setContent("M15.5,14h-0.8l-0.3-0.3c1-1.1,1.6-2.6,1.6-4.2C16,5.9,13.1,"
                + "3,9.5,3C5.9,3,3,5.9,3,9.5S5.9,16,9.5,16c1.6,0,3.1-0.6,4.2-1.6l0"
                + ".3,0.3v0.8l5,5l1.5-1.5L15.5,14z M9.5,14C7,14,5,12,5,9.5S7,5,9.5"
                + ",5C12,5,14,7,14,9.5S12,14,9.5,14z");
        return path;
    }

    @Override
    protected String getCssStyle() {
        return "material-search-icon";
    }

}
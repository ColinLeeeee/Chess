

package images;

import javafx.scene.image.Image;

/**
 * Helper class that manages image resources.
 * 
 *
 */


public class ImageManager {
    public static String getResource(String resource) {
        return ImageManager.class.getResource(resource).toExternalForm();
    }

    public static Image getImage(String image){
        return new Image(getResource(image));
    }
}
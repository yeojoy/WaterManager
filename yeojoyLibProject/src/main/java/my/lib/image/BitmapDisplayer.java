package my.lib.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Displays {@link Bitmap} in {@link ImageView}. Implementations can apply some changes to Bitmap or any animation for
 * displaying Bitmap.<br />
 * Implementations have to be thread-safe.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public interface BitmapDisplayer {
    /**
     * Display bitmap in {@link ImageView}. Displayed bitmap should be returned.<br />
     * <b>NOTE:</b> This method is called on UI thread so it's strongly recommended not to do any heavy work in it.
     *
     * @param bitmap     Source bitmap
     * @param imageView  {@linkplain ImageView Image view} to display Bitmap
     * @param loadedFrom Source of loaded image
     * @return Bitmap which was displayed in {@link ImageView}
     */
    Bitmap display(Bitmap bitmap, ImageView imageView, LoadedFrom loadedFrom);
}
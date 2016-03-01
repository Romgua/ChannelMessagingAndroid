package romain.guarnotta.channelmessaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by romain on 01/03/16.
 */
//public class CircleTransformation extends AsyncTask<String, Void, Bitmap> {
//
//    private String url;
//    private ImageView imageView;
//    private LruCache<String, Bitmap> mMemoryCache;
//
//    public CircleTransformation(ImageView imageView, String url) {
//        this.imageView = imageView;
//        this.url = url;
//    }
//
//    // STOCK IMAGE TO THE CACHE ---------------------------------------------------------------
//    protected void onCreate(Bundle savedInstanceState) {
//        // Get max available VM memory, exceeding this amount will throw an
//        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
//        // int in its constructor.
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//
//        // Use 1/8th of the available memory for this memory cache.
//        final int cacheSize = maxMemory / 8;
//
//        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                // The cache size will be measured in kilobytes rather than
//                // number of items.
//                return bitmap.getByteCount() / 1024;
//            }
//        };
//    }
//
//    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            mMemoryCache.put(key, bitmap);
//        }
//    }
//
//    public Bitmap getBitmapFromMemCache(String key) {
//        return mMemoryCache.get(key);
//    }
//
//    // ----------------------------------------------------------------------------------------
//
//    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        final float roundPx = 100;
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//
//        return output;
//    }
//
//    @Override
//    protected Bitmap doInBackground(String... params) {
//        try {
//            URL urlConnection = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) urlConnection
//                    .openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//
//            final Bitmap bitmap = BitmapFactory.decodeStream(input);
//            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
//
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap result) {
//        super.onPostExecute(result);
//        if (getBitmapFromMemCache(result.))
//        if (result != null) {
//            imageView.setImageBitmap(getRoundedCornerBitmap(result));
//        }
//    }
//}

public class CircleTransformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}

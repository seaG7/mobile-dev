package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.repository.FrameMaskRepository;

public class FrameMaskRepositoryImpl implements FrameMaskRepository {
    private static final String TAG = "FrameMaskRepo";
    private static final String MODEL_ASSET = "deeplabv3.tflite";
    private static final String[] PASCAL_LABELS = {
            "background", "aeroplane", "bicycle", "bird", "boat", "bottle", "bus",
            "car", "cat", "chair", "cow", "diningtable", "dog", "horse", "motorbike",
            "person", "pottedplant", "sheep", "sofa", "train", "tvmonitor"
    };

    private final Context appContext;
    private Interpreter interpreter;

    public FrameMaskRepositoryImpl(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @Override
    public FrameMask buildMask() {
        return new FrameMask("person", true);
    }

    @Override
    public synchronized FrameMask buildMask(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return buildMask();
        }
        Bitmap bitmap = decodeBitmap(imagePath);
        if (bitmap == null) {
            return buildMask();
        }
        try {
            Interpreter tflite = getInterpreter();
            int[] inShape = tflite.getInputTensor(0).shape();
            int[] outShape = tflite.getOutputTensor(0).shape();
            int inH = inShape.length >= 3 ? inShape[1] : 257;
            int inW = inShape.length >= 3 ? inShape[2] : 257;
            int outH = outShape.length >= 3 ? outShape[1] : inH;
            int outW = outShape.length >= 3 ? outShape[2] : inW;
            int classes = outShape.length >= 4 ? outShape[3] : PASCAL_LABELS.length;

            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, inW, inH, true);
            ByteBuffer input = toInputBuffer(scaled, inW, inH, tflite.getInputTensor(0).dataType());
            float[][][][] output = new float[1][outH][outW][classes];
            tflite.run(input, output);
            if (scaled != bitmap) {
                scaled.recycle();
            }
            bitmap.recycle();

            int[] counts = new int[classes];
            for (int y = 0; y < outH; y++) {
                for (int x = 0; x < outW; x++) {
                    int bestClass = 0;
                    float bestScore = output[0][y][x][0];
                    for (int c = 1; c < classes; c++) {
                        if (output[0][y][x][c] > bestScore) {
                            bestScore = output[0][y][x][c];
                            bestClass = c;
                        }
                    }
                    counts[bestClass]++;
                }
            }

            int topClass = 0;
            int topCount = 0;
            for (int c = 1; c < classes; c++) {
                if (counts[c] > topCount) {
                    topCount = counts[c];
                    topClass = c;
                }
            }
            int personCount = classes > 15 ? counts[15] : 0;
            int totalPixels = outH * outW;
            boolean hasPerson = personCount > totalPixels / 50;
            String label = topClass > 0 && topClass < PASCAL_LABELS.length
                    ? PASCAL_LABELS[topClass]
                    : "frame";
            Log.d(TAG, "DeepLab TFLite result: " + label + ", personPx=" + personCount + "/" + totalPixels);
            return new FrameMask(label, hasPerson);
        } catch (Exception e) {
            Log.w(TAG, "TFLite mask fallback: " + e.getMessage());
            bitmap.recycle();
            return buildMask();
        }
    }

    private Interpreter getInterpreter() throws IOException {
        if (interpreter == null) {
            MappedByteBuffer model = loadModelBuffer();
            Interpreter.Options options = new Interpreter.Options();
            options.setNumThreads(2);
            interpreter = new Interpreter(model, options);
        }
        return interpreter;
    }

    private MappedByteBuffer loadModelBuffer() throws IOException {
        try (AssetFileDescriptor fd = appContext.getAssets().openFd(MODEL_ASSET);
             FileInputStream fis = new FileInputStream(fd.getFileDescriptor());
             FileChannel channel = fis.getChannel()) {
            return channel.map(FileChannel.MapMode.READ_ONLY, fd.getStartOffset(), fd.getDeclaredLength());
        }
    }

    private ByteBuffer toInputBuffer(Bitmap bitmap, int width, int height, DataType type) {
        int channels = 3;
        int bytesPerChannel = type == DataType.UINT8 ? 1 : 4;
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * channels * bytesPerChannel);
        buffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int pixel : pixels) {
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;
            if (type == DataType.UINT8) {
                buffer.put((byte) r);
                buffer.put((byte) g);
                buffer.put((byte) b);
            } else {
                buffer.putFloat((r - 127.5f) / 127.5f);
                buffer.putFloat((g - 127.5f) / 127.5f);
                buffer.putFloat((b - 127.5f) / 127.5f);
            }
        }
        buffer.rewind();
        return buffer;
    }

    private Bitmap decodeBitmap(String imagePath) {
        try {
            if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                try (InputStream is = appContext.getContentResolver().openInputStream(Uri.parse(imagePath))) {
                    return is == null ? null : BitmapFactory.decodeStream(is);
                }
            }
            return BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {
            return null;
        }
    }
}

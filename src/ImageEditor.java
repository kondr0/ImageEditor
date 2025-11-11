import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImageEditor {

    public static void main(String[] args) {

        String inputPath = readInputPath();
        if (inputPath == null) {
            System.out.println("Place image path or link inside input.txt and run again.");
            return;
        }

        int[][] imageData = imgToTwoD(inputPath);

        if (imageData == null) {
            System.out.println("Failed to process the input image.");
            return;
        }

        int[][] trimmed = trimBorders(imageData, 60);
        twoDToImage(trimmed, "./trimmed.jpg");

        int[][] negative = negativeColor(imageData);
        twoDToImage(negative, "./negative.jpg");

        int[][] stretchedHImg = stretchHorizontally(imageData);
        twoDToImage(stretchedHImg, "./stretched.jpg");

        int[][] shrankVImg = shrinkVertically(imageData);
        twoDToImage(shrankVImg, "./shrank.jpg");

        int[][] invertedImg = invertImage(imageData);
        twoDToImage(invertedImg, "./inverted.jpg");

        int[][] coloredImg = colorFilter(imageData, -75, 30, -30);
        twoDToImage(coloredImg, "./colored.jpg");

        int[][] blankImg = new int[500][500];
        int[][] randomImg = paintRandomImage(blankImg);
        twoDToImage(randomImg, "./random_img.jpg");

        int[] rgba = {255, 255, 0, 255};
        int color = getColorIntValFromRGBA(rgba);
        int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, color);
        twoDToImage(rectangleImg, "./rectangle.jpg");

        int[][] generatedRectangles = generateRectangles(randomImg, 1000);
        twoDToImage(generatedRectangles, "./generated_rect.jpg");

        System.out.println("Done. Images saved in project directory.");
    }

    public static String readInputPath() {
        try {
            File inputFile = new File("input.txt");
            Scanner scanner = new Scanner(inputFile);
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("input.txt not found.");
        }
        return null;
    }

    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int newHeight = imageTwoD.length - pixelCount * 2;
            int newWidth = imageTwoD[0].length - pixelCount * 2;
            int[][] newImage = new int[newHeight][newWidth];
            for (int i = 0; i < newHeight; i++) {
                for (int j = 0; j < newWidth; j++) {
                    newImage[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return newImage;
        } else {
            System.out.println("Cannot trim that many pixels.");
            return imageTwoD;
        }
    }

    public static int[][] negativeColor(int[][] imageTwoD) {
        int[][] result = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                result[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return result;
    }

    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] result = new int[imageTwoD.length][imageTwoD[0].length * 2];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int color = imageTwoD[i][j];
                result[i][j * 2] = color;
                result[i][j * 2 + 1] = color;
            }
        }
        return result;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] result = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length - 1; i += 2) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                result[i / 2][j] = imageTwoD[i][j];
            }
        }
        return result;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] result = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                result[i][j] = imageTwoD[imageTwoD.length - 1 - i][imageTwoD[i].length - 1 - j];
            }
        }
        return result;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int r, int g, int b) {
        int[][] result = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = Math.max(0, Math.min(255, rgba[0] + r));
                rgba[1] = Math.max(0, Math.min(255, rgba[1] + g));
                rgba[2] = Math.max(0, Math.min(255, rgba[2] + b));
                result[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return result;
    }

    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int[] rgba = {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255};
                canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return canvas;
    }

    public static int[][] paintRectangle(int[][] canvas, int width, int height, int row, int col, int color) {
        for (int i = row; i < row + width && i < canvas.length; i++) {
            for (int j = col; j < col + height && j < canvas[i].length; j++) {
                canvas[i][j] = color;
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int width = rand.nextInt(canvas.length);
            int height = rand.nextInt(canvas[0].length);
            int row = rand.nextInt(canvas.length - width);
            int col = rand.nextInt(canvas[0].length - height);
            int[] rgba = {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255};
            int color = getColorIntValFromRGBA(rgba);
            paintRectangle(canvas, width, height, row, col, color);
        }
        return canvas;
    }

    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image;
            if (inputFileOrLink.startsWith("http")) {
                URL link = new URL(inputFileOrLink);
                image = ImageIO.read(link);
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }
            int rows = image.getHeight();
            int cols = image.getWidth();
            int[][] result = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[i][j] = image.getRGB(j, i);
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            return null;
        }
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int rows = imgData.length;
            int cols = imgData[0].length;
            BufferedImage result = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            ImageIO.write(result, "jpg", new File(fileName));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getMessage());
        }
    }

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color color = new Color(pixelColorValue, true);
        return new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
    }

    public static int getColorIntValFromRGBA(int[] colorData) {
        Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
        return color.getRGB();
    }
}

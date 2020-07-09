package sample;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Controller {

    @FXML
    private static MenuItem openImage;

    @FXML
    private TextArea enterText;

    @FXML
    private Button buttonCode;

    @FXML
    private ImageView mainImageView;

    @FXML
    private TextArea seeText;

    @FXML
    private Button buttonDecode;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    public static File openedFile;
    public static Image imageNotChanged = null;
    public static Image imageReady = null;
    public static Color[][] colorsReady;
    public static Color[][] colorsNotChanged;
    public static char[] LETTERS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' '};
    public static final char FIRST_FOR_ONE_SYMBOL = 'a';
    public static final char EMPTY_SYMBOL = 'b';
    public static String mainText;

    @FXML
    void initialize() {}

    @FXML
    private void codePressed() {
        //System.out.println(checkEnterClearness(enterText));
        if(!checkEnterClearness(enterText)){
            colorsReady = codePicture(mainText);
            writeImage();
        }
    }

    @FXML
    private void decodePressed() {
        String decodedText = decodePicture(mainImageView.getImage());
        seeText.setText(normaliseString(decodedText));
    }

    @FXML
    private void quitPressed() {
        System.exit(0);
    }

    @FXML
    private void savePressed() {
        File file = new File("RomeoProgram.png");
        for (int i = 1; true; i++) {
            if(!file.exists() && !file.isDirectory()) {
                try {
                    saveImage(mainImageView.getImage(), new FileOutputStream(file.getName()), "png");
                } catch (FileNotFoundException ignored) {}
                break;
            }
            file = new File("RomeoProgram(" + i + ").png");
        }
    }

    @FXML
    private void saveAsPressed() throws IOException {
        File file = saveAs();
        FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
        if(file.getName().endsWith("jpg"))
            saveImage(mainImageView.getImage(), outputStream, "jpg");
        else if (file.getName().endsWith("png"))
            saveImage(mainImageView.getImage(), outputStream, "png");
        else if (file.getName().endsWith("bmp"))
            saveImage(mainImageView.getImage(), outputStream, "bmp");
        outputStream.close();
    }

    private static boolean checkEnterClearness(TextArea textArea) {
        if (textArea.getText().equals("")) {
            textArea.setPromptText("Nothing to code");
            return true;
        } else {
            mainText = textArea.getText();
            textArea.clear();
            textArea.setPromptText("Wait for seconds...");
            textArea.clear();
            textArea.setPromptText("Enter text");
            return false;
        }
    }

    private static Color[][] codePicture(String text) {
        imageToPixels(imageNotChanged);
        Color[][] imageSecond = new Color[colorsNotChanged.length][colorsNotChanged[0].length];
        int lettersCounter = 0;
        int[] finalText = textToInt(text);
        String currentColor1, colorWithNoLast1, currentColor2, colorWithNoLast2;
        for (int i = 0; i < colorsNotChanged.length; i++) {
            for (int j = 0; j < colorsNotChanged[i].length; j = j + 2, lettersCounter++) {
                currentColor1 = String.valueOf(colorsNotChanged[i][j]);
                currentColor2 = String.valueOf(colorsNotChanged[i][j + 1]);
                colorWithNoLast1 = currentColor1.substring(0, currentColor1.length() - 1);
                colorWithNoLast2 = currentColor2.substring(0, currentColor2.length() - 1);
                try {
                    if (String.valueOf(finalText[lettersCounter]).length() == 1) {
                        imageSecond[i][j] = Color.valueOf(colorWithNoLast1 + FIRST_FOR_ONE_SYMBOL);
                        imageSecond[i][j + 1] = Color.valueOf(colorWithNoLast2 + finalText[lettersCounter]);
                    } else {
                        System.out.println(String.valueOf(finalText[lettersCounter]).charAt(0));
                        imageSecond[i][j] = Color.valueOf(colorWithNoLast1 + String.valueOf(finalText[lettersCounter]).charAt(0));
                        imageSecond[i][j + 1] = Color.valueOf(colorWithNoLast2 + String.valueOf(finalText[lettersCounter]).charAt(1));//змінити 1 на 2, поки що для перевірки

                    }
                    System.out.println(imageSecond[i][j]);
                    System.out.println(imageSecond[i][j + 1]);

                } catch (ArrayIndexOutOfBoundsException e) {
                    imageSecond[i][j] = Color.valueOf(colorWithNoLast1 + EMPTY_SYMBOL);
                    imageSecond[i][j + 1] = Color.valueOf(colorWithNoLast1 + EMPTY_SYMBOL);
                }
            }
        }

        return imageSecond;
    }

    private static String decodePicture(Image image) {
        imageToPixels(image);
        int lettersCounter = 0;
        ArrayList<Character> finalCharsList = new ArrayList<>();
        String currentColor1, currentColor2, finalString, currentNumber;
        int currentInteger;
        int counter = 0;
        char lastCharacter1, lastCharacter2;
        for (Color[] colors : colorsNotChanged) {
            for (int j = 0; j < colors.length; j = j + 2, lettersCounter++) {
                currentColor1 = String.valueOf(colors[j]);
                currentColor2 = String.valueOf(colors[j + 1]);
                lastCharacter1 = currentColor1.charAt(currentColor1.length() - 1);
                lastCharacter2 = currentColor2.charAt(currentColor1.length() - 1);

                if(lastCharacter1 == EMPTY_SYMBOL){
                    finalString = finalCharsList.toString();
                    return finalString;
                }

                if (lastCharacter1 == FIRST_FOR_ONE_SYMBOL) {
                    currentInteger = Integer.parseInt(String.valueOf(lastCharacter2));
                } else {
                    currentNumber = new String(new char[]{lastCharacter1, lastCharacter2});
                    currentInteger = Integer.parseInt(currentNumber);
                }
                finalCharsList.add(LETTERS[currentInteger]);
            }
        }

        finalString = finalCharsList.toString();
        return finalString;
    }

    private static int[] textToInt(String text) {
        int[] finalStatement = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < LETTERS.length; j++) {
                if (LETTERS[j] == text.charAt(i))
                    finalStatement[i] = j;
            }
        }
        return finalStatement;
    }

    @FXML
    private void openFile() {
        openedFile = getFile();
        try {
            imageNotChanged = new Image(new FileInputStream(openedFile.getAbsolutePath()));
        } catch (FileNotFoundException ignored) {
        }
        mainImageView.setImage(imageNotChanged);
        colorsNotChanged = new Color[(int) imageNotChanged.getWidth()][(int) imageNotChanged.getHeight()];
    }

    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image to code in");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All mages", "*.jpg", "*.png", "*.bmp"),
                new FileChooser.ExtensionFilter("JPG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("BMP Image", "*.bmp")
        );
        return fileChooser.showOpenDialog(new Stage());
    }

    private static void imageToPixels(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                colorsNotChanged[i][j] = pixelReader.getColor(i, j);
                //System.out.println(colors[i][j]);
            }
        }
        System.out.println("ready");
    }

    private void writeImage() {
        WritableImage wImage = new WritableImage((int) imageNotChanged.getWidth(), (int) imageNotChanged.getHeight());
        PixelWriter writer = wImage.getPixelWriter();
        for (int i = 0; i < imageNotChanged.getWidth(); i++) {
            for (int j = 0; j < imageNotChanged.getHeight(); j++) {
                writer.setColor(i, j, colorsReady[i][j]);
            }

        }

        mainImageView.setImage(new ImageView(wImage).getImage());
        System.out.println("OK");
    }

    private static String normaliseString(String inCorrectString){
        String result = "";
        inCorrectString = inCorrectString.substring(1, inCorrectString.length() - 1);
        for (int i = 0; i < inCorrectString.length(); i += 3) {
            result = result.concat(String.valueOf(inCorrectString.charAt(i)));
        }
        return result;
    }

    private static void saveImage(Image image, FileOutputStream out, String format) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bufferedImage, format, out);
        } catch (IOException ignored){}
    }

    private static File saveAs() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Saving dialog");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png"));
        return fileChooser.showSaveDialog(new Stage());
    }
}


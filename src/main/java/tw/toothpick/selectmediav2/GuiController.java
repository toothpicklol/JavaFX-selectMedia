package tw.toothpick.selectmediav2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;


import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GuiController {

    @FXML
    private TextField keyword;
    @FXML
    private Label urlLabel;
    @FXML
    private Label msg;
    @FXML
    private VBox logCon;


    public static int count = 0;
    public static String key = "";
    public static String[] array = {};
    public static List<String> media = new ArrayList<>();



    public void CheckName(ActionEvent actionEvent) {
        if(!Objects.equals(urlLabel.getText(), "選擇資料夾路徑")){
            media.clear();
            try {
                checkName(urlLabel.getText());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            msg.setText("  ");
        }
        else {
            msg.setText("選擇資料夾路徑");
        }

    }

    public void CheckFileIsMP4(ActionEvent actionEvent) {
        if(!Objects.equals(urlLabel.getText(), "選擇資料夾路徑")){
            media.clear();
            try {
                checkMP4(urlLabel.getText());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            msg.setText("  ");
        }
        else {
            msg.setText("選擇資料夾路徑");
        }

    }

    public void CheckFileExist(ActionEvent actionEvent) {

        if(!Objects.equals(urlLabel.getText(), "選擇資料夾路徑")){
            media.clear();
            msg.setText("讀取中請稍後");
            try {
                checkExist(urlLabel.getText());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            msg.setText("  ");
        }
        else {
            msg.setText("選擇資料夾路徑");
        }
    }

    public void Go(ActionEvent actionEvent) {
        if(!Objects.equals(urlLabel.getText(), "選擇資料夾路徑")){
            media.clear();
            try {
                key=keyword.getText();
                go(urlLabel.getText());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            msg.setText("  ");

        }
        else {
            msg.setText("選擇資料夾路徑");
        }
    }

    public void choseUrl(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            urlLabel.setText(selectedDirectory.getAbsolutePath());
            msg.setText("  ");
        }
        else {
            msg.setText("路徑錯誤");
        }

    }

    public static String select() {
        Random r1 = new Random();
        return media.get(r1.nextInt(media.size()));
    }

    public void newLogLabel(String s) {

        Label newLabel= new Label();
        //Button newButton=new Button();
        //HBox newHBox= new HBox();


        newLabel.setText(s);
        
        logCon.getChildren().add(newLabel);

    }
    public void openFolder(String path) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen;
        try {
            dirToOpen = new File(path);
            desktop.open(dirToOpen);
        } catch (IllegalArgumentException iae) {
           msg.setText("File Not Found");
        }
    }

    public void checkExist(String url) {
        logCon.getChildren().clear();
        newLogLabel("");
        try {
            readFile(url);
        } catch (IOException ex) {

            msg.setText("error");
        }
        for (int i = 0; i <= media.size(); i++) {
            for (int j = i + 1; j <= media.size() - 1; j++) {
                System.out.println(i+" "+j);
                String fileName1 = media.get(i);
                String fileName2 = media.get(j);
                FileInputStream fis1 = null;
                FileInputStream fis2 = null;
                try {
                    fis1 = new FileInputStream(fileName1);
                    fis2 = new FileInputStream(fileName2);
                    int len1 = fis1.available();
                    int len2 = fis2.available();

                    if (len1 == len2 && fis1.getChannel().size() == fis2.getChannel().size()) {//長度相同，則比較具體內容    //建立兩個位元組緩衝區
                        if (len1 >= 2147483647) {
                            newLogLabel("檔案過大無法判斷");
                            newLogLabel(fileName1);
                            newLogLabel(fileName2);
                            break;
                        }
                        byte[] data1 = new byte[len1];
                        byte[] data2 = new byte[len2];
                        //分別將兩個檔案的內容讀入緩衝區
                        fis1.read(data1);
                        fis2.read(data2);
                        //依次比較檔案中的每一個位元組
                        for (int k = 0; k < len1; k++) {
                            //只要有一個位元組不同，兩個檔案就不一樣
                            if (data1[k] != data2[k]) {
                                newLogLabel("檔案內容不一樣");
                                return;
                            }
                        }
                        newLogLabel("兩個檔案完全相同");

                        newLogLabel(fileName1);
                        newLogLabel(fileName2);
                    }
                } catch (FileNotFoundException e) {
                    newLogLabel(fileName1);
                    newLogLabel(fileName2);
                    e.printStackTrace();
                } catch (IOException e) {
                    newLogLabel(fileName1);
                    newLogLabel(fileName2);
                    e.printStackTrace();
                } finally {//關閉檔案流，防止記憶體洩漏
                    if (fis1 != null) {
                        try {
                            fis1.close();
                        } catch (IOException e) {     //忽略
                            newLogLabel(fileName1);
                            newLogLabel(fileName2);
                            e.printStackTrace();
                        }
                    }
                    if (fis2 != null) {
                        try {
                            fis2.close();
                        } catch (IOException e) {
                            newLogLabel(fileName1);
                            newLogLabel(fileName2);
                            //忽略
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        newLogLabel("檢查完畢");
        System.out.println("檢查完畢");

    }

    public void checkMP4(String url) {
        logCon.getChildren().clear();
        try {
            readFile(url);
        } catch (IOException ex) {
            msg.setText("error");
        }
        for (String s : media) {
            String fe = s.replaceAll("^.*\\.(.*)$", "$1");
            ;
            if (!fe.equals("mp4") &&!fe.equals("ts")&&!fe.equals("wmv")&&!fe.equals("mov")) {
                newLogLabel(s);

            }
        }
        msg.setText("檢查完畢");



    }

    public void checkName(String url) {

        logCon.getChildren().clear();

        try {
            readFile(url);
        } catch (IOException ex) {
            System.out.println("error");
        }
        count = 0;
        for (String s : media) {

            String fileName = null;
            int i = s.lastIndexOf('\\');
            if (i > 0) {
                fileName = s.substring(i + 1);

            }
            String[] name = fileName.split("\\.");
            if (!name[0].trim().matches("[0-9|_\\.]*")) {
                count++;

                String newPath = s.substring(0, i);
                File file = new File(newPath);

                newLogLabel(s);
            }
        }
        msg.setText("檢查完畢");
    }

    public String check(String name, String[] array) {
        String tmp = "";
        for (int i = 0; i < array.length; i++) {

            if (i == 0) {
                if (!name.contains(array[i])) {
                    name = "error";
                    break;
                }

            } else {

                if (!tmp.contains(array[i])) {
                    name = "error";
                    break;
                }
            }
            tmp = name;
        }

        if (name == "error") {
            name = select();
            tmp = check(name, array);
        } else {
            tmp = name;
        }
        return tmp;
    }

    /**
     * 讀取某個資料夾下的所有檔案
     */
    public void readFile(String filepath) throws IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                media.add(file.getAbsolutePath());
                count++;
            }
            else if (file.isDirectory()) {
                String[] fileList = file.list();
                assert fileList != null;
                for (String s : fileList) {
                    File readFile = new File(filepath + "//" + s);
                    if (!readFile.isDirectory()) {
                        media.add(readFile.getAbsolutePath());
                        count++;

                    }
                    else if (readFile.isDirectory()) {
                        readFile(filepath + "//" + s);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("readFile()   Exception:" + e.getMessage());
        }
    }

    public void go(String url) throws IOException {
        logCon.getChildren().clear();
        try {
            readFile(url);

        } catch (IOException ex) {
            System.out.println("error");
        }

        String name=select();
        if(!key.equals("")){
            try{
                array=key.split(",");
            }
            catch (Exception e){
                System.out.println(e);
            }
            name=check(name,array);
            if(name!=""){
                newLogLabel(name);
                openFolder(name);

                String [] tmp=name.split("\\\\");
                if(tmp[tmp.length-2].matches("[a-zA-Z]+")){
                    openFolder(name.replace(tmp[tmp.length-1],""));
                }
            }
            else {
                System.out.println("error");
            }

        }
        else{
            newLogLabel(name);
            openFolder(name);
            String [] tmp=name.split("\\\\");
            if(tmp[tmp.length-2].matches("[a-zA-Z]+")){
                openFolder(name.replace(tmp[tmp.length-1],""));
            }
        }

    }
}
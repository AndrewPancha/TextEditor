package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuSearch;
    private JMenuItem menuLoad;
    private JMenuItem menuSave;
    private JMenuItem menuExit;
    private JMenuItem menuSearchItem;
    private JMenuItem menuPrevious;
    private JMenuItem menuNext;
    private JMenuItem menuRegex;
    private JButton loadButton;
    private JButton saveButton;
    private JButton searchButton;
    private JButton previousButton;
    private JButton nextButton;
    private JCheckBox regExCheckBox;

    private JTextArea textArea;
    private JTextField textField;
    private JFileChooser fileChooser;
    private File file;
    private String filename;
    private String text;
    private List<Indexes> indexes;
    private int point;
    private int index;
    private String foundText;
    private int startIndex;
    private int endIndex;



    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        initMenu();
        initUI();
        addActionListeners();
        setVisible(true);
    }

    public void initMenu() {
        this.menuBar = new JMenuBar();

        menuFile = new JMenu("File");
        menuFile.setName("MenuFile");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFile);

        menuLoad = new JMenuItem("Load");
        menuLoad.setName("MenuOpen");
        menuFile.add(menuLoad);

        menuSave = new JMenuItem("Save");
        menuSave.setName("MenuSave");
        menuFile.add(menuSave);

        menuFile.addSeparator();

        menuExit = new JMenuItem("Exit");
        menuExit.setName("MenuExit");
        menuFile.add(menuExit);


        menuSearch = new JMenu("Search");
        menuSearch.setName("MenuSearch");
        menuBar.add(menuSearch);

        menuSearchItem = new JMenuItem("Start search");
        menuSearchItem.setName("MenuStartSearch");
        menuSearch.add(menuSearchItem);

        menuPrevious = new JMenuItem("Previous search");
        menuPrevious.setName("MenuPreviousMatch");
        menuSearch.add(menuPrevious);

        menuNext = new JMenuItem("Next match");
        menuNext.setName("MenuNextMatch");
        menuSearch.add(menuNext);

        menuRegex = new JMenuItem("Use regular expressions");
        menuRegex.setName("MenuUseRegExp");
        menuSearch.add(menuRegex);


        setJMenuBar(menuBar);
    }

    public void initUI() {
        panel = new JPanel();
        panel.setName("Panel");
        panel.setLayout(new FlowLayout());
        add(panel, BorderLayout.NORTH);


        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setName("FileChooser");
        add(fileChooser);

        loadButton = new JButton(new ImageIcon(""));
        loadButton.setName("OpenButton");
        loadButton.setPreferredSize(new Dimension(30, 30));
        panel.add(loadButton);

        saveButton = new JButton(new ImageIcon("save.png"));
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(30, 30));
        panel.add(saveButton);

        textField = new JTextField();
        textField.setName("SearchField");
        textField.setPreferredSize(new Dimension(200, 31));
        panel.add(textField);

        searchButton = new JButton(new ImageIcon("~/IdeaProjects/Text Editor1/Text Editor/task/src/editor/images/search.png"));
        searchButton.setName("StartSearchButton");
        searchButton.setPreferredSize(new Dimension(30, 30));
        panel.add(searchButton);

        previousButton = new JButton(new ImageIcon("~/IdeaProjects/Text Editor1/Text Editor/task/src/editor/images/back.png"));
        previousButton.setName("PreviousMatchButton");
        previousButton.setPreferredSize(new Dimension(30, 30));
        panel.add(previousButton);

        nextButton = new JButton();
        nextButton.setIcon(new ImageIcon("images/next.png"));
        nextButton.setName("NextMatchButton");
        nextButton.setPreferredSize(new Dimension(30, 30));
        panel.add(nextButton);

        regExCheckBox = new JCheckBox("Use regex", true);
        regExCheckBox.setName("UseRegExCheckbox");
        panel.add(regExCheckBox);

        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setName("ScrollPane");
        add(scrollPane, BorderLayout.CENTER );



        validate();
    }

    private void addActionListeners() {
        menuLoad.addActionListener(event -> loadText());
        menuSave.addActionListener(event -> saveText());
        menuExit.addActionListener(event -> System.exit(0));
        menuSearchItem.addActionListener(event -> searchText());
        menuPrevious.addActionListener(event -> previousMatch());
        menuNext.addActionListener(event -> nextMatch());
        menuRegex.addActionListener(event -> searchText());


        loadButton.addActionListener(event -> loadText());
        saveButton.addActionListener(event -> saveText());
        searchButton.addActionListener(event -> searchText());
        previousButton.addActionListener(event -> previousMatch());
        nextButton.addActionListener(event -> nextMatch());
    }


    public void loadText() {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                filename = file.getAbsolutePath();
            }

            filename = textField.getText();
            textField.setText(filename);
            file = new File(filename);
            if (!file.exists()) {
                textArea.setText("");
            } else {
                try {
                    text = Files.readString(Paths.get(filename));
                    textArea.setText(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    public void saveText() {
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                filename = file.getAbsolutePath();
                text = textArea.getText();
                try {
                    Files.write(Paths.get(filename), text.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    public void searchText() {
            point = 0;

            indexes = new ArrayList<>();
            foundText = textField.getText();
            String text = textArea.getText();
            Pattern pattern = Pattern.compile(foundText);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                startIndex = matcher.start();
                endIndex = matcher.end();
                indexes.add(new Indexes(startIndex, endIndex));
            }
            if (!indexes.isEmpty()) {
                startIndex = indexes.get(point).getStartIndex();
                endIndex = indexes.get(point).getEndIndex();
            }

        textArea.select(startIndex, endIndex);
        textArea.grabFocus();

    }

    public void previousMatch() {
            point -= 1;
            if (point < 0) {
                point = 0;
            }
            startIndex = indexes.get(point).getStartIndex();
            endIndex = indexes.get(point).getEndIndex();
            textArea.select(startIndex, endIndex);
            textArea.grabFocus();
    }

    public void nextMatch() {
            if ((point + 1) > (indexes.size())) {
                point = indexes.size() - 1;
            }
            startIndex = indexes.get(point).getStartIndex();
            endIndex = indexes.get(point).getEndIndex();
            textArea.select(startIndex, endIndex);
            textArea.grabFocus();

    }


}

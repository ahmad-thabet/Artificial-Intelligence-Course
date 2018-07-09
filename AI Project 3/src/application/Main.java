package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			// controllers
			Button ibtn = new Button("index");
			ibtn.setMinSize(80, 30);

			TextField txt = new TextField();
			txt.setPromptText("Type to Search");
			txt.setMinSize(800, 30);
			txt.setFont(new Font("Arial", 14));
			
			

			Button sbtn = new Button("search");
			sbtn.setMinSize(80, 30);
			sbtn.setDisable(true);

			TextArea txta = new TextArea("");
			txta.setEditable(false);
			txta.setFont(new Font("Arial", 16));
			txta.setWrapText(true);
			// txta.setStyle("-fx-control-inner-background:#000000; -fx-highlight-fill:
			// #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");

			HBox hbox = new HBox(15, ibtn, txt, sbtn);

			// code goes here

			TreeMap<String, HashSet<String>> map = new TreeMap<String, HashSet<String>>();
			File file = new File("/Users/mohammadtabakhna/Desktop/quran.txt");
			int totalCounter = 0;

			ibtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					readFile(file, map, totalCounter);
					sbtn.setDisable(false);
					ibtn.setDisable(true);

				}
			});

			sbtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (!txt.getText().isEmpty()) {
						if (txt.getText().trim().split(" ").length == 1) {
							search(txt.getText().trim(), map, txta, file);
						} else if (txt.getText().trim().split(" ").length == 2) {
							searchM(txt.getText().trim(), map, txta, file);
						} else if (txt.getText().trim().split(" ").length == 3) {
							searchT(txt.getText().trim(), map, txta, file);
						} else {
							String[] split = txt.getText().trim().split(" ");

							int i = split.length;
							String prt3 = split[i - 1];
							String prt2 = split[i - 2];
							String prt1 = split[i - 3];

							String tmp = prt1 + " " + prt2 + " " + prt3;
							searchT(tmp, map, txta, file);

						}
					} else {
						txta.appendText("Please Enter Some Text First !!");
					}
				}

			});

			// end of code

			BorderPane root = new BorderPane();
			root.setTop(hbox);
			root.setCenter(txta);
			Scene scene = new Scene(root, 1000, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Ahmad Thabet - Ai project 3");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFile(File file1, TreeMap<String, HashSet<String>> map, int totalCounter) {
		try (BufferedReader file = new BufferedReader(new FileReader(file1))) {
			String line;
			while ((line = file.readLine()) != null) {

				// docID position Strings
				String[] split = line.split("\\|");
				String[] twosplit = split[2].split(" ");
				for (int i = 0; i < twosplit.length; i++) {

					if (!map.containsKey(twosplit[i])) {
						map.put(twosplit[i], new HashSet<>());
					}
					map.get(twosplit[i]).add(split[0] + ":" + split[1] + ":" + i);
					totalCounter++;
				}

			}

		} catch (Exception e) {
		}

	}

	public static void search(String line1, TreeMap<String, HashSet<String>> map, TextArea txta, File file1) {
		txta.clear();
		HashSet<String> set1 = map.get(line1);

		if (set1 == null) {
			txta.appendText("Not results found !");
		} else {
			try (BufferedReader file = new BufferedReader(new FileReader(file1))) {
				String line;
				while ((line = file.readLine()) != null) {

					// docID position Strings
					String[] split = line.split("\\|");
					Set<String> set = set1;

					for (String s : set) {
						String[] tmp = s.split(":");
						if (tmp[0].equals(split[0]) && tmp[1].equals(split[1])) {
							txta.appendText(split[2] + " ( سورة: " + split[0] + " اية: " + split[1] + " )\n\n");
						}
					}
				}
			} catch (Exception e) {
				System.out.println("error: " + e);
			}

		}

	}

	public static void searchM(String line1, TreeMap<String, HashSet<String>> map, TextArea txta, File file1) {
		txta.clear();
		String[] words = line1.split(" ");
		Set<String> setW1 = map.get(words[0]);
		Set<String> setW2 = map.get(words[1]);

		if (setW1 == null || setW2 == null) {
			txta.appendText("Not results found !");
		} else {

			for (String w1 : setW1) {
				String[] tmpW1 = w1.split(":");
				for (String w2 : setW2) {
					String[] tmpW2 = w2.split(":");
					if (tmpW1[0].equals(tmpW2[0]) && tmpW1[1].equals(tmpW2[1])
							&& ((Integer.parseInt(tmpW1[2])) == (Integer.parseInt(tmpW2[2]) - 1))) {

						try (BufferedReader file = new BufferedReader(new FileReader(file1))) {
							String line;
							while ((line = file.readLine()) != null) {
								String[] split = line.split("\\|");
								if (split[0].equals(tmpW1[0]) && split[1].equals(tmpW1[1])) {
									txta.appendText(split[2] + " ( سورة: " + split[0] + " اية: " + split[1] + " )\n\n");
									break;
								}
							}

						} catch (Exception e) {
						}
					}
				}

			}
		}

	}

	public static void searchT(String line1, TreeMap<String, HashSet<String>> map, TextArea txta, File file1) {
		txta.clear();
		String[] words = line1.split(" ");
		Set<String> setW1 = map.get(words[0]);
		Set<String> setW2 = map.get(words[1]);
		Set<String> setW3 = map.get(words[2]);

		if (setW1 == null || setW2 == null || setW3 == null) {
			txta.appendText("Not results found !");
		} else {

			for (String w1 : setW1) {
				String[] tmpW1 = w1.split(":");
				for (String w2 : setW2) {
					String[] tmpW2 = w2.split(":");
					for (String w3 : setW3) {
						String[] tmpW3 = w3.split(":");
						if (tmpW1[0].equals(tmpW2[0]) && tmpW2[0].equals(tmpW3[0])) {
							if (tmpW1[1].equals(tmpW2[1]) && tmpW2[1].equals(tmpW3[1])) {
								int i = Integer.parseInt(tmpW1[2]);
								int j = Integer.parseInt(tmpW2[2]);
								int k = Integer.parseInt(tmpW3[2]);

								if (i + 1 == j && j + 1 == k) {
									try (BufferedReader file = new BufferedReader(new FileReader(file1))) {
										String line;
										while ((line = file.readLine()) != null) {
											String[] split = line.split("\\|");
											if (split[0].equals(tmpW1[0]) && split[1].equals(tmpW1[1])) {
												txta.appendText(split[2] + " ( سورة: " + split[0] + " اية: " + split[1]
														+ " )\n\n");
												break;
											}
										}
									} catch (Exception e) {
									}
								}
							}
						}
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}

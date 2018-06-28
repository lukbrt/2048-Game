package pl.lmb.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class GameController implements Initializable
{

	@FXML
	private BorderPane gamePane;

	@FXML
	private GridPane grid;

	@FXML
	private Button btn1;

	@FXML
	private Button btn2;

	@FXML
	private Button btn3;

	@FXML
	private Button btn6;

	@FXML
	private Button btn7;

	@FXML
	private Button btn8;

	@FXML
	private Button btn9;

	@FXML
	private Button btn10;

	@FXML
	private Button btn11;

	@FXML
	private Button btn4;

	@FXML
	private Button btn5;

	@FXML
	private Button btn12;

	@FXML
	private Button btn13;

	@FXML
	private Button btn14;

	@FXML
	private Button btn15;

	@FXML
	private Button btn16;

	@FXML
	private AnchorPane topPane;

	@FXML
	private Button startButton;

	@FXML
	private Button restartButton;

	@FXML
	private Label ScoreLabel;
	
	private final static Map<Integer, String> colors;
	private boolean isStarted;
	private int score;
	private Label endStatementLabel;
	
	static
	{
		colors = new HashMap<>();
		colors.put(0, "#828282");
		colors.put(2, "#84FFA5");
		colors.put(4, "#3DDE90");
		colors.put(8, "#268C5B");
		colors.put(16, "#227D3C");
		colors.put(32, "#FF8631");
		colors.put(64, "#D9712A");
		colors.put(128, "#DE5D2B");
		colors.put(256, "#DE492B");
		colors.put(512, "#DE362B");
		colors.put(1024, "#DE366F");
		colors.put(2048, "#A13682");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		System.out.println("bla ->" + ScoreLabel.getText());
		
		startButton.addEventFilter(ActionEvent.ACTION, e -> 
			{
				initializeGrid();
				isStarted = true;
				startButton.setDisable(true);
			});
		restartButton.addEventFilter(ActionEvent.ACTION, e -> restart());
		
		
		
		gamePane.addEventFilter(KeyEvent.KEY_PRESSED, e ->
		{
			if (e.getCode() == KeyCode.LEFT)
			{
				System.out.println("left");
				stepLeft();
			}
			else if (e.getCode() == KeyCode.RIGHT)
			{
				System.out.println("right");
				stepRight();
			}
			else if (e.getCode() == KeyCode.UP)
			{
				System.out.println("up");
				stepUp();
			}
			else if (e.getCode() == KeyCode.DOWN)
			{
				System.out.println("down");
				stepDown();
			}
		});
		
	}

	private void stepDown()
	{
		ObservableList<Node> nodes = grid.getChildren();
		int[][] values = createNodeArray(nodes);
		

		for (int i = 0; i < 4; i++)
		{
				for (int j = 3; j > 0; j--) //tab[j][i]
				{
					if (values[j][i] == values[j - 1][i] && values[j][i] != 0) //dolna == dolna - 1
					{
						values[j][i] += values[j - 1][i];
						values[j - 1][i] = 0;
						
						updateScore(values[j][i]);
					}
					
					if (values[j - 1][i] != 0 && values[j][i] == 0)
					{
						for (int k = j; k < 4; k++)
						{
							if (values[k][i] == 0 && (k == 3 || values[k + 1][i] != 0))
							{
								values[k][i] += values[j - 1][i];
								values[j - 1][i] = 0;
								
								updateScore(values[k][i]);
							}
						}
					}
					
				}
				
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}

	private void stepUp()
	{
		ObservableList<Node> nodes = grid.getChildren();
		int[][] values = createNodeArray(nodes);

		for (int i = 0; i < 4; i++)
		{
				for (int j = 0; j < 4; j++) //tab[j][i]
				{
					if (j < 3 && values[j][i] == values[j + 1][i] && values[j][i] != 0) //górna == górna + 1
					{
						values[j][i] += values[j + 1][i];
						values[j + 1][i] = 0;
						
						updateScore(values[j][i]);
					}
					
					if (j > 0 && values[j - 1][i] == 0 && values[j][i] != 0)
					{
						for (int k = j; k >= 0; k--)
						{
							if (values[k][i] == 0 && (k == 0 || values[k - 1][i] != 0))
							{
								values[k][i] += values[j][i];
								values[j][i] = 0;
								
								updateScore(values[k][i]);
							}
						}
					}
				}
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}

	private void stepRight()
	{
		ObservableList<Node> nodes = grid.getChildren();
		int[][] values = createNodeArray(nodes);

		for (int i = 0; i < 4; i++)
		{
				for (int j = 3; j > 0; j--) //tab[j][i]
				{
					if (values[i][j] == values[i][j - 1] && values[i][j] != 0) //dolna == dolna - 1
					{
						values[i][j] += values[i][j - 1];
						values[i][j - 1] = 0;
						
						updateScore(values[i][j]);
					}
					
					if (values[i][j - 1] != 0 && values[i][j] == 0)
					{
						for (int k = j; k < 4; k++)
						{
							if (values[i][k] == 0 && (k == 3 || values[i][k + 1] != 0))
							{
								values[i][k] += values[i][j - 1];
								values[i][j - 1] = 0;
								
								updateScore(values[i][k]);
							}
						}
					}
					
				}
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}

	private void stepLeft()
	{
		ObservableList<Node> nodes = grid.getChildren();
		int[][] values = createNodeArray(nodes);

		for (int i = 0; i < 4; i++)
		{
				for (int j = 0; j < 4; j++) //tab[j][i]
				{
					if (j < 3 && values[i][j] == values[i][j + 1] && values[i][j] != 0) //górna == górna + 1
					{
						values[i][j] += values[i][j + 1];
						values[i][j + 1] = 0;
						
						updateScore(values[i][j]);
					}
					
					if (j > 0 && values[i][j - 1] == 0 && values[i][j] != 0)
					{
						for (int k = j; k >= 0; k--)
						{
							if (values[i][k] == 0 && (k == 0 || values[i][k - 1] != 0))
							{
								values[i][k] += values[i][j];
								values[i][j] = 0;
								
								updateScore(values[i][k]);
							}
						}
					}
				}
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}
	
	private void updateScore(int points)
	{
		score += points;
		ScoreLabel.setText("Wynik: " + score);
	}
	
	private void fillRandomFreeCell()
	{
		ObservableList<Node> nodes = grid.getChildren();
		List<Integer> freeCells = new ArrayList<>();
		
		String value;
		String text; //end label
		for (int i = 0; i < nodes.size(); i++)
		{
			Button button = (Button) nodes.get(i);
			value = button.getText();
			
			if (value.equals(""))
				freeCells.add(i);
		}
		
		if (freeCells.size() > 0)
		{
			Collections.shuffle(freeCells);
			
			Button drawnCell = (Button) nodes.get(freeCells.get(0)); //get first value from shuffled pool
			drawnCell.setText("2");
			setButtonColor(drawnCell, 2);
		}
		else if (isLoser()) //TODO
		{
			text = "Przegrałeś! Chcesz Spróbować ponownie?";
			FlowPane layout = new FlowPane();
			endStatementLabel = new Label(text);
			endStatementLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-size: 32;");
			layout.getChildren().add(endStatementLabel);
			gamePane.getChildren().remove(grid);
//				gamePane.getChildren().add(layout);
			gamePane.setCenter(endStatementLabel);
			
		}
		
		if (isWinner())
		{
			text = "Wygrałeś!!!";
			FlowPane layout = new FlowPane();
			endStatementLabel = new Label();
			endStatementLabel.setStyle("-fx-text-fill: #09B079; -fx-font-size: 32;");
			layout.getChildren().add(endStatementLabel);
			gamePane.setBottom(layout);
		}
	}
	
	private boolean isLoser()
	{
		boolean lost = true;
		ObservableList<Node> nodes = grid.getChildren();
		
		for (int i = 0; i < nodes.size(); i++)
		{
			String first = ((Button) nodes.get(i)).getText();
			if ((i + 1) % 4 != 0)
			{
				String rightNeighbour = ((Button) nodes.get(i + 1)).getText();
				
				if (first.equals(rightNeighbour))
				{
					lost = false;
					break;
				}
			}
			
			if (i < 12)
			{
				String bottomNeighbour = ((Button) nodes.get(i + 4)).getText();
				if (first.equals(bottomNeighbour))
				{
					lost = false;
					break;
				}
			}
		}
		
		return lost;
	}

	private boolean isWinner()
	{
		boolean won = false;
		for (Node node : grid.getChildren())
		{
			Button button = (Button) node;
			if (button.getText().equals("2048"))
				won = true;
		}
		
		return won;
	}
	
	/*
	 * 
	 * Updating values
	 */
	private void updateValues(ObservableList<Node> nodes, int[][] values)
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				Button button = ((Button) nodes.get(i * 4 + j));
				button.setText(values[i][j] != 0 ? String.valueOf(values[i][j]) : "");
				setButtonColor(button, values[i][j]);
			}
		}
		
	}

	private int[][] createNodeArray(ObservableList<Node> nodes)
	{
		String txt;
		int[][] values = new int[4][4];
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				txt = ((Button) nodes.get(i * 4 + j)).getText();
				values[i][j] = (!txt.equals("") ? Integer.parseInt(txt) : 0);
			}
		}
		
		return values;
	}

	private void initializeGrid()
	{
		List<Integer> drawnList = randomize();
		
		int i = 0;
		for (Node node : grid.getChildren())
		{
			for (int value : drawnList)
			{
				if (value == i)
				{
					Button button = (Button) node;
					button.setText("2");
					setButtonColor(button, 2);
					break;
				}
			}
			i++;
		}
	}
	
	private void restart()
	{
		//if the game was ended and grid was replaced with label statement
		if (!gamePane.getChildren().contains(grid))
		{
			gamePane.getChildren().remove(endStatementLabel);
			gamePane.getChildren().add(grid);
		}
		
		for (Node node : grid.getChildren())
		{
			Button button = (Button) node;
			button.setText("");
			button.setStyle("-fx-background-color: #828282");
		}
		
		initializeGrid();
		updateScore(-score);
	}

	private List<Integer> randomize()
	{
		List<Integer> range = IntStream.range(0, 16).boxed()
		        .collect(Collectors.toCollection(ArrayList::new));
		Collections.shuffle(range);
		List<Integer> drawnList = new ArrayList<>(range.subList(0, 5));

		
		return drawnList;
	}
	
	private void setButtonColor(Button button, int color)
	{
		button.setStyle("-fx-background-color: " + colors.get(color));
	}

}

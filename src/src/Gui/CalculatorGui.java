package Gui;

import Calculating.CalculatingService;
import Constants.CommonConstant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGui extends JFrame implements ActionListener {
    private final SpringLayout springLayout = new SpringLayout();
    private CalculatingService calculatingService;

    // display field
    private  JTextField displayField;

    //buttons
    private JButton[] buttons;

    // flags
    private boolean pressedOperator = false;
    private boolean pressedEquals = false;

    public CalculatorGui() {
        super(CommonConstant.APP_NAME);
        setSize(CommonConstant.APP_SIZE[0], CommonConstant.APP_SIZE[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(springLayout);

        calculatingService = new CalculatingService();

        addGuiComponents();
    }

    public void addGuiComponents() {
        // add display components
        addDisplayFieldComponents();

        // add button components
        addBurronComponents();
    }

    public void addDisplayFieldComponents() {
        JPanel displayFieldPanel = new JPanel();
        displayField = new JTextField(CommonConstant.TEXTFIELD_LENGTH);
        displayField.setFont(new Font("Dialog", Font.PLAIN, CommonConstant.TEXTFIELD_FONTSIZE));
        displayField.setEditable(false);
        displayField.setText("0");
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);

        displayFieldPanel.add(displayField);

        this.getContentPane().add(displayFieldPanel);
        springLayout.putConstraint(SpringLayout.NORTH, displayFieldPanel, CommonConstant.TEXTFIELD_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, displayFieldPanel, CommonConstant.TEXTFIELD_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);
    }

    public void addBurronComponents() {
        GridLayout gridLayout = new GridLayout(CommonConstant.BUTTON_ROWCOUNT, CommonConstant.BUTTON_COLCOUNT);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(gridLayout);
        buttons = new JButton[CommonConstant.BUTTON_COUNT];
        for (int i = 0; i < CommonConstant.BUTTON_COUNT; i++)
        {
            JButton button = new JButton(getButtonLabel(i));
            button.setFont(new Font("Dialog", Font.PLAIN, CommonConstant.BUTTON_FONTSIZE));
            button.addActionListener(this);

            buttonPanel.add(button);
        }

        gridLayout.setHgap(CommonConstant.BUTTON_HGAP);
        gridLayout.setVgap(CommonConstant.BUTTON_VGAP);

        this.getContentPane().add(buttonPanel);

        springLayout.putConstraint(SpringLayout.NORTH, buttonPanel, CommonConstant.BUTTON_SPRINGLAYUOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonPanel, CommonConstant.BUTTON_SPRINGLAYUOUT_WESTPAD, SpringLayout.WEST, this);
    }

    public String getButtonLabel(int buttonIndex)
    {
        switch (buttonIndex)
        {
            case 0:
                return "7";
            case 1:
                return "8";
            case 2:
                return "9";
            case 3:
                return "/";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "x";
            case 8:
                return "1";
            case 9:
                return "2";
            case 10:
                return "3";
            case 11:
                return "-";
            case 12:
                return "0";
            case 13:
                return ".";
            case 14:
                return "+";
            case 15:
                return "=";
        }
        return "";
    }



    @Override
    public void actionPerformed (ActionEvent e)
    {
        String buttonCommand = e.getActionCommand();
        if (buttonCommand.matches("[0-9]"))
        {
            if (pressedEquals || pressedOperator || displayField.getText().equals("0"))
                displayField.setText(buttonCommand);
            else
                displayField.setText(displayField.getText() + buttonCommand);

            // update flags
            pressedOperator = false;
            pressedEquals = false;
        } else if (buttonCommand.equals("="))
        {
            // calculate
            calculatingService.setNum2(Double.parseDouble(displayField.getText()));

            double result = 0;
            switch (calculatingService.getMathSymbol())
            {
                case '+':
                    result = calculatingService.add();
                    break;
                case '-':
                    result = calculatingService.subtract();
                    break;
                case 'x':
                    result = calculatingService.multiplay();
                    break;
                case '/':
                    result = calculatingService.divide();
                    break;
            }

            // update the display field
            displayField.setText(Double.toString(result));

            // update flags
            pressedEquals = true;
            pressedOperator = false;
        } else if (buttonCommand.equals("."))
        {
            if(!displayField.getText().contains("."))
            {
                displayField.setText(displayField.getText() + buttonCommand);
            }
        } else
        {
            // operator
            if(!pressedOperator)
                calculatingService.setNum1(Double.parseDouble(displayField.getText()));
            calculatingService.setMathSymbol(buttonCommand.charAt(0));

            // update flags
            pressedOperator = true;
            pressedEquals = false;

        }
    }
}

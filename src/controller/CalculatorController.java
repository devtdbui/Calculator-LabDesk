/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gui.CalculatorFrm;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.swing.JButton;

/**
 *
 * @author DELL
 */
public class CalculatorController {

    CalculatorFrm cal = new CalculatorFrm();

    boolean process;
    boolean reset;
    boolean isMR = false;
    BigDecimal firstNum;
    BigDecimal secondNum;
    BigDecimal memory = new BigDecimal("0");
    int operator = -1;

    public CalculatorController() {
        cal.getTxtDisplay().setText("0");
        addButtonsAction();
        cal.setVisible(true);
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void addAction(JButton btn) {

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BigDecimal temp;
                String value = btn.getText();
                if (process || reset) {
                    cal.getTxtDisplay().setText("0");
                    process = false;
                    reset = false;
                }
                isMR = false;
                temp = new BigDecimal(cal.getTxtDisplay().getText() + value);
                cal.getTxtDisplay().setText(temp + "");
            }
        });
    }

    public void addButtonsAction() {
        addAction(cal.getBtn0());
        addAction(cal.getBtn1());
        addAction(cal.getBtn2());
        addAction(cal.getBtn3());
        addAction(cal.getBtn4());
        addAction(cal.getBtn5());
        addAction(cal.getBtn6());
        addAction(cal.getBtn7());
        addAction(cal.getBtn8());
        addAction(cal.getBtn9());
        pressDot(cal.getBtnDot());
        pressEqual(cal.getBtnEqual());
        pressInvert(cal.getBtnInvert());
        pressNegate(cal.getBtnOpposite());
        pressPercent(cal.getBtnPercent());
        pressSqrt(cal.getBtnSquare());
        pressAdd(cal.getBtnAdd());
        pressSub(cal.getBtnSubtract());
        pressMul(cal.getBtnMultiply());
        pressDiv(cal.getBtnDiv());
        pressClear(cal.getBtnClear());
        pressDelete(cal.getBtnDelete());
        pressCE(cal.getBtnCE());
    }

    public void pressClear(JButton btnClear) {
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                process = false;
                operator = -1;
                firstNum = new BigDecimal("0");
                secondNum = new BigDecimal("0");
                cal.txtDisplay.setText("0");
            }
        });

    }

    public void pressDelete(JButton btnDelete) {
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String txt = cal.txtDisplay.getText();
                if (txt.length() == 2 && txt.startsWith("-")) {
                    cal.txtDisplay.setText("0");
                    return;
                }
                if (txt.length() == 1) {
                    cal.txtDisplay.setText("0");
                    return;
                }
                txt = txt.substring(0, txt.length() - 1);
                cal.txtDisplay.setText(txt);
            }
        });

    }

    public void pressCE(JButton btnCE) {
        btnCE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cal.txtDisplay.setText(0 + "");
            }
        });

    }

    public void pressDot(JButton btn) {

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (process || reset) {
                    cal.getTxtDisplay().setText("0");
                    process = false;
                    reset = false;
                }
                if (!cal.getTxtDisplay().getText().contains(".")) {
                    cal.getTxtDisplay().setText(cal.getTxtDisplay().getText() + ".");
                }
            }
        });
    }

    public BigDecimal getValue() {
        if (isMR) {
            return memory;
        }
        String value = cal.getTxtDisplay().getText();
        BigDecimal temp;
        try {
            temp = new BigDecimal(value).stripTrailingZeros();
        } catch (Exception e) {
            return firstNum;
        }
        return temp;
    }

    public void processing() {
        if (!process) {
            if (operator == -1) {
                firstNum = getValue();
            } else {
                secondNum = getValue();
                switch (operator) {
                    case 1:
                        // to add value
                        firstNum = firstNum.add(secondNum, MathContext.DECIMAL64).stripTrailingZeros();
                        break;
                    case 2:
                        // to sub value
                        firstNum = firstNum.subtract(secondNum, MathContext.DECIMAL64).stripTrailingZeros();
                        break;
                    case 3:
                        // to multi value
                        firstNum = firstNum.multiply(secondNum, MathContext.DECIMAL32).stripTrailingZeros();
                        break;
                    case 4:
                        // to divide value
                        if (secondNum.compareTo(BigDecimal.ZERO) != 0) {

                            firstNum = firstNum.divide(secondNum, MathContext.DECIMAL64).stripTrailingZeros();
                            break;
                        } else {
                            cal.getTxtDisplay().setText("Error");
                            process = true;
                            return;
                        }
                }
            }
            cal.getTxtDisplay().setText(firstNum.toPlainString());
            process = true;
        }

    }

    public void checkError() {
        if (!cal.getTxtDisplay().getText().equals("Error")) {
            processing();

            operator = -1;
        } else {
            cal.getTxtDisplay().setText(firstNum.toPlainString() + "");
        }

    }

    public void pressEqual(JButton btn) {
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkError();
            }
        });
    }

    public void pressNegate(JButton btn) {

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkError();
                cal.getTxtDisplay().setText(getValue().negate().toPlainString());
                process = false;
                reset = true;
            }
        });
    }

    public void pressSqrt(JButton btn) {

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkError();

                if (getValue().doubleValue() >= 0) {
                    String display = BigDecimal.valueOf(Math.sqrt(getValue().doubleValue())).stripTrailingZeros().toPlainString();

                    cal.getTxtDisplay().setText(display);

                    process = false;
                } else {
                    cal.getTxtDisplay().setText("ERROR");
                }
                reset = true;
            }
        });
    }

    public void pressPercent(JButton btn) {

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkError();

                cal.getTxtDisplay().setText(BigDecimal.valueOf(getValue().doubleValue() / 100).stripTrailingZeros().toPlainString());
                process = false;
                reset = true;
            }
        });
    }

    public void pressInvert(JButton btn) {

        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Double value = getValue().doubleValue();
                if (value != 0) {
                    String display = 1 / value + "";
                    if(display.endsWith(".0")){
                        display = display.replace(".0", "");
                    }

                    cal.getTxtDisplay().setText(display + "");
//                    process = false;
                } else {
                    cal.getTxtDisplay().setText("Error");
                }
//                reset = true;

            }
        });
    }

    public void pressAdd(JButton btn) {
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                processing();
                setOperator(1);
            }
        });
    }

    public void pressSub(JButton btn) {
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                processing();
                setOperator(2);
            }
        });

    }

    public void pressMul(JButton btn) {
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                processing();
                setOperator(3);
            }
        });

    }

    public void pressDiv(JButton btn) {
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                processing();
                setOperator(4);
            }
        });

    }

}

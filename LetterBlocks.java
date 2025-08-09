import java.awt.*;
import javax.swing.*;
public class LetterBlocks extends JPanel {
    private JButton[] ltrBtns;
    private char slctLtr = ' ';
    public LetterBlocks(char[] ltr) {
        setLayout(new GridLayout(2, 13)); 
        ltrBtns = new JButton[ltr.length];
        for (int i = 0; i < ltr.length; i++) {
            ltrBtns[i] = new JButton(String.valueOf(ltr[i]));
            ltrBtns[i].setFont(new Font("Arial", Font.PLAIN, 24));
            ltrBtns[i].setPreferredSize(new Dimension(60, 60));
            int index = i;
            ltrBtns[i].addActionListener(e -> slctLtr = ltr[index]);
            add(ltrBtns[i]);
        }
    }
    public char getSelectedLtr() {
        return slctLtr;
    }
}


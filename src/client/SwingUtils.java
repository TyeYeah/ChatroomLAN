package client;


import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class SwingUtils {

    /**
     * press enter to trigger assigned button's event 
     * @param button
     */
    public static void enterPressesWhenFocused(JButton button) {
        button.registerKeyboardAction(button.getActionForKeyStroke(KeyStroke
                .getKeyStroke(KeyEvent.VK_SPACE, 0, false)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED);

        button.registerKeyboardAction(button.getActionForKeyStroke(KeyStroke
                .getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
    }

    /**
     * press enter in the textPane to trigger assigned event
     * @param textField (use textPane to adapt this program)
     * @param actionListener
     */
    public static void enterPressesWhenFocused(JTextPane textField,
            ActionListener actionListener) {
        textField.registerKeyboardAction(actionListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED);

        textField.registerKeyboardAction(actionListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
    }

}
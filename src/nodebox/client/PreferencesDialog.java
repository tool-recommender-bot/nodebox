package nodebox.client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesDialog extends JDialog implements ActionListener {

    private Preferences preferences;
    private JCheckBox enablePaneCustomizationCheck;
    private JCheckBox enableMovieExportCheck;
    private JButton saveButton;
    private JButton cancelButton;

    public PreferencesDialog() {
        super((Frame) null, "Preferences");
        setLocationRelativeTo(null);
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        enablePaneCustomizationCheck = new JCheckBox("Enable Pane Customization");
        rootPanel.add(enablePaneCustomizationCheck);

        rootPanel.add(Box.createVerticalStrut(10));

        // TODO: provide tooltip indicating movie export is an experimental feature.
        enableMovieExportCheck = new JCheckBox("Enable Movie Export");
        rootPanel.add(enableMovieExportCheck);

        rootPanel.add(Box.createVerticalStrut(100));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        rootPanel.add(buttonPanel);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);
        getRootPane().setDefaultButton(saveButton);

        readPreferences();

        setContentPane(rootPanel);
        setResizable(false);
        pack();
    }

    private void readPreferences() {
        this.preferences = Preferences.userNodeForPackage(Application.class);
        enablePaneCustomizationCheck.setSelected(Boolean.valueOf(preferences.get(Application.PREFERENCE_ENABLE_PANE_CUSTOMIZATION, "false")));
        enableMovieExportCheck.setSelected(Boolean.valueOf(preferences.get(Application.PREFERENCE_ENABLE_MOVIE_EXPORT, "true")));
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == saveButton) {
            setEnablePaneCustomization(enablePaneCustomizationCheck.isSelected());
            setEnableMovieExport(enableMovieExportCheck.isSelected());
            JOptionPane.showMessageDialog(this, "Please restart NodeBox for the changes to take effect.");
            try {
                preferences.flush();
            } catch (BackingStoreException e) {
                throw new RuntimeException(e);
            }
        }
        dispose();
    }

    private void setEnablePaneCustomization(boolean enabled) {
        Application.ENABLE_PANE_CUSTOMIZATION = enabled;
        preferences.put(Application.PREFERENCE_ENABLE_PANE_CUSTOMIZATION, Boolean.toString(enabled));
    }

    private void setEnableMovieExport(boolean enabled) {
        Application.ENABLE_MOVIE_EXPORT = enabled;
        preferences.put(Application.PREFERENCE_ENABLE_MOVIE_EXPORT, Boolean.toString(enabled));
    }
}

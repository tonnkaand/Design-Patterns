
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.border.*;

public class App {

    // === SINGLETON PERSISTANT ===
    static class DatabaseManager {

        private static DatabaseManager instance;
        private Connection connection;
        private Preferences prefs;
        private boolean connected = false;
        private String currentUser = null;

        private DatabaseManager() {
            prefs = Preferences.userNodeForPackage(App.class);
            System.out.println("🎯 Singleton DatabaseManager créé");
            autoConnect();
        }

        public static synchronized DatabaseManager getInstance() {
            if (instance == null) {
                instance = new DatabaseManager();
            }
            return instance;
        }

        public boolean isUserRegistered() {
            return prefs.get("registered", "false").equals("true");
        }

        public boolean registerUser(String username, String password, String email) {
            try {
                if (!connected) {
                    Class.forName("org.h2.Driver");
                    String url = "jdbc:h2:./database/singleton_users";
                    Connection tempConn = DriverManager.getConnection(url, "sa", "");

                    Statement stmt = tempConn.createStatement();
                    stmt.execute("CREATE TABLE IF NOT EXISTS app_users ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "username VARCHAR(50) UNIQUE, "
                            + "password VARCHAR(100), "
                            + "email VARCHAR(100), "
                            + "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

                    ResultSet rs = stmt.executeQuery(
                            "SELECT COUNT(*) FROM app_users WHERE username = '" + username + "'");
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        tempConn.close();
                        return false;
                    }

                    stmt.execute("INSERT INTO app_users (username, password, email) VALUES ("
                            + "'" + username + "', '" + password + "', '" + email + "')");

                    tempConn.close();

                    prefs.put("db_url", url);
                    prefs.put("db_user", "sa");
                    prefs.put("db_pass", "");
                    prefs.put("app_username", username);
                    prefs.put("app_password", password);
                    prefs.put("registered", "true");

                    return connect(url, "sa", "");
                }
                return true;

            } catch (Exception e) {
                System.err.println("❌ Erreur inscription: " + e.getMessage());
                return false;
            }
        }

        private void autoConnect() {
            if (isUserRegistered()) {
                String savedUrl = prefs.get("db_url", null);
                String savedUser = prefs.get("db_user", null);
                String savedPass = prefs.get("db_pass", null);

                if (savedUrl != null && savedUser != null) {
                    System.out.println("🔄 Tentative de reconnexion automatique...");
                    if (connect(savedUrl, savedUser, savedPass)) {
                        currentUser = prefs.get("app_username", null);
                        System.out.println("👤 Utilisateur reconnecté: " + currentUser);
                    }
                }
            }
        }

        public boolean connect(String url, String user, String pass) {
            if (connected) {
                System.out.println("✅ Déjà connecté en tant que: " + currentUser);
                return true;
            }

            try {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(url, user, pass);
                connected = true;

                System.out.println("🔗 Connecté à: " + url);
                return true;
            } catch (Exception e) {
                System.err.println("❌ Erreur: " + e.getMessage());
                return false;
            }
        }

        // === AJOUT DE LA MÉTHODE executeQuery MANQUANTE ===
        public String executeQuery(String sql) {
            if (!connected) {
                return "⚠️ Non connecté à la base de données";
            }

            try {
                Statement stmt = connection.createStatement();

                if (sql.trim().toUpperCase().startsWith("SELECT")) {
                    ResultSet rs = stmt.executeQuery(sql);
                    return formatResult(rs);
                } else {
                    int rows = stmt.executeUpdate(sql);
                    return "✅ " + rows + " ligne(s) affectée(s)";
                }
            } catch (SQLException e) {
                return "❌ Erreur SQL: " + e.getMessage();
            }
        }

        private String formatResult(ResultSet rs) throws SQLException {
            StringBuilder sb = new StringBuilder();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            // En-têtes
            for (int i = 1; i <= cols; i++) {
                sb.append(String.format("%-20s", meta.getColumnName(i)));
            }
            sb.append("\n").append("-".repeat(cols * 20)).append("\n");

            // Données
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    sb.append(String.format("%-20s", rs.getString(i)));
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        public void deleteAccount() {
            if (connected && connection != null) {
                try {
                    // Supprimer l'utilisateur de la base
                    Statement stmt = connection.createStatement();
                    if (currentUser != null) {
                        stmt.execute("DELETE FROM app_users WHERE username = '" + currentUser + "'");
                    }

                    // Fermer la connexion
                    connection.close();
                    connected = false;

                    // Supprimer toutes les préférences
                    prefs.remove("db_url");
                    prefs.remove("db_user");
                    prefs.remove("db_pass");
                    prefs.remove("app_username");
                    prefs.remove("app_password");
                    prefs.remove("registered");

                    currentUser = null;
                    System.out.println("🗑️ Compte supprimé");
                } catch (SQLException e) {
                    System.err.println("⚠️ Erreur suppression compte");
                }
            }
        }

        public boolean isConnected() {
            return connected;
        }

        public String getStatus() {
            if (!connected) {
                return "❌ Déconnecté";
            }
            return "✅ Connecté (" + (currentUser != null ? currentUser : "admin") + ")";
        }

        public String getCurrentUser() {
            return currentUser;
        }
    }

    // === INTERFACE AVEC INSCRIPTION ===
    public static void main(String[] args) {
        System.out.println("🚀 DÉMARRAGE APPLICATION SINGLETON");

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🎨 Configuration de l'interface...");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("✅ Look and feel configuré");

                JFrame frame = new JFrame("🔐 Singleton DB - Inscription");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 500);

                System.out.println("✅ Frame créée");

                // Panel principal avec fond dégradé
                JPanel mainPanel = new GradientPanel();
                mainPanel.setLayout(new BorderLayout());

                // Vérifier si déjà inscrit
                DatabaseManager db = DatabaseManager.getInstance();
                boolean alreadyRegistered = db.isUserRegistered();

                System.out.println("📊 État: " + (alreadyRegistered ? "Déjà inscrit" : "Nouvel utilisateur"));

                if (alreadyRegistered && db.isConnected()) {
                    showMainInterface(frame, db, mainPanel);
                } else {
                    showRegistrationInterface(frame, db, mainPanel);
                }

                frame.add(mainPanel);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // FORCER LE REDESSIN
                frame.revalidate();
                frame.repaint();

                System.out.println("✅ Interface affichée et redessinée");

            } catch (Exception e) {
                System.err.println("❌ ERREUR: " + e.getMessage());
                e.printStackTrace();
            }
        });

        System.out.println("🏁 Main terminé - Attente événements Swing");
    }

    static void showRegistrationInterface(JFrame frame, DatabaseManager db, JPanel mainPanel) {
        System.out.println("📝 Affichage interface d'inscription");

        mainPanel.removeAll();

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel title = new JLabel("📝 INSCRIPTION UTILISATEUR");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);

        // Panel d'inscription
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setOpaque(false);
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Carte
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel cardTitle = new JLabel("Créer votre compte");
        cardTitle.setFont(new Font("Arial", Font.BOLD, 18));
        cardTitle.setForeground(new Color(52, 152, 219));
        card.add(cardTitle, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Nom d'utilisateur:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        card.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 30));
        card.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passLabel = new JLabel("Mot de passe:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        card.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel confirmLabel = new JLabel("Confirmer:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(confirmLabel, gbc);

        gbc.gridx = 1;
        JPasswordField confirmField = new JPasswordField(20);
        confirmField.setPreferredSize(new Dimension(200, 30));
        card.add(confirmField, gbc);

        // Bouton
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton registerBtn = new JButton("📝 S'inscrire");
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        registerBtn.setPreferredSize(new Dimension(200, 40));
        registerBtn.setFocusPainted(false);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "⚠️ Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(frame, "⚠️ Les mots de passe ne correspondent pas", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = db.registerUser(username, password, email);
            if (success) {
                JOptionPane.showMessageDialog(frame, "✅ Inscription réussie!\nConnexion automatique...", "Succès", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                launchMainApp();
            } else {
                JOptionPane.showMessageDialog(frame, "❌ Échec de l'inscription\nL'utilisateur existe peut-être déjà", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(registerBtn, gbc);

        // Info
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("🎯 Après inscription, connexion automatique Singleton");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        infoLabel.setForeground(new Color(100, 100, 100));
        card.add(infoLabel, gbc);

        registerPanel.add(card);
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(registerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        JLabel footerLabel = new JLabel("Une seule connexion - Une seule instance");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        mainPanel.add(footer, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();

        System.out.println("✅ Interface d'inscription affichée");
    }

    static void showMainInterface(JFrame frame, DatabaseManager db, JPanel mainPanel) {
        System.out.println("🔐 Affichage interface principale");

        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("🔐 SESSION UTILISATEUR");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel userLabel = new JLabel("👤 " + db.getCurrentUser());
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        header.add(userLabel, BorderLayout.EAST);

        // Carte principale
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Statut
        JLabel statusLabel = new JLabel("Statut: " + db.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(db.isConnected() ? Color.GREEN : Color.RED);
        card.add(statusLabel, BorderLayout.NORTH);

        // Boutons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);

        JButton testBtn = createSimpleButton("🧪 Tester Singleton");
        JButton queryBtn = createSimpleButton("📊 Voir utilisateurs");
        JButton deleteBtn = createSimpleButton("🗑️ Supprimer compte");

        testBtn.addActionListener(e -> {
            DatabaseManager db1 = DatabaseManager.getInstance();
            DatabaseManager db2 = DatabaseManager.getInstance();

            String msg = "🧪 TEST DU SINGLETON\n\n"
                    + "Instance 1: " + System.identityHashCode(db1) + "\n"
                    + "Instance 2: " + System.identityHashCode(db2) + "\n"
                    + "Utilisateur: " + db1.getCurrentUser() + "\n\n";

            if (db1 == db2) {
                msg += "✅ MÊME INSTANCE\nLe Singleton fonctionne!\n"
                        + "Une seule connexion à la base de données.";
            } else {
                msg += "❌ INSTANCES DIFFÉRENTES";
            }

            JOptionPane.showMessageDialog(frame, msg, "Test Singleton", JOptionPane.INFORMATION_MESSAGE);
        });

        queryBtn.addActionListener(e -> {
            String result = db.executeQuery("SELECT * FROM app_users");
            JTextArea area = new JTextArea(result);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(frame, scroll, "Utilisateurs inscrits", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "⚠️ SUPPRIMER VOTRE COMPTE ?\n\n"
                    + "Cette action est irréversible.\n"
                    + "Toutes vos données seront effacées.",
                    "Confirmation suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                db.deleteAccount();
                JOptionPane.showMessageDialog(frame,
                        "🗑️ Compte supprimé\nL'application va redémarrer",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);

                frame.dispose();
                // Redémarrer l'application
                main(new String[]{});
            }
        });

        buttonPanel.add(testBtn);
        buttonPanel.add(queryBtn);
        buttonPanel.add(deleteBtn);

        card.add(buttonPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(card);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        JLabel footerLabel = new JLabel("🎯 Connexion Singleton active - Redémarrez pour tester la reconnexion automatique");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        mainPanel.add(footer, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();

        System.out.println("✅ Interface principale affichée");
    }

    static JButton createSimpleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));

        // Effet survol
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });

        return btn;
    }

    static void launchMainApp() {
        JFrame newFrame = new JFrame("🔐 Singleton DB - Session");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(600, 500);

        DatabaseManager db = DatabaseManager.getInstance();

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        showMainInterface(newFrame, db, mainPanel);

        newFrame.add(mainPanel);
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);

        newFrame.revalidate();
        newFrame.repaint();
    }

    static class GradientPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            Color color1 = new Color(52, 152, 219);
            Color color2 = new Color(41, 128, 185);
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

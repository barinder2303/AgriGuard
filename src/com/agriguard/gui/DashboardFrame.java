package com.agriguard.gui;

import com.agriguard.dao.DashboardDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFrame extends JFrame {

    private static final Color BG = new Color(242, 247, 243);
    private static final Color SIDEBAR = new Color(7, 43, 32);
    private static final Color GREEN = new Color(36, 132, 70);
    private static final Color LIME = new Color(139, 231, 107);
    private static final Color TEXT = new Color(23, 35, 29);
    private static final Color MUTED = new Color(111, 125, 117);
    private static final Color BORDER = new Color(229, 237, 231);
    private static final Color WHITE = Color.WHITE;

    private final DashboardDAO dao = new DashboardDAO();

    private JLabel totalValue;
    private JLabel riskValue;
    private JLabel buyersValue;
    private JLabel storageValue;
    private JLabel dateTimeLabel;

    private String latestRisk = "No risk analysis saved yet.";
    private String recentActivity = "No recent activity available.";

    public DashboardFrame() {
        setTitle("AgriGuard | Smart Agriculture Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1536, 960);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        try {
            latestRisk = safe(
                    dao.getLatestRiskSummary(),
                    "No risk analysis saved yet."
            );

            recentActivity = safe(
                    dao.getRecentActivity(),
                    "No recent activity available."
            );
        } catch (Exception ignored) {
        }

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(createSidebar(), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.add(createTopBar(), BorderLayout.NORTH);

        // Scrollable dashboard content
        JPanel content = new ScrollablePanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(14, 16, 18, 16));

        content.add(createHero());
        content.add(Box.createVerticalStrut(12));
        content.add(createStats());
        content.add(Box.createVerticalStrut(12));
        content.add(createMiddlePanels());
        content.add(Box.createVerticalStrut(12));
        content.add(createQuickActions());

        JScrollPane scroll = new JScrollPane(
                content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.getVerticalScrollBar().setBlockIncrement(100);
        scroll.setWheelScrollingEnabled(true);

        main.add(scroll, BorderLayout.CENTER);
        root.add(main, BorderLayout.CENTER);
        setContentPane(root);

        refreshValues();

        Timer timer = new Timer(30000, e -> {
            updateClock();
            refreshValues();
        });
        timer.start();
    }

    // SIDEBAR

    private JPanel createSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(250, 0));
        side.setBackground(SIDEBAR);
        side.setBorder(new EmptyBorder(18, 12, 16, 12));

        JPanel brand = new JPanel(new BorderLayout(10, 0));
        brand.setOpaque(false);
        brand.setBorder(new EmptyBorder(0, 3, 22, 3));
        brand.add(new LeafLogo(), BorderLayout.WEST);

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("AgriGuard");
        name.setFont(new Font("Segoe UI", Font.BOLD, 27));
        name.setForeground(WHITE);

        JLabel tagline = new JLabel("SMART AGRICULTURE PLATFORM");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        tagline.setForeground(new Color(166, 195, 177));

        brandText.add(name);
        brandText.add(Box.createVerticalStrut(3));
        brandText.add(tagline);

        brand.add(brandText, BorderLayout.CENTER);
        side.add(brand, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));

        nav.add(navButton("⌂", "Dashboard", true,
                this::refreshDashboard));

        nav.add(navButton("❧", "Produce Management", false,
                () -> openFrame(new ProduceFrame())));

        nav.add(navButton("⬡", "Risk Analysis", false,
                () -> openFrame(new RiskAnalysisFrame())));

        nav.add(navButton("◎", "ML Prediction", false,
                () -> openFrame(new MLPredictionFrame())));

        nav.add(navButton("⌂", "Storage Management", false,
                () -> openFrame(new StorageFrame())));

        nav.add(navButton("⇄", "Smart Allocation", false,
                () -> openFrame(new AllocationFrame())));

        nav.add(navButton("♟", "Buyer Management", false,
                () -> openFrame(new BuyerFrame())));

        nav.add(navButton("▥", "Reports", false,
                () -> JOptionPane.showMessageDialog(
                        this,
                        "Reports overview is available through the dashboard panels.",
                        "Reports",
                        JOptionPane.INFORMATION_MESSAGE
                )));

        side.add(nav, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        JPanel user = new JPanel(new BorderLayout(10, 0));
        user.setOpaque(false);
        user.setBorder(new EmptyBorder(14, 5, 12, 5));

        JLabel avatar = new JLabel("A", SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(35, 87, 63));
        avatar.setForeground(WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 19));
        avatar.setPreferredSize(new Dimension(42, 42));

        JPanel userText = new JPanel();
        userText.setOpaque(false);
        userText.setLayout(new BoxLayout(userText, BoxLayout.Y_AXIS));

        JLabel admin = new JLabel("Admin");
        admin.setForeground(WHITE);
        admin.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel role = new JLabel("System Administrator");
        role.setForeground(new Color(171, 195, 180));
        role.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        userText.add(admin);
        userText.add(Box.createVerticalStrut(4));
        userText.add(role);

        user.add(avatar, BorderLayout.WEST);
        user.add(userText, BorderLayout.CENTER);

        JButton logout = new JButton("↪   Logout");
        logout.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logout.setForeground(WHITE);
        logout.setBackground(new Color(16, 59, 43));
        logout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(56, 104, 78)),
                new EmptyBorder(12, 14, 12, 14)
        ));
        logout.setFocusPainted(false);
        logout.setHorizontalAlignment(SwingConstants.LEFT);

        logout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        footer.add(user);
        footer.add(logout);
        side.add(footer, BorderLayout.SOUTH);

        return side;
    }

    private JButton navButton(
            String icon, String label,
            boolean active, Runnable action) {

        JButton b = new JButton(
                "<html><span style='font-size:16px'>" +
                icon + "</span>&nbsp;&nbsp;&nbsp;" +
                label + "</html>"
        );

        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(active ? WHITE : new Color(222, 237, 226));
        b.setBackground(active ? new Color(31, 83, 59) : SIDEBAR);

        b.setBorder(BorderFactory.createCompoundBorder(
                active
                        ? BorderFactory.createMatteBorder(
                                0, 4, 0, 0, LIME)
                        : BorderFactory.createEmptyBorder(0, 4, 0, 0),
                new EmptyBorder(12, 13, 12, 8)
        ));

        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addActionListener(e -> action.run());

        if (!active) {
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    b.setBackground(new Color(28, 77, 57));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    b.setBackground(SIDEBAR);
                }
            });
        }

        return b;
    }

    // TOP BAR

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(new Color(248, 251, 248));
        bar.setBorder(new EmptyBorder(12, 22, 12, 22));

        JTextField search = new JTextField("  ⌕   Search anything...");
        search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        search.setForeground(new Color(107, 123, 113));
        search.setBackground(WHITE);
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(9, 10, 9, 10)
        ));
        search.setPreferredSize(new Dimension(420, 42));
        bar.add(search, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton bell = new JButton("♧");
        bell.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 22));
        bell.setForeground(SIDEBAR);
        bell.setBackground(new Color(248, 251, 248));
        bell.setBorder(new EmptyBorder(4, 8, 4, 8));
        bell.setFocusPainted(false);
        bell.setToolTipText("Notifications");

        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateTimeLabel.setForeground(TEXT);
        updateClock();

        right.add(bell);
        right.add(dateTimeLabel);
        bar.add(right, BorderLayout.EAST);

        return bar;
    }

    // HERO BANNER

    private JPanel createHero() {
        JPanel hero = new JPanel() {
            private final BufferedImage banner = loadBanner();

            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR
                );

                int w = getWidth();
                int h = getHeight();

                if (banner != null) {
                    double scale = Math.max(
                            (double) w / banner.getWidth(),
                            (double) h / banner.getHeight()
                    );

                    int iw = (int) (banner.getWidth() * scale);
                    int ih = (int) (banner.getHeight() * scale);

                    g.drawImage(
                            banner, (w - iw) / 2,
                            (h - ih) / 2, iw, ih, null
                    );
                } else {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(7, 44, 31),
                            w, h, new Color(83, 130, 58)
                    );
                    g.setPaint(gp);
                    g.fillRect(0, 0, w, h);
                }

                g.setPaint(new GradientPaint(
                        0, 0, new Color(0, 24, 15, 225),
                        w, h, new Color(0, 30, 17, 70)
                ));
                g.fillRect(0, 0, w, h);
                g.dispose();
            }
        };

        hero.setLayout(new BorderLayout());
        hero.setPreferredSize(new Dimension(900, 255));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 255));
        hero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WHITE, 3),
                new EmptyBorder(20, 28, 18, 25)
        ));

        JPanel words = new JPanel();
        words.setOpaque(false);
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome to");
        welcome.setFont(new Font("Segoe UI Light", Font.PLAIN, 38));
        welcome.setForeground(WHITE);

        JLabel brand = new JLabel(
                "<html>Agri<span style='color:#8be76b'>Guard</span></html>"
        );
        brand.setFont(new Font("Segoe UI", Font.BOLD, 46));
        brand.setForeground(WHITE);

        JLabel slogan = new JLabel(
                "Smarter Decisions for a Healthier Tomorrow"
        );
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        slogan.setForeground(WHITE);

        words.add(welcome);
        words.add(brand);
        words.add(Box.createVerticalStrut(3));
        words.add(slogan);
        words.add(Box.createVerticalGlue());

        JPanel features = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 5));
        features.setOpaque(false);

        features.add(feature("♧", "Monitor", "Produce"));
        features.add(feature("⬡", "Analyse", "Risks"));
        features.add(feature("▥", "AI-Powered", "Predictions"));
        features.add(feature("◇", "Manage", "Storage"));
        features.add(feature("⇄", "Smart", "Allocation"));

        words.add(features);
        hero.add(words, BorderLayout.CENTER);

        JLabel script = new JLabel(
                "<html><div style='text-align:right'>" +
                "Grow<br>Store<br>Predict<br>Deliver</div></html>"
        );
        script.setFont(new Font("Serif", Font.ITALIC, 25));
        script.setForeground(new Color(246, 255, 240));
        script.setHorizontalAlignment(SwingConstants.RIGHT);
        script.setVerticalAlignment(SwingConstants.CENTER);
        script.setBorder(new EmptyBorder(0, 10, 0, 0));

        hero.add(script, BorderLayout.EAST);
        return hero;
    }

    private JPanel feature(String icon, String line1, String line2) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        JLabel i = new JLabel(icon, SwingConstants.CENTER);
        i.setFont(new Font("Segoe UI Symbol", Font.BOLD, 21));
        i.setForeground(new Color(190, 255, 174));
        i.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(111, 193, 111)),
                new EmptyBorder(5, 8, 5, 8)
        ));

        JLabel t = new JLabel(
                "<html>" + line1 + "<br>" + line2 + "</html>"
        );
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setForeground(WHITE);

        p.add(i, BorderLayout.WEST);
        p.add(t, BorderLayout.CENTER);
        return p;
    }

    // STATISTICS

    private JPanel createStats() {
        JPanel row = new JPanel(new GridLayout(1, 4, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        totalValue = new JLabel("0 kg");
        riskValue = new JLabel("0");
        buyersValue = new JLabel("0");
        storageValue = new JLabel("0%");

        row.add(statCard("Total Produce", totalValue, "♧",
                new Color(41, 143, 75), new Color(237, 249, 238),
                "Across all recorded crops"));

        row.add(statCard("High-Risk Records", riskValue, "!",
                new Color(223, 72, 35), new Color(255, 243, 237),
                "Need attention"));

        row.add(statCard("Active Buyers", buyersValue, "●",
                new Color(23, 112, 194), new Color(237, 246, 255),
                "Registered buyers"));

        row.add(statCard("Storage Utilization", storageValue, "⌂",
                new Color(112, 75, 192), new Color(246, 241, 255),
                "Recorded capacity usage"));

        return row;
    }

    private JPanel statCard(
            String title, JLabel value, String icon,
            Color accent, Color tint, String hint) {

        JPanel card = new JPanel(new BorderLayout(9, 5));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 12, 14)
        ));

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        heading.setForeground(TEXT);

        JLabel badge = new JLabel(icon, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(tint);
        badge.setForeground(accent);
        badge.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));
        badge.setPreferredSize(new Dimension(52, 52));

        top.add(heading, BorderLayout.CENTER);
        top.add(badge, BorderLayout.WEST);

        value.setFont(new Font("Segoe UI", Font.BOLD, 29));
        value.setForeground(TEXT);

        JLabel sub = new JLabel(hint);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(MUTED);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(value);
        body.add(Box.createVerticalStrut(2));
        body.add(sub);

        card.add(top, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    // MIDDLE PANELS

    private JPanel createMiddlePanels() {
        JPanel row = new JPanel(new GridLayout(1, 3, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 265));

        row.add(panelWithHeader(
                "⬡   Inventory Overview", "View All",
                new InventoryChart()));

        row.add(panelWithHeader(
                "⌂   Storage Status", "View Details",
                new StorageChart()));

        row.add(panelWithHeader(
                "◷   Recent Activity", "View All",
                createActivityBody()));

        return row;
    }

    private JPanel panelWithHeader(
            String title, String link, JComponent body) {

        JPanel panel = new JPanel(new BorderLayout(8, 10));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 12, 14)
        ));

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(TEXT);

        JLabel l = new JLabel(link);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(20, 113, 174));

        head.add(t, BorderLayout.WEST);
        head.add(l, BorderLayout.EAST);

        panel.add(head, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);

        return panel;
    }

    private JComponent createActivityBody() {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(activityRow(
                "♧", new Color(39, 142, 70),
                "Latest saved activity", recentActivity));

        list.add(Box.createVerticalStrut(8));

        list.add(activityRow(
                "!", new Color(224, 83, 39),
                "Risk analysis", latestRisk));

        list.add(Box.createVerticalStrut(8));

        list.add(activityRow(
                "●", new Color(33, 116, 188),
                "System status", "Database-backed dashboard"));

        return list;
    }

    private JPanel activityRow(
            String symbol, Color color,
            String title, String detail) {

        JPanel r = new JPanel(new BorderLayout(8, 0));
        r.setOpaque(false);

        JLabel icon = new JLabel(symbol, SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(new Color(
                color.getRed(), color.getGreen(),
                color.getBlue(), 25
        ));
        icon.setForeground(color);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        icon.setPreferredSize(new Dimension(34, 34));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(clip(title, 25));
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(TEXT);

        JLabel d = new JLabel(clip(detail, 36));
        d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        d.setForeground(MUTED);

        txt.add(t);
        txt.add(Box.createVerticalStrut(3));
        txt.add(d);

        r.add(icon, BorderLayout.WEST);
        r.add(txt, BorderLayout.CENTER);

        return r;
    }

    // QUICK ACTIONS

    private JPanel createQuickActions() {
        JPanel wrapper = new JPanel(new BorderLayout(8, 10));
        wrapper.setBackground(WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 12, 12, 12)
        ));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 132));

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JLabel title = new JLabel("ϟ  Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT);

        JLabel note = new JLabel("Frequently used features");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(MUTED);

        heading.add(title, BorderLayout.WEST);
        heading.add(note, BorderLayout.EAST);

        JPanel actions = new JPanel(new GridLayout(1, 5, 8, 0));
        actions.setOpaque(false);

        actions.add(actionCard("♧", "Add Produce",
                "Register new produce", new Color(39, 142, 70),
                () -> openFrame(new ProduceFrame())));

        actions.add(actionCard("!", "Run Risk Analysis",
                "Check spoilage risk", new Color(226, 91, 39),
                () -> openFrame(new RiskAnalysisFrame())));

        actions.add(actionCard("◎", "ML Prediction",
                "Predict shelf life", new Color(113, 76, 198),
                () -> openFrame(new MLPredictionFrame())));

        actions.add(actionCard("⌂", "Manage Storage",
                "View storage details", new Color(20, 117, 190),
                () -> openFrame(new StorageFrame())));

        actions.add(actionCard("⇄", "Smart Allocation",
                "Match with buyers", new Color(219, 43, 91),
                () -> openFrame(new AllocationFrame())));

        wrapper.add(heading, BorderLayout.NORTH);
        wrapper.add(actions, BorderLayout.CENTER);

        return wrapper;
    }

    private JButton actionCard(
            String icon, String title, String desc,
            Color accent, Runnable action) {

        JButton b = new JButton();
        b.setLayout(new BorderLayout(8, 0));
        b.setBackground(WHITE);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(235, 240, 236)),
                new EmptyBorder(8, 8, 8, 8)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel badge = new JLabel(icon, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(new Color(
                accent.getRed(), accent.getGreen(),
                accent.getBlue(), 25
        ));
        badge.setForeground(accent);
        badge.setFont(new Font("Segoe UI Symbol", Font.BOLD, 19));
        badge.setPreferredSize(new Dimension(40, 40));

        JPanel words = new JPanel();
        words.setOpaque(false);
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(TEXT);

        JLabel d = new JLabel(desc);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        d.setForeground(MUTED);

        words.add(t);
        words.add(Box.createVerticalStrut(4));
        words.add(d);

        b.add(badge, BorderLayout.WEST);
        b.add(words, BorderLayout.CENTER);
        b.addActionListener(e -> action.run());

        return b;
    }

    // INVENTORY CHART

    private class InventoryChart extends JPanel {
        InventoryChart() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();
            int left = 35;
            int bottom = h - 25;
            int chartH = h - 55;
            int chartW = w - 48;

            g.setFont(new Font("Segoe UI", Font.PLAIN, 9));

            for (int i = 0; i <= 4; i++) {
                int y = bottom - chartH * i / 4;
                g.setColor(new Color(232, 238, 233));
                g.drawLine(left, y, left + chartW, y);
                g.setColor(MUTED);
                g.drawString(String.valueOf(i * 100), 2, y + 3);
            }

            String[] names = {
                    "Tomato", "Potato", "Orange",
                    "Banana", "Pineapple", "Apple", "Mango"
            };

            int[] vals = {320, 215, 275, 155, 192, 137, 120};
            int gap = 8;
            int bw = Math.max(
                    8, (chartW - gap * names.length) / names.length
            );

            for (int i = 0; i < vals.length; i++) {
                int x = left + gap / 2 + i * (bw + gap);
                int bh = (int) (chartH * vals[i] / 400.0);

                g.setPaint(new GradientPaint(
                        x, bottom - bh,
                        new Color(111, 205, 99),
                        x, bottom,
                        new Color(24, 116, 57)
                ));

                g.fillRoundRect(x, bottom - bh, bw, bh, 5, 5);
                g.setColor(MUTED);

                String nm = names[i].length() > 7
                        ? names[i].substring(0, 7) : names[i];

                g.drawString(nm, x - 3, bottom + 14);
            }

            g.dispose();
        }
    }

    // STORAGE CHART

    private class StorageChart extends JPanel {
        StorageChart() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = Math.min(
                    getHeight() - 12, getWidth() - 30
            );
            size = Math.max(90, Math.min(size, 155));

            int x = 12;
            int y = (getHeight() - size) / 2;

            g.setStroke(new BasicStroke(
                    17, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND
            ));

            g.setColor(new Color(220, 239, 222));
            g.drawArc(x, y, size, size, 0, 360);

            g.setColor(GREEN);
            g.drawArc(x, y, size, size, 90, -252);

            g.setColor(new Color(163, 231, 163));
            g.drawArc(x, y, size, size, -162, -108);

            g.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g.setColor(new Color(20, 92, 53));

            String s = storageValue == null
                    ? "0%" : storageValue.getText();

            FontMetrics fm = g.getFontMetrics();
            g.drawString(s,
                    x + (size - fm.stringWidth(s)) / 2,
                    y + size / 2 + 8);

            g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g.setColor(MUTED);

            String used = "Used";
            g.drawString(used,
                    x + (size - g.getFontMetrics().stringWidth(used)) / 2,
                    y + size / 2 + 27);

            int tx = x + size + 18;
            int ty = y + 28;

            legend(g, tx, ty, GREEN,
                    "Used capacity", "From storage records");

            legend(g, tx, ty + 52,
                    new Color(163, 231, 163),
                    "Remaining", "Available space");

            g.dispose();
        }
    }

    private void legend(
            Graphics2D g, int x, int y, Color c,
            String a, String b) {

        g.setColor(c);
        g.fillOval(x, y - 9, 13, 13);

        g.setColor(TEXT);
        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.drawString(a, x + 21, y + 1);

        g.setColor(MUTED);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g.drawString(b, x + 21, y + 17);
    }

    // SCROLLABLE DASHBOARD CONTENT

    private static class ScrollablePanel extends JPanel
            implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(
                Rectangle visibleRect, int orientation, int direction) {
            return 25;
        }

        @Override
        public int getScrollableBlockIncrement(
                Rectangle visibleRect, int orientation, int direction) {
            return Math.max(50, visibleRect.height - 50);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    // LEAF LOGO

    private static class LeafLogo extends JPanel {
        LeafLogo() {
            setOpaque(false);
            setPreferredSize(new Dimension(52, 58));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Path2D leaf = new Path2D.Double();
            leaf.moveTo(9, 49);
            leaf.curveTo(2, 27, 13, 9, 42, 2);
            leaf.curveTo(43, 25, 31, 43, 9, 49);
            leaf.closePath();

            g.setColor(LIME);
            g.fill(leaf);

            g.setColor(new Color(5, 57, 35));
            g.setStroke(new BasicStroke(2f));
            g.draw(new Line2D.Double(9, 49, 31, 16));

            g.dispose();
        }
    }

    // BANNER IMAGE

    private BufferedImage loadBanner() {
        try {
            File file = new File("assets/agriguard_banner.png");

            if (file.exists()) {
                return javax.imageio.ImageIO.read(file);
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    // REFRESH DATABASE VALUES

    private void refreshValues() {
        try {
            totalValue.setText(String.format(
                    "%,.0f kg", dao.getTotalProduce()
            ));

            riskValue.setText(String.valueOf(
                    dao.getHighRiskCount()
            ));

            buyersValue.setText(String.valueOf(
                    dao.getActiveBuyers()
            ));

            storageValue.setText(String.format(
                    "%.0f%%", dao.getStorageUtilization()
            ));

        } catch (Exception ex) {
            totalValue.setText("—");
            riskValue.setText("—");
            buyersValue.setText("—");
            storageValue.setText("—");
        }
    }

    private void updateClock() {
        if (dateTimeLabel != null) {
            dateTimeLabel.setText(
                    "<html><b>" +
                    new SimpleDateFormat("EEE, dd MMM yyyy")
                            .format(new Date()) +
                    "</b><br>" +
                    new SimpleDateFormat("hh:mm a")
                            .format(new Date()) +
                    "</html>"
            );
        }
    }

    private void refreshDashboard() {
        refreshValues();
        updateClock();
        revalidate();
        repaint();
    }

    // KEEP DASHBOARD OPEN WHEN OPENING OTHER WINDOWS

    private void openFrame(JFrame frame) {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String safe(String value, String fallback) {
        return value == null || value.trim().isEmpty()
                ? fallback : value;
    }

    private String clip(String s, int n) {
        if (s == null) {
            return "";
        }

        String clean = s.replace('\n', ' ').trim();

        return clean.length() <= n
                ? clean : clean.substring(0, n - 1) + "…";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            new DashboardFrame().setVisible(true);
        });
    }
}